package com.google.android.systemui.assist.uihints;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.android.systemui.assist.AssistLogger;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.recents.Recents;
import com.android.systemui.recents.RecentsImplementation;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.LSShadeTransitionLogger;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class IconController_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider configurationControllerProvider;
    public final Provider inflaterProvider;
    public final Provider parentProvider;

    public /* synthetic */ IconController_Factory(Provider provider, Provider provider2, Provider provider3, int i) {
        this.$r8$classId = i;
        this.inflaterProvider = provider;
        this.parentProvider = provider2;
        this.configurationControllerProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                LayoutInflater layoutInflater = (LayoutInflater) this.inflaterProvider.mo144get();
                return new IconController((ViewGroup) this.parentProvider.mo144get(), (ConfigurationController) this.configurationControllerProvider.mo144get());
            case 1:
                return new LSShadeTransitionLogger((LogBuffer) this.inflaterProvider.mo144get(), (LockscreenGestureLogger) this.parentProvider.mo144get(), (DisplayMetrics) this.configurationControllerProvider.mo144get());
            case 2:
                return new EdgeLightsController((Context) this.inflaterProvider.mo144get(), (ViewGroup) this.parentProvider.mo144get(), (AssistLogger) this.configurationControllerProvider.mo144get());
            default:
                return new Recents((Context) this.inflaterProvider.mo144get(), (RecentsImplementation) this.parentProvider.mo144get(), (CommandQueue) this.configurationControllerProvider.mo144get());
        }
    }
}
