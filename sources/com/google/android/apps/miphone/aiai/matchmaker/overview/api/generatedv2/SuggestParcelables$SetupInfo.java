package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public final class SuggestParcelables$SetupInfo {
    public SuggestParcelables$ErrorCode errorCode;
    @Nullable
    public String errorMesssage;
    @Nullable
    public ArrayList setupFlags;

    public final Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        SuggestParcelables$ErrorCode suggestParcelables$ErrorCode = this.errorCode;
        if (suggestParcelables$ErrorCode == null) {
            bundle.putBundle("errorCode", null);
        } else {
            Bundle bundle2 = new Bundle();
            bundle2.putInt("value", suggestParcelables$ErrorCode.value);
            bundle.putBundle("errorCode", bundle2);
        }
        bundle.putString("errorMesssage", this.errorMesssage);
        if (this.setupFlags == null) {
            bundle.putParcelableArrayList("setupFlags", null);
        } else {
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>(this.setupFlags.size());
            Iterator it = this.setupFlags.iterator();
            while (it.hasNext()) {
                SuggestParcelables$Flag suggestParcelables$Flag = (SuggestParcelables$Flag) it.next();
                if (suggestParcelables$Flag == null) {
                    arrayList.add(null);
                } else {
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("name", suggestParcelables$Flag.name);
                    bundle3.putString("value", suggestParcelables$Flag.value);
                    arrayList.add(bundle3);
                }
            }
            bundle.putParcelableArrayList("setupFlags", arrayList);
        }
        return bundle;
    }

    public SuggestParcelables$SetupInfo(Bundle bundle) {
        SuggestParcelables$ErrorCode suggestParcelables$ErrorCode;
        if (bundle.containsKey("errorCode")) {
            Bundle bundle2 = bundle.getBundle("errorCode");
            if (bundle2 == null) {
                this.errorCode = null;
            } else {
                int i = bundle2.getInt("value");
                if (i == 0) {
                    suggestParcelables$ErrorCode = SuggestParcelables$ErrorCode.ERROR_CODE_SUCCESS;
                } else if (i == 1) {
                    suggestParcelables$ErrorCode = SuggestParcelables$ErrorCode.ERROR_CODE_UNKNOWN_ERROR;
                } else if (i == 2) {
                    suggestParcelables$ErrorCode = SuggestParcelables$ErrorCode.ERROR_CODE_TIMEOUT;
                } else if (i == 3) {
                    suggestParcelables$ErrorCode = SuggestParcelables$ErrorCode.ERROR_CODE_NO_SCREEN_CONTENT;
                } else if (i == 4) {
                    suggestParcelables$ErrorCode = SuggestParcelables$ErrorCode.ERROR_CODE_NO_SUPPORTED_LOCALES;
                } else {
                    suggestParcelables$ErrorCode = null;
                }
                this.errorCode = suggestParcelables$ErrorCode;
            }
        }
        if (bundle.containsKey("errorMesssage")) {
            this.errorMesssage = bundle.getString("errorMesssage");
        }
        if (bundle.containsKey("setupFlags")) {
            ArrayList parcelableArrayList = bundle.getParcelableArrayList("setupFlags");
            if (parcelableArrayList == null) {
                this.setupFlags = null;
                return;
            }
            this.setupFlags = new ArrayList(parcelableArrayList.size());
            Iterator it = parcelableArrayList.iterator();
            while (it.hasNext()) {
                Bundle bundle3 = (Bundle) it.next();
                if (bundle3 == null) {
                    this.setupFlags.add(null);
                } else {
                    this.setupFlags.add(new SuggestParcelables$Flag(bundle3));
                }
            }
        }
    }
}
