import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Int {
        val acceptedRange = -50..50
        val cubes = mutableSetOf<Triple<Int, Int, Int>>()

        input.forEach { line ->
            val turnOn = line.startsWith("on")
            val (xRange, yRange, zRange) = line.substringAfter(' ').split(',').map { dimension ->
                val (start, end) = dimension.substringAfter('=').split("..").map { it.toInt() }
                IntRange(start, end)
            }

            if (xRange.inRange(acceptedRange) && yRange.inRange(acceptedRange) && zRange.inRange(acceptedRange)) {
                for (x in xRange) {
                    for (y in yRange) {
                        for (z in zRange) {
                            if (turnOn) {
                                cubes.add(Triple(x, y, z))
                            } else {
                                cubes.remove(Triple(x, y, z))
                            }
                        }
                    }
                }
            }
        }
        return cubes.size
    }

    fun part2(input: List<String>): Long {
        val cuboids = mutableListOf<Cuboid>()

        input.forEach { line ->
            // Parse cuboid from input line
            val (xRange, yRange, zRange) = line.substringAfter(' ').split(',').map { dimension ->
                val (start, end) = dimension.substringAfter('=').split("..").map { it.toInt() }
                IntRange(start, end)
            }
            val newCuboid = Cuboid(xRange, yRange, zRange)

            // Add the overlapping part of the new cuboid to each cuboid already turned on
            cuboids.forEach { it.addOverlappingOffCuboid(newCuboid) }

            // Turn on the new cuboid if instructed to do so
            if (line.startsWith("on")) cuboids.add(newCuboid)
        }

        return cuboids.sumOf { it.getActualVolume() }
    }

    val input = readInput("Day22")
    println("Part 1 result:")
    println(part1(input))   // 603661
    println("Part 2 result:")
    println(part2(input))   // 1237264238382479
}

fun IntRange.inRange(other: IntRange): Boolean {
    return this.first in other && this.last in other
}

fun IntRange.splitBy(other: IntRange): Set<IntRange> {
    val xSet = mutableSetOf(this.first, this.last + 1)
    if (other.first in this) xSet.add(other.first)
    if (other.last in this) xSet.add(other.last + 1)
    return xSet.sorted().windowed(2).map { IntRange(it[0], it[1] - 1) }.toSet()
}

class Cuboid(private val x: IntRange, private val y: IntRange, private val z: IntRange) {

    private val totalVolume: Long =
        (x.last - x.first + 1).toLong() * (y.last - y.first + 1).toLong() * (z.last - z.first + 1).toLong()

    private val overlappingOffCuboids = mutableSetOf<Cuboid>()

    fun addOverlappingOffCuboid(other: Cuboid) {
        val xRange = max(this.x.first, other.x.first)..Integer.min(this.x.last, other.x.last)
        val yRange = max(this.y.first, other.y.first)..Integer.min(this.y.last, other.y.last)
        val zRange = max(this.z.first, other.z.first)..Integer.min(this.z.last, other.z.last)
        if (xRange.last >= xRange.first && yRange.last >= yRange.first && zRange.last >= zRange.first) {
            overlappingOffCuboids.add(Cuboid(xRange, yRange, zRange))
        }
    }

    fun getActualVolume(): Long {
        val subCuboids = mutableSetOf(this)
        this.overlappingOffCuboids.forEach { offCuboid ->
            // Split cuboid to pieces and take only ones that are not in the current offCuboid
            val splitCuboids = subCuboids.map { it.splitBy(offCuboid) }.flatten().filter { !it.isInside(offCuboid) }

            subCuboids.clear()
            subCuboids.addAll(splitCuboids)
        }
        return subCuboids.sumOf { it.totalVolume }
    }

    private fun splitBy(other: Cuboid): Set<Cuboid> {
        val xRanges = this.x.splitBy(other.x)
        val yRanges = this.y.splitBy(other.y)
        val zRanges = this.z.splitBy(other.z)

        val set = mutableSetOf<Cuboid>()
        for (xRange in xRanges) {
            for (yRange in yRanges) {
                for (zRange in zRanges) {
                    set.add(Cuboid(xRange, yRange, zRange))
                }
            }
        }
        return set
    }

    private fun isInside(other: Cuboid) = this.x.inRange(other.x) && this.y.inRange(other.y) && this.z.inRange(other.z)
}