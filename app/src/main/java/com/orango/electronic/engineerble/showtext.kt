package com.orango.electronic.engineerble

import com.orange.jzchi.jzframework.JzAdapter
import kotlinx.android.synthetic.main.shoetext.view.*

class showtext(var text:ArrayList<String>,var time:ArrayList<String>,var type:ArrayList<Int>):JzAdapter(R.layout.shoetext){
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
holder.mView.textter.text=text[position]
        holder.mView.time.text=time[position]
        val resourse=holder.mView.resources
        when(type[position]){
            1->{holder.mView.textter.setTextColor(resourse.getColor(R.color.green))}
            2->{holder.mView.textter.setTextColor(resourse.getColor(R.color.color_FF2F81DA))}
            3->{holder.mView.textter.setTextColor(resourse.getColor(R.color.color_red))}
        }
    }

    override fun sizeInit(): Int {
        return text.size
    }

}