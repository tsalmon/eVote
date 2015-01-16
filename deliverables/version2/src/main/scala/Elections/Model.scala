package Elections

trait Model {
  /**
   * @return a map of candidate -> count adjustment
   */
  def apply(vote: SeatVote): Map[Candidate, Int]
}
