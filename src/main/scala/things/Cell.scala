package things

import java.awt.geom.Line2D
import java.awt.{AlphaComposite, BasicStroke, Color, Graphics2D}

import breeze.linalg.DenseVector
import data.Cfg

import scala.collection.mutable
import scala.util.Random


case class Cell(id: Int, pos: DenseVector[Double], vel: DenseVector[Double], r: Double, solid: Boolean, typ: Type) extends Movable {
  var energized = false
  var lines = mutable.Queue[Line2D]()
  var mot = false

  def lineTo(other: Cell): Unit = {
    lines += lineObj(x, y, other.x, other.y)
  }

  def x = pos(0)

  def y = pos(1)

  def walk(dt: Double) {
    pos += dt * vel
    //todo correct to real friction:
    vel *= Cfg.friction

    //sempre que calcula a norma de vel, trava tudo
    //    println(norm(vel))
    //    val nvel = norm(vel)
    //    val univel = 0d * vel / nvel
    //    vel:= vel - univel
  }

  def draw(g: Graphics2D) {
    val dim = (200 * (((System.currentTimeMillis() % 200) / 100d) - 1).abs).round.toInt
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f))
    g.setColor(new Color(55 + dim, 55 + dim, 0))
    g.setStroke(new BasicStroke(1f))
    println(lines.size)
    lines foreach line2(g)
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f))

    val (x, y, _, r) = getxyvr
    val (lev, intens) = if (!solid) {
      g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.07f))
      if (typ == Bulb()) g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f))
      0 -> 255
    } else 0 -> 200
    val rndLevel = Random.nextInt(intens)
    val motf = if (mot) 0.7 else 1
    val color = typ match {
      case Wire() if energized => new Color(0, 55 + dim, 0)
      case Wire() => new Color(lev, intens, lev)
      case Sensor() if energized => new Color(0, 0, 55 + dim)
      case Sensor() => new Color(lev, lev, intens)
      case Motor() => new Color((motf * intens).round.toInt, lev, lev)
      case Isolant() => new Color(intens / 2, lev, intens / 2)
      case Bulb(_) => new Color(255 - dim, 255 - dim, 0)
      case Egg() => new Color(200, 200, 200)
      case ty => sys.error(s"cell type $ty not drawable")
    }
    g.setColor(color)
    ball(g)(x, y, r)
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f))
  }
}
