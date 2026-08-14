package com.devil.app.internet

import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.URI
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import javax.net.ssl.HttpsURLConnection

/**
 * Default Stage 42 Android Internet Knowledge source.
 *
 * This is the first genuine bounded network-retrieval embodiment for Devil.
 *
 * It performs one explicit HTTPS GET for one already-validated
 * AndroidInternetKnowledgeRequest.
 *
 * The source deliberately does not provide:
 *
 * - arbitrary browsing;
 * - HTTP retrieval;
 * - automatic redirects;
 * - uploads;
 * - POST requests;
 * - cookies or authenticated sessions;
 * - account access;
 * - file downloads;
 * - unrestricted response sizes;
 * - binary-content ingestion;
 * - JavaScript execution;
 * - embedded browser execution;
 * - source trust;
 * - factual verification;
 * - ConversationInput creation;
 * - memory persistence;
 * - Devil authorization;
 * - capability execution;
 * - or verified Outcome creation.
 *
 * External content remains untrusted data.
 *
 * Network success
 * != source authenticity
 * != factual truth
 * != trusted instruction
 * != user intent
 * != authorization
 * != memory
 * != execution.
 */
class DefaultAndroidInternetKnowledgeSource(
    private val connectTimeoutMilliseconds: Int =
        DEFAULT_CONNECT_TIMEOUT_MILLISECONDS,
    private val readTimeoutMilliseconds: Int =
        DEFAULT_READ_TIMEOUT_MILLISECONDS,
    private val retrievalTimeProvider:
        AndroidInternetRetrievalTimeProvider =
        DefaultAndroidInternetRetrievalTimeProvider(),
    private val maximumResponseBytes: Int =
        DEFAULT_MAXIMUM_RESPONSE_BYTES,
) : AndroidInternetKnowledgeSource {

    init {
        require(connectTimeoutMilliseconds > 0) {
            "Internet connection timeout must be positive."
        }

        require(readTimeoutMilliseconds > 0) {
            "Internet read timeout must be positive."
        }

        require(maximumResponseBytes > 0) {
            "Internet response byte limit must be positive."
        }
    }

    override fun retrieve(
        request: AndroidInternetKnowledgeRequest,
    ): AndroidInternetKnowledgeResult {
        return runCatching {
            retrieveBounded(
                request = request,
            )
        }.getOrElse { throwable ->
            AndroidInternetKnowledgeResult.failed(
                error =
                    throwable.message
                        ?.trim()
                        ?.takeIf {
                            it.isNotEmpty()
                        }
                        ?: "Bounded HTTPS retrieval failed.",
            )
        }
    }

    private fun retrieveBounded(
        request: AndroidInternetKnowledgeRequest,
    ): AndroidInternetKnowledgeResult {
        val uri =
            request.uri

        requireAllowedDestination(
            uri = uri,
        )

        val connection =
            uri
                .toURL()
                .openConnection() as? HttpsURLConnection
                ?: return AndroidInternetKnowledgeResult.failed(
                    error =
                        "Stage 42 requires an HTTPS connection.",
                )

        try {
            configure(
                connection = connection,
            )

            val responseCode =
                connection.responseCode

            if (
                responseCode in
                HTTP_REDIRECT_START..HTTP_REDIRECT_END
            ) {
                return AndroidInternetKnowledgeResult.failed(
                    error =
                        "Stage 42 does not automatically follow Internet redirects.",
                )
            }

            if (
                responseCode !in
                HTTP_SUCCESS_START..HTTP_SUCCESS_END
            ) {
                return AndroidInternetKnowledgeResult.unavailable()
            }

            val mediaType =
                connection.contentType
                    ?.trim()
                    ?.takeIf {
                        it.isNotEmpty()
                    }

            if (!isApprovedTextualMediaType(mediaType)) {
                return AndroidInternetKnowledgeResult.unavailable()
            }

            val declaredLength =
                connection.contentLengthLong

            if (
                declaredLength >
                maximumResponseBytes.toLong()
            ) {
                return AndroidInternetKnowledgeResult.failed(
                    error =
                        "Internet knowledge response exceeded the Stage 42 byte limit.",
                )
            }

            val responseBytes =
                connection.inputStream.use { input ->
                    readBounded(
                        input = input,
                    )
                }

            if (responseBytes.isEmpty()) {
                return AndroidInternetKnowledgeResult.unavailable()
            }

            val charset =
                resolveCharset(
                    contentType = mediaType,
                )

            val content =
                responseBytes
                    .toString(charset)
                    .trim()

            if (content.isEmpty()) {
                return AndroidInternetKnowledgeResult.unavailable()
            }

            val finalUri =
                connection.url
                    ?.toURI()
                    ?: uri

            if (!sameOrigin(uri, finalUri)) {
                return AndroidInternetKnowledgeResult.failed(
                    error =
                        "Internet response provenance changed origin.",
                )
            }

            val document =
                AndroidInternetKnowledgeDocument.create(
                    sourceUri = finalUri,
                    retrievedAt =
                        retrievalTimeProvider.observedAt(),
                    mediaType = mediaType,
                    content = content,
                )

            return AndroidInternetKnowledgeResult.available(
                document = document,
            )
        } finally {
            connection.disconnect()
        }
    }

    private fun configure(
        connection: HttpsURLConnection,
    ) {
        connection.instanceFollowRedirects = false
        connection.connectTimeout =
            connectTimeoutMilliseconds
        connection.readTimeout =
            readTimeoutMilliseconds
        connection.requestMethod =
            HTTP_GET
        connection.doInput = true
        connection.doOutput = false
        connection.useCaches = false

        connection.setRequestProperty(
            "Accept",
            APPROVED_ACCEPT_HEADER,
        )

        connection.setRequestProperty(
            "Accept-Encoding",
            "identity",
        )

        connection.setRequestProperty(
            "User-Agent",
            DEVIL_STAGE_42_USER_AGENT,
        )
    }

    private fun requireAllowedDestination(
        uri: URI,
    ) {
        require(
            uri.scheme.equals(
                "https",
                ignoreCase = true,
            ),
        ) {
            "Stage 42 Internet retrieval requires HTTPS."
        }

        val host =
            uri.host
                ?.trim()
                ?.takeIf {
                    it.isNotEmpty()
                }
                ?: throw IllegalArgumentException(
                    "Stage 42 Internet retrieval requires an explicit host.",
                )

        require(!isExplicitlyForbiddenHost(host)) {
            "Stage 42 Internet retrieval rejected a local or reserved destination."
        }

        val resolvedAddresses =
            InetAddress.getAllByName(host)

        require(resolvedAddresses.isNotEmpty()) {
            "Stage 42 Internet destination could not be resolved."
        }

        require(
            resolvedAddresses.none {
                isForbiddenAddress(it)
            },
        ) {
            "Stage 42 Internet retrieval rejected a local or reserved network destination."
        }
    }

    private fun isExplicitlyForbiddenHost(
        host: String,
    ): Boolean {
        val normalized =
            host
                .trim()
                .lowercase()

        return normalized == "localhost" ||
            normalized.endsWith(".localhost") ||
            normalized == "localhost.localdomain"
    }

    private fun isForbiddenAddress(
        address: InetAddress,
    ): Boolean {
        return address.isAnyLocalAddress ||
            address.isLoopbackAddress ||
            address.isLinkLocalAddress ||
            address.isSiteLocalAddress ||
            address.isMulticastAddress
    }

    private fun readBounded(
        input: java.io.InputStream,
    ): ByteArray {
        val output =
            ByteArrayOutputStream(
                minOf(
                    maximumResponseBytes,
                    DEFAULT_BUFFER_CAPACITY,
                ),
            )

        val buffer =
            ByteArray(
                READ_BUFFER_SIZE,
            )

        var totalBytes = 0

        while (true) {
            val read =
                input.read(buffer)

            if (read < 0) {
                break
            }

            if (read == 0) {
                continue
            }

            totalBytes += read

            require(totalBytes <= maximumResponseBytes) {
                "Internet knowledge response exceeded the Stage 42 byte limit."
            }

            output.write(
                buffer,
                0,
                read,
            )
        }

        return output.toByteArray()
    }

    private fun isApprovedTextualMediaType(
        contentType: String?,
    ): Boolean {
        val mediaType =
            contentType
                ?.substringBefore(';')
                ?.trim()
                ?.lowercase()
                ?: return false

        return mediaType.startsWith("text/") ||
            mediaType == "application/json" ||
            mediaType == "application/ld+json" ||
            mediaType == "application/xml" ||
            mediaType == "application/xhtml+xml"
    }

    private fun resolveCharset(
        contentType: String?,
    ): Charset {
        val charsetName =
            contentType
                ?.split(';')
                ?.drop(1)
                ?.map {
                    it.trim()
                }
                ?.firstOrNull {
                    it.startsWith(
                        "charset=",
                        ignoreCase = true,
                    )
                }
                ?.substringAfter('=')
                ?.trim()
                ?.trim('"', '\'')

        if (charsetName.isNullOrBlank()) {
            return StandardCharsets.UTF_8
        }

        return runCatching {
            Charset.forName(charsetName)
        }.getOrDefault(
            StandardCharsets.UTF_8,
        )
    }

    private fun sameOrigin(
        requestedUri: URI,
        finalUri: URI,
    ): Boolean {
        return requestedUri.scheme.equals(
            finalUri.scheme,
            ignoreCase = true,
        ) &&
            requestedUri.host.equals(
                finalUri.host,
                ignoreCase = true,
            ) &&
            effectivePort(requestedUri) ==
            effectivePort(finalUri)
    }

    private fun effectivePort(
        uri: URI,
    ): Int {
        return if (uri.port >= 0) {
            uri.port
        } else {
            HTTPS_DEFAULT_PORT
        }
    }

    private companion object {
        const val DEFAULT_CONNECT_TIMEOUT_MILLISECONDS:
            Int = 5_000

        const val DEFAULT_READ_TIMEOUT_MILLISECONDS:
            Int = 8_000

        const val DEFAULT_MAXIMUM_RESPONSE_BYTES:
            Int = 512 * 1024

        const val DEFAULT_BUFFER_CAPACITY:
            Int = 8 * 1024

        const val READ_BUFFER_SIZE:
            Int = 8 * 1024

        const val HTTP_SUCCESS_START:
            Int = HttpURLConnection.HTTP_OK

        const val HTTP_SUCCESS_END:
            Int = 299

        const val HTTP_REDIRECT_START:
            Int = 300

        const val HTTP_REDIRECT_END:
            Int = 399

        const val HTTPS_DEFAULT_PORT:
            Int = 443

        const val HTTP_GET:
            String = "GET"

        const val APPROVED_ACCEPT_HEADER:
            String =
            "text/plain, text/html, application/json, application/ld+json, application/xml, application/xhtml+xml"

        const val DEVIL_STAGE_42_USER_AGENT:
            String =
            "Devil-Internet-Knowledge/Stage-42"
    }
}
