package sae.syntax.sql.impl

import sae.syntax.sql.{WHERE_CLAUSE_EXPRESSION, WHERE_CLAUSE_FINAL_SUB_EXPRESSION}
import sae.syntax.sql.ast._
import predicates.{Filter, Predicate}

/**
 * Created with IntelliJ IDEA.
 * User: Ralf Mitschke
 * Date: 05.08.12
 * Time: 20:41
 *
 */
case class WhereClause1Expression[Domain <: AnyRef](conditions: Seq[WhereClauseExpression])
    extends WHERE_CLAUSE_EXPRESSION[Domain]
{
    def AND(predicate: (Domain) => Boolean) =
        WhereClause1Expression (conditions ++ Seq (AndOperator, Filter (predicate)))

    def OR(predicate: (Domain) => Boolean) =
        WhereClause1Expression (conditions ++ Seq (OrOperator, Filter (predicate)))


    def AND(subExpression: WHERE_CLAUSE_FINAL_SUB_EXPRESSION[Domain]) =
        WhereClause1Expression (conditions ++ Seq (AndOperator, subExpression))

    def OR(subExpression: WHERE_CLAUSE_FINAL_SUB_EXPRESSION[Domain]) =
        WhereClause1Expression (conditions ++ Seq (OrOperator, subExpression))

    type Representation = Seq[WhereClauseExpression]

    def representation = conditions
}
