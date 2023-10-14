package scatan.lib.game.dsl

import scala.annotation.targetName

object PropertiesDSL:

  // Properties

  /** An updatable property.
    * @tparam P
    *   the type of the property
    */
  sealed trait UpdatableProperty[P]:
    def apply(newValue: P): Unit

  /** An optional property, which can be updated with a new value.
    * @param value
    *   the current value of the property
    * @tparam P
    *   the type of the property
    */
  final case class OptionalProperty[P](var value: Option[P] = None) extends UpdatableProperty[P]:
    override def apply(newValue: P): Unit = value = Some(newValue)

  /** A sequence property, in which new values can be added.
    * @param value
    *   the new value to add to the sequence
    * @tparam P
    *   the type of the property
    */
  final case class SequenceProperty[P](var value: Seq[P] = Seq.empty[P]) extends UpdatableProperty[P]:
    override def apply(newValue: P): Unit = value = value :+ newValue

  given [P]: Conversion[OptionalProperty[P], Iterable[P]] with
    def apply(optionalProperty: OptionalProperty[P]): Iterable[P] =
      optionalProperty.value.toList

  given [P]: Conversion[SequenceProperty[P], Iterable[P]] with
    def apply(sequenceProperty: SequenceProperty[P]): Iterable[P] =
      sequenceProperty.value

  // Setter

  /** A property setter, which can be used to update a property with a new value.
    * @param property
    *   the property to update
    * @tparam P
    *   the type of the property
    */
  class PropertySetter[P](property: UpdatableProperty[P]):
    @targetName("set")
    def :=(value: P): Unit = property(value)

  given [P]: Conversion[UpdatableProperty[P], PropertySetter[P]] = PropertySetter(_)

  // Updater

  /** An object builder, which can be used to update an object. Basically, it is a function which takes an implicit
    * parameter of type `P` and returns `Unit`.
    */
  private type Builder[P] = P ?=> Unit

  trait Factory[P]:
    def apply(): P

  /** A property builder. It creates a new object and build it with the given builder. The property is then updated with
    * the built value.
    * @param property
    *   the property to update
    * @param factory$P$0
    *   the factory to create a new object
    * @tparam P
    *   the type of the property
    */
  class PropertyBuilder[P: Factory](property: UpdatableProperty[P]):
    def apply(builder: Builder[P]): Unit =
      val obj = summon[Factory[P]].apply()
      builder(using obj)
      property(obj)

  given [P: Factory]: Conversion[UpdatableProperty[P], PropertyBuilder[P]] = PropertyBuilder(_)

  // Builder

  /** A property creator. It creates a new object and build it with the given builder.
    * @param factory$P$0
    *   the factory to create a new object
    * @tparam P
    *   the type of the property
    */
  class ObjectBuilder[P: Factory]:
    def apply(builder: Builder[P]): P =
      val obj = summon[Factory[P]].apply()
      builder(using obj)
      obj

  /** A contexted function, which impose a given context to the function.
    * @tparam Ctx
    *   the required context
    * @tparam P
    *   the type of the function
    */
  type Contexted[Ctx, P] = Ctx ?=> P
