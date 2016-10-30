package things

import breeze.linalg.DenseVector


case class Cell(id: Int, pos: DenseVector[Double], vel: DenseVector[Double], r: Double, solid: Boolean, wire: Boolean, sensor: Boolean, motor: Boolean) extends Movable {
  def walk(dt: Double) {
    pos += dt * vel
  }
}
