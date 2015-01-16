import Elections._
import ElectionsResultProcessing._

import scala.collection.immutable.ListMap

case class Commune(override val name:String) extends LeafDistrict(name){}
case class Department(override val name:String, communes:Commune*)
  extends ComposedDistrict[Commune](name, Set(communes: _*)){}
case class Region(override val name:String, departments:Department*)
  extends ComposedDistrict[Department](name, Set(departments: _*)){}
case class Pays(override val name:String, regions:Region*)
  extends ComposedDistrict[Region](name, Set(regions: _*)){}

class PresidentialElections(
  override val candidates:List[Electable],
  override val votes: List[PresidentialElectionsVotingPaper])
extends Elections(candidates, votes) {
  type VoteResult = Array[Candidate]
}

class PresidentialElectionsResultProcessing extends ElectionsResultProcessing[PresidentialElections]{

  var tour : Int = 0

  def calculateResult(elections: PresidentialElections): elections.VoteResult = {
    if( tour == 1){
      val votes : List[PresidentialElectionsVotingPaper] = elections.votes
      val stats = scala.collection.mutable.Map[Candidate, Int]()
      for(vote <- votes) {
        vote.output match {
          case Some(candidate) =>
            stats(candidate) = stats.getOrElse(candidate, 0) + 1 //add 1 at candidate
          case _ => ()
        }
      }
      Array[Candidate](ListMap(stats.toSeq.sortWith(_._2 > _._2):_*).keys.head)
      //Set[Candidate](t(0), t(1))
    } else {
      tour = tour + 1
      val votes : List[PresidentialElectionsVotingPaper] = elections.votes
      val stats = scala.collection.mutable.Map[Candidate, Int]()
      for(vote <- votes) {
        vote.output match {
          case Some(candidate) =>
          stats(candidate) = stats.getOrElse(candidate, 0) + 1
          case _ => ()
        }
      }
      val t =  ListMap(stats.toSeq.sortWith(_._2 > _._2):_*).keys.toArray
      //println(t(0))
      //println(t(1))
      //ListMap(stats.toSeq.sortWith(_._2 > _._2):_*).keys.head
      Array[Candidate](t(0), t(1))
    }
  }
}

class PresidentialElectionsVotingPaper( override  val notificationName: String, override val district: District) extends VotingPaper(notificationName, district){
  type VoteInput = Array[Candidate]
  type VoteOutput = Option[Candidate]

  var input = Array[Candidate]()
  var output = None : Option[Candidate]

  def isVoted = output != None

  def performVote: Unit = {
    var str_votes: String = "Please, choose your candidate\n"
    for(i <- 0 to input.length - 1){
      str_votes = str_votes + ""+i+"."+input(i)+"\n"
    }
    println(str_votes)
    var choice: Int = 0
    try {
      choice = Console.readInt;
    } catch {
      case e: NumberFormatException => println("Wrong choice")
    }

    choice match {
      case x if 0 <= x && x < input.length+1 =>
      output = Some(input(x))

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

class PresidentialElectionsManager( override val notificationName: String, override val electors: Set[Voting],val candidates: Array[Candidate]) extends ElectionsManager[PresidentialElections, PresidentialElectionsVotingPaper](notificationName, electors) {

  def createVotingPaper(voting: Voting) = {
    val votingPaper = new PresidentialElectionsVotingPaper(notificationName, voting.district())
    votingPaper.input = candidates
    votingPaper
  }

  def createElections = {
    new PresidentialElections(candidates.toList, votes)
  }
}

class ElectionsFrancaise {
  val paris = Commune("Paris")
  val villejuif = Commune("Villejuif");
  val strasbourg = Commune("Strasbourg");

  val regTest = Region("region_test",
                        Department("dep1", paris),
                        Department("dep2", villejuif));

  val france = Pays("France",
                        Region("Alsace",
                          Department("Bas-Rhin", strasbourg)),
                        Region("Ile-de-France",
                          Department("Paris", paris)))

  val candidates : Array[Candidate] = Array(
    new Candidate("Adam", "Smith"),
    new Candidate("John", "Brown"),
    new Candidate("Leonard", "Bold")
  )

  val electors:Set[Voting] = Set(
    new Elector("Parnell", "Marzullo", paris),
    new Elector("Kendricks", "Galt", paris),
    new Elector("Margalit", "Sanders", paris),
    new Elector("Vannie", "O'meara", strasbourg),
    new Elector("Aime", "Mannion", strasbourg)
  )

  def test_tour(
      title_election : String,
      electors : Set[Voting],
      candidates : Array[Candidate]
    ): Array[Candidate] = {

    val manager1 = new PresidentialElectionsManager(
      title_election, electors, candidates)

      for (elector <- electors) {
        val votingPaper = manager1.createVotingPaper(elector)
        votingPaper.vote
        votingPaper.confirm
      }

      manager1.printVotes

      val elections = manager1.createElections

      val processing = new PresidentialElectionsResultProcessing()
      processing.calculateResult(elections)
  }

  "ElectionTest: Tour" should "return the candidate more selected" in {
    var resultat = test_tour("elections 1er tour", electors, candidates)
    println("Resultat premier tour: " + resultat(0) + ", " + resultat(1))
    resultat = test_tour("elections 2e tour", electors, resultat)
    println("Resultat deuxieme tour: " + resultat(0))
  }

}
