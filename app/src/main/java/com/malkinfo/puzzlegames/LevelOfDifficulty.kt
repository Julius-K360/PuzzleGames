package com.malkinfo.puzzlegames

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LevelOfDifficulty: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schwierigkeitsgrad)


        //Button des einfachen Schwirigkeitsgrades
        val buttonArcadeMode = findViewById<Button>(R.id.ButtonEinfach);
        buttonArcadeMode.setOnClickListener {
            val intent = Intent(this,ArcadeActivity::class.java)
            startActivity(intent)
        }

        //Button des mittleren Schwirigkeitsgrades
        val buttonArcadeModeMiddle = findViewById<Button>(R.id.ButtonMittel);
        buttonArcadeModeMiddle.setOnClickListener {
            val intent = Intent(this,ArcadeActivityMiddle::class.java)
            startActivity(intent)
        }

        //Button des schweren Schwirigkeitsgrades
        val buttonArcadeModeHard = findViewById<Button>(R.id.ButtonSchwer);
        buttonArcadeModeHard.setOnClickListener {
            val intent = Intent(this,ArcadeActivityHard::class.java)
            startActivity(intent)
        }
    }
}