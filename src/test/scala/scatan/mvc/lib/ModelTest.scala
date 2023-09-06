package scatan.mvc.lib

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ModelTest extends AnyFlatSpec:

  "The Model Object" should "contains the State trait" in {
    val temp: Model.State = new Model.State {}
  }

  it should "be creatable with a State" in {
    val state: Model.State = new Model.State {}
    val model: Model[Model.State] = Model(state)
    model should not be null
    model.state should be(state)
    model.isInstanceOf[Model[?]] should be(true)
  }

  it should "contains the Provider trait" in {
    val temp: Model.Provider[Model.State] = new Model.Provider[Model.State]:
      override def model: Model[Model.State] = Model(new Model.State {})
  }
