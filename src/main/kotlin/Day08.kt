fun main() {
    fun part1(input: List<String>): Int {
        var count = 0
        input.forEach { line ->
            count += line.split(" | ").last().split(' ')
                .count { it.length == 2 || it.length == 3 || it.length == 4 || it.length == 7 }
        }
        return count
    }

    fun part2(input: List<String>): Int {
        var count = 0
        input.forEach { line ->
            count += Display(line).output
        }
        return count
    }

    val input = readInput("Day08")
    println("Part 1 result:")
    println(part1(input))   // 440
    println("Part 2 result:")
    println(part2(input))   // 1046281
}

class Display(input: String) {

    /** Map of segment (in 'a'..'g') to possible mixed values (in 'a'..'g') */
    private val segments: Map<Char, MutableSet<Char>> = ('a'..'g').associateWith { ('a'..'g').toMutableSet() }

    /** Output value of the display */
    val output: Int

    init {
        val (mixed, outputs) = input.split(" | ", limit = 2)

        // Sort out segments
        mixed.split(' ').forEach { addMixed(it) }
        calculateRemaining()
        val segmentMap: Map<Char, Char> = segments.map { it.value.single() to it.key }.toMap()

        // Calculate output into a member value
        output = outputs.split(' ').map { mixedDigit -> mixedDigit.map { segmentMap[it] }.toSet() }
            .map { outputSegmentSet -> digits.filterValues { it == outputSegmentSet }.keys.single() }
            .joinToString("") { it.toString() }.toInt()
    }

    private fun addMixed(value: String) {
        val chars = value.toSet()
        val noChars = ('a'..'g').toMutableSet()
        noChars.removeAll(chars)
        when (value.length) {
            2 -> digits[1]?.forEach { segments[it]?.removeAll(noChars) }
            3 -> digits[7]?.forEach { segments[it]?.removeAll(noChars) }
            4 -> digits[4]?.forEach { segments[it]?.removeAll(noChars) }
            8 -> digits[8]?.forEach { segments[it]?.removeAll(noChars) }
            5 -> {
                "adg".forEach { segments[it]?.removeAll(noChars) }
            }
            6 -> {
                "abfg".forEach { segments[it]?.removeAll(noChars) }
            }
        }
    }

    private fun calculateRemaining() {
        do {
            var someRemoved = false
            val singleSet: Set<Char> = segments.values.filter { it.size == 1 }.map { it.single() }.toSet()
            segments.forEach {
                if (it.value.size > 1 && it.value.removeAll(singleSet)) {
                    someRemoved = true
                }
            }
        } while (someRemoved)
    }

    companion object {
        /** Map of digit (in 0..9) to segments (in 'a'..'g') */
        private val digits: Map<Int, Set<Char>> = mapOf(
            0 to "abcefg".toSet(),
            1 to "cf".toSet(),
            2 to "acdeg".toSet(),
            3 to "acdfg".toSet(),
            4 to "bcdf".toSet(),
            5 to "abdfg".toSet(),
            6 to "abdefg".toSet(),
            7 to "acf".toSet(),
            8 to "abcdefg".toSet(),
            9 to "abcdfg".toSet(),
        )
    }
}