package ElectionsResultProcessing

import Elections.{Electable, Elections}

abstract class ElectionsResultProcessing[ElectionsType <: Elections]{
  def calculateResult(elections: ElectionsType): elections.VoteResult
}
