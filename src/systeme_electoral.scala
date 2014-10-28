class Circonscription {

}

class Electeur{

	def getCirconscriptionElecteur():Circonscription = {
		return null;
	}

	def vote(p : Parti) : Unit = {
		val c : Candidat = p.getCandidat(this.getCirconscriptionElecteur());
		c.addVote();
	}
}

class Parti{
	def getCandidat(c : Circonscription): Candidat = {
		return null;
	}
}

class Candidat{
	var nombreDeVote: Int = 0;

	def addVote(): Unit = {
		nombreDeVote = nombreDeVote + 1;
	}
}

class SystemeElectoral{
	electeurs : Array[Electeur];
	candidats: Array[Candidat];

	def tour(){
		for( i <- 0 to electeurs.length) {
			electeurs[i].vote(x);
		}
	}
}

object Test {
	def main(args: Array[String]): Unit = {
	  
	}
	
}