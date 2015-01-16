package GUI
import Elections._

import scala.swing._

class UI extends MainFrame{ 
  title = "Votalisk"
  preferredSize = new Dimension(320, 240)

    def reportAndClose() {
    println("Vous avez voté")
    sys.exit(0)
  }
}

class UI_Uninomial extends UI {  	
  contents = new ScrollPane(new BoxPanel(Orientation.Vertical) {
    var array_buttons : Array[RadioButton] = new Array[RadioButton](100);
    var statusGroup = new ButtonGroup()
    for( i <- 0 to 99) {
      array_buttons(i) = new RadioButton(f"candidat$i")
      contents += array_buttons(i)
    }
    statusGroup = new ButtonGroup(){
      for( btn <- array_buttons) {
        buttons += btn
      }
    }
    contents += Button("Voter") { reportAndClose() }
  })
}

class UI_Plurinomial extends UI {

  contents = new ScrollPane(new BoxPanel(Orientation.Vertical) {
    var array_buttons : Array[CheckBox] = new Array[CheckBox](100);
    var statusGroup = new ButtonGroup()
    for( i <- 0 to 99) {
      array_buttons(i) = new CheckBox(f"candidat$i")
      contents += array_buttons(i)
    }
    contents += Button("Voter") { reportAndClose() }
  }) 
}

object Votalisk {
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

  def main(args: Array[String]) {

    var resultat = test_tour("elections 1er tour", electors, candidates)
    println("Resultat premier tour: " + resultat(0) + ", " + resultat(1))
    resultat = test_tour("elections 2e tour", electors, resultat)
    println("Resultat deuxieme tour: " + resultat(0))

    //val ui = new UI_Plurinomial
    //ui.visible = true
  }
}