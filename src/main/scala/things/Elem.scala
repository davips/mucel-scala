package things

import java.awt.{AlphaComposite, Color, Graphics2D}

import breeze.linalg.DenseVector
import breeze.numerics.{cos, sin}
import calc.{Hit, Phy}
import data.Cfg

import scala.util.Random

trait Elem extends Movable {
  lazy val all: Seq[Cell] = bubble +: cells
  val id: Int
  val x: Double
  val y: Double
  val r: Double
  val walls: Seq[Wall]
  val intersect: Boolean
  val cells: Seq[Cell]
  val bubble: Cell

  def draw(g: Graphics2D): Unit = {
    val (xb, yb, _, rb) = bubble.getxyvr
    g.setColor(new Color(255, 255, 255))
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05f))
    bubble(g)(xb, yb, rb)
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f))

    cells foreach (_.draw(g))
  }

  def nextHit(orgs: Seq[Elem]): Seq[Hit] = {
    val pairHits = orgs flatMap (org => if (this.id < org.id) Phy.nextHit(this, org) else Seq())
    val internalHits = for {a <- all; b <- cells; if a.id < b.id} yield Hit(a, b, Phy.timeTo(a, b, intersect = intersect))
    val wallHits = for {w <- walls} yield Hit(this, w, Phy.timeTo(bubble, w))
    val allHits = pairHits ++ internalHits ++ wallHits
    val tmin = allHits.minBy(_.t).t
    allHits filter (_.t == tmin)
  }

  def walk(dt: Double): Unit = all foreach (_.walk(dt))
}

case class Sun(id: Int, x: Double, y: Double, r: Double, walls: Seq[Wall] = Seq(), intersect: Boolean = true) extends Elem {
  val pos: DenseVector[Double] = DenseVector[Double](x, y)
  val vel: DenseVector[Double] = DenseVector[Double](0, 0)
  val cells = Seq(Cell(id + 1, DenseVector(x, y), Cfg.zero, r, solid = false, Bulb(permanent = true)))
  val bubble = Cell(Int.MinValue, DenseVector(x, y), Cfg.zero, r, solid = false, Isolant())
}

case class Org(id: Int, x: Double, y: Double, r: Double, walls: Seq[Wall] = Seq(), intersect: Boolean = true) extends Elem {
  lazy val (pos, vel) = (meanpos(all), resultantvel)
  val types = Seq(Wire(), Motor(), Sensor(), Isolant(), Egg())
  // ++Seq(Bulb(false))
  val center = Cell(id + 1, DenseVector(x, y), DenseVector(ve, ve), cellR, rnd.nextBoolean(), types(rnd.nextInt(types.size)))
  val (fstLayer, sndLayer) = (layer(6), layer(12))
  val cells: Seq[Cell] = Seq(center) ++ fstLayer ++ sndLayer
  val bubble: Cell = Cell(Int.MinValue, DenseVector(x, y), Cfg.zero, r, solid = false, Isolant())
  private val cellR = 0.98 * r / 5
  private val rnd = new Random(id)

  def update(): Unit = {
    pos := meanpos(all)
    vel := resultantvel
  }

  def meanpos(cells: Seq[Cell]) = cells.map(_.pos).reduce((a, b) => a + b) / cells.length.toDouble

  def resultantvel = all.map(_.vel).reduce((a, b) => a + b)

  private def layer(n: Int) = {
    val da = 2 * math.Pi / n
    val pau = 1.01 * cellR * n / 3
    (0 until n) map { i =>
      val a = i * da
      val (xi, yi) = (x + pau * cos(a), y + pau * sin(a))
      Cell(id + 2 + i + (n / 6 - 1) * 6, DenseVector(xi, yi), DenseVector(ve, ve), cellR, rnd.nextBoolean(), types(rnd.nextInt(types.size)))
    }
  }

  def ve = (rnd.nextDouble() - 0.5) * 200
}