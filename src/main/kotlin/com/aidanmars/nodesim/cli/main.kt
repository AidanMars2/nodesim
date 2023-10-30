package com.aidanmars.nodesim.cli

fun main() {
    println("commands: cut, copy, merge, package, simulate")
    while (true) {
        print("command: ")
        val command = readln()
        try {
            when (command) {
                "cut" -> doCutCli()
                "copy" -> doCopyCli()
                "merge" -> doMergeCli()
                "package" -> doPackageCli()
                "simulate" -> doSimulatorCli()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("invalid input, please try again")
        }
    }
}