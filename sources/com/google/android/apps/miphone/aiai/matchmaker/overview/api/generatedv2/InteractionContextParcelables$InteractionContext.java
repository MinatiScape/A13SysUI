package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
/* loaded from: classes.dex */
public final class InteractionContextParcelables$InteractionContext {
    public boolean disallowCopyPaste;
    public SuggestParcelables$InteractionType interactionType;
    public boolean isPrimaryTask;
    public int versionCode;

    public final Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putLong("screenSessionId", 0L);
        bundle.putBundle("focusRect", null);
        bundle.putBoolean("expandFocusRect", false);
        bundle.putInt("focusRectExpandPx", 0);
        bundle.putBundle("previousContents", null);
        bundle.putBoolean("requestStats", false);
        bundle.putBoolean("requestDebugInfo", false);
        bundle.putBoolean("isRtlContent", false);
        bundle.putBoolean("disallowCopyPaste", this.disallowCopyPaste);
        bundle.putInt("versionCode", this.versionCode);
        bundle.putParcelableArrayList("interactionEvents", null);
        SuggestParcelables$InteractionType suggestParcelables$InteractionType = this.interactionType;
        if (suggestParcelables$InteractionType == null) {
            bundle.putBundle("interactionType", null);
        } else {
            Bundle bundle2 = new Bundle();
            bundle2.putInt("value", suggestParcelables$InteractionType.value);
            bundle.putBundle("interactionType", bundle2);
        }
        bundle.putBoolean("isPrimaryTask", this.isPrimaryTask);
        return bundle;
    }
}
