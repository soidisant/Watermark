import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun drawLines(): BufferedImage {
    val image = BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB)
    val graphics = image.createGraphics()

    graphics.background = Color.BLACK

    graphics.color = Color.red
    graphics.drawLine(0, 0, 200, 200)
    graphics.color = Color.green
    graphics.drawLine(200, 0, 0, 200)
//    ImageIO.write(image, "png", File("test.png"))
    return image
}
