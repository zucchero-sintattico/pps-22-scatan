package scatan.model.map

import cats.kernel.Monoid
import cats.syntax.semigroup.*
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import scatan.BaseTest
import scatan.model.map.Hexagon.given
import scatan.model.map.HexTiledMap.*

class UnorderedPairTest extends BaseTest with ScalaCheckPropertyChecks:

  "A UnorderedPair" should "have 2 elements" in {
    assertCompiles("UnorderedPair(0, 0)")
    assertDoesNotCompile("UnorderedPair()")
  }

  it should "be equals based on its elements" in {
    forAll { (a: Int, b: Int) =>
      val pair1 = UnorderedPair(a, b)
      val pair2 = UnorderedPair(a, b)
      pair1 shouldBe pair2
    }
  }

  it should "be equals also in reverse order of its elements" in {
    forAll { (a: Int, b: Int) =>
      val pair1 = UnorderedPair(a, b)
      val pair2 = UnorderedPair(b, a)
      pair1 shouldBe pair2
    }
  }

  it should "be not equals if its elements are different" in {
    forAll { (a1: Int, b1: Int, a2: Int, b2: Int) =>
      whenever(a1 != a2 || b1 != b2) {
        val pair1 = UnorderedPair(a1, b1)
        val pair2 = UnorderedPair(a2, b2)
        pair1 should not be pair2
      }
    }
  }
