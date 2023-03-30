package com.earl.treesnavigation.data

import com.earl.treesnavigation.data.localDataSource.NodesDao
import com.earl.treesnavigation.data.localDataSource.enteties.NodeDb
import com.earl.treesnavigation.data.mappers.NodeDbToMainMapper
import com.earl.treesnavigation.domain.Repository
import com.earl.treesnavigation.domain.mappers.ChildNodeToDbMapper
import com.earl.treesnavigation.domain.models.ChildNode
import javax.inject.Inject

class BaseRepository @Inject constructor(
    private val nodesDao: NodesDao,
    private val childNodeToDbMapper: ChildNodeToDbMapper<NodeDb>,
    private val nodeDbToMainMapper: NodeDbToMainMapper<ChildNode>
): Repository {

    override suspend fun insertNodeIntoDb(node: ChildNode) {
        nodesDao.insertNode(node.mapToDb(childNodeToDbMapper))
    }

    override suspend fun fetchAllNodesFromLocalDb() =
        nodesDao.fetchAllNodes().map { it.map(nodeDbToMainMapper) }

    override suspend fun deleteNodeFromLocalDb(nodeName: String) {
        nodesDao.deleteNodeAndItsChildFromDb(nodeName)
    }

    override suspend fun updateChildsListForNode(nodeParent: String, nodeChild: String) {
        nodesDao.addNewChildForNode(nodeParent, nodeChild)
    }
}