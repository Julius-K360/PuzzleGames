package com.malkinfo.puzzlegames
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    companion object {
        val DATABASE_NAME = "LeaderboardDB.db"
        val TABLE_NAME = "SpielerListe"
        val ID = "ID"
        val NAME = "NAME"
        val HOECHSTERPUNKTESTAND = "HOECHSTERPUNKTESTAND"
            }


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table $TABLE_NAME (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,HOECHSTERPUNKTESTAND INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    /**
     * Daten einfügen
     */
    fun insertData(name: String?, punkte: Int?) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NAME, name)
        contentValues.put(HOECHSTERPUNKTESTAND, punkte)
        db.insert(TABLE_NAME, null, contentValues)
    }

    /**
     * Gebe Liste der User
     */
    fun listOfUserInfo(): ArrayList<UserInfo>  {
        val db = this.writableDatabase
        val res = db.rawQuery("select * from " + TABLE_NAME, null)
        val useList = ArrayList<UserInfo>()
        while (res.moveToNext()) {
            var userInfo = UserInfo()
            userInfo.id = Integer.valueOf(res.getString(0))
            userInfo.name = res.getString(1)
            userInfo.punkte = Integer.valueOf(res.getString(2))
            useList.add(userInfo)
        }
        return useList
    }

    //Gebe alle User Daten
    fun getAllUserData(): ArrayList<UserInfo> {
        val stuList: ArrayList<UserInfo> = arrayListOf<UserInfo>()
        val cursor: Cursor = getReadableDatabase().query(TABLE_NAME, arrayOf(ID, NAME, HOECHSTERPUNKTESTAND), null, null, null, null, null)
        try {
            if (cursor.getCount() != 0) {
                cursor.moveToFirst()
                if (cursor.getCount() > 0) {
                    do {
                        val id : Int = cursor.getInt(cursor.getColumnIndex(ID))
                        val name: String = cursor.getString(cursor.getColumnIndex(NAME))
                        val punkte: Int = cursor.getInt(cursor.getColumnIndex(HOECHSTERPUNKTESTAND))
                        var userInfo = UserInfo()
                        userInfo.id = id
                        userInfo.name = name
                        userInfo.punkte = punkte
                        stuList.add(userInfo)
                    } while ((cursor.moveToNext()))
                }
            }
        } finally {
            cursor.close()
        }

        return stuList
    }
    /**
     * Hole bestimmte Userdaten
     */
    fun getParticularUserData(id: String): UserInfo {
        var userInfo  = UserInfo()
        val db = this.readableDatabase
        val selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + ID + " = '" + id + "'"
        val cursor = db.rawQuery(selectQuery, null)
        try {
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    do {
                        userInfo.id = cursor.getInt(cursor.getColumnIndex(ID))
                        userInfo.name = cursor.getString(cursor.getColumnIndex(NAME))
                        userInfo.punkte = cursor.getInt(cursor.getColumnIndex(HOECHSTERPUNKTESTAND))
                    } while ((cursor.moveToNext()));
                }
            }
        } finally {
            cursor.close();
        }
        return userInfo
    }

    /**
     * update der userdata
     */
    fun updateData(id: String, name: String, punkte: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, id)
        contentValues.put(NAME, name)
        contentValues.put(HOECHSTERPUNKTESTAND, punkte)
        db.update(TABLE_NAME, contentValues, "ID = ?", arrayOf(id))
        return true
    }

    /**
     * Lösche die Daten eines Users
     */
    fun deleteData(id : String) : Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME,"ID = ?", arrayOf(id))

    }


}