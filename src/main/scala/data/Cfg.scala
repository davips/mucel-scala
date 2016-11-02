package data

import breeze.linalg.DenseVector
import calc.World
import things._

import scala.util.Random

object Cfg {
  val friction = 0.000001
  val seed = 42238
  val rnd = new Random(seed)
  val norgs = 3
  val (minCells, maxCells) = (30, 50)
  val (verySmall, veryLarge) = (0.00000001, 999999999)
  val (frameWidth, frameHeight) = (650, 700)
  val (minPos, maxPos) = (-70d, 70d)
  val (minVel, maxVel) = (-12d, 12d)
  val (minRad, maxRad) = (9d, 20d)
  val walls = Seq(HWall(Cfg.frameHeight / 2d), HWall(-Cfg.frameHeight / 2d), VWall(Cfg.frameWidth / 2d), VWall(-Cfg.frameWidth / 2d))
  val orgs = (1 to Cfg.norgs) map (id => Factory.newOrg(rnd, walls, intersect = false, 1)(id))
  val world = World(walls, Factory.newBulb(walls) +: orgs)

  def zero = DenseVector[Double](0, 0)
}
