package com.android.systemui.qs.dagger;

import android.view.LayoutInflater;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import com.android.internal.util.Preconditions;
import com.android.systemui.biometrics.AuthRippleView;
import com.android.systemui.communal.conditions.CommunalSettingCondition;
import com.android.systemui.dagger.DependencyProvider;
import com.android.systemui.dreams.DreamOverlayContainerView;
import com.android.systemui.qs.QuickQSPanel;
import com.android.systemui.qs.QuickStatusBarHeader;
import com.android.systemui.shared.system.TaskStackChangeListeners;
import com.android.systemui.statusbar.notification.DynamicChildBindController;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import dagger.internal.Factory;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSFragmentModule_ProvidesQuickQSPanelFactory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Object quickStatusBarHeaderProvider;

    public /* synthetic */ QSFragmentModule_ProvidesQuickQSPanelFactory(Object obj, int i) {
        this.$r8$classId = i;
        this.quickStatusBarHeaderProvider = obj;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                QuickQSPanel quickQSPanel = (QuickQSPanel) ((QuickStatusBarHeader) ((Provider) this.quickStatusBarHeaderProvider).mo144get()).findViewById(2131428659);
                Objects.requireNonNull(quickQSPanel, "Cannot return null from a non-@Nullable @Provides method");
                return quickQSPanel;
            case 1:
                return Float.valueOf(((ViewConfiguration) ((Provider) this.quickStatusBarHeaderProvider).mo144get()).getScaledTouchSlop());
            case 2:
                return new HashSet(Collections.singletonList((CommunalSettingCondition) ((Provider) this.quickStatusBarHeaderProvider).mo144get()));
            case 3:
                DreamOverlayContainerView dreamOverlayContainerView = (DreamOverlayContainerView) Preconditions.checkNotNull((DreamOverlayContainerView) ((LayoutInflater) ((Provider) this.quickStatusBarHeaderProvider).mo144get()).inflate(2131624091, (ViewGroup) null), "R.layout.dream_layout_container could not be properly inflated");
                Objects.requireNonNull(dreamOverlayContainerView, "Cannot return null from a non-@Nullable @Provides method");
                return dreamOverlayContainerView;
            case 4:
                return new DynamicChildBindController((RowContentBindStage) ((Provider) this.quickStatusBarHeaderProvider).mo144get());
            case 5:
                return (AuthRippleView) ((NotificationShadeWindowView) ((Provider) this.quickStatusBarHeaderProvider).mo144get()).findViewById(2131427523);
            default:
                Objects.requireNonNull((DependencyProvider) this.quickStatusBarHeaderProvider);
                TaskStackChangeListeners taskStackChangeListeners = TaskStackChangeListeners.INSTANCE;
                Objects.requireNonNull(taskStackChangeListeners, "Cannot return null from a non-@Nullable @Provides method");
                return taskStackChangeListeners;
        }
    }
}
