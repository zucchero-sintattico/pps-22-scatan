package scatan.utils

import cats.kernel.Monoid
import cats.syntax.semigroup.*
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import scatan.BaseTest
import scatan.model.map.Hexagon.given
import scatan.model.map.HexTiledMap.*
import scatan.utils.{UnorderedPair, UnorderedTriple}

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

  it should "be not equals if, at least, one element is different" in {
    val itemsInPair = 2
    forAll { (a1: Int, b1: Int, a2: Int, b2: Int) =>
      whenever((Set(a1, b1) | Set(a2, b2)).sizeIs > itemsInPair) {
        val pair1 = UnorderedPair(a1, b1)
        val pair2 = UnorderedPair(a2, b2)
        pair1 should not be pair2
      }
    }
  }

class UnorderedTripleTest extends BaseTest with ScalaCheckPropertyChecks:

  "A UnorderedTriple" should "have 3 elements" in {
    assertCompiles("UnorderedTriple(0, 0, 0)")
    assertDoesNotCompile("UnorderedTriple()")
  }

  it should "be equals based on its elements" in {
    forAll { (a: Int, b: Int, c: Int) =>
      val triple1 = UnorderedTriple(a, b, c)
      val triple2 = UnorderedTriple(a, b, c)
      triple1 shouldBe triple2
    }
  }

  it should "be equals also in reverse order of its elements" in {
    forAll { (a: Int, b: Int, c: Int) =>
      val triple1 = UnorderedTriple(a, b, c)
      val triple2 = UnorderedTriple(c, a, b)
      val triple3 = UnorderedTriple(b, c, a)
      triple1 shouldBe triple2
      triple1 shouldBe triple3
    }
  }

  it should "be not equals if, at least, one element is different" in {
    val itemsInTriple = 3
    forAll { (a1: Int, b1: Int, c1: Int, a2: Int, b2: Int, c2: Int) =>
      whenever((Set(a1, b1, c1) | Set(a2, b2, c2)).sizeIs > itemsInTriple) {
        val triple1 = UnorderedTriple(a1, b1, c1)
        val triple2 = UnorderedTriple(a2, b2, c2)
        triple1 should not be triple2
      }
    }
  }
