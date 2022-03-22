package com.android.wm.shell.bubbles;

import android.app.ActivityOptions;
import android.content.Intent;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Rect;
import android.util.Log;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.phone.StatusBarRemoteInputCallback;
import com.android.wm.shell.TaskView;
import com.android.wm.shell.bubbles.BubbleExpandedView;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubbleExpandedView$1$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ Object f$2;

    public /* synthetic */ BubbleExpandedView$1$$ExternalSyntheticLambda1(Object obj, Object obj2, Object obj3, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
        this.f$2 = obj3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                BubbleExpandedView.AnonymousClass1 r0 = (BubbleExpandedView.AnonymousClass1) this.f$0;
                ActivityOptions activityOptions = (ActivityOptions) this.f$1;
                Rect rect = (Rect) this.f$2;
                Objects.requireNonNull(r0);
                try {
                    activityOptions.setTaskAlwaysOnTop(true);
                    activityOptions.setLaunchedFromBubble(true);
                    BubbleExpandedView bubbleExpandedView = BubbleExpandedView.this;
                    if (bubbleExpandedView.mIsOverflow || !bubbleExpandedView.mBubble.hasMetadataShortcutId()) {
                        Intent intent = new Intent();
                        intent.addFlags(524288);
                        intent.addFlags(134217728);
                        BubbleExpandedView bubbleExpandedView2 = BubbleExpandedView.this;
                        Bubble bubble = bubbleExpandedView2.mBubble;
                        if (bubble != null) {
                            bubble.mIntentActive = true;
                        }
                        bubbleExpandedView2.mTaskView.startActivity(bubbleExpandedView2.mPendingIntent, intent, activityOptions, rect);
                        return;
                    }
                    activityOptions.setApplyActivityFlagsForBubbles(true);
                    BubbleExpandedView bubbleExpandedView3 = BubbleExpandedView.this;
                    TaskView taskView = bubbleExpandedView3.mTaskView;
                    Bubble bubble2 = bubbleExpandedView3.mBubble;
                    Objects.requireNonNull(bubble2);
                    taskView.startShortcutActivity(bubble2.mShortcutInfo, activityOptions, rect);
                    return;
                } catch (RuntimeException e) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Exception while displaying bubble: ");
                    m.append(BubbleExpandedView.this.getBubbleKey());
                    m.append(", ");
                    m.append(e.getMessage());
                    m.append("; removing bubble");
                    Log.w("Bubbles", m.toString());
                    BubbleExpandedView bubbleExpandedView4 = BubbleExpandedView.this;
                    bubbleExpandedView4.mController.removeBubble(bubbleExpandedView4.getBubbleKey(), 10);
                    return;
                }
            default:
                StatusBarRemoteInputCallback statusBarRemoteInputCallback = (StatusBarRemoteInputCallback) this.f$0;
                NotificationStackScrollLayout notificationStackScrollLayout = (NotificationStackScrollLayout) this.f$1;
                Objects.requireNonNull(statusBarRemoteInputCallback);
                BubbleStackView$$ExternalSyntheticLambda21 bubbleStackView$$ExternalSyntheticLambda21 = new BubbleStackView$$ExternalSyntheticLambda21(statusBarRemoteInputCallback, notificationStackScrollLayout, 2);
                if (notificationStackScrollLayout.scrollTo((ExpandableNotificationRow) this.f$2)) {
                    notificationStackScrollLayout.mFinishScrollingCallback = bubbleStackView$$ExternalSyntheticLambda21;
                    return;
                } else {
                    bubbleStackView$$ExternalSyntheticLambda21.run();
                    return;
                }
        }
    }
}
