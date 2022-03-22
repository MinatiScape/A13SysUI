package com.android.systemui.power;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import com.android.systemui.keyguard.LifecycleScreenStatusProvider;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.unfold.UnfoldTransitionModule;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PowerNotificationWarnings_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Object activityStarterProvider;
    public final Provider contextProvider;

    public /* synthetic */ PowerNotificationWarnings_Factory(Provider provider, Provider provider2, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.activityStarterProvider = provider2;
    }

    public PowerNotificationWarnings_Factory(UnfoldTransitionModule unfoldTransitionModule, Provider provider) {
        this.$r8$classId = 2;
        this.activityStarterProvider = unfoldTransitionModule;
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new PowerNotificationWarnings((Context) this.contextProvider.mo144get(), (ActivityStarter) ((Provider) this.activityStarterProvider).mo144get());
            case 1:
                Context context = (Context) this.contextProvider.mo144get();
                Notification build = new Notification.Builder(context, "ALR").setSmallIcon(2131232259).setContentTitle(context.getString(2131952310)).setContentText(context.getString(2131952309)).setContentIntent((PendingIntent) ((Provider) this.activityStarterProvider).mo144get()).setAutoCancel(true).build();
                Objects.requireNonNull(build, "Cannot return null from a non-@Nullable @Provides method");
                return build;
            default:
                LifecycleScreenStatusProvider lifecycleScreenStatusProvider = (LifecycleScreenStatusProvider) this.contextProvider.mo144get();
                Objects.requireNonNull((UnfoldTransitionModule) this.activityStarterProvider);
                return lifecycleScreenStatusProvider;
        }
    }
}
