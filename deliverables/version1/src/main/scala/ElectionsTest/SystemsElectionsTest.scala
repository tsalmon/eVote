package ElectionsTest

import Elections._
import ElectionsResultProcessing.{ElectionsResultProcessingMixed, ElectionsResultProcessingProportional, ElectionsResultProcessingMajority, ElectionsResultProcessing}

import scala.collection.immutable.ListMap

case class Commune(override val name:String) extends LeafDistrict(name){}
case class Department(override val name:String, communes:Commune*) extends ComposedDistrict[Commune](name, Set(communes: _*)){}
case class Region(override val name:String, departments:Department*) extends ComposedDistrict[Department](name, Set(departments: _*)){}
case class Country(override val name:String, regions:Region*) extends ComposedDistrict[Region](name, Set(regions: _*)){}


class PresidentialElections(override val candidates:List[Candidate],
                            override val votes: List[PresidentialElectionsVotingPaper])
  extends Elections(candidates, votes) {
  type VoteResult = Candidate
  //override var listResults: List[(Candidate, Int)] = _
}

class PresidentialElectionsResultProcessing extends ElectionsResultProcessingProportional[PresidentialElections] {
  //class PresidentialElectionsResultProcessing extends ElectionsResultProcessingMajority[PresidentialElections] {
  var listResult: List[(Any, Int)] = Nil
  var listResultRate : List[(Any, Double)] = Nil

  def calculateResult(elections: PresidentialElections): elections.VoteResult = {
    val votes : List[PresidentialElectionsVotingPaper] = elections.votes

    val stats = scala.collection.mutable.Map[Candidate, Int]()
    for(vote <- votes) {
      vote.output match {
        case Some(candidate) =>
          stats(candidate) = stats.getOrElse(candidate, 0) + 1
        case _ => ()
      }
    }

    for(candidate <- ListMap(stats.toSeq.sortWith(_._2 > _._2):_*).keys){
      listResult = (candidate, stats(candidate)) :: listResult
    }

    listResultRate = calculateRate(listResult, elections.votes.size)

    ListMap(stats.toSeq.sortWith(_._2 > _._2):_*).keys.head
  }
}
class MixedElectionsResultProcessing extends ElectionsResultProcessingMixed[PresidentialElections]{
  var listResult: List[(Any, Int)] = Nil
  var listResultRate : List[(Any, Double)] = Nil

  def calculateResult(elections: PresidentialElections): elections.VoteResult = {
    val votes : List[PresidentialElectionsVotingPaper] = elections.votes

    val stats = scala.collection.mutable.Map[Candidate, Int]()
    for(vote <- votes) {
      vote.output match {
        case Some(candidate) =>
          stats(candidate) = stats.getOrElse(candidate, 0) + 1
        case _ => ()
      }
    }

    for(candidate <- ListMap(stats.toSeq.sortWith(_._2 > _._2):_*).keys){
      listResult = (candidate, stats(candidate)) :: listResult
    }

    listResultRate = calculateRate(listResult, elections.votes.size)

    ListMap(stats.toSeq.sortWith(_._2 > _._2):_*).keys.head
  }
}

class PresidentialElectionsVotingPaper(override  val notificationName: String,
                                       override val district: District) extends VotingPaper(notificationName, district){
  type VoteInput = Array[Candidate]
  type VoteOutput = Option[Candidate]

  var input = Array[Candidate]()
  var output = None : Option[Candidate]

  def isVoted = output != None

  def performVote: Unit ={
    println("Please, choose your candidate \n 1."+input(0)+" \n 2."+input(1)+"  \n 3."+input(2))
    var choice: Int = 0
    try {
      choice = Console.readInt;
    } catch {
      case e: NumberFormatException => println("Wrong choice")
    }

    choice match {
      case x if 1 to input.length contains x =>
        output = Some(input(x-1))

      case _ =>
        println("Wrong choice")
        output = None
    }
  }

  override def toString = output match {
    case Some(output) => output.toString
    case None => "Blank voting paper"
  }
}

class PresidentialElectionsManager(override val notificationName: String, override val electors: Set[Voting],
                                   val candidates: Array[Candidate])
  extends ElectionsManager[PresidentialElections, PresidentialElectionsVotingPaper](notificationName, electors) {
  def createVotingPaper(voting: Voting) = {
    val votingPaper = new PresidentialElectionsVotingPaper(notificationName, voting.district())
    votingPaper.input = candidates
    votingPaper
  }

  def createElections = {
    new PresidentialElections(candidates.toList, votes)
  }
}


object SystemsElectionsTest extends App{
  val paris = Commune("Paris")
  val strasbourg = Commune("Strasbourg")
  val france = Country("France", Region("Alsace", Department("Bas-Rhin", strasbourg)), Region("Ile-de-France", Department("Paris", paris)))

  val candidates = Array(
    new Candidate("Adam", "Smith"),
    new Candidate("John", "Brown"),
    new Candidate("Leonard", "Bold"))
  1
  val electors:Set[Voting] = Set(
    new Elector("Parnell", "Marzullo", paris),
    new Elector("Kendricks", "Galt", paris),
    new Elector("Margalit", "Sanders", paris),
    new Elector("Vannie", "O'meara", strasbourg),
    new Elector("Kendricks", "Galt", paris),
    new Elector("Margalit", "Sanders", paris))

  val manager = new PresidentialElectionsManager("presidentialElections", electors, candidates)

  for (elector <- electors) {
    val votingPaper = manager.createVotingPaper(elector)
    votingPaper.vote
    votingPaper.confirm
  }

  manager.printVotes

  val elections = manager.createElections

  val processing = new MixedElectionsResultProcessing()
  print("Amount of electors: ")
  println(elections.votes.size)
  print("list of candidate: ")
  println(elections.candidates.toString())
  print("numero 1: ")
  println(processing.calculateResult(elections))
  print("list of result (%): ")
  println(processing.listResultRate.toString())
  print("winner of propority")
  println(processing.calculate(processing.listResultRate, 8))
  print("winner of mixed")
  println(processing.calculate(processing.listResultRate, 8))
}