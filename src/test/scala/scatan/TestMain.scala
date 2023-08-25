package scatan

import org.scalatest.flatspec.AnyFlatSpec

class TestMain extends AnyFlatSpec:

  "An dummyFunction" should "return 1" in:
    assert(dummyFunction() == 1)
