package ivm
package tests

import org.scalatest.junit.{ShouldMatchersForJUnit, JUnitSuite}
import org.junit.Test

import expressiontree._

import expressiontree.Lifting._
import optimization.Optimization
import optimization.SubquerySharing


/**
* User: pgiarrusso
* Date: 25/8/2011
*/

class SubquerySharingTests extends JUnitSuite with ShouldMatchersForJUnit {
  val l: Exp[Traversable[(Int,Int)]] = toExp(Vector.range(1,3).flatMap( (i) => Vector.range(1,2).map((i,_))))

  @Test def testSimpleSharing {
    val s1 = l.map(p => (p._1 + 1, p._2 + 2))
    val ress1 = s1.interpret()
    val subqueries: Map[Exp[_],_] = Map(s1 -> ress1)
    val q = l.map(p => (p._1 + 1, p._2 + 2)).withFilter( _._1 is 5)
    val res = new SubquerySharing(subqueries).shareSubqueries(q)
    res should not equal (q)
    res should equal (Const(ress1).withFilter( _._1 is 5))

    val q2 = l.map(p => (p._1 + 2, p._2 + 1)).withFilter(_._1 is 5)
    val res2 = new SubquerySharing(subqueries).shareSubqueries(q2)
    res2 should equal  (q2)
  }

  @Test def testIndexing {
    val index = l.groupBy(p => p._2 + p._1)
    val indexres = index.interpret()
    val subqueries : Map[Exp[_],_] = Map(index -> indexres)
    val testquery = l.withFilter(p => p._1 + p._2 is 5)
    val optimized = new SubquerySharing(subqueries).shareSubqueries(testquery)
    optimized should equal (App(Const(indexres),Const(5)))
  }

  @Test def testCNFconversion {
    val index = l.groupBy(p => p._1 + p._2)
    val indexres = index.interpret()
    val subqueries : Map[Exp[_],_] = Map(index -> indexres)
    val testquery = l.withFilter(p => (p._1 <= 7) && (p._1 + p._2 is 5))
    val optimized = new SubquerySharing(subqueries).shareSubqueries(testquery)
    optimized should equal (App(Const(indexres),Const(5)).withFilter(p => p._1 <= 7))
  }

}