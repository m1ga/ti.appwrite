# Appcelerator Titanium module for Appwrite

<img src="images/screenshot.png" alt="screenshot"/>

Appcelerator Titanium Android module using [Appwrite's Android SDK](https://github.com/appwrite/sdk-for-android)

## What is Appwrite:

> Appwrite is an open-source backend as a service server that abstract and simplify complex and repetitive development tasks behind a very simple to use REST API. Appwrite aims to help you develop your apps faster and in a more secure way. Use the Android SDK to integrate your app with the Appwrite server to easily start interacting with all of Appwrite backend APIs and tools. For full API documentation and tutorials go to https://appwrite.io/docs


## Installation

* create and run an Appwrite docker machine: https://appwrite.io/docs/installation
* download `ti.appwrite` module
* add `<module>ti.appwrite</module>` to your `tiapp.xml`
* add the following section to your `<application>` section:
```xml
<application>
	<activity android:name="io.appwrite.views.CallbackActivity" android:exported="true">
		<intent-filter android:label="android_web_auth">
			<action android:name="android.intent.action.VIEW"/>
			<category android:name="android.intent.category.DEFAULT"/>
			<category android:name="android.intent.category.BROWSABLE"/>
			<data android:scheme="appwrite-callback-[PROJECT_ID]"/>
		</intent-filter>
	</activity>
</application>
```

## Appwrite setup

Start the docker machine and create a new project. Make sure to add an `Android platform` with your package name. Under `Settings` you will find the `Project ID`.

## Titanium setup

```js
var appwrite = require("ti.appwrite");
var SERVER_URL = "http://192.168.0.10/v1"
var PROJECT_ID = "12345";
var DATABASE_ID = "12345";

var btns = [
	["connect", onClickConnect],
	["create account", onClickCreate],
	["verify mail", onClickVerify],
	["delete account", onClickDelete],
	["login", onClickLogin],
	["get account", onClickGetAccount],
	["get documents", onClickGetDocuments],
	["subscribe", onClickSub],
	["unsubscribe", onClickUnsub],
	["create file", onClickCreateFile],
	["list file", onClickListFiles]
];

var win = Ti.UI.createWindow({
	backgroundColor: '#fff',
	layout: 'vertical'
});
var log = Ti.UI.createTextArea({
	width: Ti.UI.FILL,
	height: 100,
	borderColor: 'red',
	borderWidth: 1,
	color: "#fff",
	backgroundColor: "#333",
	font: {
		fontSize: 14
	}
});
win.add(log);

var btnArray = [];

_.each(btns, function(btn) {
	var button = Ti.UI.createButton({
		title: btn[0],
		width: 200
	});
	button.addEventListener("click", btn[1]);
	btnArray.push(button);
})
win.add(btnArray);
btnArray[0].backgroundColor = "#f44";

appwrite.addEventListener("realtimeEvent", function(e) {
	console.log("event: " + e.action);
})

appwrite.addEventListener("error", function(e) {
	console.error("error: " + e.action);
	console.error(e);
	log.value += e.action + " " + e.message + "\n";
})

appwrite.addEventListener("documents", function(e) {
	console.log("---documents---");
	console.log("data: " + e.data);
	console.log("");
})
appwrite.addEventListener("storage", function(e) {
	console.log("---storage---");
	console.log("data: " + e.data);
	console.log("");
})

appwrite.addEventListener("account", function(e) {
	console.log("---account---");
	console.log("Action:", e.action);
	console.log(e);
	console.log("");
})
appwrite.addEventListener("connected", function(e) {
	btnArray[0].backgroundColor = "#4f4";
})

function onClickSub(e) {
	appwrite.subscribe(["files", "account"]);
}


function onClickLogin(e) {
	appwrite.login({
		email: "mail@mail.com",
		password: "password"
	});

}

function onClickCreate(e) {
	appwrite.createAccount({
		email: "mail@mail.com",
		password: "password"
	});

}

function onClickDelete(e) {
	appwrite.deleteAccount();
}

function onClickUnsub(e) {
	appwrite.unsubscribe(["files", "account"]);
}

function onClickConnect(e) {
	appwrite.create({
		endpoint: SERVER_URL,
		project: PROJECT_ID,
		selfSigned: true,
		channels: ["files", "account"]
	});

	// appwrite.endpoint = SERVER_URL;
	// appwrite.project = PROJECT_ID;
	// appwrite.selfSigned = true;
}

function onClickGetDocuments(e) {
	appwrite.getDocuments(DATABASE_ID);
}

function onClickGetAccount(e) {
	appwrite.getAccount();
}

function onClickVerify(e) {
	appwrite.verifyMail(SERVER_URL);
}


function onClickCreateFile(e) {
	var file = Ti.Filesystem.getFile(Ti.Filesystem.externalStorageDirectory, "appicon.png");

	if (!file.exists()) {
		// copy it from /assets/ to the external storage
		var source = Ti.Filesystem.getFile(Ti.Filesystem.resourcesDirectory, "appicon.png");
		file.write(source.read());
	}

	if (file.exists()) {
		appwrite.createFile({
			file: file,
			read: ["*"],
			write: ["*"]
		});
	}
}

function onClickListFiles(e) {
	appwrite.listFiles();
}

win.open();
```

## Methods

* create()
* createAccount()
* deleteAccount();
* login()
* getAccount()
* getDocuments()
* subscribe([])
* unsubscribe([])
* createFile({file, read[], write[]})
* listFiles();

## Events
* account
* error: action, message, code, response
* realtimeEvent
* documents
* storage
* connected

## Author

* Michael Gangolf (<a href="https://github.com/m1ga">@MichaelGangolf</a> / <a href="https://www.migaweb.de">Web</a>)

<span class="badge-buymeacoffee"><a href="https://www.buymeacoffee.com/miga" title="donate"><img src="https://img.shields.io/badge/buy%20me%20a%20coke-donate-orange.svg" alt="Buy Me A Coke donate button" /></a></span>
