package ivm.expressiontree

import collection.mutable.{Buffer, Set}

// This is for a modifiable sequence. Actually, it's a problem since we don't preserve ordering, but we should.
// On the one hand, this interface matches a relation in the closest way.
trait ObservableBuffer[T] extends Buffer[T] with MsgSeqPublisher[T] {
  type Pub <: ObservableBuffer[T]
  abstract override def clear() = {
    //publish(Script(this.map(Remove(_))))
    publish(Reset())
    super.clear()
  }
  abstract override def += (el: T) = {
    publish(Include(el))
    super.+=(el)
  }
  abstract override def +=: (el: T) = {
    publish(Include(el))
    super.+=:(el)
  }
  abstract override def update(n: Int, newelem: T) = {
    publish(Update(this(n), newelem))
    super.update(n, newelem)
  }
  abstract override def remove(n: Int) = {
    publish(Remove(this(n)))
    super.remove(n)
  }
  abstract override def insertAll(n: Int, iter: Traversable[T]) = {
    // Where we call toSeq is important - by creating the sequence once, we guarantee that the order of events is the
    // same as the actual insertion order.
    val seq = iter.toSeq
    publish(seq.map(Include(_)))
    super.insertAll(n, seq)
  }
}

// Here we don't get info about replaced elements. However, for observable elements, we should still register ourselves
// to forward them.
trait ObservableSet[T] extends Set[T] with MsgSeqPublisher[T] {
  type Pub <: ObservableSet[T]
  abstract override def clear() = {
    //publish(Script(this.map(Remove(_))))
    publish(Reset())
    super.clear()
  }
  abstract override def += (el: T) = {
    // If we `Include` an element twice, we'll need to `Remove` it twice as well before e.g. the final IncrementalResult
    // instance realizes that it should disappear.
    if (!this(el))
      publish(Include(el))
    super.+=(el)
  }
  abstract override def -= (el: T) = {
    if (this(el))
      publish(Remove(el))
    super.-=(el)
  }
}