package scatan.utils

/** An unordered pair of elements.
  * @param a
  *   an element
  * @param b
  *   another element
  */
final case class UnorderedPair[A](_1: A, _2: A):
  private val _set: Set[A] = Set(_1, _2)

  override def equals(that: Any): Boolean = that match
    case UnorderedPair(a, b) => _set == Set[Any](a, b)
    case _                   => false

  override def hashCode: Int = _set.hashCode

object UnorderedPair:
  extension [A](pair: UnorderedPair[A])
    def contains(elem: A): Boolean = pair._set.contains(elem)
    def toSet: Set[A] = pair._set

/** An unordered triple of elements.
  * @param a
  *   element
  * @param b
  *   element
  * @param c
  *   element
  */
final case class UnorderedTriple[A](_1: A, _2: A, _3: A):
  private val _set: Set[A] = Set(_1, _2, _3)

  override def equals(obj: Any): Boolean = obj match
    case UnorderedTriple(a, b, c) => _set == Set[Any](a, b, c)
    case _                        => false

  override def hashCode: Int = _set.hashCode

object UnorderedTriple:
  extension [A](triple: UnorderedTriple[A])
    def toSet: Set[A] = triple._set
    def contains(elem: A): Boolean = triple._set.contains(elem)
