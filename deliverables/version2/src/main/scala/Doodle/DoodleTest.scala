package Doodle

/**
 * Created by coolermaster on 16/01/15.
 */

object DoodleTest extends App{

  //Pre:
  val m1 = new DoodleMember("Member1")
  val m2 = new DoodleMember("Member2")
  val m3 = new DoodleMember("Member3")

  val ts1 = new TimeSlot("27/01/2015")
  val ts2 = new TimeSlot("28/01/2015")
  val ts3 = new TimeSlot("29/01/2015")

  val d = m1.createDoodle("Meeting", "1 rue st. Honore, 75001, Paris", "Discussions about contract", List(m2, m3), List(ts1, ts2, ts3), None, false, false, false)
  d.makeVote(m2)
  d.makeVote(m3)
  d.showCurrentState
}