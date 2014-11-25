package Statistics

import Elections.{District, Electable, VotingPaper, Elections}


abstract class Statistics {
 type VotingPapers = List[VotingPaper]
 var candidateResults : List[CandidateResult]
 var districtResults: List[DistrictResult]
 
  def set_Stat_CandidateResult(election: Elections) = {
	 for (candidate <- election.candidates){
//	   (CandidateResult(candidate))::candidateResults
	 }
  }
 
  def set_Stat_DistrictResult(election: Elections) =  {
	 for(district <- election.districts){
	   
	 }
  }
  
}

abstract class CandidateResult(val _candidate:Electable){
    var candidate = _candidate;
    var nbVotes : Int;
    var result : Double;
    def caculateCandidatResult(votingPapers: List[VotingPaper])= {
      for (voting <- votingPapers){
	      if (voting.toString.contains(_candidate)) {
	        this.nbVotes= this.nbVotes + 1;
	      }
      }
      this.result = votingPapers.length / this.nbVotes *100 
    }
}

abstract class DistrictResult(val _district:District){

	var resultDistrict : List[(Electable,Int, Double)]; // List((electable, (votes, percent)))
	var votesDistrict : Int; // for counter the votes in this District
	votesDistrict = 0;
	
	// setting the format of the result in this District
	def formResultDistrict(election: Elections) = {
	  for (candidate <- election.candidates){
	    resultDistrict = (candidate,0,0.00)::resultDistrict
	  }
	}
	
    def calculateDistrictResult(votingPapers: List[VotingPaper]) = {
      
      for (voting <- votingPapers){
        if (voting.district == _district){
          votesDistrict = votesDistrict+1
          for ((electable,votes, percent) <- resultDistrict){
            if(voting.toString.contains(electable)){
//              (electable,votes+1,votes+1/votesDistrict)
	        }
          }
          
        }
      }
    }
}