package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public final class SuggestParcelables$ActionGroup {
    @Nullable
    public ArrayList alternateActions;
    @Nullable
    public String displayName;
    @Nullable
    public String id;
    public boolean isHiddenAction;
    @Nullable
    public SuggestParcelables$Action mainAction;
    @Nullable
    public String opaquePayload;

    public SuggestParcelables$ActionGroup(Bundle bundle) {
        if (bundle.containsKey("id")) {
            this.id = bundle.getString("id");
        }
        if (bundle.containsKey("displayName")) {
            this.displayName = bundle.getString("displayName");
        }
        if (bundle.containsKey("mainAction")) {
            Bundle bundle2 = bundle.getBundle("mainAction");
            if (bundle2 == null) {
                this.mainAction = null;
            } else {
                this.mainAction = new SuggestParcelables$Action(bundle2);
            }
        }
        if (bundle.containsKey("alternateActions")) {
            ArrayList parcelableArrayList = bundle.getParcelableArrayList("alternateActions");
            if (parcelableArrayList == null) {
                this.alternateActions = null;
            } else {
                this.alternateActions = new ArrayList(parcelableArrayList.size());
                Iterator it = parcelableArrayList.iterator();
                while (it.hasNext()) {
                    Bundle bundle3 = (Bundle) it.next();
                    if (bundle3 == null) {
                        this.alternateActions.add(null);
                    } else {
                        this.alternateActions.add(new SuggestParcelables$Action(bundle3));
                    }
                }
            }
        }
        if (bundle.containsKey("isHiddenAction")) {
            this.isHiddenAction = bundle.getBoolean("isHiddenAction");
        }
        if (bundle.containsKey("opaquePayload")) {
            this.opaquePayload = bundle.getString("opaquePayload");
        }
    }

    public SuggestParcelables$ActionGroup() {
    }
}
