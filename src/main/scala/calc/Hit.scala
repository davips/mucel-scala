package calc

import breeze.linalg.{DenseVector, norm}
import things._

case class Hit(a: Thing, b: Thing, t: Double, bubbleHit: Boolean = false) {
  def run(): Unit = (a, b) match {
    case (org: Org, _: HWall) => org.bubble.vel(1) *= -1
    case (org: Org, _: VWall) => org.bubble.vel(0) *= -1
    case (cella: Cell, cellb: Cell) => collide(cella, cellb)
    case (orga: Org, orgb: Org) => println("tried to hit two orgs!")
  }

  def collide(cella: Cell, cellb: Cell): Unit = {
    val (ficaNoA, vaiProB, vaiProBUni) = decompose(cella.pos, cellb.pos, cella.vel)
    val (ficaNoB, vaiProA, vaiProAUni) = decompose(cellb.pos, cella.pos, cellb.vel)
    manageEnergy(cella, cellb)
    cellb.vel := ficaNoB + vaiProB
    cella.vel := ficaNoA + vaiProA

    def manageEnergy(a: Cell, b: Cell) = (a.typ, b.typ) match {
      case (Wire(), Wire()) =>
        val tmp = b.energized
        b.energized = a.energized
        a.energized = tmp
      case (Sensor(), Wire()) if a.energized && !b.energized => b.energized = true; a.energized = false
      case (Wire(), Sensor()) if b.energized && !a.energized => a.energized = true; b.energized = false
      case (Motor(), Wire() | Sensor()) if b.energized => vaiProA += 30d * vaiProAUni; b.energized = false
      case (Wire() | Sensor(), Motor()) if a.energized => vaiProB += 30d * vaiProBUni; a.energized = false
      case (_, Isolant()) | (Isolant(), _) | (Sensor(), Sensor()) | (Motor(), Motor()) =>
      case (Motor(), Sensor() | Wire()) | (Sensor() | Wire(), Motor()) =>
      case (Sensor(), Wire() | Motor()) | (Wire() | Motor(), Sensor()) =>
      case (Bulb(_), _) | (_, Bulb(_)) =>
    }
  }

  def decompose(posa: DenseVector[Double], posb: DenseVector[Double], vela: DenseVector[Double]) = {
    val ab = posb - posa
    val abuni = ab / norm(ab)
    val projMag = vela.dot(abuni)
    val vai = projMag * abuni
    val fica = vela - vai
    (fica, vai, abuni)
  }
}
