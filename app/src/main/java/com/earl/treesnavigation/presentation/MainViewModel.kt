package com.earl.treesnavigation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earl.treesnavigation.domain.Interactor
import com.earl.treesnavigation.domain.models.ChildNode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val interactor: Interactor
) : ViewModel() {

    private val _childs: MutableStateFlow<List<ChildNode>> = MutableStateFlow(emptyList())
    val childs : StateFlow<List<ChildNode>> = _childs.asStateFlow()

    fun fetchAllONodesFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            _childs.value = interactor.fetchAllNodesFromLocalDb()
        }
    }

    fun addChild(child: ChildNode) {
        _childs.value += child
        viewModelScope.launch(Dispatchers.IO) {
            interactor.insertNodeIntoDb(child)
        }
    }

    fun removeChild(child: ChildNode) {
        _childs.value -= child
        viewModelScope.launch(Dispatchers.IO) {
            interactor.deleteNodeFromLocalDb(child.name)
        }
    }
}