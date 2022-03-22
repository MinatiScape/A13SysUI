package com.android.systemui.statusbar.policy;

import com.android.systemui.plugins.IntentButtonProvider;
import com.android.systemui.statusbar.policy.ExtensionControllerImpl;
/* loaded from: classes.dex */
public interface ExtensionController {

    /* loaded from: classes.dex */
    public interface Extension<T> {
    }

    /* loaded from: classes.dex */
    public interface PluginConverter<T, P> {
        IntentButtonProvider.IntentButton getInterfaceFromPlugin(Object obj);
    }

    /* loaded from: classes.dex */
    public interface TunerFactory<T> {
    }

    ExtensionControllerImpl.ExtensionBuilder newExtension();
}
