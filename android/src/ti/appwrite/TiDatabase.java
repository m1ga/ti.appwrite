package ti.appwrite;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.common.Log;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import io.appwrite.Client;
import io.appwrite.exceptions.AppwriteException;
import io.appwrite.services.Database;
import io.appwrite.services.Storage;
import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import okhttp3.Response;

public class TiDatabase {
    String LCAT = "TiDatabase";
    Database database  = null;
    private Client client = null;
    private KrollProxy proxy = null;

    public TiDatabase(Client _client, KrollProxy _proxy) {
        client = _client;
        database = new Database(client);
        proxy = _proxy;
    }
    public void getDocuments(String collectionId) {
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
                                kd.put("data", json.has("documents") ? json.get("documents").toString() : "{}");
                                proxy.fireEvent("documents", kd);
                            }
                        } catch (AppwriteException e) {
                            ErrorClass.reportError("list documents", e, proxy);
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
}
