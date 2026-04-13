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
	*   [Instalación y Configuración](#android-install)
		1.   [Clonar la librería](#android-clone)
		2.   [Configuración del Proyecto](#android-config-project)
		3.   [Configuración del Módulo](#android-config-module)
		4.   [Configuración del Manifest](#android-config-manifest)
		5.   [Configuración de Colores](#android-config-colors)
		6.   [Integración de la clase Java](#android-config-java)
		7.   [Integración de la calse JavaScript](#android-config-js)
		8.   [Inicialización](#android-config-mainactivityactivity)
	*   [Métodos Java](#android-methods)
	*   [Uso en JavaScript para Android](#android-js-usage)
*   [Implementación en iOS](#ios)
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
	Complementos de la librería UI principal.<br><br>
-	[https://github.com/wui-js/wuijs-environment-lib](https://github.com/wui-js/wuijs-environment-lib)<br>
	Puente entre entornos web y motores de renderizado web nativos mediante JavaScript.<br><br>
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
        ├── android/
        ├── ios/
        └── environment/
            └── test/
```

| Ruta                                                 | Descripción |
| ---------------------------------------------------- | ----------- |
| [imgs](imgs/)                                        | Imágenes utilizadas en la documentación. |
| [imgs/logo](imgs/logo/)                              | Logotipo e isotipo del proyecto en formato SVG y PNG. |
| [src](src/)                                          | Fuentes principales de la última versión. |
| [src/wui-js](src/wui-js)                             | Directorio del proyecto WUI/JS. |
| [src/wui-js/android](src/wui-js/android/)                   | Librería WUI/JS Enviroment para Android. |
| [src/wui-js/ios](src/ios/)                           | Librería WUI/JS Enviroment para iOS. |
| [src/wui-js/environment](src/environment/)           | Librería WUI/JS Enviroment para Web. |
| [src/wui-js/environment/test](src/environment/test/) | Directorio con interfaz de prueba de la librería WUI/JS Enviroment. |

> [!NOTE]
> La librería `wuijs-environment-lib` opera conjuntamente, es decir, se debe implementar la combinación **Android + Web** o **iOS + Web** para su correcto funcionamiento.

<a name="android"></a>

## Implementación en Android

La implementación en Android utiliza como motor de renderización WebView.

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

<a name="android-config-java"></a>

#### 6. Integración de la clase Java `WUIEnvironment.java`

Copiar el archivo `src/wui-js/android/WUIEnvironment.java` a la carpeta de fuentes de tu proyecto (ej: `app/src/main/java/com/tu/paquete/` si el ID del paquete definido fuese `com.tu.paquete`).

> [!IMPORTANT]
> Debes editar la primera línea del archivo para que coincida con el ID del paquete de la aplicación:

```java
package com.tu.paquete; // Cambia esto por el ID de paquete de tu proyecto
```

<a name="android-config-js"></a>

#### 7. Integración de la calse JavaScript `wui-environment-0.1.js`

Copia el contenido del directorio `src/web/` al directorio `assets/` del proyecto Android. Se recomienda utilizar la siguiente estructura:

- `app/src/main/assets/libraries/wui-js/environment/wui-environment-0.1.js`
- `app/src/main/assets/libraries/wui-js/environment/test/test.html`

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
            // Carga página test
			// Comenta la siguiente línea después de validar la prueba
            wuiEnvironment.openURL("file:///android_asset/libreries/wuienv/test/test.html");
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

<a name="android-methods"></a>

### Métodos Java

| Método                  | Tipo de retorno | Descripción |
| ----------------------- | --------------- | ----------- |
| `getDeviceInfo`         | `JSONObject`    | `getDeviceInfo()`<br><br>Devuelve un objeto JSON con detalles de hardware, incluyendo UUID, modelo, fabricante y versión del SO. |
| `getDisplayInfo`        | `JSONObject`    | `getDisplayInfo()`<br><br>Devuelve métricas de pantalla, incluyendo densidad, tasa de refresco, relación de aspecto, insets del área segura y modo de navegación (Gestos/Botones). |
| `getAppInfo`            | `JSONObject`    | `getAppInfo()`<br><br>Devuelve metadatos de la aplicación como nombre, paquete, versión y código de compilación. |
| `getPermissionsStatus`  | `JSONObject`    | `getPermissionsStatus()`<br><br>Verifica y devuelve el estado de varios permisos del sistema (Cámara, Ubicación, Almacenamiento, etc.). |
| `getCurrentPosition`    | `void`          | `getCurrentPosition()`<br><br>Obtiene las coordenadas actuales GPS/Red y las envía de vuelta al WebView a través del evento `onReceiveCurrentPosition`. |
| `getConnectionStatus`   | `JSONObject`    | `getConnectionStatus()`<br><br>Verifica si hay una conexión a internet activa y devuelve su tipo (WiFi, Móvil, etc.). |
| `setStatusbarStyle`     | `void`          | `setStatusbarStyle(colorId, darkIcons)`<br><br>Argumentos:<br>**• colorId:** `string`, ID de recurso de colors.xml<br>**• darkIcons:** `boolean`, true para iconos oscuros, false para claros.<br><br>Actualiza el estilo de la barra de estado del sistema. |
| `setNavigationbarStyle` | `void`          | `setNavigationbarStyle(colorId, darkIcons)`<br><br>Argumentos:<br>**• colorId:** `string`, ID de recurso de colors.xml<br>**• darkIcons:** `boolean`, true para iconos oscuros, false para claros.<br><br>Actualiza el estilo de la barra de navegación del sistema. |
| `saveFile`              | `boolean`       | `saveFile(name, content)`<br><br>Argumentos:<br>**• name:** `string`, nombre del archivo.<br>**• content:** `string`, contenido del archivo.<br><br>Escribe datos en el almacenamiento interno de la aplicación. |
| `readFile`              | `string`        | `readFile(name)`<br><br>Argumentos:<br>**• name:** `string`, nombre del archivo.<br>**• content:** `string`, contenido del archivo.<br><br>Lee datos del almacenamiento interno de la aplicación. |
| `openURL`               | `void`          | `openURL(url)`<br><br>Argumentos:<br>**• url:** `string`, la URL de destino o ruta de asset local.<br>**• url:** `string`, la URL de destino o ruta de asset local.<br><br>Abre un asset local o una URL externa en el WebView. |

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

La implementación en iOS utiliza como motor de renderización WebKit (WKWebView).

### Instalación y Configuración

#### 1. Clonar la librería

Si aún no lo has hecho, clona el repositorio desde GitHub:

```bash
git clone https://github.com/wui-is/wuijs-environment-lib.git
```

> [!NOTE]
> El repositorio contendrá las 3 clases, Java para Android, Swift para iOS y JavaScript como contraparte Web.

#### 2. Integración de la clase Swift `WUIEnvironment.swift`

Copiar el archivo `src/ios/WUIEnvironment.swift` al proyecto en Xcode.

#### 3. Integración de la calse JavaScript `wui-environment-0.1.js`

Copia el contenido del directorio `src/web/` al directorio `assets/` del proyecto Android. Se recomienda utilizar la siguiente estructura:

- `app/src/main/assets/libraries/wui-js/environment/wui-environment-0.1.js`
- `app/src/main/assets/libraries/wui-js/environment/test/test.html`

Esto asegurará que los ejemplos de inicialización funcionen correctamente.

<a name="web"></a>

## Implementación en Web

<a name="web-methods"></a>

### Métodos JavaScript

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
