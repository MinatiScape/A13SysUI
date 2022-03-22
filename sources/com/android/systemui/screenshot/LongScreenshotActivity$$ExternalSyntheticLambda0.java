package com.android.systemui.screenshot;

import android.view.View;
import com.android.systemui.statusbar.notification.row.NotificationConversationInfo;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class LongScreenshotActivity$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ LongScreenshotActivity$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                LongScreenshotActivity.$r8$lambda$qOUpLbBDnQlAC3CzBI4dsyuYNs4((LongScreenshotActivity) this.f$0, view);
                return;
            default:
                NotificationConversationInfo notificationConversationInfo = (NotificationConversationInfo) this.f$0;
                int i = NotificationConversationInfo.$r8$clinit;
                Objects.requireNonNull(notificationConversationInfo);
                notificationConversationInfo.setSelectedAction(4);
                notificationConversationInfo.updateToggleActions(notificationConversationInfo.mSelectedAction, true);
                return;
        }
    }
}
