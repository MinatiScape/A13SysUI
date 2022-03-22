package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import androidx.core.graphics.drawable.IconCompat;
/* loaded from: classes.dex */
public final class SuggestParcelables$IntentParam {
    public boolean boolValue;
    @Nullable
    public String contentUri;
    public float floatValue;
    public int intValue;
    @Nullable
    public SuggestParcelables$IntentInfo intentValue;
    public long longValue;
    @Nullable
    public String name;
    @Nullable
    public String strValue;
    public SuggestParcelables$IntentParamType type;

    public SuggestParcelables$IntentParam(Bundle bundle) {
        SuggestParcelables$IntentParamType suggestParcelables$IntentParamType;
        if (bundle.containsKey("name")) {
            this.name = bundle.getString("name");
        }
        if (bundle.containsKey(IconCompat.EXTRA_TYPE)) {
            Bundle bundle2 = bundle.getBundle(IconCompat.EXTRA_TYPE);
            if (bundle2 == null) {
                this.type = null;
            } else {
                int i = bundle2.getInt("value");
                if (i == 0) {
                    suggestParcelables$IntentParamType = SuggestParcelables$IntentParamType.INTENT_PARAM_TYPE_UNKNOWN;
                } else if (i == 1) {
                    suggestParcelables$IntentParamType = SuggestParcelables$IntentParamType.INTENT_PARAM_TYPE_STRING;
                } else if (i == 2) {
                    suggestParcelables$IntentParamType = SuggestParcelables$IntentParamType.INTENT_PARAM_TYPE_INT;
                } else if (i == 3) {
                    suggestParcelables$IntentParamType = SuggestParcelables$IntentParamType.INTENT_PARAM_TYPE_FLOAT;
                } else if (i == 4) {
                    suggestParcelables$IntentParamType = SuggestParcelables$IntentParamType.INTENT_PARAM_TYPE_LONG;
                } else if (i == 5) {
                    suggestParcelables$IntentParamType = SuggestParcelables$IntentParamType.INTENT_PARAM_TYPE_INTENT;
                } else if (i == 6) {
                    suggestParcelables$IntentParamType = SuggestParcelables$IntentParamType.INTENT_PARAM_TYPE_CONTENT_URI;
                } else {
                    suggestParcelables$IntentParamType = i == 7 ? SuggestParcelables$IntentParamType.INTENT_PARAM_TYPE_BOOL : null;
                }
                this.type = suggestParcelables$IntentParamType;
            }
        }
        if (bundle.containsKey("strValue")) {
            this.strValue = bundle.getString("strValue");
        }
        if (bundle.containsKey("intValue")) {
            this.intValue = bundle.getInt("intValue");
        }
        if (bundle.containsKey("floatValue")) {
            this.floatValue = bundle.getFloat("floatValue");
        }
        if (bundle.containsKey("longValue")) {
            this.longValue = bundle.getLong("longValue");
        }
        if (bundle.containsKey("boolValue")) {
            this.boolValue = bundle.getBoolean("boolValue");
        }
        if (bundle.containsKey("intentValue")) {
            Bundle bundle3 = bundle.getBundle("intentValue");
            if (bundle3 == null) {
                this.intentValue = null;
            } else {
                this.intentValue = new SuggestParcelables$IntentInfo(bundle3);
            }
        }
        if (bundle.containsKey("contentUri")) {
            this.contentUri = bundle.getString("contentUri");
        }
    }

    public SuggestParcelables$IntentParam() {
    }
}
