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
      hexagon1 shouldBe hexagon2
    }
  }

  it should "respect the identity law in order to be a Monoid" in {
    forAll { (r: Int, c: Int, s: Int) =>
      val hexagon = Hexagon(r, c, s)
      hexagon |+| Monoid[Hexagon].empty shouldBe hexagon
      Monoid[Hexagon].empty |+| hexagon shouldBe hexagon
    }
  }

  it should "respect the associative law in order to be a Monoid" in {
    forAll { (r1: Int, c1: Int, s1: Int, r2: Int, c2: Int, s2: Int) =>
      val hexagon1 = Hexagon(r1, c1, s1)
      val hexagon2 = Hexagon(r2, c2, s2)
      hexagon1 |+| hexagon2 shouldBe (hexagon2 |+| hexagon1)
    }
  }

  it should "have 6 neighbours" in {
    forAll { (r: Int, c: Int, s: Int) =>
      val hexagon = Hexagon(r, c, s)
      hexagon.neighbours should have size 6
    }
  }

  "An Hexagon in the center" should "be in layer 0" in {
    val centeredHexagon = Hexagon(0, 0, 0)
    centeredHexagon.layer shouldBe 0
  }

  it should "have neighbours at layer 1" in {
    val centeredHexagon = Hexagon(0, 0, 0)
    all(centeredHexagon.neighbours.map(_.layer)) shouldBe 1
  }
