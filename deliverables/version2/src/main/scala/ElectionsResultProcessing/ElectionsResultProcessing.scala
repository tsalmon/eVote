package ElectionsResultProcessing

import Elections.{Electable, Elections}

abstract class ElectionsResultProcessing[ElectionsType <: Elections]{
  def calculateRate(listResult:List[(Any,Int)],nbVoting:Int):List[(Any, Double)]=
    listResult match {
      case (candidate, score)::listRest =>
        (candidate, (score.toDouble*100/nbVoting.toDouble).toDouble)::calculateRate(listRest, nbVoting)
      case _ => Nil
    }
  def calculateResult(elections: ElectionsType): elections.VoteResult
}

abstract class ElectionsResultProcessingMajority[ElectionsType <: Elections]
  extends ElectionsResultProcessing[ElectionsType] {
  def calculate(listResultRate:List[(Any,Double)]) = listResultRate.sortWith((x,y)=>x._2>y._2).head
}

abstract class ElectionsResultProcessingProportional[ElectionsType <: Elections]
  extends ElectionsResultProcessing[ElectionsType] {
  def calculate(listResultRate:List[(Any,Double)], nbSeat: Int) =
    listResultRate.map((x) => (x._1,(x._2*nbSeat/100).round))
}

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