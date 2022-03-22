package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
/* loaded from: classes.dex */
public final class ContentParcelables$AppActionSuggestion {
    @Nullable
    public String displayText;
    @Nullable
    public String subtitle;

    public ContentParcelables$AppActionSuggestion(Bundle bundle) {
        if (bundle.containsKey("displayText")) {
            this.displayText = bundle.getString("displayText");
        }
        if (bundle.containsKey("subtitle")) {
            this.subtitle = bundle.getString("subtitle");
        }
    }
}
