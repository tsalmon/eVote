package Elections

class ElectableGroup(override val name:String, override var position: String, override var group: Option[Group]) extends Group(name) with Electable{

}
