package com.earl.treesnavigation.presentation.utils

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.earl.treesnavigation.presentation.ChildNodeFragment
import com.earl.treesnavigation.presentation.MainViewModel
import com.earl.treesnavigation.R
import com.earl.treesnavigation.domain.models.ChildNode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

abstract class BaseFragment<VB: ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        _binding = viewBinding(inflater, container)
        return binding.root
    }

    abstract fun viewBinding(inflater: LayoutInflater, container: ViewGroup?) : VB

    private fun generateRandomColor() =
        Color.argb(255, Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))

    protected fun navigate(needToShowChildName: String, currentFragment: String) {
        val needToShow = viewModel.childs.value.find { it.name == needToShowChildName }
        if (needToShow?.parent == currentFragment) {
            lifecycleScope.launch(Dispatchers.Main) {
                delay(500)
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.root_container,
                        getFragmentState(needToShowChildName, needToShowChildName)
                    )
                    .addToBackStack(null)
                    .commit()
            }
        } else {
            val current = viewModel.childs.value.find { it.name == currentFragment }
            val currentBackStackNumber = if (currentFragment == Nodes.root) 0 else current?.level
            var parentOfParent = needToShowChildName
            for (i in needToShow?.level!! - 1 downTo currentBackStackNumber!! + 1) {
                val parent = viewModel.childs.value.filter { it.level == i }
                    .find { it.childsNames.contains(parentOfParent) }
                parentOfParent = parent?.name!!
                Log.d("tag", "navigate: ${viewModel.childs.value} ")
            }
            lifecycleScope.launch(Dispatchers.Main) {
                delay(500)
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.root_container,
                        getFragmentState(parentOfParent, needToShowChildName)
                    )
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    protected fun returnToRoot(currentFragment: String) {
        val currentNodeLevel = viewModel.childs.value.find { it.name == currentFragment }?.level
//        val needToShow = viewModel.childs.value.find { it.name == needToShowChildName }
//        if (needToShow?.parent == currentFragment) {
//            lifecycleScope.launch(Dispatchers.Main) {
//                delay(500)
//                parentFragmentManager.beginTransaction()
//                    .replace(
//                        R.id.root_container,
//                        getFragmentState(needToShowChildName, needToShowChildName)
//                    )
//                    .addToBackStack(null)
//                    .commit()
//            }
//        } else {
//            val current = viewModel.childs.value.find { it.name == currentFragment }
//            val currentBackStackNumber = if (currentFragment == Nodes.root) 0 else current?.numberInBackstack
//            var parentOfParent = needToShowChildName
//            for (i in needToShow?.numberInBackstack!! - 1 downTo currentBackStackNumber!! + 1) {
//                val parent = viewModel.childs.value.filter { it.numberInBackstack == i }
//                    .find { it.childsNames.contains(parentOfParent) }
//                parentOfParent = parent?.name!!
//            }
//            lifecycleScope.launch(Dispatchers.Main) {
//                delay(500)
//                parentFragmentManager.beginTransaction()
//                    .replace(
//                        R.id.root_container,
//                        getFragmentState(parentOfParent, needToShowChildName)
//                    )
//                    .addToBackStack(null)
//                    .commit()
//            }
//        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}