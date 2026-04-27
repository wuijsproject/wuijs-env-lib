# Change Log

## [v0.2.0] - 2026-04-26

Características:

1. Se actualizó el método `getDisplayInfo()`.
	- Se agregó la clave `statusbarOverlay`:
		- **Android**: `true` cuando el contenido se renderiza detrás de la barra de estado. Se detecta mediante `FLAG_TRANSLUCENT_STATUS`, `FLAG_LAYOUT_NO_LIMITS`, o un color de barra de estado con alpha < 255. Cubre dispositivos OEM (ej. C2250) que aplican barra de estado transparente sin activar el flag clásico translúcido.
		- **iOS**: `true` cuando no hay ninguna UIView opaca cubriendo la barra de estado (`statusbarTransparent`) o cuando `safeAreaInsets.top` es cero (sin barra de estado visible).
	- Se agregó la clave `navigationbarOverlay`:
		- **Android**: `true` cuando el contenido se renderiza detrás de la barra de navegación. Se detecta mediante `FLAG_TRANSLUCENT_NAVIGATION`, `FLAG_LAYOUT_NO_LIMITS`, o un color de barra de navegación con alpha < 255. Cubre dispositivos OEM (ej. C2250) que aplican barra semitransparente en modo overlay sin activar el flag clásico translúcido.
		- **iOS**: `true` cuando no hay ninguna UIView opaca cubriendo el área de navegación (`navigationbarTransparent`) o cuando `safeAreaInsets.bottom` es cero (dispositivos con botón de inicio o landscape sin barra de gestos).
2. Se actualió forzó la salida de tipo boleeana de los métodos `isLocal()`, `isMobile()` y `isTouch()` en la librería JS.

## [v0.1.0] - 2026-04-25

Características:

1. Versión de lanzamiento.