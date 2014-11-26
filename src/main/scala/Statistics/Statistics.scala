package Statistics

import Elections.{District, Electable, VotingPaper, Elections}


abstract class Statistics {
   var nbElectionVotes : Int
   var candidateResults : List[CandidateResult]
   var districtResults: List[DistrictResult]
   
   def addCandidateResult(CR: CandidateResult) = CR::candidateResults
   def addDistrictResult(DR: DistrictResult)= DR::districtResults
   def removeCandidateResult(CR: CandidateResult)= {
     candidateResults.filter(_ != CR);
   }
   def removeDistrictResult(DR: DistrictResult)= {
     districtResults.filter(_ != DR);
   }
}

abstract class CandidateResult(val _candidate:Electable){
    var nbVotes : Int;
    var listVotes : List[VotingPaper] // list votingPaper was voted for this Electable
    var result : Double;
    def addVote(vote : VotingPaper) = vote::listVotes
    def removeVote(vote: VotingPaper) = listVotes.filter(_ != vote)
}

abstract class DistrictResult(val _district:District){
  
  var nbDistrictVotes: Int
  var districtVotingPapers: List[VotingPaper]
  
  // List result in this district : List((electable, votes, percent))
  var resultDistrict : List[CandidateResult]; 
  
  def add_districtVotingPaper(votingPaper: VotingPaper)= votingPaper::districtVotingPapers
  
  def remove_districtVotingPaper(votingPaper: VotingPaper) = districtVotingPapers.filter(_ != votingPaper)
  
  // filter votingPaper of District in list_VotingPaper of Election
    def set_districtVotingPapers(votingPapers:List[VotingPaper])={
      for(votingPaper <- votingPapers){
        if (votingPaper.district == _district){
          add_districtVotingPaper(votingPaper)
        }
      }
    } 
    
  def add_resultDistrict(CR: CandidateResult) = CR::resultDistrict
  
  def remove_resultDistrict(CR: CandidateResult) = resultDistrict.filter(_ != CR)
   
}