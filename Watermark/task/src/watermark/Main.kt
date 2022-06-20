package watermark

import java.awt.Color
import java.awt.Transparency
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.system.exitProcess

class Watermark {
    interface WatermarkPattern
    data class Single(val x: Int, val y: Int) : WatermarkPattern
    class Grid : WatermarkPattern

    companion object {


        fun printImageProperties(image: File) {
            if (image.exists()) {
                println("Image file: ${image.path}")
                val bufferedImage = ImageIO.read(image)
                println("Width: ${bufferedImage.width}")
                println("Height: ${bufferedImage.height}")
                println("Number of components: ${bufferedImage.colorModel.numComponents}")
                println("Number of color components: ${bufferedImage.colorModel.numColorComponents}")
                println("Bits per pixel: ${bufferedImage.colorModel.pixelSize}")
                when (bufferedImage.colorModel.transparency) {
                    Transparency.OPAQUE -> println("Transparency: OPAQUE")
                    Transparency.TRANSLUCENT -> println("Transparency: TRANSLUCENT")
                    Transparency.BITMASK -> println("Transparency: BITMASK")
                }
            } else {
                println("The file ${image.path} doesn't exist.")
            }
        }

        fun validateFile(filename: String, name: String): BufferedImage {
            val file = File(filename)
            if (file.exists()) {
                val bufferedImage = ImageIO.read(file)
                if (bufferedImage.colorModel.numColorComponents != 3) {
                    throw Exception("The number of $name color components isn't 3.")
                }
                if (bufferedImage.colorModel.pixelSize !in listOf(24, 32)) {
                    throw Exception("The $name isn't 24 or 32-bit. ")
                }
                return bufferedImage
            } else {
                throw Exception("The file $filename doesn't exist.")
            }
        }


        fun createWatermark(
            image: BufferedImage,
            watermark: BufferedImage,
            weight: Int,
            useAlpha: Boolean = false,
            transparencyColor: Color?,
            pattern: WatermarkPattern,
            output: String
        ) {
            val outputBufferedImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB)
            for (x in 0 until image.width) {
                for (y in 0 until image.height) {
                    val imageCurrentPixelColor = Color(image.getRGB(x, y))


                    val waterMarkPixelColor = if (pattern is Single) {
                        if (x in pattern.x until (pattern.x + watermark.width) && y in pattern.y until (pattern.y + watermark.height)) {
                            Color(watermark.getRGB(x - pattern.x, y - pattern.y), useAlpha)
                        } else {
                            null
                        }
                    } else {
                        Color(watermark.getRGB(x % watermark.width, y % watermark.height), useAlpha)
                    }

                    val color =
                        if (waterMarkPixelColor == null) {
                            imageCurrentPixelColor
                        } else {
                            if (useAlpha && waterMarkPixelColor.alpha == 0) {
                                imageCurrentPixelColor
                            } else if (!useAlpha && transparencyColor != null && transparencyColor == waterMarkPixelColor) {
                                imageCurrentPixelColor
                            } else {
                                Color(
                                    (weight * waterMarkPixelColor.red + (100 - weight) * imageCurrentPixelColor.red) / 100,
                                    (weight * waterMarkPixelColor.green + (100 - weight) * imageCurrentPixelColor.green) / 100,
                                    (weight * waterMarkPixelColor.blue + (100 - weight) * imageCurrentPixelColor.blue) / 100
                                )
                            }
                        }
                    outputBufferedImage.setRGB(x, y, color.rgb)
                }
            }

            if (ImageIO.write(outputBufferedImage, output.substringAfterLast("."), File(output))) {
                println("The watermarked image $output has been created.")
            }
        }
    }
}

fun main() {
    println("Input the image filename:")
    try {
        val image = Watermark.validateFile(readln(), "image")
        println("Input the watermark image filename:")

        val watermark = Watermark.validateFile(readln(), "watermark")
        if (watermark.width > image.width || watermark.height > image.height)
            throw Exception("The watermark's dimensions are larger.")

        val useAlpha: Boolean =
            if (watermark.colorModel.hasAlpha() && watermark.colorModel.transparency == Transparency.TRANSLUCENT) {
                println("Do you want to use the watermark's Alpha channel?")
                readln().let {
                    when (it.lowercase()) {
                        "yes" -> true
                        else -> false
                    }
                }
            } else {
                false
            }
        val transparencyColor: Color? = try {
            if (!watermark.colorModel.hasAlpha()) {
                println("Do you want to set a transparency color?")
                if (readln() == "yes") {
                    println("Input a transparency color ([Red] [Green] [Blue]):")
                    val (r, g, b) = readln().split(" ").let {
                        if (it.size != 3) {
                            throw Exception()
                        }
                        it
                    }.map {
                        if (it.toInt() !in 0..255) {
                            throw Exception()
                        }
                        it.toInt()
                    }
                    Color(r, g, b)
                } else
                    null
            } else {
                null
            }
        } catch (e: Exception) {
            throw Exception("The transparency color input is invalid.")
        }

        println("Input the watermark transparency percentage (Integer 0-100):")
        val transparency =
            readln().toIntOrNull() ?: throw Exception("The transparency percentage isn't an integer number.")
        if (transparency !in 0..100)
            throw Exception("The transparency percentage is out of range.")

        println("Choose the position method (single, grid):")
        val watermarkPattern = readln().let {
            when (it) {
                "single" -> {
                    println("Input the watermark position ([x 0-${image.width - watermark.width}] [y 0-${image.height - watermark.height}]):")
                    readln().split(" ").let {
                        if (it.size != 2) {
                            throw Exception("The position input is invalid.")
                        }
                        it
                    }.map { it.toIntOrNull() ?: throw Exception("The position input is invalid.") }.let {
                        if (it.first() !in 0..(image.width - watermark.width) || it.last() !in 0..(image.height - watermark.height)) {
                            throw Exception("The position input is out of range.")
                        }
                        Watermark.Single(it.first(), it.last())
                    }
                }
                "grid" -> Watermark.Grid()
                else -> throw Exception("The position method input is invalid.")

            }
        }


        println("Input the output image filename (jpg or png extension):")
        val output = readln().also {
            if (!(it.endsWith(".jpg") || it.endsWith(".png"))) {
                throw Exception("The output file extension isn't \"jpg\" or \"png\".")
            }
        }
        Watermark.createWatermark(image, watermark, transparency, useAlpha, transparencyColor, watermarkPattern, output)

    } catch (exception: Exception) {
        println(exception.message)
        exitProcess(666)
    }
}
