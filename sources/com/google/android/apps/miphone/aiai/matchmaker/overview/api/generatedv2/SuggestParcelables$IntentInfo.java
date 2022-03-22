package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import androidx.core.graphics.drawable.IconCompat;
import com.google.android.setupcompat.partnerconfig.ResourceEntry;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public final class SuggestParcelables$IntentInfo {
    @Nullable
    public String action;
    @Nullable
    public String className;
    public int flags;
    @Nullable
    public ArrayList intentParams;
    public SuggestParcelables$IntentType intentType;
    @Nullable
    public String mimeType;
    @Nullable
    public String packageName;
    @Nullable
    public String uri;

    public final Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        if (this.intentParams == null) {
            bundle.putParcelableArrayList("intentParams", null);
        } else {
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>(this.intentParams.size());
            Iterator it = this.intentParams.iterator();
            while (it.hasNext()) {
                SuggestParcelables$IntentParam suggestParcelables$IntentParam = (SuggestParcelables$IntentParam) it.next();
                if (suggestParcelables$IntentParam == null) {
                    arrayList.add(null);
                } else {
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("name", suggestParcelables$IntentParam.name);
                    SuggestParcelables$IntentParamType suggestParcelables$IntentParamType = suggestParcelables$IntentParam.type;
                    if (suggestParcelables$IntentParamType == null) {
                        bundle2.putBundle(IconCompat.EXTRA_TYPE, null);
                    } else {
                        Bundle bundle3 = new Bundle();
                        bundle3.putInt("value", suggestParcelables$IntentParamType.value);
                        bundle2.putBundle(IconCompat.EXTRA_TYPE, bundle3);
                    }
                    bundle2.putString("strValue", suggestParcelables$IntentParam.strValue);
                    bundle2.putInt("intValue", suggestParcelables$IntentParam.intValue);
                    bundle2.putFloat("floatValue", suggestParcelables$IntentParam.floatValue);
                    bundle2.putLong("longValue", suggestParcelables$IntentParam.longValue);
                    bundle2.putBoolean("boolValue", suggestParcelables$IntentParam.boolValue);
                    SuggestParcelables$IntentInfo suggestParcelables$IntentInfo = suggestParcelables$IntentParam.intentValue;
                    if (suggestParcelables$IntentInfo == null) {
                        bundle2.putBundle("intentValue", null);
                    } else {
                        bundle2.putBundle("intentValue", suggestParcelables$IntentInfo.writeToBundle());
                    }
                    bundle2.putString("contentUri", suggestParcelables$IntentParam.contentUri);
                    arrayList.add(bundle2);
                }
            }
            bundle.putParcelableArrayList("intentParams", arrayList);
        }
        bundle.putString(ResourceEntry.KEY_PACKAGE_NAME, this.packageName);
        bundle.putString("className", this.className);
        bundle.putString("action", this.action);
        bundle.putString("uri", this.uri);
        bundle.putString("mimeType", this.mimeType);
        bundle.putInt("flags", this.flags);
        SuggestParcelables$IntentType suggestParcelables$IntentType = this.intentType;
        if (suggestParcelables$IntentType == null) {
            bundle.putBundle("intentType", null);
        } else {
            Bundle bundle4 = new Bundle();
            bundle4.putInt("value", suggestParcelables$IntentType.value);
            bundle.putBundle("intentType", bundle4);
        }
        return bundle;
    }

    public SuggestParcelables$IntentInfo(Bundle bundle) {
        SuggestParcelables$IntentType suggestParcelables$IntentType = null;
        if (bundle.containsKey("intentParams")) {
            ArrayList parcelableArrayList = bundle.getParcelableArrayList("intentParams");
            if (parcelableArrayList == null) {
                this.intentParams = null;
            } else {
                this.intentParams = new ArrayList(parcelableArrayList.size());
                Iterator it = parcelableArrayList.iterator();
                while (it.hasNext()) {
                    Bundle bundle2 = (Bundle) it.next();
                    if (bundle2 == null) {
                        this.intentParams.add(null);
                    } else {
                        this.intentParams.add(new SuggestParcelables$IntentParam(bundle2));
                    }
                }
            }
        }
        if (bundle.containsKey(ResourceEntry.KEY_PACKAGE_NAME)) {
            this.packageName = bundle.getString(ResourceEntry.KEY_PACKAGE_NAME);
        }
        if (bundle.containsKey("className")) {
            this.className = bundle.getString("className");
        }
        if (bundle.containsKey("action")) {
            this.action = bundle.getString("action");
        }
        if (bundle.containsKey("uri")) {
            this.uri = bundle.getString("uri");
        }
        if (bundle.containsKey("mimeType")) {
            this.mimeType = bundle.getString("mimeType");
        }
        if (bundle.containsKey("flags")) {
            this.flags = bundle.getInt("flags");
        }
        if (bundle.containsKey("intentType")) {
            Bundle bundle3 = bundle.getBundle("intentType");
            if (bundle3 == null) {
                this.intentType = null;
                return;
            }
            int i = bundle3.getInt("value");
            if (i == 0) {
                suggestParcelables$IntentType = SuggestParcelables$IntentType.DEFAULT;
            } else if (i == 1) {
                suggestParcelables$IntentType = SuggestParcelables$IntentType.COPY_TEXT;
            } else if (i == 2) {
                suggestParcelables$IntentType = SuggestParcelables$IntentType.SHARE_IMAGE;
            } else if (i == 3) {
                suggestParcelables$IntentType = SuggestParcelables$IntentType.LENS;
            } else if (i == 4) {
                suggestParcelables$IntentType = SuggestParcelables$IntentType.SAVE;
            } else if (i == 5) {
                suggestParcelables$IntentType = SuggestParcelables$IntentType.COPY_IMAGE;
            } else if (i == 6) {
                suggestParcelables$IntentType = SuggestParcelables$IntentType.SMART_REC;
            }
            this.intentType = suggestParcelables$IntentType;
        }
    }
}
