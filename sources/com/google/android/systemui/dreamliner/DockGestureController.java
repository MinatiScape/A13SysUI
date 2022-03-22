package com.google.android.systemui.dreamliner;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda8;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda19;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda3;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda6;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.google.android.systemui.gamedashboard.ShortcutBarView$$ExternalSyntheticLambda1;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.functions.Function1;
/* loaded from: classes.dex */
public final class DockGestureController extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener, StatusBarStateController.StateListener, KeyguardStateController.Callback, ConfigurationController.ConfigurationListener {
    public static final /* synthetic */ int $r8$clinit = 0;
    public static final long GEAR_VISIBLE_TIME_MILLIS;
    public static final long PREVIEW_DELAY_MILLIS;
    public final AccessibilityManager mAccessibilityManager;
    public final Context mContext;
    public float mDiffX;
    public final DockIndicationController mDockIndicationController;
    public float mFirstTouchX;
    public float mFirstTouchY;
    public boolean mFromRight;
    @VisibleForTesting
    public GestureDetector mGestureDetector;
    public final KeyguardStateController mKeyguardStateController;
    public float mLastTouchX;
    public boolean mLaunchedPhoto;
    public final int mPhotoDiffThreshold;
    public boolean mPhotoEnabled;
    public final FrameLayout mPhotoPreview;
    public final TextView mPhotoPreviewText;
    public PhysicsAnimator<View> mPreviewTargetAnimator;
    public final ImageView mSettingsGear;
    public boolean mShouldConsumeTouch;
    public final StatusBarStateController mStatusBarStateController;
    public PendingIntent mTapAction;
    public final View mTouchDelegateView;
    public boolean mTriggerPhoto;
    public VelocityTracker mVelocityTracker;
    public float mVelocityX;
    public final PhysicsAnimator.SpringConfig mTargetSpringConfig = new PhysicsAnimator.SpringConfig(1500.0f, 1.0f);
    public final AnonymousClass1 mKeyguardMonitorCallback = new KeyguardStateController.Callback() { // from class: com.google.android.systemui.dreamliner.DockGestureController.1
        @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
        public final void onKeyguardShowingChanged() {
            if (DockGestureController.this.mKeyguardStateController.isOccluded()) {
                DockGestureController.this.hidePhotoPreview(false);
            }
        }
    };
    public final TaskView$$ExternalSyntheticLambda3 mHideGearRunnable = new TaskView$$ExternalSyntheticLambda3(this, 11);

    static {
        TimeUnit timeUnit = TimeUnit.SECONDS;
        GEAR_VISIBLE_TIME_MILLIS = timeUnit.toMillis(15L);
        PREVIEW_DELAY_MILLIS = timeUnit.toMillis(1L);
    }

    public final void hideGear() {
        if (this.mSettingsGear.isVisibleToUser()) {
            this.mSettingsGear.removeCallbacks(this.mHideGearRunnable);
            this.mSettingsGear.animate().setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN).alpha(0.0f).withEndAction(new KeyguardUpdateMonitor$$ExternalSyntheticLambda8(this, 6)).start();
        }
    }

    public final void hidePhotoPreview(boolean z) {
        if (this.mPhotoPreview.getVisibility() == 0) {
            if (z) {
                this.mPhotoPreview.post(new StatusBar$$ExternalSyntheticLambda19(this, 10));
                return;
            }
            this.mPhotoPreview.setAlpha(0.0f);
            this.mPhotoPreview.setVisibility(4);
        }
    }

    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
    public final boolean onDown(MotionEvent motionEvent) {
        sendProtectedBroadcast(new Intent("com.google.android.systemui.dreamliner.TOUCH_EVENT"));
        return false;
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public final void onDozingChanged(boolean z) {
        if (z) {
            this.mTouchDelegateView.setOnTouchListener(this);
            showGear();
            return;
        }
        this.mTouchDelegateView.setOnTouchListener(null);
        hideGear();
        if (!this.mLaunchedPhoto) {
            hidePhotoPreview(true);
        } else {
            this.mPhotoPreview.postDelayed(new TaskView$$ExternalSyntheticLambda6(this, 8), PREVIEW_DELAY_MILLIS);
        }
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public final void onLocaleListChanged() {
        this.mPhotoPreviewText.setText(this.mContext.getResources().getString(2131952297));
    }

    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
    public final boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        PendingIntent pendingIntent = this.mTapAction;
        if (pendingIntent != null) {
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                Log.w("DLGestureController", "Tap action pending intent cancelled", e);
            }
        }
        showGear();
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x0134  */
    @Override // android.view.View.OnTouchListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean onTouch(android.view.View r10, android.view.MotionEvent r11) {
        /*
            Method dump skipped, instructions count: 334
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.dreamliner.DockGestureController.onTouch(android.view.View, android.view.MotionEvent):boolean");
    }

    public final void sendProtectedBroadcast(Intent intent) {
        try {
            this.mContext.sendBroadcastAsUser(intent, UserHandle.CURRENT);
        } catch (SecurityException e) {
            Log.w("DLGestureController", "Cannot send event", e);
        }
    }

    public final void showGear() {
        long j;
        if (this.mTapAction == null) {
            if (!this.mSettingsGear.isVisibleToUser()) {
                this.mSettingsGear.setVisibility(0);
                this.mSettingsGear.animate().setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN).alpha(1.0f).start();
            }
            this.mSettingsGear.removeCallbacks(this.mHideGearRunnable);
            ImageView imageView = this.mSettingsGear;
            TaskView$$ExternalSyntheticLambda3 taskView$$ExternalSyntheticLambda3 = this.mHideGearRunnable;
            AccessibilityManager accessibilityManager = this.mAccessibilityManager;
            if (accessibilityManager == null) {
                j = GEAR_VISIBLE_TIME_MILLIS;
            } else {
                j = accessibilityManager.getRecommendedTimeoutMillis(Math.toIntExact(GEAR_VISIBLE_TIME_MILLIS), 5);
            }
            imageView.postDelayed(taskView$$ExternalSyntheticLambda3, j);
        }
    }

    public final void stopMonitoring() {
        this.mStatusBarStateController.removeCallback(this);
        this.mKeyguardStateController.removeCallback(this.mKeyguardMonitorCallback);
        onDozingChanged(false);
        this.mSettingsGear.setVisibility(8);
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.google.android.systemui.dreamliner.DockGestureController$1] */
    public DockGestureController(Context context, ImageView imageView, FrameLayout frameLayout, View view, DockIndicationController dockIndicationController, StatusBarStateController statusBarStateController, KeyguardStateController keyguardStateController) {
        this.mDockIndicationController = dockIndicationController;
        this.mContext = context;
        this.mGestureDetector = new GestureDetector(context, this);
        this.mTouchDelegateView = view;
        this.mSettingsGear = imageView;
        this.mPhotoPreview = frameLayout;
        TextView textView = (TextView) frameLayout.findViewById(2131428584);
        this.mPhotoPreviewText = textView;
        textView.setText(context.getResources().getString(2131952297));
        imageView.setOnClickListener(new ShortcutBarView$$ExternalSyntheticLambda1(this, 1));
        this.mAccessibilityManager = (AccessibilityManager) context.getSystemService("accessibility");
        this.mPhotoDiffThreshold = context.getResources().getDimensionPixelSize(2131165671);
        this.mStatusBarStateController = statusBarStateController;
        this.mKeyguardStateController = keyguardStateController;
        Function1<Object, ? extends PhysicsAnimator<?>> function1 = PhysicsAnimator.instanceConstructor;
        this.mPreviewTargetAnimator = PhysicsAnimator.Companion.getInstance(frameLayout);
    }
}
