package com.oe.saleclient.ui.viewmodels

import android.widget.EditText
import android.widget.TextView
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.*
import com.oe.saleclient.data.remote.SaleService
import com.oe.saleclient.domain.model.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val saleService: SaleService
) : ViewModel() {

    private val eventChannel = Channel<Status>()
    val eventFlow = eventChannel.receiveAsFlow()

    private val eventControlChannel = Channel<Status>()
    val eventControlFlow = eventControlChannel.receiveAsFlow()

    private val eventAddChannel = Channel<Status>()
    val eventAddFlow = eventAddChannel.receiveAsFlow()

    val isLoading = ObservableBoolean()

    private val _currentItem = MutableLiveData<Item>()
    val currentItem: LiveData<Item>
        get() = _currentItem

    private val _items = MutableLiveData<MutableList<Item>>()
    val items: LiveData<MutableList<Item>>
        get() = _items

    init{
        getAllItems()
    }

    fun onRefresh() {
        isLoading.set(true)
        getAllItems()
    }

    fun getAllItems() {
        viewModelScope.launch {
            eventChannel.send(Status.Loading(null))
            try {
                _items.value = saleService.getAllItems()
                eventChannel.send(Status.Success(null))
                isLoading.set(false)
            }catch (throwable: Throwable){
                withContext(Dispatchers.Main) {
                    eventChannel.send(Status.Error(throwable.toString()))
                    isLoading.set(false)
                }
            }
        }
    }

    fun setItem(index: Int) {
        _currentItem.value = _items.value?.get(index)
    }

    fun addItem(itemName: EditText, itemPrice: EditText, itemVat: EditText, itemBarcode: EditText ) {
        viewModelScope.launch {
            eventAddChannel.send(Status.Loading(null))
            try {
                val item = Item(
                    itemName.text.toString(), itemPrice.text.toString().toDouble(), itemVat.text.toString().toInt(),
                    itemBarcode.text.toString(), System.currentTimeMillis()
                )
                saleService.addItem(item)
                eventAddChannel.send(Status.Success(null))
                _items.value?.add(item)

            } catch (throwable: Throwable) {
                eventAddChannel.send(Status.Error(throwable.toString()))
            }
        }
    }

    fun updateItem(itemName: EditText, itemPrice: EditText, itemVat: EditText, itemBarcode: EditText, itemID: TextView) {
        viewModelScope.launch {
            eventControlChannel.send(Status.Loading(null))
            try {
                val item = Item(
                    itemName.text.toString(), itemPrice.text.toString().toDouble(), itemVat.text.toString().toInt(),
                    itemBarcode.text.toString(), System.currentTimeMillis(), itemID.text.toString()
                )
                saleService.updateItem(item)
                eventControlChannel.send(Status.Success(null))
                _items.value?.set(_items.value?.indexOf(_items.value?.find { it.id == itemID.text.toString() })!!, item)

            } catch (throwable: Throwable) {
                eventControlChannel.send(Status.Error(throwable.toString()))
            }
        }
    }

    fun deleteItem(itemID: TextView) {
        viewModelScope.launch {
            eventControlChannel.send(Status.Loading(null))
            try {
                saleService.deleteItem(itemID.text.toString())
                eventControlChannel.send(Status.Success(null))
                _items.value?.remove(_items.value?.find { it.id == itemID.text.toString() })

            } catch (throwable: Throwable) {
                eventControlChannel.send(Status.Error(throwable.toString()))
            }
        }
    }

    sealed class Status {
        data class Loading(val message: String?) : Status()
        data class Success(val message: String?) : Status()
        data class Error(val message: String?) : Status()
    }
}