package com.orango.electronic.engineerble

import android.app.Dialog
import android.view.KeyEvent
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orango.electronic.engineerble.Frag.MainPeace

class Da_Scan_ble() : SetupDialog()
{
    val manager= (JzActivity.getControlInstance().getNowPage() as MainPeace)

    override fun dismess() {

    }

    override fun keyevent(event: KeyEvent): Boolean {
        return true
    }

    override fun setup(rootview: Dialog) {
        manager.devicelist.clear()
        rootview.findViewById<RecyclerView>(R.id.re).layoutManager = LinearLayoutManager(JzActivity.getControlInstance().getRootActivity())
        rootview.findViewById<RecyclerView>(R.id.re).adapter = manager.adscan
        manager.adscan.notifyDataSetChanged()
        rootview.findViewById<Button>(R.id.close).setOnClickListener {
            manager.helper.stopScan()

            manager.helper.disconnect()

            JzActivity.getControlInstance().closeDiaLog()
        }
        rootview.findViewById<Button>(R.id.rescan).setOnClickListener {
            manager.helper.startScan()
        }
    }


}