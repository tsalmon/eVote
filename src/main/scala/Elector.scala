package Elections


class Elector(override val name:String, override val surname:String, override val assuranceNumber:Int, override val address:String, override val commune: District) extends Person(name, surname,assuranceNumber,address,commune) with Voting{
  def this(name: String, surname: String) = this(name, surname, 0, "", null)
}
