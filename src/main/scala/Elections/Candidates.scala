package Elections

sealed trait Candidates {
  val name: String

  val party: Party
}

case class Party(name: String) extends Candidate {
  val party: Party = this

  override val toString: String = name
}

case class Persone(name: String, party: Party) extends Candidate {
  override val toString: String = name + " (" + party + ")"
}