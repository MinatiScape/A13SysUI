package com.android.systemui.qs.customize;

import android.content.Context;
import android.media.session.MediaSessionManager;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.media.dialog.MediaOutputDialogFactory;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSCustomizerController_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider configurationControllerProvider;
    public final Provider keyguardStateControllerProvider;
    public final Provider lightBarControllerProvider;
    public final Provider qsTileHostProvider;
    public final Provider screenLifecycleProvider;
    public final Provider tileAdapterProvider;
    public final Provider tileQueryHelperProvider;
    public final Provider uiEventLoggerProvider;
    public final Provider viewProvider;

    public /* synthetic */ QSCustomizerController_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, int i) {
        this.$r8$classId = i;
        this.viewProvider = provider;
        this.tileQueryHelperProvider = provider2;
        this.qsTileHostProvider = provider3;
        this.tileAdapterProvider = provider4;
        this.screenLifecycleProvider = provider5;
        this.keyguardStateControllerProvider = provider6;
        this.lightBarControllerProvider = provider7;
        this.configurationControllerProvider = provider8;
        this.uiEventLoggerProvider = provider9;
    }

    public static QSCustomizerController_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9) {
        return new QSCustomizerController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, 1);
    }

    public static QSCustomizerController_Factory create$1(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9) {
        return new QSCustomizerController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, 0);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new QSCustomizerController((QSCustomizer) this.viewProvider.mo144get(), (TileQueryHelper) this.tileQueryHelperProvider.mo144get(), (QSTileHost) this.qsTileHostProvider.mo144get(), (TileAdapter) this.tileAdapterProvider.mo144get(), (ScreenLifecycle) this.screenLifecycleProvider.mo144get(), (KeyguardStateController) this.keyguardStateControllerProvider.mo144get(), (LightBarController) this.lightBarControllerProvider.mo144get(), (ConfigurationController) this.configurationControllerProvider.mo144get(), (UiEventLogger) this.uiEventLoggerProvider.mo144get());
            default:
                return new MediaOutputDialogFactory((Context) this.viewProvider.mo144get(), (MediaSessionManager) this.tileQueryHelperProvider.mo144get(), (LocalBluetoothManager) this.qsTileHostProvider.mo144get(), (ShadeController) this.tileAdapterProvider.mo144get(), (ActivityStarter) this.screenLifecycleProvider.mo144get(), (CommonNotifCollection) this.keyguardStateControllerProvider.mo144get(), (UiEventLogger) this.lightBarControllerProvider.mo144get(), (DialogLaunchAnimator) this.configurationControllerProvider.mo144get(), (SystemUIDialogManager) this.uiEventLoggerProvider.mo144get());
        }
    }
}
