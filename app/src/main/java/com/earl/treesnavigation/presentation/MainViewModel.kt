package com.earl.treesnavigation.presentation

import androidx.lifecycle.ViewModel
import com.earl.treesnavigation.domain.models.ChildNode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _childs: MutableStateFlow<List<ChildNode>> = MutableStateFlow(emptyList())
    val childs : StateFlow<List<ChildNode>> = _childs.asStateFlow()

    fun addChild(child: ChildNode) {
        _childs.value += child
    }

    fun removeChild(child: ChildNode) {
        _childs.value -= child
    }

//    private val _childs: MutableStateFlow<List<Pair<String, MutableList<ChildNode>>>> = MutableStateFlow(emptyList())
//    val childs : StateFlow<List<Pair<String, MutableList<ChildNode>>>> = _childs.asStateFlow()
//
//    fun addChild(parent: String, child: ChildNode) {
//        val existedParent = _childs.value.find { it.first == parent }
//        if (existedParent == null) {
//            _childs.value += Pair(parent, mutableListOf(child))
//            Log.d("tag", "not existed: ${childs.value.flatMap { it.second }}")
//        } else {
//            existedParent.second.add(child)
//            Log.d("tag", "existed, new list = ${childs.value.flatMap { it.second }}")
//        }
//    }
//
//    fun removeChild(parent: String, child: ChildNode) {
//        _childs.value.find {
//            it.first == parent
//        }?.second?.remove(child)
//    }
}