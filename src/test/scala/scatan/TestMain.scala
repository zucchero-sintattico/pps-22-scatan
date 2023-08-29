package scatan

import org.scalatest.flatspec.AnyFlatSpec

class TestMain extends AnyFlatSpec:

  "A dummy test that" should "pass" in
    assert(1 == 1)

  "A dummy test that" should "not pass" in
    assert(1 == 2)
