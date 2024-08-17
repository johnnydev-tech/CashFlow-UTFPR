package tech.johnnydev.cashflow.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import tech.johnnydev.cashflow.entity.Balance
import tech.johnnydev.cashflow.entity.Transaction
import tech.johnnydev.cashflow.entity.TransactionType

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                _id INTEGER PRIMARY KEY AUTOINCREMENT,
                type TEXT,
                detail TEXT,
                value REAL,
                date TEXT
            )
        """.trimIndent()
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insert(transaction: Transaction) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(TYPE, transaction.type.name)
            put(DETAIL, transaction.detail)
            put(VALUE, transaction.value)
            put(DATE, transaction.date)
        }
        db.insert(TABLE_NAME, null, values)
    }

    fun list(): MutableList<Transaction> {
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        val transactions = mutableListOf<Transaction>()

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val transaction = Transaction(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                    type = TransactionType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(TYPE))),
                    detail = cursor.getString(cursor.getColumnIndexOrThrow(DETAIL)),
                    value = cursor.getDouble(cursor.getColumnIndexOrThrow(VALUE)),
                    date = cursor.getString(cursor.getColumnIndexOrThrow(DATE))
                )
                transactions.add(transaction)
            }
        }
        cursor.close()
        return transactions
    }


    fun getBalance(): Balance {
        val db = this.readableDatabase


        val creditCursor = db.rawQuery("SELECT SUM($VALUE) FROM $TABLE_NAME WHERE $TYPE = ?", arrayOf(TransactionType.CREDIT.name))
        val debitCursor = db.rawQuery("SELECT SUM($VALUE) FROM $TABLE_NAME WHERE $TYPE = ?", arrayOf(TransactionType.DEBIT.name))


        val creditSum = if (creditCursor.moveToFirst()) creditCursor.getDouble(0) else 0.0
        val debitSum = if (debitCursor.moveToFirst()) debitCursor.getDouble(0) else 0.0

        creditCursor.close()
        debitCursor.close()

        return Balance(credit = creditSum, debit = debitSum)
    }

    companion object {
        private const val DATABASE_NAME = "transactions.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "transactions"

        private const val TYPE = "type"
        private const val DETAIL = "detail"
        private const val VALUE = "value"
        private const val DATE = "date"
        private const val ID = "_id"
    }
}