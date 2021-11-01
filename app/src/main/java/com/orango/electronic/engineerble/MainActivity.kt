
package com.orango.electronic.engineerble

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import com.jzsql.lib.mmySql.ItemDAO
import com.orange.jzchi.jzframework.JzActivity
import com.orango.electronic.engineerble.Frag.MainPeace

class MainActivity : JzActivity() {

    lateinit var TXdata: ItemDAO

    override fun changePageListener(tag: String, frag: Fragment) {

    }

    override fun keyEventListener(event: KeyEvent): Boolean {
        return true
    }

    override fun savedInstanceAble(): Boolean {
        return true
    }

    override fun viewInit(rootview: View) {

        TXdata = ItemDAO(this, "TX.db").create()
        //建立資料表
        TXdata.exsql(
            "CREATE TABLE   IF NOT EXISTS TX (\n" +
                    "    id   INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "    name VARCHAR NOT NULL,\n" +
                    "    Txuuid VARCHAR NOT NULL\n" +
                    ");\n"
        )

      JzActivity.getControlInstance().setHome(MainPeace(),"MainPeace")
    }

}
