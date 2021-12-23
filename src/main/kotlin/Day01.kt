fun main() {
    fun part1(input: List<String>): Int = input.map { it.toInt() }.windowed(2).count{ it.last() > it.first() }

    fun part2(input: List<String>): Int = input.map { it.toInt() }.windowed(4).count{ it.last() > it.first() }

    val input = readInput("Day01")
    println("Part 1 result:")
    println(part1(input))   // 1559
    println("Part 2 result:")
    println(part2(input))   // 1600
}
