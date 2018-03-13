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

import java.util.*

class NotationStack {

    private val doublyLinked = LinkedList<Boolean>()

    val isEmpty get() = doublyLinked.isEmpty()

    operator fun plusAssign(v: Boolean) =
        this.push(v)

    fun push(v: Boolean) =
        this.doublyLinked.push(v)

    fun pop(): Boolean =
        this.doublyLinked.pop()

    fun peek(): Boolean =
        this.doublyLinked.peek()

    override fun toString(): String = this.doublyLinked.toString()
}
