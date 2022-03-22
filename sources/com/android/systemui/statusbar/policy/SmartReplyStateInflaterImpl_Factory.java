package com.android.systemui.statusbar.policy;

import android.content.Context;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.DevicePolicyManagerWrapper;
import com.android.systemui.shared.system.PackageManagerWrapper;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.compatui.CompatUIController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SmartReplyStateInflaterImpl_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider activityManagerWrapperProvider;
    public final Provider constantsProvider;
    public final Provider devicePolicyManagerWrapperProvider;
    public final Provider packageManagerWrapperProvider;
    public final Provider smartActionsInflaterProvider;
    public final Provider smartRepliesInflaterProvider;

    public /* synthetic */ SmartReplyStateInflaterImpl_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, int i) {
        this.$r8$classId = i;
        this.constantsProvider = provider;
        this.activityManagerWrapperProvider = provider2;
        this.packageManagerWrapperProvider = provider3;
        this.devicePolicyManagerWrapperProvider = provider4;
        this.smartRepliesInflaterProvider = provider5;
        this.smartActionsInflaterProvider = provider6;
    }

    public static SmartReplyStateInflaterImpl_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, SmartActionInflaterImpl_Factory smartActionInflaterImpl_Factory) {
        return new SmartReplyStateInflaterImpl_Factory(provider, provider2, provider3, provider4, provider5, smartActionInflaterImpl_Factory, 0);
    }

    public static SmartReplyStateInflaterImpl_Factory create$1(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6) {
        return new SmartReplyStateInflaterImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, 1);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new SmartReplyStateInflaterImpl((SmartReplyConstants) this.constantsProvider.mo144get(), (ActivityManagerWrapper) this.activityManagerWrapperProvider.mo144get(), (PackageManagerWrapper) this.packageManagerWrapperProvider.mo144get(), (DevicePolicyManagerWrapper) this.devicePolicyManagerWrapperProvider.mo144get(), (SmartReplyInflater) this.smartRepliesInflaterProvider.mo144get(), (SmartActionInflater) this.smartActionsInflaterProvider.mo144get());
            default:
                return new CompatUIController((Context) this.constantsProvider.mo144get(), (DisplayController) this.activityManagerWrapperProvider.mo144get(), (DisplayInsetsController) this.packageManagerWrapperProvider.mo144get(), (DisplayImeController) this.devicePolicyManagerWrapperProvider.mo144get(), (SyncTransactionQueue) this.smartRepliesInflaterProvider.mo144get(), (ShellExecutor) this.smartActionsInflaterProvider.mo144get());
        }
    }
}
