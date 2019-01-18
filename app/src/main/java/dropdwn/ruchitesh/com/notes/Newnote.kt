package dropdwn.ruchitesh.com.notes

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_newnote.*
import org.w3c.dom.Text

class Newnote : AppCompatActivity() {
    var mydatabase=mydbhelper(this,"mydb.db",null,1)
    var thisisupdate=false
    var id:Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newnote)

        try {
            var bundle:Bundle=intent.extras
            id=bundle.getInt("idintent")
            newtitle.setText(bundle.getString("titleintent").toString())
           newdesc.setText(bundle.getString("descintent").toString())
            thisisupdate=true
        }catch (ex:Exception){}
    }


    fun clicked(view:View)
    {
        if(!thisisupdate) {
            mydatabase.startdatabase()
            val id = mydatabase.insert(newtitle.text.toString(), newdesc.text.toString())
            mydatabase.closedatabase()
            finish()
        }
        else
        {
            mydatabase.startdatabase()
            val id = mydatabase.update(id!!,newtitle.text.toString(), newdesc.text.toString())
            mydatabase.closedatabase()
            finish()
        }
    }
}
