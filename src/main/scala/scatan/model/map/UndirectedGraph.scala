package scatan.model.map

import scatan.utils.UnorderedPair

/** A graph is a set of nodes and edges.
  */
trait UndirectedGraph[Node, Edge <: UnorderedPair[Node]]:

  /** @return
    *   the set of nodes
    */
  def nodes: Set[Node]

  /** @return
    *   the set of edges
    */
  def edges: Set[Edge]
