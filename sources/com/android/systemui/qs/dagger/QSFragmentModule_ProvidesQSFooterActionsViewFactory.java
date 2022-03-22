package com.android.systemui.qs.dagger;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Handler;
import android.os.UserHandle;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import com.android.settingslib.bluetooth.LocalBluetoothAdapter;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.qs.FooterActionsView;
import com.android.systemui.statusbar.phone.StatusBarMoveFromCenterAnimationController;
import com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSFragmentModule_ProvidesQSFooterActionsViewFactory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider featureFlagsProvider;
    public final Provider viewProvider;

    public /* synthetic */ QSFragmentModule_ProvidesQSFooterActionsViewFactory(Provider provider, Provider provider2, int i) {
        this.$r8$classId = i;
        this.viewProvider = provider;
        this.featureFlagsProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        ViewStub viewStub;
        LocalBluetoothAdapter localBluetoothAdapter;
        BluetoothAdapter defaultAdapter;
        switch (this.$r8$classId) {
            case 0:
                View view = (View) this.viewProvider.mo144get();
                if (((FeatureFlags) this.featureFlagsProvider.mo144get()).isEnabled(Flags.NEW_FOOTER)) {
                    viewStub = (ViewStub) view.requireViewById(2131427736);
                } else {
                    viewStub = (ViewStub) view.requireViewById(2131427985);
                }
                viewStub.inflate();
                FooterActionsView footerActionsView = (FooterActionsView) view.findViewById(2131428650);
                Objects.requireNonNull(footerActionsView, "Cannot return null from a non-@Nullable @Provides method");
                return footerActionsView;
            case 1:
                Context context = (Context) this.viewProvider.mo144get();
                Handler handler = (Handler) this.featureFlagsProvider.mo144get();
                UserHandle userHandle = UserHandle.ALL;
                synchronized (LocalBluetoothAdapter.class) {
                    if (LocalBluetoothAdapter.sInstance == null && (defaultAdapter = BluetoothAdapter.getDefaultAdapter()) != null) {
                        LocalBluetoothAdapter.sInstance = new LocalBluetoothAdapter(defaultAdapter);
                    }
                    localBluetoothAdapter = LocalBluetoothAdapter.sInstance;
                }
                if (localBluetoothAdapter == null) {
                    return null;
                }
                return new LocalBluetoothManager(localBluetoothAdapter, context, handler, userHandle);
            default:
                return new StatusBarMoveFromCenterAnimationController((ScopedUnfoldTransitionProgressProvider) this.viewProvider.mo144get(), (WindowManager) this.featureFlagsProvider.mo144get());
        }
    }
}
