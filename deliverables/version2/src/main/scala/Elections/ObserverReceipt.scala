package Elections

trait ObserverReceipt extends Observer{
  var receipt : Receipt;

  def notify(notification:Notification):Unit={
    this.receipt=notification.internalObject;
    ReceiptList.addReceipt(this.receipt);
  }
}
