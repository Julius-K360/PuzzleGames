package com.malkinfo.puzzlegames
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.BitmapFactory.Options
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.kotlincodes.sharedpreferenceswithkotlin.SharedPreference
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

/**
 *
 * Klasse für den eigenen Spielmodus
 *
 */
class ArcadeActivityMiddle : AppCompatActivity() {
    //Variablen
    var pieces: ArrayList<PuzzlePiece?>? = null
    var mCurrentPhotoPath: String? = null
    var mCurrentPhotoUri: String? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arcade_mode_puzzle)
        val layout = findViewById<RelativeLayout>(R.id.layout)
        val imageView = findViewById<ImageView>(R.id.imageView)
        val intent = intent

        /** Eigener Code ****/

        startTimeCounter(findViewById<TextView>(R.id.countTime))
        /**ProgressBar ******************/ //noch nicht richtig

        val sharedPreference = SharedPreference(this)
        var aktuellerCounterWert = sharedPreference.getValueInt("counter")
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.min = 0
        progressBar.max = 80
        progressBar.progress = aktuellerCounterWert

        var currentProgress = aktuellerCounterWert

        //Farbe der Progressbar ändern
        progressBar.getProgressDrawable().setColorFilter(Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);

        //lade aktuellen Punktestand
        loadDatainTextView()

        //Animation der Progressbar
        while(currentProgress >0){
            currentProgress --
            ObjectAnimator.ofInt(progressBar,"progress",currentProgress)
                .setDuration((aktuellerCounterWert * 1000).toLong())
                .start()

        }
        //sharedPreference.save("Punktestand",)


        //val assetName = intent.getStringExtra("assetName")
        mCurrentPhotoPath = intent.getStringExtra("mCurrentPhotoPath")
        mCurrentPhotoUri = intent.getStringExtra("mCurrentPhotoUri")

        //starte den code nach dem View geladen wurde
        // um alle Dimensionen errechnet zu haben
        imageView.post {

            /*Unterschied zur Puzzle Activity: Nun wird ein random Puzzle gestartet.
             Sobald dies fertig gepuzzled wurde kommt direkt das nächste
             Ist der Timer abgelaufen ist das Spiel vorbei**/

            /*Wir erstellen eine Liste von Nummern, mischen diese und nehmen dann die erste der Liste.*/
            val randomInteger = (1..10).shuffled().first()
            when (randomInteger) {
                1 -> setPicFromAsset("pexels-anete-lusina-4793237.jpg", imageView)
                2 -> setPicFromAsset("pexels-anush-gorak-1431283.jpg", imageView)
                3 -> setPicFromAsset("pexels-cleyder-duque-3588479.jpg", imageView)
                4 -> setPicFromAsset("pexels-leon-ardho-1552101.jpg", imageView)
                5 -> setPicFromAsset("pexels-mister-mister-3490348.jpg", imageView)
                6 -> setPicFromAsset("barbell-g2d3946955_1920.jpg", imageView)
                7 -> setPicFromAsset("pexels-li-sun-2294362.jpg", imageView)
                8 -> setPicFromAsset("pexels-tima-miroshnichenko-5327539.jpg", imageView)
                9 -> setPicFromAsset("training-g9fa5c6459_1920.jpg", imageView)
                10 -> setPicFromAsset("weight-lifting-gc80e7cf2f_1920.jpg", imageView)
            }
            pieces = splitImage()
            val touchListener = TouchListenerArcadeActivityMiddle(this@ArcadeActivityMiddle)
            // Mische alle Puzzle Teile
            Collections.shuffle(pieces)
            for (piece in pieces!!) {
                piece!!.setOnTouchListener(touchListener)
                layout.addView(piece)
                // zufällige Position am unteren Bildschirmrand
                val lParams = piece.layoutParams as RelativeLayout.LayoutParams
                lParams.leftMargin = Random().nextInt(layout.width - piece.pieceWidth)
                lParams.topMargin = layout.height - piece.pieceHeight
                piece.layoutParams = lParams
            }
        }


    }


    /***************Timer
     * ursprüngliche Quelle: https://www.tutorialspoint.com/how-to-set-a-timer-in-android-using-kotlin
     * in allerdings deutlich umgewandelter Form
     * *****************/

    /**
     * Funktion für den Timer
     * @param isGameActiv
     * @param aktuellerCounterWert
     *
     */
    fun startTimeCounter(view: View) {
        val sharedPreference = SharedPreference(this)
        var isGameActiv = sharedPreference.getValueBoolien("IsGameAlive",false) // Speichert den Status des Spiels in einer Preference
        if (isGameActiv == false){

            sharedPreference.save("counter",50) // Wenn spiel nicht Aktiv setze Timer auf 80 Millisekunden

            sharedPreference.save("IsGameAlive",true) //Setzte das Spiel auf True nachdem es gestartet wurde, dadurch kein Timer Reset

        }

        val countTime: TextView = findViewById(R.id.countTime)


        var aktuellerCounterWert = sharedPreference.getValueInt("counter") //COunterwert aus der SharedPreference nehmen ihn zu verändern
        object : CountDownTimer(aktuellerCounterWert.toLong()*1000, 1000) {  //CounterWert w8ird mal 1000 gerechnet damit wir Sekunden haben
            override fun onTick(millisUntilFinished: Long) {
                countTime.text = aktuellerCounterWert.toString()
                aktuellerCounterWert-- //Der Timer zählt jede Sekunde um 1 runter
                sharedPreference.save("counter",aktuellerCounterWert)


            }
            override fun onFinish() {
                //SPeichert Namen in Datenbank
                val aktuellePunktzahl = loadData();
                val aktuellerSpielerName = loadName();
                var helper = DatabaseHandler(this@ArcadeActivityMiddle)
                helper.insertData(aktuellerSpielerName,aktuellePunktzahl)


                //setzte die Anzahl gelöster Puzzle zurück
                saveData(0)
                sharedPreference.save("IsGameAlive",false) //Setze das Spiel auf false zurück
                countTime.text = "Finished"
                AlertDialog.Builder(this@ArcadeActivityMiddle) // Baut den Dialog der am ende Angezeigt wird
                    .setTitle("Oh no verloren")
                    .setIcon(R.drawable.ic_baseline_alarm_24)
                    .setMessage("Du Disco Pumper\n Geh mehr Beine trainieren, am besten im SingleGame Modus ..!!") // Text der im Alert angezeigt wird
                    .setPositiveButton("Hauptmenü"){
                            dialog, _->
                        val intent = Intent(this@ArcadeActivityMiddle,MainMenu::class.java)  //Button leitet einen zurück zum Mainmenü
                        startActivity(intent)
                        dialog.dismiss()
                    }
                    .create() // Erstellt den Alert
                    .show() // Zeigt den Alert auf dem Display an
            }
        }.start()  // Startet den Alert
    }


    fun loadName(): String? {
        val sharedPreference2 = getSharedPreferences("Name", Context.MODE_PRIVATE)
        val savedString = sharedPreference2.getString("STRING_KEY", null)
        return savedString
    }



    /********************************/


    /**
     * Funktion die Anzeigt wie viel Puzzle gelöst wurde
     */
    fun loadDatainTextView(){
        val sharedPreference = getSharedPreferences("Punktestand", Context.MODE_PRIVATE)
        val savedString = sharedPreference.getString("aktuellerPunktestand", null)
        val aktuellerPunktestand = findViewById<TextView>(R.id.aktuellerPunkteStand);
        aktuellerPunktestand.text = "gelöste Puzzle: " + savedString
    }

    /**
     *  Gibt den Int Wert der gelösten Puzzle zurück
     *  @return aktuelleAnzahlfertigerPuzzle int
     */
    fun loadData(): Int? {
        val sharedPreference = getSharedPreferences("Punktestand", Context.MODE_PRIVATE)
        val savedString = sharedPreference.getString("aktuellerPunktestand", null)
        val aktuelleAnzahlfertigerPuzzle = savedString?.toInt()
        return aktuelleAnzahlfertigerPuzzle
    }

    /**
     *  Speichert die Anzahl der gelösten Puzzle
     */
    fun saveData(punkte : Int ){
        val insertedNumber = punkte.toString()
        val aktuellerName = findViewById<TextView>(R.id.aktuellerPunkteStand);
        aktuellerName.text = insertedNumber
        val sharedPreference = getSharedPreferences("Punktestand", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.apply{
            putString("aktuellerPunktestand",insertedNumber)
        }.apply()
    }

    fun checkGameOver()  {
        if (isGameOver) {
            val sharedPreference = SharedPreference(this)
            sharedPreference.save("IsGameAlive",true)
            var aktuelleAnzahlfertigerPuzzle = loadData()
            //speicher neuen Punktestand
            if (aktuelleAnzahlfertigerPuzzle != null) {
                saveData(aktuelleAnzahlfertigerPuzzle +1)
            }


            //Funktioniert nicht richtig --> klappt einmal und dann ist der Timer wieder auf 40
            //Timer erhöhen nachdem das Puzzle geschafft wurde

            val sharedPreference2 = SharedPreference(this)
            var temp = sharedPreference2.getValueInt("counter")
            sharedPreference2.save("counter",temp + 10)

            //Aktivität neustarten für neues Puzzle
            val intent = Intent(this,ArcadeActivity::class.java)
            startActivity(intent)
        }
    }


    /**
     *
     * Tutorial code ab hier nicht weiter schreiben
     *
     */

    /************/
    private fun setPicFromAsset(assetName: String, imageView: ImageView) {
        //hole das Verhältnis der View
        val targetW = imageView.width
        val targetH = imageView.height
        val am = assets
        try {
            val `is` = am.open("img/$assetName")
            //hole das Verhältnis der Bitmap
            val bmOptions = Options()
            bmOptions.inJustDecodeBounds = true
            BitmapFactory.decodeStream(`is`, Rect(-1, -1, -1, -1), bmOptions)
            val photoW = bmOptions.outWidth
            val photoH = bmOptions.outHeight

            //finde heraus wie stark das Image herunter skalliert werden muss
            val scaleFactor = Math.min(photoW / targetW, photoH / targetH)
            `is`.reset()

            // Dekodiere das image File in eine Bitmap in der größe um die View zu füllen
            bmOptions.inJustDecodeBounds = false
            bmOptions.inSampleSize = scaleFactor
            bmOptions.inPurgeable = true
            val bitmap = BitmapFactory.decodeStream(`is`, Rect(-1, -1, -1, -1), bmOptions)
            imageView.setImageBitmap(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    //aus dem Tutorial: Methode schneidet Bilder aus. Fügt diese auf eine Bitmap
    // und zeichnet die Ränder mit Pfaden
    private fun splitImage(): ArrayList<PuzzlePiece?> {
        val piecesNumber = 12
        val rows = 4
        val cols = 3
        val imageView = findViewById<ImageView>(R.id.imageView)
        val pieces = ArrayList<PuzzlePiece?>(piecesNumber)

        //hole das skalierte bitmap von dem roh Image
        val drawable = imageView.drawable as BitmapDrawable
        val bitmap = drawable.bitmap
        val dimensions = getBitmapPositionInsideImageView(imageView)
        val scaledBitmapLeft = dimensions[0]
        val scaledBitmapTop = dimensions[1]
        val scaledBitmapWidth = dimensions[2]
        val scaledBitmapHeight = dimensions[3]
        val croppedImageWidth = scaledBitmapWidth - 2 * Math.abs(scaledBitmapLeft)
        val croppedImageHeight = scaledBitmapHeight - 2 * Math.abs(scaledBitmapTop)
        val scaledBitmap =
            Bitmap.createScaledBitmap(bitmap, scaledBitmapWidth, scaledBitmapHeight, true)
        val croppedBitmap = Bitmap.createBitmap(
            scaledBitmap,
            Math.abs(scaledBitmapLeft),
            Math.abs(scaledBitmapTop),
            croppedImageWidth,
            croppedImageHeight
        )

        //Berechne die breite und höhe jedes Teils
        val pieceWidth = croppedImageWidth / cols
        val pieceHeight = croppedImageHeight / rows

        //erstelle jedes bitmap piece und füge es dem daraus resultierenden Array hinzu
        var yCoord = 0
        for (row in 0 until rows) {
            var xCoord = 0
            for (col in 0 until cols) {
                // calculate offset for each piece
                var offsetX = 0
                var offsetY = 0
                if (col > 0) {
                    offsetX = pieceWidth / 3
                }
                if (row > 0) {
                    offsetY = pieceHeight / 3
                }

                // füge das offset zu jedem Puzzleteil
                val pieceBitmap = Bitmap.createBitmap(
                    croppedBitmap,
                    xCoord - offsetX,
                    yCoord - offsetY,
                    pieceWidth + offsetX,
                    pieceHeight + offsetY
                )
                val piece = PuzzlePiece(applicationContext)
                piece.setImageBitmap(pieceBitmap)
                piece.xCoord = xCoord - offsetX + imageView.left
                piece.yCoord = yCoord - offsetY + imageView.top
                piece.pieceWidth = pieceWidth + offsetX
                piece.pieceHeight = pieceHeight + offsetY

                // diese bitmap enthält das finale Puzzle image
                val puzzlePiece = Bitmap.createBitmap(
                    pieceWidth + offsetX,
                    pieceHeight + offsetY,
                    Bitmap.Config.ARGB_8888
                )

                // zeichne Pfad
                val bumpSize = pieceHeight / 4
                val canvas = Canvas(puzzlePiece)
                val path = Path()
                path.moveTo(offsetX.toFloat(), offsetY.toFloat())
                if (row == 0) {
                    // top side piece
                    path.lineTo(pieceBitmap.width.toFloat(), offsetY.toFloat())
                } else {
                    // stoßt an obere kante
                    path.lineTo(
                        (offsetX + (pieceBitmap.width - offsetX) / 3).toFloat(),
                        offsetY.toFloat()
                    )
                    path.cubicTo(
                        (offsetX + (pieceBitmap.width - offsetX) / 6).toFloat(),
                        (offsetY - bumpSize).toFloat(),
                        (offsetX + (pieceBitmap.width - offsetX) / 6 * 5).toFloat(),
                        (offsetY - bumpSize).toFloat(),
                        (offsetX + (pieceBitmap.width - offsetX) / 3 * 2).toFloat(),
                        offsetY.toFloat()
                    )
                    path.lineTo(pieceBitmap.width.toFloat(), offsetY.toFloat())
                }
                if (col == cols - 1) {
                    // right side piece
                    path.lineTo(pieceBitmap.width.toFloat(), pieceBitmap.height.toFloat())
                } else {
                    // right bump
                    path.lineTo(
                        pieceBitmap.width.toFloat(),
                        (offsetY + (pieceBitmap.height - offsetY) / 3).toFloat()
                    )
                    path.cubicTo(
                        (pieceBitmap.width - bumpSize).toFloat(),
                        (offsetY + (pieceBitmap.height - offsetY) / 6).toFloat(),
                        (pieceBitmap.width - bumpSize).toFloat(),
                        (offsetY + (pieceBitmap.height - offsetY) / 6 * 5).toFloat(),
                        pieceBitmap.width.toFloat(),
                        (offsetY + (pieceBitmap.height - offsetY) / 3 * 2).toFloat()
                    )
                    path.lineTo(pieceBitmap.width.toFloat(), pieceBitmap.height.toFloat())
                }
                if (row == rows - 1) {
                    // bottom side piece
                    path.lineTo(offsetX.toFloat(), pieceBitmap.height.toFloat())
                } else {
                    // bottom bump
                    path.lineTo(
                        (offsetX + (pieceBitmap.width - offsetX) / 3 * 2).toFloat(),
                        pieceBitmap.height.toFloat()
                    )
                    path.cubicTo(
                        (offsetX + (pieceBitmap.width - offsetX) / 6 * 5).toFloat(),
                        (pieceBitmap.height - bumpSize).toFloat(),
                        (offsetX + (pieceBitmap.width - offsetX) / 6).toFloat(),
                        (pieceBitmap.height - bumpSize).toFloat(),
                        (offsetX + (pieceBitmap.width - offsetX) / 3).toFloat(),
                        pieceBitmap.height.toFloat()
                    )
                    path.lineTo(offsetX.toFloat(), pieceBitmap.height.toFloat())
                }
                if (col == 0) {
                    // left side piece
                    path.close()
                } else {
                    // left bump
                    path.lineTo(
                        offsetX.toFloat(),
                        (offsetY + (pieceBitmap.height - offsetY) / 3 * 2).toFloat()
                    )
                    path.cubicTo(
                        (offsetX - bumpSize).toFloat(),
                        (offsetY + (pieceBitmap.height - offsetY) / 6 * 5).toFloat(),
                        (offsetX - bumpSize).toFloat(),
                        (offsetY + (pieceBitmap.height - offsetY) / 6).toFloat(),
                        offsetX.toFloat(),
                        (offsetY + (pieceBitmap.height - offsetY) / 3).toFloat()
                    )
                    path.close()
                }

                // mask the piece
                val paint = Paint()
                paint.color = -0x1000000
                paint.style = Paint.Style.FILL
                canvas.drawPath(path, paint)
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
                canvas.drawBitmap(pieceBitmap, 0f, 0f, paint)

                // draw a white border
                var border = Paint()
                border.color = -0x7f000001
                border.style = Paint.Style.STROKE
                border.strokeWidth = 8.0f
                canvas.drawPath(path, border)

                // draw a black border
                border = Paint()
                border.color = -0x80000000
                border.style = Paint.Style.STROKE
                border.strokeWidth = 3.0f
                canvas.drawPath(path, border)

                // set the resulting bitmap to the piece
                piece.setImageBitmap(puzzlePiece)
                pieces.add(piece)
                xCoord += pieceWidth
            }
            yCoord += pieceHeight
        }
        return pieces
    }

    private fun getBitmapPositionInsideImageView(imageView: ImageView?): IntArray {
        val ret = IntArray(4)
        if (imageView == null || imageView.drawable == null) return ret

        // Hole das Verhältnis des Bildes
        //Bekomme die Image Matrix Werte und platziere sie in einem Array
        val f = FloatArray(9)
        imageView.imageMatrix.getValues(f)

        //Extrahiere die skalierten Werte, verwende hierfür die Konstanten
        val scaleX = f[Matrix.MSCALE_X]
        val scaleY = f[Matrix.MSCALE_Y]

        //Hole das drawable
        val d = imageView.drawable
        val origW = d.intrinsicWidth
        val origH = d.intrinsicHeight

        // Berechne die aktuelle dimesion
        val actW = Math.round(origW * scaleX)
        val actH = Math.round(origH * scaleY)
        ret[2] = actW
        ret[3] = actH

        // Hole die Image Position
        //Hierbei wird davon ausgegangen das das image zentriert in den View gefügt wurde
        val imgViewW = imageView.width
        val imgViewH = imageView.height
        val top = (imgViewH - actH) / 2
        val left = (imgViewW - actW) / 2
        ret[0] = left
        ret[1] = top
        return ret
    }



    private val isGameOver: Boolean
        private get() {
            for (piece in pieces!!) {
                if (piece!!.canMove) {
                    return false
                }
            }
            return true
        }




    companion object {
        fun rotateImage(source: Bitmap, angle: Float): Bitmap {
            val matrix = Matrix()
            matrix.postRotate(angle)
            return Bitmap.createBitmap(
                source, 0, 0, source.width, source.height,
                matrix, true
            )
        }
    }
}