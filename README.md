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
	*   [Installation and Setup](#android-install)
		1.   [Clone the library](#android-clone)
		2.   [Project Configuration](#android-config-project)
		3.   [Module Configuration](#android-config-module)
		4.   [Manifest Configuration](#android-config-manifest)
		5.   [Colors Configuration](#android-config-colors)
		6.   [Java Class Integration](#android-config-java)
		7.   [JavaScript Class Integration](#android-config-js)
		8.   [Initialization](#android-config-mainactivityactivity)
	*   [Java Methods](#android-methods)
	*   [JavaScript Usage for Android](#android-js-usage)
*   [iOS Implementation](#ios)
*   [Web Implementation](#web)
	*   [JavaScript Methods](#web-methods)

<a name="overview"></a>

## Overview

WUI/JS Environment is a bridge between web environments and native web rendering engines via JavaScript, designed to facilitate the creation of hybrid applications. It provides access to hardware instances and the file system directly from JavaScript.
It is currently available for Android in Java via WebView and for iOS in Swift via WebKit.

<a name="project"></a>

### About the WUI/JS Project

WUI/JS Lib is part of the WUI JS project, which currently consists of 3 repositories:

-	[https://github.com/wui-js/wuijs-main-lib](https://github.com/wui-js/wuijs-main-lib)<br>
	Main UI library.<br><br>
-	[https://github.com/wui-js/wuijs-plugins-lib](https://github.com/wui-js/wuijs-plugins-lib)<br>
	Plugins for the main UI library.<br><br>
-	[https://github.com/wui-js/wuijs-environment-lib](https://github.com/wui-js/wuijs-environment-lib)<br>
	Bridge between web environments and native web rendering engines via JavaScript.<br><br>
-	[https://github.com/wui-js/wuijs-lab](https://github.com/wui-js/wuijs-lab)<br>
	Repository with demos and usage examples for both the main UI library classes and the plugins.<br><br>

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
        ├── android/
        ├── ios/
        └── environment/
            └── test/
```

| Path                                                 | Description |
| ---------------------------------------------------- | ----------- |
| [imgs](imgs/)                                        | Images used in the documentation. |
| [imgs/logo](imgs/logo/)                              | Project logotype and isotype in SVG and PNG format. |
| [src](src/)                                          | Main sources for the latest version. |
| [src/wui-js](src/wui-js)                             | WUI/JS project directory. |
| [src/wui-js/android](src/wui-js/android/)                   | WUI/JS Environment library for Android. |
| [src/wui-js/ios](src/ios/)                           | WUI/JS Environment library for iOS. |
| [src/wui-js/environment](src/environment/)           | WUI/JS Environment library for Web. |
| [src/wui-js/environment/test](src/environment/test/) | Directory with the WUI/JS Environment library test interface. |

> [!NOTE]
> The `wuijs-environment-lib` library operates jointly, meaning the **Android + Web** or **iOS + Web** combination must be implemented for it to work correctly.

<a name="android"></a>

## Android Implementation

The Android implementation uses WebView as its rendering engine.

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

Make sure the repositories are correctly defined:

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

Make sure `buildConfig` is enabled with the value `true`:

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

Make sure the application has the necessary permissions. The bridge requires at least Internet and Network State.

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
> Use `android:scheme` to assign the application scheme. This will allow access via a URL in the browser using the scheme as the protocol.
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
    <color name="statusbarDarkColor">#226d79</color>
    <color name="statusbarDarkOverlayColor">#1c5863</color>
    <color name="navigationbarLightColor">#efeff6</color>
    <color name="navigationbarLightOverlayColor">#c0c0c6</color>
    <color name="navigationbarDarkColor">#226d79</color>
    <color name="navigationbarDarkOverlayColor">#1c5863</color>
</resources>
```

> [!WARNING]
> All keys must be defined or the Java class will throw an error.

<a name="android-config-java"></a>

#### 6. Java Class Integration `WUIEnvironment.java`

Copy the file `src/wui-js/android/WUIEnvironment.java` to your project's source folder (e.g.: `app/src/main/java/com/your/package/` if the defined package ID is `com.your.package`).

> [!IMPORTANT]
> You must edit the first line of the file to match your application's package ID:

```java
package com.your.package; // Change this to your project's package ID
```

<a name="android-config-js"></a>

#### 7. JavaScript Class Integration `wui-environment-0.1.js`

Copy the contents of the `src/web/` directory to the `assets/` directory of the Android project. The following structure is recommended:

- `app/src/main/assets/libraries/wui-js/environment/wui-environment-0.1.js`
- `app/src/main/assets/libraries/wui-js/environment/test/test.html`

This will ensure that the initialization examples work correctly.

<a name="android-config-mainactivity"></a>

#### 8. Initialization in `MainActivity.java`

```java
public class MainActivity extends AppCompatActivity {
    private WUIEnvironment wuiEnvironment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            wuiEnvironment = new WUIEnvironment(this);
            // Load test page
			// Comment out the following line after validating the test
            wuiEnvironment.openURL("file:///android_asset/libreries/wuienv/test/test.html");
            // Load start page
			// Uncomment the following line after validating the test
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

<a name="android-methods"></a>

### Java Methods

| Method                  | Return type  | Description |
| ----------------------- | ------------ | ----------- |
| `getDeviceInfo`         | `JSONObject` | `getDeviceInfo()`<br><br>Returns a JSON object with hardware details, including UUID, model, manufacturer, and OS version. |
| `getDisplayInfo`        | `JSONObject` | `getDisplayInfo()`<br><br>Returns screen metrics, including density, refresh rate, aspect ratio, safe area insets, and navigation mode (Gestures/Buttons). |
| `getAppInfo`            | `JSONObject` | `getAppInfo()`<br><br>Returns application metadata such as name, package, version, and build code. |
| `getPermissionsStatus`  | `JSONObject` | `getPermissionsStatus()`<br><br>Checks and returns the status of various system permissions (Camera, Location, Storage, etc.). |
| `getCurrentPosition`    | `void`       | `getCurrentPosition()`<br><br>Obtains the current GPS/Network coordinates and sends them back to the WebView via the `onReceiveCurrentPosition` event. |
| `getConnectionStatus`   | `JSONObject` | `getConnectionStatus()`<br><br>Checks whether an active internet connection exists and returns its type (WiFi, Mobile, etc.). |
| `setStatusbarStyle`     | `void`       | `setStatusbarStyle(colorId, darkIcons)`<br><br>Arguments:<br>**• colorId:** `string`, resource ID from colors.xml<br>**• darkIcons:** `boolean`, true for dark icons, false for light.<br><br>Updates the system status bar style. |
| `setNavigationbarStyle` | `void`       | `setNavigationbarStyle(colorId, darkIcons)`<br><br>Arguments:<br>**• colorId:** `string`, resource ID from colors.xml<br>**• darkIcons:** `boolean`, true for dark icons, false for light.<br><br>Updates the system navigation bar style. |
| `saveFile`              | `boolean`    | `saveFile(name, content)`<br><br>Arguments:<br>**• name:** `string`, file name.<br>**• content:** `string`, file content.<br><br>Writes data to the application's internal storage. |
| `readFile`              | `string`     | `readFile(name)`<br><br>Arguments:<br>**• name:** `string`, file name.<br><br>Reads data from the application's internal storage. |
| `openURL`               | `void`       | `openURL(url)`<br><br>Arguments:<br>**• url:** `string`, the destination URL or local asset path.<br><br>Opens a local asset or an external URL in the WebView. |

<a name="android-js-usage"></a>

### JavaScript Usage for Android

The native way to handle system functions is via the global `Android` object provided by the WebView. Through this object you can call any bridge function using `Android.request()`:

```javascript
// Example: Get display information
const display = JSON.parse(Android.request(JSON.stringify({ func: "getDisplayInfo" })));
console.log("Navigation mode:", display.navigationMode);
```

However, handling events sent from Java requires the global `WUIEnvironment` object provided by the JavaScript library via the public method `response()`:

```javascript
// Handling events sent from Java
WUIEnvironment.response = function(args) {
    if (args.event == "onReceiveDeepLink") {
        console.log("Deep Link received:", args.url);
    }
};
```

<a name="ios"></a>

## iOS Implementation

The iOS implementation uses WebKit (WKWebView) as its rendering engine.

### Installation and Setup

#### 1. Clone the library

If you haven't done so already, clone the repository from GitHub:

```bash
git clone https://github.com/wui-is/wuijs-environment-lib.git
```

> [!NOTE]
> The repository will contain all 3 classes: Java for Android, Swift for iOS, and JavaScript as the web counterpart.

#### 2. Swift Class Integration `WUIEnvironment.swift`

Copy the file `src/ios/WUIEnvironment.swift` into the Xcode project.

#### 3. JavaScript Class Integration `wui-environment-0.1.js`

Copy the contents of the `src/web/` directory to the `assets/` directory of the iOS project. The following structure is recommended:

- `app/src/main/assets/libraries/wui-js/environment/wui-environment-0.1.js`
- `app/src/main/assets/libraries/wui-js/environment/test/test.html`

This will ensure that the initialization examples work correctly.

<a name="web"></a>

## Web Implementation

<a name="web-methods"></a>

### JavaScript Methods

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
