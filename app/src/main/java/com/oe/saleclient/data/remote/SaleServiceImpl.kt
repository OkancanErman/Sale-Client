package com.oe.saleclient.data.remote

import com.oe.saleclient.domain.model.Item
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class SaleServiceImpl(
    private val client: HttpClient
): SaleService {

    override suspend fun getAllItems(): MutableList<Item> {
        return try {
            client.get<MutableList<Item>>(SaleService.Endpoints.GetAllItems.url)
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    override suspend fun addItem(item: Item) {
        client.post<HttpResponse>(SaleService.Endpoints.AddItem.url) {
            contentType(ContentType.Application.Json)
            body = item
        }
    }

    override suspend fun updateItem(item: Item) {
        client.post<HttpResponse>(SaleService.Endpoints.UpdateItem.url) {
            contentType(ContentType.Application.Json)
            body = item
        }
    }

    override suspend fun deleteItem(itemID: String) {
        client.post<HttpResponse>(SaleService.Endpoints.DeleteItem.url) {
            body = itemID
        }
    }



}