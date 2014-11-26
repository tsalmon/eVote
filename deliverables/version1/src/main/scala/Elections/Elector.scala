package Elections

class Elector(override val name:String, override val surname:String, override val socialSecurityNumber:Int, override val address:String, override val commune: District) extends Person(name, surname,socialSecurityNumber,address,commune) with Voting{
  def this(name: String, surname: String) = this(name, surname, 0, "", null)
  def this(name: String, surname: String, district: District) = this(name, surname, 0, "", district)
  def district() = this.commune
}
