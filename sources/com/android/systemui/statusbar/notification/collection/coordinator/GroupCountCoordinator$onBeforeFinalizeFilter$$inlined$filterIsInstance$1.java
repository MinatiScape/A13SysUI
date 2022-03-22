package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.GroupEntry;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: _Sequences.kt */
/* loaded from: classes.dex */
public final class GroupCountCoordinator$onBeforeFinalizeFilter$$inlined$filterIsInstance$1 extends Lambda implements Function1<Object, Boolean> {
    public static final GroupCountCoordinator$onBeforeFinalizeFilter$$inlined$filterIsInstance$1 INSTANCE = new GroupCountCoordinator$onBeforeFinalizeFilter$$inlined$filterIsInstance$1();

    public GroupCountCoordinator$onBeforeFinalizeFilter$$inlined$filterIsInstance$1() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Boolean invoke(Object obj) {
        return Boolean.valueOf(obj instanceof GroupEntry);
    }
}
