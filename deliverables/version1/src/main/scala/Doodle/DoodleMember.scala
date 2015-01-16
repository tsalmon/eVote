package Doodle

import Elections.Voting

/**
 * Created by Vladislav Fitc on 04/01/15.
 */

//Class de participant d'un Doodle
class DoodleMember(val fullname: String) extends Voting{
  def createDoodle(title: String, location: String, description: String, participants: List[DoodleMember], timeSlots: List[TimeSlot], limit: Option[Int], single: Boolean, public: Boolean, ifNeedBeOption: Boolean) =
    new Doodle(title, location, description, this, participants, timeSlots, limit, single, public, ifNeedBeOption)
}