package com.earl.treesnavigation.presentation.utils

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.earl.treesnavigation.R
import com.earl.treesnavigation.domain.models.ChildNode
import com.earl.treesnavigation.presentation.ChildNodeFragment
import com.earl.treesnavigation.presentation.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

abstract class BaseFragment<VB: ViewBinding> : Fragment() {

    companion object {
        private var currentNodeParent = ""
    }

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        backPressedCallback()
        _binding = viewBinding(inflater, container)
        return binding.root
    }

    abstract fun viewBinding(inflater: LayoutInflater, container: ViewGroup?) : VB

    private fun generateRandomColor() =
        Color.argb(255, Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))

    protected fun navigate(needToShowChildName: String, currentFragment: String) {
        val needToShow = viewModel.childs.value.find { it.name == needToShowChildName }
        val current = viewModel.childs.value.find { it.name == currentFragment }
        currentNodeParent = needToShow?.parent ?: ""
        if (needToShow?.parent == currentFragment) {
            lifecycleScope.launch(Dispatchers.Main) {
                delay(300)
                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in, R.anim.slide_down)
                    .replace(
                        R.id.root_container,
                        getFragmentState(needToShowChildName, needToShowChildName).apply {
                            arguments = Bundle().apply {
                                putString(Nodes.nodeName, needToShowChildName)
                                putString(Nodes.needToShow, needToShowChildName)
                            }
                        },
                        needToShowChildName
                    )
                    .addToBackStack(null)
                    .commit()
            }
        } else {
            val currentBackStackNumber = if (currentFragment == Nodes.root) 0 else current?.level
            var parentOfParent = needToShowChildName
            for (i in needToShow?.level!! - 1 downTo currentBackStackNumber!! + 1) {
                val parent = viewModel.childs.value.filter { it.level == i }
                    .find { it.childsNames.contains(parentOfParent) }
                parentOfParent = parent?.name!!
            }
            lifecycleScope.launch(Dispatchers.Main) {
                delay(300)
                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in, R.anim.slide_down)
                    .replace(
                        R.id.root_container,
                        getFragmentState(parentOfParent, needToShowChildName).apply {
                            arguments = Bundle().apply {
                                putString(Nodes.nodeName, parentOfParent)
                                putString(Nodes.needToShow, needToShowChildName)
                            }
                        },
                        parentOfParent
                    )
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    protected fun returnToRoot(currentNode: String) {
        val currentNodeParent = viewModel.childs.value.find { it.name == currentNode }?.parent
        if (currentNodeParent != null) {
            val fragment = getFragmentState(currentNodeParent, currentNodeParent)
            fragment.apply {
                arguments = Bundle().apply {
                    putString(Nodes.nodeName, currentNodeParent)
                    putString(Nodes.needToShow, currentNodeParent)
                }
            }
            lifecycleScope.launch(Dispatchers.Main) {
                delay(300)
                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_back_down, R.anim.slide_back_in)
                    .replace(
                        R.id.root_container,
                        fragment,
                        currentNodeParent
                    )
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    private fun getFragmentState(name: String, needToShowChildName: String) =
        parentFragmentManager.findFragmentByTag(name) ?: ChildNodeFragment.newInstance(name, needToShowChildName)

    protected fun addChild(parent: String) {
        val newNode = ChildNode(
            name = "",
            level = parentFragmentManager.backStackEntryCount,
            parent = parent,
            childsNames = mutableListOf(),
            color = generateRandomColor()
        ).apply {
            this.name = NodeNameGenerator.generateName(this.hashCode())
        }
        viewModel.childs.value.find { it.name == parent }?.childsNames?.add(newNode.name)
        if (parent != Nodes.root) {
            viewModel.addChildForNodeInDb(parent, newNode.name)
        }
        viewModel.addChild(newNode)
    }

    protected fun updateCurrentNodeParent(currentNode: String) {
        currentNodeParent = viewModel.childs.value.find { it.name == currentNode }?.parent ?: ""
    }

    private fun backPressedCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (currentNodeParent != "") {
                    val fragment = getFragmentState(currentNodeParent, currentNodeParent)
                    fragment.apply {
                        arguments = Bundle().apply {
                            putString(Nodes.nodeName, currentNodeParent)
                            putString(Nodes.needToShow, currentNodeParent)
                        }
                        lifecycleScope.launch(Dispatchers.Main) {
                            delay(300)
                            parentFragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.slide_back_down, R.anim.slide_back_in)
                                .replace(
                                    R.id.root_container,
                                    fragment,
                                    currentNodeParent
                                )
                                .addToBackStack(null)
                                .commit()
                        }
                    }
                }
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}