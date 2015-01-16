package Doodle

import Elections.{ElectionsManager, Voting}

import scala.collection.mutable

/**
 * Created by Vladislav Fitc on 04/01/15.
 */

class DoodleManager(val limit:Option[Int], val location:Location, val single: Boolean, val public: Boolean, val ifNeedBeOption:Boolean, electors: Set[Voting], val timeSlots: List[TimeSlot], notificationName:String) extends ElectionsManager[DoodleElections, DoodleVote](notificationName, electors){
  override def createElections: DoodleElections = {
    new DoodleElections(timeSlots, votes)
  }

  /*
      Une fonction qui est utilisé pour calculer le nombre
      de votes "positives" (Yes ou IfNeedBe)
   */

  private def voteCountForTimeSlot(timeSlot: TimeSlot): Int = {
    val votesCount = mutable.Map.empty[TimeSlot, Int]
    val allVotes = votes.map{v => v.output}.flatten

    def availabilityToInt(a: Availability):Int = {
      a match {
        case Yes()| IfNeedBe() => 1
        case No() => 0
      }
    }

    allVotes.foreach{case (ts, av) =>
      votesCount(ts) =  votesCount.getOrElse(ts, 0) + availabilityToInt(av)
    }

    votesCount.getOrElse(timeSlot, 0)
  }

  def availableTimeSlots: List[TimeSlot] = {
    limit match{
      case Some(l) => timeSlots.filter(voteCountForTimeSlot(_) < l)
      case None => timeSlots
    }
  }

  override def createVotingPaper(member: Voting): DoodleVote = {
    val m = member.asInstanceOf[DoodleMember]
    (single, public,ifNeedBeOption) match {
      case (false ,false  , false ) => new DoodleVote(m, this.notificationName, location, availableTimeSlots) with MultipleOptions
      case (false ,false  , true  ) => new DoodleVote(m, this.notificationName, location, availableTimeSlots) with MultipleOptions  with IfNeedBeOption
      case (false ,true   , false ) => new DoodleVote(m, this.notificationName, location, availableTimeSlots) with MultipleOptions  with Public
      case (false ,true   , true  ) => new DoodleVote(m, this.notificationName, location, availableTimeSlots) with MultipleOptions  with Public with IfNeedBeOption
      case (true  ,false  , false ) => new DoodleVote(m, this.notificationName, location, availableTimeSlots) with SingleOption
      case (true  ,false  , true  ) => new DoodleVote(m, this.notificationName, location, availableTimeSlots) with SingleOption     with IfNeedBeOption
      case (true  ,true   , false ) => new DoodleVote(m, this.notificationName, location, availableTimeSlots) with SingleOption     with Public
      case (true  ,true   , true  ) => new DoodleVote(m, this.notificationName, location, availableTimeSlots) with SingleOption     with Public with IfNeedBeOption
    }
  }

  override def printVotes = {
    votes.foreach { v => println(v)}
  }
}
