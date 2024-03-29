package ElectionsTest

import org.scalatest.{Matchers, FlatSpec}

class SeatVoteTest extends FlatSpec with Matchers {

  import VoteTest._

  "A vote" should "have party with most votes as winner" in {
    sv(PartyA -> 1, PartyB -> 2).winner should be(PartyB)
  }
  it should "give correct count for party" in {
    sv(PartyA -> 2).count(PartyA) should be(2)
  }
  it should "give zero count for unknown party" in {
    sv(PartyA -> 2).count(PartyB) should be(0)
  }
  it should "give correct count for party when candidates are people" in {
    sv(PersonA -> 2, PersonB -> 3).count(PartyA) should be(2)
  }
  it should "return sum of counts as total" in {
    sv(PartyA -> 2, PartyB -> 1).count should be(3)
  }
  it should "give correct percentage for party" in {
    sv(PartyA -> 2, PartyB -> 3).percent(PartyA) should be(Percentage(40))
  }
  it should "give zero percentage for unknown party" in {
    sv(PartyA -> 2).percent(PartyB) should be(Percentage(0))
  }
  it should "expose set of all parties" in {
    sv(PartyA -> 2, PartyB -> 2).parties should be(Set(PartyA, PartyB))
  }
  it should "expose set of all candidates when parties" in {
    sv(PartyA -> 2, PartyB -> 2).candidates should be(Set(PartyA, PartyB))
  }
  it should "expose set of all candidates when people" in {
    sv(PersonA -> 2, PersonB -> 2).candidates should be(Set(PersonA, PersonB))
  }
  it should "return candidate for party when people" in {
    sv(PersonA -> 1).candidates(PartyA) should be(Set(PersonA))
  }
  it should "return no candidates for unknown party" in {
    sv(PersonA -> 1).candidates(PartyB) should be(Set.empty)
  }
  it should "derive new vote by adjusting counts according to model" in {
    val increment = new Model() {
      def apply(vote: SeatVote) = Map(PartyA -> 1)
    }
    sv(PartyA -> 1).project(increment).count(PartyA) should be(2)
  }
  it should "allow model to add new parties" in {
    val increment = new Model() {
      def apply(vote: SeatVote) = Map(PartyB -> 1)
    }
    sv(PartyA -> 1).project(increment).count(PartyB) should be(1)
  }
  it should "allows model to produce zero count" in {
    val increment = new Model() {
      def apply(vote: SeatVote) = Map(PartyA -> -1)
    }
    sv(PartyA -> 1).project(increment).count(PartyA) should be(0)
  }
  it should "throw an IllegalArgumentException if model tries to produce -ve count" in {
    val increment = new Model() {
      def apply(vote: SeatVote) = Map(PartyA -> -2)
    }
    intercept[IllegalArgumentException] {
      sv(PartyA -> 1).project(increment)
    }
  }
}