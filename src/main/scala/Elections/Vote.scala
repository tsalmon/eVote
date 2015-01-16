/*
package Elections

trait Vote {
  /**
   * @return the winning party
   */
  def winner: Party

  /**
   * @return the total count
   */
  def count: Int

  /**
   * @return the count for a particular party
   */
  def count(party: Party): Int

  /**
   * @return the % of the vote won by a particular party
   */
  def percent(party: Party): Percentage = Percentage(count(party), count)

  /**
   * Derives new vote by adjusting counts according to the supplied model
   */
  def project(delta: Model): Vote
}
*/