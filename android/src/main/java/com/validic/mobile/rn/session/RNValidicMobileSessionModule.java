package com.validic.mobile.rn.session;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.validic.common.GsonUtil;
import com.validic.common.ValidicLog;
import com.validic.mobile.Session;
import com.validic.mobile.SessionListener;
import com.validic.mobile.User;
import com.validic.mobile.ValidicMobile;
import com.validic.mobile.record.Record;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import static com.validic.mobile.rn.session.Utils.sendEvent;

/**
 * Created by griffinwilson on 2/21/18.
 */

public class RNValidicMobileSessionModule extends ReactContextBaseJavaModule implements SessionListener {

    public RNValidicMobileSessionModule(ReactApplicationContext reactContext) {
        super(reactContext);
        ValidicMobile.getInstance().initialize(reactContext.getApplicationContext());
        Session.getInstance().setSessionListener(this);
        ValidicLog.globalLoggingEnabled(BuildConfig.DEBUG);
    }

    @Override
    public String getName() {
        return "RNValidicMobileSession";
    }

    @ReactMethod
    public void startSession(String user, String org, String token) {
        Session.getInstance().startSessionWithUser(new User(org, user, token));
    }

    @ReactMethod
    public void endSession() {
        Session.getInstance().endSession();
    }

    @ReactMethod
    public void getUser(Callback userCallback) {
        User user = Session.getInstance().getUser();
        WritableMap callbackMap = Arguments.createMap();
        if (user != null) {
            WritableMap userMap = Arguments.createMap();
            userMap.putString("user_id", user.getValidicUserID());
            userMap.putString("org_id", user.getOrganizationID());
            userMap.putString("user_token", user.getAccessToken());
            callbackMap.putMap("user", userMap);
        } else {
            callbackMap.putMap("user", null);
        }
        userCallback.invoke(callbackMap);
    }

    @ReactMethod
    public void submitRecord(Record record) {
        Session.getInstance().submitRecord(record);
    }

    @Override
    public void didSubmitRecord(Record record) {
        try {
            JSONObject obj = new JSONObject(GsonUtil.getInstance().toJson(record));
            sendEvent(getReactApplicationContext(), "validic:session:onsuccess", Utils.convertJsonToMap(obj));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void didFailToSubmitRecord(Record record, Error error) {
        try {
            JSONObject obj = new JSONObject(GsonUtil.getInstance().toJson(record));
            WritableMap map = Arguments.createMap();
            WritableMap rMap = Utils.convertJsonToMap(obj);
            map.putString("error", error.getMessage());
            map.putMap("record", rMap);
            sendEvent(getReactApplicationContext(), "validic:session:onerror", map);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        Map<String, Object> constants = new HashMap<>();
        constants.put("SESSION_VERSION", Session.libraryVersion());
        return constants;
    }
}