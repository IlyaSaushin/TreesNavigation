package com.earl.treesnavigation.data.localDataSource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.earl.treesnavigation.data.localDataSource.enteties.NodeDb

@Dao
interface NodesDao {

    @Insert
    suspend fun insertNode(nodeDb: NodeDb)

    @Query("select * from NodesTable")
    suspend fun fetchAllNodes() : List<NodeDb>

    @Query("delete from NodesTable where name = :nodeName")
    suspend fun deleteNode(nodeName: String)
}