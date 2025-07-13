import UIKit
import SwiftUI
import ComposeApp
import GoogleMaps

struct GoogleMapView: UIViewRepresentable {
    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }

    func makeUIView(context: Context) -> GMSMapView {
        let options = GMSMapViewOptions()
        options.camera = GMSCameraPosition.camera(withLatitude: 23.0, longitude: 78.0, zoom: 5.0)

        let mapView = GMSMapView(options: options)
        mapView.delegate = context.coordinator

        // hook up location manager
        context.coordinator.mapView = mapView
        context.coordinator.requestLocationAuthorization()

        // Listen for search click event
        CommonKt.onSearchClick = { latitude, longitude in
            // Handle search click event
            context.coordinator.setMarkerAndAnimateCamera(latitude: Double(truncating: latitude), longitude: Double(truncating: longitude))
        }

        // Listen for Digipin click event
        CommonKt.onShareDigipinClick = { digipin in
            // Handle share Digipin click event
            let latLong = DigiPinUsecase().getLatLngByDigipin(digiPin: digipin)
            guard let latNumber = latLong.first,
                  let longNumber = latLong.second
            else {
                return
            }
            let latitude = Double(truncating: latNumber)
            let longitude = Double(truncating: longNumber)

            let shareMessage = "Digipin: \(digipin)\n\nMaps Link: https://www.google.com/maps/search/?api=1&query=\(latitude),\(longitude)"

            let activityVC = UIActivityViewController(activityItems: [shareMessage], applicationActivities: nil)
            if let scene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
               let top = scene.windows.first?.rootViewController {
                top.present(activityVC, animated: true)
            }
        }

        // Listen for Go to My Location click event
        CommonKt.onGoToMyLocationClick = {
            // Handle go to my location click event
            context.coordinator.requestLocationAuthorization()
            if CLLocationManager.locationServicesEnabled() {
                mapView.clear()
                context.coordinator.locationManager.startUpdatingLocation()
            } else {
                print("Location services are not enabled.")
            }
        }

        return mapView
    }

    func updateUIView(_ uiView: GMSMapView, context: Context) {
    }

    class Coordinator: NSObject, GMSMapViewDelegate, CLLocationManagerDelegate {
        var parent: GoogleMapView
        var locationManager = CLLocationManager()
        weak var mapView: GMSMapView?

        init(_ parent: GoogleMapView) {
            self.parent = parent
            super.init()
            locationManager.delegate = self
        }

        func requestLocationAuthorization() {
            locationManager.requestWhenInUseAuthorization()
        }

        func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
            guard let mapView = mapView else {
                return
            }
            switch status {
            case .authorizedWhenInUse, .authorizedAlways:
                mapView.isMyLocationEnabled = true
                mapView.settings.myLocationButton = false
                manager.startUpdatingLocation()
            default:
                mapView.isMyLocationEnabled = false
                mapView.settings.myLocationButton = false
            }
        }

        func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
            guard let location = locations.first, let mapView = mapView else {
                return
            }
            let camera = GMSCameraPosition.camera(
                withLatitude: location.coordinate.latitude,
                longitude: location.coordinate.longitude,
                zoom: 17.0
            )
            mapView.animate(to: camera)
            manager.stopUpdatingLocation()

            let digiPin = DigiPinUsecase().getDigiPin(lat: location.coordinate.latitude, lon: location.coordinate.longitude)
            let actionData = MapActionDataDigipin(digiPin: digiPin)
            MainViewControllerKt.mapActionCallback(actionData)
        }

        func mapView(_ mapView: GMSMapView, didLongPressAt coordinate: CLLocationCoordinate2D) {
            mapView.clear()
            let marker = GMSMarker(position: coordinate)
            marker.title = "Selected Digipin Location"
            marker.snippet = "Lat: \(coordinate.latitude), Lng: \(coordinate.longitude)"
            marker.map = mapView

            let digiPin = DigiPinUsecase().getDigiPin(lat: coordinate.latitude, lon: coordinate.longitude)
            print("Digipin: \(digiPin)")

            let actionData = MapActionDataDigipin(digiPin: digiPin)
            MainViewControllerKt.mapActionCallback(actionData)
        }

        func setMarkerAndAnimateCamera(latitude: Double, longitude: Double) {
            let searchedCoordinates = CLLocationCoordinate2D(latitude: latitude, longitude: longitude)
            guard let mapView = mapView else {
                return
            }

            // Add marker
            mapView.clear()
            let marker = GMSMarker(position: searchedCoordinates)
            marker.map = mapView

            // Animate camera
            let cameraUpdate = GMSCameraUpdate.setTarget(searchedCoordinates, zoom: 17.0)
            mapView.animate(with: cameraUpdate)

            // Fetch Digipin data
            let digipin = DigiPinUsecase().getDigiPin(
                lat: searchedCoordinates.latitude,
                lon: searchedCoordinates.longitude
            )

            // Handle map action
            let actionData = MapActionDataDigipin(digiPin: digipin)
            MainViewControllerKt.mapActionCallback(actionData)
        }
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(
            mapUIViewController: { () -> UIViewController in
                return UIHostingController(
                    rootView: GoogleMapView()
                        .ignoresSafeArea()
                )
            }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea(edges: .all) // Add this line
            .ignoresSafeArea(.keyboard)
    }
}
