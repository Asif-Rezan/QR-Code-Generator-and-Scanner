package com.asifrezan.qrcodegenerator.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.asifrezan.qrcodegenerator.data.MyData

class DBHelper(context: Context) : SQLiteOpenHelper(context,DATABASE_NAME, null, DATABASE_VERSION){
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "qrCodeText"

        // Table Name and Column Names
        private const val TABLE_NAME = "mytable"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TITLE TEXT, $COLUMN_NAME TEXT, $COLUMN_DATE TEXT)"
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(DROP_TABLE)
        onCreate(db)

    }

    fun addData(title:String, name: String, date: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_TITLE,title)
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_DATE, date)
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result
    }



    fun getData(): List<MyData> {
        val data = ArrayList<MyData>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_ID DESC"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst() && cursor.count > 0) {
            val idIndex = cursor.getColumnIndex(COLUMN_ID)
            val nameIndex = cursor.getColumnIndex(COLUMN_NAME)
            val titleIndex = cursor.getColumnIndex(COLUMN_TITLE)
            val dateIndex = cursor.getColumnIndex(COLUMN_DATE)
            do {
                val id = cursor.getLong(idIndex)
                val title = cursor.getString(titleIndex)
                val name = cursor.getString(nameIndex)
                val date = cursor.getString(dateIndex)
                val myData = MyData(id, name, title, date)
                data.add(myData)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return data
    }

    fun getDataById(id: Long): MyData? {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(id.toString()))

        var myData: MyData? = null
        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(COLUMN_ID)
            val nameIndex = cursor.getColumnIndex(COLUMN_NAME)
            val titleIndex = cursor.getColumnIndex(COLUMN_TITLE)
            val dateIndex = cursor.getColumnIndex(COLUMN_DATE)

            if (idIndex >= 0 && nameIndex >= 0 && titleIndex >=0 && dateIndex >= 0) {
                val myId = cursor.getLong(idIndex)
                val name = cursor.getString(nameIndex)
                val title = cursor.getString(titleIndex)
                val date = cursor.getString(dateIndex)
                myData = MyData(myId, name, title, date)
            }
        }

        cursor.close()
        db.close()
        return myData
    }




    fun updateData(id: Long, name: String, date: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_DATE, date)
        val result = db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return result != -1
    }

    fun deleteData(id: Long): Boolean {
        val db = this.writableDatabase
        val success = db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return success > 0
    }
}