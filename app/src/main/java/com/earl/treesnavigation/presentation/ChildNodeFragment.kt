package com.earl.treesnavigation.presentation

import android.os.Bundle
import android.util.Log
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ChildNodeFragment : BaseFragment<FragmentChildBinding>(), OnChildClickListener {

    override fun viewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentChildBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nodeName = getNodeName()
        initRecyclerAdapter(nodeName)
        binding.numberInBackstack.text = String.format(requireContext().getString(R.string.level_s), viewModel.childs.value.find { it.name == nodeName }?.level)
        binding.nodeName.text = String.format(requireContext().getString(R.string.child_name), nodeName)
        binding.rootLayout.setBackgroundColor(viewModel.childs.value.find { it.name == nodeName }?.color!!)
        binding.addChild.setOnClickListener {
            addChild(nodeName)
        }
        binding.returnToRootBtn.setOnClickListener {
            returnToRoot(nodeName)
        }
    }

    override fun onResume() {
        super.onResume()
        if (getNodeName() != getNeedToShowFragmentName()) {
            navigate(getNeedToShowFragmentName(), getNodeName())
        }
        updateCurrentNodeParent(getNodeName())
    }

    private fun initRecyclerAdapter(nodeName: String) {
        val adapter = ChildsRecyclerViewAdapter(this)
        binding.childsRecycler.adapter = adapter
        viewModel.childs.onEach { list ->
            adapter.submitList(list.filter { it.parent == nodeName })
        }.launchIn(lifecycleScope)
    }

    override fun onChildNavigateClick(childNode: ChildNode) {
        navigate(childNode.name, getNodeName())
    }

    override fun onChildRemoveClick(childName: ChildNode) {
        viewModel.removeChild(childName)
    }

    override fun onShowChildsBtnClick(childNode: ChildNode, newList: (List<ChildNode>) -> Unit) {
        val list = viewModel.childs.value.filter { it.parent == childNode.name }
        newList(list)
    }

    override fun onHideChildsBtnClick(childNode: ChildNode, list: (List<ChildNode>) -> Unit) {
        viewModel.findAllChildsOfNode(childNode) { readyList ->
            list(readyList)
        }
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