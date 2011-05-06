package sae
package collections

/**
 * A relation backed by a multi set for efficient access to elements.
 * Each element may have multiple occurrences in this relation.
 */
trait Bag[V <: AnyRef]
        extends Collection[V] //	 	with HashIndexedRelation[V]
        {

    import com.google.common.collect.HashMultiset;
    private val data : HashMultiset[V] = HashMultiset.create[V]()

    def size : Int =
        {
            data.size()
        }

    def singletonValue : Option[V] =
        {
            if (size != 1)
                None
            else
                Some(data.iterator().next())
        }

    def add_element(v : V) : Unit =
        {
            data.add(v)
        }

    def remove_element(v : V) : Unit =
        {
            data.remove(v)
        }

    def materialized_foreach[U](f : V => U) : Unit =
        {
            val it : java.util.Iterator[V] = data.iterator()
            while (it.hasNext()) {
                f(it.next())
            }
        }

   /*
    def copy : Collection[V] =
        {
            val copy = new Bag[V] { def materialize() : Unit = { /* nothing to do, the set itself is the data */ } }
            this.foreach(e =>
                {
                    copy.add_element(e)
                }
            )
            copy
        }
    */      
}