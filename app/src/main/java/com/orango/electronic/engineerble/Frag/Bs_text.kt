package com.orango.electronic.engineerble.Frag

import android.widget.EditText

class Bs_text
{
    var Name = ArrayList<String>()
    var text = ArrayList<String>()

    fun add(Name:String,text:String)
    {
        this.Name.add(Name)
        this.text.add(text)
    }

    fun clear()
    {
        Name.clear()
        text.clear()
    }
}