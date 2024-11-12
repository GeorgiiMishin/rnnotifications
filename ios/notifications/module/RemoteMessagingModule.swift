import Foundation
import React

fileprivate extension Notification {
  func toDict() -> [AnyHashable: Any] {
    var result:  [AnyHashable: Any]  = [:]
    
    result["title"] = title
    result["body"] = body
    
    return result
  }
}

fileprivate extension RemoteMessage {
  func toDict() -> [AnyHashable: Any] {
    var result:  [AnyHashable: Any]  = [:]
    
    result["data"] = data
    result["id"] = id
    result["notification"] = notification?.toDict()
    
    return result
  }
}

@objc(RemoteMessagingModule)
class RemoteMessagingModule : RCTEventEmitter, RemoteMessagingObserver {
  var id: String {
    get {
      return "RemoteMessagingModule"
    }
  }
  
  func onEvent(event: RemoteMessagingEvent) {
    switch event {
    case .newToken(let newToken):
      sendEvent(withName: "NewToken", body: ["token": newToken])
      break
    case .newMessage(let message):
      sendEvent(withName: "NewMessage", body: message.toDict())
      break
      
    case .notificationOpened(let message):
      sendEvent(withName: "MessageOpened", body: message.toDict())
      break
    }
  }
  
  @objc func requestPermissions(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) { result, error in
      if let error {
        reject("Error", "Error request permissions", error)
      } else {
        resolve(result)
      }
    }
  }
  
  @objc func getToken(_ resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) {
    resolve(TokenHolder.shared.token)
  }
  
  override func supportedEvents() -> [String]! {
    return ["NewToken", "NewMessage", "MessageOpened"]
  }
  
  override func startObserving() {
    RemoteMessagingEventEmitter.shared.subscribe(self)
  }
  
  override func stopObserving() {
    RemoteMessagingEventEmitter.shared.unsubscribe(self)
  }
  
  @objc override public static func requiresMainQueueSetup() -> Bool {
    return false
  }
}

