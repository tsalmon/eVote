package Elections

abstract class ElectionsManager {
  type VoteInput
  type VoteOutput
  def createVotingPaper(voting: Voting): VotingPaper
  def acceptVotingPaper(votingPaper: VotingPaper)
}
