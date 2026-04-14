> [!NOTE]
> For the English version of this document, see [README.md](./README.md).

> [!WARNING]
> Este documento aún no ha sido terminado y esta en una versión preliminar.

# wuijs-environment-lib

<div align="center">
	<img src="https://github.com/wui-js/wuijs-environment-lib/blob/main/imgs/logo/wuijs-environment-logotype-color.svg" width="220" height="220">
</div>

Versión librería: `0.1.0`

Versión documentación: `0.1.0.20260406.0`

Licencia: `Licencia Apache 2.0`

Autor: `Sergio E. Belmar V. <wuijs.project@gmail.com>`

## Índice

*   [Descripción General](#overview)
	*   [Acerca del Proyecto WUI JS](#project)
	*   [Mapa de Directorios](#dirmap)
*   [Implementación en Android](#android)
	*   [Constructor Java](#android-constructor)
	*   [Métodos Java](#android-methods)
	*   [Instalación y Configuración](#android-install)
		1.   [Clonar la librería](#android-clone)
		2.   [Configuración del Proyecto](#android-config-project)
		3.   [Configuración del Módulo](#android-config-module)
		4.   [Configuración del Manifest](#android-config-manifest)
		5.   [Configuración de Colores](#android-config-colors)
		6.   [Integración de la clase Java](#android-config-wui-environment-java)
		7.   [Integración de la calse JavaScript](#android-config-wui-environment-js)
		8.   [Inicialización](#android-config-mainactivity)
	*   [Uso en JavaScript para Android](#android-js-usage)
*   [Implementación en iOS](#ios)
	*   [Constructor Swift](#ios-constructor)
	*   [Métodos Swift](#ios-methods)
	*   [Instalación y Configuración](#ios-install)
		1.   [Clonar la librería](#ios-clone)
		2.   [Integración de la clase Swift](#ios-config-wui-environment-swift)
		3.   [Inicialización](#ios-config-viewcontroller)
*   [Implementación en Web](#web)
	*   [Métodos JavaScript](#web-methods)

<a name="overview"></a>

## Descripción General

WUI/JS Environment es un puente (bridge) entre entornos web y motores de renderizado web nativos mediante JavaScript, diseñado para facilitar la creación de aplicaciones híbridas. Proporciona acceso a instancias del hardware y al sistema de archivos directamente desde JavaScript.
Actualmente está disponible para Android en Java mediante WebView y para iOS en Swift mediante WebKit.

<a name="project"></a>

### Acerca del Proyecto WUI/JS

WUI/JS Lib es parte del proyecto WUI JS, que consta actualmente de 3 repositorios:

-	[https://github.com/wui-js/wuijs-main-lib](https://github.com/wui-js/wuijs-main-lib)<br>
	Librería UI principal.<br><br>
-	[https://github.com/wui-js/wuijs-plugins-lib](https://github.com/wui-js/wuijs-plugins-lib)<br>
	Librería de complementos UI.<br><br>
-	[https://github.com/wui-js/wuijs-environment-lib](https://github.com/wui-js/wuijs-environment-lib)<br>
	Librería puente entre entornos web y motores de renderizado web nativos mediante JavaScript.<br><br>
-	[https://github.com/wui-js/wuijs-lab](https://github.com/wui-js/wuijs-lab)<br>
	Repositorio con demos y ejemplos de uso tanto de las clases de la librería UI principal como de los complementos.<br><br>

<a name="dirmap"></a>

### Mapa de Directorios

La librería debe ser descargada desde el repositorio de GitHub [wui-is/wuijs-environment-lib](https://github.com/wui-is/wuijs-environment-lib). Esta librería cuenta con las 3 clases, Java para Android, Swift para iOS y JavaScript como contraparte Web para las dos anteriores.

La estructura de directorios del repositorio es:

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

| Ruta                                                              | Descripción |
| ----------------------------------------------------------------- | ----------- |
| [imgs](imgs/)                                                     | Imágenes utilizadas en la documentación. |
| [imgs/logo](imgs/logo/)                                           | Logotipo e isotipo del proyecto en formato SVG y PNG. |
| [src](src/)                                                       | Fuentes principales de la última versión. |
| [src/wui-js](src/wui-js)                                          | Directorio del proyecto WUI/JS. |
| [src/wui-js/environment/android](src/wui-js/environment/android/) | Librería WUI/JS Enviroment para Android. |
| [src/wui-js/environment/ios](src/wui-js/environment/ios/)         | Librería WUI/JS Enviroment para iOS. |
| [src/wui-js/environment/web](src/wui-js/environment/web/)         | Librería WUI/JS Enviroment para Web. |
| [src/wui-js/environment/demo](src/wui-js/environment/demo/)       | Directorio con interfaz de prueba para entornos Android e iOS. |

> [!NOTE]
> La librería `wuijs-environment-lib` opera conjuntamente, es decir, se debe implementar la combinación **Android + Web** o **iOS + Web** para su correcto funcionamiento.

<a name="android"></a>

## Implementación en Android

La implementación en Android utiliza como motor de renderización WebView.

<a name="android-constructor"></a>

### Constructor Java

| Constructor | Descripción |
| ----------- | ----------- |
| `WUIEnvironment(Context context[, boolean developMode])` | Inicializa el entorno WUI con configuración por defecto. `developMode = true` permite SSL con certificados no confiables y activa logs de depuración. |

<a name="android-methods"></a>

### Métodos Java

| Método                  | Tipo de retorno | Descripción |
| ----------------------- | --------------- | ----------- |
| `isAppInForeground`     | `boolean`       | `isAppInForeground()`<br><br>Verifica si la aplicación está actualmente en primer plano. |
| `getDeviceInfo`         | `JSONObject`    | `getDeviceInfo()`<br><br>Devuelve información de hardware del dispositivo: `id`, `uuid`, `name`, `platform`, `version`, `maker`, `model`. |
| `getDisplayInfo`        | `JSONObject`    | `getDisplayInfo()`<br><br>Devuelve métricas de pantalla: `width`, `height`, `density`, `densityDpi`, `orientation`, `refreshRate`, `aspectRatio`, `navigationMode`, `statusbarHeight`, `navigationbarHeight`, `notch` y flags de estilo de barras del sistema. |
| `getAppInfo`            | `JSONObject`    | `getAppInfo()`<br><br>Devuelve metadatos de la aplicación: `name`, `version`, `package`, `build`. |
| `getPermissionsStatus`  | `JSONObject`    | `getPermissionsStatus()`<br><br>Verifica el estado de los permisos del sistema: `phone`, `location`, `storage`, `contacts`, `camera`, `notifications`. Valores posibles: `granted`, `denied`, `default`. |
| `getCurrentPosition`    | `JSONObject`    | `getCurrentPosition()`<br><br>Obtiene las coordenadas actuales GPS/Red: `latitude`, `longitude`, `accuracy`, `provider`, `timestamp`. Solicita el permiso de ubicación si no ha sido otorgado. |
| `getConnectionStatus`   | `boolean`       | `getConnectionStatus()`<br><br>Verifica si hay una conexión a internet activa (WiFi, datos móviles o Ethernet). |
| `setStatusbarStyle`     | `void`          | `setStatusbarStyle(color, darkIcons)`<br><br>Argumentos:<br>**• color:** `String`, color HEX (`#RRGGBB`) o clave de `colors.xml` (`statusbarLightColor`, `statusbarDarkColor`, etc.).<br>**• darkIcons:** `boolean`, `true` para iconos oscuros, `false` para claros.<br><br>Actualiza el color y el estilo de iconos de la barra de estado. |
| `setNavigationbarStyle` | `void`          | `setNavigationbarStyle(color, darkIcons)`<br><br>Argumentos:<br>**• color:** `String`, color HEX (`#RRGGBB`) o clave de `colors.xml` (`navigationbarLightColor`, `navigationbarDarkColor`, etc.).<br>**• darkIcons:** `boolean`, `true` para iconos oscuros, `false` para claros.<br><br>Actualiza el color y el estilo de iconos de la barra de navegación. |
| `saveFile`              | `boolean`       | `saveFile(name, content)`<br><br>Argumentos:<br>**• name:** `String`, nombre del archivo.<br>**• content:** `String`, contenido a guardar.<br><br>Escribe un archivo en el almacenamiento interno privado de la aplicación. Devuelve `true` si tuvo éxito. |
| `readFile`              | `String`        | `readFile(name)`<br><br>Argumentos:<br>**• name:** `String`, nombre del archivo.<br><br>Lee un archivo del almacenamiento interno. Devuelve `null` si no existe o hay error. |
| `removeFile`            | `boolean`       | `removeFile(name)`<br><br>Argumentos:<br>**• name:** `String`, nombre del archivo.<br><br>Elimina un archivo del almacenamiento interno. Devuelve `true` si tuvo éxito. |
| `openAppSettings`       | `void`          | `openAppSettings()`<br><br>Abre la pantalla de configuración de la aplicación en Ajustes del sistema. |
| `openURL`               | `void`          | `openURL(url)`<br><br>Argumentos:<br>**• url:** `String`, URL de destino o ruta de asset local (`file:///android_asset/...`).<br><br>Carga un asset local en el WebView o abre una URL externa mediante el Intent del sistema. |
| `saveDeepLink`          | `void`          | `saveDeepLink(intent)`<br><br>Argumentos:<br>**• intent:** `Intent`, intent recibido en `onCreate` u `onNewIntent`.<br><br>Extrae y almacena la URL del Deep Link del intent. Si la página ya está cargada, la envía inmediatamente al JavaScript. |
| `sendDeepLink`          | `void`          | `sendDeepLink()`<br><br>Envía la URL de Deep Link almacenada al JavaScript llamando a `WUIEnvironment.response()`. Solo actúa si la página ya está cargada. |
| `readDeepLink`          | `String`        | `readDeepLink()`<br><br>Devuelve la última URL de Deep Link almacenada, o `null` si no hay ninguna. |
| `clearDeepLink`         | `void`          | `clearDeepLink()`<br><br>Elimina la URL de Deep Link almacenada. |

<a name="android-install"></a>

### Instalación y Configuración

<a name="android-clone"></a>

#### 1. Clonar la librería

Clonar el repositorio desde la cuenta oficial de wuiproject en GitHub:

```bash
git clone https://github.com/wui-is/wuijs-environment-lib.git
```

> [!NOTE]
> El repositorio contendrá las 3 clases, Java para Android, Swift para iOS y JavaScript como contraparte Web.

<a name="android-config-project"></a>

#### 2. Configuración del Proyecto

Asegúrese de que los repositorios estén correctamente definidos:

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

#### 3. Configuración del Módulo (`app/build.gradle.kts`)

Asegúrese de que `buildConfig` esté habilitado con el valor `true`:

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

#### 4. Configuración del Manifest en `AndroidManifest.xml`

**Agregar Permisos**

Asegúrese de que la aplicación tenga los permisos necesarios. El bridge requiere al menos Internet y Estado de Red.

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

**Configuración Deep Link**

Para habilitar el Deep Link en la aplicación, agregar a la sección `application.activity` la siguiente sentencia:

```xml
<intent-filter>
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />
    <data android:scheme="esquema_app" />
</intent-filter>
```

> [!IMPORTANT]
> Utiliza `android:scheme` para asignar el esquema de la aplicación. Esto permitirá acceder mediante una dirección URL en el navegador utilizando el esquema como protocolo.
> (ej: esquema_app://recurso_url)

**Configuración de intercambio de archivos**

Para habilitar el intercambio de archivos en la aplicación, agregar a la sección `application.activity` la siguiente sentencia:

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
> Esta configuración es requerida la implementación de descargas, cámara y compartir archivos por WhatsApp o Correo desde WebView.

<a name="android-config-colors"></a>

#### 5. Configuración de Colores en `res/values/colors.xml`

La librería utiliza estas llaves para el estilo de las barras de estado y navegación:

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
> Se deben definir todas las llaves para que la clase Java no arroje error.

<a name="android-config-wui-environment-java"></a>

#### 6. Integración de la clase Java `WUIEnvironment.java`

Copiar el archivo `src/wui-js/environment/android/WUIEnvironment.java` a la carpeta de fuentes de tu proyecto (ej: `app/src/main/java/your/package/name/` si el ID del paquete definido fuese `your.package.name`).

> [!IMPORTANT]
> Debes editar la primera línea del archivo para que coincida con el ID del paquete de la aplicación:

```java
package YOUR.PACKAGE.NAME; // Update this to match your project package
```

<a name="android-config-wui-environment-js"></a>

#### 7. Integración de la calse JavaScript `wui-environment-0.1.js`

Copia el contenido del directorio `src/web/` al directorio `assets/` del proyecto Android. Se recomienda utilizar la siguiente estructura:

- `app/src/main/assets/libraries/wui-js/environment/web/wui-environment-0.1.js`
- `app/src/main/assets/libraries/wui-js/environment/demo/index.html`

Esto asegurará que los ejemplos de inicialización funcionen correctamente.

<a name="android-config-mainactivity"></a>

#### 8. Inicialización en `MainActivity.java`

```java
public class MainActivity extends AppCompatActivity {
    private WUIEnvironment wuiEnvironment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            wuiEnvironment = new WUIEnvironment(this);
            // Carga página demo
			// Comenta la siguiente línea después de validar la prueba
            wuiEnvironment.openURL("file:///android_asset/libreries/wui-js/environment/demo/index.html");
            // Carga página inicial
			// Descomenta la siguiente línea después de validar la prueba
            //wuiEnvironment.openURL("file:///android_asset/pages/index.html");
            // Habilitar peticiones Deep Link durante la apertura de la app
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
            // Habilitar peticiones Deep Link durante la ejecución de la app
            wuiEnvironment.saveDeepLink(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
```

<a name="android-js-usage"></a>

### Uso en JavaScript para Android

El modo nativo de manejo de funciones de sistema es mediante el objeto global `Android` proporcionado por el WebView. Pro medio de este objeto se puede llamar a cualquier función del bridge usando `Android.request()`:

```javascript
// Ejemplo: Obtener información de pantalla
const display = JSON.parse(Android.request(JSON.stringify({ func: "getDisplayInfo" })));
console.log("Modo de navegación:", display.navigationMode);
```

No obstante, para manejo de eventos enviados desde Java se requiere el objeto global `WUIEnvironment` proporcionado por la librería JavaScript mediante el método público `response()`:

```javascript
// Manejo de eventos enviados desde Java
WUIEnvironment.response = function(args) {
    if (args.event == "onReceiveDeepLink") {
        console.log("Deep Link recibido:", args.url);
    }
};
```

<a name="ios"></a>

## Implementación en iOS

La implementación en iOS utiliza como motor de renderización WebKit (WKWebView) y se comunica mediante `WKScriptMessageHandler`.

<a name="ios-constructor"></a>

### Constructor Swift

| Constructor | Descripción |
| ----------- | ----------- |
| `WUIEnvironment(viewController: UIViewController[, developMode: Bool])` | Inicializa el entorno WUI. `developMode = true` permite SSL con certificados no confiables y activa logs de depuración. |

<a name="ios-methods"></a>

### Métodos Swift

| Método                  | Tipo de retorno | Descripción |
| ----------------------- | --------------- | ----------- |
| `isAppInForeground`     | `Bool`          | `isAppInForeground()`<br><br>Verifica si la aplicación está actualmente en primer plano. |
| `getDeviceInfo`         | `[String: Any]` | `getDeviceInfo()`<br><br>Devuelve información de hardware del dispositivo: `id`, `uuid`, `name`, `platform`, `version`, `maker`, `model`. |
| `getDisplayInfo`        | `[String: Any]` | `getDisplayInfo()`<br><br>Devuelve métricas de pantalla: `width`, `height`, `density`, `densityDpi`, `orientation`, `refreshRate`, `aspectRatio`, `navigationMode`, `statusbarHeight`, `navigationbarHeight`, `notch` y flags de estilo de barras del sistema. |
| `getAppInfo`            | `[String: Any]` | `getAppInfo()`<br><br>Devuelve metadatos de la aplicación: `name`, `version`, `package`, `build`. |
| `getPermissionsStatus`  | `void`          | `getPermissionsStatus(completion)`<br><br>Argumentos:<br>**• completion:** `([String: Any]) -> Void`, callback con el resultado.<br><br>Verifica el estado de los permisos del sistema: `location`, `camera`, `contacts`, `notifications`. Valores posibles: `granted`, `denied`, `default`, `undefined`. Async — entrega el resultado en el main thread. |
| `getCurrentPosition`    | `void`          | `getCurrentPosition(completion)`<br><br>Argumentos:<br>**• completion:** `([String: Any]) -> Void`, callback con el resultado.<br><br>Obtiene las coordenadas GPS actuales: `latitude`, `longitude`, `accuracy`, `provider`, `timestamp`. Solicita el permiso de ubicación si no ha sido otorgado. Async — entrega el resultado vía `CLLocationManagerDelegate`. |
| `getConnectionStatus`   | `Bool`          | `getConnectionStatus()`<br><br>Verifica si hay una conexión a internet activa mediante `NWPathMonitor`. |
| `setStatusbarStyle`     | `void`          | `setStatusbarStyle(color, darkIcons)`<br><br>Argumentos:<br>**• color:** `String`, color HEX (`#RRGGBB`) o nombre de color del asset catalog.<br>**• darkIcons:** `Bool`, `true` para iconos oscuros, `false` para claros.<br><br>Coloca una UIView con el color indicado sobre el área del status bar. El estilo de iconos se aplica vía `preferredStatusBarStyle` — el host ViewController debe exponer esta propiedad y llamar a `setNeedsStatusBarAppearanceUpdate()`. |
| `setNavigationbarStyle` | `void`          | `setNavigationbarStyle(color, darkIcons)`<br><br>Argumentos:<br>**• color:** `String`, color HEX (`#RRGGBB`) o nombre de color del asset catalog.<br>**• darkIcons:** `Bool`, ignorado en iOS (el home indicator no es configurable).<br><br>Coloca una UIView con el color indicado sobre el área `safeAreaInsets.bottom`. Sin efecto en dispositivos con botón de inicio. |
| `saveFile`              | `Bool`          | `saveFile(name, content)`<br><br>Argumentos:<br>**• name:** `String`, nombre del archivo.<br>**• content:** `String`, contenido a guardar.<br><br>Escribe un archivo en el directorio `Documents` de la aplicación. Devuelve `true` si tuvo éxito. |
| `readFile`              | `String?`       | `readFile(name)`<br><br>Argumentos:<br>**• name:** `String`, nombre del archivo.<br><br>Lee un archivo del directorio `Documents`. Devuelve `nil` si no existe o hay error. |
| `removeFile`            | `Bool`          | `removeFile(name)`<br><br>Argumentos:<br>**• name:** `String`, nombre del archivo.<br><br>Elimina un archivo del directorio `Documents`. Devuelve `true` si tuvo éxito. |
| `openAppSettings`       | `void`          | `openAppSettings()`<br><br>Abre la pantalla de configuración de la aplicación en Ajustes del sistema. |
| `openURL`               | `void`          | `openURL(url)`<br><br>Argumentos:<br>**• url:** `String`, URL de destino o ruta de archivo local (`file://...`).<br><br>Carga un archivo local en el WKWebView con `loadFileURL` o abre una URL externa mediante `UIApplication.shared.open`. |
| `saveDeepLink`          | `void`          | `saveDeepLink(url)`<br><br>Argumentos:<br>**• url:** `URL?`, URL recibida en `scene(_:openURLContexts:)` o `application(_:open:)`.<br><br>Extrae y almacena la URL del Deep Link. Si la página ya está cargada, la envía inmediatamente al JavaScript. |
| `sendDeepLink`          | `void`          | `sendDeepLink()`<br><br>Envía la URL de Deep Link almacenada al JavaScript llamando a `WUIEnvironment.response()`. Solo actúa si la página ya está cargada. |
| `readDeepLink`          | `String?`       | `readDeepLink()`<br><br>Devuelve la última URL de Deep Link almacenada, o `nil` si no hay ninguna. |
| `clearDeepLink`         | `void`          | `clearDeepLink()`<br><br>Elimina la URL de Deep Link almacenada. |

<a name="ios-install"></a>

### Instalación y Configuración

<a name="ios-clone"></a>

#### 1. Clonar la librería

Si aún no lo has hecho, clona el repositorio desde GitHub:

```bash
git clone https://github.com/wui-is/wuijs-environment-lib.git
```

<a name="ios-config-wui-environment-swift"></a>

#### 2. Integración de la clase Swift `WUIEnvironment.swift`

Copia el archivo `src/wui-js/ios/WUIEnvironment.swift` en tu proyecto Xcode.

<a name="ios-config-viewcontroller">

#### 3. Inicialización en tu ViewController `ContentView.swift`

```swift
class MyViewController: UIViewController {
    private var wuiEnvironment: WUIEnvironment?

    override var preferredStatusBarStyle: UIStatusBarStyle {
        return wuiEnvironment?.preferredStatusBarStyle ?? .default
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        wuiEnvironment = WUIEnvironment(viewController: self)
        // Carga página demo
        // Comenta la siguiente línea después de validar la prueba
        wuiEnvironment?.openURL(url: "file:///\(Bundle.main.bundlePath)/assets/wui-js/environment/demo/index.html")
        // Carga página inicial
        // Descomenta la siguiente línea después de validar la prueba
        // wuiEnvironment?.openURL(url: "file:///\(Bundle.main.bundlePath)/assets/pages/index.html")
    }

    func scene(_ scene: UIScene, openURLContexts contexts: Set<UIOpenURLContext>) {
        // Habilitar peticiones Deep Link durante la ejecución de la app
        wuiEnvironment?.saveDeepLink(url: contexts.first?.url)
    }
}
```

<a name="web"></a>

## Implementación en Web

<a name="web-methods"></a>

### Métodos JavaScript de la Clase

| Método    | Tipo de retorno | Descripción |
| --------- |-----------------| ----------- |
| `onReady` | `void`          | `onReady(done)`<br><br>Argumentos:<br>**• done:** `function`, callback que recibe el número total de solicitudes realizadas.<br><br>Ejecuta el callback cuando todas las solicitudes pendientes han recibido respuesta. Útil para sincronizar la carga inicial de datos antes de renderizar la UI. |

### Métodos JavaScript de la Instancia

| Método                  | Tipo de retorno           | Descripción |
| ----------------------- |---------------------------| ----------- |
| `isAppInForeground`     | `Promise<boolean>`        | `isAppInForeground(done)`<br><br>Verifica si la aplicación está en primer plano. |
| `getDeviceInfo`         | `Promise<Object>`         | `getDeviceInfo(done)`<br><br>Obtiene información del hardware (UUID, modelo, plataforma, etc.). |
| `getDisplayInfo`        | `Promise<Object>`         | `getDisplayInfo(done)`<br><br>Obtiene métricas de pantalla y modo de navegación. |
| `getAppInfo`            | `Promise<Object>`         | `getAppInfo(done)`<br><br>Obtiene metadatos de la aplicación. |
| `getPermissionsStatus`  | `Promise<Object>`         | `getPermissionsStatus(done)`<br><br>Consulta el estado de los permisos del sistema. |
| `getCurrentPosition`    | `Promise<Object>`         | `getCurrentPosition(done)`<br><br>Obtiene la ubicación GPS actual. |
| `getConnectionStatus`   | `Promise<boolean>`        | `getConnectionStatus(done)`<br><br>Verifica si hay conexión a internet activa. |
| `setStatusbarStyle`     | `void`                    | `setStatusbarStyle(color, darkIcons, done)`<br><br>Argumentos:<br>**• color:** `string`, color en formato HEX o ID de recurso.<br>**• darkIcons:** `boolean`, iconos oscuros.<br>**• done:** `function`, callback opcional.<br><br>Cambia el color y estilo de la barra de estado. |
| `setNavigationbarStyle` | `void`                    | `setNavigationbarStyle(color, darkIcons, done)`<br><br>Argumentos:<br>**• color:** `string`, color en formato HEX o ID de recurso.<br>**• darkIcons:** `boolean`, iconos oscuros.<br>**• done:** `function`, callback opcional.<br><br>Cambia el color y estilo de la barra de navegación. |
| `saveFile`              | `Promise<boolean>`        | `saveFile(name, content, done)`<br><br>Argumentos:<br>**• name:** `string`, nombre del archivo.<br>**• content:** `string|Object`, contenido.<br>**• done:** `function`, callback opcional.<br><br>Guarda un archivo en el almacenamiento local. |
| `readFile`              | `Promise<string\|Object>` | `readFile(name, done)`<br><br>Argumentos:<br>**• name:** `string`, nombre del archivo.<br>**• done:** `function`, callback opcional.<br><br>Lee un archivo del almacenamiento local. |
| `removeFile`            | `Promise<boolean>`        | `removeFile(name, done)`<br><br>Argumentos:<br>**• name:** `string`, nombre del archivo.<br>**• done:** `function`, callback opcional.<br><br>Elimina un archivo del almacenamiento. |
| `openAppSettings`       | `void`                    | `openAppSettings(done)`<br><br>Argumentos:<br>**• done:** `function`, callback opcional.<br><br>Abre la pantalla de configuración de la aplicación. |
| `openURL`               | `void`                    | `openURL(url)`<br><br>Argumentos:<br>**• url:** `string`, la URL de destino o ruta de asset local.<br><br>Abre un recurso local en el WebView o un enlace externo. |
| `readDeepLink`          | `Promise<string>`         | `readDeepLink(done)`<br><br>Argumentos:<br>**• done:** `function`, callback opcional.<br><br>Lee la última URL de Deep Link recibida. |
| `clearDeepLink`         | `void`                    | `clearDeepLink(done)`<br><br>Argumentos:<br>**• done:** `function`, callback opcional.<br><br>Limpia la URL de Deep Link almacenada. |
