# FindMyDigipin

[![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-Multiplatform-7F52FF.svg?logo=kotlin)](https://kotlinlang.org/docs/multiplatform.html)
[![Android](https://img.shields.io/badge/Platform-Android-3DDC84.svg?logo=android)](https://www.android.com/)
[![iOS](https://img.shields.io/badge/Platform-iOS-000000.svg?logo=ios)](https://www.apple.com/ios/)

A **Kotlin Multiplatform Mobile (KMM)** application for finding and sharing **Digipin** codes - India's revolutionary digital addressing system developed by India Post.

## 🇮🇳 What is Digipin?

**Digipin** (Digital Postal Index Number) is a groundbreaking **10-digit alphanumeric addressing system** developed by **India Post** in collaboration with **IIT Hyderabad**, **NRSC (National Remote Sensing Centre)**, and **ISRO**. 

### Key Features of Digipin:
- **Precision**: Each Digipin represents a unique **4m × 4m area** anywhere in India
- **Coverage**: Covers entire Indian territory including maritime zones
- **Format**: 10-character code using symbols: `2, 3, 4, 5, 6, 7, 8, 9, C, J, K, L, M, P, F, T`
- **Structure**: Hierarchical grid system (e.g., `39J-49L-L8T4`)
- **Coordinates**: Encodes latitude and longitude into a human-readable format

> 📍 **Example**: New Delhi's Dak Bhawan has Digipin: `39J-49L-L8T4`

Learn more about Digipin: [India Post Digipin Service](https://www.indiapost.gov.in/VAS/Pages/digipin.aspx)

## 🚀 What This App Does

**FindMyDigipin** makes it easy to:

- 🗺️ **Find Digipin codes** for any location on an interactive map
- 📍 **Get your current location's** Digipin instantly  
- 👆 **Long press on map** to get Digipin for any visible location
- 🔍 **Search locations** using latitude/longitude coordinates
- 🏠 **Find locations** using existing Digipin codes
- 📤 **Share Digipin codes** with others
- 📋 **Copy Digipin codes** to clipboard
- 🧭 **Navigate to specific locations** on the map

## 🎬 Demo Video

**Watch FindMyDigipin in action!**

See the app running on both Android emulator and iOS simulator, demonstrating all the key features including map interactions, Digipin generation, search functionality, and more.
<video src='https://github.com/user-attachments/assets/8658bf1a-4de3-4e3c-b720-3a8ae5f79fd1' />

## 📱 Platform Support

This Kotlin Multiplatform Mobile project targets:

| Platform | Status | Features |
|----------|--------|----------|
| **Android** | ✅ Supported | Full functionality with Google Maps integration |
| **iOS** | ✅ Supported | Full functionality with Google Maps integration |

## 🛠️ Technical Architecture

### Project Structure

```
FindMyDigipin/
├── composeApp/                    # Shared Compose Multiplatform code
│   ├── src/
│   │   ├── commonMain/           # Shared business logic
│   │   │   ├── kotlin/
│   │   │   │   └── com/sameershelar/findmydigipin/
│   │   │   │       ├── domain/   # Core Digipin algorithms
│   │   │   │       ├── ui/       # Shared UI components
│   │   │   │       └── utils/    # Common utilities
│   │   ├── androidMain/          # Android-specific code
│   │   └── iosMain/              # iOS-specific code
├── iosApp/                       # iOS application wrapper
└── gradle/                       # Build configuration
```

### Core Components

- **`DigiPinUsecase.kt`**: Core algorithms for Digipin ↔ Coordinates conversion
- **`MapComponent.kt`**: Platform-agnostic map interface
- **`App.kt`**: Main UI with search functionality and user interactions
- **`UserGuideDialog.kt`**: Interactive user guide for first-time users

### Key Features Implementation

#### 🧮 Digipin Algorithm
- Hierarchical grid partitioning (10 levels)
- Latitude range: 2.5° - 38.5° North
- Longitude range: 63.5° - 99.5° East
- 4×4 subdivision at each level
- Anticlockwise spiral labeling for directional properties

#### 🎯 Location Services
- Real-time location detection
- Interactive map with tap-to-select
- Search by coordinates or Digipin
- Bidirectional conversion (Coords ↔ Digipin)

#### 📱 User Experience
- Clean Material Design 3 UI
- Search modes: Lat/Long and Digipin
- Share and copy functionality
- User guide for first-time users
- Responsive design across Android and iOS

## 🏗️ Getting Started

### Prerequisites

- **Android Studio** (Arctic Fox or later)
- **Xcode** (for iOS development)
- **JDK 11** or later
- **Kotlin Multiplatform** plugin

### Setup & Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/SameerShelarr/FindMyDigipin.git
   cd FindMyDigipin
   ```

2. **Open in Android Studio**
   - Open the project in Android Studio
   - Sync Gradle files
   - Wait for indexing to complete

3. **Run on Android and iOS**

   **Android:**
   - In Android Studio, select **`FindMyDigipin.composeApp`** module from the run configuration dropdown
   - Choose your target device/emulator
   - Click the **Run** button (▶️) in Android Studio

   **iOS:** 
   - Open `iosApp/iosApp.xcodeproj` in Xcode
   - Select target device/simulator
   - Build and run

## 📋 Usage Guide

### 1. **Find Current Location Digipin**
- Tap the location button (📍) to get your current position's Digipin
- The Digipin will appear in the bottom card

### 2. **Get Digipin for Any Map Location**
- **Long press** anywhere on the map to get the Digipin for that specific location
- The map will show the selected point and display its Digipin code
- Perfect for exploring and finding Digipins of places you can see on the map

### 3. **Search by Coordinates**
- Tap the search button (🔍)
- Select "Lat Long" mode  
- Enter latitude and longitude
- Tap search to find the location and its Digipin

### 4. **Search by Digipin**
- Tap the search button (🔍)
- Select "Digipin" mode
- Enter a valid Digipin code (with or without dashes)
- Tap search to find the location on map

### 5. **Share & Copy**
- Once you have a Digipin, use the share (📤) or copy (📋) icons
- Share via messaging apps or copy to clipboard

### 6. **User Guide**
- Tap the question mark (❓) button for interactive help
- First-time users will see an automatic guide after 10 seconds

## 🔧 Dependencies

### Core Technologies
- **Kotlin Multiplatform**: Shared business logic
- **Compose Multiplatform**: Cross-platform UI framework
- **Material 3**: Modern design system
- **Coroutines**: Asynchronous programming

### Platform-Specific
- **Android**: Google Maps SDK, Android location services
- **iOS**: Google Maps SDK (GMSMapView), Core Location

### Third-Party Libraries
- **Settings**: `com.russhwolf.settings` for preferences
- **Serialization**: Kotlinx serialization for data classes

## 🤝 Contributing

We welcome contributions! Here's how you can help:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

### Development Guidelines
- Follow Kotlin coding conventions
- Add tests for new features
- Update documentation as needed
- Ensure cross-platform compatibility

## 🙏 Acknowledgments

- **India Post** for developing the Digipin system
- **IIT Hyderabad** for the technical collaboration
- **NRSC & ISRO** for geospatial expertise
- **JetBrains** for Kotlin Multiplatform technology
- **Google** for Compose Multiplatform framework

## 📞 Support

- 🐛 **Bug Reports**: [Open an issue](https://github.com/SameerShelarr/FindMyDigipin/issues)
- 💡 **Feature Requests**: [Request a feature](https://github.com/SameerShelarr/FindMyDigipin/issues)
- 📧 **Contact**: workingshelar@gmail.com

---

**Built with ❤️ using Kotlin Multiplatform**

*Helping India navigate with precision, one Digipin at a time! 🇮🇳*
