import breeze.linalg.DenseVector
import things.{Cell, Org, Wall}

import scala.util.Random

object Bio {
  def newCell(rnd: Random)(x: Double, y: Double, posmin: Double, posmax: Double, minVel: Double, maxVel: Double, minRad: Double, maxRad: Double) = {
    val pos = DenseVector.fill(2)(rnd.nextDouble())
    pos *= posmax - posmin
    pos += posmin
    pos += DenseVector(x, y)
    val vel = DenseVector.fill(2)(rnd.nextDouble())
    vel *= maxVel - minVel
    vel += minVel
    var r = rnd.nextDouble()
    r *= maxRad - minRad
    r += minRad
    val (so, wi, se, mo) = (rnd.nextBoolean, rnd.nextBoolean, rnd.nextBoolean, rnd.nextBoolean)
    Cell(rnd.nextInt(999999999), pos, vel, r, so, wi, se, mo)
  }

  def newOrg(rnd: Random, walls: Seq[Wall]) = {
    val (x, y) = (340 * (rnd.nextDouble - 0.5), 340 * (rnd.nextDouble - 0.5))
    def nc = newCell(rnd)(x, y, -50, 50, -100, 100, 8, 14)
    Org(rnd.nextInt(999999999), 80, Seq.fill(30)(nc), walls)
  }
}
