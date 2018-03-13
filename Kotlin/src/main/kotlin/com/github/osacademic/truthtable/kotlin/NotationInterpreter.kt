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

import com.github.osacademic.truthtable.kotlin.grammar.NotationParser
import org.antlr.v4.runtime.ParserRuleContext
import java.lang.Math.pow
import java.util.*


fun interpret(notation: NotationParser.NotationContext): TruthTable =
    NotationInterpreter(notation).truthTable


private class NotationInterpreter(notation: NotationParser.NotationContext) {
    val stack = NotationStack()
    val truthTable = TruthTable(notation.text)
    val localVariable = mutableMapOf<String, Boolean>()

    init {
        doEval(notation)
    }

    internal fun getPrepositions(notation: NotationParser.NotationContext): List<String> {
        val prepositions = mutableListOf<String>()
        val deque = LinkedList<ParserRuleContext>()
        deque.add(notation)

        while (deque.isNotEmpty()) {
            val last = deque.removeLast()

            when (last) {
                is NotationParser.NotationContext -> {
                    last.notationPart(1)?.let(deque::addLast)
                    last.notationPart(0)?.let(deque::addLast)
                }
                is NotationParser.NotationPartContext -> {
                    last.notation()?.let(deque::addLast)
                    last.preposition()?.let(deque::addLast)
                }
                is NotationParser.PrepositionContext -> {
                    val text = last.Text().text
                    if (!prepositions.contains(text))
                        prepositions += text
                }
            }
        }

        return prepositions
    }

    internal fun doEval(notation: NotationParser.NotationContext) {
        val prepositions = getPrepositions(notation)

        this.truthTable.prepositions += prepositions

        val possible = mutableListOf<Boolean>()

        val size = prepositions.size

        val numbersOfCombinations = pow(POSS.size.toDouble(), size.toDouble()).toInt()

        for (i in 0 until numbersOfCombinations) {
            for (curr in (0 until size).reversed()) {
                possible += (i / (pow(POSS.size.toDouble(), curr.toDouble())).toInt()) % 2 != 0
            }
        }

        val results = mutableListOf<Result>()

        possible.chunked(size).reversed().forEach {
            this.localVariable.clear()

            it.forEachIndexed { index, b ->
                this.localVariable[prepositions[index]] = b
            }

            eval(notation, results)

            val r = this.stack.pop()

            this.truthTable.entries += TableEntry(it, r, results.toList())

            results.clear()

            if (!this.stack.isEmpty) {
                throw IllegalStateException("Stack is not empty")
            }

        }

    }

    internal fun eval(notation: NotationParser.PrepositionContext) {
        val t = this.localVariable[notation.text]
                ?: throw IllegalStateException("Preposition ${notation.text} does not have assigned value")

        this.stack.push(t)
    }

    internal fun eval(notation: NotationParser.NotationContext,
                      res: MutableList<Result>) {
        val first = notation.notationPart(0)!!
        val operation = notation.operation()!!
        val second = notation.notationPart(1)!!

        first.notation()?.let {
            this.eval(it, res)
        }

        first.preposition()?.let {
            this.eval(it)
        }

        first.not()?.let {
            this.applyNeg()
        }

        second.notation()?.let {
            this.eval(it, res)
        }

        second.preposition()?.let {
            this.eval(it)
        }

        second.not()?.let {
            this.applyNeg()
        }

        val (f, s) = applyOp(operation)

        res += Result(Data(first, f), operation, Data(second, s), stack.peek())
    }

    private fun applyNeg() {
        /* |F|S|
         * |1|0|
         * |0|1|
         */
        this.stack += !stack.pop()
    }

    private fun applyOp(operation: NotationParser.OperationContext): Pair<Boolean, Boolean> {
        val second = stack.pop()
        val first = stack.pop()

        fun ret(): Pair<Boolean, Boolean> = first to second
        /* |F|S|
         * |1|0|
         * |0|1|
         */
        /*
                operation.Negation()?.also {
                    stack += !first
                    stack += second
                    return
                }
        */

        /* |F|S|F·S|
         * |1|1| 1 |
         * |1|0| 0 |
         * |0|1| 0 |
         * |0|0| 0 |
         */
        operation.Conjunction()?.also {
            stack += first && second
            return ret()
        }

        /* |F|S|F+S|
         * |1|1| 1 |
         * |1|0| 1 |
         * |0|1| 1 |
         * |0|0| 0 |
         */
        operation.Disjunction()?.also {
            stack += first || second
            return ret()
        }

        /* |F|S|F⊕S|
         * |1|1| 0 |
         * |1|0| 1 |
         * |0|1| 1 |
         * |0|0| 0 |
         */
        operation.Exclusive_Disjuction()?.also {
            stack += first.xor(second)
            return ret()
        }

        /* |F|S|F->S|
         * |1|1| 1  |
         * |1|0| 0  |
         * |0|1| 1  |
         * |0|0| 1  |
         */
        operation.Conditional()?.also {
            stack += (first == second) || (second)
            return ret()
        }

        /* |F|S|F<->S|
         * |1|1|  1  |
         * |1|0|  0  |
         * |0|1|  0  |
         * |0|0|  1  |
         */
        operation.Biconditional()?.also {
            stack += (first == second)
            return ret()
        }

        return ret()
    }

    companion object {
        private val POSS = booleanArrayOf(true, false)
    }

}

