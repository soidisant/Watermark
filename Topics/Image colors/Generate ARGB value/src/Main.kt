

fun printARGB() {
    val (alpha, red, green, blue) = readln().split(" ").map {
        if (it.toInt() !in 0..255) {
            println("Invalid input")
            return
        }
        it.toInt()
    }

    val color = java.awt.Color(red, green, blue, alpha)
    println(color.rgb.toUInt())
}
