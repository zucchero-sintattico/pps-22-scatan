package scatan.lib.game.dsl

import scala.annotation.targetName
import scala.reflect.ClassTag

object PropertiesDSL:

  // Properties

  sealed trait Property[P]:
    def apply(newValue: P): Unit

  final case class OptionalProperty[P](var value: Option[P] = None) extends Property[P]:
    override def apply(newValue: P): Unit = value = Some(newValue)

  final case class SequenceProperty[P](var value: Seq[P] = Seq.empty[P]) extends Property[P]:
    override def apply(newValue: P): Unit = value = value :+ newValue

  class MonadProperty[P <: IterableOnce[T], T](elem: P):
    def foreach(f: T => Unit): Unit = elem.iterator.foreach(f)
    def map[R](f: T => R): Seq[R] = elem.iterator.map(f).toSeq
    def flatMap[R](f: T => Seq[R]): Seq[R] = elem.iterator.flatMap(f).toSeq

  given [P]: Conversion[OptionalProperty[P], MonadProperty[Option[P], P]] with
    def apply(optionalProperty: OptionalProperty[P]): MonadProperty[Option[P], P] =
      new MonadProperty(optionalProperty.value)

  given [P]: Conversion[SequenceProperty[P], MonadProperty[Seq[P], P]] with
    def apply(sequenceProperty: SequenceProperty[P]): MonadProperty[Seq[P], P] =
      new MonadProperty(sequenceProperty.value)


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
