package com.orango.electronic.engineerble;

import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.util.Log;

 class daqiAdvertiseCallback extends AdvertiseCallback {
    //開啟廣播成功回調
    @Override
    public void onStartSuccess(AdvertiseSettings settingsInEffect){
        super.onStartSuccess(settingsInEffect);
        Log.d("daqi","開啟服務成功");
    }
    //無法啟動廣播回調。
    @Override
    public void onStartFailure(int errorCode) {
        super.onStartFailure(errorCode);
        Log.d("daqi","開啟服務失敗，失敗碼 = " + errorCode);
    }
}