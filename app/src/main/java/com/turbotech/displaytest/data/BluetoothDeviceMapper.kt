package com.turbotech.displaytest.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.turbotech.displaytest.data.domain.BluetoothDeviceDomain

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceDomain {
    return BluetoothDeviceDomain(
        name = name,
        address = address
    )
}