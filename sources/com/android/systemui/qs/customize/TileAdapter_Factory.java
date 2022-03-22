package com.android.systemui.qs.customize;

import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.text.TextUtils;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.util.sensors.ProximitySensor;
import com.google.android.systemui.columbus.ColumbusSettings;
import com.google.android.systemui.columbus.gates.FlagEnabled;
import com.google.android.systemui.gamedashboard.GameModeDndController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TileAdapter_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;
    public final Provider qsHostProvider;
    public final Provider uiEventLoggerProvider;

    public /* synthetic */ TileAdapter_Factory(Provider provider, Provider provider2, Provider provider3, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.qsHostProvider = provider2;
        this.uiEventLoggerProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        ProximitySensor proximitySensor;
        switch (this.$r8$classId) {
            case 0:
                return new TileAdapter((Context) this.contextProvider.mo144get(), (QSTileHost) this.qsHostProvider.mo144get(), (UiEventLogger) this.uiEventLoggerProvider.mo144get());
            case 1:
                Lazy lazy = DoubleCheck.lazy(this.qsHostProvider);
                Lazy lazy2 = DoubleCheck.lazy(this.uiEventLoggerProvider);
                String[] stringArray = ((Resources) this.contextProvider.mo144get()).getStringArray(2130903122);
                boolean z = false;
                if (!(stringArray == null || stringArray.length == 0)) {
                    int length = stringArray.length;
                    int i = 0;
                    while (true) {
                        if (i < length) {
                            if (!TextUtils.isEmpty(stringArray[i])) {
                                z = true;
                            } else {
                                i++;
                            }
                        }
                    }
                }
                if (z) {
                    proximitySensor = (ProximitySensor) lazy.get();
                } else {
                    proximitySensor = (ProximitySensor) lazy2.get();
                }
                Objects.requireNonNull(proximitySensor, "Cannot return null from a non-@Nullable @Provides method");
                return proximitySensor;
            case 2:
                return new FlagEnabled((Context) this.contextProvider.mo144get(), (ColumbusSettings) this.qsHostProvider.mo144get(), (Handler) this.uiEventLoggerProvider.mo144get());
            default:
                return new GameModeDndController((Context) this.contextProvider.mo144get(), (NotificationManager) this.qsHostProvider.mo144get(), (BroadcastDispatcher) this.uiEventLoggerProvider.mo144get());
        }
    }
}
