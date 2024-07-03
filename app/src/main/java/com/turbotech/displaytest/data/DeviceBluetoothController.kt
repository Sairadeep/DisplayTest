package com.turbotech.displaytest.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import com.turbotech.displaytest.data.domain.BluetoothController
import com.turbotech.displaytest.data.domain.BluetoothDeviceDomain
import com.turbotech.displaytest.data.domain.DeviceFoundReceiver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@SuppressLint("MissingPermission")
class DeviceBluetoothController(private val context: Context) : BluetoothController {

    private val bluetoothManager by lazy {
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager.adapter
    }

    private val _scannedList = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    override val scannedList: StateFlow<List<BluetoothDeviceDomain>>
        get() = _scannedList.asStateFlow()

    private val _pairedList = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    override val pairedList: StateFlow<List<BluetoothDeviceDomain>>
        get() = _pairedList.asStateFlow()

    private val foundDeviceReceiver = DeviceFoundReceiver { device ->
        _scannedList.update { devices ->
            val newDevice = device.toBluetoothDeviceDomain()
            Log.d("newDevice", "$newDevice")
            if (newDevice in devices) devices else devices + newDevice
        }
    }

    init {
        updatePairedDevices()
    }

    override fun startScan() {
        if (bluetoothAdapter.isEnabled) {
            if (!hasPermission(android.Manifest.permission.BLUETOOTH_SCAN)) {
                Log.d("DeviceBluetoothController", "startScan: No BLUETOOTH_SCAN permission")
                return
            }
            Toast.makeText(context, "Scanning...", Toast.LENGTH_SHORT).show()
            context.registerReceiver(
                foundDeviceReceiver,
                IntentFilter(BluetoothDevice.ACTION_FOUND)
            )
            updatePairedDevices()
            bluetoothAdapter.startDiscovery()
        } else {
            Log.d("DeviceBluetoothController", "startScan: Bluetooth is disabled")
        }
    }

    override fun stopScan() {
        if (!hasPermission(android.Manifest.permission.BLUETOOTH_SCAN)) {
            Log.d("DeviceBluetoothController", "startScan: No BLUETOOTH_SCAN permission")
            return
        }
        bluetoothAdapter.cancelDiscovery()
    }

    override fun release() {
        context.unregisterReceiver(foundDeviceReceiver)
    }

    private fun updatePairedDevices() {
        if (!hasPermission(android.Manifest.permission.BLUETOOTH_CONNECT)) {
            Log.d("DeviceBluetoothController", "startScan: No BLUETOOTH_CONNECT permission")
            return
        }
//        bondedDevices => Paired ones
        bluetoothAdapter.bondedDevices?.map { it.toBluetoothDeviceDomain() }?.also { devices ->
            _pairedList.update { devices }
        }
    }

    private fun hasPermission(permission: String): Boolean {
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }
}