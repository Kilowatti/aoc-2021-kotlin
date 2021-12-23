fun main() {
    fun part1(input: List<String>): Int = input.sumOf { line ->
        val charList = mutableListOf<Char>()
        var error = 0
        for (char in line) {
            when (char) {
                '(', '[', '{', '<' -> charList.add(char)
                ')' -> if (charList.removeLast() != '(') { error = 3; break; }
                ']' -> if (charList.removeLast() != '[') { error = 57; break; }
                '}' -> if (charList.removeLast() != '{') { error = 1197; break; }
                '>' -> if (charList.removeLast() != '<') { error = 25137; break; }
            }
        }
        error
    }

    fun part2(input: List<String>): Long {
        val scoreList = input.map { line ->
            val characters = mutableListOf<Char>()
            for (char in line) {
                when (char) {
                    '(', '[', '{', '<' -> characters.add(char)
                    ')' -> if (characters.removeLast() != '(') { characters.clear(); break; }
                    ']' -> if (characters.removeLast() != '[') { characters.clear(); break; }
                    '}' -> if (characters.removeLast() != '{') { characters.clear(); break; }
                    '>' -> if (characters.removeLast() != '<') { characters.clear(); break; }
                }
            }

            var sum = 0L
            characters.reversed().forEach {
                sum = sum * 5 + when (it) {
                    '(' -> 1   // ): 1 point
                    '[' -> 2    // ]: 2 points
                    '{' -> 3    // }: 3 points
                    '<' -> 4    // >: 4 points
                    else -> 0
                }
            }
            sum
        }.filter { it > 0 }.sorted()

        // Middle score
        return scoreList[scoreList.size / 2]
    }

    val input = readInput("Day10")
    println("Part 1 result:")
    println(part1(input))   // 392043
    println("Part 2 result:")
    println(part2(input))   // 1605968119
}
