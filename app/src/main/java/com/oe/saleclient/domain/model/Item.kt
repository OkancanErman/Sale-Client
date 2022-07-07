package com.oe.saleclient.domain.model

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Item(
    val itemName: String,
    val itemPrice: Double,
    val itemVat: Int,
    val itemBarcode: String,
    val timestamp: Long,
    val id: String = UUID.randomUUID().toString()
)
