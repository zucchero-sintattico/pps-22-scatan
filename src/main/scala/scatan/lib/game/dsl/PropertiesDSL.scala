package scatan.lib.game.dsl

import scala.annotation.targetName
import scala.reflect.ClassTag

object PropertiesDSL:

  // Properties

  sealed trait Property[P]:
    def apply(newValue: P): Unit

  final class OptionalProperty[P] extends Property[P]:
    var value: Option[P] = None
    override def apply(newValue: P): Unit = value = Some(newValue)

  final class SequenceProperty[P] extends Property[P]:
    var value: Seq[P] = Seq.empty
    override def apply(newValue: P): Unit = value = value :+ newValue

  // Setter

  class PropertySetter[P](property: Property[P]):
    @targetName("set")
    def :=(value: P): Unit = property(value)

  given [P]: Conversion[Property[P], PropertySetter[P]] = PropertySetter(_)

  // Updater

  private type Updater[P] = P ?=> Unit

  class PropertyUpdater[P: ClassTag](property: Property[P]):
    def apply(updater: Updater[P]): Unit =
      val prop = summon[ClassTag[P]].runtimeClass.getConstructor().newInstance().asInstanceOf[P]
      updater(using prop)
      property(prop)

  given [P: ClassTag]: Conversion[Property[P], PropertyUpdater[P]] = PropertyUpdater(_)

  // Builder

  class PropertyBuilder[P: ClassTag]:
    def apply(updater: Updater[P]): P =
      val ctx = summon[ClassTag[P]].runtimeClass.getConstructor().newInstance().asInstanceOf[P]
      updater(using ctx)
      ctx

  // Utils

  def property[P <: Property[?]](using ClassTag[P]): P =
    summon[ClassTag[P]].runtimeClass.getConstructor().newInstance().asInstanceOf[P]

  type Contexted[Ctx, P] = Ctx ?=> P
