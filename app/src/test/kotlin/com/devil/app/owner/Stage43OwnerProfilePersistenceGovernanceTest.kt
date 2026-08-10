package com.devil.app.owner

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse

class Stage43OwnerProfilePersistenceGovernanceTest {

    @Test
    fun `Stage 43 owner model contains no hidden persistence mechanism`() {
        val ownerDirectory =
            File(
                "../core/model/src/main/kotlin/com/devil/core/model/owner",
            )

        val productionText =
            ownerDirectory
                .walkTopDown()
                .filter {
                    it.isFile &&
                        it.extension == "kt"
                }
                .joinToString(
                    separator = "\n",
                ) {
                    it.readText()
                }

        assertFalse(
            productionText.contains(
                "SharedPreferences",
            ),
        )

        assertFalse(
            productionText.contains(
                "RoomDatabase",
            ),
        )

        assertFalse(
            productionText.contains(
                "SQLiteDatabase",
            ),
        )

        assertFalse(
            productionText.contains(
                "FileOutputStream",
            ),
        )

        assertFalse(
            productionText.contains(
                "DefaultMemoryAuthority(",
            ),
        )

        assertFalse(
            productionText.contains(
                "MemoryPersistenceAuthority(",
            ),
        )
    }
}
