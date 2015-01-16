package Doodle

import Elections.{District, VotingPaper}

/**
 * Created by Vladislav Fitc on 05/01/15.
 */


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