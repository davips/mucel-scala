package calc

import breeze.linalg.min
import data.Cfg
import things._
import view.Paint

case class World(walls: Seq[Wall], orgs: Seq[Org]) {
  val bulbs = orgs.filter(_.cells.exists(_.typ == Bulb())).map(_.cells.head)
  val sensors = orgs.flatMap(_.cells.filter(_.typ == Sensor()))

  def blocked(a: Cell, b: Cell) = false

  def advance(dt: Double): Unit = {
    val allHits = orgs.par flatMap (_.nextHit(orgs))
    if (allHits.nonEmpty) {
      val tmin = allHits.par.minBy(_.t).t
      val t = min((1 - Cfg.verySmall) * tmin, dt)
      orgs.par foreach (_.walk(t))
      if (t < dt) {
        allHits.par.filter(x => !x.bubbleHit && x.t == tmin) foreach (_.run())
        advance(dt - t)
      } else //time quantum completed (usually 1/30 i.e. 30fps)
        for {bulb <- bulbs; sensor <- sensors} {
          if (!blocked(sensor, bulb)) bulb.lineTo(sensor)
        }
    }
  }
}
