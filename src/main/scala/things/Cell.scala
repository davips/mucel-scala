package things

import java.awt.geom.{Ellipse2D, Line2D}
import java.awt.{AlphaComposite, BasicStroke, Color, Graphics2D}
import javafx.scene.paint

import breeze.linalg.{DenseVector, norm}
import data.Cfg

import scala.collection.mutable
import scala.util.Random


case class Cell(id: Int, pos: DenseVector[Double], vel: DenseVector[Double], r: Double, solid: Boolean, typ: Type) extends Movable {
  var energized = false
  var lines = mutable.Queue[Line2D](new Line2D.Double(0 + sizex / 2, 0 + sizey / 2, 100 + sizex / 2, 100 + sizey / 2))

  def lineTo(other: Cell): Unit = {
    lines += lineObj(pos(0), pos(1), other.pos(0), other.pos(1))
  }

  def walk(dt: Double) {
    pos += dt * vel
    //sempre que calcula a norma de vel, trava tudo
    //    println(norm(vel))
    //    val nvel = norm(vel)
    //    val univel = 0d * vel / nvel
    //    vel:= vel - univel
  }

  def draw(g: Graphics2D) {
    val dim = (200 * (((System.currentTimeMillis() % 2000) / 1000d) - 1).abs).round.toInt
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f))
    g.setColor(new Color(55+dim, 55+dim, 0))
    g.setStroke(new BasicStroke(1f))
    lines foreach line2(g)
    lines.clear()
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f))

    val (x, y, _, r) = getxyvr
    val (lev, intens) = if (!solid) {
      g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.07f))
      0 -> 255 //((id.toDouble / Cfg.maxCells) * 127 + 128).round.toInt
    } else 20 -> 255 //((id.toDouble / Cfg.maxCells) * 127 + 128).round.toInt
    def rndLevel = Random.nextInt(intens)
    val color = this match {
      case _ if this.energized => new Color(rndLevel, rndLevel, rndLevel)
      case Cell(_, _, _, _, _, Motor()) => new Color(intens, lev, lev)
      case Cell(_, _, _, _, _, Wire()) => new Color(lev, intens, lev)
      case Cell(_, _, _, _, _, Sensor()) => new Color(lev, lev, intens)
      case Cell(_, _, _, _, _, Isolant()) => new Color(intens / 2, lev, intens / 2)
      case Cell(_, _, _, _, _, Bulb(_)) => new Color(255-dim, 255-dim, 0)
      case _ => ???
    }
    g.setColor(color)
    ball(g)(x, y, r)
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f))
  }
}
