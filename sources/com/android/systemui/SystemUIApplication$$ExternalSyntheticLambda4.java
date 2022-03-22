package com.android.systemui;

import android.net.wifi.hotspot2.PasspointConfiguration;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.icon.IconPack;
import com.android.wifitrackerlib.PasspointWifiEntry;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Function;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class SystemUIApplication$$ExternalSyntheticLambda4 implements Function {
    public static final /* synthetic */ SystemUIApplication$$ExternalSyntheticLambda4 INSTANCE = new SystemUIApplication$$ExternalSyntheticLambda4(0);
    public static final /* synthetic */ SystemUIApplication$$ExternalSyntheticLambda4 INSTANCE$1 = new SystemUIApplication$$ExternalSyntheticLambda4(1);
    public static final /* synthetic */ SystemUIApplication$$ExternalSyntheticLambda4 INSTANCE$2 = new SystemUIApplication$$ExternalSyntheticLambda4(2);
    public final /* synthetic */ int $r8$classId;

    public /* synthetic */ SystemUIApplication$$ExternalSyntheticLambda4(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                return ((Class) obj).getName();
            case 1:
                NotificationEntry notificationEntry = (NotificationEntry) obj;
                HashSet<Integer> hashSet = NotificationMediaManager.PAUSED_MEDIA_STATES;
                Objects.requireNonNull(notificationEntry);
                IconPack iconPack = notificationEntry.mIcons;
                Objects.requireNonNull(iconPack);
                return iconPack.mShelfIcon;
            default:
                return PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(((PasspointConfiguration) obj).getUniqueId());
        }
    }
}
