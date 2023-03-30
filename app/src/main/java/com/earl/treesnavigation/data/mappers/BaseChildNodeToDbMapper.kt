package com.earl.treesnavigation.data.mappers

import com.earl.treesnavigation.data.localDataSource.enteties.NodeDb
import com.earl.treesnavigation.domain.mappers.ChildNodeToDbMapper
import javax.inject.Inject

class BaseChildNodeToDbMapper @Inject constructor() : ChildNodeToDbMapper<NodeDb> {

    override fun map(
        name: String,
        level: Int,
        parent: String,
        childsNames: MutableList<String>,
        color: Int
    ) = NodeDb(
        0,
        name,
        level,
        parent,
        childsNames,
        color
    )
}