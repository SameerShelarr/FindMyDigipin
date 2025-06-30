import SwiftUI
import GoogleMaps

@main
struct iOSApp: App {

    init() {
        if let apiKey = Bundle.main.object(forInfoDictionaryKey: "GOOGLE_MAPS_API_KEY") as? String {
            GMSServices.provideAPIKey(apiKey)
        } else {
            fatalError("Google Maps API key not found in Info.plist")
        }
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
