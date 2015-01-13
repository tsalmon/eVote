package Elections

trait Model {
  /**
   * @return a map of candidates -> count adjustment
   */
  def apply(vote: SeatVote): Map[Candidates, Int]
}
