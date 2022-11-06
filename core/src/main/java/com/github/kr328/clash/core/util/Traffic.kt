package com.github.kr328.clash.core.util

import com.github.kr328.clash.core.model.Traffic

fun Traffic.trafficUpload(): String {
    return trafficString(scaleTraffic(this ushr 32))
}

fun Traffic.trafficDownload(): String {
    return trafficString(scaleTraffic(this and 0xFFFFFFFF))
}

fun Traffic.trafficTotal(): String {
    val upload = scaleTraffic(this ushr 32)
    val download = scaleTraffic(this and 0xFFFFFFFF)

    return trafficString(upload + download)
}

private fun trafficString(scaled: Long): String {
    return when {
        scaled > 1024 * 1024 * 1024 * 1024L ->
            String.format("%.2f TiB", (scaled.toDouble() / 1024 / 1024 / 1024 / 1024))
        scaled > 1024 * 1024 * 1024 ->
            String.format("%.2f GiB", (scaled.toDouble() / 1024 / 1024 / 1024))
        scaled > 1024 * 1024 ->
            String.format("%.2f MiB", (scaled.toDouble() / 1024 / 1024))
        scaled > 1024 ->
            String.format("%.2f KiB", (scaled.toDouble() / 1024))
        else ->
            "$scaled Bytes"
    }
}

private fun scaleTraffic(value: Long): Long {
    val e = (value ushr 29) and 0x7
    val fraction = value and 0x1FFFFFFF

    return fraction shl (10 * e).toInt()
}