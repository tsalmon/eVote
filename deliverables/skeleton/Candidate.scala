package Elections


class Candidate(override val name:String,
                override val surname:String,
                override val assuranceNumber:Int,
                override val address:String,
                override val commune: District,
                var position: String,
                var group: Option[Group]) extends Person(name, surname,assuranceNumber,address,commune) with Electable{
  def this(name: String, surname: String) = this(name, surname, 0, "", null, "", None)
}
