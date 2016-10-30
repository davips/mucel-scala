package things

import java.awt.geom.Ellipse2D
import java.awt.{Color, Graphics2D}

import breeze.linalg.DenseVector
import data.Cfg


case class Cell(id: Int, pos: DenseVector[Double], vel: DenseVector[Double], r: Double, solid: Boolean, wire: Boolean, sensor: Boolean, motor: Boolean) extends Movable {
  def walk(dt: Double) {
    pos += dt * vel
  }

  def draw(g: Graphics2D) {
    val (x, y, _, r) = getxyvr
    g.setColor(new Color(0, 0, ((id.toDouble / Cfg.ncells) * 127 + 128).round.toInt)) //new Random(c.id).nextInt(255)
    ball(g)(x, y, r)
    g.fill(new Ellipse2D.Double(x + sizex / 2 - r, y + sizey / 2 - r, 2 * r, 2 * r))
  }
}
