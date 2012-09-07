package sae.syntax.sql.impl

import sae.syntax.sql.ast._
import predicates.{Filter, Predicate}
import sae.syntax.sql
import sql.{WHERE_CLAUSE_EXPRESSION_2, WHERE_CLAUSE_FINAL_SUB_EXPRESSION_2}

/**
 * Created with IntelliJ IDEA.
 * User: Ralf Mitschke
 * Date: 05.08.12
 * Time: 16:42
 */

case class WhereClause2Expression[DomainA <: AnyRef, DomainB <: AnyRef](conditions: Seq[WhereClauseExpression])
    extends WHERE_CLAUSE_EXPRESSION_2[DomainA, DomainB]
{
    def AND(predicate: (DomainA) => Boolean) =
        WhereClause2Expression (conditions ++ Seq (AndOperator, Filter (predicate)))

    def OR(predicate: (DomainA) => Boolean) =
        WhereClause2Expression (conditions ++ Seq (OrOperator, Filter (predicate)))

    def AND[RangeA, RangeB](join: sql.JOIN_CONDITION[DomainA, DomainB, RangeA, RangeB]) =
        WhereClause2Expression (conditions ++ Seq (AndOperator, join))

    def OR[RangeA, RangeB](join: sql.JOIN_CONDITION[DomainA, DomainB, RangeA, RangeB]) =
        WhereClause2Expression (conditions ++ Seq (OrOperator, join))

    def AND(subExpression: WHERE_CLAUSE_FINAL_SUB_EXPRESSION_2[DomainA, DomainB]) =
        WhereClause2Expression (conditions ++ Seq (AndOperator, subExpression))

    def OR(subExpression: WHERE_CLAUSE_FINAL_SUB_EXPRESSION_2[DomainA, DomainB]) =
        WhereClause2Expression (conditions ++ Seq (OrOperator, subExpression))

    type Representation = Seq[WhereClauseExpression]

    def representation = conditions
}
