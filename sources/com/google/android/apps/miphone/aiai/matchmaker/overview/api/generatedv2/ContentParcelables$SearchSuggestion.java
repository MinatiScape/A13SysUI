package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
/* loaded from: classes.dex */
public final class ContentParcelables$SearchSuggestion {
    @Nullable
    public ContentParcelables$AppActionSuggestion appActionSuggestion;
    @Nullable
    public ContentParcelables$AppIcon appIcon;
    public float confScore;
    @Nullable
    public ContentParcelables$ExecutionInfo executionInfo;

    public ContentParcelables$SearchSuggestion(Bundle bundle) {
        if (bundle.containsKey("appActionSuggestion")) {
            Bundle bundle2 = bundle.getBundle("appActionSuggestion");
            if (bundle2 == null) {
                this.appActionSuggestion = null;
            } else {
                this.appActionSuggestion = new ContentParcelables$AppActionSuggestion(bundle2);
            }
        }
        if (bundle.containsKey("appIcon")) {
            Bundle bundle3 = bundle.getBundle("appIcon");
            if (bundle3 == null) {
                this.appIcon = null;
            } else {
                this.appIcon = new ContentParcelables$AppIcon(bundle3);
            }
        }
        if (bundle.containsKey("executionInfo")) {
            Bundle bundle4 = bundle.getBundle("executionInfo");
            if (bundle4 == null) {
                this.executionInfo = null;
            } else {
                this.executionInfo = new ContentParcelables$ExecutionInfo(bundle4);
            }
        }
        if (bundle.containsKey("confScore")) {
            this.confScore = bundle.getFloat("confScore");
        }
    }

    public ContentParcelables$SearchSuggestion() {
    }
}
