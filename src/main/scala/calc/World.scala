package calc

import breeze.linalg.min
import data.Cfg
import things.{Org, Wall}

case class World(walls: Seq[Wall], orgs: Seq[Org]) {
  def advance(dt: Double): Unit = {
    val allHits = orgs.par flatMap (_.nextHit(orgs))
    val tmin = allHits.par.minBy(_.t).t
    val t = min((1 - Cfg.verySmall) * tmin, dt)
    orgs.par foreach (_.walk(t))
    if (t < dt) {
      allHits.par.filter(x => !x.bubbleHit && x.t == tmin) foreach (_.run())
      advance(dt - t)
    }
  }
}
