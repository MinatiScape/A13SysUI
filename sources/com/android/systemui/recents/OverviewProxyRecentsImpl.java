package com.android.systemui.recents;

import android.app.trust.TrustManager;
import android.content.Context;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import com.android.systemui.Dependency;
import com.android.systemui.keyguard.KeyguardViewMediator$9$$ExternalSyntheticLambda0;
import com.android.systemui.shared.recents.IOverviewProxy;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda19;
import dagger.Lazy;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public class OverviewProxyRecentsImpl implements RecentsImplementation {
    public Handler mHandler;
    public OverviewProxyService mOverviewProxyService;
    public final Lazy<Optional<StatusBar>> mStatusBarOptionalLazy;

    @Override // com.android.systemui.recents.RecentsImplementation
    public final void hideRecentApps(boolean z, boolean z2) {
        OverviewProxyService overviewProxyService = this.mOverviewProxyService;
        Objects.requireNonNull(overviewProxyService);
        IOverviewProxy iOverviewProxy = overviewProxyService.mOverviewProxy;
        if (iOverviewProxy != null) {
            try {
                iOverviewProxy.onOverviewHidden(z, z2);
            } catch (RemoteException e) {
                Log.e("OverviewProxyRecentsImpl", "Failed to send overview hide event to launcher.", e);
            }
        }
    }

    @Override // com.android.systemui.recents.RecentsImplementation
    public final void onStart(Context context) {
        this.mHandler = new Handler();
        TrustManager trustManager = (TrustManager) context.getSystemService("trust");
        this.mOverviewProxyService = (OverviewProxyService) Dependency.get(OverviewProxyService.class);
    }

    @Override // com.android.systemui.recents.RecentsImplementation
    public final void showRecentApps(boolean z) {
        OverviewProxyService overviewProxyService = this.mOverviewProxyService;
        Objects.requireNonNull(overviewProxyService);
        IOverviewProxy iOverviewProxy = overviewProxyService.mOverviewProxy;
        if (iOverviewProxy != null) {
            try {
                iOverviewProxy.onOverviewShown(z);
            } catch (RemoteException e) {
                Log.e("OverviewProxyRecentsImpl", "Failed to send overview show event to launcher.", e);
            }
        }
    }

    @Override // com.android.systemui.recents.RecentsImplementation
    public final void toggleRecentApps() {
        OverviewProxyService overviewProxyService = this.mOverviewProxyService;
        Objects.requireNonNull(overviewProxyService);
        if (overviewProxyService.mOverviewProxy != null) {
            StatusBar$$ExternalSyntheticLambda19 statusBar$$ExternalSyntheticLambda19 = new StatusBar$$ExternalSyntheticLambda19(this, 3);
            Optional<StatusBar> optional = this.mStatusBarOptionalLazy.get();
            if (((Boolean) optional.map(OverviewProxyRecentsImpl$$ExternalSyntheticLambda0.INSTANCE).orElse(Boolean.FALSE)).booleanValue()) {
                optional.get().executeRunnableDismissingKeyguard(new KeyguardViewMediator$9$$ExternalSyntheticLambda0(this, statusBar$$ExternalSyntheticLambda19, 1), true, false, true);
            } else {
                statusBar$$ExternalSyntheticLambda19.run();
            }
        }
    }

    public OverviewProxyRecentsImpl(Lazy<Optional<StatusBar>> lazy) {
        this.mStatusBarOptionalLazy = lazy;
    }
}
