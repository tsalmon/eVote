package Elections

abstract class District(val name: String, val weight:Double, var parent:Option[District] = None) {
  def this(name:String) = this(name, 1.0)
}

abstract class LeafDistrict(override val name: String) extends District(name) {
}

abstract class ComposedDistrict[Subdistrict <: District](override val name:String, private var subdistricts:Set[Subdistrict]) extends District(name){
  subdistricts.foreach({_.parent = Some(this)})

  def this(name: String) = this(name, Set[Subdistrict]())
  def this(name: String, subdistricts:Subdistrict *) = this(name, Set(subdistricts: _*)); subdistricts.foreach({_.parent = Some(this)})
  def addSubdistricts(subdistricts: Subdistrict*) = subdistricts.foreach({sd=> this.subdistricts += sd; sd.parent = Some(this)})
  def removeSubdistrict(subdistricts: Subdistrict*) = subdistricts.foreach({sd=> this.subdistricts -= sd; sd.parent = None})

  override def toString = this.name+": ["+this.subdistricts+"]"
}