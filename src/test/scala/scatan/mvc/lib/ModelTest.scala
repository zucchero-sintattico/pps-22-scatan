package scatan.mvc.lib

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ModelTest extends AnyFlatSpec:

  "The Model Object" should "contains the State trait" in {
    val temp: Model.State = new Model.State {}
  }

  it should "contains the Interface class" in {
    val state: Model.State = new Model.State {}
    val temp: Model.Interface[Model.State] = new Model.Interface(state)
  }

  it should "contains the Provider trait" in {
    val temp: Model.Provider[Model.State] = new Model.Provider[Model.State]:
      override def model: Model.Interface[Model.State] = Model(new Model.State {})
  }

  it should "be creatable with a State" in {
    val state: Model.State = new Model.State {}
    val model: Model.Interface[Model.State] = Model(state)
    model should not be null
    model.getClass should be(classOf[Model.Interface[Model.State]])
  }
