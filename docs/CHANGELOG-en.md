# Change Log

## [v0.2.0] - 2026-04-26

Features:

1. Updated `getDisplayInfo()` method.
	- Added `statusbarOverlay` key:
		- **Android**: `true` when content renders behind the status bar. Detected via `FLAG_TRANSLUCENT_STATUS`, `FLAG_LAYOUT_NO_LIMITS`, or a status bar color with alpha < 255. Covers OEM devices (e.g. C2250) that apply a transparent status bar without setting the classic translucent flag.
		- **iOS**: `true` when no opaque UIView covers the status bar area (`statusbarTransparent`) or when `safeAreaInsets.top` is zero (no visible status bar).
	- Added `navigationbarOverlay` key:
		- **Android**: `true` when content renders behind the navigation bar. Detected via `FLAG_TRANSLUCENT_NAVIGATION`, `FLAG_LAYOUT_NO_LIMITS`, or a navigation bar color with alpha < 255. Covers OEM devices (e.g. C2250) that apply semi-transparent overlay bars without setting the classic translucent flag.
		- **iOS**: `true` when no opaque UIView covers the navigation area (`navigationbarTransparent`) or when `safeAreaInsets.bottom` is zero (devices with home button or landscape without gesture bar).
2. Updated to force boolean return type for `isLocal()`, `isMobile()` and `isTouch()` methods in the JS library.

## [v0.1.0] - 2026-04-25

Features:

1. Release version.