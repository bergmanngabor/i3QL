package ivm.expressiontree

import collection.TraversableView

//case class View[T](col: Exp[QueryReifier[T]]) extends Exp[TraversableView[T, Traversable[T]]] {
//override def interpret = col.interpret().exec().view //XXX. The return value should be executed when exec() is called.
case class View[T](col: QueryReifier[T]) extends ChildlessQueryReifier[T] {
  override def exec() = col.exec().view
}

case class Force[T](col: QueryReifier[T]) extends ChildlessQueryReifier[T]  {
  /* XXX the default case might be unwanted - why should it ever trigger?
   * Do we build Force nodes on non-views?
   * We probably want to forbid that when constructing the expression tree, i.e., when
   * invoking .force and building this node.
   * XXX asInstanceOf is needed because of erasure, and it's uglymost - especially because it means
   * constructing a collection (instead of whatever was originally constructed).
   */
  override def exec() = col.exec() match {
    case view: TraversableView[_, _] => view.asInstanceOf[TraversableView[T, Traversable[T]]].force
    case coll => coll
  }
}
/*case class View[T](col: TraversableView[T, Traversable[T]]) extends Exp[TraversableView[T, Traversable[T]]] {
  override def interpret = col
  override def genericConstructor = _ => this
  override def children = Seq()
}*/
