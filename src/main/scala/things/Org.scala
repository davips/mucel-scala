package things

import java.awt.{AlphaComposite, Color, Graphics2D}

import breeze.linalg.{DenseVector, min}
import calc.{Hit, Phy}

case class Org(id: Int, x: Double, y: Double, r: Double, cells: Seq[Cell], walls: Seq[Wall] = Seq()) extends Movable {
  val bubble = Cell(Int.MinValue, DenseVector(x, y), zero, r, solid = false, wire = false, sensor = false, motor = false)
  val all = bubble +: cells
  lazy val (pos, vel) = (meanpos(all), resultantvel)

  def zero = DenseVector[Double](0, 0)

  def nextHit(orgs: Seq[Org]) = {
    val pairHits = orgs flatMap (org => if (this.id < org.id) Phy.nextHit(this, org) else Seq())
    val internalHits = for {a <- all; b <- cells; if a.id < b.id} yield Hit(a, b, Phy.timeTo(a, b))
    val wallHits = for {w <- walls} yield Hit(this, w, Phy.timeTo(bubble, w))
    val allHits = pairHits ++ internalHits ++ wallHits
    val tmin = allHits.minBy(_.t).t
    allHits filter (_.t == tmin)
  }

  def walk(dt: Double) = all foreach (_.walk(dt))

  def update(): Unit = {
    pos := meanpos(all)
    vel := resultantvel
  }

  def meanpos(cells: Seq[Cell]) = cells.map(_.pos).reduce((a, b) => a + b) / cells.length.toDouble

  def resultantvel = all.map(_.vel).reduce((a, b) => a + b)

  def draw(g: Graphics2D): Unit = {
    val (xb, yb, _, rb) = bubble.getxyvr
    g.setColor(new Color(255, 255, 255))
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05f))
    ball(g)(xb, yb, rb)
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f))

    cells foreach (_.draw(g))
  }
}