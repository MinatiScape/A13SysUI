package com.google.android.systemui.assist.uihints;

import android.content.Context;
import com.android.systemui.assist.AssistLogger;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsController;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NgaUiController_Factory implements Factory<NgaUiController> {
    public final Provider<AssistLogger> assistLoggerProvider;
    public final Provider<AssistManager> assistManagerProvider;
    public final Provider<AssistantPresenceHandler> assistantPresenceHandlerProvider;
    public final Provider<AssistantWarmer> assistantWarmerProvider;
    public final Provider<ColorChangeHandler> colorChangeHandlerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<EdgeLightsController> edgeLightsControllerProvider;
    public final Provider<FlingVelocityWrapper> flingVelocityProvider;
    public final Provider<GlowController> glowControllerProvider;
    public final Provider<IconController> iconControllerProvider;
    public final Provider<LightnessProvider> lightnessProvider;
    public final Provider<NavBarFader> navBarFaderProvider;
    public final Provider<ScrimController> scrimControllerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<TimeoutManager> timeoutManagerProvider;
    public final Provider<TouchInsideHandler> touchInsideHandlerProvider;
    public final Provider<TranscriptionController> transcriptionControllerProvider;
    public final Provider<OverlayUiHost> uiHostProvider;

    public NgaUiController_Factory(Provider<Context> provider, Provider<TimeoutManager> provider2, Provider<AssistantPresenceHandler> provider3, Provider<TouchInsideHandler> provider4, Provider<ColorChangeHandler> provider5, Provider<OverlayUiHost> provider6, Provider<EdgeLightsController> provider7, Provider<GlowController> provider8, Provider<ScrimController> provider9, Provider<TranscriptionController> provider10, Provider<IconController> provider11, Provider<LightnessProvider> provider12, Provider<StatusBarStateController> provider13, Provider<AssistManager> provider14, Provider<FlingVelocityWrapper> provider15, Provider<AssistantWarmer> provider16, Provider<NavBarFader> provider17, Provider<AssistLogger> provider18) {
        this.contextProvider = provider;
        this.timeoutManagerProvider = provider2;
        this.assistantPresenceHandlerProvider = provider3;
        this.touchInsideHandlerProvider = provider4;
        this.colorChangeHandlerProvider = provider5;
        this.uiHostProvider = provider6;
        this.edgeLightsControllerProvider = provider7;
        this.glowControllerProvider = provider8;
        this.scrimControllerProvider = provider9;
        this.transcriptionControllerProvider = provider10;
        this.iconControllerProvider = provider11;
        this.lightnessProvider = provider12;
        this.statusBarStateControllerProvider = provider13;
        this.assistManagerProvider = provider14;
        this.flingVelocityProvider = provider15;
        this.assistantWarmerProvider = provider16;
        this.navBarFaderProvider = provider17;
        this.assistLoggerProvider = provider18;
    }

    public static NgaUiController_Factory create(Provider<Context> provider, Provider<TimeoutManager> provider2, Provider<AssistantPresenceHandler> provider3, Provider<TouchInsideHandler> provider4, Provider<ColorChangeHandler> provider5, Provider<OverlayUiHost> provider6, Provider<EdgeLightsController> provider7, Provider<GlowController> provider8, Provider<ScrimController> provider9, Provider<TranscriptionController> provider10, Provider<IconController> provider11, Provider<LightnessProvider> provider12, Provider<StatusBarStateController> provider13, Provider<AssistManager> provider14, Provider<FlingVelocityWrapper> provider15, Provider<AssistantWarmer> provider16, Provider<NavBarFader> provider17, Provider<AssistLogger> provider18) {
        return new NgaUiController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        Context context = this.contextProvider.mo144get();
        TimeoutManager timeoutManager = this.timeoutManagerProvider.mo144get();
        AssistantPresenceHandler assistantPresenceHandler = this.assistantPresenceHandlerProvider.mo144get();
        TouchInsideHandler touchInsideHandler = this.touchInsideHandlerProvider.mo144get();
        ColorChangeHandler colorChangeHandler = this.colorChangeHandlerProvider.mo144get();
        OverlayUiHost overlayUiHost = this.uiHostProvider.mo144get();
        EdgeLightsController edgeLightsController = this.edgeLightsControllerProvider.mo144get();
        GlowController glowController = this.glowControllerProvider.mo144get();
        ScrimController scrimController = this.scrimControllerProvider.mo144get();
        TranscriptionController transcriptionController = this.transcriptionControllerProvider.mo144get();
        IconController iconController = this.iconControllerProvider.mo144get();
        LightnessProvider lightnessProvider = this.lightnessProvider.mo144get();
        return new NgaUiController(context, timeoutManager, assistantPresenceHandler, touchInsideHandler, colorChangeHandler, overlayUiHost, edgeLightsController, glowController, scrimController, transcriptionController, iconController, lightnessProvider, this.statusBarStateControllerProvider.mo144get(), DoubleCheck.lazy(this.assistManagerProvider), this.flingVelocityProvider.mo144get(), this.assistantWarmerProvider.mo144get(), this.navBarFaderProvider.mo144get(), this.assistLoggerProvider.mo144get());
    }
}
