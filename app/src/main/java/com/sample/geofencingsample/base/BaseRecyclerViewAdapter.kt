package com.sample.geofencingsample.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.it_reminder.view.*
import kotlinx.coroutines.DelicateCoroutinesApi

abstract class BaseRecyclerViewAdapter<T>(private val callback: ((item: T) -> Unit)? = null) :
    RecyclerView.Adapter<DataBindingViewHolder<T>>() {

    private var _items: MutableList<T> = mutableListOf()

    /**
     * Returns the _items data
     */
    private val items: List<T>
        get() = this._items

    override fun getItemCount() = _items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil
            .inflate<ViewDataBinding>(layoutInflater, getLayoutRes(viewType), parent, false)
        binding.lifecycleOwner = getLifecycleOwner()
        return DataBindingViewHolder(binding)
    }

    @DelicateCoroutinesApi
    override fun onBindViewHolder(holder: DataBindingViewHolder<T>, position: Int) {
        val item = getItem(position)
        holder.bind(position, item, callback)
        holder.itemView.img_btn_delete.setOnClickListener {
            callback?.invoke(item)
            removeItem(position)
        }
    }

    private fun removeItem(position: Int) {
//        if (position == _items.size - 1) { // if last element is deleted, no need to shift
//            _items.removeAt(position)
//            notifyItemRemoved(position)
//        } else { // if the element deleted is not the last one
//            var shift =
//                1 // not zero, shift=0 is the case where position == dataList.size() - 1, which is already checked above
//            while (true) {
//                try {
//                    _items.removeAt(position - shift)
//                    notifyItemRemoved(position)
//                    break
//                } catch (e: IndexOutOfBoundsException) { // if fails, increment the shift and try again
//                    shift++
//                }
//            }
//        }

//        if(_items.isNotEmpty() && _items.size > position) {
            _items.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
//        }
//        if(_items.size == 0){
//            _items.clear()
//        }
    }

    private fun getItem(position: Int) = _items[position]

    /**
     * Adds data to the actual Dataset
     *
     * @param items to be merged
     */
    fun addData(items: List<T>) {
        _items.addAll(items)
        notifyDataSetChanged()
    }

    /**
     * Clears the _items data
     */
    fun clear() {
        _items.clear()
        notifyDataSetChanged()
    }

    @LayoutRes
    abstract fun getLayoutRes(viewType: Int): Int

    open fun getLifecycleOwner(): LifecycleOwner? {
        return null
    }
}