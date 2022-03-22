package com.android.systemui.unfold.updates.hinge;

import androidx.core.util.Consumer;
/* compiled from: EmptyHingeAngleProvider.kt */
/* loaded from: classes.dex */
public final class EmptyHingeAngleProvider implements HingeAngleProvider {
    public static final EmptyHingeAngleProvider INSTANCE = new EmptyHingeAngleProvider();

    @Override // com.android.systemui.unfold.updates.hinge.HingeAngleProvider
    public final void start() {
    }

    @Override // com.android.systemui.unfold.updates.hinge.HingeAngleProvider
    public final void stop() {
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final /* bridge */ /* synthetic */ void addCallback(Consumer<Float> consumer) {
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final /* bridge */ /* synthetic */ void removeCallback(Consumer<Float> consumer) {
    }
}
