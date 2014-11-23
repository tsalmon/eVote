package Statistics

import Elections.{District, Electable, VotingPaper}

abstract class Statistics {
  var votingPapers:List[VotingPaper]
}

abstract class CandidateResult(val candidate:Electable){

}

abstract class DistrictResult(val district:District){

}