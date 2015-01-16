package Elections

abstract class VotingPaper(val notificationName: String, val district: District) {
  // Type of vote input – set of Electables (e.g. list of candidates, list of parties, groups of candidates)
  type VoteInput

  // Type of vote output – choice of Voting (e.g. candidate, party, set of candidates, sequence of candidates)
  type VoteOutput

  var input: VoteInput
  var output: VoteOutput

  // Represents voting process
  def performVote

  protected var confirmed = false

  // Send VotingPaper to ElectionsManager by notification and after that votingpaper can't be modified

  def confirm =
    this.confirmed match{
      case true => ()
      case false => confirmed = true
                    NotificationCenter.postNotification(notificationName, this)
    }

  final def vote =
    this.confirmed match {
      case true => println("VotingPaper is already finalized and can't be modified")
      case false => performVote
    }

  override def toString = output.toString
}