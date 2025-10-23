import Foundation
import ComposeApp
import UIKit
import UserNotifications // Make sure this is imported
import FirebaseCore
import FirebaseMessaging


class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate, MessagingDelegate {
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        FirebaseApp.configure()
        
        UNUserNotificationCenter.current().delegate = self
        Messaging.messaging().delegate = self
        
        // --- THIS IS THE MISSING SECTION ---
        // 1. Request permission from the user
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) { granted, error in
            if let error = error {
                print("iOS: Error requesting notification permission: \(error.localizedDescription)")
                return
            }
            
            if granted {
                print("iOS: Notification permission granted.")
                // 2. Register for remote notifications on the main thread
                //    This MUST be called after permission is requested.
                DispatchQueue.main.async {
                    application.registerForRemoteNotifications()
                }
            } else {
                print("iOS: Notification permission denied.")
            }
        }
        // --- END OF MISSING SECTION ---
        
        return true
    }
    
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        print("iOS: Successfully registered for remote notifications (APNs). Setting token for Firebase.")
        Messaging.messaging().apnsToken = deviceToken
    }
    
    func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        print("iOS: Failed to register for push notifications: \(error.localizedDescription)")
    }
    
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        print("iOS: Firebase got new FCM token: \(fcmToken ?? "nil")")
        guard let token = fcmToken, !token.isEmpty else {
            return
        }
        
        // Save to UserDefaults and update KMP
        UserDefaults.standard.set(token, forKey: "FCM_TOKEN")
        IosDeviceTokenHolderBridge.shared.updateToken(token: token)
    }
    
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any], fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        // This handles background notifications
        Messaging.messaging().appDidReceiveMessage(userInfo)
        completionHandler(.newData)
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        // This handles foreground notifications
        completionHandler([.banner, .sound, .list]) // Show banner, play sound, add to list
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        // This handles tapping on a notification
        let userInfo = response.notification.request.content.userInfo
        Messaging.messaging().appDidReceiveMessage(userInfo)
        
        // You can add logic here to navigate to a specific screen
        print("iOS: User tapped on notification: \(userInfo)")
        
        completionHandler()
    }
    
    // The custom refreshToken() function is not needed
    // The `messaging(_:didReceiveRegistrationToken:)` delegate is the
    // correct and automatic way to handle token updates.
}
