package com.earl.treesnavigation.domain.mappers

interface ChildNodeToDbMapper<T> {

    fun map(
        name: String = "",
        level: Int,
        parent: String,
        childsNames: MutableList<String>,
        color: Int
    ) : T
}