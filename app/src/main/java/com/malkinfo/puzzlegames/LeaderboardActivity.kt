package com.malkinfo.puzzlegames
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView

/**
 * Klasse der Activity, welche eine Bestenliste zeigt.
 */
class LeaderboardActivity :AppCompatActivity() {

    //Instanziere eine Instanz der Klasse DatabaseHandler
    private var helper = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //setzte die ContentView auf die listview.xml
        setContentView(R.layout.listview)

        //lade mithilfe des DatabaseHandler eine Liste aller bisherigen Spieler
        var listofData: ArrayList<UserInfo> = helper.listOfUserInfo()
        //hole listView
        var listView = findViewById(R.id.listView) as ListView
        //hole einen Instanz der Klasse UserListAdapter und übergebe
        // ihn die Liste aller bisherigen Spieler
        var listAdapter = UserListAdapter(this, listofData)
        //Füge diese Liste nun in die View der XML Datei
        listView.adapter = listAdapter

    }
}