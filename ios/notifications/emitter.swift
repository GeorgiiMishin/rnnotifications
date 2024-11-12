import Foundation

enum RemoteMessagingEvent {
  case newToken(String)
  case newMessage(RemoteMessage)
  case notificationOpened(RemoteMessage)
}

protocol RemoteMessagingObserver {
  var id: String { get }
  func onEvent(event: RemoteMessagingEvent)
}

class RemoteMessagingEventEmitter {
  static let shared = RemoteMessagingEventEmitter()
  
  private var observers: [RemoteMessagingObserver] = []
  
  func subscribe(_ emitter: RemoteMessagingObserver) {
    observers.append(emitter)
  }
  
  func unsubscribe(_ emitter: RemoteMessagingObserver) {
    observers = observers.filter { $0.id != emitter.id }
  }
  
  func notify(event: RemoteMessagingEvent) {
    observers.forEach { $0.onEvent(event: event) }
  }
}
