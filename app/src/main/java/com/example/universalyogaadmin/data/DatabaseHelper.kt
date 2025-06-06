package com.example.universalyogaadmin

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.universalyogaadmin.model.YogaClass
import com.example.universalyogaadmin.model.ClassInstance
import java.text.SimpleDateFormat
import java.util.*

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "yoga_classes.db"
        private const val DATABASE_VERSION = 2
        const val TABLE_CLASSES = "classes"
        const val TABLE_INSTANCES = "instances"
        const val COLUMN_INSTANCE_ID = "_id"
        const val COLUMN_CLASS_ID = "class_id"
        const val COLUMN_DATE = "date"
        const val COLUMN_TEACHER = "teacher"
        const val COLUMN_COMMENTS = "comments"
        const val COLUMN_ID = "_id"
        const val COLUMN_DAY = "day"
        const val COLUMN_TIME = "time"
        const val COLUMN_CAPACITY = "capacity"
        const val COLUMN_DURATION = "duration"
        const val COLUMN_PRICE = "price"
        const val COLUMN_TYPE = "type"
        const val COLUMN_DESCRIPTION = "description"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_CLASSES (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_DAY TEXT," +
                "$COLUMN_TIME TEXT," +
                "$COLUMN_CAPACITY INTEGER," +
                "$COLUMN_DURATION TEXT," +
                "$COLUMN_PRICE REAL," +
                "$COLUMN_TYPE TEXT," +
                "$COLUMN_DESCRIPTION TEXT)")
        val createInstanceTable = ("CREATE TABLE $TABLE_INSTANCES (" +
                "$COLUMN_INSTANCE_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_CLASS_ID INTEGER," +
                "$COLUMN_DATE TEXT," +
                "$COLUMN_TEACHER TEXT," +
                "$COLUMN_COMMENTS TEXT)")
        db.execSQL(createTable)
        db.execSQL(createInstanceTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            val createInstanceTable = ("CREATE TABLE $TABLE_INSTANCES (" +
                    "$COLUMN_INSTANCE_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_CLASS_ID INTEGER," +
                    "$COLUMN_DATE TEXT," +
                    "$COLUMN_TEACHER TEXT," +
                    "$COLUMN_COMMENTS TEXT)")
            db.execSQL(createInstanceTable)
        }
    }

    fun addClass(yogaClass: YogaClass) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_DAY, yogaClass.day)
        values.put(COLUMN_TIME, yogaClass.time)
        values.put(COLUMN_CAPACITY, yogaClass.capacity)
        values.put(COLUMN_DURATION, yogaClass.duration)
        values.put(COLUMN_PRICE, yogaClass.price)
        values.put(COLUMN_TYPE, yogaClass.type)
        values.put(COLUMN_DESCRIPTION, yogaClass.description)
        db.insert(TABLE_CLASSES, null, values)
        db.close()
    }

    fun addInstance(classId: Int, date: String, teacher: String, comments: String?) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.isLenient = false
        try {
            dateFormat.parse(date) // Kiểm tra định dạng ngày
            val db = writableDatabase
            val values = ContentValues()
            values.put(COLUMN_CLASS_ID, classId)
            values.put(COLUMN_DATE, date)
            values.put(COLUMN_TEACHER, teacher)
            values.put(COLUMN_COMMENTS, comments)
            db.insert(TABLE_INSTANCES, null, values)
            db.close()
        } catch (e: Exception) {
            throw IllegalArgumentException("Date must be in dd/MM/yyyy format")
        }
    }

    fun updateClass(yogaClass: YogaClass) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DAY, yogaClass.day)
            put(COLUMN_TIME, yogaClass.time)
            put(COLUMN_CAPACITY, yogaClass.capacity)
            put(COLUMN_DURATION, yogaClass.duration)
            put(COLUMN_PRICE, yogaClass.price)
            put(COLUMN_TYPE, yogaClass.type)
            put(COLUMN_DESCRIPTION, yogaClass.description)
        }
        db.update(TABLE_CLASSES, values, "$COLUMN_ID = ?", arrayOf(yogaClass.id.toString()))
        db.close()
    }

    fun getAllClasses(): List<YogaClass> {
        val classes = mutableListOf<YogaClass>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_CLASSES", null)
        if (cursor.moveToFirst()) {
            do {
                val yogaClass = YogaClass(
                    id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    day = cursor.getString(cursor.getColumnIndex(COLUMN_DAY)),
                    time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME)),
                    capacity = cursor.getString(cursor.getColumnIndex(COLUMN_CAPACITY)),
                    duration = cursor.getString(cursor.getColumnIndex(COLUMN_DURATION)),
                    price = cursor.getString(cursor.getColumnIndex(COLUMN_PRICE)),
                    type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)),
                    description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
                )
                classes.add(yogaClass)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return classes
    }

    fun getAllInstances(): List<ClassInstance> {
        val instances = mutableListOf<ClassInstance>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_INSTANCES", null)
        if (cursor.moveToFirst()) {
            do {
                val instance = ClassInstance(
                    id = cursor.getInt(cursor.getColumnIndex(COLUMN_INSTANCE_ID)),
                    classId = cursor.getInt(cursor.getColumnIndex(COLUMN_CLASS_ID)),
                    date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE)),
                    teacher = cursor.getString(cursor.getColumnIndex(COLUMN_TEACHER)),
                    comments = cursor.getString(cursor.getColumnIndex(COLUMN_COMMENTS))
                )
                instances.add(instance)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return instances
    }

    fun getInstancesForClass(classId: Int): List<ClassInstance> {
        val instances = mutableListOf<ClassInstance>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM $TABLE_INSTANCES WHERE $COLUMN_CLASS_ID = ?",
            arrayOf(classId.toString())
        )
        if (cursor.moveToFirst()) {
            do {
                val instance = ClassInstance(
                    id = cursor.getInt(cursor.getColumnIndex(COLUMN_INSTANCE_ID)),
                    classId = cursor.getInt(cursor.getColumnIndex(COLUMN_CLASS_ID)),
                    date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE)),
                    teacher = cursor.getString(cursor.getColumnIndex(COLUMN_TEACHER)),
                    comments = cursor.getString(cursor.getColumnIndex(COLUMN_COMMENTS))
                )
                instances.add(instance)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return instances
    }

    fun deleteClass(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_CLASSES, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun updateInstance(instance: ClassInstance) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CLASS_ID, instance.classId)
            put(COLUMN_DATE, instance.date)
            put(COLUMN_TEACHER, instance.teacher)
            put(COLUMN_COMMENTS, instance.comments)
        }
        db.update(TABLE_INSTANCES, values, "$COLUMN_INSTANCE_ID = ?", arrayOf(instance.id.toString()))
        db.close()
    }

    fun deleteInstance(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_INSTANCES, "$COLUMN_INSTANCE_ID = ?", arrayOf(id.toString()))
        db.close()
    }
}