/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2018 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package ti.appwrite;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiFileProxy;
import org.appcelerator.titanium.io.TiBaseFile;
import org.appcelerator.titanium.util.TiConvert;
import org.json.JSONObject;

import java.util.HashMap;

import io.appwrite.Client;
import io.appwrite.models.RealtimeSubscription;
import io.appwrite.services.Realtime;


@Kroll.module(name = "TiAppwrite", id = "ti.appwrite")
public class TiAppwriteModule extends KrollModule {

    // Standard Debugging variables
    private static final String LCAT = "Ti.Appwrite";
    private static final boolean DBG = TiConfig.LOGD;
    private TiAccount account = null;
    private TiStorage storage = null;
    private TiDatabase database = null;
    private Client client = null;
    private String projectId = "";
    private boolean isSelfSigned = false;
    private boolean isConnected = false;

    public TiAppwriteModule() {
        super();
    }

    @Kroll.onAppCreate
    public static void onAppCreate(TiApplication app) {
    }

    // Methods
    @Kroll.method
    public void create(HashMap map) {
        client = new Client(TiApplication.getAppCurrentActivity());
        String _endpoint = TiConvert.toString(map.get("endpoint"), "");
        String _project = TiConvert.toString(map.get("project"), "");
        Object[] _channels = map.containsKey("channels") ? (Object[]) map.get("channels") : null;
        isSelfSigned = TiConvert.toBoolean(map.get("selfSigned"), false);

        if (_endpoint != "" && _project != "") {
            endpoint(_endpoint);
            project(_project);
            selfSigned(isSelfSigned);
            isConnected = true;

            if (_channels != null) {
                subscribeChannels(_channels);
            }
            account = new TiAccount(client, this);
            storage = new TiStorage(client, this);
            database = new TiDatabase(client, this);
            Log.i(LCAT, "connected: " + client.getEndPoint());
            fireEvent("connected", new KrollDict());
        }
    }

    private void subscribeChannels(Object[] data) {
        if (!checkConnection()) return;
        Realtime realtime = new Realtime(client);
        String[] channels = new String[data.length];
        System.arraycopy(data, 0, channels, 0, data.length);

        realtime.subscribe(channels, response -> {
            KrollDict kd = new KrollDict();
            kd.put("action", response.getEvent());
            fireEvent("realtimeEvent", kd);
            return null;
        });
    }

    @Kroll.method
    public void subscribe(Object map) {
        if (!checkConnection()) return;
        if (map != null) {
            subscribeChannels((Object[]) map);
        }
    }

    @Kroll.method
    public void listFiles() {
        if (!checkConnection()) return;
        if (storage != null) {
            storage.listFiles();
        }
    }

    @Kroll.method
    public void getFile(String fileId) {
        if (!checkConnection()) return;
        if (storage != null && fileId != "") {
            storage.getFile(fileId);
        }
    }

    @Kroll.method
    public void downloadFile(String fileId) {
        if (!checkConnection()) return;
        if (storage != null && fileId != "") {
            storage.downloadFile(fileId);
        }
    }

    @Kroll.method
    public void deleteFile(String fileId) {
        if (!checkConnection()) return;
        if (storage != null && fileId != "") {
            storage.deleteFile(fileId);
        }
    }

    @Kroll.method
    public void unsubscribe(Object _input) {
        if (!checkConnection()) return;
        Object[] data = (Object[]) _input;
        Realtime realtime = new Realtime(client);
        String[] channels = new String[data.length];
        System.arraycopy(data, 0, channels, 0, data.length);

        RealtimeSubscription sub = realtime.subscribe(channels, response -> {
            return null;
        });
        sub.close();
    }

    @Kroll.method
    public void getDocuments(String collectionId) {
        if (!checkConnection()) return;
        if (collectionId != "") {
            database.getDocuments(collectionId);
        }
    }
    @Kroll.method
    public void getDocument(String collectionId, String documentId) {
        if (!checkConnection()) return;
        if (collectionId != "" && documentId != "") {
            database.getDocument(collectionId, documentId);
        }
    }

    @Kroll.method
    public void login(HashMap map) {
        if (!checkConnection()) return;
        account.login(map);
    }

    @Kroll.method
    public void verifyMail(String url) {
        if (!checkConnection()) return;
        account.verifyMail(url);
    }

    @Kroll.method
    public void getAccount() {
        if (!checkConnection()) return;
        account.getAccount();
    }

    @Kroll.method
    public void createAccount(HashMap map) {
        if (!checkConnection()) return;
        account.createAccount(map);
    }

    @Kroll.method
    public void deleteAccount() {
        if (!checkConnection()) return;
        account.deleteAccount();
    }

    @Kroll.method
    public void createFile(HashMap map) {
        if (!checkConnection()) return;
        Object obj = map.get("file");
        String[] _read = TiConvert.toStringArray((Object[]) map.get("read"));
        String[] _write = TiConvert.toStringArray((Object[]) map.get("write"));
        if (storage == null) return;
        if (obj instanceof TiFileProxy) {
            TiBaseFile file = ((TiFileProxy) obj).getBaseFile();
            //Log.i(LCAT, "File: " + file.nativePath() + " " + file.exists());
            try {
                storage.createFile(file.getNativeFile(), _read, _write);
            } catch (Exception e) {
                Log.e(LCAT, "Error in file: " + e.getMessage());
            }
        }

    }

    @Kroll.setProperty
    public void endpoint(String url) {
        if (client != null) {
            client.setEndpoint(url);
        }
    }

    @Kroll.setProperty
    public void project(String id) {
        if (client != null) {
            projectId = id;
            client.setProject(projectId);
        }
    }

    @Kroll.getProperty
    public String endpoint() {
        if (client == null){
            return "";
        }
        return client.getEndPoint();
    }

    @Kroll.getProperty
    public String project() {
        return projectId;
    }


    @Kroll.setProperty
    public void selfSigned(boolean val) {
        if (client != null) {
            isSelfSigned = val;
            client.setSelfSigned(isSelfSigned);
        }
    }

    @Kroll.getProperty
    public boolean selfSigned() {
        return isSelfSigned;
    }


    public boolean checkConnection(){
        if (isConnected == false){
            KrollDict kd = new KrollDict();
            kd.put("action", "connection");
            kd.put("message", "no active connection");
            fireEvent("error", kd);
            return false;
        } else {
            return true;
        }
    }
}
