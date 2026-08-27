package com.devil.app.modelprovider.conversation

import com.devil.core.runtime.modelprovider.conversation.ConversationalModelInferenceRequest
import java.io.ByteArrayOutputStream
import java.net.InetAddress
import java.net.URI
import java.nio.charset.StandardCharsets
import javax.net.ssl.HttpsURLConnection

/**
 * Stage 313 bounded real HTTPS conversational-model transport.
 *
 * This transport performs one explicit HTTPS POST for one already-established
 * provider-neutral ConversationalModelInferenceRequest and one explicitly
 * resolved Android ConversationalModelConfiguration.
 *
 * Transport contract:
 *
 * - HTTPS only;
 * - one POST only;
 * - redirects disabled;
 * - UTF-8 text request body;
 * - explicit model identifier header;
 * - bearer credential supplied only through the HTTPS Authorization header;
 * - bounded UTF-8 textual response;
 * - no cookies;
 * - no authenticated browser session;
 * - no JavaScript;
 * - no arbitrary file download;
 * - no automatic redirect following;
 * - no unrestricted response size.
 *
 * Destination validation rejects localhost, loopback, link-local, site-local,
 * and unspecified-address destinations before transport.
 *
 * This class does not:
 *
 * - establish identity, trust, authentication, or Devil authorization;
 * - perform Conversation Intake Authority;
 * - choose constitutional classifications;
 * - select a model provider;
 * - create credentials;
 * - execute Devil capabilities;
 * - establish constitutional Observation, Verification, or Outcome;
 * - perform Learning;
 * - create, commit, persist, or recall Memory;
 * - or treat network/provider success as verified truth.
 *
 * HTTPS_SUCCESS != PROVIDER_TRUST.
 * HTTPS_SUCCESS != FACTUAL_TRUTH.
 * HTTPS_SUCCESS != CONSTITUTIONAL_VERIFICATION.
 * HTTPS_SUCCESS != VERIFIED_OUTCOME.
 * GENERATED != VERIFIED.
 * MODEL != DEVIL.
 * MODEL != BRAIN.
 * MODEL != AUTHORITY.
 */
class DefaultHttpsConversationalModelTransport(
    private val connectTimeoutMilliseconds: Int =
        DEFAULT_CONNECT_TIMEOUT_MILLISECONDS,
    private val readTimeoutMilliseconds: Int =
        DEFAULT_READ_TIMEOUT_MILLISECONDS,
    private val maximumResponseBytes: Int =
        DEFAULT_MAXIMUM_RESPONSE_BYTES,
) : ConversationalModelTransport {

    init {
        require(connectTimeoutMilliseconds > 0) {
            "Conversational-model connection timeout must be positive."
        }

        require(readTimeoutMilliseconds > 0) {
            "Conversational-model read timeout must be positive."
        }

        require(maximumResponseBytes > 0) {
            "Conversational-model response byte limit must be positive."
        }
    }

    override fun invoke(
        request: ConversationalModelInferenceRequest,
        configuration: ConversationalModelConfiguration,
    ): ConversationalModelTransportResult {
        return runCatching {
            invokeBounded(
                request = request,
                configuration = configuration,
            )
        }.getOrElse {
            ConversationalModelTransportResult.unavailable(
                traceId = request.traceId,
            )
        }
    }

    private fun invokeBounded(
        request: ConversationalModelInferenceRequest,
        configuration: ConversationalModelConfiguration,
    ): ConversationalModelTransportResult {
        val endpoint =
            URI.create(
                configuration.endpoint.trim(),
            )

        requireAllowedDestination(
            uri = endpoint,
        )

        val connection =
            endpoint
                .toURL()
                .openConnection() as? HttpsURLConnection
                ?: return ConversationalModelTransportResult.unavailable(
                    traceId = request.traceId,
                )

        try {
            configure(
                connection = connection,
                configuration = configuration,
                request = request,
            )

            val requestBytes =
                request.content.toByteArray(
                    StandardCharsets.UTF_8,
                )

            connection.setFixedLengthStreamingMode(
                requestBytes.size,
            )

            connection.outputStream.use { output ->
                output.write(requestBytes)
                output.flush()
            }

            val responseCode =
                connection.responseCode

            if (
                responseCode in
                HTTP_REDIRECT_START..HTTP_REDIRECT_END
            ) {
                return ConversationalModelTransportResult.unavailable(
                    traceId = request.traceId,
                )
            }

            if (
                responseCode !in
                HTTP_SUCCESS_START..HTTP_SUCCESS_END
            ) {
                return ConversationalModelTransportResult.unavailable(
                    traceId = request.traceId,
                )
            }

            val mediaType =
                connection.contentType
                    ?.substringBefore(';')
                    ?.trim()
                    ?.lowercase()

            if (
                mediaType != null &&
                mediaType !in APPROVED_RESPONSE_MEDIA_TYPES
            ) {
                return ConversationalModelTransportResult.unavailable(
                    traceId = request.traceId,
                )
            }

            val declaredLength =
                connection.contentLengthLong

            if (
                declaredLength >
                maximumResponseBytes.toLong()
            ) {
                return ConversationalModelTransportResult.unavailable(
                    traceId = request.traceId,
                )
            }

            val responseBytes =
                connection.inputStream.use { input ->
                    val output =
                        ByteArrayOutputStream()

                    val buffer =
                        ByteArray(
                            READ_BUFFER_BYTES,
                        )

                    var totalBytes = 0

                    while (true) {
                        val read =
                            input.read(buffer)

                        if (read < 0) {
                            break
                        }

                        totalBytes += read

                        if (
                            totalBytes >
                            maximumResponseBytes
                        ) {
                            return ConversationalModelTransportResult.unavailable(
                                traceId = request.traceId,
                            )
                        }

                        output.write(
                            buffer,
                            0,
                            read,
                        )
                    }

                    output.toByteArray()
                }

            if (responseBytes.isEmpty()) {
                return ConversationalModelTransportResult.unavailable(
                    traceId = request.traceId,
                )
            }

            val generatedText =
                responseBytes
                    .toString(
                        StandardCharsets.UTF_8,
                    )
                    .trim()

            if (generatedText.isEmpty()) {
                return ConversationalModelTransportResult.unavailable(
                    traceId = request.traceId,
                )
            }

            return ConversationalModelTransportResult.generated(
                traceId = request.traceId,
                generatedText = generatedText,
            )
        } finally {
            connection.disconnect()
        }
    }

    private fun configure(
        connection: HttpsURLConnection,
        configuration: ConversationalModelConfiguration,
        request: ConversationalModelInferenceRequest,
    ) {
        connection.instanceFollowRedirects = false
        connection.connectTimeout =
            connectTimeoutMilliseconds
        connection.readTimeout =
            readTimeoutMilliseconds
        connection.requestMethod =
            HTTP_METHOD_POST
        connection.doInput = true
        connection.doOutput = true
        connection.useCaches = false

        connection.setRequestProperty(
            HEADER_ACCEPT,
            MEDIA_TYPE_TEXT_UTF8,
        )

        connection.setRequestProperty(
            HEADER_CONTENT_TYPE,
            MEDIA_TYPE_TEXT_UTF8,
        )

        connection.setRequestProperty(
            HEADER_AUTHORIZATION,
            "Bearer ${configuration.credential}",
        )

        connection.setRequestProperty(
            HEADER_MODEL_ID,
            configuration.modelId,
        )

        connection.setRequestProperty(
            HEADER_TRACE_ID,
            request.traceId.toString(),
        )
    }

    private fun requireAllowedDestination(
        uri: URI,
    ) {
        require(
            uri.scheme.equals(
                HTTPS_SCHEME,
                ignoreCase = true,
            ),
        ) {
            "Conversational-model transport requires HTTPS."
        }

        require(uri.userInfo == null) {
            "Conversational-model endpoint must not contain URI user-info credentials."
        }

        require(uri.fragment == null) {
            "Conversational-model endpoint must not contain a fragment."
        }

        val host =
            requireNotNull(
                uri.host,
            ) {
                "Conversational-model endpoint requires an explicit host."
            }

        require(
            !isExplicitlyForbiddenHost(
                host = host,
            ),
        ) {
            "Conversational-model endpoint host is forbidden."
        }

        val resolvedAddresses =
            InetAddress.getAllByName(
                host,
            )

        require(
            resolvedAddresses.isNotEmpty(),
        ) {
            "Conversational-model endpoint host could not be resolved."
        }

        require(
            resolvedAddresses.none {
                isForbiddenAddress(
                    address = it,
                )
            },
        ) {
            "Conversational-model endpoint resolved to a forbidden local address."
        }
    }

    private fun isExplicitlyForbiddenHost(
        host: String,
    ): Boolean {
        val normalized =
            host
                .trim()
                .trimEnd('.')
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
            address.isSiteLocalAddress
    }

    companion object {
        internal const val DEFAULT_CONNECT_TIMEOUT_MILLISECONDS:
            Int = 10_000

        internal const val DEFAULT_READ_TIMEOUT_MILLISECONDS:
            Int = 30_000

        internal const val DEFAULT_MAXIMUM_RESPONSE_BYTES:
            Int = 256 * 1024

        private const val READ_BUFFER_BYTES:
            Int = 8 * 1024

        private const val HTTPS_SCHEME =
            "https"

        private const val HTTP_METHOD_POST =
            "POST"

        private const val HEADER_ACCEPT =
            "Accept"

        private const val HEADER_CONTENT_TYPE =
            "Content-Type"

        private const val HEADER_AUTHORIZATION =
            "Authorization"

        private const val HEADER_MODEL_ID =
            "X-Devil-Model-Id"

        private const val HEADER_TRACE_ID =
            "X-Devil-Trace-Id"

        private const val MEDIA_TYPE_TEXT_UTF8 =
            "text/plain; charset=utf-8"

        private const val HTTP_SUCCESS_START =
            200

        private const val HTTP_SUCCESS_END =
            299

        private const val HTTP_REDIRECT_START =
            300

        private const val HTTP_REDIRECT_END =
            399

        private val APPROVED_RESPONSE_MEDIA_TYPES =
            setOf(
                "text/plain",
                "application/json",
            )
    }
}
