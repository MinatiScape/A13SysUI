package com.android.wm.shell.bubbles;

import android.view.View;
import com.android.keyguard.KeyguardPasswordViewController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubbleStackView$$ExternalSyntheticLambda6 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ BubbleStackView$$ExternalSyntheticLambda6(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                BubbleStackView.$r8$lambda$VRRlSoOy6XdJCiRiE6ME0w93vig((BubbleStackView) this.f$0);
                return;
            case 1:
                KeyguardPasswordViewController keyguardPasswordViewController = (KeyguardPasswordViewController) this.f$0;
                Objects.requireNonNull(keyguardPasswordViewController);
                keyguardPasswordViewController.mKeyguardSecurityCallback.reset();
                keyguardPasswordViewController.mKeyguardSecurityCallback.onCancelClicked();
                return;
            default:
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) this.f$0;
                ExpandableNotificationRow.AnonymousClass2 r4 = ExpandableNotificationRow.TRANSLATE_CONTENT;
                Objects.requireNonNull(expandableNotificationRow);
                if (expandableNotificationRow.mBubblesManagerOptional.isPresent()) {
                    NotificationEntry notificationEntry = expandableNotificationRow.mEntry;
                    expandableNotificationRow.mBubblesManagerOptional.get().onUserChangedBubble(notificationEntry, !notificationEntry.isBubble());
                }
                HeadsUpManager headsUpManager = expandableNotificationRow.mHeadsUpManager;
                NotificationEntry notificationEntry2 = expandableNotificationRow.mEntry;
                Objects.requireNonNull(notificationEntry2);
                headsUpManager.removeNotification(notificationEntry2.mKey, true);
                return;
        }
    }
}
