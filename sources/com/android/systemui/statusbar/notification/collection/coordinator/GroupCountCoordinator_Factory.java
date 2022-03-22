package com.android.systemui.statusbar.notification.collection.coordinator;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class GroupCountCoordinator_Factory implements Factory<GroupCountCoordinator> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final GroupCountCoordinator_Factory INSTANCE = new GroupCountCoordinator_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new GroupCountCoordinator();
    }
}
