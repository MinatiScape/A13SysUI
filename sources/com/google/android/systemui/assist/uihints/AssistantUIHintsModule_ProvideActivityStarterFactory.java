package com.google.android.systemui.assist.uihints;

import android.content.Intent;
import android.util.Log;
import com.android.systemui.statusbar.phone.StatusBar;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AssistantUIHintsModule_ProvideActivityStarterFactory implements Factory<NgaMessageHandler.StartActivityInfoListener> {
    public final Provider<StatusBar> statusBarLazyProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        final Lazy lazy = DoubleCheck.lazy(this.statusBarLazyProvider);
        return new NgaMessageHandler.StartActivityInfoListener() { // from class: com.google.android.systemui.assist.uihints.AssistantUIHintsModule$1
            @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.StartActivityInfoListener
            public final void onStartActivityInfo(Intent intent, boolean z) {
                if (intent == null) {
                    Log.e("ActivityStarter", "Null intent; cannot start activity");
                } else {
                    ((StatusBar) Lazy.this.get()).startActivity(intent, z);
                }
            }
        };
    }

    public AssistantUIHintsModule_ProvideActivityStarterFactory(Provider<StatusBar> provider) {
        this.statusBarLazyProvider = provider;
    }
}
