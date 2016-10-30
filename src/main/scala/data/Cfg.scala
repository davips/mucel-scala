package data

import breeze.linalg.DenseVector
import calc.World
import things._

import scala.util.Random

object Cfg {
  val seed = 40

  val rnd = new Random(seed)
  val norgs = 4
  val (minCells, maxCells) = (10, 50)
  val (verySmall, veryLarge) = (0.00000001, 999999999)
  val (frameWidth, frameHeight) = (650, 700)
  val (minPos, maxPos) = (-40d, 40d)
  val (minVel, maxVel) = (-40d, 40d)
  val (minRad, maxRad) = (6d, 13d)

  val walls = Seq(HWall(Cfg.frameHeight / 2d), HWall(-Cfg.frameHeight / 2d), VWall(Cfg.frameWidth / 2d), VWall(-Cfg.frameWidth / 2d))
  val world = World(walls, (1 to Cfg.norgs) map (id => Factory.newOrg(rnd, walls, rnd.nextBoolean())(id)))
}
