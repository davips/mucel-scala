package view

import java.awt.Graphics2D
import java.awt.geom.{Ellipse2D, Line2D}

import data.Cfg

trait Paint {
  val (sizex, sizey) = Cfg.frameWidth -> Cfg.frameHeight

  def line(g: Graphics2D)(x1: Double, y1: Double, x2: Double, y2: Double) = lineObj(x1, y1, x2, y2)

  def line2(g: Graphics2D)(l: Line2D) {
    g.draw(l)
  }

  def lineObj(x1: Double, y1: Double, x2: Double, y2: Double) = new Line2D.Double(x1 + sizex / 2, y1 + sizey / 2, x2 + sizex / 2, y2 + sizey / 2)

  def ball(g: Graphics2D)(x: Double, y: Double, r: Double) {
    g.fill(new Ellipse2D.Double(x + sizex / 2 - r, y + sizey / 2 - r, 2 * r, 2 * r))
  }

  def bubble(g: Graphics2D)(x: Double, y: Double, r: Double) {
    g.draw(new Ellipse2D.Double(x + sizex / 2 - r, y + sizey / 2 - r, 2 * r, 2 * r))
  }
}
