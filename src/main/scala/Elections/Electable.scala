package Elections

trait Electable {
  var position: String
  var group: Option[Group]
}
