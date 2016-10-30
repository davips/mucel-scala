package things

import java.awt.Graphics2D

import view.Paint

trait Thing extends Paint {
  val id: Int

  def draw(g: Graphics2D)
}

