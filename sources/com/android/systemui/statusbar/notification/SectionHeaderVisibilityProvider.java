package com.android.systemui.statusbar.notification;

import android.content.Context;
/* compiled from: SectionHeaderVisibilityProvider.kt */
/* loaded from: classes.dex */
public final class SectionHeaderVisibilityProvider {
    public boolean neverShowSectionHeaders;
    public boolean sectionHeadersVisible = true;

    public SectionHeaderVisibilityProvider(Context context) {
        this.neverShowSectionHeaders = context.getResources().getBoolean(2131034147);
    }
}
