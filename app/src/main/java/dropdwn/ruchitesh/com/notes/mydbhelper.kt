package dropdwn.ruchitesh.com.notes
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast
class mydbhelper: SQLiteOpenHelper{

    private var mydb:SQLiteDatabase?=null
    var context:Context?=null
    var tname="notestable"
    var c1="id"
    var c2="title"
    var c3="description"


    constructor(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : super(context,name , factory, version)
    {
    this.context=context

    }

    override fun onCreate(db: SQLiteDatabase?) {

       try {

           db!!.execSQL("CREATE TABLE if not exists $tname ( $c1 INTEGER PRIMARY KEY AUTOINCREMENT , $c2 text , $c3 text )")
          // Toast.makeText(this.context,"table is created !!",Toast.LENGTH_LONG).show()
       }catch (ex:Exception)
       {
          // Toast.makeText(this.context,ex.message.toString(),Toast.LENGTH_LONG).show()
       }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS " + tname)
        }
    }
    fun startdatabase()
    {
            mydb=getWritableDatabase()
    }
    fun closedatabase()
    {
        if(mydb!!.isOpen())
        {
            mydb!!.close()
        }
    }
    fun insert(title:String,description:String):Long
    {

        try {

            var cv=ContentValues()
            cv.put("title",title)
            cv.put("description",description)
            //(mydb!!.insert(tname,"",cv))
//            mydb!!.execSQL("insert into $tname ($c1 , $c2 , $c3) values ($id,\" $title \",\"$description\" )")
//              id++
            return mydb!!.insert(tname,null,cv)

        }
        catch (ex:Exception)
        {

          //  Toast.makeText(this.context,(ex.message.toString()),Toast.LENGTH_LONG).show()
            return (-1).toLong()
        }

    }
    fun query(projection:Array<String>,selection:String,selectionArgs:Array<String>,sortOrder:String):Cursor{
        var qb=SQLiteQueryBuilder()
        qb.tables=tname

            var cursor=qb.query(mydb,projection,selection,selectionArgs,null,null,sortOrder)
            return cursor



    }
    fun delete(selection:String,id:String)
    {var selectionCriteria=arrayOf(id)
        mydb!!.delete(tname,selection,selectionCriteria)
    }
    fun update(id:Int,title:String,description: String):Int
    {
        var cv=ContentValues()
        cv!!.put("title",title)
        cv!!.put("description",description)
        var args= arrayOf(id.toString())

       val count= mydb!!.update(tname,cv,"id=?",args)
        return count

    }

}