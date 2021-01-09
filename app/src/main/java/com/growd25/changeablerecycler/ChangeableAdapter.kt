package com.growd25.changeablerecycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.growd25.changeablerecycler.databinding.ChangeableItemBinding

class ChangeableAdapter(private val deleteImageViewListener: (Item) -> Unit) :
    ListAdapter<Item, ChangeableAdapter.ChangeableViewHolder>(ChangeableDiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChangeableViewHolder {
        return ChangeableViewHolder(
            changeableItemBinding = ChangeableItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            deleteImageViewListener = deleteImageViewListener
        )
    }

    override fun onBindViewHolder(holder: ChangeableViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class ChangeableViewHolder(
        private val changeableItemBinding: ChangeableItemBinding,
        deleteImageViewListener: (Item) -> Unit
    ) : RecyclerView.ViewHolder(changeableItemBinding.root) {
        private lateinit var item: Item

        init {
            changeableItemBinding.deleteImageView.setOnClickListener { deleteImageViewListener.invoke(item) }
        }

        fun bind(item: Item) {
            this.item = item
            changeableItemBinding.numberTextView.text = item.itemId.toString()
        }
    }

    private object ChangeableDiffCallBack : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.itemId == newItem.itemId
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }
}
