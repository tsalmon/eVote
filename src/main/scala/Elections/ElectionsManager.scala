package Elections

abstract class ElectionsManager[ElectionsType <: Elections, VotingPaperType <: VotingPaper](val notificationName: String, val electors:Set[Voting]) extends Observer {
  NotificationCenter.addObserver(this, notificationName)

  protected var votes = List[VotingPaperType]()

  def createVotingPaper(voting: Voting): VotingPaperType

  def notify(notification:Notification) = {
    val votingPaper = notification.internalObject
    notification.internalObject match{
      case votingPaper : Some[VotingPaperType] => votes =  votingPaper.get::votes
      case _ => ()
    }
  }
  def createElections:ElectionsType

  def printVotes = println(votes)
}
