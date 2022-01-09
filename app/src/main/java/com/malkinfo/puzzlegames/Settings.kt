package com.malkinfo.puzzlegames

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text


class Settings : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        loadData()

        val button = findViewById<Button>(R.id.addName);
        button.setOnClickListener {
            saveData()
        }
        val button2 = findViewById<Button>(R.id.zurück);
        button2.setOnClickListener {
            saveData()
            finish()
            val intent = Intent(this,MainMenu::class.java)
            startActivity(intent)
        }
    }
        fun saveData(){
            val enterName = findViewById<TextView>(R.id.enterName);
            val insertedText = enterName.text.toString()
            val aktuellerName = findViewById<TextView>(R.id.aktuellerName);
            aktuellerName.text = insertedText
            val sharedPreference = getSharedPreferences("Name", Context.MODE_PRIVATE)
            val editor = sharedPreference.edit()
            editor.apply{
                putString("STRING_KEY",insertedText)
            }.apply()
            Toast.makeText(this,"Name gespeichert",Toast.LENGTH_SHORT).show()

        }

        fun loadData(){
            val sharedPreference = getSharedPreferences("Name", Context.MODE_PRIVATE)
            val savedString = sharedPreference.getString("STRING_KEY", null)
            val aktuellerName = findViewById<TextView>(R.id.aktuellerName);
            aktuellerName.text = savedString
        }

}