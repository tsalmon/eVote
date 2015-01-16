package Elections


abstract class Elections(val candidates:List[Electable], val votes: List[VotingPaper], var parent: Option[Elections] = None) {
  type VoteResult
  type VotingPaperType
  var result: Option[VoteResult] = None
}

abstract class LeafElections(override val candidates:List[Electable], override val votes: List[VotingPaper]) extends Elections(candidates, votes){
}

abstract class ComposedElections[Subelections <: Elections](override val candidates:List[Electable], override val votes: List[VotingPaper], private var subelections:Set[Subelections]) extends Elections(candidates, votes){
  subelections.foreach({_.parent = Some(this)})

  def this(candidates:List[Electable], votes: List[VotingPaper]) = this(candidates, votes, Set[Subelections]())
  def this(candidates:List[Electable], votes: List[VotingPaper], subelections:Subelections *) = this(candidates, votes, Set(subelections: _*)); subelections.foreach({_.parent = Some(this)})
  def addSubelections(subelections: Subelections*) = subelections.foreach({s=> s.parent = Some(this); this.subelections += s})
  def removeSubelections(subelections: Subelections*) = subelections.foreach({s=> s.parent = None; this.subelections -= s})
}