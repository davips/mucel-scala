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
    //    val internalHits = for {a <- cells; b <- cells; if a.id < b.id} yield Hit(a, b, Phy.timeTo(a, b, intersect = intersect))
    val internalHits = for {a <- cells; b <- a.neighbors} yield Hit(a, b, Phy.timeTo(a, b, intersect = intersect))
    val bubbleHits = for {c <- cells} yield Hit(bubble, c, Phy.timeTo(bubble, c, intersect = intersect))
    val wallHits = for {w <- walls} yield Hit(this, w, Phy.timeTo(bubble, w))
    val allHits = pairHits ++ internalHits ++ bubbleHits ++ wallHits
    val tmin = allHits.minBy(_.t).t
    allHits filter (_.t == tmin)
  }

  def walk(dt: Double): Unit = all foreach (_.walk(dt))
}

case class Sun(id: Int, x: Double, y: Double, r: Double, walls: Seq[Wall] = Seq(), intersect: Boolean = true) extends Elem {
  val pos: DenseVector[Double] = DenseVector[Double](x, y)
  val vel: DenseVector[Double] = DenseVector[Double](0, 0)
  val cells = Seq(Cell(id + 1, DenseVector(x, y), Cfg.zero, r / 2, solid = false, Bulb(permanent = true)))
  val bubble = Cell(Int.MinValue, DenseVector(x, y), Cfg.zero, r, solid = false, Isolant())
}

case class Org(id: Int, x: Double, y: Double, r: Double, walls: Seq[Wall] = Seq(), intersect: Boolean = true) extends Elem {
  lazy val (pos, vel) = (null: DenseVector[Double], resultantvel)
  val cellR: Double = 0.98 * r / 5
  val rnd = new Random(id)
  val types = Seq(Wire(), Motor(), Sensor(), Isolant(), Egg())

  //  val center = makeCenter(0)
  // ++Seq(Bulb(false))
  def makeCenter(n: Int) = Cell(id + 50 + n, DenseVector(x, y), DenseVector(ve, ve), cellR, rnd.nextBoolean(), types(rnd.nextInt(types.size)))

  val (fstLayer, sndLayer) = (makeLayer(6), makeLayer(12))
  //  center.neighbors ++= fstLayer
  fstLayer.zipWithIndex foreach { case (c, i) =>
    c.neighbors.enqueue(fstLayer((i + 7) % 6))
    c.neighbors.enqueue(sndLayer((i * 2 + 11) % 12), sndLayer((i * 2 + 12) % 12), sndLayer((i * 2 + 13) % 12))
  }
  sndLayer.zipWithIndex foreach { case (c, i) =>
    c.neighbors.enqueue(sndLayer((i + 13) % 12))
  }
  val cells: Seq[Cell] = fstLayer ++ sndLayer
  //  val cells: Seq[Cell] = Seq(center) ++ fstLayer ++ sndLayer
  cells foreach (x => println(x.neighbors.size))
  val bubble: Cell = Cell(Int.MinValue, DenseVector(x, y), Cfg.zero, r, solid = false, Isolant())

  def update(): Unit = {
    pos := meanpos(all)
    vel := resultantvel
  }

  def meanpos(cells: Seq[Cell]): DenseVector[Double] = cells.map(_.pos).reduce((a, b) => a + b) / cells.length.toDouble

  def resultantvel: DenseVector[Double] = all.map(_.vel).reduce((a, b) => a + b)

  private def makeLayer(n: Int) = {
    val da = 2 * math.Pi / n
    val pau = 1.01 * cellR * n / 3
    (0 until n) map { i =>
      val a = i * da
      val (xi, yi) = (x + pau * cos(a), y + pau * sin(a))
      Cell(id + 2 + i + (n / 6 - 1) * 6, DenseVector(xi, yi), DenseVector(ve, ve), cellR, rnd.nextBoolean(), types(rnd.nextInt(types.size)))
    }
  }

  def ve: Double = (rnd.nextDouble() - 0.5) * 200
}