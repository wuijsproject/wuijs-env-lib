//
//  File: WUIEnvironment.swift
//  Class: WUIEnvironment
//  Version: 0.1
//  Author: Sergio E. Belmar V. (wuijs.project@gmail.com)
//  Copyright: Sergio E. Belmar V. (wuijs.project@gmail.com)
//

import Foundation
import WebKit
import UIKit
import CoreLocation
import AVFoundation
import Contacts
import UserNotifications
import Network

class WUIEnvironment: NSObject {

    var webView: WKWebView!
    private weak var viewController: UIViewController?
    private var developMode: Bool = false
    private var deepLinkURL: String? = nil
    private var pageLoaded: Bool = false
    private let logTag = "WUIEnvironment"

    // Status/navigation bar overlay views
    private weak var statusbarView: UIView?
    private weak var navigationbarView: UIView?
    /// Host ViewController should read this from `preferredStatusBarStyle` and call
    /// `setNeedsStatusBarAppearanceUpdate()` after each change.
    var preferredStatusBarStyle: UIStatusBarStyle = .default

    // Network monitoring
    private var networkMonitor: NWPathMonitor?
    private var networkQueue = DispatchQueue(label: "WUIEnvironment.network")
    private var isConnected: Bool = false

    // Location
    private var locationManager: CLLocationManager?
    private var locationCompletion: (([String: Any]) -> Void)?

    // MARK: - Constructors

    init(viewController: UIViewController, developMode: Bool = false) {
        super.init()
        self.viewController = viewController
        self.developMode = developMode
        webViewInit()
        setupNetworkMonitor()
    }

    // MARK: - Private Setup

    private func webViewInit() {
        let contentController = WKUserContentController()
        contentController.add(self, name: "request")
        let config = WKWebViewConfiguration()
        config.userContentController = contentController
        config.allowsInlineMediaPlayback = true
        if #available(iOS 10.0, *) {
            config.mediaTypesRequiringUserActionForPlayback = []
        }
        webView = WKWebView(frame: .zero, configuration: config)
        webView.navigationDelegate = self
        if let vc = viewController {
            vc.view.addSubview(webView)
            webView.translatesAutoresizingMaskIntoConstraints = false
            NSLayoutConstraint.activate([
                webView.topAnchor.constraint(equalTo: vc.view.topAnchor),
                webView.bottomAnchor.constraint(equalTo: vc.view.bottomAnchor),
                webView.leadingAnchor.constraint(equalTo: vc.view.leadingAnchor),
                webView.trailingAnchor.constraint(equalTo: vc.view.trailingAnchor)
            ])
        }
        setupUserAgent()
    }

    /// Appends "WUIEnvironment (name/version)" to the default WKWebView user agent.
    /// This is what wui-environment-0.1.js reads to detect the wui.ios environment.
    private func setupUserAgent() {
        let appInfo = getAppInfo()
        let name = appInfo["name"] as? String ?? ""
        let version = appInfo["version"] as? String ?? ""
        webView.evaluateJavaScript("navigator.userAgent") { [weak self] result, _ in
            guard let self = self else { return }
            let base = result as? String ?? ""
            if !base.isEmpty {
                self.webView.customUserAgent = "\(base) WUIEnvironment (\(name)/\(version))"
            } else {
                let device = UIDevice.current
                let sysVersion = device.systemVersion.replacingOccurrences(of: ".", with: "_")
                let model = device.model.lowercased().contains("ipad") ? "iPad" : "iPhone"
                self.webView.customUserAgent = "Mozilla/5.0 (\(model); CPU iPhone OS \(sysVersion) like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 WUIEnvironment (\(name)/\(version))"
            }
        }
    }

    private func setupNetworkMonitor() {
        networkMonitor = NWPathMonitor()
        networkMonitor?.pathUpdateHandler = { [weak self] path in
            self?.isConnected = (path.status == .satisfied)
        }
        networkMonitor?.start(queue: networkQueue)
    }

    // MARK: - Public Methods

    func isAppInForeground() -> Bool {
        var result = false
        let read = { result = UIApplication.shared.applicationState == .active }
        Thread.isMainThread ? read() : DispatchQueue.main.sync { read() }
        return result
    }

    func getDeviceInfo() -> [String: Any] {
        let device = UIDevice.current
        let vendorId = device.identifierForVendor?.uuidString ?? ""
        return [
            "id":       vendorId,
            "uuid":     vendorId,
            "name":     device.name,
            "platform": "iOS",
            "version":  device.systemVersion,
            "maker":    "Apple",
            "model":    device.model
        ]
    }

    func getDisplayInfo() -> [String: Any] {
        var result: [String: Any] = [:]
        let read: () -> Void = {
            let screen = UIScreen.main
            let bounds = screen.bounds
            let scale = screen.scale
            let width = Int(bounds.width)
            let height = Int(bounds.height)
            let maxDim = max(bounds.width, bounds.height)
            let minDim = min(bounds.width, bounds.height)
            let orientation = bounds.width > bounds.height ? "landscape" : "portrait"
            let refreshRate: Int
            var statusbarHeight = 0
            var navigationbarHeight = 0
            var notch = false
            var statusbarLightMode = false
            if #available(iOS 10.3, *) {
                refreshRate = screen.maximumFramesPerSecond
            } else {
                refreshRate = 60
            }
            if let window = UIApplication.shared.windows.first(where: { $0.isKeyWindow }) {
                let insets = window.safeAreaInsets
                statusbarHeight = Int(insets.top)
                navigationbarHeight = Int(insets.bottom)
                notch = insets.top > 20
            }
            if #available(iOS 13.0, *) {
                if let scene = UIApplication.shared.connectedScenes.first as? UIWindowScene {
                    statusbarLightMode = scene.statusBarManager?.statusBarStyle == .darkContent
                }
            }
            result = [
                "width":                     width,
                "height":                    height,
                "density":                   Double(scale),
                "densityDpi":                Int(scale * 160),
                "orientation":               orientation,
                "refreshRate":               refreshRate,
                "aspectRatio":               minDim > 0 ? Double(maxDim / minDim) : 0.0,
                "navigationMode":            "gestures",
                "statusbarHeight":           statusbarHeight,
                "navigationbarHeight":       navigationbarHeight,
                "notch":                     notch,
                "statusbarTransparent":      true,
                "statusbarLightMode":        statusbarLightMode,
                "navigationbarTransparent":  true,
                "navigationbarLightMode":    false,
                "systembarDrawsBackgrounds": false
            ]
        }
        Thread.isMainThread ? read() : DispatchQueue.main.sync { read() }
        return result
    }

    func getAppInfo() -> [String: Any] {
        let info = Bundle.main.infoDictionary
        return [
            "name":    info?["CFBundleName"] as? String ?? "",
            "version": info?["CFBundleShortVersionString"] as? String ?? "",
            "package": Bundle.main.bundleIdentifier ?? "",
            "build":   info?["CFBundleVersion"] as? String ?? ""
        ]
    }

    /// Async — result is delivered via completion on the main thread.
    func getPermissionsStatus(completion: @escaping ([String: Any]) -> Void) {
        var permissions: [String: String] = [
            "phone":         "undefined",
            "location":      "undefined",
            "storage":       "undefined",
            "contacts":      "undefined",
            "camera":        "undefined",
            "notifications": "undefined"
        ]

        // Location (sync)
        switch CLLocationManager.authorizationStatus() {
			case .authorizedAlways, .authorizedWhenInUse: permissions["location"] = "granted"
			case .denied, .restricted:                    permissions["location"] = "denied"
			case .notDetermined:                          permissions["location"] = "default"
			@unknown default: break
        }

        // Camera (sync)
        switch AVCaptureDevice.authorizationStatus(for: .video) {
			case .authorized:            permissions["camera"] = "granted"
			case .denied, .restricted:   permissions["camera"] = "denied"
			case .notDetermined:         permissions["camera"] = "default"
			@unknown default: break
        }

        // Contacts (sync)
        switch CNContactStore.authorizationStatus(for: .contacts) {
			case .authorized:            permissions["contacts"] = "granted"
			case .denied, .restricted:   permissions["contacts"] = "denied"
			case .notDetermined:         permissions["contacts"] = "default"
			@unknown default: break
        }

        // Notifications (async — must be last)
        UNUserNotificationCenter.current().getNotificationSettings { settings in
            switch settings.authorizationStatus {
				case .authorized, .provisional: permissions["notifications"] = "granted"
				case .denied:                   permissions["notifications"] = "denied"
				case .notDetermined:            permissions["notifications"] = "default"
				@unknown default: break
            }
            DispatchQueue.main.async { completion(permissions) }
        }
    }

    /// Async — result is delivered via completion on the main thread.
    func getCurrentPosition(completion: @escaping ([String: Any]) -> Void) {
        locationCompletion = completion
        let manager = CLLocationManager()
        manager.delegate = self
        manager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager = manager
        let status = CLLocationManager.authorizationStatus()
        switch status {
        case .notDetermined:
            manager.requestWhenInUseAuthorization()
        case .denied, .restricted:
            locationCompletion?(["error": "Location permission denied"])
            locationCompletion = nil
            locationManager = nil
        default:
            if CLLocationManager.locationServicesEnabled() {
                manager.requestLocation()
            } else {
                locationCompletion?(["error": "Location services are disabled"])
                locationCompletion = nil
                locationManager = nil
            }
        }
    }

    func getConnectionStatus() -> Bool {
        return isConnected
    }

    /// Sets the status bar background color and icon style.
    /// To control the icon style (dark/light icons), the host ViewController must expose
    /// `preferredStatusBarStyle` via this instance and call `setNeedsStatusBarAppearanceUpdate()`.
    func setStatusbarStyle(color: String, darkIcons: Bool) {
        DispatchQueue.main.async { [weak self] in
            guard let self = self, let vc = self.viewController else { return }
            let uiColor = self.parseColor(color) ?? UIColor.white
            self.statusbarView?.removeFromSuperview()
            let height: CGFloat
            if let window = UIApplication.shared.windows.first(where: { $0.isKeyWindow }) {
                height = window.safeAreaInsets.top
            } else {
                height = UIApplication.shared.statusBarFrame.height
            }
            let view = UIView(frame: CGRect(x: 0, y: 0, width: vc.view.bounds.width, height: height))
            view.backgroundColor = uiColor
            view.autoresizingMask = [.flexibleWidth]
            vc.view.addSubview(view)
            vc.view.bringSubviewToFront(view)
            self.statusbarView = view
            self.preferredStatusBarStyle = darkIcons ? .darkContent : .lightContent
            vc.setNeedsStatusBarAppearanceUpdate()
            NSLog("%@ Statusbar set color: %@, darkIcons: %@", self.logTag, color, darkIcons ? "true" : "false")
        }
    }

    /// Sets a background color over the home indicator area (iOS safe area bottom inset).
    /// iOS does not expose a system-level navigation bar equivalent to Android's;
    /// this method places a UIView overlay over the bottom safe area.
    func setNavigationbarStyle(color: String, darkIcons: Bool) {
        DispatchQueue.main.async { [weak self] in
            guard let self = self, let vc = self.viewController else { return }
            let uiColor = self.parseColor(color) ?? UIColor.white
            self.navigationbarView?.removeFromSuperview()
            let safeBottom: CGFloat
            if #available(iOS 11.0, *) {
                safeBottom = vc.view.safeAreaInsets.bottom
            } else {
                safeBottom = 0
            }
            guard safeBottom > 0 else { return }
            let yPos = vc.view.bounds.height - safeBottom
            let view = UIView(frame: CGRect(x: 0, y: yPos, width: vc.view.bounds.width, height: safeBottom))
            view.backgroundColor = uiColor
            view.autoresizingMask = [.flexibleWidth, .flexibleTopMargin]
            vc.view.addSubview(view)
            vc.view.bringSubviewToFront(view)
            self.navigationbarView = view
            NSLog("%@ Navigationbar set color: %@", self.logTag, color)
        }
    }

    @discardableResult
    func saveFile(name: String, content: String) -> Bool {
        let fm = FileManager.default
        guard let dir = fm.urls(for: .documentDirectory, in: .userDomainMask).first else { return false }
        let url = dir.appendingPathComponent(name)
        do {
            let parentDir = url.deletingLastPathComponent()
            if !fm.fileExists(atPath: parentDir.path) {
                try fm.createDirectory(at: parentDir, withIntermediateDirectories: true)
            }
            try content.write(to: url, atomically: true, encoding: .utf8)
            NSLog("%@ File saved: %@", logTag, name)
            return true
        } catch {
            NSLog("%@ Failed to save file: %@ - %@", logTag, name, error.localizedDescription)
            return false
        }
    }

    func readFile(name: String) -> String? {
        let fm = FileManager.default
        guard let url = fm.urls(for: .documentDirectory, in: .userDomainMask).first?.appendingPathComponent(name) else { return nil }
        do {
            let content = try String(contentsOf: url, encoding: .utf8)
            NSLog("%@ File read: %@", logTag, name)
            return content
        } catch {
            NSLog("%@ Failed to read file: %@ - %@", logTag, name, error.localizedDescription)
            return nil
        }
    }

    @discardableResult
    func removeFile(name: String) -> Bool {
        let fm = FileManager.default
        guard let url = fm.urls(for: .documentDirectory, in: .userDomainMask).first?.appendingPathComponent(name) else { return false }
        do {
            try fm.removeItem(at: url)
            NSLog("%@ File removed: %@", logTag, name)
            return true
        } catch {
            NSLog("%@ Failed to remove file: %@ - %@", logTag, name, error.localizedDescription)
            return false
        }
    }

    func openAppSettings() {
        DispatchQueue.main.async {
            if let url = URL(string: UIApplication.openSettingsURLString) {
                UIApplication.shared.open(url)
            }
        }
    }

    func openURL(url: String) {
        DispatchQueue.main.async { [weak self] in
            guard let self = self else { return }
            NSLog("%@ openURL requested: %@", self.logTag, url)
            if url.hasPrefix("file://"), let fileURL = URL(string: url) {
                let accessDir = fileURL.deletingLastPathComponent()
                self.webView.loadFileURL(fileURL, allowingReadAccessTo: accessDir)
            } else if let externalURL = URL(string: url) {
                UIApplication.shared.open(externalURL)
            }
        }
    }

    func saveDeepLink(url: URL?) {
        guard let url = url else { return }
        deepLinkURL = url.absoluteString
        sendDeepLink()
    }

    func sendDeepLink() {
        guard let url = deepLinkURL, pageLoaded else { return }
        pushJavascript(arguments: ["event": "onReceiveDeepLink", "url": url])
    }

    func readDeepLink() -> String? {
        return deepLinkURL
    }

    func clearDeepLink() {
        deepLinkURL = nil
    }

    // MARK: - Private Helpers

    private func pushJavascript(arguments: [String: Any]) {
        guard let data = try? JSONSerialization.data(withJSONObject: arguments),
              let json = String(data: data, encoding: .utf8) else { return }
        let js = "WUIEnvironment.response(\(json))"
        DispatchQueue.main.async { [weak self] in
            self?.webView.evaluateJavaScript(js, completionHandler: nil)
        }
    }

    private func parseColor(_ color: String) -> UIColor? {
        if color.hasPrefix("#") {
            var hex = String(color.dropFirst())
            if hex.count == 6 { hex = "FF" + hex }
            guard hex.count == 8 else { return nil }
            var value: UInt64 = 0
            Scanner(string: hex).scanHexInt64(&value)
            return UIColor(
                red:   CGFloat((value >> 16) & 0xFF) / 255,
                green: CGFloat((value >> 8)  & 0xFF) / 255,
                blue:  CGFloat(value         & 0xFF) / 255,
                alpha: CGFloat((value >> 24) & 0xFF) / 255
            )
        }
        return UIColor(named: color)
    }
}

// MARK: - WKScriptMessageHandler

extension WUIEnvironment: WKScriptMessageHandler {

    func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        guard message.name == "request",
              let json = message.body as? [String: Any],
              let funcName = json["func"] as? String else { return }
        let code = json["code"] as? Int

        func push(_ response: Any) {
            guard let code = code else { return }
            pushJavascript(arguments: ["code": code, "response": response])
        }

        switch funcName {
			case "isAppInForeground":
				push(isAppInForeground())
			case "getDeviceInfo":
				push(getDeviceInfo())
			case "getDisplayInfo":
				push(getDisplayInfo())
			case "getAppInfo":
				push(getAppInfo())
			case "getPermissionsStatus":
				// Async: push happens in completion handler
				getPermissionsStatus { result in push(result) }
				return
			case "getCurrentPosition":
				// Async: push happens in CLLocationManagerDelegate
				getCurrentPosition { result in push(result) }
				return
			case "getConnectionStatus":
				push(getConnectionStatus())
			case "setStatusbarStyle":
				if let color = json["color"] as? String, let darkIcons = json["darkIcons"] as? Bool {
					setStatusbarStyle(color: color, darkIcons: darkIcons)
				}
				push("null")
			case "setNavigationbarStyle":
				if let color = json["color"] as? String, let darkIcons = json["darkIcons"] as? Bool {
					setNavigationbarStyle(color: color, darkIcons: darkIcons)
				}
				push("null")
			case "saveFile":
				if let name = json["name"] as? String, let content = json["content"] as? String {
					push(saveFile(name: name, content: content))
				} else {
					push(false)
				}
			case "readFile":
				if let name = json["name"] as? String {
					push(readFile(name: name) as Any)
				} else {
					push("")
				}
			case "removeFile":
				if let name = json["name"] as? String {
					push(removeFile(name: name))
				} else {
					push(false)
				}
			case "openAppSettings":
				openAppSettings()
				push("null")
			case "openURL":
				if let url = json["url"] as? String { openURL(url: url) }
				push("null")
			case "readDeepLink":
				push(readDeepLink() as Any)
			case "clearDeepLink":
				clearDeepLink()
				push("null")
			default:
				push("")
        }
    }
}

// MARK: - WKNavigationDelegate

extension WUIEnvironment: WKNavigationDelegate {

    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        pageLoaded = true
        NSLog("%@ Page loaded: %@", logTag, webView.url?.absoluteString ?? "")
        sendDeepLink()
    }

    func webView(_ webView: WKWebView, didFail navigation: WKNavigation!, withError error: Error) {
        NSLog("%@ WebView error: %@", logTag, error.localizedDescription)
    }

    func webView(_ webView: WKWebView, didFailProvisionalNavigation navigation: WKNavigation!, withError error: Error) {
        NSLog("%@ WebView provisional navigation error: %@", logTag, error.localizedDescription)
    }

    func webView(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
        guard let url = navigationAction.request.url else {
            decisionHandler(.allow)
            return
        }
        if url.isFileURL || url.scheme == "about" {
            decisionHandler(.allow)
        } else if navigationAction.navigationType == .linkActivated {
            UIApplication.shared.open(url)
            decisionHandler(.cancel)
        } else {
            decisionHandler(.allow)
        }
    }
}

// MARK: - CLLocationManagerDelegate

extension WUIEnvironment: CLLocationManagerDelegate {

    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        guard let location = locations.last else { return }
        let result: [String: Any] = [
            "latitude":  location.coordinate.latitude,
            "longitude": location.coordinate.longitude,
            "accuracy":  location.horizontalAccuracy,
            "provider":  "gps",
            "timestamp": Int(location.timestamp.timeIntervalSince1970 * 1000)
        ]
        DispatchQueue.main.async { [weak self] in
            self?.locationCompletion?(result)
            self?.locationCompletion = nil
            self?.locationManager = nil
        }
    }

    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        NSLog("%@ Location error: %@", logTag, error.localizedDescription)
        DispatchQueue.main.async { [weak self] in
            self?.locationCompletion?(["error": "Failed to get location: \(error.localizedDescription)"])
            self?.locationCompletion = nil
            self?.locationManager = nil
        }
    }

    func locationManagerDidChangeAuthorization(_ manager: CLLocationManager) {
        switch manager.authorizationStatus {
			case .authorizedWhenInUse, .authorizedAlways:
				if CLLocationManager.locationServicesEnabled() {
					manager.requestLocation()
				}
			case .denied, .restricted:
				DispatchQueue.main.async { [weak self] in
					self?.locationCompletion?(["error": "Location permission denied"])
					self?.locationCompletion = nil
					self?.locationManager = nil
				}
			default:
				break
        }
    }
}
