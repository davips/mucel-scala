import java.awt.geom.{Ellipse2D, Line2D}
import java.awt.image.BufferedImage
import java.awt._
import java.io.{File, IOException}
import javax.imageio.ImageIO
import javax.swing.JPanel

import things._

import scala.util.Random

case class Animation(var dt: Double, sizex: Int, sizey: Int, world: World) extends JPanel {
  def update(): Unit = {
    val ti = System.currentTimeMillis()
    world.advance(dt)
    val elapsed = System.currentTimeMillis() - ti
    val remTime = (dt * 1000).round - elapsed
    if (remTime > 0) Thread.sleep(remTime)
  }

  def line(g: Graphics2D)(x1: Double, y1: Double, x2: Double, y2: Double) {
    g.draw(new Line2D.Double(x1 + sizex / 2, y1 + sizey / 2, x2 + sizex / 2, y2 + sizey / 2))
  }

  def ball(g: Graphics2D)(x: Double, y: Double, r: Double) {
    g.fill(new Ellipse2D.Double(x + sizex / 2 - r, y + sizey / 2 - r, 2 * r, 2 * r))
  }

  def bubble(g: Graphics2D)(x: Double, y: Double, r: Double) {
    g.draw(new Ellipse2D.Double(x + sizex / 2 - r, y + sizey / 2 - r, 2 * r, 2 * r))
  }

  def draw(g: Graphics2D)(t: Thing): Unit = t match {
    case (org: Org) =>
      //    val (x, y, _, r) = org.getxyvr
      //    g.setColor(new Color(255, 0, 0))
      //    bubble(g)(x, y, 1.5 * r)

      //    val (xb, yb, _, rb) = org.bubble.getxyvr
      //    g.setColor(new Color(255, 255, 255))
      //    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.01f))
      //    ball(g)(xb, yb, rb)
      //    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f))

      org.cells.zipWithIndex foreach { case (c, i) =>
        val (x, y, _, r) = c.getxyvr
        g.setColor(new Color(0, 0, ((i.toDouble / org.cells.length) * 127 + 128).round.toInt)) //new Random(c.id).nextInt(255)
        ball(g)(x, y, r)
      }
    case HWall(y) =>
      g.setColor(new Color(255, 0, 255))
      g.setStroke(new BasicStroke(3f))
      line(g)(-3000, y, 3000, y)
    case VWall(x) =>
      g.setColor(new Color(255, 0, 255))
      g.setStroke(new BasicStroke(3f))
      line(g)(x, -3000, x, 3000)
  }

  override def paint(G: Graphics) {
    val canvas = new BufferedImage(sizex, sizey, BufferedImage.TYPE_INT_RGB)
    val g = canvas.createGraphics()
    g.setColor(Color.BLACK)
    g.fillRect(0, 0, canvas.getWidth, canvas.getHeight)
    g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON)
    world.orgs ++ world.walls foreach draw(g)
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
