package com.turbotech.displaytest.data.domain

import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {

    val scannedList: StateFlow<List<BluetoothDevice>>
    val pairedList: StateFlow<List<BluetoothDevice>>

    fun startScan()

    fun stopScan()

    fun release()

}