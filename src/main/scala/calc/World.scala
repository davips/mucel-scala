package calc

import breeze.linalg.min
import breeze.numerics.{abs, sqrt}
import data.Cfg
import things._

case class World(walls: Seq[Wall], orgs: Seq[Org]) {
  val bulbs = orgs.filter(_.cells.exists(_.typ == Bulb())).map(_.cells.head)
  val sensors = orgs.flatMap(_.cells.filter(_.typ == Sensor()))

  def blockers = orgs.map { o =>
    (o.bubble.x, o.bubble.y, o.bubble.r) -> o.cells.filter(c => c.solid && c.typ != Bulb() && c.typ != Sensor())
  }.filter(_._2.nonEmpty)

  def distLineToPoint(x1: Double, y1: Double, x2: Double, y2: Double)(x0: Double, y0: Double) = {
    val y21 = y2 - y1
    val x21 = x2 - x1
    abs(y21 * x0 - x21 * y0 + x2 * y1 - y2 * x1) / sqrt(y21 * y21 + x21 * x21)
  }

  //  def between(x: Double, a: Double, b: Double) = (x - a) * (x - b) < 0
  //
  //  def between2d(c: Cell, a: Cell, b: Cell) = between(c.x, a.x, b.x) && between(c.y, a.y, b.y)
  def between(c: Cell, a: Cell, b: Cell) = {
    val ac = a.pos-c.pos
    val bc = b.pos -c.pos
    ac.dot(bc) < 0
  }

  def blocked(a: Cell, b: Cell) = {
    val margin = distLineToPoint(a.x, a.y, b.x, b.y) _
    blockers.filter { case ((x, y, r), _) => margin(x, y) <= r }.exists { case ((x, y, r), cells) =>
      cells.exists(c => margin(c.x, c.y) <= c.r && between(c, a, b))
    }
  }

  def advance(dt: Double): Unit = {
    val allHits = orgs.par flatMap (_.nextHit(orgs))
    if (allHits.nonEmpty) {
      val tmin = allHits.par.minBy(_.t).t
      val t = min((1 - Cfg.verySmall) * tmin, dt)
      orgs.par foreach (_.walk(t))
      if (t < dt) {
        allHits.par.filter(x => !x.bubbleHit && x.t == tmin) foreach (_.run())
        advance(dt - t)
      } else {
        //time quantum completed (usually 1/30 i.e. 30fps)
        for {bulb <- bulbs; sensor <- sensors} {
          if (!blocked(sensor, bulb)) {
            bulb.lineTo(sensor)
            sensor.energized = true
          }
        }
      }
    }
  }
}
