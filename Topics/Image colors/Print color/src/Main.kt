fun printColor(myImage: BufferedImage) {
    val input = readln().split(" ").map { it.toInt() }
    val (x, y) = Pair(input.first(), input.last())
    val color = Color(myImage.getRGB(x, y), true)
    println("${color.red} ${color.green} ${color.blue} ${color.alpha}")
}
