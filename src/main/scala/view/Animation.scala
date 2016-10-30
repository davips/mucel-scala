package view

import java.awt._
import java.awt.image.BufferedImage
import java.io.{File, IOException}
import javax.imageio.ImageIO
import javax.swing.JPanel

import data.Cfg

case class Animation(var dt: Double) extends JPanel {
  val world = Cfg.world

  def update(): Unit = {
    val ti = System.currentTimeMillis()
    world.advance(dt)
    val elapsed = System.currentTimeMillis() - ti
    val remTime = (dt * 1000).round - elapsed
    if (remTime > 0) Thread.sleep(remTime)
  }

  override def paint(G: Graphics) {
    val canvas = new BufferedImage(Cfg.frameWidth, Cfg.frameHeight, BufferedImage.TYPE_INT_RGB)
    val g = canvas.createGraphics()
    g.setColor(Color.BLACK)
    g.fillRect(0, 0, canvas.getWidth, canvas.getHeight)
    g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON)
    world.orgs ++ world.walls foreach (_.draw(g))
//    println("asd")
    g.dispose()
    //  javax.imageio.ImageIO.write(canvas, "png", new java.io.File("drawing.png"))
    G.drawImage(canvas, 0, 0, this)
  }

  def createImage(): BufferedImage = {
    try ImageIO.read(new File("drawing.png")) catch {
      case e: IOException => e.printStackTrace()
    }
    null
  }
}
