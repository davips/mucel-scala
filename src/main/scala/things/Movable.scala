package things

import breeze.linalg.functions.euclideanDistance
import breeze.linalg.{DenseVector, squaredDistance}

trait Movable extends Thing {
  val pos: DenseVector[Double]
  val vel: DenseVector[Double]
  val r: Double

  def getxyvr = (pos(0), pos(1), vel, r)

  def walk(dt: Double)

  def dist(mov: Movable) = euclideanDistance(pos, mov.pos)

  def sqDist(mov: Movable) = squaredDistance(pos, mov.pos)

  def get = (pos, vel, r)
}