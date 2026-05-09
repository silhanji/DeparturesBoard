package dev.kluci_jak_buci.departuresboard.data.local.db

import kotlin.math.min

/**
 * Calculates Levenshtein distance between two strings.
 * Optimized for progressive search by limiting haystack to same size as needle
 */
fun progressiveLevenshtein(haystack: String, needle: String): Int {
    val trimmedHaystack = haystack.substring(
        0, min(haystack.length, needle.length)
    )
    return levenshtein(trimmedHaystack, needle)
}

/**
 * Calculates Levenshtein distance between two strings.
 */
fun levenshtein(s1: String, s2: String): Int {
    // Calculation of Levenshtein distance through dynamic programming
    //
    // Dist matrix at [i, j] will contain distance between first i characters of s1 and first
    // j characters of s2
    val dist = Array(s1.length + 1) { Array(s2.length + 1) { 0 }}

    for(i in 1 until s1.length + 1) {
        dist[i][0] = i
    }
    for(j in 1 until s2.length + 1) {
        dist[0][j] = j
    }

    for(i in 1 until s1.length + 1) {
        for(j in 1 until s2.length + 1) {
            if(s1[i - 1] == s2[j - 1]) {
                dist[i][j] = dist[i - 1][j - 1]
                continue
            }

            dist[i][j] = minOf(
                dist[i - 1][j] + 1,
                dist[i][j - 1] + 1,
                dist[i - 1][j - 1] + 1
            )
        }
    }

    return dist[s1.length][s2.length]
}