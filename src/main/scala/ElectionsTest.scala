package ElectionsTest

import Elections._

class Commune(override val name:String) extends LeafDistrict(name){
}

class Department(override val name:String) extends ComposedDistrict[Commune](name){
}

class Region(override val name:String) extends ComposedDistrict[Department](name){
}

class Country(override val name:String) extends ComposedDistrict[Region](name){
}

class PresidentialElectionsVotingPaper() extends VotingPaper{
  type VoteInput = Array[Candidate]
  type VoteOutput = Option[Candidate]

  var input = Array(new Candidate("Adam", "Smith"), new Candidate("John", "Brown"), new Candidate("Leonard", "Bold"))
  var output = None : Option[Candidate]

  def isVoted = output != None

  def performVote: Unit ={
    println("Please, choose your candidate \n 1."+input(0)+" \n 2."+input(1)+"  \n 3."+input(2))
    var choice: Int = 0
    try {
      choice = Console.readInt;//scala.io.StdIn.readInt()
    } catch {
      case e: NumberFormatException => println("Wrong choice")
    }

    choice match {
      case x if 1 to input.length contains x => output = Some(input(x-1))
      case _ => println("Wrong choice"); output = None
    }
  }

  def voteResult = output match {
    case Some(output) => output.toString
    case None => "Not voted"
  }
}

class PresidentialElectionsManager extends ElectionsManager {
  type VoteInput = Array[Candidate]
  type VoteOutput = Option[Candidate]
  private var votes = Set[VotingPaper]()

  def createVotingPaper(voting: Voting): VotingPaper = new PresidentialElectionsVotingPaper
  def acceptVotingPaper(votingPaper: VotingPaper) = votes += votingPaper

  def printVotes = println(votes)
}

class PresidentialElections(override val candidates:List[Electable], override val votes:Set[VotingPaper]) extends Elections(candidates, votes){
  type VoteResult = Candidate
}

object ElectionsTest extends App{
  val france = new Country("France")

  val alsace = new Region("Alsace")
  val basRhin = new Department("Bas-Rhin")
  val strasbourg = new Commune("Strasbourg")

  basRhin.addSubunit(strasbourg)
  alsace.addSubunit(basRhin)
  france.addSubunit(alsace)


  val ileDeFrance = new Region("Ile-de-France")
  val parisDept = new Department("Paris")
  val parisCommune = new Commune("Paris")

  parisDept.addSubunit(parisCommune)
  ileDeFrance.addSubunit(parisDept)
  france.addSubunit(ileDeFrance)

  val electors = Array(
    new Elector("Parnell", "Marzullo"),
    new Elector("Kendricks", "Galt"),
    new Elector("Margalit", "Sanders"),
    new Elector("Vannie", "O'meara"),
    new Elector("Aime", "Mannion"))

  val manager = new PresidentialElectionsManager

  for (elector <- electors) {
    val votingPaper = manager.createVotingPaper(elector)
    votingPaper.vote
    votingPaper.confirm
    manager.acceptVotingPaper(votingPaper)
  }

  manager.printVotes
}
