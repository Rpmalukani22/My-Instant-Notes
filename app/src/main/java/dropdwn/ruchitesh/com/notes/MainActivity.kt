package dropdwn.ruchitesh.com.notes

import android.app.SearchManager

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import dropdwn.ruchitesh.com.notes.R.id.menuadd
import dropdwn.ruchitesh.com.notes.R.id.rate

import kotlinx.android.synthetic.main.activity_main.*

import kotlinx.android.synthetic.main.ticket.view.*

import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    var notelist = ArrayList<Note>()
    var textlist = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadquery("%")
        printdata()
        var myadapter = MynotesAdapter(notelist,this)
        lvtickets.adapter = myadapter

    }

    fun printdata() {
        notelist.clear()
        if (cursor!!.moveToFirst()) {
            do {
                var ID = cursor!!.getInt(cursor!!.getColumnIndex("id"))
                var Title = cursor!!.getString(cursor!!.getColumnIndex("title"))
                var Desc = cursor!!.getString(cursor!!.getColumnIndex("description"))
                notelist.add(Note(ID, Title, Desc))

            } while (cursor!!.moveToNext())
        }
    }

    override fun onResume() {
        super.onResume()
        loadquery("%")
        printdata()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val sv = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setQueryHint(getString(R.string.shint))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                loadquery("%" + newText + "%")
                printdata()
                return false
            }


            override fun onQueryTextSubmit(query: String?): Boolean {
                //Toast.makeText(this@MainActivity, query, Toast.LENGTH_LONG).show()
                loadquery("%" + query + "%")
                printdata()
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)

    }
    var cursor: Cursor? = null
    var dh = mydbhelper(this@MainActivity, "mydb.db", null, 1)
    fun loadquery(contenttosearch: String) {
        var myadapter = MynotesAdapter(notelist,this)
        lvtickets.adapter = myadapter
        dh.startdatabase()
        var projection = arrayOf("id", "title", "description")
        var contenttosearchlist = arrayOf(contenttosearch)
        cursor = dh.query(projection, "title like ?", contenttosearchlist, "title")
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                menuadd -> {
                    var intent = Intent(this, Newnote::class.java)
                    startActivity(intent)
                }
                rate->
                {

                    try {
                        var uri:Uri= Uri.parse("market://details?id="+ packageName)
                        var intent=Intent(Intent.ACTION_VIEW,uri)
                        startActivity(intent)
                    }
                    catch (ex:Exception)
                    {
                        var uri:Uri= Uri.parse("http://play.google.com/store/apps/details?id="+ packageName)
                        var intent=Intent(Intent.ACTION_VIEW,uri)
                        startActivity(intent)
                    }



                }
            }
        }

        return super.onOptionsItemSelected(item)
    }


    inner class MynotesAdapter : BaseAdapter {
        var listnoteadapter = ArrayList<Note>()
        var context:Context?=null
        constructor(listnoteadapter: ArrayList<Note>,context:Context) {
            this.listnoteadapter = listnoteadapter
            this.context=context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var myview = layoutInflater.inflate(R.layout.ticket, null)
            var note = listnoteadapter[position]
            myview.tvtitle.text = listnoteadapter[position].notetitle
            myview.tvdesc.text = listnoteadapter[position].notedesc
            myview.ivdel.setOnClickListener(View.OnClickListener {
                dh.startdatabase()
                dh.delete("id=?",(note.noteid.toString()))
                loadquery("%")
                printdata()
               // Toast.makeText(context,"deleted successfully",Toast.LENGTH_LONG).show()

            })
            myview.ived.setOnClickListener(View.OnClickListener {
                var intent = Intent(this@MainActivity,Newnote::class.java)
                intent.putExtra("idintent",note.noteid)
                intent.putExtra("titleintent",note.notetitle)
                intent.putExtra("descintent",note.notedesc)
                startActivity(intent)
            })

            return myview
        }

        override fun getItem(position: Int): Any {
            return listnoteadapter[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listnoteadapter.size
        }


    }

}
