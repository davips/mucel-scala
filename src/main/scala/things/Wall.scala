package things

import java.awt.{BasicStroke, Color, Graphics, Graphics2D}

import view.Paint

trait Wall extends Thing {
  val p: Double
  val dir: Int

  def timeTo(mov: Movable) = {
    val v = mov.vel(dir)
    val dist = if (p < 0) p - mov.pos(dir) + mov.r else p - mov.pos(dir) - mov.r
    val timeToHit = if (v == 0) Double.PositiveInfinity else dist / v
    if (timeToHit <= 0) Double.PositiveInfinity else 0.99999 * timeToHit //todo remover 999999 e ver se quebra
  }
}

case class VWall(p: Double) extends Wall {
  val id = Int.MaxValue
  val dir = 0

  def draw(g: Graphics2D) {
    g.setColor(new Color(255, 0, 255))
    g.setStroke(new BasicStroke(3f))
    line(g)(p, -3000, p, 3000)
  }
}

case class HWall(p: Double) extends Wall {
  val id = Int.MaxValue
  val dir = 1

  def draw(g:Graphics2D) {
    g.setColor(new Color(255, 0, 255))
    g.setStroke(new BasicStroke(3f))
    line(g)(-3000, p, 3000, p)
  }
}