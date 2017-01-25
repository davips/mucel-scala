package data

import breeze.linalg.DenseVector
import calc.World
import things._

import scala.util.Random

object Cfg {
  val friction = 1d
  //0.999995
  val seed = 422
  val rnd = new Random(seed)
  val norgs = 1
  val (minCells, maxCells) = (5, 25)
  val (verySmall, veryLarge) = (0.000001, 9999999)
  val (frameWidth, frameHeight) = (650, 700)
  val (minPos, maxPos) = (-60d, 60d)
  val (minVel, maxVel) = (-5000d, 5000d)
  val (minRad, maxRad) = (12d, 30d)
  val walls = Seq(HWall(Cfg.frameHeight / 2d), HWall(-Cfg.frameHeight / 2d), VWall(Cfg.frameWidth / 2d), VWall(-Cfg.frameWidth / 2d))
  //  val orgs = Factory.planarian(400, walls) +: Factory.planarian(500, walls) +: Factory.planarian(600, walls) +: Factory.planarian(700, walls) +: Factory.planarian(800, walls) +: Factory.planarian(900, walls) +:
  //    ((1 to Cfg.norgs) map (id => Factory.newOrg(rnd, walls, intersect = false, 1)(id)))
  val (r, l) = 50 -> 110
  val world = World(walls, Seq(
    //      ,    Org(0, l, 0, r, walls, intersect = false)
    Sun(Int.MinValue, -50, -50, r / 5, walls)
    , Org(1000, l, l, r, walls, intersect = false)
    , Org(2000, 0, l, r, walls, intersect = false)
    , Org(3000, -l, l, r, walls, intersect = false)
    , Org(4000, -l, 0, r, walls, intersect = false)
    , Org(5000, -l, -l, r, walls, intersect = false)
    , Org(6000, 0, -l, r, walls, intersect = false)
    , Org(7000, l, -l, r, walls, intersect = false)
  ))
  //val world = World(walls,  orgs)

  def zero = DenseVector[Double](0, 0)
}
