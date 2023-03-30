package com.earl.treesnavigation.presentation.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.view.marginStart
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earl.treesnavigation.R
import com.earl.treesnavigation.databinding.ChildRecyclerItemBinding
import com.earl.treesnavigation.domain.models.ChildNode

interface OnChildClickListener {
    fun onChildNavigateClick(childNode: ChildNode)
    fun onChildRemoveClick(childName: ChildNode)
    fun onShowChildsBtnClick(childNode: ChildNode, newList: (List<ChildNode>) -> Unit)
    fun onHideChildsBtnClick(childNode: ChildNode, list: (List<ChildNode>) -> Unit)
}

class ChildsRecyclerViewAdapter(
    private val clickListener: OnChildClickListener
) : ListAdapter<ChildNode, ChildsRecyclerViewAdapter.ItemViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ChildRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewHolder(private val binding: ChildRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChildNode) {
            val context = binding.childName.context
            binding.childName.text = String.format(context.getString(R.string.child_name), item.name)
            binding.numberInBackstack.text = String.format(context.getString(R.string.number_in_backstack_s), item.level)
            binding.layout.setBackgroundColor(item.color)
            binding.navigate.setOnClickListener {
                clickListener.onChildNavigateClick(item)
            }
            binding.remvoeBtn.setOnClickListener {
                clickListener.onChildRemoveClick(item)
            }
            binding.showChildsBtn.setOnClickListener {
                binding.showChildsBtn.visibility = View.GONE
                binding.hideChildsBtn.visibility = View.VISIBLE
                clickListener.onShowChildsBtnClick(item) {
                    val index = currentList.indexOf(item)
                    if (!item.isExpanded) {
                        val newList = mutableListOf<ChildNode>().apply {
                            addAll(currentList)
                            addAll(index + 1, it)
                        }
                        submitList(emptyList())
                        submitList(newList)
                        notifyDataSetChanged()
                        item.isExpanded = true
                    }
                }
            }
            binding.hideChildsBtn.setOnClickListener {
                binding.showChildsBtn.visibility = View.VISIBLE
                binding.hideChildsBtn.visibility = View.GONE
                clickListener.onHideChildsBtnClick(item) {
                    val newList = mutableListOf<ChildNode>().apply {
                        addAll(currentList)
                        removeAll(it)
                    }
                    submitList(emptyList())
                    submitList(newList)
                    notifyDataSetChanged()
                    item.isExpanded = false
                }
            }
        }
    }

    private companion object Diff : DiffUtil.ItemCallback<ChildNode>() {
        override fun areItemsTheSame(oldItem: ChildNode, newItem: ChildNode) = oldItem.same(newItem)
        override fun areContentsTheSame(oldItem: ChildNode, newItem: ChildNode) = oldItem.equals(newItem)
    }
}