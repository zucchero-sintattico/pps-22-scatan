package scatan.model.game.ops

import scatan.model.components.*
import scatan.model.game.ScatanState
import scatan.model.game.{BaseScatanStateTest, ScatanState}
import scatan.model.game.ops.BuildingOps.{assignBuilding, build}
import scatan.model.game.ops.CardOps.assignResourceCard
import scatan.model.game.ops.EmptySpotsOps.{emptyStructureSpot, emptyRoadSpot}
import scatan.model.game.BaseScatanStateTest
import scatan.model.game.config.ScatanPlayer
import scatan.model.map.{RoadSpot, StructureSpot}

class BuildingOpsTest extends BaseScatanStateTest:

  private def spotToBuildStructure(state: ScatanState): StructureSpot =
    state.emptyStructureSpot.head

  private def spotToBuildRoad(state: ScatanState): RoadSpot =
    state.emptyRoadSpot.head

  private def roadNearSpot(state: ScatanState, spot: StructureSpot): RoadSpot =
    state.emptyRoadSpot.filter(_.contains(spot)).head

  "A State with buildings Ops" should "have empty buildings when state start" in {
    val state = ScatanState(threePlayers)
    state.assignedBuildings.keySet should have size 0
  }

  it should "permit to assign a road" in {
    val state = ScatanState(threePlayers)
    given ScatanState = state
    state
      .assignRoadWithoutRule(spotToBuildRoad(state), threePlayers.head) match
      case Some(state) => state.assignedBuildings should have size 1
      case None        => fail("state should be defined")
  }

  it should "permit to assign a settlement" in {
    val state = ScatanState(threePlayers)
    state
      .assignBuilding(spotToBuildStructure(state), BuildingType.Settlement, threePlayers.head) match
      case Some(state) => state.assignedBuildings should have size 1
      case None        => fail("state should be defined")
  }

  it should "not permit to assign a settlement if the spot is not empty" in {
    val state = ScatanState(threePlayers)
    val spot = spotToBuildStructure(state)
    val stateWithBuilding = state.assignSettlmentWithoutRule(spot, threePlayers.head)
    stateWithBuilding match
      case Some(state) =>
        state.assignedBuildings(spot) should be(AssignmentInfo(threePlayers.head, BuildingType.Settlement))
        state
          .assignBuilding(spot, BuildingType.Settlement, threePlayers.head) match
          case Some(_) => fail("state should not be defined")
          case None    => succeed

      case None => fail("stateWithBuilding should be defined")
  }

  it should "not permit to assign a road if the spot is not empty" in {
    val state = ScatanState(threePlayers)
    val spot = spotToBuildRoad(state)
    val stateWithBuilding =
      for stateWithRoad <- state.assignBuilding(
          spot,
          BuildingType.Road,
          threePlayers.head,
          spotShouldBeEmptyToBuildRoad
        )
      yield stateWithRoad
    stateWithBuilding match
      case Some(state) =>
        state.assignedBuildings(spot) should be(AssignmentInfo(threePlayers.head, BuildingType.Road))
        state
          .assignBuilding(spot, BuildingType.Road, threePlayers.head, spotShouldBeEmptyToBuildRoad) match
          case Some(_) => fail("state should not be defined")
          case None    => succeed

      case None => fail("stateWithBuilding should be defined")
  }

  it should "not allow to assign buildings if the player has't the necessary resources" in {
    val state = ScatanState(threePlayers)
    val stateWithNoBuildings = state.build(spotToBuildStructure(state), BuildingType.Settlement, threePlayers.head)
    stateWithNoBuildings should be(None)
  }

  it should "allow to assign buildings if the player has the necessary resources" in {
    val state = ScatanState(threePlayers)
    val spot = spotToBuildStructure(state)
    val stateWithBuilding = for
      stateWithWood <- state.assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Wood))
      stateWithBrick <- stateWithWood.assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Brick))
      stateWithSheep <- stateWithBrick.assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Sheep))
      stateWithBuilding <- stateWithSheep.build(spot, BuildingType.Settlement, threePlayers.head)
    yield stateWithBuilding
    stateWithBuilding match
      case Some(state) =>
        state.assignedBuildings(spot) should be(AssignmentInfo(threePlayers.head, BuildingType.Settlement))
      case None => fail("stateWithBuilding should be defined")
  }

  it should "consume the resources when a building is assigned" in {
    val state = ScatanState(threePlayers)
    val stateWithBuilding = for
      stateWithWood <- state.assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Wood))
      stateWithBrick <- stateWithWood.assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Brick))
      stateWithSheep <- stateWithBrick.assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Sheep))
      stateWithBuilding <- stateWithSheep.build(spotToBuildStructure(state), BuildingType.Settlement, threePlayers.head)
    yield stateWithBuilding
    stateWithBuilding match
      case Some(state) => state.resourceCards(threePlayers.head) should be(Seq.empty[ResourceCard])
      case None        => fail("stateWithBuilding should be defined")
  }

  it should "allow to place a City instead of a Settlement" in {
    val state = ScatanState(threePlayers)
    val spot = spotToBuildStructure(state)
    val stateWithBuilding = for
      stateWithBuilding <- state.assignBuilding(spot, BuildingType.Settlement, threePlayers.head)
      stateWithBuilding <- stateWithBuilding.assignBuilding(spot, BuildingType.City, threePlayers.head)
    yield stateWithBuilding
    stateWithBuilding match
      case Some(state) =>
        state.assignedBuildings(spot) should be(AssignmentInfo(threePlayers.head, BuildingType.City))
      case None => fail("stateWithBuilding should be defined")
  }

  it should "not allow to place a City if there is no Settlement in the spot" in {
    val state = ScatanState(threePlayers)
    val spot = spotToBuildStructure(state)
    val stateWithBuilding = for
      stateWithWood <- state.assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Wood))
      stateWithBrick <- stateWithWood.assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Brick))
      stateWithSheep <- stateWithBrick.assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Sheep))
      stateWithWheat <- stateWithSheep.assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Wheat))
      stateWithBuilding <- stateWithWheat.build(spot, BuildingType.City, threePlayers.head)
    yield stateWithBuilding
    stateWithBuilding should be(None)
  }

  it should "not allow to place a City if there is a Settlement of another player in the spot" in {
    val state = ScatanState(threePlayers)
    val spot = spotToBuildStructure(state)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    state
      .assignBuilding(spot, BuildingType.Settlement, player1)
      .flatMap(_.assignBuilding(spot, BuildingType.City, player2)) should be(None)
  }

  it should "not allow to assign a road without nothing near" in {
    val state = ScatanState(threePlayers)
    val roadSpot = spotToBuildRoad(state)
    val stateWithRoad =
      state
        .assignBuilding(roadSpot, BuildingType.Road, threePlayers.head)
    stateWithRoad should be(None)
  }

  it should "allow to assign a road if it is near a building" in {
    val state = ScatanState(threePlayers)
    val spot = spotToBuildStructure(state)
    val roadSpot = roadNearSpot(state, spot)
    val stateAssigned =
      state
        .assignBuilding(spot, BuildingType.Settlement, threePlayers.head)
        .flatMap(_.assignBuilding(roadSpot, BuildingType.Road, threePlayers.head))
    stateAssigned should not be (None)
  }

  it should "allow to assign a road if it has road near" in {
    val state = ScatanState(threePlayers)
    val roadSpot = spotToBuildRoad(state)
    val roadSpot2 = state.gameMap.edgesOfNodesConnectedBy(roadSpot).head
    val stateAssigned =
      state
        .assignBuilding(roadSpot, BuildingType.Road, threePlayers.head, noConstrainToBuildRoad)
        .flatMap(_.assignBuilding(roadSpot2, BuildingType.Road, threePlayers.head))
    stateAssigned should not be (None)
  }

  it should "not allow to assign a building if another is near" in {
    val state = ScatanState(threePlayers)
    val spot = spotToBuildStructure(state)
    val anotherSpot = (state.gameMap.neighboursOf(spot) & state.emptyStructureSpot.toSet).head
    val stateAssigned =
      state
        .assignBuilding(anotherSpot, BuildingType.Settlement, threePlayers.head)
        .flatMap(_.assignBuilding(spot, BuildingType.Settlement, threePlayers.head))
    stateAssigned should be(None)
  }
