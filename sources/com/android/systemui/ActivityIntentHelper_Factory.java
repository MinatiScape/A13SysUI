package com.android.systemui;

import android.app.INotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.ServiceManager;
import android.view.View;
import com.android.systemui.dagger.DependencyProvider;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.log.LogBufferFactory;
import com.android.systemui.media.taptotransfer.MediaTttFlags;
import com.android.systemui.qs.customize.QSCustomizer;
import com.android.systemui.util.settings.GlobalSettingsImpl;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ActivityIntentHelper_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Object contextProvider;

    public /* synthetic */ ActivityIntentHelper_Factory(Object obj, int i) {
        this.$r8$classId = i;
        this.contextProvider = obj;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new ActivityIntentHelper((Context) ((Provider) this.contextProvider).mo144get());
            case 1:
                return (WifiManager) ((Context) ((Provider) this.contextProvider).mo144get()).getSystemService(WifiManager.class);
            case 2:
                return ((LogBufferFactory) ((Provider) this.contextProvider).mo144get()).create("ToastLog", 50);
            case 3:
                return new MediaTttFlags((FeatureFlags) ((Provider) this.contextProvider).mo144get());
            case 4:
                QSCustomizer qSCustomizer = (QSCustomizer) ((View) ((Provider) this.contextProvider).mo144get()).findViewById(2131428647);
                Objects.requireNonNull(qSCustomizer, "Cannot return null from a non-@Nullable @Provides method");
                return qSCustomizer;
            case 5:
                return new GlobalSettingsImpl((ContentResolver) ((Provider) this.contextProvider).mo144get());
            default:
                Objects.requireNonNull((DependencyProvider) this.contextProvider);
                INotificationManager asInterface = INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
                Objects.requireNonNull(asInterface, "Cannot return null from a non-@Nullable @Provides method");
                return asInterface;
        }
    }
}
