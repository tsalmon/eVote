package Elections

abstract class VotingPaper {
  type VoteOutput
  type VoteInput

  var input: VoteInput
  var output: VoteOutput
  protected def performVote

  private var confirmed = false
  def confirm = confirmed = true

  def isVoted:Boolean

  final def vote =
    this.confirmed match {
      case true => println("VotingPaper is already finalized and can't be modified")
      case false => performVote
    }

  def voteResult:String

  override def toString = output.toString
}