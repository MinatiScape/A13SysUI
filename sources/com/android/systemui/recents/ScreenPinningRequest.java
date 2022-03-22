package com.android.systemui.recents;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import androidx.preference.R$id;
import com.android.systemui.Dependency;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.statusbar.phone.StatusBar;
import dagger.Lazy;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public final class ScreenPinningRequest implements View.OnClickListener, NavigationModeController.ModeChangedListener {
    public final AccessibilityManager mAccessibilityService;
    public final Context mContext;
    public int mNavBarMode = ((NavigationModeController) Dependency.get(NavigationModeController.class)).addListener(this);
    public RequestWindowView mRequestWindow;
    public final Lazy<Optional<StatusBar>> mStatusBarOptionalLazy;
    public final WindowManager mWindowManager;
    public int taskId;

    /* loaded from: classes.dex */
    public class RequestWindowView extends FrameLayout {
        public final ColorDrawable mColor;
        public ValueAnimator mColorAnim;
        public ViewGroup mLayout;
        public boolean mShowCancel;
        public final BroadcastDispatcher mBroadcastDispatcher = (BroadcastDispatcher) Dependency.get(BroadcastDispatcher.class);
        public final AnonymousClass2 mUpdateLayoutRunnable = new Runnable() { // from class: com.android.systemui.recents.ScreenPinningRequest.RequestWindowView.2
            @Override // java.lang.Runnable
            public final void run() {
                int i;
                ViewGroup viewGroup = RequestWindowView.this.mLayout;
                if (viewGroup != null && viewGroup.getParent() != null) {
                    RequestWindowView requestWindowView = RequestWindowView.this;
                    ViewGroup viewGroup2 = requestWindowView.mLayout;
                    ScreenPinningRequest screenPinningRequest = ScreenPinningRequest.this;
                    int rotation = RequestWindowView.getRotation(((FrameLayout) requestWindowView).mContext);
                    Objects.requireNonNull(screenPinningRequest);
                    if (rotation == 3) {
                        i = 19;
                    } else if (rotation == 1) {
                        i = 21;
                    } else {
                        i = 81;
                    }
                    viewGroup2.setLayoutParams(new FrameLayout.LayoutParams(-2, -2, i));
                }
            }
        };
        public final AnonymousClass3 mReceiver = new BroadcastReceiver() { // from class: com.android.systemui.recents.ScreenPinningRequest.RequestWindowView.3
            @Override // android.content.BroadcastReceiver
            public final void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("android.intent.action.CONFIGURATION_CHANGED")) {
                    RequestWindowView requestWindowView = RequestWindowView.this;
                    requestWindowView.post(requestWindowView.mUpdateLayoutRunnable);
                } else if (intent.getAction().equals("android.intent.action.USER_SWITCHED") || intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                    ScreenPinningRequest screenPinningRequest = ScreenPinningRequest.this;
                    Objects.requireNonNull(screenPinningRequest);
                    RequestWindowView requestWindowView2 = screenPinningRequest.mRequestWindow;
                    if (requestWindowView2 != null) {
                        screenPinningRequest.mWindowManager.removeView(requestWindowView2);
                        screenPinningRequest.mRequestWindow = null;
                    }
                }
            }
        };

        /* JADX WARN: Type inference failed for: r0v4, types: [com.android.systemui.recents.ScreenPinningRequest$RequestWindowView$2] */
        /* JADX WARN: Type inference failed for: r0v5, types: [com.android.systemui.recents.ScreenPinningRequest$RequestWindowView$3] */
        public RequestWindowView(Context context, boolean z) {
            super(context);
            ColorDrawable colorDrawable = new ColorDrawable(0);
            this.mColor = colorDrawable;
            setClickable(true);
            setOnClickListener(ScreenPinningRequest.this);
            setBackground(colorDrawable);
            this.mShowCancel = z;
        }

        @Override // android.view.ViewGroup, android.view.View
        public final void onAttachedToWindow() {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ScreenPinningRequest.this.mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
            float f = displayMetrics.density;
            int rotation = getRotation(((FrameLayout) this).mContext);
            inflateView(rotation);
            int color = ((FrameLayout) this).mContext.getColor(2131100528);
            if (ActivityManager.isHighEndGfx()) {
                this.mLayout.setAlpha(0.0f);
                if (rotation == 3) {
                    this.mLayout.setTranslationX(f * (-96.0f));
                } else if (rotation == 1) {
                    this.mLayout.setTranslationX(f * 96.0f);
                } else {
                    this.mLayout.setTranslationY(f * 96.0f);
                }
                this.mLayout.animate().alpha(1.0f).translationX(0.0f).translationY(0.0f).setDuration(300L).setInterpolator(new DecelerateInterpolator()).start();
                ValueAnimator ofObject = ValueAnimator.ofObject(new ArgbEvaluator(), 0, Integer.valueOf(color));
                this.mColorAnim = ofObject;
                ofObject.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.recents.ScreenPinningRequest.RequestWindowView.1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        RequestWindowView.this.mColor.setColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
                    }
                });
                this.mColorAnim.setDuration(1000L);
                this.mColorAnim.start();
            } else {
                this.mColor.setColor(color);
            }
            IntentFilter intentFilter = new IntentFilter("android.intent.action.CONFIGURATION_CHANGED");
            intentFilter.addAction("android.intent.action.USER_SWITCHED");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            this.mBroadcastDispatcher.registerReceiver(this.mReceiver, intentFilter);
        }

        @Override // android.view.ViewGroup, android.view.View
        public final void onDetachedFromWindow() {
            this.mBroadcastDispatcher.unregisterReceiver(this.mReceiver);
        }

        public static int getRotation(Context context) {
            if (context.getResources().getConfiguration().smallestScreenWidthDp >= 600) {
                return 0;
            }
            return R$id.getRotation(context);
        }

        /* JADX WARN: Removed duplicated region for block: B:29:0x00c6  */
        /* JADX WARN: Removed duplicated region for block: B:30:0x00d4  */
        /* JADX WARN: Removed duplicated region for block: B:39:0x0112  */
        /* JADX WARN: Removed duplicated region for block: B:40:0x0116  */
        /* JADX WARN: Removed duplicated region for block: B:51:0x016c  */
        /* JADX WARN: Removed duplicated region for block: B:66:0x0224  */
        /* JADX WARN: Removed duplicated region for block: B:69:0x0248  */
        /* JADX WARN: Removed duplicated region for block: B:70:0x024b  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void inflateView(int r11) {
            /*
                Method dump skipped, instructions count: 602
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.recents.ScreenPinningRequest.RequestWindowView.inflateView(int):void");
        }

        public final void onConfigurationChanged() {
            removeAllViews();
            inflateView(getRotation(((FrameLayout) this).mContext));
        }
    }

    public ScreenPinningRequest(Context context, Lazy<Optional<StatusBar>> lazy) {
        this.mContext = context;
        this.mStatusBarOptionalLazy = lazy;
        this.mAccessibilityService = (AccessibilityManager) context.getSystemService("accessibility");
        this.mWindowManager = (WindowManager) context.getSystemService("window");
        OverviewProxyService overviewProxyService = (OverviewProxyService) Dependency.get(OverviewProxyService.class);
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        if (view.getId() == 2131428754 || this.mRequestWindow == view) {
            try {
                ActivityTaskManager.getService().startSystemLockTaskMode(this.taskId);
            } catch (RemoteException unused) {
            }
        }
        RequestWindowView requestWindowView = this.mRequestWindow;
        if (requestWindowView != null) {
            this.mWindowManager.removeView(requestWindowView);
            this.mRequestWindow = null;
        }
    }

    @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
    public final void onNavigationModeChanged(int i) {
        this.mNavBarMode = i;
    }
}
