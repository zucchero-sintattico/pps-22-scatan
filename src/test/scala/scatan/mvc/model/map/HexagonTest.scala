package scatan.mvc.model.map

import cats.kernel.Monoid
import cats.syntax.semigroup.*
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import scatan.BaseTest
import scatan.mvc.model.map.Hexagon.given

class HexagonTest extends BaseTest with ScalaCheckPropertyChecks:

  "An Hexagon" should "have 3 coordinates" in {
    assertCompiles("Hexagon(0, 0, 0)")
    assertDoesNotCompile("Hexagon()")
  }

  it should "be equals based on its coordinates" in {
    forAll { (r: Int, c: Int, s: Int) =>
      val hexagon1 = Hexagon(r, c, s)
      val hexagon2 = Hexagon(r, c, s)
      hexagon1 should equal(hexagon2)
    }
  }

  it should "respect the identity law in order to be a Monoid" in {
    forAll { (r: Int, c: Int, s: Int) =>
      val hexagon = Hexagon(r, c, s)
      hexagon |+| Monoid[Hexagon].empty should be(hexagon)
      Monoid[Hexagon].empty |+| hexagon should be(hexagon)
    }
  }

  it should "respect the associative law in order to be a Monoid" in {
    forAll { (r1: Int, c1: Int, s1: Int, r2: Int, c2: Int, s2: Int) =>
      val hexagon1 = Hexagon(r1, c1, s1)
      val hexagon2 = Hexagon(r2, c2, s2)
      hexagon1 |+| hexagon2 should be(hexagon2 |+| hexagon1)
    }
  }

  it should "have 6 neighbours" in {
    forAll { (r: Int, c: Int, s: Int) =>
      val hexagon = Hexagon(r, c, s)
      hexagon.neighbours.size should equal(6)
    }
  }
