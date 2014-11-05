package Elections

abstract class District(val name: String, val weight:Double) {
  def this(name:String) = this(name, 1.0)
}

abstract class LeafDistrict(override val name: String) extends District(name) {
}

abstract class ComposedDistrict[Subdistrict <: District](override val name:String) extends District(name){
  private var subdistricts = Set[Subdistrict]()
  def addSubunit(subunit: Subdistrict) = this.subdistricts += subunit
  def removeSubunit(subunit: Subdistrict) = this.subdistricts -= subunit

  override def toString = this.name+": ["+this.subdistricts+"]"
}