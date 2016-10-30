package things

trait Wall extends Thing {
  val p: Double
  val dir: Int

  def timeTo(mov: Movable) = {
    val v = mov.vel(dir)
    val dist = if (p < 0) p - mov.pos(dir) + mov.r else p - mov.pos(dir) - mov.r
    val timeToHit = if (v == 0) Double.PositiveInfinity else dist / v
    if (timeToHit <= 0) Double.PositiveInfinity else 0.99999 * timeToHit
  }
}

case class VWall(p: Double) extends Wall {
  val id = Int.MaxValue
  val dir = 0
}

case class HWall(p: Double) extends Wall {
  val id = Int.MaxValue
  val dir = 1
}