package com.earl.treesnavigation.data.localDataSource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.earl.treesnavigation.data.localDataSource.enteties.NodeDb

@Dao
interface NodesDao {

    @Insert
    suspend fun insertNode(nodeDb: NodeDb)

    @Query("select * from NodesTable")
    suspend fun fetchAllNodes() : List<NodeDb>

    @Query("delete from NodesTable where name = :nodeName")
    suspend fun deleteNode(nodeName: String)

    @Query("select * from NodesTable where name = :nodeName")
    suspend fun fetchNodeByName(nodeName: String) : NodeDb

    @Query("update NodesTable set childsNames = :newChildsList where name = :nodeParent")
    suspend fun updateNodeChildsList(nodeParent: String, newChildsList: List<String>)

    @Transaction
    suspend fun addNewChildForNode(nodeName: String, newChildNodeName: String) {
        val oldNodeChildList = fetchNodeByName(nodeName).childsNames
        val newNodeChildsList = mutableListOf<String>().apply {
            addAll(oldNodeChildList)
            add(newChildNodeName)
        }
        updateNodeChildsList(nodeName, newNodeChildsList)
    }
 }