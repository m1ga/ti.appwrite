package ti.appwrite;

import androidx.annotation.NonNull;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiBlob;
import org.appcelerator.titanium.util.TiConvert;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

import io.appwrite.Client;
import io.appwrite.exceptions.AppwriteException;
import io.appwrite.services.Storage;
import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import okhttp3.Response;

public class TiStorage {

    String LCAT = "TiStorage";
    Storage storage = null;
    private Client client = null;
    private KrollProxy proxy = null;

    public TiStorage(Client _client, KrollProxy _proxy) {
        client = _client;
        storage = new Storage(client);
        proxy = _proxy;
    }

    public void createFile(File file, String[] _read, String[] _write) {
        String _action = "createFile";
        try {
            storage.createFile("unique()","unique()", file, Arrays.asList(_read.clone()), Arrays.asList(_write.clone()), new Continuation<Object>() {
                @NonNull
                @Override
                public CoroutineContext getContext() {
                    return EmptyCoroutineContext.INSTANCE;
                }

                @Override
                public void resumeWith(@NonNull Object o) {
                    try {
                        if (o instanceof Result.Failure) {
                            Result.Failure failure = (Result.Failure) o;
                            throw failure.exception;
                        } else {
                            Response response = (Response) o;
                            JSONObject json = new JSONObject(response.body().string());
                            KrollDict kd = getFileData(json);
                            kd.put("action", _action);
                            proxy.fireEvent("storage", kd);
                        }
                    } catch (AppwriteException e) {
                        ErrorClass.reportError(_action, e, proxy);
                    } catch (Throwable th) {
                        Log.e(LCAT, _action + th.toString());
                    }
                }
            });
        } catch (AppwriteException e) {
            ErrorClass.reportError(_action, e, proxy);
        }
    }

    public void listFiles(String bucketId) {
        String _action = "listFiles";
        try {
            storage.listFiles(bucketId, new Continuation<Object>() {
                @NonNull
                @Override
                public CoroutineContext getContext() {
                    return EmptyCoroutineContext.INSTANCE;
                }

                @Override
                public void resumeWith(@NonNull Object o) {
                    try {
                        if (o instanceof Result.Failure) {
                            Result.Failure failure = (Result.Failure) o;
                            throw failure.exception;
                        } else {
                            Response response = (Response) o;
                            JSONObject json = new JSONObject(response.body().string());

                            KrollDict kd = new KrollDict();
                            JSONArray files = new JSONArray(json.get("files").toString());
                            Object[] kdFiles = new Object[files.length()];
                            for (var i = 0; i < files.length(); ++i) {
                                JSONObject file = files.getJSONObject(i);
                                KrollDict kfile = getFileData(file);
                                kdFiles[i] = kfile;
                            }
                            kd.put("action", _action);
                            kd.put("files", kdFiles);
                            proxy.fireEvent("storage", kd);
                        }
                    } catch (AppwriteException e) {
                        ErrorClass.reportError(_action, e, proxy);
                    } catch (Throwable th) {
                        Log.e(LCAT, _action + th.toString());
                    }
                }
            });
        } catch (AppwriteException e) {
            ErrorClass.reportError(_action, e, proxy);
        }
    }

    public void getFile(String buckedId, String fileId) {
        String _action = "getFile";
        try {
            storage.getFile(buckedId, fileId, new Continuation<Object>() {
                @NonNull
                @Override
                public CoroutineContext getContext() {
                    return EmptyCoroutineContext.INSTANCE;
                }

                @Override
                public void resumeWith(@NonNull Object o) {
                    try {
                        if (o instanceof Result.Failure) {
                            Result.Failure failure = (Result.Failure) o;
                            throw failure.exception;
                        } else {
                            Response response = (Response) o;
                            JSONObject json = new JSONObject(response.body().string());

                            KrollDict kd = getFileData(json);
                            kd.put("action", _action);
                            proxy.fireEvent("storage", kd);
                        }
                    } catch (AppwriteException e) {
                        ErrorClass.reportError(_action, e, proxy);
                    } catch (Throwable th) {
                        Log.e(LCAT, _action + th.toString());
                    }
                }
            });
        } catch (AppwriteException e) {
            ErrorClass.reportError(_action, e, proxy);
        }
    }

    public void deleteFile(String bucketId, String fileId) {
        String _action = "deleteFile";
        try {
            storage.deleteFile(bucketId, fileId, new Continuation<Object>() {
                @NonNull
                @Override
                public CoroutineContext getContext() {
                    return EmptyCoroutineContext.INSTANCE;
                }

                @Override
                public void resumeWith(@NonNull Object o) {
                    try {
                        if (o instanceof Result.Failure) {
                            Result.Failure failure = (Result.Failure) o;
                            throw failure.exception;
                        } else {
                            Response response = (Response) o;
                            KrollDict kd = new KrollDict();
                            kd.put("action", _action);
                            proxy.fireEvent("storage", kd);
                        }
                    } catch (AppwriteException e) {
                        ErrorClass.reportError(_action, e, proxy);
                    } catch (Throwable th) {
                        Log.e(LCAT, _action + th.toString());
                    }
                }
            });
        } catch (AppwriteException e) {
            ErrorClass.reportError(_action, e, proxy);
        }
    }

    public void downloadFile(String bucketId, String fileId) {
        String _action = "downloadFile";
        try {
            storage.getFileDownload(bucketId, fileId, new Continuation<Object>() {
                @NonNull
                @Override
                public CoroutineContext getContext() {
                    return EmptyCoroutineContext.INSTANCE;
                }

                @Override
                public void resumeWith(@NonNull Object o) {
                    try {
                        if (o instanceof Result.Failure) {
                            Result.Failure failure = (Result.Failure) o;
                            throw failure.exception;
                        } else {
                            Response response = (Response) o;
                            KrollDict kd = new KrollDict();
                            kd.put("action", _action);
                            kd.put("blob", TiBlob.blobFromData(response.body().bytes()));
                            proxy.fireEvent("storage", kd);

                        }
                    } catch (AppwriteException e) {
                        ErrorClass.reportError(_action, e, proxy);
                    } catch (Throwable th) {
                        Log.e(LCAT, _action + th.toString());
                    }
                }
            });
        } catch (AppwriteException e) {
            ErrorClass.reportError(_action, e, proxy);
        }
    }

    public void getPreview(HashMap data) {
        String _action = "previewFile";
        String fileId = TiConvert.toString(data.get("id"), "");
        String bucketId = TiConvert.toString(data.get("bucketId"), "");
        long width = TiConvert.toInt(data.get("width"), -1);
        long height = TiConvert.toInt(data.get("height"), -1);
        long quality = TiConvert.toInt(data.get("quality"), 100);

        if (fileId != "") {
            try {
                storage.getFilePreview(bucketId, fileId, width == -1 ? null : width, height == -1 ? null : height, null, quality, new Continuation<Object>() {
                    @NonNull
                    @Override
                    public CoroutineContext getContext() {
                        return EmptyCoroutineContext.INSTANCE;
                    }

                    @Override
                    public void resumeWith(@NonNull Object o) {
                        try {
                            if (o instanceof Result.Failure) {
                                Result.Failure failure = (Result.Failure) o;
                                throw failure.exception;
                            } else {
                                Response response = (Response) o;
                                KrollDict kd = new KrollDict();
                                kd.put("action", _action);
                                kd.put("blob", TiBlob.blobFromData(response.body().bytes()));
                                proxy.fireEvent("storage", kd);

                            }
                        } catch (AppwriteException e) {
                            ErrorClass.reportError(_action, e, proxy);
                        } catch (Throwable th) {
                            Log.e(LCAT, _action + th.toString());
                        }
                    }
                });
            } catch (AppwriteException e) {
                ErrorClass.reportError(_action, e, proxy);
            }
        }
    }


    private KrollDict getFileData(JSONObject json) {
        KrollDict kd = new KrollDict();
        if (json != null) {
            try {
                kd.put("id", json.has("$id") ? json.get("$id") : "");
                kd.put("name", json.has("name") ? json.get("name") : "");
                kd.put("permissions", json.has("$name") ? json.get("$name") : "");
                kd.put("dateCreated", json.has("dateCreated") ? json.get("dateCreated") : "");
                kd.put("signature", json.has("signature") ? json.get("signature") : "");
                kd.put("mimeType", json.has("mimeType") ? json.get("mimeType") : "");
            } catch (Exception e) {
                //
            }
        }
        return kd;
    }
}
