package com.earl.treesnavigation.domain.models

import com.earl.treesnavigation.domain.mappers.ChildNodeToDbMapper
import com.earl.treesnavigation.presentation.utils.Same

data class ChildNode(
    var name: String = "",
    val level: Int,
    val parent: String,
    val childsNames: MutableList<String>,
    val color: Int
) : Same<ChildNode> {
    override fun same(value: ChildNode) = value == this

    fun <T> mapToDb(mapper: ChildNodeToDbMapper<T>) =
        mapper.map(name, level, parent, childsNames, color)
}
