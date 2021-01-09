package com.growd25.changeablerecycler

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class ChangeableViewModel : ViewModel() {
    private val disposable: Disposable
    private val random: Random = Random()
    private val deletedItems: MutableList<Item> = mutableListOf()
    private val currentList: MutableList<Item>
    private var lastIndex: Int
    private val _items: MutableLiveData<List<Item>> = MutableLiveData()
    val items: LiveData<List<Item>> = _items

    init {
        currentList = MutableList(INITIAL_LIST_SIZE) { index -> Item(index) }
        lastIndex = currentList.last().itemId
        _items.value = ArrayList(currentList)
        disposable = Observable
            .interval(INTERVAL_PERIOD, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::addItem)
    }

    private fun addItem(number: Long) {
        if (deletedItems.isEmpty()) {
            val position = random.nextInt(currentList.size)
            lastIndex++
            val newItem = Item(lastIndex)
            currentList.add(position, newItem)
        } else {
            val lastItem = deletedItems.last()
            currentList.add(lastItem)
            deletedItems.removeLast()
        }
        _items.value = ArrayList(currentList)
    }

    fun onItemClicked(item: Item) {
        currentList.remove(item)
        deletedItems.add(item)
        _items.value = ArrayList(currentList)
    }

    override fun onCleared() {
        disposable.dispose()
    }

    companion object {
        private const val INITIAL_LIST_SIZE = 15
        private const val INTERVAL_PERIOD = 5L
    }
}
