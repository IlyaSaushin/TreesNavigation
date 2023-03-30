package com.earl.treesnavigation.data.localDataSource.enteties

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.earl.treesnavigation.data.mappers.NodeDbToMainMapper

@Entity(tableName = "NodesTable")
data class NodeDb(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "level") val level: Int,
    @ColumnInfo(name = "parent") val parent: String,
    @ColumnInfo(name = "childsNames") val childsNames: List<String>,
    @ColumnInfo(name = "color") val color: Int
) {
    fun <T> map(mapper: NodeDbToMainMapper<T>) =
        mapper.map(name, level, parent, childsNames.toMutableList(), color)
}
