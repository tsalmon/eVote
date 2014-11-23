package Elections

trait Observer {
  def notify(notification:Notification)
}

class Notification(val name:String, val internalObject: Option[AnyRef]) {}

object NotificationCenter{
  private var observers = Map[String, Set[Observer]]()

  def postNotification(notificationName: String) = {
    this.observers(notificationName).foreach(observer => observer.notify(new Notification(notificationName, None)))
  }

  def postNotification(notificationName: String, notificationObject: AnyRef) = {
    this.observers(notificationName).foreach(observer => observer.notify(new Notification(notificationName, Some(notificationObject))))
  }

  def addObserver(observer: Observer, notificationName: String) = {
    val observersForNotification = this.observers.getOrElse(notificationName, Set[Observer]())
    observers += (notificationName -> (observersForNotification + observer))
  }

  def removeObserver(observer: Observer) = {}
  def removeObserver(observer: Observer, notificationName: String) = {}
}
