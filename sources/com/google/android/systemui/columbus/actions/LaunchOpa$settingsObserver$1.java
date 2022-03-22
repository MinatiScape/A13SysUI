package com.google.android.systemui.columbus.actions;

import android.net.Uri;
import android.provider.Settings;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: LaunchOpa.kt */
/* loaded from: classes.dex */
public final class LaunchOpa$settingsObserver$1 extends Lambda implements Function1<Uri, Unit> {
    public final /* synthetic */ LaunchOpa this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public LaunchOpa$settingsObserver$1(LaunchOpa launchOpa) {
        super(1);
        this.this$0 = launchOpa;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Unit invoke(Uri uri) {
        boolean z;
        LaunchOpa launchOpa = this.this$0;
        Objects.requireNonNull(launchOpa);
        boolean z2 = true;
        if (Settings.Secure.getIntForUser(launchOpa.context.getContentResolver(), "assist_gesture_enabled", 1, -2) != 0) {
            z = true;
        } else {
            z = false;
        }
        launchOpa.isGestureEnabled = z;
        if (!z || !launchOpa.isOpaEnabled) {
            z2 = false;
        }
        launchOpa.setAvailable(z2);
        return Unit.INSTANCE;
    }
}
