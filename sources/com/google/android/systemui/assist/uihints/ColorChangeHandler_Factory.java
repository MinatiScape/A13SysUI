package com.google.android.systemui.assist.uihints;

import android.content.Context;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarterLogger;
import com.android.systemui.statusbar.policy.Clock;
import com.android.wm.shell.draganddrop.DragAndDropController;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ColorChangeHandler_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;

    public /* synthetic */ ColorChangeHandler_Factory(Provider provider, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new ColorChangeHandler((Context) this.contextProvider.mo144get());
            case 1:
                return new AccessibilityButtonModeObserver((Context) this.contextProvider.mo144get());
            case 2:
                return Integer.valueOf(((Context) this.contextProvider.mo144get()).getDisplayId());
            case 3:
                SectionHeaderController headerController = ((SectionHeaderControllerSubcomponent) this.contextProvider.mo144get()).getHeaderController();
                Objects.requireNonNull(headerController, "Cannot return null from a non-@Nullable @Provides method");
                return headerController;
            case 4:
                return new StatusBarNotificationActivityStarterLogger((LogBuffer) this.contextProvider.mo144get());
            case 5:
                Clock clock = (Clock) ((PhoneStatusBarView) this.contextProvider.mo144get()).findViewById(2131427717);
                Objects.requireNonNull(clock, "Cannot return null from a non-@Nullable @Provides method");
                return clock;
            default:
                DragAndDropController dragAndDropController = (DragAndDropController) this.contextProvider.mo144get();
                Objects.requireNonNull(dragAndDropController);
                Optional of = Optional.of(dragAndDropController.mImpl);
                Objects.requireNonNull(of, "Cannot return null from a non-@Nullable @Provides method");
                return of;
        }
    }
}
