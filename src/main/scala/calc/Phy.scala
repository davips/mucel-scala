package calc

import breeze.linalg.{max, min, norm}
import breeze.numerics._
import things._

object Phy {
  def nextHit(a: Org, b: Org) = if (a.bubble.dist(b.bubble) < a.r + b.r) {
    val hits = for {
      x <- a.cells.filter(_.solid)
      y <- b.cells.filter(_.solid)
    } yield Hit(x, y, timeTo(x, y, sameOrg = false))
    val tmin = if (hits.isEmpty) Double.MaxValue else hits.minBy(_.t).t
    hits filter (_.t == tmin)
  } else Seq(Hit(a, b, timeTo(a.bubble, b.bubble, sameOrg = false), bubbleHit = true))

  def timeTo(t1: Thing, t2: Thing, sameOrg: Boolean = true, intersect: Boolean = true): Double = (t1, t2) match {
    case (movable: Movable, wall: Wall) => wall.timeTo(movable)
    case (a: Cell, b: Cell) =>
      val (p1, s1, r10) = a.get
      val bubble = r10 >= 50
      val (p2, s2, r20) = b.get
      val s1s2 = s1 - s2
      val ns1s2 = norm(s1s2)
      val p1p2 = p1 - p2
      val p1p2norm = norm(p1p2)
      val p1p2sq = pow(p1p2norm, 2)
      val baska2A = 2 * pow(ns1s2, 2)
      val baskamB = -2 * p1p2.dot(s1s2)
      val (r1, r2) = r10 -> r20
      val rsum = if (intersect) r10 + r20 else (r10 - r20).abs
      // /if (sqrt(p1p2sq) < r10 + r20) (0.99999 * r10, 0.99999 * r20) else (1.00001 * r10, 1.00001 * r20)
      val baskaC = p1p2sq - pow(if (bubble) r1 - r2 else r1 + r2, 2)
      val baskaD = pow(baskamB, 2) - 2 * baska2A * baskaC
      val baskaDRooted = sqrt(baskaD)
      val tp = (baskamB + baskaDRooted) / baska2A
      val tm = (baskamB - baskaDRooted) / baska2A
      //            val t0 = min(tp, tm)
      val t0 = if (sameOrg && sqrt(p1p2sq) < rsum) max(tp, tm) else min(tp, tm)

      if (t0 <= 0 || ns1s2 == 0 || baskaD <= 0) Double.PositiveInfinity else t0
  }
}
