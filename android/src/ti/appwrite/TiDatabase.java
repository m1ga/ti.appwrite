package ti.appwrite;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.common.Log;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import io.appwrite.Client;
import io.appwrite.exceptions.AppwriteException;
import io.appwrite.services.Database;
import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import okhttp3.Response;

public class TiDatabase {
    String LCAT = "TiDatabase";
    Database database = null;
    private Client client = null;
    private KrollProxy proxy = null;

    public TiDatabase(Client _client, KrollProxy _proxy) {
        client = _client;
        database = new Database(client);
        proxy = _proxy;
    }

    public void createDocument(String collectionId, HashMap objectData) {
        String _action = "createDocument";

        if (collectionId != "") {
            Database database = new Database(client);
            try {
                JSONObject jsonArray = new JSONObject(objectData);
                database.createDocument(collectionId, objectData, new Continuation<Object>() {
                    @NotNull
                    @Override
                    public CoroutineContext getContext() {
                        return EmptyCoroutineContext.INSTANCE;
                    }

                    @Override
                    public void resumeWith(@NotNull Object o) {
                        try {
                            if (o instanceof Result.Failure) {
                                Result.Failure failure = (Result.Failure) o;
                                throw failure.exception;
                            } else {
                                Response response = (Response) o;
                                JSONObject json = new JSONObject(response.body().string());

                                KrollDict kd = getDocumentData(json);
                                kd.put("action", _action);
                                proxy.fireEvent("database", kd);
                            }
                        } catch (AppwriteException e) {
                            ErrorClass.reportError(_action, e, proxy);
                        } catch (Throwable th) {
                            Log.e("ERROR", th.toString());
                        }
                    }
                });
            } catch (Exception e) {
                //
            }
        }
    }


    public void getDocuments(String collectionId) {
        String _action = "getDocuments";

        if (collectionId != "") {
            Database database = new Database(client);
            try {
                database.listDocuments(collectionId, new Continuation<Object>() {
                    @NotNull
                    @Override
                    public CoroutineContext getContext() {
                        return EmptyCoroutineContext.INSTANCE;
                    }

                    @Override
                    public void resumeWith(@NotNull Object o) {
                        try {
                            if (o instanceof Result.Failure) {
                                Result.Failure failure = (Result.Failure) o;
                                throw failure.exception;
                            } else {
                                Response response = (Response) o;
                                JSONObject json = new JSONObject(response.body().string());

                                KrollDict kd = new KrollDict();
                                JSONArray files = new JSONArray(json.get("documents").toString());
                                Object[] kdFiles = new Object[files.length()];
                                for (var i = 0; i < files.length(); ++i) {
                                    JSONObject file = files.getJSONObject(i);
                                    KrollDict kfile = getDocumentData(file);
                                    kdFiles[i] = kfile;
                                }
                                kd.put("action", _action);
                                kd.put("documents", kdFiles);
                                proxy.fireEvent("database", kd);
                            }
                        } catch (AppwriteException e) {
                            ErrorClass.reportError(_action, e, proxy);
                        } catch (Throwable th) {
                            Log.e("ERROR", th.toString());
                        }
                    }
                });
            } catch (Exception e) {
                //
            }
        }
    }

    public void getDocument(String collectionId, String documentId) {
        String _action = "getDocument";

        if (collectionId != "" && documentId != "") {
            Database database = new Database(client);
            try {
                database.getDocument(collectionId, documentId, new Continuation<Object>() {
                    @NotNull
                    @Override
                    public CoroutineContext getContext() {
                        return EmptyCoroutineContext.INSTANCE;
                    }

                    @Override
                    public void resumeWith(@NotNull Object o) {
                        try {
                            if (o instanceof Result.Failure) {
                                Result.Failure failure = (Result.Failure) o;
                                throw failure.exception;
                            } else {
                                Response response = (Response) o;
                                JSONObject json = new JSONObject(response.body().string());
                                KrollDict kd = getDocumentData(json);
                                kd.put("action", _action);
                                proxy.fireEvent("database", kd);
                            }
                        } catch (AppwriteException e) {
                            ErrorClass.reportError(_action, e, proxy);
                        } catch (Throwable th) {
                            Log.e("ERROR", th.toString());
                        }
                    }
                });
            } catch (Exception e) {
                //
            }
        }
    }

    public void deleteDocument(String collectionId, String documentId) {
        String _action = "deleteDocuments";

        if (collectionId != "") {
            Database database = new Database(client);
            try {
                database.deleteDocument(collectionId, documentId, new Continuation<Object>() {
                    @NotNull
                    @Override
                    public CoroutineContext getContext() {
                        return EmptyCoroutineContext.INSTANCE;
                    }

                    @Override
                    public void resumeWith(@NotNull Object o) {
                        try {
                            if (o instanceof Result.Failure) {
                                Result.Failure failure = (Result.Failure) o;
                                throw failure.exception;
                            } else {
                                KrollDict kd = new KrollDict();
                                kd.put("action", _action);
                                proxy.fireEvent("database", kd);
                            }
                        } catch (AppwriteException e) {
                            ErrorClass.reportError(_action, e, proxy);
                        } catch (Throwable th) {
                            Log.e("ERROR", th.toString());
                        }
                    }
                });
            } catch (Exception e) {
                //
            }
        }
    }

    private KrollDict getDocumentData(JSONObject json) {
        KrollDict kd = new KrollDict();
        if (json != null) {
            try {
                kd.put("document_id", json.has("$id") ? json.get("$id") : "");
                kd.put("permissions", json.has("$name") ? json.get("$name") : "");
                kd.put("collection", json.has("$collection") ? json.get("$collection") : "");

                for (Iterator key = json.keys(); key.hasNext(); ) {
                    String keyValue = key.next().toString();
                    if (keyValue.charAt(0) != '$') {
                        kd.put(keyValue, json.get(keyValue));
                    }
                }
            } catch (Exception e) {
                //
            }
        }
        return kd;
    }
}
