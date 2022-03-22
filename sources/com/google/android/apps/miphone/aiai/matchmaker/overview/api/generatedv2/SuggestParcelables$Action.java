package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
/* loaded from: classes.dex */
public final class SuggestParcelables$Action {
    @Nullable
    public String dEPRECATEDIconBitmapId;
    @Nullable
    public SuggestParcelables$IntentInfo dEPRECATEDIntentInfo;
    @Nullable
    public String displayName;
    @Nullable
    public String fullDisplayName;
    public boolean hasProxiedIntentInfo;
    @Nullable
    public String id;
    @Nullable
    public String opaquePayload;
    @Nullable
    public SuggestParcelables$IntentInfo proxiedIntentInfo;

    public SuggestParcelables$Action(Bundle bundle) {
        if (bundle.containsKey("id")) {
            this.id = bundle.getString("id");
        }
        if (bundle.containsKey("displayName")) {
            this.displayName = bundle.getString("displayName");
        }
        if (bundle.containsKey("dEPRECATEDIconBitmapId")) {
            this.dEPRECATEDIconBitmapId = bundle.getString("dEPRECATEDIconBitmapId");
        }
        if (bundle.containsKey("fullDisplayName")) {
            this.fullDisplayName = bundle.getString("fullDisplayName");
        }
        if (bundle.containsKey("dEPRECATEDIntentInfo")) {
            Bundle bundle2 = bundle.getBundle("dEPRECATEDIntentInfo");
            if (bundle2 == null) {
                this.dEPRECATEDIntentInfo = null;
            } else {
                this.dEPRECATEDIntentInfo = new SuggestParcelables$IntentInfo(bundle2);
            }
        }
        if (!bundle.containsKey("proxiedIntentInfo")) {
            this.hasProxiedIntentInfo = false;
        } else {
            this.hasProxiedIntentInfo = true;
            Bundle bundle3 = bundle.getBundle("proxiedIntentInfo");
            if (bundle3 == null) {
                this.proxiedIntentInfo = null;
            } else {
                this.proxiedIntentInfo = new SuggestParcelables$IntentInfo(bundle3);
            }
        }
        if (bundle.containsKey("opaquePayload")) {
            this.opaquePayload = bundle.getString("opaquePayload");
        }
    }

    public final Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("id", this.id);
        bundle.putString("displayName", this.displayName);
        bundle.putString("dEPRECATEDIconBitmapId", this.dEPRECATEDIconBitmapId);
        bundle.putString("fullDisplayName", this.fullDisplayName);
        SuggestParcelables$IntentInfo suggestParcelables$IntentInfo = this.dEPRECATEDIntentInfo;
        if (suggestParcelables$IntentInfo == null) {
            bundle.putBundle("dEPRECATEDIntentInfo", null);
        } else {
            bundle.putBundle("dEPRECATEDIntentInfo", suggestParcelables$IntentInfo.writeToBundle());
        }
        SuggestParcelables$IntentInfo suggestParcelables$IntentInfo2 = this.proxiedIntentInfo;
        if (suggestParcelables$IntentInfo2 == null) {
            bundle.putBundle("proxiedIntentInfo", null);
        } else {
            bundle.putBundle("proxiedIntentInfo", suggestParcelables$IntentInfo2.writeToBundle());
        }
        bundle.putString("opaquePayload", this.opaquePayload);
        return bundle;
    }

    public SuggestParcelables$Action() {
    }
}
