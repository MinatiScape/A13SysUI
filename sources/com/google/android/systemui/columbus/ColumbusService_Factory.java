package com.google.android.systemui.columbus;

import com.google.android.systemui.columbus.actions.Action;
import com.google.android.systemui.columbus.feedback.FeedbackEffect;
import com.google.android.systemui.columbus.gates.Gate;
import com.google.android.systemui.columbus.sensors.GestureController;
import dagger.internal.Factory;
import java.util.List;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ColumbusService_Factory implements Factory<ColumbusService> {
    public final Provider<List<Action>> actionsProvider;
    public final Provider<Set<FeedbackEffect>> effectsProvider;
    public final Provider<Set<Gate>> gatesProvider;
    public final Provider<GestureController> gestureControllerProvider;
    public final Provider<PowerManagerWrapper> powerManagerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ColumbusService(this.actionsProvider.mo144get(), this.effectsProvider.mo144get(), this.gatesProvider.mo144get(), this.gestureControllerProvider.mo144get(), this.powerManagerProvider.mo144get());
    }

    public ColumbusService_Factory(Provider<List<Action>> provider, Provider<Set<FeedbackEffect>> provider2, Provider<Set<Gate>> provider3, Provider<GestureController> provider4, Provider<PowerManagerWrapper> provider5) {
        this.actionsProvider = provider;
        this.effectsProvider = provider2;
        this.gatesProvider = provider3;
        this.gestureControllerProvider = provider4;
        this.powerManagerProvider = provider5;
    }
}
