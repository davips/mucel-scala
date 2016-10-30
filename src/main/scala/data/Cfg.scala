package data

import breeze.linalg.DenseVector
import calc.World
import things._

import scala.util.Random

object Cfg {
  val seed = 41

  val rnd = new Random(seed)
  val (norgs, ncells) = 4 -> 30
  val (verySmall, veryLarge) = (0.00000001, 999999999)
  val (frameWidth, frameHeight) = (650, 700)
  val (minPos, maxPos) = (-40d, 40d)
  val (minVel, maxVel) = (-100d, 100d)
  val (minRad, maxRad) = (5d, 10d)

  val walls = Seq(HWall(Cfg.frameHeight / 2d), HWall(-Cfg.frameHeight / 2d), VWall(Cfg.frameWidth / 2d), VWall(-Cfg.frameWidth / 2d))
  val world = World(walls, (1 to Cfg.norgs) map (id => Factory.newOrg(rnd, walls)(id)))
}
