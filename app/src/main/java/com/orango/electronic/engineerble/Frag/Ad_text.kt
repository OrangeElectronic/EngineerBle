package com.orango.electronic.engineerble.Frag

import com.jzsql.lib.mmySql.Sql_Result
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.jzchi.jzframework.JzFragement
import com.orango.electronic.engineerble.Frag.MainPeace
import com.orango.electronic.engineerble.MainActivity
import com.orango.electronic.engineerble.R
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.adapter_ad_text.view.*

class Ad_text(val myitem: Bs_text):JzAdapter(R.layout.adapter_ad_text) {

    val mMainActivity=JzActivity.getControlInstance().getRootActivity() as MainActivity

    override fun sizeInit(): Int {
        return myitem.text.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mView.editText2.isFocusable = false
        holder.mView.editText2.text = myitem.Name[position]
        holder.mView.textView7.text = myitem.text[position]
        holder.mView.twotext_button.setOnClickListener{
            val mMainActivity= JzActivity.getControlInstance().findFragByTag("MainPeace")
            if(mMainActivity is MainPeace){
                mMainActivity.rootview.name.setText(myitem.text[position])
                mMainActivity.rootview.button2.performClick()
                JzActivity.getControlInstance().closeDiaLog("dialog_all_tx")
            }
        }
        holder.mView.textView9.setOnClickListener {
            if(position>=4) {
                mMainActivity.TXdata.exsql("delete from TX where `name`='" + myitem.Name[position] + "'" + "and `Txuuid` = '" + myitem.text[position] + "'")
                myitem.text.removeAt(position)
                this.notifyDataSetChanged()
                //myitem.text.removeAt(position)
            }
        }
    }
}