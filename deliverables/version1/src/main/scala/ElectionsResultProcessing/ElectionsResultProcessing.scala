package ElectionsResultProcessing

import Elections.{Electable, Elections}

abstract class ElectionsResultProcessing[ElectionsType <: Elections]{
  //calculate the rate of each candiate
  def calculateRate(listResult:List[(Any,Int)],nbVoting:Int):List[(Any, Double)]=
    listResult match {
      case (candidate, score)::listRest =>
        (candidate, (score.toDouble*100/nbVoting.toDouble).toDouble)::calculateRate(listRest, nbVoting)
      case _ => Nil
    }

  //calculate the result, the user will definer this fonction
  def calculateResult(elections: ElectionsType): elections.VoteResult
}

//processing for system majority
abstract class ElectionsResultProcessingMajority[ElectionsType <: Elections]
  extends ElectionsResultProcessing[ElectionsType] {
  //sort the list and access the first element in list
  def calculate(listResultRate:List[(Any,Double)]) = listResultRate.sortWith((x,y)=>x._2>y._2).head
}

//processing for system proportional
abstract class ElectionsResultProcessingProportional[ElectionsType <: Elections]
  extends ElectionsResultProcessing[ElectionsType] {
  //calculate the amount of seat for each candidate
  def calculate(listResultRate:List[(Any,Double)], nbSeat: Int) =
    listResultRate.map((x) => (x._1,(x._2*nbSeat/100).round))
}

//processing for system mixed
abstract class ElectionsResultProcessingMixed[ElectionsType <: Elections]
  extends ElectionsResultProcessing[ElectionsType] {
  def calculate(listResultRate:List[(Any,Double)], nbSeat: Int) = {
    var listResultMixed:List[(Any, Long)] = Nil
    //apply method proportional on the half of seat
    listResultMixed = listResultRate.map((x) => (x._1,(x._2*nbSeat/200).round))
    //apply method majority on the rest
    listResultMixed = listResultMixed.sortWith((x,y)=>x._2>y._2)

    (listResultMixed.head._1, listResultMixed.head._2 + nbSeat/2)::listResultMixed.drop(1)
  }
}