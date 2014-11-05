package ElectionsResultProcessing

import Elections.{Electable, Elections}

abstract class ElectionsResultProcessing {
  var candidates:Option[Electable] = None
  def calculateResult(elections:Elections): elections.VoteResult
}
