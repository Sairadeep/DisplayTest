package com.turbotech.displaytest.data.domain

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class DeviceFoundReceiver(
    private val onDeviceFound: (BluetoothDevice) -> Unit
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("DeviceFoundReceiver", "onReceive: Called")
        if (intent != null) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    Log.d("DeviceFoundReceiver", "Device Found..!")
                    val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra(
                            BluetoothDevice.EXTRA_DEVICE,
                            BluetoothDevice::class.java
                        )
                    } else {
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    }
                    device?.let(onDeviceFound)
                }

                else -> {
                    Log.d("DeviceFoundReceiver", "Another intent action.")
                }
            }
        } else {
            Log.d("DeviceFoundReceiver", "onReceive: Intent is empty")
        }
    }
}