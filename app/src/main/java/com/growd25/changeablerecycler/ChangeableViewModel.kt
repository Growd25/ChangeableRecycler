package com.growd25.changeablerecycler

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class ChangeableViewModel : ViewModel() {
    private val disposable: Disposable
    private val deletedItems: MutableList<Item> = mutableListOf()
    private val currentList: MutableList<Item>
    private var item: Item
    private val _items: MutableLiveData<MutableList<Item>> = MutableLiveData()
    val items: LiveData<MutableList<Item>> = _items

    init {
        currentList = List(15) { index -> Item(index, index) }.toMutableList()
        item = currentList.last()
        _items.value = ArrayList(currentList)
        disposable = Observable
            .interval(5, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::addItem)
    }

    private fun addItem(number: Long) {
        if (deletedItems.isEmpty()) {
            val newItem = item.copy(itemId = item.itemId + 1, itemNumber = item.itemNumber + 1)
            currentList.add(newItem)
            this.item = newItem
            _items.value = ArrayList(currentList)
        } else {
            val lastItem = deletedItems.last()
            currentList.add(lastItem)
            deletedItems.remove(lastItem)
            _items.value = ArrayList(currentList)
        }
    }

    fun onItemClicked(item: Item) {
        currentList.remove(item)
        deletedItems.add(item)
        _items.value = ArrayList(currentList)
    }

    override fun onCleared() {
        disposable.dispose()
    }
}
