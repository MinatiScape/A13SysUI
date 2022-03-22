package com.google.android.systemui.columbus;

import com.google.android.systemui.columbus.actions.LaunchApp;
import com.google.android.systemui.columbus.actions.LaunchOpa;
import com.google.android.systemui.columbus.actions.LaunchOverview;
import com.google.android.systemui.columbus.actions.ManageMedia;
import com.google.android.systemui.columbus.actions.OpenNotificationShade;
import com.google.android.systemui.columbus.actions.TakeScreenshot;
import com.google.android.systemui.columbus.actions.ToggleFlashlight;
import com.google.android.systemui.columbus.actions.UserAction;
import dagger.internal.Factory;
import java.util.Map;
import java.util.Objects;
import javax.inject.Provider;
import kotlin.Pair;
import kotlin.collections.MapsKt___MapsKt;
/* loaded from: classes.dex */
public final class ColumbusModule_ProvideUserSelectedActionsFactory implements Factory<Map<String, UserAction>> {
    public final Provider<LaunchApp> launchAppProvider;
    public final Provider<LaunchOpa> launchOpaProvider;
    public final Provider<LaunchOverview> launchOverviewProvider;
    public final Provider<ManageMedia> manageMediaProvider;
    public final Provider<OpenNotificationShade> openNotificationShadeProvider;
    public final Provider<TakeScreenshot> takeScreenshotProvider;
    public final Provider<ToggleFlashlight> toggleFlashlightProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        Map mapOf = MapsKt___MapsKt.mapOf(new Pair("assistant", this.launchOpaProvider.mo144get()), new Pair("media", this.manageMediaProvider.mo144get()), new Pair("screenshot", this.takeScreenshotProvider.mo144get()), new Pair("overview", this.launchOverviewProvider.mo144get()), new Pair("notifications", this.openNotificationShadeProvider.mo144get()), new Pair("launch", this.launchAppProvider.mo144get()), new Pair("flashlight", this.toggleFlashlightProvider.mo144get()));
        Objects.requireNonNull(mapOf, "Cannot return null from a non-@Nullable @Provides method");
        return mapOf;
    }

    public ColumbusModule_ProvideUserSelectedActionsFactory(Provider<LaunchOpa> provider, Provider<ManageMedia> provider2, Provider<TakeScreenshot> provider3, Provider<LaunchOverview> provider4, Provider<OpenNotificationShade> provider5, Provider<LaunchApp> provider6, Provider<ToggleFlashlight> provider7) {
        this.launchOpaProvider = provider;
        this.manageMediaProvider = provider2;
        this.takeScreenshotProvider = provider3;
        this.launchOverviewProvider = provider4;
        this.openNotificationShadeProvider = provider5;
        this.launchAppProvider = provider6;
        this.toggleFlashlightProvider = provider7;
    }
}
