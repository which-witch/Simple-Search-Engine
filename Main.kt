package search

import java.io.File

fun menu() {
    println(
        "\n" +
                "=== Menu ===\n" +
                "1. Find a person\n" +
                "2. Print all people\n" +
                "0. Exit"
    )
}

fun mapLines(listOfLines: List<String>): MutableMap<Int, MutableList<String>> {
    val linesMap = mutableMapOf<Int, MutableList<String>>()
    for (i in listOfLines.indices) {
        linesMap[i] = listOfLines[i].split(" ").toMutableList()
    }
    return linesMap
}

fun printAll(listOfLines: List<String>) {
    listOfLines.forEach { println(it) }
}

fun find(mapOfLines: MutableMap<Int, MutableList<String>>) {
    printSearchingResult(selectSearchingStrategy(mapOfLines))
}

fun selectSearchingStrategy(mapOfLines: MutableMap<Int, MutableList<String>>): Set<String> {
    println("Select a matching strategy: ALL, ANY, NONE")
    val searchingStrategy = readln().trim()
    println("Enter a name or email to search all suitable people.")
    val toFind = readln().split(" ")
    return when (searchingStrategy.uppercase()) {
        "ALL" -> searchAll(mapOfLines, toFind)
        "ANY" -> searchAny(mapOfLines, toFind)
        "NONE" -> searchNone(mapOfLines, toFind)
        else -> mutableSetOf()
    }
}

fun printSearchingResult(found: Set<String>) {
    if (found.isNotEmpty()) {
        println("People found:")
        found.forEach { println(it) }
    } else {
        println("No matching people found.")
    }
}

fun searchAny(mapOfLines: MutableMap<Int, MutableList<String>>, toFind: List<String>): MutableSet<String> {
    val found = mutableSetOf<String>()
    for (i in mapOfLines.values) {
        for (j in toFind) {
            if (i.joinToString(" ").lowercase().split(" ")
                    .contains(j.lowercase())
            ) {
                found.add(i.joinToString(" "))
            }
        }
    }
    return found
}

fun searchAll(mapOfLines: MutableMap<Int, MutableList<String>>, toFind: List<String>): MutableSet<String> {
    val found = mutableSetOf<String>()
    for (i in mapOfLines.values) {
        if (i.joinToString(" ").lowercase().split(" ")
                .containsAll(toFind.joinToString(" ").lowercase().split(" "))
        ) {
            found.add(i.joinToString(" "))
        }
    }
    return found
}

fun searchNone(mapOfLines: MutableMap<Int, MutableList<String>>, toFind: List<String>): Set<String> {
    val found = searchAny(mapOfLines, toFind)
    val all = mutableSetOf<String>()
    mapOfLines.values.forEach { all.add(it.joinToString(" ")) }
    return all.subtract(found)

}


fun main(args: Array<String>) {
    if (args.contains("--data")) {
        val listOfLines = File(args.last()).readLines().toList()
        val mapOfLines = mapLines(listOfLines)
        while (true) {
            menu()
            when (readln().trim().toInt()) {
                0 -> {
                    println("Bye!")
                    break
                }
                2 -> printAll(listOfLines)
                1 -> find(mapOfLines)
                else -> println("Incorrect option! Try again.")
            }
        }
    }
}
