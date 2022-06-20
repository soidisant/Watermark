import java.awt.Color
import java.awt.image.BufferedImage

fun drawSquare(): BufferedImage {
    val image = BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB)
    val graphics = image.createGraphics()

    graphics.background = Color.BLACK
    graphics.color = Color.red
    graphics.drawRect(100, 100, 300, 300)
    return image
}
