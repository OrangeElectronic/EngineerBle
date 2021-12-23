package com.orango.electronic.engineerble.Brocadcast

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import android.os.Build
import android.os.ParcelUuid
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.PackageManagerCompat.LOG_TAG
import com.orange.jzchi.jzframework.JzActivity
import java.util.*


class Ble_Brocadcast {
    var UUID_SERVICE: UUID = UUID.fromString("0000fff7-0000-1000-8000-00805f9b34fb")
//
//    //設置廣播模式，以控制廣播的功率和延遲。
    var mAdvertiseSettings: AdvertiseSettings =
        //初始化廣播設置
        AdvertiseSettings.Builder().setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            //設置廣播模式，以控制廣播的功率和延遲。
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
            //不得超過180000毫秒。值為0將禁用時間限制。
            .setTimeout(0)
            //設置是否可以連接
            .setConnectable(false).build()

//初始化廣播包
    var mAdvertiseData = AdvertiseData.Builder()
    //設定廣播裝置名稱
    .setIncludeDeviceName(true)
    //設定發射功率級別
    .setIncludeDeviceName(true)
    .build();

    //    //初始化掃描響應包
    var mScanResponseData = AdvertiseData.Builder()
//隱藏廣播設備名稱
        .setIncludeDeviceName(true)
//設置廣播的服務UUID
        .addServiceUuid(ParcelUuid(UUID_SERVICE))
//設置廠商數據
        .addManufacturerData(0x11, "12345678".hexToByte())
        .build();

    fun start(){
        val bluetoothManager = JzActivity.getControlInstance().getRootActivity()
            .getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager;
        val mBluetoothAdapter = bluetoothManager.adapter;
//設置設備藍牙名稱
        mBluetoothAdapter.name = "daqi";
        val mBluetoothLeAdvertiser = mBluetoothAdapter.bluetoothLeAdvertiser;
        if(mBluetoothAdapter.isEnabled){
            if (mBluetoothLeAdvertiser != null){
//開啟廣播
                mBluetoothLeAdvertiser.startAdvertising(mAdvertiseSettings,
                    mAdvertiseData, mScanResponseData, object : AdvertiseCallback(){
                        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
                            super.onStartSuccess(settingsInEffect)
                            Log.d("daqi", "開啟服務成功")
                        }
                        override fun onStartFailure(code:Int){
                            super.onStartFailure(code)
                            Log.d("daqi", "開啟服務失敗，失敗碼 = $code")
                        }
                    });
            }else {
                Log.d("daqi","該手機不支持ble廣播");
            }
        }else{
            Log.d("daqi","手機藍牙未開啟");
        }
    }
}
//將HexString轉換成Byte
fun String.hexToByte(): ByteArray {
    val bytes = ByteArray(this.length / 2)
    for (i in 0 until this.length / 2)
        bytes[i] = Integer.parseInt(this.toString().substring(2 * i, 2 * i + 2), 16).toByte()
    return bytes
}