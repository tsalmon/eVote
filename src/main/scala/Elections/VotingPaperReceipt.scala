package Elections

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
    NotificationCenter.postNotification(this.id)
  }

}