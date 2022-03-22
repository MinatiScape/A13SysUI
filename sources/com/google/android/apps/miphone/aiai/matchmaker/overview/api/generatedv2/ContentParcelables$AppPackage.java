package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.google.android.setupcompat.partnerconfig.ResourceEntry;
/* loaded from: classes.dex */
public final class ContentParcelables$AppPackage {
    @Nullable
    public String packageName;

    public ContentParcelables$AppPackage(Bundle bundle) {
        if (bundle.containsKey(ResourceEntry.KEY_PACKAGE_NAME)) {
            this.packageName = bundle.getString(ResourceEntry.KEY_PACKAGE_NAME);
        }
    }
}
