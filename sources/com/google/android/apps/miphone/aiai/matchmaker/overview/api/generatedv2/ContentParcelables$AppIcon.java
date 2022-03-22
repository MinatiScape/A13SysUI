package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
/* loaded from: classes.dex */
public final class ContentParcelables$AppIcon {
    public ContentParcelables$AppIconType appIconType;
    @Nullable
    public ContentParcelables$AppPackage appPackage;
    @Nullable
    public String iconUri;

    public ContentParcelables$AppIcon(Bundle bundle) {
        if (bundle.containsKey("iconUri")) {
            this.iconUri = bundle.getString("iconUri");
        }
        ContentParcelables$AppIconType contentParcelables$AppIconType = null;
        if (bundle.containsKey("appPackage")) {
            Bundle bundle2 = bundle.getBundle("appPackage");
            if (bundle2 == null) {
                this.appPackage = null;
            } else {
                this.appPackage = new ContentParcelables$AppPackage(bundle2);
            }
        }
        if (bundle.containsKey("appIconType")) {
            Bundle bundle3 = bundle.getBundle("appIconType");
            if (bundle3 == null) {
                this.appIconType = null;
                return;
            }
            int i = bundle3.getInt("value");
            if (i == 0) {
                contentParcelables$AppIconType = ContentParcelables$AppIconType.UNDEFINED;
            } else if (i == 1) {
                contentParcelables$AppIconType = ContentParcelables$AppIconType.URI;
            } else if (i == 2) {
                contentParcelables$AppIconType = ContentParcelables$AppIconType.DRAWABLE;
            }
            this.appIconType = contentParcelables$AppIconType;
        }
    }
}
