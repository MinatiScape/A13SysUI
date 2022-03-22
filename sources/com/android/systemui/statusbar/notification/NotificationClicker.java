package com.android.systemui.statusbar.notification;

import android.util.Log;
import android.view.View;
import com.android.systemui.DejankUtils;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.navigationbar.NavigationBarView$$ExternalSyntheticLambda2;
import com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin;
import com.android.systemui.qs.QSAnimator$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter;
import com.android.wm.shell.bubbles.Bubbles;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public final class NotificationClicker implements View.OnClickListener {
    public final Optional<Bubbles> mBubblesOptional;
    public final NotificationClickerLogger mLogger;
    public final NotificationActivityStarter mNotificationActivityStarter;
    public AnonymousClass1 mOnDragSuccessListener = new AnonymousClass1();
    public final Optional<StatusBar> mStatusBarOptional;

    /* renamed from: com.android.systemui.statusbar.notification.NotificationClicker$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements ExpandableNotificationRow.OnDragSuccessListener {
        public AnonymousClass1() {
        }
    }

    /* loaded from: classes.dex */
    public static class Builder {
        public final NotificationClickerLogger mLogger;

        public Builder(NotificationClickerLogger notificationClickerLogger) {
            this.mLogger = notificationClickerLogger;
        }
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        boolean z;
        if (!(view instanceof ExpandableNotificationRow)) {
            Log.e("NotificationClicker", "NotificationClicker called on a view that is not a notification row.");
            return;
        }
        this.mStatusBarOptional.ifPresent(new NavigationBarView$$ExternalSyntheticLambda2(view, 1));
        ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
        Objects.requireNonNull(expandableNotificationRow);
        NotificationEntry notificationEntry = expandableNotificationRow.mEntry;
        NotificationClickerLogger notificationClickerLogger = this.mLogger;
        Objects.requireNonNull(notificationClickerLogger);
        LogBuffer logBuffer = notificationClickerLogger.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationClickerLogger$logOnClick$2 notificationClickerLogger$logOnClick$2 = NotificationClickerLogger$logOnClick$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationClicker", logLevel, notificationClickerLogger$logOnClick$2);
            obtain.str1 = notificationEntry.mKey;
            obtain.str2 = notificationEntry.mRanking.getChannel().getId();
            logBuffer.push(obtain);
        }
        NotificationMenuRowPlugin notificationMenuRowPlugin = expandableNotificationRow.mMenuRow;
        boolean z2 = false;
        if (notificationMenuRowPlugin == null || !notificationMenuRowPlugin.isMenuVisible()) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            NotificationClickerLogger notificationClickerLogger2 = this.mLogger;
            Objects.requireNonNull(notificationClickerLogger2);
            LogBuffer logBuffer2 = notificationClickerLogger2.buffer;
            NotificationClickerLogger$logMenuVisible$2 notificationClickerLogger$logMenuVisible$2 = NotificationClickerLogger$logMenuVisible$2.INSTANCE;
            Objects.requireNonNull(logBuffer2);
            if (!logBuffer2.frozen) {
                LogMessageImpl obtain2 = logBuffer2.obtain("NotificationClicker", logLevel, notificationClickerLogger$logMenuVisible$2);
                obtain2.str1 = notificationEntry.mKey;
                logBuffer2.push(obtain2);
            }
            expandableNotificationRow.animateResetTranslation();
            return;
        }
        if (expandableNotificationRow.isChildInGroup()) {
            ExpandableNotificationRow expandableNotificationRow2 = expandableNotificationRow.mNotificationParent;
            Objects.requireNonNull(expandableNotificationRow2);
            NotificationMenuRowPlugin notificationMenuRowPlugin2 = expandableNotificationRow2.mMenuRow;
            if (notificationMenuRowPlugin2 != null && notificationMenuRowPlugin2.isMenuVisible()) {
                z2 = true;
            }
            if (z2) {
                NotificationClickerLogger notificationClickerLogger3 = this.mLogger;
                Objects.requireNonNull(notificationClickerLogger3);
                LogBuffer logBuffer3 = notificationClickerLogger3.buffer;
                NotificationClickerLogger$logParentMenuVisible$2 notificationClickerLogger$logParentMenuVisible$2 = NotificationClickerLogger$logParentMenuVisible$2.INSTANCE;
                Objects.requireNonNull(logBuffer3);
                if (!logBuffer3.frozen) {
                    LogMessageImpl obtain3 = logBuffer3.obtain("NotificationClicker", logLevel, notificationClickerLogger$logParentMenuVisible$2);
                    obtain3.str1 = notificationEntry.mKey;
                    logBuffer3.push(obtain3);
                }
                expandableNotificationRow.mNotificationParent.animateResetTranslation();
                return;
            }
        }
        if (expandableNotificationRow.mIsSummaryWithChildren && expandableNotificationRow.mChildrenExpanded) {
            NotificationClickerLogger notificationClickerLogger4 = this.mLogger;
            Objects.requireNonNull(notificationClickerLogger4);
            LogBuffer logBuffer4 = notificationClickerLogger4.buffer;
            NotificationClickerLogger$logChildrenExpanded$2 notificationClickerLogger$logChildrenExpanded$2 = NotificationClickerLogger$logChildrenExpanded$2.INSTANCE;
            Objects.requireNonNull(logBuffer4);
            if (!logBuffer4.frozen) {
                LogMessageImpl obtain4 = logBuffer4.obtain("NotificationClicker", logLevel, notificationClickerLogger$logChildrenExpanded$2);
                obtain4.str1 = notificationEntry.mKey;
                logBuffer4.push(obtain4);
            }
        } else if (expandableNotificationRow.areGutsExposed()) {
            NotificationClickerLogger notificationClickerLogger5 = this.mLogger;
            Objects.requireNonNull(notificationClickerLogger5);
            LogBuffer logBuffer5 = notificationClickerLogger5.buffer;
            NotificationClickerLogger$logGutsExposed$2 notificationClickerLogger$logGutsExposed$2 = NotificationClickerLogger$logGutsExposed$2.INSTANCE;
            Objects.requireNonNull(logBuffer5);
            if (!logBuffer5.frozen) {
                LogMessageImpl obtain5 = logBuffer5.obtain("NotificationClicker", logLevel, notificationClickerLogger$logGutsExposed$2);
                obtain5.str1 = notificationEntry.mKey;
                logBuffer5.push(obtain5);
            }
        } else {
            expandableNotificationRow.mJustClicked = true;
            DejankUtils.postAfterTraversal(new QSAnimator$$ExternalSyntheticLambda0(expandableNotificationRow, 2));
            if (!expandableNotificationRow.mEntry.isBubble() && this.mBubblesOptional.isPresent()) {
                this.mBubblesOptional.get().collapseStack();
            }
            ((StatusBarNotificationActivityStarter) this.mNotificationActivityStarter).onNotificationClicked(notificationEntry.mSbn, expandableNotificationRow);
        }
    }

    public NotificationClicker(NotificationClickerLogger notificationClickerLogger, Optional optional, Optional optional2, NotificationActivityStarter notificationActivityStarter) {
        this.mLogger = notificationClickerLogger;
        this.mStatusBarOptional = optional;
        this.mBubblesOptional = optional2;
        this.mNotificationActivityStarter = notificationActivityStarter;
    }
}
