package com.example.universalyogaadmin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.universalyogaadmin.DatabaseHelper

class AddInstanceActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(this)

        setContentView(R.layout.activity_add_instance)

        val classId = intent.getIntExtra("classId", 0)

        // UI components for date, teacher, comments
        val dateEditText: EditText = findViewById(R.id.date_edit_text)
        val teacherEditText: EditText = findViewById(R.id.teacher_edit_text)
        val commentsEditText: EditText = findViewById(R.id.comments_edit_text)

        val addButton: Button = findViewById(R.id.add_button)
        addButton.setOnClickListener {
            val date = dateEditText.text.toString()
            val teacher = teacherEditText.text.toString()
            val comments = commentsEditText.text.toString()

            if (date.isNotEmpty() && teacher.isNotEmpty()) {
                dbHelper.addInstance(classId, date, teacher, comments)
                finish()
            } else {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

