import Foundation
import UIKit

@objc(RemoteMessagingFacade)
class RemoteMessagingFacade : NSObject, UNUserNotificationCenterDelegate {
  @objc static let shared = RemoteMessagingFacade()
  
  @objc func setup(_ application: UIApplication) {
    UNUserNotificationCenter.current().delegate = self
    
    application.registerForRemoteNotifications()
  }
  
  @objc func application(
    _ application: UIApplication,
    didReceiveRemoteNotification userInfo: [AnyHashable : Any],
    fetchCompletionHandler completionHandler: @escaping (
      UIBackgroundFetchResult
    ) -> Void
  ) {
    RemoteMessagingEventEmitter.shared.notify(event: .newMessage(userInfo.toRemoteMessage()))
    completionHandler(.noData)
  }
  
  @objc func application(
    _ application: UIApplication,
    didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
  ) {
    let tokenParts = deviceToken.map { data -> String in
      return String(format: "%02.2hhx", data)
    }
    
    let token = tokenParts.joined()
    TokenHolder.shared.token = token
    
    RemoteMessagingEventEmitter.shared.notify(event: .newToken(token))
  }
  
  @objc func userNotificationCenter(
    _ center: UNUserNotificationCenter,
    didReceive response: UNNotificationResponse,
    withCompletionHandler completionHandler: @escaping () -> Void
  ) {
    RemoteMessagingEventEmitter.shared.notify(event: .notificationOpened(response.notification.request.content.userInfo.toRemoteMessage()))
    completionHandler()
  }
}

fileprivate extension Dictionary<AnyHashable, Any> {
  private enum Keys {
    static let id = "notification_id"
    static let aps = "aps"
    static let alert = "alert"
    static let data = "data"
    static let notification = "notification"
  }
  
  func toRemoteNotification() -> Notification? {
    let title = self["title"] as? String
    let body = self["body"] as? String
    
    if title == nil, body == nil {
      return nil
    }
    
    return Notification(title: title ?? "", body: body ?? "")
  }
  
  func toRemoteMessage() -> RemoteMessage {
    let messageId = (self[Keys.id] as? String) ?? UUID().uuidString
    let data = self[Keys.data] as? [AnyHashable: Any]
    let notification = (self[Keys.aps] as? [AnyHashable: Any])?[Keys.alert] as? [AnyHashable: Any]
    
    return RemoteMessage(
      id: messageId,
      data: data ?? [:],
      notification: notification?.toRemoteNotification()
    )
  }
}
