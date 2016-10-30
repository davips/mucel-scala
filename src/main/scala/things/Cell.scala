package things

import java.awt.geom.Ellipse2D
import java.awt.{AlphaComposite, Color, Graphics2D}

import breeze.linalg.DenseVector
import data.Cfg


case class Cell(id: Int, pos: DenseVector[Double], vel: DenseVector[Double], r: Double, solid: Boolean, wire: Boolean, sensor: Boolean, motor: Boolean) extends Movable {
  def walk(dt: Double) {
    pos += dt * vel
  }

  def draw(g: Graphics2D) {
    val (x, y, _, r) = getxyvr
    this match {
      case Cell(_, _, _, _, true, _, _, _) =>
        g.setColor(new Color(0, 0, ((id.toDouble / Cfg.maxCells) * 127 + 128).round.toInt))
      case Cell(_, _, _, _, false, _, _, _) =>
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05f))
        g.setColor(new Color(0, ((id.toDouble / Cfg.maxCells) * 127 + 128).round.toInt, 0 ))
    }
    ball(g)(x, y, r)
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f))
  }
}
