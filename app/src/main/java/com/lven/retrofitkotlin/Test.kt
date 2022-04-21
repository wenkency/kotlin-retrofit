package com.lven.retrofitkotlin

class Test {
    // 求和
    fun sum(a: Int, b: Int) = a + b

    // 求大的值
    fun max(a: Int, b: Int) = if (a > b) a else b

    fun parseInt(str: String): Int? {
        return str.toIntOrNull()
    }

    fun wtest(x: Int) {
        when (x) {
            1, 2 -> {

            }
            3 -> {

            }
            else -> {

            }
        }
        when (x) {
            in 0..10 -> {
            }
            else -> {
            }
        }
    }

    // 测试
    fun main(args: Array<String>) {
        val a = parseInt("2")
        val b = parseInt("3")
        if (a == null || b == null) {
            return
        }
        val c = a * b
        println("c = $c")
        var items = listOf("a", "b", "c")
        // 循环一
        for (item in items) {
            println("item = $item")
        }
        // 循环二
        for ((index, item) in items.withIndex()) {
            println("item:$item,index:$index")
        }
        // 循环三
        items.forEachIndexed { index, item -> println("item:$item,index:$index") }

        var bb = B("name", 10)
        var cc = B("name")
    }

    open class A(var age: Int) {
        open fun test() {

        }
    }

    class B(val name: String, age: Int = 0) : A(age) {
        override fun test() {
            println("name = $name,age = $age")
        }
    }

}