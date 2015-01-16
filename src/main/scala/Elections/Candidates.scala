/*
package Elections

sealed trait Candidates {
  val name: String

  val party: Party
}

case class Party(name: String) extends Candidates {
  val party: Party = this

  override val toString: String = name
}

case class Persone(name: String, party: Party) extends Candidates {
  override val toString: String = name + " (" + party + ")"
}
*/