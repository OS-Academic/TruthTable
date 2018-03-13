/*
 *      OSA-TruthTable-Kotlin - Truth table generator written in Kotlin/JVM
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2018 JonathanxD <https://github.com/OSAcademic/TruthTable/Kotlin>
 *      Copyright (c) contributors
 *
 *
 *      Permission is hereby granted, free of charge, to any person obtaining a copy
 *      of this software and associated documentation files (the "Software"), to deal
 *      in the Software without restriction, including without limitation the rights
 *      to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *      copies of the Software, and to permit persons to whom the Software is
 *      furnished to do so, subject to the following conditions:
 *
 *      The above copyright notice and this permission notice shall be included in
 *      all copies or substantial portions of the Software.
 *
 *      THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *      IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *      FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *      AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *      LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *      OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *      THE SOFTWARE.
 */
package com.github.osacademic.truthtable.kotlin

import com.github.osacademic.truthtable.kotlin.grammar.NotationLexer
import com.github.osacademic.truthtable.kotlin.grammar.NotationParser
import org.antlr.v4.runtime.CodePointBuffer
import org.antlr.v4.runtime.CodePointCharStream
import org.antlr.v4.runtime.CommonTokenStream
import java.nio.CharBuffer


fun main(args: Array<String>) {

    Until(
        operation = { readLine() },
        matches = { it != "<EOF>" },
        run = {
            val buf = CharBuffer.wrap(it.toCharArray())

            val stream = CodePointCharStream.fromBuffer(CodePointBuffer.withChars(buf))

            val lexer = NotationLexer(stream)
            val parser = NotationParser(CommonTokenStream(lexer))
            val notation = parser.notation()

            val table = interpret(notation)

            println("===================================")
            println("  Table  ")
            printTable(table)
            println("===================================")
        }
    )

}

fun printTable(table: TruthTable) {
    val nTable = table.prepositions.map { " $it " } + " ${table.input} "

    val joinTable: List<String>.(autoSize: Boolean) -> String = { autoSize ->
        var index = -1
        this.joinToString(separator = "|", prefix = "|", postfix = "|") {
            ++index
            if (autoSize) spaced(nTable[index].length, it)
            else it
        }
    }

    val boolStr: Boolean.() -> String = { if (this) "T" else "F" }

    val builder = StringBuilder()
    val joined = nTable.joinTable(false)

    val lines = StringBuilder().let { s -> repeat(joined.length) { s.append('-') }; s.toString() }

    builder.append(lines).append('\n')

    builder.append(joined).append('\n')
    builder.append(lines).append('\n')

    val valuesLines = table.entries
        .map {
            (it.values.map(boolStr) + it.result.boolStr()).joinTable(true)
        }

    valuesLines.forEach {
        builder.append(it).append('\n')
    }

    builder.append(lines).append('\n')

    builder.append('\n').append("== Details ==").append('\n')

    table.entries.forEachIndexed { i, tableEntry ->
        val tableString = "${tableEntry.values.map(boolStr).joinTable(false)} = ${tableEntry.result.boolStr()}"

        builder.append("$i: Details of $tableString").append('\n')
        tableEntry.results.forEach {
            builder.append("(${it.first.ctx.text}=${it.first.value}, ${it.op.text}, ${it.second.ctx.text}=${it.second.value}) = ${it.result}")
                .append('\n')
        }
        builder.append('\n')

    }

    println(builder.toString())

}

fun spaced(length: Int, str: String): String =
    StringBuilder().let { s -> repeat(length - str.length) { s.append(' ') }; s }.let {
        val down = length / 2
        if (down - 1 != 0) it.insert(down - 1, str) // Left preference
        else it.insert(down, str)
        it.toString()
    }

data class TableEntry(val values: List<Boolean>, val result: Boolean, val results: List<Result>)

class TruthTable(val input: String) {
    val prepositions = mutableListOf<String>()
    val entries = mutableListOf<TableEntry>()
}