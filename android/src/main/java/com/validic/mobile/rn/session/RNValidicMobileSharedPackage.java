package com.validic.mobile.rn.session;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.validic.mobile.ValidicMobile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RNValidicMobileSharedPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        ValidicMobile.getInstance().initialize(reactContext.getApplicationContext());
        return Arrays.<NativeModule>asList(new RNValidicMobileSessionModule(reactContext));
    }

    // Deprecated from RN 0.47
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}
