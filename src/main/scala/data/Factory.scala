package data

import breeze.linalg.DenseVector
import things.{Cell, Org, Wall}

import scala.util.Random

/**
  * Created by davi on 10/30/16.
  */
object Factory {
  def newCell(rnd: Random)(id: Int, x: Double, y: Double) = {
    val pos = DenseVector.fill(2)(rnd.nextDouble())
    pos *= Cfg.maxPos - Cfg.minPos
    pos += Cfg.minPos
    pos += DenseVector(x, y)
    val vel = DenseVector.fill(2)(rnd.nextDouble())
    vel *= Cfg.maxVel - Cfg.minVel
    vel += Cfg.minVel
    var r = rnd.nextDouble()
    r *= Cfg.maxRad - Cfg.minRad
    r += Cfg.minRad
    val (so, wi, se, mo) = (rnd.nextBoolean, rnd.nextBoolean, rnd.nextBoolean, rnd.nextBoolean)
    Cell(id, pos, vel, r, so, wi, se, mo)
  }

  def newOrg(rnd: Random, walls: Seq[Wall])(id: Int) = {
    val r = Cfg.maxPos * math.sqrt(2) + Cfg.maxRad + 1
    val (x, y) = (Cfg.frameWidth - 2 * r) * (rnd.nextDouble - 0.5) -> (Cfg.frameHeight - 2 * r) * (rnd.nextDouble - 0.5)
    def nc(id: Int) = newCell(rnd)(id, x, y)
    Org(id, x, y, r, (1 to Cfg.ncells) map nc, walls)
  }
}
