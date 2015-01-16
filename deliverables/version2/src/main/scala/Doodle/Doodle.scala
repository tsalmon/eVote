package Doodle

import java.text.SimpleDateFormat

import Elections._

import scala.collection.mutable

/**
 * Created by Vladislav Fitc on 04/01/15.
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

class DoodleElections(options: List[TimeSlot], votes:List[VotingPaper]) extends Elections(options, votes) {
  type VoteResult = List[(DoodleMember, TimeSlot)]
  type VotingPaperType = DoodleVote
}

class DoodleManager(val limit:Option[Int], val location:Location, val single: Boolean, val public: Boolean, val ifNeedBeOption:Boolean, electors: Set[Voting], val timeSlots: List[TimeSlot], notificationName:String) extends ElectionsManager[DoodleElections, DoodleVote](notificationName, electors){
  override def createElections: DoodleElections = {
    new DoodleElections(timeSlots, votes)
  }

  /*
      Une fonction qui est utilisé pour calculer le nombre
      de votes "positives" (Yes ou IfNeedBe)
   */

  private def voteCountForTimeSlot(timeSlot: TimeSlot): Int = {
    val votesCount = mutable.Map.empty[TimeSlot, Int]
    val allVotes = votes.map{v => v.output}.flatten

    def availabilityToInt(a: Availability):Int = {
      a match {
        case Yes()| IfNeedBe() => 1
        case No() => 0
      }
    }

    allVotes.foreach{case (ts, av) =>
      votesCount(ts) =  votesCount.getOrElse(ts, 0) + availabilityToInt(av)
    }

    votesCount.getOrElse(timeSlot, 0)
  }

  def availableTimeSlots: List[TimeSlot] = {
    limit match{
      case Some(l) => timeSlots.filter(voteCountForTimeSlot(_) < l)
      case None => timeSlots
    }
  }

  override def createVotingPaper(member: Voting): DoodleVote = {
    val m = member.asInstanceOf[DoodleMember]
    (single, public,ifNeedBeOption) match {
      case (false ,false  , false ) => new DoodleVote(m, this.notificationName, location, availableTimeSlots) with MultipleOptions
      case (false ,false  , true  ) => new DoodleVote(m, this.notificationName, location, availableTimeSlots) with MultipleOptions  with IfNeedBeOption
      case (false ,true   , false ) => new DoodleVote(m, this.notificationName, location, availableTimeSlots) with MultipleOptions  with Public
      case (false ,true   , true  ) => new DoodleVote(m, this.notificationName, location, availableTimeSlots) with MultipleOptions  with Public with IfNeedBeOption
      case (true  ,false  , false ) => new DoodleVote(m, this.notificationName, location, availableTimeSlots) with SingleOption
      case (true  ,false  , true  ) => new DoodleVote(m, this.notificationName, location, availableTimeSlots) with SingleOption     with IfNeedBeOption
      case (true  ,true   , false ) => new DoodleVote(m, this.notificationName, location, availableTimeSlots) with SingleOption     with Public
      case (true  ,true   , true  ) => new DoodleVote(m, this.notificationName, location, availableTimeSlots) with SingleOption     with Public with IfNeedBeOption
    }
  }

  override def printVotes = {
    votes.foreach { v => println(v)}
  }
}

case class TimeSlot(dateTime:String, override var position: String = null, override var group: Option[Group] = null) extends Electable {
  val date =  new SimpleDateFormat("dd/MM/yyyy").parse(dateTime)
  override def toString = date.toString
}

abstract class Availability
case class Yes() extends Availability
case class No() extends Availability
case class IfNeedBe() extends Availability

class Location(name: String) extends District(name)

abstract class DoodleVote(val member: DoodleMember, notificationName: String, location: Location, timeSlots: List[TimeSlot]) extends VotingPaper(notificationName, location){

  //On propose à un participant une liste des creneaux possibles
  type VoteInput = List[TimeSlot]

  //Le résultat de scrutin est une liste des couples creneau * possibilité qui caracterise les possibilités d'un participant
  type VoteOutput = List[(TimeSlot, Availability)]

  override var input = timeSlots
  override var output = List[(TimeSlot, Availability)]()

  //Les reponses possibles pour un creneau. Par défaut c'est Yes et No, on peut ajouter les autres dans les extensions
  val possibleChoices: Array[Availability] = Array(Yes(), No())

  //Methode de préchargement d'un scrutin
  def preliminaryInput(in: Array[Availability]): Unit ={
    output = input.zipWithIndex.map{case (ts, i) => (ts, in(i))}
  }

  //Montre à l'utilisateur des réponses possibles pour un creneau

  def selectChoice: Availability = {
    println("Choose option")
    possibleChoices.zipWithIndex.foreach{ case(slot,i) => println(i+1+") "+slot) }
    try {
      scala.io.StdIn.readInt() match {
        case x if 1 to possibleChoices.length contains x =>
          possibleChoices(x - 1)

        case _ =>
          println("Wrong choice")
          selectChoice
      }
    } catch {
      case e: NumberFormatException =>
        println("Wrong choice")
        selectChoice
    }
  }

  //Methode d'interaction avec un participant
  def makeChoice(timeSlots: List[TimeSlot]) : List[Availability]

  override def toString = output.toString()

  override def performVote = {
    preliminaryInput(makeChoice(input).toArray)
  }
}

//Extension de DoodleVote avec une seule choix de reponse "positive" (Yes ou IfNeedBe)
trait SingleOption extends DoodleVote{
  def makeChoice(timeSlots: List[TimeSlot]) : List[Availability] =  {
    def makeChoiceAux(timeSlots: List[TimeSlot], availabilities: List[Availability]): List[Availability] = {
      timeSlots match {
        case (date:TimeSlot) :: (tail:List[TimeSlot]) =>
          println(date)
          val choice = selectChoice
          choice match {
            case Yes() | IfNeedBe() => choice::availabilities ::: tail.map(x => No())
            case No() => makeChoiceAux(tail, No()::availabilities)
          }

        case Nil =>
          availabilities
      }
    }
    makeChoiceAux(timeSlots, Nil)
  }
}

//Extension de DoodleVote avec plusieurs choix de reponses "positives" (Yes ou IfNeedBe)
trait MultipleOptions extends DoodleVote{
  def makeChoice(list: List[TimeSlot]) : List[Availability] =  {
    def makeChoiceAux(list: List[TimeSlot], result: List[Availability]): List[Availability] = {
      list match {
        case (date:TimeSlot) :: (tail:List[TimeSlot]) =>
          println(date)
          val choice = selectChoice
          makeChoiceAux(tail, choice :: result)

        case Nil =>
          result
      }
    }
    makeChoiceAux(list, Nil)
  }
}

//Extension de DoodleVote avec output d'info sur les choix d'autres participants qui ont déjà voté
trait Public extends DoodleVote{
  override def toString = member.fullname+": "+super.toString
}

//Extension de DoodleVote qui ajoute IfNeedBe option aux choix possibles
trait IfNeedBeOption extends DoodleVote{
  override val possibleChoices: Array[Availability] = Array(Yes(), No(), IfNeedBe())
}

//Class de participant d'un Doodle
class DoodleMember(val fullname: String, override val district: Location = null) extends Voting{
  def createDoodle(title: String, location: String, description: String, participants: List[DoodleMember], timeSlots: List[TimeSlot], limit: Option[Int], single: Boolean, public: Boolean, ifNeedBeOption: Boolean) =
    new Doodle(title, location, description, this, participants, timeSlots, limit, single, public, ifNeedBeOption)
}