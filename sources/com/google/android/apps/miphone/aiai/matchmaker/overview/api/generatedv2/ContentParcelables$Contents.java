package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public final class ContentParcelables$Contents {
    @Nullable
    public ArrayList contentGroups;
    @Nullable
    public String contentUri;
    @Nullable
    public SuggestParcelables$DebugInfo debugInfo;
    @Nullable
    public String id;
    @Nullable
    public String opaquePayload;
    public long screenSessionId;
    @Nullable
    public SuggestParcelables$SetupInfo setupInfo;
    @Nullable
    public SuggestParcelables$Stats stats;

    public ContentParcelables$Contents(Bundle bundle) {
        if (bundle.containsKey("id")) {
            this.id = bundle.getString("id");
        }
        if (bundle.containsKey("screenSessionId")) {
            this.screenSessionId = bundle.getLong("screenSessionId");
        }
        if (bundle.containsKey("contentGroups")) {
            ArrayList parcelableArrayList = bundle.getParcelableArrayList("contentGroups");
            if (parcelableArrayList == null) {
                this.contentGroups = null;
            } else {
                this.contentGroups = new ArrayList(parcelableArrayList.size());
                Iterator it = parcelableArrayList.iterator();
                while (it.hasNext()) {
                    Bundle bundle2 = (Bundle) it.next();
                    if (bundle2 == null) {
                        this.contentGroups.add(null);
                    } else {
                        this.contentGroups.add(new ContentParcelables$ContentGroup(bundle2));
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
        if (bundle.containsKey("opaquePayload")) {
            this.opaquePayload = bundle.getString("opaquePayload");
        }
        if (bundle.containsKey("setupInfo")) {
            Bundle bundle4 = bundle.getBundle("setupInfo");
            if (bundle4 == null) {
                this.setupInfo = null;
            } else {
                this.setupInfo = new SuggestParcelables$SetupInfo(bundle4);
            }
        }
        if (bundle.containsKey("contentUri")) {
            this.contentUri = bundle.getString("contentUri");
        }
    }

    public ContentParcelables$Contents() {
    }
}
