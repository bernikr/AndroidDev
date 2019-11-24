package com.kralofsky.cipherbox

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

object CipherBoxDbContract {
    // Table contents are grouped together in an anonymous object.
    object HistoryEntry : BaseColumns {
        const val TABLE_NAME = "entry"
        const val COLUMN_NAME_CLEARTEXT = "cleartext"
        const val COLUMN_NAME_CIPHERTEXT = "ciphertext"
        const val COLUMN_NAME_CIPHER = "cipher"
    }
}

private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE ${CipherBoxDbContract.HistoryEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${CipherBoxDbContract.HistoryEntry.COLUMN_NAME_CLEARTEXT} TEXT," +
            "${CipherBoxDbContract.HistoryEntry.COLUMN_NAME_CIPHERTEXT} TEXT," +
            "${CipherBoxDbContract.HistoryEntry.COLUMN_NAME_CIPHER} TEXT)"

private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${CipherBoxDbContract.HistoryEntry.TABLE_NAME}"

class CipherboxDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        throw NotImplementedError()
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        throw NotImplementedError()
    }
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "cipherbox.db"
    }
}