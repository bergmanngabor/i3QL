package sae.functions

import sae.operators._

/**
 * A aggregation function that finds the minimum in set of domain entries
 * @author Malte V
 */
private class MinIntern[Domain <: AnyRef](val f: Domain => Int)
        extends NotSelfMaintainableAggregateFunction[Domain, Int]
{
    var min = Integer.MAX_VALUE

    def add(d: Domain, data: Iterable[Domain]) = {
        if (f(d) < min)
            min = f(d)
        min
    }

    def remove(d: Domain, data: Iterable[Domain]) = {
        if (f(d) == min) {
            min = f(data.head)
            min = (min /: data)((i, s) => if (i < f(s)) i else f(s))
        }
        min
    }

    def update(oldV: Domain, newV: Domain, data: Iterable[Domain]) = {
        if (f(oldV) == min || f(newV) < min) {
            min = f(data.head)
            min = (min /: data)((i, s) => if (i < f(s)) i else f(s))
        }
        min
    }
}

object Min
{
    def apply[Domain <: AnyRef](f: (Domain => Int)) = {
        new NotSelfMaintainableAggregateFunctionFactory[Domain, Int]
        {
            def apply(): NotSelfMaintainableAggregateFunction[Domain, Int] = {
                new MinIntern[Domain](f)
            }
        }
    }
}