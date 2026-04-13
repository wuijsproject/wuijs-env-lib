/*
 * @file wui-environment-0.1.js
 * @class WUIEnvironment
 * @version 0.1
 * @author Sergio E. Belmar V. (wuijs.project@gmail.com)
 * @copyright Sergio E. Belmar V. (wuijs.project@gmail.com)
 */

class WUIEnvironment {

	static version = "0.1";
	static #instance = null;
	static #defaults = {
		localStorageEnabled: true,
		onDownloadFile: null,
		onReceiveDeepLink: null
	};

	#checkInterval = 100;
	#reqCount = 0;
	#resCount = 0;
	#responses = {};

	static response(args) {
		const instance = WUIEnvironment.#instance;
		const event = args.event || "";
		delete args.event;
		if (instance != null) {
			switch (event) {
				case "onDownloadFile":
					if (typeof (instance.onDownloadFile) == "function") {
						instance.onDownloadFile(args);
					}
					break;
				case "onReceiveDeepLink":
					if (typeof (instance.onReceiveDeepLink) == "function") {
						instance.onReceiveDeepLink(args.url || "");
					}
					break;
			}
		}
	}

	constructor(properties) {
		this.userAgent = navigator.userAgent;
		this.platform = navigator.userAgentData?.platform || navigator.platform;
		this.systemName =
			this.platform.match(/iphone|ipad/i) ? ("iOS"
			) : this.platform.match(/android/i) ? ("Android"
			) : this.platform.match(/mac/i) ? ("macOS"
			) : this.platform.match(/linux/i) ? ("Linux"
			) : this.platform.match(/windows phone|windows mobile/i) ? ("Windows Phone"
			) : this.platform.match(/win/i) ? ("Windows"
			) : "";
		this.environment =
			this.userAgent.match(/WUIEnvironment/i) && this.userAgent.match(/android/i) && typeof (Android) != "undefined" ? ("wui.android"
			) : this.userAgent.match(/WUIEnvironment/i) && this.userAgent.match(/iphone|ipad/i) && typeof (webkit) != "undefined" && typeof (webkit.messageHandlers) != "undefined" ? ("wui.ios"
			) : ("web");
		this.isLocalEnv = this.environment.match(/wui/i);
		Object.keys(WUIEnvironment.#defaults).forEach(prop => {
			this[prop] = typeof (properties) != "undefined" && prop in properties ? properties[prop] : prop in WUIEnvironment.#defaults ? WUIEnvironment.#defaults[prop] : null;
		});
		WUIEnvironment.#instance = this;
	}

	get localStorageEnabled() {
		return this._localStorageEnabled;
	}

	get onDownloadFile() {
		return this._onDownloadFile;
	}

	get onReceiveDeepLink() {
		return this._onReceiveDeepLink;
	}

	set localStorageEnabled(value) {
		if (typeof (value) == "boolean") {
			this._localStorageEnabled = value;
		}
	}

	set onDownloadFile(value) {
		if (typeof (value) == "function") {
			this._onDownloadFile = value;
		}
	}

	set onReceiveDeepLink(value) {
		if (typeof (value) == "function") {
			this._onReceiveDeepLink = value;
		}
	}

	#request(options) {
		this.#reqCount++;
		return new Promise((resolve) => {
			if (this.environment == "wui.android") {
				const response = Android.request(JSON.stringify(options));
				if (options.func.match(/^(getDeviceInfo|getDisplayInfo|getAppInfo|getPermissionsStatus|getCurrentPosition)$/) || (options.func == "readFile" && options.name.match(/\.json$/i))) {
					resolve(JSON.parse(response || "{}"));
				} else {
					resolve(response);
				}
				this.#resCount++;
			} else if (this.environment == "wui.ios") {
				const code = this.#reqCount;
				this.#responses[code] = "";
				options.code = code;
				webkit.messageHandlers.request.postMessage(options);
				const check = setInterval(() => {
					if (Object.keys(this.#responses[code]).length > 0) {
						clearInterval(check);
						resolve(response);
						delete this.#responses[code];
						this.#resCount++;
					}
				}, this.#checkInterval);
			}
		});
	}

	onReady(done) {
		const check = setInterval(() => {
			if (this.#reqCount == this.#resCount) {
				clearInterval(check);
				if (typeof (done) == "function") {
					done(this.#reqCount);
				}
			}
		}, this.#checkInterval);
	}

	isAppInForeground(done) {
		if (this.isLocalEnv) {
			return this.#request({ func: "isAppInForeground" }).then(done);
		}
		return null;
	}

	getDeviceInfo(done) {
		if (this.isLocalEnv) {
			return this.#request({ func: "getDeviceInfo" }).then(done);
		}
		return { platform: this.systemName };
	}

	getDisplayInfo(done) {
		if (this.isLocalEnv) {
			return this.#request({ func: "getDisplayInfo" }).then(done);
		}
		return {
			width: screen.width,
			height: screen.height,
			notch: false
		};
	}

	getAppInfo(done) {
		if (this.isLocalEnv) {
			return this.#request({ func: "getAppInfo" }).then(done);
		}
		return null;
	}

	getPermissionsStatus(done) {
		if (this.isLocalEnv) {
			return this.#request({ func: "getPermissionsStatus" }).then(done);
		} else if (navigator.permissions) {
			const $this = this;
			return (async () => {
				const permissions = {
					location: "undefined",
					storage: "undefined",
					contacts: "undefined",
					camera: "undefined",
					notifications: "undefined"
				};
				try {
					const location = await navigator.permissions.query({ name: "geolocation" });
					const storage = !$this.userAgent.match(/Safari/i) ? await navigator.permissions.query({ name: "storage-access" }) : { state: "undefined" };
					const camera = await navigator.permissions.query({ name: "camera" });
					const notifications = await navigator.permissions.query({ name: "notifications" });
					permissions.location = location.state;
					permissions.storage = storage.state;
					permissions.camera = camera.state;
					permissions.notifications = notifications.state;
				} catch (error) {
					console.error("Failed to querying permissions:", error);
				}
				return permissions;
			})().then(done);
		} else {
			return null;
		}
	}

	getCurrentPosition(done) {
		if (this.isLocalEnv) {
			return this.#request({ func: "getCurrentPosition" }).then(done);
		} else if (navigator.geolocation) {
			return new Promise((resolve) => {
				navigator.geolocation.getCurrentPosition((location) => {
					const coords = location.coords;
					const position = {
						latitude: coords.latitude,
						longitude: coords.longitude,
						accuracy: coords.accuracy
					};
					resolve(position);
					if (typeof (done) == "function") {
						done(position);
					}
				}, (error) => {
					console.error("Failed to get current position:", error);
				}, {
					enableHighAccuracy: true,
					timeout: 10000,
					maximumAge: 0
				});
			});
		}
		return null;
	}

	getConnectionStatus(done) {
		if (this.isLocalEnv) {
			return this.#request({ func: "getConnectionStatus" }).then(done);
		} else if (typeof (navigator.onLine) != "undefined") {
			if (typeof (done) == "function") {
				done(navigator.onLine);
			}
			return navigator.onLine;
		}
		return null;
	}

	setStatusbarStyle(color, darkIcons, done) {
		if (this.isLocalEnv) {
			this.#request({ func: "setStatusbarStyle", color: color, darkIcons: darkIcons }).then(done);
		}
	}

	setNavigationbarStyle(color, darkIcons, done) {
		if (this.isLocalEnv) {
			this.#request({ func: "setNavigationbarStyle", color: color, darkIcons: darkIcons }).then(done);
		}
	}

	saveFile(name, content, done) {
		if (name.match(/\.json$/i) && typeof (content) == "object") {
			content = JSON.stringify(content);
		}
		if (this.isLocalEnv) {
			return this.#request({ func: "saveFile", name: name, content: content }).then(done);
		} else if (this._localStorageEnabled && window.localStorage) {
			window.localStorage.setItem(name, content);
			if (typeof (done) == "function") {
				done(true);
			}
			return true;
		}
		return false;
	}

	readFile(name, done) {
		if (this.isLocalEnv) {
			return this.#request({ func: "readFile", name: name }).then(done);
		} else if (this._localStorageEnabled && window.localStorage) {
			let content = window.localStorage.getItem(name) || "";
			if (name.match(/\.json$/i)) {
				try {
					content = JSON.parse(content || "{}");
				} catch (error) {
					console.error("Failed to parse content json storage:", error);
				}
			}
			if (typeof (done) == "function") {
				done(content);
			}
			return content;
		}
		return null;
	}

	removeFile(name, done) {
		if (this.isLocalEnv) {
			return this.#request({ func: "removeFile", name: name }).then(done);
		} else if (window.localStorage) {
			window.localStorage.removeItem(name);
			if (typeof (done) == "function") {
				done(true);
			}
			return true;
		}
		return false;
	}

	openAppSettings(done) {
		if (this.isLocalEnv) {
			this.#request({ func: "openAppSettings" }).then(done);
		}
	}

	openURL(url) {
		if (this.isLocalEnv) {
			this.#request({ func: "openURL", url: url });
		} else if (this.systemName.match(/(Android|iOS)/)) {
			const link = document.createElement("a");
			link.setAttribute("href", url);
			link.style.display = "none";
			document.body.appendChild(link);
			link.click();
			document.body.removeChild(link);
		} else {
			window.open(url, "_blank");
		}
	}

	readDeepLink(done) {
		if (this.isLocalEnv) {
			return this.#request({ func: "readDeepLink" }).then(done);
		}
		return null;
	}

	clearDeepLink(done) {
		if (this.isLocalEnv) {
			this.#request({ func: "clearDeepLink" }).then(done);
		}
	}
}