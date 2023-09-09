package scatan.model

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class PlayerTest extends AnyFlatSpec with should.Matchers:

  "A Player" should "exists" in {
    val player: Player = Player("name", Color.Red)
    player should not be null
  }

  it should "have a name" in {
    val player: Player = Player("name", Color.Red)
    player.name should be("name")
  }

  it should "have a color" in {
    val player: Player = Player("name", Color.Red)
    player.color should be(Color.Red)
  }

  it should "have buildings" in {
    val player: Player = Player("name", Color.Red, Seq.empty)
    player.buildings should be(Seq.empty[Building])
  }

  it should "have resources" in {
    val player: Player = Player("name", Color.Red, Seq.empty, Seq.empty)
    player.resources should be(Seq.empty[ResourceCard])
  }

  it should "have development cards" in {
    val player: Player = Player("name", Color.Red, Seq.empty, Seq.empty, Seq.empty)
    player.developmentCards should be(Seq.empty[DevelopmentCard])
  }

  it should "be able to add a building" in {
    val player: Player = Player("name", Color.Red)
    val building: Building = Building(BuildingType.City)
    val playerWithBuilding: Player = player.addBuilding(building)
    playerWithBuilding.buildings should be(Seq(building))
  }

  it should "be able to add a resource" in {
    val player: Player = Player("name", Color.Red)
    val resource: ResourceCard = ResourceCard(ResourceType.Brick)
    val playerWithResource: Player = player.addResource(resource)
    playerWithResource.resources should be(Seq(resource))
  }

  it should "be able to add a development card" in {
    val player: Player = Player("name", Color.Red)
    val developmentCard: DevelopmentCard = DevelopmentCard(DevelopmentType.Knight)
    val playerWithDevelopmentCard: Player = player.addDevelopmentCard(developmentCard)
    playerWithDevelopmentCard.developmentCards should be(Seq(developmentCard))
  }
