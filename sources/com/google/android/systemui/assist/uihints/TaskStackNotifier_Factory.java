package com.google.android.systemui.assist.uihints;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class TaskStackNotifier_Factory implements Factory<TaskStackNotifier> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final TaskStackNotifier_Factory INSTANCE = new TaskStackNotifier_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new TaskStackNotifier();
    }
}
