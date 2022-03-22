package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
/* loaded from: classes.dex */
public final class ContentParcelables$ExecutionInfo {
    @Nullable
    public String deeplinkUri;

    public ContentParcelables$ExecutionInfo(Bundle bundle) {
        if (bundle.containsKey("deeplinkUri")) {
            this.deeplinkUri = bundle.getString("deeplinkUri");
        }
    }
}
