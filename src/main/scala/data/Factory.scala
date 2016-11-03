package data

import breeze.linalg.DenseVector
import breeze.numerics.{cos, sin, sqrt}
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
    val (so, ty) = rnd.shuffle(Seq(true, false, false, false, false)).head -> rnd.shuffle(Seq.fill(8)(Isolant()) ++ Seq.fill(3)(Wire()) ++ Seq(Motor(), Sensor())).head
    val cell = Cell(id, pos, vel, r, so || ty == Motor() || ty == Sensor(), ty)
    cell.energized = false
    cell
  }

  def newBulb(walls: Seq[Wall], x: Double = 0, y: Double = 0, r: Double = 5) = {
    Org(Int.MinValue, x, y, 2 * r, Seq(Cell(1, DenseVector(x, y), Cfg.zero, r, solid = false, Bulb())), walls)
  }

  def newOrg(rnd: Random, walls: Seq[Wall], intersect: Boolean = true, scale: Double = 1)(id: Int) = {
    val r = scale * (Cfg.maxPos * math.sqrt(2) + Cfg.maxRad + 10)
    val (x, y) = (Cfg.frameWidth - 2 * r) * (rnd.nextDouble - 0.5) -> (Cfg.frameHeight - 2 * r) * (rnd.nextDouble - 0.5)
    def nc(id: Int) = newCell(rnd)(id, x, y)
    def ncells = rnd.nextInt(Cfg.maxCells - Cfg.minCells) + Cfg.minCells
    Org(id, x, y, r, (1 to ncells) map nc, walls, intersect)
  }

  val r = 15

  def isol(id: Int, x: Double, y: Double) = Cell(id, DenseVector(x, y), DenseVector(-50, 10), 0.95 * r, solid = true, Isolant())

  def sens(id: Int, x: Double, y: Double) = Cell(id, DenseVector(x, y), DenseVector(1, -100), 0.95 * r, solid = true, Sensor())

  def moto(id: Int, x: Double, y: Double) = Cell(id, DenseVector(x, y), DenseVector(-100, -100), 0.95 * r, solid = true, Motor())

  def isol2(id: Int, x: Double, y: Double) = Cell(id, DenseVector(x, y), DenseVector(-10, 10), 1.95 * r, solid = true, Isolant())

  def sens2(id: Int, x: Double, y: Double) = Cell(id, DenseVector(x, y), DenseVector(1, -10), 1.95 * r, solid = true, Sensor())

  def moto2(id: Int, x: Double, y: Double) = Cell(id, DenseVector(x, y), DenseVector(-1, -10), 1.95 * r, solid = true, Motor())

  def x(a: Double) = 2 * r * cos(a)

  def y(a: Double) = 2 * r * sin(a)

  def x2(a: Double) = 2.6 * r * cos(a)

  def y2(a: Double) = 2.6 * r * sin(a)

  def planarian(walls: Seq[Wall]) = Org(-1, 0, 0, 3.5 * r, isol(8, 0, 0) +: Seq(
    (a: Double) => sens(4, x(a), y(a))
    , (a: Double) => isol(3, x(a), y(a))
    , (a: Double) => sens(5, x(a), y(a))
    , (a: Double) => moto(6, x(a), y(a))
    , (a: Double) => isol(2, x(a), y(a))
    , (a: Double) => moto(7, x(a), y(a))
  ).zip(0 until 6).map { case (f, a) => f(math.Pi * a / 3) }, walls, intersect = false)

  def miniPlanarian(walls: Seq[Wall]) = Org(-2, 150, 0, 4.6 * r, Seq(
    (a: Double) => sens2(4, x2(a) + 150, y2(a))
    , (a: Double) => sens2(5, x2(a) + 150, y2(a))
    , (a: Double) => moto2(6, x2(a) + 150, y2(a))
  ).zip(0 until 3).map { case (f, a) => f(2 * math.Pi * a / 3) }, walls, intersect = false)
}
