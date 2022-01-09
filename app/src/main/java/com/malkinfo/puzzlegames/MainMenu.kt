package com.malkinfo.puzzlegames

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.kotlincodes.sharedpreferenceswithkotlin.SharedPreference
import android.content.SharedPreferences
import android.widget.TextView


/**
 * Logik hinter dem Hauptmenü der Muscle Puzzle App.
 * Diese Klasse ist im Manifest eingetragen, als die Klasse welche beim starten der App zusehen seien soll.
 *
 */
class MainMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainmenu)

        //Button zum starten des SingleGame Modus
        val buttonSingleGame = findViewById<Button>(R.id.SingleGame);
        buttonSingleGame.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        //Button zum starten des ArcadeGame Modus
        val buttonArcadeMode = findViewById<Button>(R.id.ArcadeGame);
        buttonArcadeMode.setOnClickListener {
            val intent = Intent(this,LevelOfDifficulty::class.java)
            startActivity(intent)
        }
        //Button zum öffnen der Einstellungen
        val buttonSettings = findViewById<Button>(R.id.settings);
        buttonSettings.setOnClickListener {
            val intent = Intent(this,Settings::class.java)
            startActivity(intent)
        }
        //Button zum öffnen der Bestenliste
        val buttonLeaderboard = findViewById<Button>(R.id.LeaderBoard);
        buttonLeaderboard.setOnClickListener {
            val intent = Intent(this,LeaderboardActivity::class.java)
            startActivity(intent)
        }
       loadData()
    }

    /**
     * Funktion, welche den Namen des aktuellen Spielers zurück gibt.
     * Hierbei wird der Name mithilfe einer sharedPreference global gespeichert.
     */
fun loadData() {
    val sharedPreference2 = getSharedPreferences("Name", Context.MODE_PRIVATE)
    val savedString = sharedPreference2.getString("STRING_KEY", null)
    val aktuellerSpieler = findViewById<TextView>(R.id.aktuellerSpieler);
    aktuellerSpieler.text = savedString
}
}