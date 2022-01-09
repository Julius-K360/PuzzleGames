package com.malkinfo.puzzlegames

/**
 * Klasse zum speichern der Daten eines Spielers
 * Diese wird benötigt um persönlichen Fortschritt zu speichern
 * und um eine Bestenliste zu erstellen
 */
class UserInfo {
    //ID der Spielers
    var id: Int = 0
    //Name des Spielers
    var name: String? = null
    //höchste jemals erreichte Punktzahl im Arcade Mode
    var punkte: Int = 0
}