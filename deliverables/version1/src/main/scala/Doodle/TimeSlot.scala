package Doodle

import java.text.SimpleDateFormat

import Elections.{Electable, Group}

/**
 * Created by Vladislav Fitc on 04/01/15.
 */

case class TimeSlot(dateTime:String, override var position: String = null, override var group: Option[Group] = null) extends Electable {
  val date =  new SimpleDateFormat("dd/MM/yyyy").parse(dateTime)
  override def toString = date.toString
}
