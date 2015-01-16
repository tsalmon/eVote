package Elections


class Person(val name:String, val surname:String, val socialSecurityNumber:Int, val address:String, val commune: District) {
  override def toString = name+" "+surname
}
