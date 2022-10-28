package com.github.kr328.clash.service.data.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // TODO: Use auto migration if we have `exportSchema = true`
        database.execSQL("CREATE TABLE `subscription_user_info` (`uuid` TEXT NOT NULL, `upload` INTEGER NOT NULL, `download` INTEGER NOT NULL, `total` INTEGER NOT NULL, `expire` INTEGER NOT NULL, PRIMARY KEY(`uuid`), FOREIGN KEY(`uuid`) REFERENCES `imported`(`uuid`) ON UPDATE CASCADE ON DELETE CASCADE )")
    }
}

val MIGRATIONS: Array<Migration> = arrayOf(MIGRATION_1_2)

val LEGACY_MIGRATION = ::migrationFromLegacy