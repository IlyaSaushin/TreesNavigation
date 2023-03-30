package com.earl.treesnavigation.data.mappers

import com.earl.treesnavigation.domain.models.ChildNode
import javax.inject.Inject

class BaseNodeDbToMainMapper @Inject constructor() : NodeDbToMainMapper<ChildNode> {

    override fun map(
        name: String,
        level: Int,
        parent: String,
        childsNames: MutableList<String>,
        color: Int
    ) = ChildNode(
        name,
        level,
        parent,
        childsNames,
        color
    )
}