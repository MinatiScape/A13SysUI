package com.android.wifitrackerlib;

import android.service.notification.ConversationChannelWrapper;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda25 implements Predicate {
    public final /* synthetic */ int $r8$classId;
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda25 INSTANCE$1 = new WifiPickerTracker$$ExternalSyntheticLambda25(1);
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda25 INSTANCE = new WifiPickerTracker$$ExternalSyntheticLambda25(0);

    public /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda25(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                StandardWifiEntry standardWifiEntry = (StandardWifiEntry) obj;
                Objects.requireNonNull(standardWifiEntry);
                if (standardWifiEntry.mLevel == -1) {
                    return true;
                }
                return false;
            default:
                ConversationChannelWrapper conversationChannelWrapper = (ConversationChannelWrapper) obj;
                HashMap hashMap = PeopleSpaceWidgetManager.mListeners;
                if (conversationChannelWrapper.getNotificationChannel() == null || !conversationChannelWrapper.getNotificationChannel().isImportantConversation()) {
                    return false;
                }
                return true;
        }
    }
}
