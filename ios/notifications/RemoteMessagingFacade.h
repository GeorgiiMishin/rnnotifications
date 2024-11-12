#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <UserNotifications/UserNotifications.h>

NS_ASSUME_NONNULL_BEGIN

@interface RemoteMessagingFacade : NSObject

+ (instancetype)shared;

- (void)setup: (UIApplication*) application;

- (void)application:(UIApplication *)application
didReceiveRemoteNotification:(NSDictionary *)userInfo
fetchCompletionHandler:(void (^)(UIBackgroundFetchResult result))completionHandler;

- (void)application:(UIApplication *)application
didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken;

- (void) userNotificationCenter:(UNUserNotificationCenter *) center
 didReceiveNotificationResponse:(UNNotificationResponse *) response
          withCompletionHandler:(void (^)()) completionHandler;

@end

NS_ASSUME_NONNULL_END


