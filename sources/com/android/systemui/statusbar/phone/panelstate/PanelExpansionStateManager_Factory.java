package com.android.systemui.statusbar.phone.panelstate;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class PanelExpansionStateManager_Factory implements Factory<PanelExpansionStateManager> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final PanelExpansionStateManager_Factory INSTANCE = new PanelExpansionStateManager_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new PanelExpansionStateManager();
    }
}
