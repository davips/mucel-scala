package things

import java.awt.geom.Ellipse2D
import java.awt.{AlphaComposite, Color, Graphics2D}
import javafx.scene.paint

import breeze.linalg.DenseVector
import data.Cfg

import scala.util.Random


case class Cell(id: Int, pos: DenseVector[Double], vel: DenseVector[Double], r: Double, solid: Boolean, wire: Boolean, sensor: Boolean, motor: Boolean) extends Movable {
  var energized = false

  def walk(dt: Double) {
    pos += dt * vel
  }

  def draw(g: Graphics2D) {
    val (x, y, _, r) = getxyvr
    val intens = ((id.toDouble / Cfg.maxCells) * 127 + 128).round.toInt
    val level = 0
    def rndLevel = Random.nextInt(intens)
    if (solid) g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f))
    val color = this match {
      case _ if this.energized => new Color(rndLevel, rndLevel, rndLevel)
      case Cell(_, _, _, _, _, true, _, _) => new Color(level, level, intens)
      case Cell(_, _, _, _, _, _, true, _) => new Color(level, intens, level)
      case Cell(_, _, _, _, _, _, _, true) => new Color(intens, level, level)
      case _ => Color.WHITE
    }
    g.setColor(color)
    ball(g)(x, y, r)
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f))
  }
}
