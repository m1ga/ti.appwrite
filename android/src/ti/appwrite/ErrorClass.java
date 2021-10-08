package ti.appwrite;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.titanium.TiApplication;

import org.appcelerator.kroll.KrollProxy;
import org.json.JSONException;
import org.json.JSONObject;

import io.appwrite.exceptions.AppwriteException;

public class ErrorClass {
    public static void reportError(String action, AppwriteException ex, KrollProxy proxy) {
        KrollDict kd = new KrollDict();

        String message = "";
        try {
            JSONObject json = new JSONObject(ex.getResponse());
            message = json.get("message").toString();
        } catch (JSONException e) {

        }

        kd.put("action", action);
        kd.put("message", message);
        kd.put("code", ex.getCode());
        kd.put("response", ex.getResponse());
        proxy.fireEvent("error", kd);
    }
}
