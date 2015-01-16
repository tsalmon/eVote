package ElectionsResultProcessing

import Elections._
import scala.collection.immutable.ListMap

abstract class ElectionsResultProcessing[ElectionsType <: Elections]{
  def calculateRate(listResult:List[(Any,Int)],nbVoting:Int):List[(Any, Double)]=
      listResult match {
        case (candidate, score)::listRest =>
          (candidate, (score.toDouble*100/nbVoting.toDouble).toDouble)::calculateRate(listRest, nbVoting)
        case _ => Nil
      }
  def calculateMajority(listResultRate:List[(Any,Double)]) = listResultRate.sortWith((x,y)=>x._2>y._2).head

  def calculateProportional(listResult:List[(Any,Int)], nbSeat:Int):List[(Any,Int)]={
    for (result <- listResult){
      result._
    }
  }
  def calculateResult(elections: ElectionsType): elections.VoteResult
}