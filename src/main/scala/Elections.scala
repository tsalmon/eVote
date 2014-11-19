package Elections

abstract class Elections(val candidates:List[Electable], val votes: Set[VotingPaper]) {
  type VoteResult
  var result: Option[VoteResult] = None
}
