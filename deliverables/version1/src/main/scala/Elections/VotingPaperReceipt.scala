package Elections

import Elections.Receipt

object idManager{
  private var nextid=0;
  def newId(): String ={
    val res = nextid;
    nextid=nextid+1;
    return "vote_"+res.toString();
  }
}

abstract class VotingPaperReceipt(notificationName: String, district: District) extends VotingPaper(notificationName, district) {

  private val id = idManager.newId();

  def confirm(observer : VotingReceipt) : Unit={
    super.confirm;
    NotificationCenter.addObserver(observer, this.id);
  }

  def counted() : Unit={
    var receipt=new Receipt(notificationName,district);
    receipt.input=this.input;
    NotificationCenter.postNotification(this.id, receipt);
  }

}