package com.android.systemui.theme;

import android.app.people.ConversationStatus;
import android.content.om.OverlayInfo;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.text.TextUtils;
import com.android.systemui.people.PeopleTileViewHelper;
import com.android.wifitrackerlib.PasspointWifiEntry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ThemeOverlayApplier$$ExternalSyntheticLambda5 implements Predicate {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ ThemeOverlayApplier$$ExternalSyntheticLambda5(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                ThemeOverlayApplier themeOverlayApplier = (ThemeOverlayApplier) this.f$0;
                OverlayInfo overlayInfo = (OverlayInfo) obj;
                Objects.requireNonNull(themeOverlayApplier);
                return ((Set) themeOverlayApplier.mTargetPackageToCategories.get(overlayInfo.targetPackageName)).contains(overlayInfo.category);
            case 1:
                ConversationStatus conversationStatus = (ConversationStatus) obj;
                Pattern pattern = PeopleTileViewHelper.DOUBLE_EXCLAMATION_PATTERN;
                Objects.requireNonNull((PeopleTileViewHelper) this.f$0);
                int activity = conversationStatus.getActivity();
                if (activity == 1 || activity == 2 || !TextUtils.isEmpty(conversationStatus.getDescription()) || conversationStatus.getIcon() != null) {
                    return true;
                }
                return false;
            default:
                return TextUtils.equals((String) this.f$0, PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(((PasspointConfiguration) obj).getUniqueId()));
        }
    }
}
