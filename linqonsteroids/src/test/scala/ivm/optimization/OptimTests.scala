package ivm
package optimization

import expressiontree.Lifting._
import org.scalatest.junit.{ShouldMatchersForJUnit, JUnitSuite}
import org.junit.Test
import expressiontree.{Const, Plus, FuncExp, Exp}

/**
 * User: pgiarrusso
 * Date: 2/1/2012
 */

class OptimTests extends JUnitSuite with ShouldMatchersForJUnit {
  val x = FuncExp.gensym[Int]()

  def testIdempotence[T](e: Exp[T]) = {
    val opt = Optimization.reassociateOps(e)
    opt should be (Optimization.reassociateOps(opt))
    opt
  }
  
  @Test
  def reassociateOpsF() {
    def f(e: Exp[Int]) = e + 1
    val composedF = f(f(f(x)))

    val optF = testIdempotence(composedF)
    optF should be (Plus(Const(3), x))
  }

  @Test
  def reassociateOpsG() {
    def g(e: Exp[Int]) = 1 + e
    val composedG = g(g(g(x)))
    val optG = testIdempotence(composedG)
    optG should be (Plus(Const(3), x))
  }

  @Test
  def reassociateOpsH() {
    val h = 1 + (1 + x) + 1
    val optH = testIdempotence(h)
    optH should be (Plus(Const(3), x))
  }

  //Optimization results below are not the best, but it's hard to get this kind of patterns right in general
  //(consider increasing distance between constants)
  @Test
  def reassociateOpsI() {
    val i = x + x + 1 + x + 1
    val optI = testIdempotence(i)
    optI should be (Plus(Plus(Plus(Const(2), x), x), x))
  }

  @Test
  def reassociateOpsJ() {
    val j = x + 1 + x + 1
    val optJ = testIdempotence(j)
    optJ should be (Plus(Plus(Const(2), x), x))
  }

  @Test
  def testHoistFilter() {
    val base = Vector.range(1, 6).asSmartCollection
    val query =
      for {
        i <- base
        j <- base
        if i < 3
      } yield (i, j)
    val opt = Optimization.hoistFilter(query)
    query.interpret() should be (for (i <- 1 to 2; j <- 1 to 5) yield (i, j))
    opt.interpret() should be (query.interpret())
    opt should be (
      for {
        i <- base
        if i < 3
        j <- base
      } yield (i, j)
    )

    val query2 =
      for {
        i <- base
        j <- base
        k <- base
        l <- base
        if i < 3
      } yield (i, j, l)
    val opt2 = Optimization.hoistFilter(query2)
    query2.interpret() should be (for (i <- 1 to 2; j <- 1 to 5; k <- 1 to 5; l <- 1 to 5) yield (i, j, l))
    opt2 should be (
      for {
        i <- base
        if i < 3
        j <- base
        k <- base
        l <- base
      } yield (i, j, l)
    )
    opt2.interpret() should be (query2.interpret())

    val opt21 = Optimization.hoistFilter(opt2)
    opt21 should be (
      for {
        i <- base
        if i < 3
        j <- base
        k <- base
        l <- base
      } yield (i, j, l)
    )
    opt21.interpret() should be (query2.interpret())
  }

  @Test
  def testRemoveRedundantOption() {
    val base = Seq(1) asSmartCollection
    val query = for (i <- base.typeFilter[Int]; j <- Let(i) if j % 2 === 1) yield j
    val opt = Optimization.removeRedundantOption(query)
    opt.interpret() should be (query.interpret())
    opt should be (for (i <- base.typeFilter[Int]; if i % 2 === 1) yield i)
  }
}