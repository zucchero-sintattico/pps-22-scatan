package scatan.views.game.components

import com.raquo.laminar.api.L.*
import scatan.model.ApplicationState
import scatan.model.components.ResourceType
import scatan.views.utils.TypeUtils.{DisplayableSource, reactiveState}

class Popup[P](title: String, options: Signal[ApplicationState] => Signal[Seq[P]]):
  var onSelect: Option[P => Unit] = None
  val toBeShown: Var[Boolean] = Var(false)
  def show(onSelect: P => Unit): Unit =
    this.onSelect = Some(onSelect)
    toBeShown.writer.onNext(true)
  def element: DisplayableSource[Element] =
    div(
      display <-- this.toBeShown.signal.map(if _ then "block" else "none"),
      position.fixed,
      top("50%"),
      left("50%"),
      transform := "translate(-50%, -50%)",
      padding := "20px",
      backgroundColor := "white",
      boxShadow := "0 2px 10px rgba(0, 0, 0, 0.1)",
      borderRadius := "5px",
      zIndex := 1000,
      div(
        title,
        textAlign.center,
        fontSize := "20px",
        margin := "10px"
      ),
      children <-- options(reactiveState).map(_.map { option =>
        button(
          option.toString,
          onClick.mapTo(option) --> { _ =>
            this.onSelect.foreach(_(option)); toBeShown.writer.onNext(false)
          },
          margin := "10px"
        )
      })
    )

object DevelopmentCardPopups:

  def All: Seq[DisplayableSource[Element]] = Seq(
    FirstYearOfPlentyCardPopup,
    SecondYearOfPlentyCardPopup,
    MonopolyCardPopup
  ).map(_.element)

  val FirstYearOfPlentyCardPopup =
    new Popup(
      title = "Select the first resource",
      options = _ => Var(Seq(ResourceType.values.toSeq*)).signal
    )

  val SecondYearOfPlentyCardPopup =
    new Popup(
      title = "Select the second resource",
      options = _ => Var(Seq(ResourceType.values.toSeq*)).signal
    )

  val MonopolyCardPopup =
    new Popup(
      title = "Select the resource to monopolize",
      options = _ => Var(Seq(ResourceType.values.toSeq*)).signal
    )
