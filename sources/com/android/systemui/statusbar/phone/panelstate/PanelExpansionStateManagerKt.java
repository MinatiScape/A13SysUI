package com.android.systemui.statusbar.phone.panelstate;

import kotlin.jvm.internal.Reflection;
/* compiled from: PanelExpansionStateManager.kt */
/* loaded from: classes.dex */
public final class PanelExpansionStateManagerKt {
    static {
        Reflection.getOrCreateKotlinClass(PanelExpansionStateManager.class).getSimpleName();
    }

    public static final String access$stateToString(int i) {
        if (i == 0) {
            return "CLOSED";
        }
        if (i == 1) {
            return "OPENING";
        }
        if (i != 2) {
            return String.valueOf(i);
        }
        return "OPEN";
    }
}
