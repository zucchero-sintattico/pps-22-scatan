package scatan.lib.game.dsl

import cats.Monad

import scala.annotation.targetName

object PropertiesDSL:

  // Properties

  sealed trait Property[P]:
    def apply(newValue: P): Unit

  final case class OptionalProperty[P](var value: Option[P] = None) extends Property[P]:
    override def apply(newValue: P): Unit = value = Some(newValue)

  final case class SequenceProperty[P](var value: Seq[P] = Seq.empty[P]) extends Property[P]:
    override def apply(newValue: P): Unit = value = value :+ newValue

  given [P]: Conversion[OptionalProperty[P], Iterable[P]] with
    def apply(optionalProperty: OptionalProperty[P]): Iterable[P] =
      optionalProperty.value.toList

  given [P]: Conversion[SequenceProperty[P], Iterable[P]] with
    def apply(sequenceProperty: SequenceProperty[P]): Iterable[P] =
      sequenceProperty.value

  // Setter

  class PropertySetter[P](property: Property[P]):
    @targetName("set")
    def :=(value: P): Unit = property(value)

  given [P]: Conversion[Property[P], PropertySetter[P]] = PropertySetter(_)

  // Updater

  private type Updater[P] = P ?=> Unit

  trait Factory[P]:
    def apply(): P

  class PropertyUpdater[P: Factory](property: Property[P]):
    def apply(updater: Updater[P]): Unit =
      val prop = summon[Factory[P]].apply()
      updater(using prop)
      property(prop)

  given [P: Factory]: Conversion[Property[P], PropertyUpdater[P]] = PropertyUpdater(_)

  // Builder

  class PropertyBuilder[P: Factory]:
    def apply(updater: Updater[P]): P =
      val prop = summon[Factory[P]].apply()
      updater(using prop)
      prop

  type Contexted[Ctx, P] = Ctx ?=> P
