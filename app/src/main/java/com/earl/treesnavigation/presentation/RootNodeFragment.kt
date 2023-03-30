package com.earl.treesnavigation.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.earl.treesnavigation.databinding.FragmentRootNodeBinding
import com.earl.treesnavigation.domain.models.ChildNode
import com.earl.treesnavigation.presentation.utils.BaseFragment
import com.earl.treesnavigation.presentation.utils.ChildsRecyclerViewAdapter
import com.earl.treesnavigation.presentation.utils.Nodes
import com.earl.treesnavigation.presentation.utils.OnChildClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RootNodeFragment : BaseFragment<FragmentRootNodeBinding>(), OnChildClickListener {

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRootNodeBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchAllNodesFromDb()
        initRecyclerAdapter()
        binding.addChild.setOnClickListener {
            addChild(Nodes.root)
        }
    }

    private fun initRecyclerAdapter() {
        val adapter = ChildsRecyclerViewAdapter(this)
        binding.childsRecycler.adapter = adapter
        viewModel.childs.onEach { list ->
//            adapter.submitList(list.filter { it.parent == Nodes.root })
            adapter.submitList(list)
        }.launchIn(lifecycleScope)
    }

    override fun onChildNavigateClick(childNode: ChildNode) {
        navigate(childNode.name, getNodeName())
    }

    override fun onChildRemoveClick(childName: ChildNode) {
        viewModel.removeChild(childName)
    }

    private fun getNodeName() = arguments?.getString(Nodes.nodeName) ?: ""

    companion object {
        fun newInstance(name: String) = RootNodeFragment().apply {
            arguments = Bundle().apply {
                putString(Nodes.nodeName, name)
            }
        }
    }
}