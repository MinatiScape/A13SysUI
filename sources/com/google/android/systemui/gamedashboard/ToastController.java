package com.google.android.systemui.gamedashboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.leanback.R$color;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.google.android.systemui.gamedashboard.GameDashboardUiEventLogger;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ToastController implements ConfigurationController.ConfigurationListener, NavigationModeController.ModeChangedListener {
    public final Context mContext;
    public int mFadeInAnimationDuration;
    public int mFadeOutAnimationDuration;
    public View mLaunchContainer;
    public TextView mLaunchDndMessageView;
    public TextView mLaunchGameModeMessageView;
    public View mLaunchLayout;
    public int mNavBarMode;
    public int mOrientationMargin;
    public View mRecordSaveContainer;
    public TextView mRecordSaveView;
    public TextView mShortcutView;
    public int mTranslateDownAnimationDuration;
    public int mTranslateUpAnimationDuration;
    public final UiEventLogger mUiEventLogger;
    public final WindowManager mWindowManager;
    public int mIsAddedToWindowManager = 0;
    public int mGameTaskId = 0;

    public final int getMargin() {
        int i;
        int i2 = this.mContext.getResources().getConfiguration().orientation;
        int i3 = this.mOrientationMargin;
        if (i2 != 1) {
            return i3;
        }
        if (R$color.isGesturalMode(this.mNavBarMode)) {
            i = this.mContext.getResources().getDimensionPixelSize(2131166594);
        } else {
            i = this.mContext.getResources().getDimensionPixelSize(2131166595);
        }
        return i3 + i;
    }

    public final boolean hide() {
        int i = this.mIsAddedToWindowManager;
        if (i == 0) {
            return false;
        }
        if (i == 1) {
            this.mLaunchLayout.animate().translationY(500.0f).setDuration(this.mTranslateDownAnimationDuration).setStartDelay(3000L).setListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.gamedashboard.ToastController.4
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    ToastController.this.mLaunchLayout.setVisibility(8);
                    ToastController.this.removeViewImmediate();
                    ToastController.this.mLaunchLayout.animate().setListener(null);
                }
            });
        } else if (i == 2) {
            this.mShortcutView.animate().alpha(0.0f).setDuration(this.mFadeOutAnimationDuration).setStartDelay(1000L).setListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.gamedashboard.ToastController.5
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    ToastController.this.mShortcutView.setVisibility(8);
                    ToastController.this.removeViewImmediate();
                    ToastController.this.mShortcutView.animate().setListener(null);
                }
            });
        } else if (i == 3) {
            this.mRecordSaveView.animate().translationY(500.0f).setDuration(this.mTranslateDownAnimationDuration).setStartDelay(3000L).setListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.gamedashboard.ToastController.6
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    ToastController.this.mRecordSaveView.setVisibility(8);
                    ToastController.this.removeViewImmediate();
                    ToastController.this.mRecordSaveView.animate().setListener(null);
                }
            });
        }
        return true;
    }

    public final void removeViewImmediate() {
        this.mLaunchLayout.animate().cancel();
        this.mShortcutView.animate().cancel();
        this.mRecordSaveView.animate().cancel();
        int i = this.mIsAddedToWindowManager;
        if (i == 1) {
            this.mWindowManager.removeViewImmediate(this.mLaunchContainer);
        } else if (i == 2) {
            this.mWindowManager.removeViewImmediate(this.mShortcutView);
        } else if (i == 3) {
            this.mWindowManager.removeViewImmediate(this.mRecordSaveContainer);
        }
        this.mIsAddedToWindowManager = 0;
    }

    public final void setResourceValues() {
        Resources resources = this.mContext.getResources();
        if (this.mIsAddedToWindowManager != 1) {
            View inflate = LayoutInflater.from(this.mContext).inflate(2131624108, (ViewGroup) null);
            this.mLaunchContainer = inflate;
            View findViewById = inflate.findViewById(2131427998);
            this.mLaunchLayout = findViewById;
            this.mLaunchDndMessageView = (TextView) findViewById.findViewById(2131428000);
            this.mLaunchGameModeMessageView = (TextView) this.mLaunchLayout.findViewById(2131428001);
            ((TextView) this.mLaunchLayout.findViewById(2131427999)).setOnClickListener(new View.OnClickListener() { // from class: com.google.android.systemui.gamedashboard.ToastController$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ToastController toastController = ToastController.this;
                    Objects.requireNonNull(toastController);
                    toastController.removeViewImmediate();
                    Context context = toastController.mContext;
                    IntentFilter intentFilter = GameMenuActivity.DND_FILTER;
                    Intent intent = new Intent(context, GameMenuActivity.class);
                    ActivityOptions makeCustomTaskAnimation = ActivityOptions.makeCustomTaskAnimation(toastController.mContext, 2130772507, 2130772508, null, null, null);
                    ActivityManager.RunningTaskInfo runningTask = ActivityManagerWrapper.sInstance.getRunningTask();
                    makeCustomTaskAnimation.setLaunchTaskId(runningTask.taskId);
                    toastController.mUiEventLogger.log(GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_LAUNCH, 0, runningTask.baseActivity.getPackageName());
                    toastController.mContext.startActivity(intent, makeCustomTaskAnimation.toBundle());
                }
            });
        }
        if (this.mIsAddedToWindowManager != 2) {
            this.mShortcutView = (TextView) LayoutInflater.from(this.mContext).inflate(2131624110, (ViewGroup) null);
        }
        if (this.mIsAddedToWindowManager != 3) {
            View inflate2 = LayoutInflater.from(this.mContext).inflate(2131624116, (ViewGroup) null);
            this.mRecordSaveContainer = inflate2;
            this.mRecordSaveView = (TextView) inflate2.findViewById(2131428017);
        }
        this.mFadeInAnimationDuration = resources.getInteger(2131492933);
        this.mFadeOutAnimationDuration = resources.getInteger(2131492934);
        this.mTranslateUpAnimationDuration = resources.getInteger(2131492936);
        this.mTranslateDownAnimationDuration = resources.getInteger(2131492935);
        this.mOrientationMargin = resources.getDimensionPixelSize(2131165757);
    }

    public final void show(WindowManager.LayoutParams layoutParams, int i) {
        if (i == 1) {
            this.mLaunchLayout.setTranslationY(500.0f);
            this.mWindowManager.addView(this.mLaunchContainer, layoutParams);
            this.mIsAddedToWindowManager = 1;
            this.mLaunchLayout.setVisibility(0);
            this.mLaunchLayout.animate().translationY(0.0f).setDuration(this.mTranslateUpAnimationDuration).setStartDelay(1000L).setListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.gamedashboard.ToastController.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    ToastController.this.mLaunchLayout.animate().setListener(null);
                    ToastController.this.hide();
                }
            });
        } else if (i == 2) {
            this.mShortcutView.setAlpha(0.0f);
            this.mWindowManager.addView(this.mShortcutView, layoutParams);
            this.mIsAddedToWindowManager = 2;
            this.mShortcutView.setVisibility(0);
            this.mShortcutView.animate().alpha(1.0f).setDuration(this.mFadeInAnimationDuration).setStartDelay(0L).setListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.gamedashboard.ToastController.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    ToastController.this.mShortcutView.animate().setListener(null);
                    ToastController.this.hide();
                }
            });
        } else if (i == 3) {
            this.mRecordSaveView.setTranslationY(500.0f);
            this.mWindowManager.addView(this.mRecordSaveContainer, layoutParams);
            this.mIsAddedToWindowManager = 3;
            this.mRecordSaveView.setVisibility(0);
            this.mRecordSaveView.animate().translationY(0.0f).setDuration(this.mTranslateUpAnimationDuration).setStartDelay(1000L).setListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.gamedashboard.ToastController.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    ToastController.this.mRecordSaveView.animate().setListener(null);
                    ToastController.this.hide();
                }
            });
        }
    }

    public final void showShortcutText(int i) {
        String str = (String) this.mContext.getResources().getText(i);
        this.mShortcutView.setText(str);
        int margin = getMargin();
        removeViewImmediate();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, -2, 0, margin, 2024, 8, -3);
        layoutParams.privateFlags |= 16;
        layoutParams.layoutInDisplayCutoutMode = 3;
        layoutParams.setTitle("ToastText");
        layoutParams.setFitInsetsTypes(0);
        layoutParams.gravity = 80;
        show(layoutParams, 2);
        this.mShortcutView.announceForAccessibility(str);
    }

    public ToastController(Context context, ConfigurationController configurationController, WindowManager windowManager, UiEventLogger uiEventLogger, NavigationModeController navigationModeController) {
        this.mContext = context;
        this.mWindowManager = windowManager;
        this.mUiEventLogger = uiEventLogger;
        this.mNavBarMode = navigationModeController.addListener(this);
        configurationController.addCallback(this);
        setResourceValues();
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public final void onConfigChanged(Configuration configuration) {
        setResourceValues();
    }

    @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
    public final void onNavigationModeChanged(int i) {
        this.mNavBarMode = i;
    }
}
