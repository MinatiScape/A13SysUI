package com.android.systemui.statusbar.policy;

import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.SmartReplyController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SmartActionInflaterImpl_Factory implements Factory<SmartActionInflaterImpl> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<SmartReplyConstants> constantsProvider;
    public final Provider<HeadsUpManager> headsUpManagerProvider;
    public final Provider<SmartReplyController> smartReplyControllerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        this.headsUpManagerProvider.mo144get();
        return new SmartActionInflaterImpl(this.constantsProvider.mo144get(), this.activityStarterProvider.mo144get(), this.smartReplyControllerProvider.mo144get());
    }

    public SmartActionInflaterImpl_Factory(Provider<SmartReplyConstants> provider, Provider<ActivityStarter> provider2, Provider<SmartReplyController> provider3, Provider<HeadsUpManager> provider4) {
        this.constantsProvider = provider;
        this.activityStarterProvider = provider2;
        this.smartReplyControllerProvider = provider3;
        this.headsUpManagerProvider = provider4;
    }
}
