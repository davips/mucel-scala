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
    val (ficaNoA, vaiProB, vaiProBuni) = decompose(cella.pos, cellb.pos, cella.vel)
    val (ficaNoB, vaiProA, vaiProAUni) = decompose(cellb.pos, cella.pos, cellb.vel)
    manageMotor(cella, cellb, vaiProB, vaiProBuni)
    manageMotor(cellb, cella, vaiProA, vaiProAUni)
    if (!manageEnergy(cella, cellb)) manageEnergy(cellb, cella)
    cellb.vel := ficaNoB + vaiProB
    cella.vel := ficaNoA + vaiProA

    def manageMotor(a: Cell, b: Cell, vaiPro: DenseVector[Double], impulse: DenseVector[Double]) =
      if (a.energized && b.typ == Motor()) {
        impulse *= 100d
        vaiPro += impulse
        a.energized = false
      }
    def manageEnergy(a: Cell, b: Cell) = {
      val manage = a.energized && b.typ == Wire()
      if (manage) {
        if (a.typ == Wire()) a.energized = b.energized
        if (a.typ == Sensor()) a.energized = false
        b.energized = true
      }
      manage
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
