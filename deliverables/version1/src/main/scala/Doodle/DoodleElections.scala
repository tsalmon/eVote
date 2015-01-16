package Doodle

import Elections.{Elections, VotingPaper}

/**
 * Created by Vladislav Fitc on 14/01/15.
 */

class DoodleElections(options: List[TimeSlot], votes:List[VotingPaper]) extends Elections(options, votes) {
  type VoteResult = List[(DoodleMember, TimeSlot)]
  type VotingPaperType = DoodleVote
}
