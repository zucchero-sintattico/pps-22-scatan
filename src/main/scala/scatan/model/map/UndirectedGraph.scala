package scatan.model.map

/** A graph is a set of nodes and edges.
  */
trait UndirectedGraph[Node, Edge <: UnorderedPair[Node, Node]]:

  /** @return
    *   the set of nodes
    */
  def nodes: Set[Node]

  /** @return
    *   the set of edges
    */
  def edges: Set[Edge]

/** An unordered pair of elements.
  * @param a
  *   an element
  * @param b
  *   another element
  */
case class UnorderedPair[A, B](a: A, b: B):
  override def equals(that: Any): Boolean = that match
    case UnorderedPair(a1, b1) => (a == a1 && b == b1) || (a == b1 && b == a1)
    case _                     => false

  override def hashCode: Int = a.hashCode + b.hashCode
