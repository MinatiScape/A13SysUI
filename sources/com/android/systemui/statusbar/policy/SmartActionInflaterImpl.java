package com.android.systemui.statusbar.policy;

import android.app.Notification;
import android.app.PendingIntent;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.NotificationClickNotifier;
import com.android.systemui.statusbar.NotificationClickNotifier$onNotificationActionClick$1;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.policy.SmartReplyView;
import java.util.Objects;
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes.dex */
public final class SmartActionInflaterImpl implements SmartActionInflater {
    public final ActivityStarter activityStarter;
    public final SmartReplyConstants constants;
    public final SmartReplyController smartReplyController;

    public SmartActionInflaterImpl(SmartReplyConstants smartReplyConstants, ActivityStarter activityStarter, SmartReplyController smartReplyController) {
        this.constants = smartReplyConstants;
        this.activityStarter = activityStarter;
        this.smartReplyController = smartReplyController;
    }

    @Override // com.android.systemui.statusbar.policy.SmartActionInflater
    public final Button inflateActionButton(SmartReplyView smartReplyView, final NotificationEntry notificationEntry, final SmartReplyView.SmartActions smartActions, final int i, final Notification.Action action, boolean z, ContextThemeWrapper contextThemeWrapper) {
        View inflate = LayoutInflater.from(smartReplyView.getContext()).inflate(2131624489, (ViewGroup) smartReplyView, false);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.widget.Button");
        Button button = (Button) inflate;
        button.setText(action.title);
        Drawable loadDrawable = action.getIcon().loadDrawable(contextThemeWrapper);
        int dimensionPixelSize = button.getContext().getResources().getDimensionPixelSize(2131167021);
        loadDrawable.setBounds(0, 0, dimensionPixelSize, dimensionPixelSize);
        button.setCompoundDrawables(loadDrawable, null, null, null);
        View.OnClickListener smartActionInflaterImpl$inflateActionButton$1$onClickListener$1 = new View.OnClickListener() { // from class: com.android.systemui.statusbar.policy.SmartActionInflaterImpl$inflateActionButton$1$onClickListener$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SmartActionInflaterImpl smartActionInflaterImpl = SmartActionInflaterImpl.this;
                NotificationEntry notificationEntry2 = notificationEntry;
                SmartReplyView.SmartActions smartActions2 = smartActions;
                int i2 = i;
                Notification.Action action2 = action;
                Objects.requireNonNull(smartActionInflaterImpl);
                if (!smartActions2.fromAssistant || 11 != action2.getSemanticAction()) {
                    ActivityStarter activityStarter = smartActionInflaterImpl.activityStarter;
                    PendingIntent pendingIntent = action2.actionIntent;
                    Objects.requireNonNull(notificationEntry2);
                    ExpandableNotificationRow expandableNotificationRow = notificationEntry2.row;
                    final SmartActionInflaterImpl$onSmartActionClick$1 smartActionInflaterImpl$onSmartActionClick$1 = new SmartActionInflaterImpl$onSmartActionClick$1(smartActionInflaterImpl, notificationEntry2, i2, action2, smartActions2);
                    boolean z2 = SmartReplyStateInflaterKt.DEBUG;
                    activityStarter.startPendingIntentDismissingKeyguard(pendingIntent, new Runnable() { // from class: com.android.systemui.statusbar.policy.SmartReplyStateInflaterKt$startPendingIntentDismissingKeyguard$1
                        @Override // java.lang.Runnable
                        public final void run() {
                            smartActionInflaterImpl$onSmartActionClick$1.invoke();
                        }
                    }, expandableNotificationRow);
                    return;
                }
                Objects.requireNonNull(notificationEntry2);
                ExpandableNotificationRow expandableNotificationRow2 = notificationEntry2.row;
                expandableNotificationRow2.doSmartActionClick(((int) expandableNotificationRow2.getX()) / 2, ((int) notificationEntry2.row.getY()) / 2);
                SmartReplyController smartReplyController = smartActionInflaterImpl.smartReplyController;
                boolean z3 = smartActions2.fromAssistant;
                Objects.requireNonNull(smartReplyController);
                NotificationVisibility obtain = smartReplyController.mVisibilityProvider.obtain(notificationEntry2, true);
                NotificationClickNotifier notificationClickNotifier = smartReplyController.mClickNotifier;
                String str = notificationEntry2.mKey;
                Objects.requireNonNull(notificationClickNotifier);
                try {
                    notificationClickNotifier.barService.onNotificationActionClick(str, i2, action2, obtain, z3);
                } catch (RemoteException unused) {
                }
                notificationClickNotifier.mainExecutor.execute(new NotificationClickNotifier$onNotificationActionClick$1(notificationClickNotifier, str));
            }
        };
        if (z) {
            SmartReplyConstants smartReplyConstants = this.constants;
            Objects.requireNonNull(smartReplyConstants);
            smartActionInflaterImpl$inflateActionButton$1$onClickListener$1 = new DelayedOnClickListener(smartActionInflaterImpl$inflateActionButton$1$onClickListener$1, smartReplyConstants.mOnClickInitDelay);
        }
        button.setOnClickListener(smartActionInflaterImpl$inflateActionButton$1$onClickListener$1);
        ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
        Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type com.android.systemui.statusbar.policy.SmartReplyView.LayoutParams");
        ((SmartReplyView.LayoutParams) layoutParams).mButtonType = SmartReplyView.SmartButtonType.ACTION;
        return button;
    }
}
