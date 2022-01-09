package com.malkinfo.puzzlegames
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class UserListAdapter(context: Context, arrayOfData: ArrayList<UserInfo>)  : BaseAdapter() {

    var arrayOfData : ArrayList<UserInfo>;
    private val mInflator: LayoutInflater

    init {
        this.arrayOfData = arrayOfData
        this.mInflator = LayoutInflater.from(context)
    }

    override fun getItem(position: Int): Any {
        return arrayOfData[position];
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return arrayOfData.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        val view: View?
        val vh: ListRowHolder
        if (convertView == null) {
            view = this.mInflator.inflate(R.layout.row_item, parent, false)
            vh = ListRowHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ListRowHolder
        }
        vh.name.text = arrayOfData[position].name
        vh.punkte.text = ""+arrayOfData[position].punkte
        return view
    }




    private class ListRowHolder(row: View?) {
        val name: TextView
        val punkte : TextView

        init {
            this.name = row?.findViewById(R.id.name) as TextView
            this.punkte = row?.findViewById(R.id.punkte) as TextView
        }
    }
}