package com.earl.treesnavigation.domain

import com.earl.treesnavigation.domain.models.ChildNode
import javax.inject.Inject

interface Interactor {

    suspend fun insertNodeIntoDb(node: ChildNode)

    suspend fun fetchAllNodesFromLocalDb() : List<ChildNode>

    suspend fun deleteNodeFromLocalDb(nodeName: String)

    suspend fun updateChildsListForNode(nodeParent: String, nodeChild: String)

    class Base @Inject constructor(
        private val repository: Repository
    ) : Interactor {

        override suspend fun insertNodeIntoDb(node: ChildNode) {
            repository.insertNodeIntoDb(node)
        }

        override suspend fun fetchAllNodesFromLocalDb() =
            repository.fetchAllNodesFromLocalDb()

        override suspend fun deleteNodeFromLocalDb(nodeName: String) {
            repository.deleteNodeFromLocalDb(nodeName)
        }

        override suspend fun updateChildsListForNode(nodeParent: String, nodeChild: String) {
            repository.updateChildsListForNode(nodeParent, nodeChild)
        }
    }
}