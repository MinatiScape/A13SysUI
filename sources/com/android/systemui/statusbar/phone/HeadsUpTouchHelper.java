package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import java.util.Objects;
/* loaded from: classes.dex */
public final class HeadsUpTouchHelper implements Gefingerpoken {
    public Callback mCallback;
    public boolean mCollapseSnoozes;
    public HeadsUpManagerPhone mHeadsUpManager;
    public float mInitialTouchX;
    public float mInitialTouchY;
    public NotificationPanelViewController mPanel;
    public ExpandableNotificationRow mPickedChild;
    public float mTouchSlop;
    public boolean mTouchingHeadsUpView;
    public boolean mTrackingHeadsUp;
    public int mTrackingPointer;

    /* loaded from: classes.dex */
    public interface Callback {
    }

    /* JADX WARN: Code restructure failed: missing block: B:64:0x0148, code lost:
        if (r11.mIsPinned != false) goto L_0x014c;
     */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0108  */
    @Override // com.android.systemui.Gefingerpoken
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean onInterceptTouchEvent(android.view.MotionEvent r11) {
        /*
            Method dump skipped, instructions count: 375
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.HeadsUpTouchHelper.onInterceptTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // com.android.systemui.Gefingerpoken
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.mTrackingHeadsUp) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 1 || actionMasked == 3) {
            this.mTrackingPointer = -1;
            this.mPickedChild = null;
            this.mTouchingHeadsUpView = false;
            setTrackingHeadsUp(false);
        }
        return true;
    }

    public final void setTrackingHeadsUp(boolean z) {
        ExpandableNotificationRow expandableNotificationRow;
        this.mTrackingHeadsUp = z;
        HeadsUpManagerPhone headsUpManagerPhone = this.mHeadsUpManager;
        Objects.requireNonNull(headsUpManagerPhone);
        headsUpManagerPhone.mTrackingHeadsUp = z;
        NotificationPanelViewController notificationPanelViewController = this.mPanel;
        if (z) {
            expandableNotificationRow = this.mPickedChild;
        } else {
            expandableNotificationRow = null;
        }
        if (expandableNotificationRow != null) {
            Objects.requireNonNull(notificationPanelViewController);
            notificationPanelViewController.mTrackedHeadsUpNotification = expandableNotificationRow;
            for (int i = 0; i < notificationPanelViewController.mTrackingHeadsUpListeners.size(); i++) {
                notificationPanelViewController.mTrackingHeadsUpListeners.get(i).accept(expandableNotificationRow);
            }
            notificationPanelViewController.mExpandingFromHeadsUp = true;
            return;
        }
        Objects.requireNonNull(notificationPanelViewController);
    }

    public HeadsUpTouchHelper(HeadsUpManagerPhone headsUpManagerPhone, Callback callback, NotificationPanelViewController notificationPanelViewController) {
        Context context;
        this.mHeadsUpManager = headsUpManagerPhone;
        this.mCallback = callback;
        this.mPanel = notificationPanelViewController;
        NotificationStackScrollLayout.AnonymousClass9 r2 = (NotificationStackScrollLayout.AnonymousClass9) callback;
        Objects.requireNonNull(r2);
        context = ((ViewGroup) NotificationStackScrollLayout.this).mContext;
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }
}
