package com.earl.treesnavigation.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.earl.treesnavigation.R
import com.earl.treesnavigation.databinding.FragmentChildBinding
import com.earl.treesnavigation.domain.models.ChildNode
import com.earl.treesnavigation.presentation.utils.BaseFragment
import com.earl.treesnavigation.presentation.utils.ChildsRecyclerViewAdapter
import com.earl.treesnavigation.presentation.utils.Nodes
import com.earl.treesnavigation.presentation.utils.OnChildClickListener
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ChildNodeFragment : BaseFragment<FragmentChildBinding>(), OnChildClickListener {

    override fun viewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentChildBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nodeName = getNodeName()
        initRecyclerAdapter()
        binding.numberInBackstack.text = (parentFragmentManager.backStackEntryCount - 1).toString()
        binding.nodeName.text = String.format(requireContext().getString(R.string.child_name), nodeName)
        binding.rootLayout.setBackgroundColor(viewModel.childs.value.find { it.name == nodeName }?.color!!)
        binding.addChild.setOnClickListener {
            addChild(nodeName)
        }
        binding.returnToRootBtn.setOnClickListener {
            navigate(Nodes.root, nodeName)
        }
    }

    override fun onResume() {
        super.onResume()
        if (getNodeName() != getNeedToShowFragmentName()) {
            navigate(getNeedToShowFragmentName(), getNodeName())
        }
    }

    private fun initRecyclerAdapter() {
        val adapter = ChildsRecyclerViewAdapter(this)
        binding.childsRecycler.adapter = adapter
        viewModel.childs.onEach { list ->
//            adapter.submitList(list.filter { it.parent == nodeName })
            adapter.submitList(list)
        }.launchIn(lifecycleScope)
    }

    override fun onChildClick(childNode: ChildNode) {
        navigate(childNode.name, getNodeName())
    }

    private fun getNodeName() = arguments?.getString(Nodes.nodeName) ?: ""

    private fun getNeedToShowFragmentName() = arguments?.getString(Nodes.needToShow) ?: ""

    companion object {
        fun newInstance(name: String, needToShow: String) = ChildNodeFragment().apply {
            arguments = Bundle().apply {
                putString(Nodes.nodeName, name)
                putString(Nodes.needToShow, needToShow)
            }
        }
    }
}