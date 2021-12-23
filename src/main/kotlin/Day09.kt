fun main() {
    fun part1(input: List<String>): Int {
        val matrix = input.map { line -> line.map { char -> char.digitToInt() } }
        val lowPoints = matrix.getLowPoints()

        return lowPoints.sumOf { (row, column) -> matrix[row][column] + 1 }
    }

    fun part2(input: List<String>): Int {
        val matrix = input.map { line -> line.map { char -> char.digitToInt() } }
        val lowPoints = matrix.getLowPoints()
        val threeLargestBasinSizes = lowPoints.map { getBasin(matrix, it).size }.sorted().takeLast(3)
        var productOfSizes = 1
        for (size in threeLargestBasinSizes) productOfSizes *= size

        return productOfSizes
    }

    val input = readInput("Day09")
    println("Part 1 result:")
    println(part1(input))   // 491
    println("Part 2 result:")
    println(part2(input))   // 1075536
}

fun List<List<Int>>.getLowPoints(): List<Pair<Int, Int>> {
    val lowPoints = mutableListOf<Pair<Int, Int>>()
    val rows = this.indices
    for (row in rows) {
        val columns = this[row].indices
        for (column in columns) {
            val minAdjacent = this.getAdjacent(row, column)
            val current = this[row][column]
            if (minAdjacent > current) {
                lowPoints.add(Pair(row, column))
            }
        }
    }
    return lowPoints
}

fun List<List<Int>>.getAdjacent(row: Int, column: Int): Int {
    val adjacentSet = mutableSetOf<Int>()
    if (row > 0) adjacentSet.add(this[row - 1][column])
    if (row < lastIndex) adjacentSet.add(this[row + 1][column])
    if (column > 0) adjacentSet.add(this[row][column - 1])
    if (column < this[row].lastIndex) adjacentSet.add(this[row][column + 1])

    return adjacentSet.minOrNull() ?: 0
}

fun getBasin(matrix: List<List<Int>>, lowPoint: Pair<Int, Int>): Set<Pair<Int, Int>> {
    val coordinates = mutableSetOf(lowPoint)
    do {
        val size = coordinates.size

        val newCoordinates = mutableSetOf(lowPoint)
        for (coordinate in coordinates) {
            val (row, column) = coordinate
            for (searchRow in row downTo 0) {
                if (matrix[searchRow][column] < 9) newCoordinates.add(Pair(searchRow, column))
                else break
            }
            for (searchRow in row..matrix.lastIndex) {
                if (matrix[searchRow][column] < 9) newCoordinates.add(Pair(searchRow, column))
                else break
            }
            for (searchColumn in column downTo 0) {
                if (matrix[row][searchColumn] < 9) newCoordinates.add(Pair(row, searchColumn))
                else break
            }
            for (searchColumn in column..matrix[row].lastIndex) {
                if (matrix[row][searchColumn] < 9) newCoordinates.add(Pair(row, searchColumn))
                else break
            }
        }
        coordinates.addAll(newCoordinates)

    } while (coordinates.size > size)
    return coordinates
}