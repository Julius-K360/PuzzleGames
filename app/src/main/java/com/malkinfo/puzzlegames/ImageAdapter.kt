package com.malkinfo.puzzlegames

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import java.io.IOException
/*
 * Diese Klasse bietet Methoden um an Bilder aus unserem assets Ordner
 *  zu erhalten und passend vorbereiten zu können.
 */

class ImageAdapter(private val mContext: Context) : BaseAdapter() {

    /**
     *  Bietet Zugriff auf die Rohdatendateien einer Anwendung
     */
    private val am: AssetManager
    private var files: Array<String>? = null

    override fun getCount(): Int {
        return files!!.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }
    /**
     * erstelle eine ImageView für jedes Item welches von Adapter referenziert wird
     */

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val v = LayoutInflater.from(mContext).inflate(R.layout.grid_element,null)


        val imageView = v.findViewById<ImageView>(R.id.gridImageview)
        imageView.setImageBitmap(null)
        // Der bildbezogenen Code soll ausgeführt werden, nachdem die Ansicht angelegt wurde.
        imageView.post {
            object : AsyncTask<Void?, Void?, Void?>() {
                private var bitmap: Bitmap? = null
                protected override fun doInBackground(vararg p0: Void?): Void? {
                    bitmap = getPicFromAsset(imageView, files!![position])
                    return null
                }

                override fun onPostExecute(aVoid: Void?) {
                    super.onPostExecute(aVoid)
                    imageView.setImageBitmap(bitmap)
                }
            }.execute()
        }
        return v
    }
    /**
     * Methode um Bilder aus den Asset zu holen und unsere View zu damit zu füllen
     */
    private fun getPicFromAsset(imageView: ImageView, assetName: String): Bitmap? {
        // Bekomme das Verhältnis der View
        val targetW = imageView.width
        val targetH = imageView.height
        return if (targetW == 0 || targetH == 0) {
            // View hat keine Größe
            null
        } else try {
            val `is` = am.open("img/$assetName")
            // hole das Größenverhältnis der Bitmap
            val bmOptions = BitmapFactory.Options()
            bmOptions.inJustDecodeBounds = true
            BitmapFactory.decodeStream(`is`, Rect(-1, -1, -1, -1), bmOptions)
            val photoW = bmOptions.outWidth
            val photoH = bmOptions.outHeight

            // Finde heraus wie das Bild herunter skalliert werden muss
            val scaleFactor = Math.min(photoW / targetW, photoH / targetH)
            `is`.reset()

            //Dekodiere die Bilddatei in eine Bitmap, welche die richtige Bröße der View hat.
            bmOptions.inJustDecodeBounds = false
            bmOptions.inSampleSize = scaleFactor
            bmOptions.inPurgeable = true
            BitmapFactory.decodeStream(`is`, Rect(-1, -1, -1, -1), bmOptions)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Initialisiere eine liste an Bildern, welche für die Puzzle verwendet werden sollen.
     */
    init {
        am = mContext.assets
        try {
            files = am.list("img")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}