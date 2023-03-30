package com.earl.treesnavigation.domain

import com.earl.treesnavigation.domain.models.ChildNode

interface Repository {

    suspend fun insertNodeIntoDb(node: ChildNode)

    suspend fun fetchAllNodesFromLocalDb() : List<ChildNode>

    suspend fun deleteNodeFromLocalDb(nodeName: String)

    suspend fun updateChildsListForNode(nodeParent: String, nodeChild: String)
}