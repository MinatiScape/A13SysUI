package com.google.android.systemui.elmyra.actions;

import android.content.Context;
import com.android.systemui.keyboard.KeyboardUI_Factory;
import com.android.systemui.statusbar.notification.row.ExpandableOutlineView;
import com.android.systemui.statusbar.notification.row.ExpandableOutlineViewController;
import com.android.systemui.statusbar.notification.row.ExpandableViewController;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class UnpinNotifications_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;
    public final Provider headsUpManagerOptionalProvider;

    public /* synthetic */ UnpinNotifications_Factory(Provider provider, Provider provider2, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.headsUpManagerOptionalProvider = provider2;
    }

    public static UnpinNotifications_Factory create(Provider provider, KeyboardUI_Factory keyboardUI_Factory) {
        return new UnpinNotifications_Factory(provider, keyboardUI_Factory, 1);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new UnpinNotifications((Context) this.contextProvider.mo144get(), (Optional) this.headsUpManagerOptionalProvider.mo144get());
            default:
                ExpandableOutlineView expandableOutlineView = (ExpandableOutlineView) this.contextProvider.mo144get();
                return new ExpandableOutlineViewController((ExpandableViewController) this.headsUpManagerOptionalProvider.mo144get());
        }
    }
}
