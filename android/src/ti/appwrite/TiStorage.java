package ti.appwrite;

import androidx.annotation.NonNull;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.common.Log;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;

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
        try {
            Log.i(LCAT, "File: " + Arrays.asList(_read.clone()).toString());
            storage.createFile(file, Arrays.asList(_read.clone()), Arrays.asList(_write.clone()), new Continuation<Response>() {
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
                            proxy.fireEvent("storage", kd);
                        }
                    }catch (AppwriteException e) {
                        ErrorClass.reportError("create file", e, proxy);
                    }  catch (Throwable th) {
                        Log.e(LCAT, "create file: " + th.toString());
                    }
                }
            });
        } catch (AppwriteException e) {
            ErrorClass.reportError("create file", e, proxy);
        }
    }

    public void listFiles() {
        try {
            storage.listFiles(new Continuation<Response>() {
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
                            kd.put("data", json.has("files") ? json.get("files").toString() : "{}");
                            proxy.fireEvent("storage", kd);
                        }
                    }catch (AppwriteException e) {
                        ErrorClass.reportError("create file", e, proxy);
                    }  catch (Throwable th) {
                        Log.e(LCAT, "create file: " + th.toString());
                    }
                }
            });
        } catch (AppwriteException e) {
            ErrorClass.reportError("create file", e, proxy);
        }
    }
}
