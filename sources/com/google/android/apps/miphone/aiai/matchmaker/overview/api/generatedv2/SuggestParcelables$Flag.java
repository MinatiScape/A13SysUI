package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
/* loaded from: classes.dex */
public final class SuggestParcelables$Flag {
    @Nullable
    public String name;
    @Nullable
    public String value;

    public SuggestParcelables$Flag(Bundle bundle) {
        if (bundle.containsKey("name")) {
            this.name = bundle.getString("name");
        }
        if (bundle.containsKey("value")) {
            this.value = bundle.getString("value");
        }
    }

    public SuggestParcelables$Flag() {
    }
}
