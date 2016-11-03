package data

import breeze.linalg.DenseVector
import calc.World
import things._

import scala.util.Random

object Cfg {
  val friction = 0.000001
  val seed = 422
  val rnd = new Random(seed)
  val norgs = 3
  val (minCells, maxCells) = (5, 25)
  val (verySmall, veryLarge) = (0.00000001, 999999999)
  val (frameWidth, frameHeight) = (650, 700)
  val (minPos, maxPos) = (-60d, 60d)
  val (minVel, maxVel) = (-2d, 2d)
  val (minRad, maxRad) = (12d, 30d)
  val walls = Seq(HWall(Cfg.frameHeight / 2d), HWall(-Cfg.frameHeight / 2d), VWall(Cfg.frameWidth / 2d), VWall(-Cfg.frameWidth / 2d))
  val orgs = (1 to Cfg.norgs) map (id => Factory.newOrg(rnd, walls, intersect = false, 1)(id))
  val world = World(walls, Factory.newBulb(walls) +: orgs)

  def zero = DenseVector[Double](0, 0)
}
