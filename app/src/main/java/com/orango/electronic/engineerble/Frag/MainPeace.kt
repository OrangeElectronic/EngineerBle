package com.orango.electronic.engineerble.Frag
import android.app.Dialog
import android.bluetooth.BluetoothDevice
import android.util.Log
import android.view.KeyEvent
import androidx.recyclerview.widget.LinearLayoutManager
import com.jianzhi.jzblehelper.BleHelper
import com.jianzhi.jzblehelper.FormatConvert.StringHexToByte
import com.jianzhi.jzblehelper.FormatConvert.bytesToHex
import com.jianzhi.jzblehelper.callback.BleCallBack
import com.jianzhi.jzblehelper.models.BleBinary
import com.jzsql.lib.mmySql.Sql_Result
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.jzchi.jzframework.callback.permission_C
import com.orango.electronic.engineerble.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.add_layout.*
import kotlinx.android.synthetic.main.dialog_all_tx.*
import kotlinx.android.synthetic.main.dialog_all_tx.cancel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.experimental.xor

class MainPeace : JzFragement(R.layout.activity_main), BleCallBack {

    val mMainActivity=JzActivity.getControlInstance().getRootActivity() as MainActivity

    var text = ArrayList<String>()
    var time = ArrayList<String>()
    var type = ArrayList<Int>()

    var devicelist = ArrayList<BluetoothDevice>()
    var adapter = showtext(text, time, type)
    var adscan: Ad_ScanBle

    var allmyitem = Bs_text()
    var alladapter = Ad_text(allmyitem)

    lateinit var helper: BleHelper
    private val rxUUID = "00008D81-0000-1000-8000-00805F9B34FB"
    private val TXUUID = "00008D82-0000-1000-8000-00805F9B34FB"
    init {
        adscan = Ad_ScanBle(devicelist, this)
    }

    override fun needGPS() {
        JzActivity.getControlInstance().toast("請打開定位")
    }

    override fun onConnectFalse() {
        val sdf = SimpleDateFormat("HH:mm:ss:SSS")
        val past = sdf.format(Date())
        text.add("onConnectSuccess:")
        time.add(past.toString())
        type.add(3)
        handler.post {adapter.notifyDataSetChanged()
            rootview.recy.scrollToPosition(type.size-1)}
    }

    override fun onConnectSuccess() {
        val sdf = SimpleDateFormat("HH:mm:ss:SSS")
        val past = (sdf.format(Date()))
        text.add("onConnectSuccess:")
        time.add(past.toString())
        type.add(3)
        handler.post {adapter.notifyDataSetChanged()
            rootview.recy.scrollToPosition(type.size-1)}

    }

    override fun onConnecting() {
        val sdf = SimpleDateFormat("HH:mm:ss:SSS")
        val past = sdf.format(Date())
        text.add("onConnecting:")
        time.add(past.toString())
        type.add(3)
        handler.post {adapter.notifyDataSetChanged()
            rootview.recy.scrollToPosition(type.size-1)}
    }

    override fun onDisconnect() {
        val sdf = SimpleDateFormat("HH:mm:ss:SSS")
        val past = sdf.format(Date())
        text.add("onDisconnect:")
        time.add(past.toString())
        type.add(3)
        handler.post {adapter.notifyDataSetChanged()
            rootview.recy.scrollToPosition(type.size-1)}
    }

    override fun requestPermission(permission: ArrayList<String>) {
        JzActivity.getControlInstance().permissionRequest(permission.toTypedArray(),  object :
            permission_C {
            override fun requestFalse(a: String?) {
                Log.e("JzBleMessage_False", a.toString())
                JzActivity.getControlInstance().closeDiaLog()
                JzActivity.getControlInstance().toast("使用藍芽需要定位權限")
            }

            override fun requestSuccess(a: String?) {
                Log.e("JzBleMessage_Success", a.toString())

            }
        },10)
    }

    override fun rx(a: BleBinary) {
        val sdf = SimpleDateFormat("HH:mm:ss:SSS")
        val past = sdf.format(Date())
        text.add("rx:" + a.readHEX())
        time.add(past.toString())
        type.add(2)
        handler.post {adapter.notifyDataSetChanged()
            rootview.recy.scrollToPosition(type.size-1)}
    }

    override fun scanBack(device: BluetoothDevice) {
        //當掃描到新裝置時觸發
        if (!devicelist.contains(device) && device.name != null) {
            devicelist.add(device)
            adscan.notifyDataSetChanged()
        }
        Log.d("JzBleMessage", "掃描到裝置:名稱${device.name}/地址:${device.address}")

        //當獲取到device.address即可儲存下來，藍牙連線時會使用到
    }

    override fun tx(b: BleBinary) {
        val sdf = SimpleDateFormat("HH:mm:ss:SSS")
        val past = sdf.format(Date())
        text.add("tx:" + b.readHEX())
        time.add(past.toString())
        type.add(1)
        handler.post {adapter.notifyDataSetChanged()
            rootview.recy.scrollToPosition(type.size-1)
            }
    }

    override fun viewInit() {

        rootview.recy.layoutManager = LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false)
        rootview.recy.adapter = adapter
        helper = BleHelper(act, this)

        rootview.button5.setOnClickListener {
            allmyitem.clear()
            alladapter.notifyDataSetChanged()

            JzActivity.getControlInstance().showDiaLog(R.layout.dialog_all_tx,false,false,object :
                SetupDialog(){
                override fun dismess() {

                }

                override fun keyevent(event: KeyEvent): Boolean {
                    return false
                }

                override fun setup(rootview: Dialog) {

                    rootview.ShowAllView.layoutManager = LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false)
                    rootview.ShowAllView.adapter = alladapter

                    rootview.cancel.setOnClickListener { JzActivity.getControlInstance().closeDiaLog("dialog_all_tx") }

                    allmyitem.add("震動馬達","0AE000030000F5")
                    allmyitem.add("LED閃爍","0AE100030000F5")
                    allmyitem.add("蜂鳴器","0AE200030000F5")
                    allmyitem.add("LF模擬","0AE300030000F5")

                    //Thread {
                    mMainActivity.TXdata.query(
                        "select * from TX order by id desc", Sql_Result {
                            val Name = it.getString(1)
                            val Txuuid = it.getString(2)

                            handler.post {
                                allmyitem.add(Name,Txuuid)
                                alladapter.notifyDataSetChanged()

                                Log.e("Txuuid",Txuuid)
                            }
                        })
                }
            },"dialog_all_tx")

        }
        rootview.button6.setOnClickListener {
            JzActivity.getControlInstance().showDiaLog(R.layout.add_layout,false,false,object :
            SetupDialog(){
                override fun dismess() {

                }

                override fun keyevent(event: KeyEvent): Boolean {
                    return true
                }

                override fun setup(rootview: Dialog) {
                    rootview.ok.setOnClickListener {

                        if(!rootview.name.text.toString().trim().equals("") && !rootview.txuuid.text.toString().trim().equals("")) {
                            mMainActivity.TXdata.exsql(
                                "insert into TX(name,Txuuid) " +
                                        "values ( '" + rootview.name.text.toString() + "','" + rootview.txuuid.text.toString() + "' )"
                            )
                            JzActivity.getControlInstance().closeDiaLog("add_layout")
                        }
                        else
                        {
                            JzActivity.getControlInstance().toast("資料不能為空")
                        }
                    }
                    rootview.cancel.setOnClickListener { JzActivity.getControlInstance().closeDiaLog("add_layout") }
                }
            },"add_layout")
        }
        rootview.button7.setOnClickListener {
            text.clear()
            time.clear()
            type.clear()
            adapter.notifyDataSetChanged()
        }
        rootview.button.setOnClickListener {
            helper.startScan()
            JzActivity.getControlInstance().showDiaLog(R.layout.activity_scan_ble, false, false,
                Da_Scan_ble(), "Da_Scan_ble")
        }
        rootview.button2.setOnClickListener {
            Thread{
                if(!rootview.name.text.isEmpty()){
                    helper.writeHex(rootview.name.text.toString(),rxUUID,TXUUID)
                }
            }.start()
        }
        rootview.button3.setOnClickListener {
            Thread{
                sendObdData(ArrayList(arrayOf(rootview.id1.text.toString(),rootview.id2.text.toString(),rootview.id3.text.toString(),rootview.id4.text.toString()).toList()))
            }.start()
        }
    }
    fun sendObdData(Id:ArrayList<String>){
        val tmpsend = java.util.ArrayList<String>()
        tmpsend.add("60A200FFFFFFFFC20A")
        var i = 0
        Log.e("setTireId","addcheckbyte$Id")
        val position= arrayOf("4","1","2","3","5")
        for (id in Id) {
            Log.e("setTireId","id${AddEmpty(id)}")
            tmpsend.add(addcheckbyte("60A20XidFF0A".replace("id", AddEmpty(id)).replace("X", position[i])))
            i++
        }
        tmpsend.add("60A2FFFFFFFFFF3D0A")
        for (a in tmpsend) {
            helper.writeHex("60A200FFFFFFFFC20A",rxUUID,TXUUID)
            Thread.sleep(50)
        }
    }
    fun AddEmpty(a: String): String {
        var temp=a
        while (temp.length<8){
            temp="0$temp"
        }
        return temp
    }

    fun addcheckbyte(com: String): String {
        val a = StringHexToByte(com)
        var checkbyte = a[0]
        for (i in 1 until a.size - 2) {
            checkbyte = checkbyte xor a[i]
        }
        a[a.size - 2] = checkbyte
        return bytesToHex(a)
    }
}
