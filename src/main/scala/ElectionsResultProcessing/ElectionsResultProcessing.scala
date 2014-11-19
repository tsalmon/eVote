package ElectionsResultProcessing

import Elections.{Electable, Elections}

abstract class ElectionsResultProcessing{
  type ElectionsType <: Elections
  def calculateResult(elections: ElectionsType): elections.VoteResult
}
