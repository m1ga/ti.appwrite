package ti.appwrite;

import androidx.annotation.NonNull;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.util.TiConvert;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;

import io.appwrite.Client;
import io.appwrite.exceptions.AppwriteException;
import io.appwrite.services.Account;
import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import okhttp3.Response;

public class TiAccount {
    String LCAT = "TiAccount";
    Account account = null;
    private Client client = null;
    private KrollProxy proxy = null;

    public TiAccount(Client _client, KrollProxy _proxy) {
        client = _client;
        account = new Account(client);
        proxy = _proxy;
    }

    public void createAccount(HashMap map) {
        String email = TiConvert.toString(map.get("email"), "");
        String password = TiConvert.toString(map.get("password"), "");

        if (account == null && email != "" && password != "") {
            return;
        }

        try {
            account.create(email, password, new Continuation<Response>() {
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

                            KrollDict kd = TiAppwriteModule.getUserData("createAccount", json);
                            proxy.fireEvent("account", kd);
                        }
                    } catch (Throwable th) {
                        th.printStackTrace();
                        reportError("create account");
                    }
                }
            });
        } catch (AppwriteException e) {
            reportError("create account", e.getCode() + "\n" + e.getResponse() + "\n" + e.getMessage());
        }
    }


    public void login(HashMap map) {
        if (account == null) {
            return;
        }

        String email = TiConvert.toString(map.get("email"), "");
        String password = TiConvert.toString(map.get("password"), "");

        if (email != "" && password != "") {
            try {
                account.createSession(email, password, new Continuation<Object>() {
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

                                KrollDict kd = TiAppwriteModule.getUserData("login", json);
                                proxy.fireEvent("account", kd);
                            }
                        } catch (Throwable th) {
                            reportError("login");
                        }
                    }
                });
            } catch (AppwriteException e) {
                reportError("login", e.getCode() + "\n" + e.getResponse() + "\n" + e.getMessage());
            }
        }
    }

    public void verifyMail(String url) {
        if (account == null && url != "") {
            return;
        }
        try {
            account.createVerification(url,
                    new Continuation<Object>() {
                        @NotNull
                        @Override
                        public CoroutineContext getContext() {
                            return EmptyCoroutineContext.INSTANCE;
                        }

                        @Override
                        public void resumeWith(@NotNull Object o) {
                            String json = "";
                            try {
                                if (o instanceof Result.Failure) {
                                    Result.Failure failure = (Result.Failure) o;
                                    throw failure.exception;
                                } else {
                                    Response response = (Response) o;
                                    //JSONObject json = new JSONObject(response.body().string());
                                    Log.i("---", response.body().string());
                                    //KrollDict kd = TiAppwriteModule.getUserData("login", json);
                                    //proxy.fireEvent("account", kd);
                                }
                            } catch (Throwable th) {
                                Log.e("ERROR", th.toString());
                            }
                        }
                    }
            );
        } catch (AppwriteException e) {
            reportError("verify mail", e.getCode() + "\n" + e.getResponse() + "\n" + e.getMessage());
        }

    }

    public void getAccount() {
        if (account == null) {
            return;
        }
        try {
            account.get(new Continuation<Object>() {
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
                            KrollDict kd = TiAppwriteModule.getUserData("getAccount", json);
                            proxy.fireEvent("account", kd);
                        }
                    } catch (Throwable th) {
                        reportError("get account", th.toString());
                    }
                }
            });
        } catch (AppwriteException e) {
            reportError("get account", e.getCode() + "\n" + e.getResponse() + "\n" + e.getMessage());
        }
    }

    public void deleteAccount() {
        if (account == null) {
            return;
        }
        try {
            account.delete(new Continuation<Response>() {
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
                            KrollDict kd = new KrollDict();
                            kd.put("deleteAccount", true);
                            proxy.fireEvent("account", kd);
                        }
                    } catch (Throwable th) {
                        th.printStackTrace();
                        reportError("delete account", ((AppwriteException)th).getMessage());
                    }
                }
            });
        } catch (AppwriteException e) {
            reportError("delete account", e.getCode() + "\n" + e.getResponse() + "\n" + e.getMessage());
        }
    }

    private void reportError(String action, String message) {
        Log.e(LCAT, "error: " + message);
        reportError(action);
    }

    private void reportError(String action) {
        KrollDict kd = new KrollDict();
        kd.put("action", action);
        proxy.fireEvent("error", kd);

    }
}
