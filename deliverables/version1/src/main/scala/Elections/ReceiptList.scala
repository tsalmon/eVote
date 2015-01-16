package Elections;

import scala.collection.mutable.ArrayBuffer

abstract class Receipt (notificationName: String, district: District) extends VotingPaper(notificationName, district){
  this.confirmed=true;
}

object ReceiptList{
  protected var receipts= ArrayBuffer[Receipt]();

  def addReceipt(receipt : Receipt): Unit ={
      receipts+=receipt;
  }

  def getReceipts():Array[Receipt]={
    return receipts.toArray;
  }
}