# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

MotionEmulator is an Android application for mocking location and sensor data using Xposed hooks and debugging methods. It targets fitness apps and games by simulating GPS traces, motion data, and telephony information.

## Build Commands

```shell
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run unit tests
./gradlew test

# Run Android instrumented tests
./gradlew connectedAndroidTest

# Clean build
./gradlew clean
```

## Required Configuration

Create `local.properties` with API keys (not tracked in git):

```properties
sdk.dir=<Android SDK Path>
amap.web.key="<AMap Web API Key>"
AMAP_SDK_KEY="<AMap Android SDK Key>"
GCP_MAPS_KEY="<Google Maps API Key>"

# Optional: Custom server for online features
server_uri="<Your Server URL>"
product="<Product Name>"
```

## Architecture

### Package Structure

- **`data/`**: Data models and persistence using `DataStore<T>` abstraction
  - `Traces`, `Motions`, `Telephonies` - Simulation data stores
  - `Emulations` - Emulation configuration store
  - `DataStore<T>` - Abstract base with JSON serialization and file I/O

- **`plugin/`**: Xposed plugin management
  - `Plugins` - Global plugin manager initialized in `MeApplication`
  - `Plugin` - Individual plugin with broadcast-based communication

- **`provider/`**: Emulation scheduling via embedded Ktor server
  - `Scheduler` - Manages agent lifecycle (PENDING → RUNNING → COMPLETED/CANCELED)
  - WebSocket-based communication with emulation agents
  - Self-signed TLS support for local server

- **`ui/`**: Jetpack Compose UI with responsive layouts
  - Three navigation tabs: Plugins, Emulate, Data
  - `AppHome` - Main scaffold with adaptive navigation (bottom bar/rail/drawer)
  - ViewModels in `ui/model/` for state management
  - Map integration with AMap and Google Maps

- **`extension/`**: Utility functions
  - Map coordinate converters (AMap, Google Maps, GCJ02/WGS84)
  - Networking with Ktor client
  - SharedPreferences helpers

### Key Patterns

1. **Data Layer**: All simulation data extends `Data` from stub library. `DataStore<T>` handles lazy loading, import/export, and file persistence.

2. **Emulation Flow**: `Scheduler` starts embedded Ktor server → Agents connect via WebSocket → Receive `EmulationInfo` → Stream `Intermediate` updates → Signal completion/failure.

3. **Plugin System**: Plugins are separate APKs with `me_plugin` metadata. Communication via Android broadcasts to `ControllerReceiver`.

4. **Navigation**: `NavigationDestinations` enum defines routes. `AppHome` adapts layout based on `WindowSizeClass`.

## Dependencies

- **Ktor**: Both client (API calls) and server (agent communication) with Protobuf serialization
- **Compose BOM**: 2024.01.00 with Material3
- **Map SDKs**: AMap 9.5.0, Google Maps 18.2.0
- **Serialization**: KotlinX Serialization with JSON and Protobuf
- **Internal**: `com.zhufucdev.me:stub` (data models), `com.zhufucdev.sdk:kotlin` (utilities), `com.zhufucdev.update:app` (auto-update)

## Development Notes

- Uses Kotlin 1.9.21 with KSP for annotation processing
- JDK 11 target compatibility
- Configuration cache enabled in `gradle.properties`
- ABI splits enabled for release builds (universal + per-ABI)
- Self-signed certificate generation for local TLS (SpongyCastle)
