package Elections

import java.lang.IllegalArgumentException

class SeatVote(val seat: Seat, candidateToCount: Map[Candidates, Int]) extends Vote {
  private val byParty: Map[Party, Map[Candidates, Int]] = candidateToCount.groupBy(_._1.party)

  /**
   * @return the winning party
   */
  val winner: Party = candidateToCount.maxBy(_._2)._1.party

  /**
   * @return the total count
   */
  val count: Int = candidateToCount.values.sum

  /**
   * @return the count for the supplied party
   */
  def count(party: Party): Int = byParty.getOrElse(party, Map.empty).values.sum

  /**
   * @return the set of candidates that contested this vote
   */
  val candidates: Set[Candidates] = candidateToCount.keySet

  /**
   * @return the candidate(s) for the supplied party
   */
  def candidates(party: Party): Set[Candidates] = byParty.getOrElse(party, Map.empty).keys.toSet

  /**
   * @return the set of parties that contested this vote
   */
  val parties: Set[Party] = byParty.keySet

  /**
   * Derives new vote by adjusting counts according to the supplied model
   */
  def project(model: Model): SeatVote = {
    val deltas: Map[Candidates, Int] = model(this)
    new SeatVote(seat, deltas ++ candidateToCount.map {
      case (candidates: Candidates, count: Int) => {
        val newCount = count + deltas.getOrElse(candidates, 0)
        if (newCount < 0) throw new IllegalArgumentException(candidates + "->" + newCount)
        candidates -> newCount
      }
    }.toMap)
  }
}
