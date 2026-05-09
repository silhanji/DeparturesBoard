package dev.kluci_jak_buci.departuresboard

import dev.kluci_jak_buci.departuresboard.data.local.db.levenshtein
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class LevenshteinDistanceUnitTest(
    private val s1: String,
    private val s2: String,
    private val expectedDistance: Int
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: levenshtein({0}, {1}) = {2}")
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf("kitten", "sitting", 3),
                arrayOf("flaw", "lawn", 2),
                arrayOf("gumbo", "gambol", 2),
                arrayOf("book", "back", 2),
                arrayOf("extra", "extra", 0),      // Identical
                arrayOf("", "abc", 3),             // Empty source
                arrayOf("abc", "", 3),             // Empty target
                arrayOf("", "", 0),                // Both empty
                arrayOf("a", "b", 1),              // Single char substitution
                arrayOf("ab", "acb", 1),           // Insertion
                arrayOf("react", "race", 2)        // Complex mix
            )
        }
    }

    @Test
    fun distance_isCorrect() {
        val distance = levenshtein(s1, s2)
        assertEquals(expectedDistance, distance)
    }
}