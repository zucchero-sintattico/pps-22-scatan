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

/** A mixin that add ops over a graph.
  */
trait UndirectedGraphOps[Node, Edge <: UnorderedPair[Node]] extends UndirectedGraph[Node, Edge]:

  /** Returns the set of edges that are connected to the given node.
    *
    * @param node
    *   the node to get the edges
    * @return
    *   the edges that are connected to the given node
    */
  def edgesOf(node: Node): Set[Edge] = edges.filter(_.contains(node))

  /** Returns the set of nodes that are neighbours of the given node.
    *
    * @param node
    *   the node to get the neighbours
    * @return
    *   the nodes that are neighbours of the given node
    */
  def neighboursOf(node: Node): Set[Node] = edgesOf(node).flatMap(_.toSet).filterNot(_ == node)

  /** Returns the set of edges that are connected to the nodes those are connected by the given edge.
    *
    * @param edge
    *   the edge to start from
    *
    * @return
    *   the set of edges
    */
  def edgesOfNodesConnectedBy(edge: Edge): Set[Edge] =
    (edgesOf(edge._1) | edgesOf(edge._2)).filterNot(_ == edge)
