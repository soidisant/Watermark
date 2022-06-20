import java.awt.Color
import java.awt.image.BufferedImage

fun drawCircles(): BufferedImage {
    val image = BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB)
    val graphics = image.createGraphics()

    graphics.background = Color.BLACK
    graphics.color = Color.red
    graphics.drawOval(50, 50, 100, 100)
    graphics.color = Color.yellow
    graphics.drawOval(50, 75, 100, 100)
    graphics.color = Color.green
    graphics.drawOval(75, 50, 100, 100)
    graphics.color = Color.blue
    graphics.drawOval(75, 75, 100, 100)
    return image
}
