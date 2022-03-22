package com.android.systemui.people;

import android.app.people.PeopleSpaceTile;
import android.net.wifi.ScanResult;
import android.service.notification.ConversationChannelWrapper;
import android.text.TextUtils;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.people.widget.PeopleTileKey;
import java.util.HashMap;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PeopleSpaceUtils$$ExternalSyntheticLambda8 implements Predicate {
    public static final /* synthetic */ PeopleSpaceUtils$$ExternalSyntheticLambda8 INSTANCE = new PeopleSpaceUtils$$ExternalSyntheticLambda8(0);
    public static final /* synthetic */ PeopleSpaceUtils$$ExternalSyntheticLambda8 INSTANCE$1 = new PeopleSpaceUtils$$ExternalSyntheticLambda8(1);
    public static final /* synthetic */ PeopleSpaceUtils$$ExternalSyntheticLambda8 INSTANCE$2 = new PeopleSpaceUtils$$ExternalSyntheticLambda8(2);
    public final /* synthetic */ int $r8$classId;

    public /* synthetic */ PeopleSpaceUtils$$ExternalSyntheticLambda8(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                PeopleSpaceTile peopleSpaceTile = (PeopleSpaceTile) obj;
                PeopleTileKey peopleTileKey = PeopleSpaceUtils.EMPTY_KEY;
                if (peopleSpaceTile == null || TextUtils.isEmpty(peopleSpaceTile.getUserName())) {
                    return false;
                }
                return true;
            case 1:
                ConversationChannelWrapper conversationChannelWrapper = (ConversationChannelWrapper) obj;
                HashMap hashMap = PeopleSpaceWidgetManager.mListeners;
                if (conversationChannelWrapper.getNotificationChannel() == null || !conversationChannelWrapper.getNotificationChannel().isImportantConversation()) {
                    return true;
                }
                return false;
            default:
                return !TextUtils.isEmpty(((ScanResult) obj).SSID);
        }
    }
}
