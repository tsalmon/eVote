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
  override def calculateMajority(listResultRate:List[(Any,Double)]) = listResultRate.sortWith((x,y)=>x._2>y._2).head
}
