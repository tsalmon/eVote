package Elections

abstract class ElectionsManager(val notificationName: String, val electors:Set[Voting]) extends Observer {
  NotificationCenter.addObserver(this, notificationName)

  type ElectionsType <: Elections
  type VotingPaperType <: VotingPaper

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
