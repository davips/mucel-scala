package calc

import breeze.linalg.{DenseVector, norm}
import things._

case class Hit(a: Thing, b: Thing, t: Double, bubbleHit:Boolean=false) {
  def run(): Unit = (a, b) match {
    case (org: Org, _: HWall) => org.bubble.vel(1) *= -1
    case (org: Org, _: VWall) => org.bubble.vel(0) *= -1
    case (cella: Cell, cellb: Cell) => collide(cella, cellb)
    case (orga: Org, orgb: Org) => println("tried to hit two orgs!")
  }

  def collide(mova: Movable, movb: Movable): Unit = {
    val (ficaNoA, vaiProB) = decompose(mova.pos, movb.pos, mova.vel)
    val (ficaNoB, vaiProA) = decompose(movb.pos, mova.pos, movb.vel)
    mova.vel := ficaNoA + vaiProA
    movb.vel := ficaNoB + vaiProB
  }

  def decompose(posa: DenseVector[Double], posb: DenseVector[Double], vela: DenseVector[Double]) = {
    val ab = posb - posa
    val abuni = ab / norm(ab)
    val projMag = vela.dot(abuni)
    val vai = projMag * abuni
    val fica = vela - vai
    (fica, vai)
  }
}
