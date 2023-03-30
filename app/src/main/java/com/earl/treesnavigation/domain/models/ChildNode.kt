package com.earl.treesnavigation.domain.models

import com.earl.treesnavigation.presentation.utils.Same

data class ChildNode(
    var name: String = "",
    val level: Int,
    val parent: String,
    val childsNames: MutableList<String>,
    val color: Int
) : Same<ChildNode> {
    override fun same(value: ChildNode) = value == this
}
