import breeze.linalg.{max, min}
import things.{Org, Wall}

case class World(walls: Seq[Wall], orgs: Seq[Org]) {
  def advance(dt: Double): Unit = {
    val allHits = orgs.par flatMap (_.nextHit(orgs))
    val tmin = allHits.par.minBy(_.t).t
    val t = max(0.0000000001, min(0.9999999 * tmin, dt))  //0.0000000001 está evitando que dt entre bolhas continue sendo considerado infinitamente. o caso das bolohas é especial porque elas não colidem entre si, fazendo com que continuem aparecendo como a colisão mais imediata.
    orgs.par foreach (_.walk(t))
    if (t < dt) {
      val minHits = allHits.par.filter(_.t == tmin)
      minHits.par foreach (_.run())
      advance(dt - t)
    }
  }
}
