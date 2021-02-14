package com.theapache64.stackzy.ui.feature.selectdevice

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.data.local.AndroidDevice
import com.theapache64.stackzy.ui.common.AlphabetCircle
import com.theapache64.stackzy.ui.common.ContentScreen
import com.theapache64.stackzy.ui.common.FullScreenError
import com.theapache64.stackzy.ui.common.Selectable
import com.theapache64.stackzy.util.R

@Composable
fun SelectDeviceScreen(
    selectDeviceViewModel: SelectDeviceViewModel,
    onDeviceSelected: (AndroidDevice) -> Unit
) {
    val devices by selectDeviceViewModel.connectedDevices.collectAsState()

    Content(
        devices = devices,
        onDeviceSelected = onDeviceSelected
    )
}

@Composable
fun Content(
    devices: List<AndroidDevice>?,
    onDeviceSelected: (AndroidDevice) -> Unit
) {
    if (devices == null) {
        // Just background
        Box(
            modifier = Modifier.fillMaxSize()
        )
        return
    }

    if (devices.isEmpty()) {
        FullScreenError(
            title = R.string.device_no_device_title,
            message = R.string.device_no_device_message,
        )
    } else {
        ContentScreen(
            title = R.string.device_select_the_device
        ) {
            LazyColumn {
                items(devices) { device ->
                    Selectable(
                        data = device,
                        modifier = Modifier
                            .width(400.dp),
                        onSelected = onDeviceSelected
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )
                }
            }
        }

    }
}





