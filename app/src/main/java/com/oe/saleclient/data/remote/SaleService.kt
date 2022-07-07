package com.oe.saleclient.data.remote

import com.oe.saleclient.domain.model.Item

interface SaleService {

    suspend fun getAllItems(): MutableList<Item>

    suspend fun addItem(item: Item)

    suspend fun updateItem(item: Item)

    suspend fun deleteItem(itemID: String)

    companion object {
        const val BASE_URL = "http://192.168.1.1:8080"
    }

    sealed class Endpoints(val url: String) {
        object GetAllItems: Endpoints("$BASE_URL/items")
        object AddItem: Endpoints("$BASE_URL/additem")
        object UpdateItem: Endpoints("$BASE_URL/updateitem")
        object DeleteItem: Endpoints("$BASE_URL/deleteitem")
    }
}