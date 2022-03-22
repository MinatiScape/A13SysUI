package com.android.systemui.statusbar.notification.collection.coordinator;

import android.util.Log;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: RemoteInputCoordinator.kt */
/* loaded from: classes.dex */
public final class RemoteInputCoordinatorKt$DEBUG$2 extends Lambda implements Function0<Boolean> {
    public static final RemoteInputCoordinatorKt$DEBUG$2 INSTANCE = new RemoteInputCoordinatorKt$DEBUG$2();

    public RemoteInputCoordinatorKt$DEBUG$2() {
        super(0);
    }

    @Override // kotlin.jvm.functions.Function0
    public final Boolean invoke() {
        return Boolean.valueOf(Log.isLoggable("RemoteInputCoordinator", 3));
    }
}
