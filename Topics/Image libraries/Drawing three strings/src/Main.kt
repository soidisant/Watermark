import java.awt.Color
import java.awt.image.BufferedImage

fun drawStrings(): BufferedImage {
    val image = BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB)
    val graphics = image.createGraphics()

    graphics.background = Color.BLACK
    graphics.color = Color.red
    graphics.drawString("Hello, images!", 50, 50)
    graphics.color = Color.green
    graphics.drawString("Hello, images!", 51, 51)
    graphics.color = Color.blue
    graphics.drawString("Hello, images!", 52, 52)
    return image
}
