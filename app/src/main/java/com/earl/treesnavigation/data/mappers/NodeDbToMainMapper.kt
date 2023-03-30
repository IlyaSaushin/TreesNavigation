package com.earl.treesnavigation.data.mappers

interface NodeDbToMainMapper<T> {

    fun map(
        name: String = "",
        level: Int,
        parent: String,
        childsNames: MutableList<String>,
        color: Int
    ) : T
}