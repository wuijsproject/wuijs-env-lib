> [!NOTE]
> Para la versión en Español de este documento, consulte [LEEME.md](./LEEME.md).

> [!WARNING]
> This document is not yet finished and is in a preliminary version.

# wuijs-environment-lib

<div align="center">
	<img src="https://github.com/wui-js/wuijs-environment-lib/blob/main/imgs/logo/wuijs-environment-logotype-color.svg" width="220" height="220">
</div>

Library version: `0.1.0`

Documentation version: `0.1.0.20260406.0`

License: `Apache License 2.0`

Author: `Sergio E. Belmar V. <wuijs.project@gmail.com>`

## Table of Contents

*   [Overview](#overview)
	*   [About the WUI/JS Project](#project)
	*   [Directory Map](#dirmap)
*   [Android Implementation](#android)
	*   [Java Constructor](#android-constructor)
	*   [Java Methods](#android-methods)
	*   [Android Events](#android-events)
	*   [Installation and Setup](#android-install)
		1.   [Clone the library](#android-clone)
		2.   [Project Configuration](#android-config-project)
		3.   [Module Configuration](#android-config-module)
		4.   [Manifest Configuration](#android-config-manifest)
		5.   [Colors Configuration](#android-config-colors)
		6.   [Java Class Integration](#android-config-wui-environment-java)
		7.   [JavaScript Class Integration](#android-config-wui-environment-js)
		8.   [MainActivity Initialization](#android-config-mainactivity)
*   [iOS Implementation](#ios)
	*   [Swift Constructor](#ios-constructor)
	*   [Swift Methods](#ios-methods)
	*   [iOS Events](#ios-events)
	*   [Installation and Setup](#ios-install)
		1.   [Clone the library](#ios-clone)
		2.   [Permissions Configuration](#ios-config-permissions)
		3.   [Colors Configuration](#ios-config-colors)
		4.   [Swift Class Integration](#ios-config-wui-environment-swift)
		5.   [JavaScript Class Integration](#ios-config-wui-environment-js)
		6.   [PackageApp Initialization](#ios-config-packageapp)
		7.   [MainView Initialization](#ios-config-mainview)
*   [Web Implementation](#web)
	*   [JavaScript Methods](#web-methods)
	*   [JavaScript Usage](#web-js-usage)

<a name="overview"></a>

## Overview

WUI/JS Environment Lib is a bridge between web environments and native web rendering engines via JavaScript, designed to facilitate the creation of hybrid applications. It provides access to hardware instances and the file system directly from JavaScript.
It is currently available for Android in Java via WebView and for iOS in Swift via WebKit.

<a name="project"></a>

### About the WUI/JS Project

WUI/JS Environment Lib is part of the WUI/JS project, which currently consists of 4 repositories:

-	[https://github.com/wui-js/wuijs-main-lib](https://github.com/wui-js/wuijs-main-lib)<br>
	Main UI library.<br><br>
-	[https://github.com/wui-js/wuijs-plugins-lib](https://github.com/wui-js/wuijs-plugins-lib)<br>
	UI plugins library.<br><br>
-	[https://github.com/wui-js/wuijs-environment-lib](https://github.com/wui-js/wuijs-environment-lib)<br>
	Bridge library between a web environments and native web rendering engines.<br><br>
-	[https://github.com/wui-js/wuijs-lab](https://github.com/wui-js/wuijs-lab)<br>
	Repository with demos and usage examples for the project libraries.<br><br>

<a name="dirmap"></a>

### Directory Map

The library must be downloaded from the GitHub repository [wui-is/wuijs-environment-lib](https://github.com/wui-is/wuijs-environment-lib). This library includes 3 classes: Java for Android, Swift for iOS, and JavaScript as the web counterpart for both.

The repository directory structure is:

```bash
wuijs-environment-lib/
├── imgs/
│   └── logo/
└── src/
    └── wui-js/
        └── environment/
            ├── android/
            ├── ios/
            ├── web/
            └── demo/
```

| Path                                                              | Description |
| ----------------------------------------------------------------- | ----------- |
| [imgs](imgs/)                                                     | Images used in the documentation. |
| [imgs/logo](imgs/logo/)                                           | Project logotype and isotype in SVG and PNG format. |
| [src](src/)                                                       | Main sources for the latest version. |
| [src/wui-js](src/wui-js)                                          | WUI/JS project directory. |
| [src/wui-js/environment/android](src/wui-js/environment/android/) | WUI/JS Environment library for Android. |
| [src/wui-js/environment/ios](src/wui-js/environment/ios/)         | WUI/JS Environment library for iOS. |
| [src/wui-js/environment/web](src/wui-js/environment/web/)         | WUI/JS Environment library for Web. |
| [src/wui-js/environment/demo](src/wui-js/environment/demo/)       | Directory with the demo interface for Android and iOS environments. |

> [!NOTE]
> The `wuijs-environment-lib` library operates jointly, meaning the **Android + Web** or **iOS + Web** combination must be implemented for it to work correctly.

<a name="android"></a>

## Android Implementation

The Android implementation uses WebView as its rendering engine.

<a name="android-constructor"></a>

### Java Constructor

| Constructor | Description |
| ----------- | ----------- |
| `WUIEnvironment(Context context[, boolean developMode])` | Initializes the WUI environment with default settings. `developMode = true` allows SSL with untrusted certificates and enables debug logging. |

<a name="android-methods"></a>

### Java Methods

| Method                  | Return type  | Description |
| ----------------------- | ------------ | ----------- |
| `isAppInForeground`     | `boolean`    | `isAppInForeground()`<br><br>Checks whether the application is currently in the foreground. |
| `getDeviceInfo`         | `JSONObject` | `getDeviceInfo()`<br><br>Returns device hardware information: `id`, `uuid`, `name`, `platform`, `version`, `maker`, `model`. |
| `getDisplayInfo`        | `JSONObject` | `getDisplayInfo()`<br><br>Returns screen metrics: `width`, `height`, `density`, `densityDpi`, `orientation`, `refreshRate`, `aspectRatio`, `navigationMode`, `statusbarHeight`, `navigationbarHeight`, `notch`, and system bar style flags. |
| `getAppInfo`            | `JSONObject` | `getAppInfo()`<br><br>Returns application metadata: `name`, `version`, `package`, `build`. |
| `getPermissionsStatus`  | `JSONObject` | `getPermissionsStatus()`<br><br>Checks the status of system permissions: `phone`, `location`, `storage`, `contacts`, `camera`, `notifications`. Possible values: `granted`, `denied`, `default`. |
| `getCurrentPosition`    | `JSONObject` | `getCurrentPosition()`<br><br>Obtains current GPS/Network coordinates: `latitude`, `longitude`, `accuracy`, `provider`, `timestamp`. Requests location permission if not yet granted. |
| `getConnectionStatus`   | `boolean`    | `getConnectionStatus()`<br><br>Checks whether an active internet connection exists (WiFi, mobile data, or Ethernet). |
| `setStatusbarStyle`     | `void`       | `setStatusbarStyle(color, darkIcons)`<br><br>Arguments:<br>**• color:** `String`, HEX color (`#RRGGBB`) or a `colors.xml` key (`statusbarLightColor`, `statusbarDarkColor`, etc.).<br>**• darkIcons:** `boolean`, `true` for dark icons, `false` for light.<br><br>Updates the color and icon style of the status bar. |
| `setNavigationbarStyle` | `void`       | `setNavigationbarStyle(color, darkIcons)`<br><br>Arguments:<br>**• color:** `String`, HEX color (`#RRGGBB`) or a `colors.xml` key (`navigationbarLightColor`, `navigationbarDarkColor`, etc.).<br>**• darkIcons:** `boolean`, `true` for dark icons, `false` for light.<br><br>Updates the color and icon style of the navigation bar. |
| `saveFile`              | `boolean`    | `saveFile(name, content)`<br><br>Arguments:<br>**• name:** `String`, file name.<br>**• content:** `String`, content to save.<br><br>Writes a file to the application's private internal storage. Returns `true` on success. |
| `readFile`              | `String`     | `readFile(name)`<br><br>Arguments:<br>**• name:** `String`, file name.<br><br>Reads a file from internal storage. Returns `null` if the file does not exist or an error occurs. |
| `removeFile`            | `boolean`    | `removeFile(name)`<br><br>Arguments:<br>**• name:** `String`, file name.<br><br>Deletes a file from internal storage. Returns `true` on success. |
| `openAppSettings`       | `void`       | `openAppSettings()`<br><br>Opens the application settings screen in the system Settings app. |
| `openURL`               | `void`       | `openURL(url)`<br><br>Arguments:<br>**• url:** `String`, destination URL or local asset path (`file:///android_asset/...`).<br><br>Loads a local asset directly in the WebView or opens an external URL via the system Intent. |
| `saveDeepLink`          | `void`       | `saveDeepLink(intent)`<br><br>Arguments:<br>**• intent:** `Intent`, intent received in `onCreate` or `onNewIntent`.<br><br>Extracts and stores the Deep Link URL from the intent. If the page is already loaded, it is sent to JavaScript immediately. |
| `sendDeepLink`          | `void`       | `sendDeepLink()`<br><br>Sends the stored Deep Link URL to JavaScript by calling `WUIEnvironment.response()`. Only acts if the page is already loaded. |
| `readDeepLink`          | `String`     | `readDeepLink()`<br><br>Returns the last stored Deep Link URL, or `null` if none is stored. |
| `clearDeepLink`         | `void`       | `clearDeepLink()`<br><br>Clears the stored Deep Link URL. |

<a name="android-events"></a>

### Android Events

Events are callbacks that the native side sends to JavaScript when an asynchronous action completes. They must be configured on the `WUIEnvironment` instance before loading the first page.

| Event               | Arguments                     | Description |
| ------------------- | ----------------------------- | ----------- |
| `onDownloadFile`    | `filename`, `mimetype`, `uri` | Fired when a file download initiated from the WebView completes. The file is saved to the device's public `Downloads` directory and automatically opened with the corresponding application. |
| `onReceiveDeepLink` | `url`                         | Fired when the application receives a Deep Link URL (on launch or while running). |

<a name="android-install"></a>

### Installation and Setup

<a name="android-clone"></a>

#### 1. Clone the library

Clone the repository from the official wuiproject account on GitHub:

```bash
git clone https://github.com/wui-is/wuijs-environment-lib.git
```

> [!NOTE]
> The repository will contain all 3 classes: Java for Android, Swift for iOS, and JavaScript as the web counterpart.

<a name="android-config-project"></a>

#### 2. Project Configuration

Ensure the repositories are correctly defined:

##### **settings.gradle.kts (Kotlin)**

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
```

##### **settings.gradle (Groovy)**

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
```

<a name="android-config-module"></a>

#### 3. Module Configuration (`app/build.gradle.kts`)

Ensure `buildConfig` is enabled with the value `true`:

##### **build.gradle.kts (Kotlin)**

```kotlin
android {
    buildFeatures {
        buildConfig = true
    }
}
```

##### **build.gradle (Groovy)**

```groovy
android {
    buildFeatures {
        buildConfig true
    }
}
```

<a name="android-config-manifest"></a>

#### 4. Manifest Configuration in `AndroidManifest.xml`

**Add Permissions**

Ensure the application has the necessary permissions. The bridge requires at least Internet and Network State.

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<!-- <uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" /> -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

**Deep Link Configuration**

To enable Deep Link in the application, add the following statement to the `application.activity` section:

```xml
<intent-filter>
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />
    <data android:scheme="app_scheme" />
</intent-filter>
```

> [!IMPORTANT]
> The `android:scheme` attribute defines the application scheme. It allows access to the application via a URL in the browser using the scheme as the protocol.
> (e.g.: app_scheme://url_resource)

**File Sharing Configuration**

To enable file sharing in the application, add the following statement to the `application.activity` section:

```xml
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.provider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths" />
</provider>
```

> [!NOTE]
> This configuration is required for the implementation of downloads, camera, and file sharing via WhatsApp or Email from WebView.

<a name="android-config-colors"></a>

#### 5. Colors Configuration in `res/values/colors.xml`

The library uses these keys for the status and navigation bar styles:

```xml
<resources>
    <color name="black">#FF000000</color>
    <color name="white">#FFFFFFFF</color>
    <color name="statusbarLightColor">#f5f5f5</color>
    <color name="statusbarLightOverlayColor">#c2c2c2</color>
    <color name="statusbarDarkColor">#212121</color>
    <color name="statusbarDarkOverlayColor">#616161</color>
    <color name="navigationbarLightColor">#efeff6</color>
    <color name="navigationbarLightOverlayColor">#c0c0c6</color>
    <color name="navigationbarDarkColor">#212121</color>
    <color name="navigationbarDarkOverlayColor">#616161</color>
</resources>
```

> [!WARNING]
> All keys must be defined or the Java class will throw an error.

<a name="android-config-wui-environment-java"></a>

#### 6. Java Class Integration `WUIEnvironment.java`

Copy the file `src/wui-js/environment/android/WUIEnvironment.java` to the project's source folder (e.g.: `app/src/main/java/your/package/name/` if the defined package ID is `your.package.name`).

> [!IMPORTANT]
> The first line of the file must be edited to match the application's package ID:

```java
package YOUR.PACKAGE.NAME; // Update this to match your project package
```

<a name="android-config-wui-environment-js"></a>

#### 7. JavaScript Class Integration `wui-environment-0.1.js`

Copy the contents of the `src/wui-js/environment/web/` directory to the `assets/` directory of the Android project. The following structure is recommended:

- `app/src/main/assets/libraries/wui-js/environment/web/wui-environment-0.1.js`
- `app/src/main/assets/libraries/wui-js/environment/demo/index.html`

The above ensures that the initialization examples work correctly.

<a name="android-config-mainactivity"></a>

#### 8. MainActivity Initialization `MainActivity.java`

```java
package YOUR.PACKAGE.NAME;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

	private WUIEnvironment wuiEnvironment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            wuiEnvironment = new WUIEnvironment(this);
            // Load demo page (comment out the following line after validating the test)
            wuiEnvironment.openURL("file:///android_asset/libreries/wui-js/environment/demo/index.html");
            // Load start page (uncomment the following line after validating the test)
			//wuiEnvironment.openURL("file:///android_asset/pages/index.html");
            // Enable Deep Link requests when the app opens
            wuiEnvironment.saveDeepLink(getIntent());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        try {
            // Enable Deep Link requests while the app is running
            wuiEnvironment.saveDeepLink(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
```

<a name="ios"></a>

## iOS Implementation

The iOS implementation uses WebKit (WKWebView) as its rendering engine and communicates via `WKScriptMessageHandler`.

<a name="ios-constructor"></a>

### Swift Constructor

| Constructor | Description |
| ----------- | ----------- |
| `WUIEnvironment(viewController: UIViewController[, developMode: Bool])` | Initializes the WUI environment. `developMode = true` allows SSL with untrusted certificates and enables debug logging. |

<a name="ios-methods"></a>

### Swift Methods

| Method                  | Return type     | Description |
| ----------------------- | --------------- | ----------- |
| `isAppInForeground`     | `Bool`          | `isAppInForeground()`<br><br>Checks whether the application is currently in the foreground. |
| `getDeviceInfo`         | `[String: Any]` | `getDeviceInfo()`<br><br>Returns device hardware information: `id`, `uuid`, `name`, `platform`, `version`, `maker`, `model`. |
| `getDisplayInfo`        | `[String: Any]` | `getDisplayInfo()`<br><br>Returns screen metrics: `width`, `height`, `density`, `densityDpi`, `orientation`, `refreshRate`, `aspectRatio`, `navigationMode`, `statusbarHeight`, `navigationbarHeight`, `notch`, and system bar style flags.<br><br>`statusbarTransparent` and `navigationbarTransparent` reflect the current state of the UIView overlays managed by `setStatusbarStyle` and `setNavigationbarStyle`: `true` when no opaque overlay is placed over that area. `navigationbarLightMode` is always `false` (the iOS home indicator adapts automatically). |
| `getAppInfo`            | `[String: Any]` | `getAppInfo()`<br><br>Returns application metadata: `name`, `version`, `package`, `build`. |
| `getPermissionsStatus`  | `void`          | `getPermissionsStatus(completion)`<br><br>Arguments:<br>**• completion:** `([String: Any]) -> Void`, callback with the result.<br><br>Checks the status of system permissions: `location`, `camera`, `contacts`, `notifications`. Possible values: `granted`, `denied`, `default`, `undefined`. The `phone` and `storage` keys are always `undefined` (no equivalent system permission in iOS). Async — delivers the result on the main thread. |
| `getCurrentPosition`    | `void`          | `getCurrentPosition(completion)`<br><br>Arguments:<br>**• completion:** `([String: Any]) -> Void`, callback with the result.<br><br>Obtains current GPS coordinates: `latitude`, `longitude`, `accuracy`, `provider`, `timestamp`. Requests location permission if not yet granted. Async — delivers the result via `CLLocationManagerDelegate`.<br><br>> **Requires** `NSLocationWhenInUseUsageDescription` in `Info.plist`. Without it, iOS silently ignores the permission request. |
| `getConnectionStatus`   | `Bool`          | `getConnectionStatus()`<br><br>Checks whether an active internet connection exists. Reads the current network state synchronously via `NWPathMonitor.currentPath`; the internal `pathUpdateHandler` keeps the cached state updated for subsequent calls. |
| `setStatusbarStyle`     | `void`          | `setStatusbarStyle(color, darkIcons)`<br><br>Arguments:<br>**• color:** `String`, HEX color (`#RRGGBB`) or `Assets.xcassets` color set name (`statusbarLightColor`, `statusbarDarkColor`, etc.).<br>**• darkIcons:** `Bool`, `true` for dark icons, `false` for light.<br><br>Places a UIView with the given color over the status bar area. Icon style is applied via `preferredStatusBarStyle` — the host ViewController must expose this property and call `setNeedsStatusBarAppearanceUpdate()`. |
| `setNavigationbarStyle` | `void`          | `setNavigationbarStyle(color, darkIcons)`<br><br>Arguments:<br>**• color:** `String`, HEX color (`#RRGGBB`) or `Assets.xcassets` color set name (`navigationbarLightColor`, `navigationbarDarkColor`, etc.).<br>**• darkIcons:** `Bool`, ignored on iOS (home indicator is not configurable).<br><br>Places a UIView with the given color over the `safeAreaInsets.bottom` area. No effect on devices with a home button. |
| `saveFile`              | `Bool`          | `saveFile(name, content)`<br><br>Arguments:<br>**• name:** `String`, file name.<br>**• content:** `String`, content to save.<br><br>Writes a file to the application's `Documents` directory. Returns `true` on success. |
| `readFile`              | `String?`       | `readFile(name)`<br><br>Arguments:<br>**• name:** `String`, file name.<br><br>Reads a file from the `Documents` directory. Returns `nil` if the file does not exist or an error occurs. |
| `removeFile`            | `Bool`          | `removeFile(name)`<br><br>Arguments:<br>**• name:** `String`, file name.<br><br>Deletes a file from the `Documents` directory. Returns `true` on success. |
| `openAppSettings`       | `void`          | `openAppSettings()`<br><br>Opens the application settings screen in the system Settings app. |
| `openURL`               | `void`          | `openURL(url)`<br><br>Arguments:<br>**• url:** `String`, destination URL or local file path (`file://...`).<br><br>Loads a local file in the WKWebView via `loadFileURL` or opens an external URL via `UIApplication.shared.open`. |
| `saveDeepLink`          | `void`          | `saveDeepLink(url)`<br><br>Arguments:<br>**• url:** `URL?`, URL received in `scene(_:openURLContexts:)` or `application(_:open:)`.<br><br>Extracts and stores the Deep Link URL. If the page is already loaded, it is sent to JavaScript immediately. |
| `sendDeepLink`          | `void`          | `sendDeepLink()`<br><br>Sends the stored Deep Link URL to JavaScript by calling `WUIEnvironment.response()`. Only acts if the page is already loaded. |
| `readDeepLink`          | `String?`       | `readDeepLink()`<br><br>Returns the last stored Deep Link URL, or `nil` if none is stored. |
| `clearDeepLink`         | `void`          | `clearDeepLink()`<br><br>Clears the stored Deep Link URL. |

<a name="ios-events"></a>

### iOS Events

Events are callbacks that the native side sends to JavaScript when an asynchronous action completes. They must be configured on the `WUIEnvironment` instance before loading the first page.

| Event               | Arguments                     | Description |
| ------------------- | ----------------------------- | ----------- |
| `onDownloadFile`    | `filename`, `mimetype`, `uri` | Fired when a file download initiated from the WebView completes. The file is saved to `Documents/Downloads/` inside the app sandbox and opened via `UIActivityViewController`. Requires iOS 14.5+ for `WKDownloadDelegate`; a `URLSession` fallback is used on earlier versions. |
| `onReceiveDeepLink` | `url`                         | Fired when the application receives a Deep Link URL (on launch or while running). |

<a name="ios-install"></a>

### Installation and Setup

<a name="ios-clone"></a>

#### 1. Clone the library

Clone the repository from GitHub if it has not been cloned previously:

```bash
git clone https://github.com/wui-is/wuijs-environment-lib.git
```

> [!NOTE]
> The repository will contain all 3 classes: Java for Android, Swift for iOS, and JavaScript as the web counterpart.

<a name="ios-config-permissions"></a>

#### 2. Permissions Configuration (`Info.plist`)

If the project uses `GENERATE_INFOPLIST_FILE = YES` (Xcode 13+), add the required usage description keys directly to the target's build settings:

| Build Setting Key                                   | Required for |
| --------------------------------------------------- | ------------ |
| `INFOPLIST_KEY_NSLocationWhenInUseUsageDescription` | `getCurrentPosition()` |
| `INFOPLIST_KEY_NSCameraUsageDescription`            | `getPermissionsStatus()` (camera) |
| `INFOPLIST_KEY_NSContactsUsageDescription`          | `getPermissionsStatus()` (contacts) |

> [!WARNING]
> Without `NSLocationWhenInUseUsageDescription`, iOS silently ignores `requestWhenInUseAuthorization()` — the permission dialog never appears and `getCurrentPosition` never delivers a result.

If the project uses a manual `Info.plist`, add the keys directly to that file:

```xml
<key>NSLocationWhenInUseUsageDescription</key>
<string>Required to obtain the device's current position.</string>
<key>NSCameraUsageDescription</key>
<string>Required to check camera permission status.</string>
<key>NSContactsUsageDescription</key>
<string>Required to check contacts permission status.</string>
```

<a name="ios-config-colors"></a>

#### 3. Colors Configuration in `Assets.xcassets`

The library uses these color set names for the status and navigation bar styles:

| Color Set Name                   | Default Value |
| -------------------------------- | ------------- |
| `statusbarLightColor`            | `#f5f5f5` |
| `statusbarLightOverlayColor`     | `#c2c2c2` |
| `statusbarDarkColor`             | `#212121` |
| `statusbarDarkOverlayColor`      | `#616161` |
| `navigationbarLightColor`        | `#efeff6` |
| `navigationbarLightOverlayColor` | `#c0c0c6` |
| `navigationbarDarkColor`         | `#212121` |
| `navigationbarDarkOverlayColor`  | `#616161` |

To add a color set in Xcode: open `Assets.xcassets`, click **+** → **Color Set**, name it exactly as shown, and set the color value in the Attributes Inspector.

> [!WARNING]
> All color sets used by the application must be defined. If a named color is not found in `Assets.xcassets`, `UIColor(named:)` returns `nil` and the affected bar defaults to white.

<a name="ios-config-wui-environment-swift"></a>

#### 4. Swift Class Integration `WUIEnvironment.swift`

Copy the file `src/wui-js/environment/ios/WUIEnvironment.swift` into the Xcode project.

> [!NOTE]
> Add the file to the Xcode target's **Sources** build phase. Do not add it as a bundle resource.

<a name="ios-config-wui-environment-js"></a>

#### 5. JavaScript Class Integration `wui-environment-0.1.js`

Copy the contents of the `src/wui-js/environment/web/` directory to the `assets/` directory of the Xcode project. The following structure is recommended:

- `package/assets/libraries/wui-js/environment/web/wui-environment-0.1.js`
- `package/assets/libraries/wui-js/environment/demo/index.html`

The above ensures that the initialization examples work correctly.

<a name="ios-config-packageapp"></a>

#### 6. PackageApp Initialization `packageApp.swift`

The name of the `packageApp.swift` file changes depending on the package name assigned to the project.

```swift
import SwiftUI

@main
struct packageApp: App {
    var body: some Scene {
        WindowGroup {
            MainView()
        }
    }
}
```

<a name="ios-config-mainview"></a>

#### 7. MainView Initialization `MainView.swift`

The filename `MainView.swift` is optional; however, it should be consistent with the name of the function called from `packageApp.swift`.

```swift
import SwiftUI
import UIKit
import WebKit

struct MainView: View {
    var body: some View {
        EnvironmentView().ignoresSafeArea()
    }
}

struct EnvironmentView: UIViewControllerRepresentable {

    func makeUIViewController(context: Context) -> EnvironmentViewController {
        EnvironmentViewController()
    }

    func updateUIViewController(_ uiViewController: EnvironmentViewController, context: Context) {}
}

class EnvironmentViewController: UIViewController {

    private var wuiEnvironment: WUIEnvironment?

    override var preferredStatusBarStyle: UIStatusBarStyle {
        return wuiEnvironment?.preferredStatusBarStyle ?? .default
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        wuiEnvironment = WUIEnvironment(viewController: self)
        // Load demo page (comment out the following line after validating the test)
        wuiEnvironment?.openURL(url: "file:///\(Bundle.main.bundlePath)/assets/libraries/wui-js/environment/demo/index.html")
        // Load start page (uncomment the following line after validating the test)
        // wuiEnvironment?.openURL(url: "file:///\(Bundle.main.bundlePath)/assets/pages/index.html")
    }

    func scene(_ scene: UIScene, openURLContexts contexts: Set<UIOpenURLContext>) {
        // Enable Deep Link requests during app execution
        wuiEnvironment?.saveDeepLink(url: contexts.first?.url)
    }
}
```

<a name="web"></a>

## Web Implementation

<a name="web-methods"></a>

### JavaScript Class Methods

| Method    | Return type | Description |
| --------- | ----------- | ----------- |
| `onReady` | `void`      | `onReady(done)`<br><br>Arguments:<br>**• done:** `function`, callback that receives the total number of requests made.<br><br>Executes the callback once all pending requests have received a response. Useful for synchronizing initial data loading before rendering the UI. |

### JavaScript Instance Methods

| Method                  | Return type               | Description |
| ----------------------- |---------------------------| ----------- |
| `isAppInForeground`     | `Promise<boolean>`        | `isAppInForeground(done)`<br><br>Checks whether the application is in the foreground. |
| `getDeviceInfo`         | `Promise<Object>`         | `getDeviceInfo(done)`<br><br>Gets hardware information (UUID, model, platform, etc.). |
| `getDisplayInfo`        | `Promise<Object>`         | `getDisplayInfo(done)`<br><br>Gets screen metrics and navigation mode. |
| `getAppInfo`            | `Promise<Object>`         | `getAppInfo(done)`<br><br>Gets application metadata. |
| `getPermissionsStatus`  | `Promise<Object>`         | `getPermissionsStatus(done)`<br><br>Queries the status of system permissions. |
| `getCurrentPosition`    | `Promise<Object>`         | `getCurrentPosition(done)`<br><br>Gets the current GPS location. |
| `getConnectionStatus`   | `Promise<boolean>`        | `getConnectionStatus(done)`<br><br>Checks whether there is an active internet connection. |
| `setStatusbarStyle`     | `void`                    | `setStatusbarStyle(color, darkIcons, done)`<br><br>Arguments:<br>**• color:** `string`, color in HEX format or resource ID.<br>**• darkIcons:** `boolean`, dark icons.<br>**• done:** `function`, optional callback.<br><br>Changes the color and style of the status bar. |
| `setNavigationbarStyle` | `void`                    | `setNavigationbarStyle(color, darkIcons, done)`<br><br>Arguments:<br>**• color:** `string`, color in HEX format or resource ID.<br>**• darkIcons:** `boolean`, dark icons.<br>**• done:** `function`, optional callback.<br><br>Changes the color and style of the navigation bar. |
| `saveFile`              | `Promise<boolean>`        | `saveFile(name, content, done)`<br><br>Arguments:<br>**• name:** `string`, file name.<br>**• content:** `string\|Object`, content.<br>**• done:** `function`, optional callback.<br><br>Saves a file to local storage. |
| `readFile`              | `Promise<string\|Object>` | `readFile(name, done)`<br><br>Arguments:<br>**• name:** `string`, file name.<br>**• done:** `function`, optional callback.<br><br>Reads a file from local storage. |
| `removeFile`            | `Promise<boolean>`        | `removeFile(name, done)`<br><br>Arguments:<br>**• name:** `string`, file name.<br>**• done:** `function`, optional callback.<br><br>Deletes a file from storage. |
| `openAppSettings`       | `void`                    | `openAppSettings(done)`<br><br>Arguments:<br>**• done:** `function`, optional callback.<br><br>Opens the application settings screen. |
| `openURL`               | `void`                    | `openURL(url)`<br><br>Arguments:<br>**• url:** `string`, the destination URL or local asset path.<br><br>Opens a local resource in the WebView or an external link. |
| `readDeepLink`          | `Promise<string>`         | `readDeepLink(done)`<br><br>Arguments:<br>**• done:** `function`, optional callback.<br><br>Reads the last received Deep Link URL. |
| `clearDeepLink`         | `void`                    | `clearDeepLink(done)`<br><br>Arguments:<br>**• done:** `function`, optional callback.<br><br>Clears the stored Deep Link URL. |

<a name="web-js-usage"></a>

### JavaScript Usage

All bridge calls on iOS are asynchronous — the native side responds via `WUIEnvironment.response()` after the native operation completes.
The JavaScript library abstracts this into Promises:

```javascript
const env = new WUIEnvironment();

// Use onReady to wait for all initial requests to settle
env.getDeviceInfo(function(info) {
    console.log("Platform:", info.platform);
});

env.getDisplayInfo(function(display) {
    console.log("Notch:", display.notch);
    console.log("Status bar height:", display.statusbarHeight);
});

env.getConnectionStatus(function(connected) {
    console.log("Connected:", connected);
});

env.getCurrentPosition(function(position) {
    if (position.error) {
        console.error("Location error:", position.error);
    } else {
        console.log("Lat:", position.latitude, "Lon:", position.longitude);
    }
});

env.onReady(function(count) {
    console.log("All", count, "requests resolved");
});

// Configure event handlers before loading the first page
env.onDownloadFile = function(args) {
    console.log("Downloaded:", args.filename, args.mimetype, args.uri);
};
env.onReceiveDeepLink = function(url) {
    console.log("Deep Link received:", url);
};
```

> [!NOTE]
> When testing `getCurrentPosition` on the iOS Simulator, a simulated location must be configured: **Simulator menu → Features → Location**, on the Android Simulator, it must be configured in: **Simulator menu → Extended controls → Location**
