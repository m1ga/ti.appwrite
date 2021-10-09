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

                            KrollDict kd = getUserData("createAccount", json);
                            proxy.fireEvent("account", kd);
                        }
                    } catch (AppwriteException e) {
                        ErrorClass.reportError("create account", e, proxy);
                    } catch (Throwable th) {
                    }
                }
            });
        } catch (AppwriteException e) {
            ErrorClass.reportError("create account", e, proxy);
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

                                KrollDict kd = getUserData("login", json);
                                proxy.fireEvent("account", kd);
                            }
                        } catch (AppwriteException e) {
                            ErrorClass.reportError("login", e, proxy);
                        } catch (Throwable th) {
                        }
                    }
                });
            } catch (AppwriteException e) {
                ErrorClass.reportError("login", e, proxy);
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
                            } catch (AppwriteException e) {
                                ErrorClass.reportError("verify mail", e, proxy);
                            } catch (Throwable th) {
                            }
                        }
                    }
            );
        } catch (AppwriteException e) {
            ErrorClass.reportError("verify mail", e, proxy);
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
                            KrollDict kd = getUserData("getAccount", json);
                            proxy.fireEvent("account", kd);
                        }
                    } catch (AppwriteException e) {
                        ErrorClass.reportError("get account", e, proxy);
                    } catch (Throwable th) {
                    }
                }
            });
        } catch (AppwriteException e) {
            ErrorClass.reportError("get account", e, proxy);
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
                    } catch (AppwriteException e) {
                        ErrorClass.reportError("delete account", e, proxy);
                    } catch (Throwable throwable) {
                    }
                }
            });
        } catch (AppwriteException e) {
            ErrorClass.reportError("delete account", e, proxy);
        }
    }

    private void reportError(String action) {
        KrollDict kd = new KrollDict();
        kd.put("action", action);
        proxy.fireEvent("error", kd);

    }


    private KrollDict getUserData(String action, JSONObject json) {
        KrollDict kd = new KrollDict();
        kd.put("action", action);

        if (json != null) {
            try {
                kd.put("id", json.has("$id") ? json.get("$id") : "");
                kd.put("name", json.has("name") ? json.get("name") : "");
                kd.put("registration", json.has("registration") ? json.get("registration") : "");
                kd.put("status", json.has("status") ? json.get("status") : "");
                kd.put("passwordUpdate", json.has("passwordUpdate") ? json.get("passwordUpdate") : "");
                kd.put("email", json.has("email") ? json.get("email") : "");
                kd.put("emailVerification", json.has("emailVerification") ? json.get("emailVerification") : "");
                kd.put("prefs", json.has("prefs") ? json.get("prefs").toString() : "");
            } catch (Exception e) {
                //
            }
        }
        return kd;
    }
}
