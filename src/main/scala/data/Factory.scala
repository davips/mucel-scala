package data

import breeze.linalg.DenseVector
import things._

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
    val (so, ty) = rnd.shuffle(Seq(true, false, false, false, false)).head -> rnd.shuffle(Seq.fill(7)(Isolant()) ++ Seq.fill(2)(Wire()) ++ Seq(Motor(), Sensor())).head
    val cell = Cell(id, pos, vel, r, so || ty == Motor() || ty == Sensor(), ty)
    cell.energized = ty == Sensor() //&& rnd.nextBoolean()
    cell
  }

  def newBulb(walls: Seq[Wall], x: Double = 0, y: Double = 0, r: Double = 5) = {
    Org(Int.MinValue, x, y, 2 * r, Seq(Cell(1, DenseVector(x, y), Cfg.zero, r, solid = true, Bulb())), walls)
  }

  def newOrg(rnd: Random, walls: Seq[Wall], intersect: Boolean = true, scale: Double = 1)(id: Int) = {
    val r = scale * (Cfg.maxPos * math.sqrt(2) + Cfg.maxRad + 10)
    val (x, y) = (Cfg.frameWidth - 2 * r) * (rnd.nextDouble - 0.5) -> (Cfg.frameHeight - 2 * r) * (rnd.nextDouble - 0.5)
    def nc(id: Int) = newCell(rnd)(id, x, y)
    def ncells = rnd.nextInt(Cfg.maxCells - Cfg.minCells) + Cfg.minCells
    Org(id, x, y, r, (1 to ncells) map nc, walls, intersect)
  }
}
