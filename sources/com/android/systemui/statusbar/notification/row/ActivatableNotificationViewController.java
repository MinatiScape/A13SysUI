package com.android.systemui.statusbar.notification.row;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationView;
import com.android.systemui.statusbar.phone.NotificationTapHelper;
import com.android.systemui.util.ViewController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ActivatableNotificationViewController extends ViewController<ActivatableNotificationView> {
    public final AccessibilityManager mAccessibilityManager;
    public final ExpandableOutlineViewController mExpandableOutlineViewController;
    public final FalsingCollector mFalsingCollector;
    public final FalsingManager mFalsingManager;
    public final TouchHandler mTouchHandler = new TouchHandler();

    /* loaded from: classes.dex */
    public class TouchHandler implements Gefingerpoken, View.OnTouchListener {
        @Override // com.android.systemui.Gefingerpoken
        public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        @Override // com.android.systemui.Gefingerpoken
        public final boolean onTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        public TouchHandler() {
        }

        @Override // android.view.View.OnTouchListener
        public final boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 1) {
                ActivatableNotificationView activatableNotificationView = (ActivatableNotificationView) ActivatableNotificationViewController.this.mView;
                long uptimeMillis = SystemClock.uptimeMillis();
                Objects.requireNonNull(activatableNotificationView);
                activatableNotificationView.mLastActionUpTime = uptimeMillis;
            }
            if (ActivatableNotificationViewController.this.mAccessibilityManager.isTouchExplorationEnabled() || motionEvent.getAction() != 1) {
                return false;
            }
            boolean isFalseTap = ActivatableNotificationViewController.this.mFalsingManager.isFalseTap(1);
            if (!isFalseTap && (view instanceof ActivatableNotificationView)) {
                ((ActivatableNotificationView) view).onTap();
            }
            return isFalseTap;
        }
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
    }

    @Override // com.android.systemui.util.ViewController
    public final void onInit() {
        ExpandableOutlineViewController expandableOutlineViewController = this.mExpandableOutlineViewController;
        Objects.requireNonNull(expandableOutlineViewController);
        Objects.requireNonNull(expandableOutlineViewController.mExpandableViewController);
        ((ActivatableNotificationView) this.mView).setOnTouchListener(this.mTouchHandler);
        ActivatableNotificationView activatableNotificationView = (ActivatableNotificationView) this.mView;
        TouchHandler touchHandler = this.mTouchHandler;
        Objects.requireNonNull(activatableNotificationView);
        activatableNotificationView.mTouchHandler = touchHandler;
        Objects.requireNonNull((ActivatableNotificationView) this.mView);
    }

    public ActivatableNotificationViewController(ActivatableNotificationView activatableNotificationView, NotificationTapHelper.Factory factory, ExpandableOutlineViewController expandableOutlineViewController, AccessibilityManager accessibilityManager, FalsingManager falsingManager, FalsingCollector falsingCollector) {
        super(activatableNotificationView);
        this.mExpandableOutlineViewController = expandableOutlineViewController;
        this.mAccessibilityManager = accessibilityManager;
        this.mFalsingManager = falsingManager;
        this.mFalsingCollector = falsingCollector;
        Objects.requireNonNull(activatableNotificationView);
        Objects.requireNonNull(activatableNotificationView);
        Objects.requireNonNull(factory);
        ActivatableNotificationView.OnActivatedListener onActivatedListener = new ActivatableNotificationView.OnActivatedListener() { // from class: com.android.systemui.statusbar.notification.row.ActivatableNotificationViewController.1
            @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView.OnActivatedListener
            public final void onActivationReset(ActivatableNotificationView activatableNotificationView2) {
            }
        };
        Objects.requireNonNull(activatableNotificationView);
        activatableNotificationView.mOnActivatedListener = onActivatedListener;
    }
}
