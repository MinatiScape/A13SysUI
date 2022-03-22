package com.google.android.systemui.assist.uihints;

import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import dagger.internal.Factory;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AssistantUIHintsModule_ProvideConfigInfoListenersFactory implements Factory<Set<NgaMessageHandler.ConfigInfoListener>> {
    public final Provider<AssistantPresenceHandler> assistantPresenceHandlerProvider;
    public final Provider<ColorChangeHandler> colorChangeHandlerProvider;
    public final Provider<ConfigurationHandler> configurationHandlerProvider;
    public final Provider<KeyboardMonitor> keyboardMonitorProvider;
    public final Provider<TaskStackNotifier> taskStackNotifierProvider;
    public final Provider<TouchInsideHandler> touchInsideHandlerProvider;
    public final Provider<TouchOutsideHandler> touchOutsideHandlerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new HashSet(Arrays.asList(this.assistantPresenceHandlerProvider.mo144get(), this.touchInsideHandlerProvider.mo144get(), this.touchOutsideHandlerProvider.mo144get(), this.taskStackNotifierProvider.mo144get(), this.keyboardMonitorProvider.mo144get(), this.colorChangeHandlerProvider.mo144get(), this.configurationHandlerProvider.mo144get()));
    }

    public AssistantUIHintsModule_ProvideConfigInfoListenersFactory(Provider<AssistantPresenceHandler> provider, Provider<TouchInsideHandler> provider2, Provider<TouchOutsideHandler> provider3, Provider<TaskStackNotifier> provider4, Provider<KeyboardMonitor> provider5, Provider<ColorChangeHandler> provider6, Provider<ConfigurationHandler> provider7) {
        this.assistantPresenceHandlerProvider = provider;
        this.touchInsideHandlerProvider = provider2;
        this.touchOutsideHandlerProvider = provider3;
        this.taskStackNotifierProvider = provider4;
        this.keyboardMonitorProvider = provider5;
        this.colorChangeHandlerProvider = provider6;
        this.configurationHandlerProvider = provider7;
    }
}
