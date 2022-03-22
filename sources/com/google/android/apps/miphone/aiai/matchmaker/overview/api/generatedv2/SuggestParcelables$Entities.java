package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public final class SuggestParcelables$Entities {
    @Nullable
    public SuggestParcelables$DebugInfo debugInfo;
    @Nullable
    public ArrayList entities;
    @Nullable
    public SuggestParcelables$ExtrasInfo extrasInfo;
    @Nullable
    public String id;
    @Nullable
    public String opaquePayload;
    @Nullable
    public SuggestParcelables$SetupInfo setupInfo;
    @Nullable
    public SuggestParcelables$Stats stats;
    public boolean success;

    public SuggestParcelables$Entities(Bundle bundle) {
        if (bundle.containsKey("id")) {
            this.id = bundle.getString("id");
        }
        if (bundle.containsKey("success")) {
            this.success = bundle.getBoolean("success");
        }
        if (bundle.containsKey("entities")) {
            ArrayList parcelableArrayList = bundle.getParcelableArrayList("entities");
            if (parcelableArrayList == null) {
                this.entities = null;
            } else {
                this.entities = new ArrayList(parcelableArrayList.size());
                Iterator it = parcelableArrayList.iterator();
                while (it.hasNext()) {
                    Bundle bundle2 = (Bundle) it.next();
                    if (bundle2 == null) {
                        this.entities.add(null);
                    } else {
                        this.entities.add(new SuggestParcelables$Entity(bundle2));
                    }
                }
            }
        }
        if (bundle.containsKey("stats")) {
            Bundle bundle3 = bundle.getBundle("stats");
            if (bundle3 == null) {
                this.stats = null;
            } else {
                this.stats = new SuggestParcelables$Stats(bundle3);
            }
        }
        if (bundle.containsKey("debugInfo")) {
            if (bundle.getBundle("debugInfo") == null) {
                this.debugInfo = null;
            } else {
                this.debugInfo = new SuggestParcelables$DebugInfo();
            }
        }
        if (bundle.containsKey("extrasInfo")) {
            Bundle bundle4 = bundle.getBundle("extrasInfo");
            if (bundle4 == null) {
                this.extrasInfo = null;
            } else {
                this.extrasInfo = new SuggestParcelables$ExtrasInfo(bundle4);
            }
        }
        if (bundle.containsKey("opaquePayload")) {
            this.opaquePayload = bundle.getString("opaquePayload");
        }
        if (bundle.containsKey("setupInfo")) {
            Bundle bundle5 = bundle.getBundle("setupInfo");
            if (bundle5 == null) {
                this.setupInfo = null;
            } else {
                this.setupInfo = new SuggestParcelables$SetupInfo(bundle5);
            }
        }
    }

    public SuggestParcelables$Entities() {
    }
}
