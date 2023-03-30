package com.earl.treesnavigation.presentation.utils

import java.security.MessageDigest

object NodeNameGenerator {

    fun generateName(hash: Int) : String {
        val nodeHash = MessageDigest.getInstance("SHA-256").digest(hash.numberToByteArray())
        val lastTwentyBytes = nodeHash.takeLast(20).toByteArray()
        return lastTwentyBytes.toString()
    }

    private fun Int.numberToByteArray(size: Int = 4): ByteArray =
        ByteArray (size) { i -> (this.toLong() shr (i * 8)).toByte() }
}