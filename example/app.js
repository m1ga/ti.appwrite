const appwrite = require("ti.appwrite");
const SERVER_URL = "http://192.168.0.1:80/v1";
const PROJECT_ID = "61684fb485ee5";
const DATABASE_ID = "616c139208ffd";
var fileId = "";
var documentId = "";
var sessionId = "";
var USER_MAIL = "mail@mail.com";
var USER_PASSWORD = "1234password";

var btns = [
	["connect", onClickConnect],
	["-"],
	["create account", onClickCreate],
	["verify mail", onClickVerify],
	["delete account", onClickDelete],
	["create session", onClickLogin],
	["delete session", onClickDeleteSession],
	["get account", onClickGetAccount],
	["-"],
	["get documents", onClickGetDocuments],
	["get document", onClickGetDocument],
	["create document", onClickCreateDocument],
	["delete document", onClickDeleteDocument],
	["-"],
	["subscribe", onClickSub],
	["unsubscribe", onClickUnsub],
	["-"],
	["create file", onClickCreateFile],
	["list files", onClickListFiles],
	["get file", onClickGetFile],
	["get preview", onClickGetPreview],
	["download file", onClickDownloadFile],
	["delete file", onClickDeleteFile],
];

var win = Ti.UI.createWindow({
	backgroundColor: "#fff",
});
var log = Ti.UI.createTextArea({
	top: 0,
	width: Ti.UI.FILL,
	height: 100,
	borderColor: "red",
	borderWidth: 1,
	color: "#fff",
	backgroundColor: "#333",
	font: {
		fontSize: 14,
	},
});
win.add(log);
var scrollView = Ti.UI.createScrollView({
	top: 100,
	bottom: 0,
	contentWidth: Ti.UI.FILL,
	contentHeight: Ti.UI.SIZE,
	layout: "vertical",
});
win.add(scrollView);

var btnArray = [];

_.each(btns, function(btn) {
	if (btn[0] == "-") {
		var view = Ti.UI.createView({
			height: 10,
		});
		btnArray.push(view);
	} else {
		var button = Ti.UI.createButton({
			title: btn[0],
			width: 200,
			height: 50,
		});
		button.addEventListener("click", btn[1]);
		btnArray.push(button);
	}
});
scrollView.add(btnArray);
btnArray[0].backgroundColor = "#f44";

appwrite.addEventListener("realtimeEvent", function(e) {
	console.log("event: " + e.action);
});

appwrite.addEventListener("error", function(e) {
	console.error("error: " + e.action);
	console.error(e);
	log.value += e.action + " " + e.message + "\n";
});

appwrite.addEventListener("database", function(e) {
	console.log("---database---");
	if (e.documents) {
		console.log("data: " + JSON.stringify(e.documents));

		if (e.documents.length > 0) {
			documentId = e.documents[0]["document_id"];
			console.log(documentId);
		}
	} else if (e.data) {
		console.log("data: " + e.data);
	} else {
		console.log(e);
	}
	console.log("");
});

appwrite.addEventListener("storage", function(e) {
	console.log("---storage---");
	if (e.blob) {
		var img = Ti.UI.createImageView({
			width: 300,
			height: 300,
			image: e.blob,
		});
		win.add(img);
		img.addEventListener("click", function() {
			win.remove(img);
		});
	} else if (e.data) {
		console.log("data: " + e.data);
	} else if (e.files) {
		console.log("data: " + e.files);

		fileId = e.files[0]["id"];
		console.log(fileId);
	} else {
		console.log(e);
	}
	console.log("");
});

appwrite.addEventListener("account", function(e) {
	console.log("---account---");
	console.log("Action:", e.action);
	console.log(e);
	console.log("");
	if (e.session_id) {
		sessionId = e.session_id;
		console.log(sessionId);
	}
});

appwrite.addEventListener("connected", function(e) {
	btnArray[0].backgroundColor = "#4f4";
});

function onClickSub(e) {
	appwrite.subscribe(["files", "account"]);
}

function onClickLogin(e) {
	appwrite.createSession({
		email: USER_MAIL,
		password: USER_PASSWORD,
	});
}

function onClickCreate(e) {
	appwrite.createAccount({
		email: USER_MAIL,
		password: USER_PASSWORD,
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
		channels: ["files", "account"],
	});

	// appwrite.endpoint = SERVER_URL;
	// appwrite.project = PROJECT_ID;
	// appwrite.selfSigned = true;
}

function onClickGetDocuments(e) {
	appwrite.getDocuments(DATABASE_ID);
}

function onClickGetDocument(e) {
	appwrite.getDocument(DATABASE_ID, documentId);
}

function onClickCreateDocument(e) {
	appwrite.createDocument(DATABASE_ID, {
		id: 2,
		title: "test2",
	});
}

function onClickDeleteDocument(e) {
	appwrite.deleteDocument(DATABASE_ID, documentId);
}

function onClickGetAccount(e) {
	appwrite.getAccount();
}

function onClickVerify(e) {
	appwrite.verifyMail(SERVER_URL);
}

function onClickCreateFile(e) {
	var file = Ti.Filesystem.getFile(
		Ti.Filesystem.externalStorageDirectory,
		"appicon.png"
	);

	if (!file.exists()) {
		// copy it from /assets/ to the external storage
		var source = Ti.Filesystem.getFile(
			Ti.Filesystem.resourcesDirectory,
			"appicon.png"
		);
		file.write(source.read());
	}

	if (file.exists()) {
		appwrite.createFile({
			file: file,
			read: ["*"],
			write: ["*"],
		});
	}
}

function onClickListFiles(e) {
	appwrite.listFiles();
}

function onClickGetFile(e) {
	if (fileId != "") {
		appwrite.getFile(fileId);
	}
}

function onClickGetPreview(e) {
	if (fileId != "") {
		appwrite.getPreview({
			id: fileId,
			width: 10,
			height: 10,
			quality: 50,
		});
	}
}

function onClickDownloadFile(e) {
	if (fileId != "") {
		appwrite.downloadFile(fileId);
	}
}

function onClickDeleteFile(e) {
	if (fileId != "") {
		appwrite.deleteFile(fileId);
	}
}

function onClickDeleteSession(e) {
	if (sessionId != "") {
		appwrite.deleteSession(sessionId);
	}
}

win.open();
