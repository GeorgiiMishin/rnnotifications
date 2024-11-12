
struct Notification {
  let title: String
  let body: String
}

struct RemoteMessage {
  let id: String
  let data: [AnyHashable: Any]
  let notification: Notification?
}

class TokenHolder {
  static let shared = TokenHolder()
  
  private var _token: String? = ""
  
  var token: String? {
    get {
      return _token
    }
    
    set {
      _token = newValue
    }
  }
}


