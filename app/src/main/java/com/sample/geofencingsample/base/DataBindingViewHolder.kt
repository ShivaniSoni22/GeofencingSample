package com.sample.geofencingsample.base

import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.it_reminder.view.*

/**
 * View Holder for the Recycler View to bind the data item to the UI
 */
class DataBindingViewHolder<T>(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

//    var removedPosition : Int ? = null

    fun bind(position: Int, item: T, listener: ((item: T) -> Unit)?) {
//        itemView.img_btn_delete.setOnClickListener {
//            if (listener != null) {
//                item.removeAt(position)
//                removedPosition = position
//                notifyDataSetChanged()
//                listener(item)
//            }
//        }
        binding.setVariable(BR.item , item)
        binding.executePendingBindings()
    }
}