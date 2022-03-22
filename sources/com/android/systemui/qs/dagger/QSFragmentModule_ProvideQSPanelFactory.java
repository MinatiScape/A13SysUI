package com.android.systemui.qs.dagger;

import android.content.Context;
import android.os.Looper;
import android.telecom.TelecomManager;
import android.view.View;
import com.android.keyguard.KeyguardSecurityContainer;
import com.android.keyguard.KeyguardSecurityViewFlipper;
import com.android.systemui.log.LogBufferFactory;
import com.android.systemui.qs.QSPanel;
import com.android.systemui.unfold.util.ATraceLoggerTransitionProgressListener;
import com.android.systemui.util.concurrency.ExecutorImpl;
import com.android.wm.shell.startingsurface.StartingWindowController;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSFragmentModule_ProvideQSPanelFactory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider viewProvider;

    public /* synthetic */ QSFragmentModule_ProvideQSPanelFactory(Provider provider, int i) {
        this.$r8$classId = i;
        this.viewProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                QSPanel qSPanel = (QSPanel) ((View) this.viewProvider.mo144get()).findViewById(2131428662);
                Objects.requireNonNull(qSPanel, "Cannot return null from a non-@Nullable @Provides method");
                return qSPanel;
            case 1:
                KeyguardSecurityViewFlipper keyguardSecurityViewFlipper = (KeyguardSecurityViewFlipper) ((KeyguardSecurityContainer) this.viewProvider.mo144get()).findViewById(2131429183);
                Objects.requireNonNull(keyguardSecurityViewFlipper, "Cannot return null from a non-@Nullable @Provides method");
                return keyguardSecurityViewFlipper;
            case 2:
                return (TelecomManager) ((Context) this.viewProvider.mo144get()).getSystemService(TelecomManager.class);
            case 3:
                return ((LogBufferFactory) this.viewProvider.mo144get()).create("NotifHeadsUpLog", 1000);
            case 4:
                return new ATraceLoggerTransitionProgressListener((String) this.viewProvider.mo144get());
            case 5:
                return new ExecutorImpl((Looper) this.viewProvider.mo144get());
            default:
                StartingWindowController startingWindowController = (StartingWindowController) this.viewProvider.mo144get();
                Objects.requireNonNull(startingWindowController);
                Optional of = Optional.of(startingWindowController.mImpl);
                Objects.requireNonNull(of, "Cannot return null from a non-@Nullable @Provides method");
                return of;
        }
    }
}
