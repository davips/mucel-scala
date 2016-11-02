package things

import java.awt.geom.Ellipse2D
import java.awt.{AlphaComposite, Color, Graphics2D}
import javafx.scene.paint

import breeze.linalg.{DenseVector, norm}
import data.Cfg

import scala.util.Random


case class Cell(id: Int, pos: DenseVector[Double], vel: DenseVector[Double], r: Double, solid: Boolean, typ: Type) extends Movable {
  var energized = false

  def walk(dt: Double) {
    pos += dt * vel
    //sempre que calcula a norma de vel, trava tudo
    //    println(norm(vel))
    //    val nvel = norm(vel)
    //    val univel = 0d * vel / nvel
    //    vel:= vel - univel
  }

  def draw(g: Graphics2D) {
    val (x, y, _, r) = getxyvr
    val (lev, intens) = if (!solid) {
      g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.07f))
      0 -> 255 //((id.toDouble / Cfg.maxCells) * 127 + 128).round.toInt
    } else 20 -> 255 //((id.toDouble / Cfg.maxCells) * 127 + 128).round.toInt
    def rndLevel = Random.nextInt(intens)
    val dim = (255 * (((System.currentTimeMillis() % 1000) / 500d) - 1).abs).round.toInt
    val color = this match {
      case _ if this.energized => new Color(rndLevel, rndLevel, rndLevel)
      case Cell(_, _, _, _, _, Motor()) => new Color(intens, lev, lev)
      case Cell(_, _, _, _, _, Wire()) => new Color(lev, intens, lev)
      case Cell(_, _, _, _, _, Sensor()) => new Color(lev, lev, intens)
      case Cell(_, _, _, _, _, Isolant()) => new Color(intens / 2, lev, intens / 2)
      case Cell(_, _, _, _, _, Bulb(_)) => new Color(dim, dim, 0)
      case _ => ???
    }
    g.setColor(color)
    ball(g)(x, y, r)
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f))
  }
}
