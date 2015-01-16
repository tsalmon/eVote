package Doodle

/**
 * Created by Vladislav Fitc on 05/01/15.
 */

class Doodle(val title: String, val location: String, val description: String, val organizator: DoodleMember, participants: List[DoodleMember], val timeSlots: List[TimeSlot], limit: Option[Int], single: Boolean, val public: Boolean, ifNeedBeOption: Boolean){
  private val manager = new DoodleManager(limit,new Location(location), single, public, ifNeedBeOption ,participants.toSet, timeSlots, title+location+description)

  def showCurrentState(): Unit = {
    manager.printVotes
  }

  /* On peut soit précharger le vote en utilisant une variable "preliminaryVotes"
    soit passer "None" et utiliser la fonction "vote" de VotingPaper
  */

  def makeVote(participant: DoodleMember, preliminaryVotes: Option[Array[Availability]] = None) = {
    val vp = manager.createVotingPaper(participant)
    preliminaryVotes match {
      case Some(apre) => vp.preliminaryInput(apre)
      case None => vp.vote
    }

    vp.confirm
  }


}

object DoodleTest extends App{

  //Pre:
  val m1 = new DoodleMember("Member1")
  val m2 = new DoodleMember("Member2")
  val m3 = new DoodleMember("Member3")

  val ts1 = new TimeSlot("27/01/2015")
  val ts2 = new TimeSlot("28/01/2015")
  val ts3 = new TimeSlot("29/01/2015")

  //Tests:

  def test1:Boolean = {
    val d = m1.createDoodle("Meeting", "1 rue st. Honore, 75001, Paris", "Discussions about contract", List(m2, m3), List(ts1, ts2, ts3), None, false, false, false)
    d.makeVote(m2, Option(Array(Yes(), No(), No())))
    d.makeVote(m3, Option(Array(IfNeedBe(), No(), No())))
    d.showCurrentState

    return true
  }

  def test2:Boolean = {
    return true
  }

  def test3:Boolean = {
    return true
  }

  def test4:Boolean = {
    return true
  }

  def test5:Boolean = {
    return true
  }

  def test6:Boolean = {
    return true
  }

  def test7:Boolean = {
    return true
  }

  def test8:Boolean = {
    return true
  }
  val ok = List(test1, test2, test3, test4, test5, test6, test7, test8).map{t => t}.foldLeft(true){(a,b) => a&&b}

  if(ok) {
    println("OK")
  } else {
    println("Problem")
  }
}
