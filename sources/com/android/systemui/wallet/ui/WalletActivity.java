package com.android.systemui.wallet.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.biometrics.BiometricSourceType;
import android.os.Bundle;
import android.os.Handler;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import android.service.quickaccesswallet.WalletServiceEvent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.settingslib.Utils;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.LifecycleActivity;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda4;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class WalletActivity extends LifecycleActivity implements QuickAccessWalletClient.WalletServiceEventListener {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final ActivityStarter mActivityStarter;
    public final Executor mExecutor;
    public FalsingCollector mFalsingCollector;
    public final FalsingManager mFalsingManager;
    public final Handler mHandler;
    public boolean mHasRegisteredListener;
    public final KeyguardDismissUtil mKeyguardDismissUtil;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public AnonymousClass1 mKeyguardUpdateMonitorCallback;
    public final StatusBarKeyguardViewManager mKeyguardViewManager;
    public final UiEventLogger mUiEventLogger;
    public final UserTracker mUserTracker;
    public QuickAccessWalletClient mWalletClient;
    public WalletScreenController mWalletScreenController;

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onDestroy() {
        this.mKeyguardStateController.removeCallback(this.mWalletScreenController);
        AnonymousClass1 r0 = this.mKeyguardUpdateMonitorCallback;
        if (r0 != null) {
            this.mKeyguardUpdateMonitor.removeCallback(r0);
        }
        WalletScreenController walletScreenController = this.mWalletScreenController;
        Objects.requireNonNull(walletScreenController);
        if (!walletScreenController.mIsDismissed) {
            walletScreenController.mIsDismissed = true;
            walletScreenController.mSelectedCardId = null;
            walletScreenController.mHandler.removeCallbacks(walletScreenController.mSelectionRunnable);
            walletScreenController.mWalletClient.notifyWalletDismissed();
            WalletView walletView = walletScreenController.mWalletView;
            Objects.requireNonNull(walletView);
            if (walletView.mCardCarouselContainer.getVisibility() == 0) {
                walletView.mCardCarousel.animate().translationX(walletView.mAnimationTranslationX).setInterpolator(walletView.mOutInterpolator).setDuration(200L).start();
                walletView.mCardCarouselContainer.animate().alpha(0.0f).setDuration(100L).setStartDelay(50L).start();
            }
            walletScreenController.mContext = null;
        }
        this.mWalletClient.removeWalletServiceEventListener(this);
        this.mHasRegisteredListener = false;
        super.onDestroy();
    }

    public WalletActivity(KeyguardStateController keyguardStateController, KeyguardDismissUtil keyguardDismissUtil, ActivityStarter activityStarter, Executor executor, Handler handler, FalsingManager falsingManager, FalsingCollector falsingCollector, UserTracker userTracker, KeyguardUpdateMonitor keyguardUpdateMonitor, StatusBarKeyguardViewManager statusBarKeyguardViewManager, UiEventLogger uiEventLogger) {
        this.mKeyguardStateController = keyguardStateController;
        this.mKeyguardDismissUtil = keyguardDismissUtil;
        this.mActivityStarter = activityStarter;
        this.mExecutor = executor;
        this.mHandler = handler;
        this.mFalsingManager = falsingManager;
        this.mFalsingCollector = falsingCollector;
        this.mUserTracker = userTracker;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mKeyguardViewManager = statusBarKeyguardViewManager;
        this.mUiEventLogger = uiEventLogger;
    }

    /* JADX WARN: Type inference failed for: r0v12, types: [com.android.systemui.wallet.ui.WalletActivity$1] */
    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addFlags(Integer.MIN_VALUE);
        requestWindowFeature(1);
        setContentView(2131624436);
        Toolbar toolbar = (Toolbar) findViewById(2131427412);
        if (toolbar != null) {
            setActionBar(toolbar);
        }
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getActionBar();
        Drawable drawable = getDrawable(2131231790);
        drawable.setTint(getColor(2131100295));
        actionBar.setHomeAsUpIndicator(drawable);
        getActionBar().setHomeActionContentDescription(2131951707);
        WalletView walletView = (WalletView) requireViewById(2131429243);
        this.mWalletClient = QuickAccessWalletClient.create(this);
        this.mWalletScreenController = new WalletScreenController(this, walletView, this.mWalletClient, this.mActivityStarter, this.mExecutor, this.mHandler, this.mUserTracker, this.mFalsingManager, this.mKeyguardUpdateMonitor, this.mKeyguardStateController, this.mUiEventLogger);
        this.mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.wallet.ui.WalletActivity.1
            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public final void onBiometricRunningStateChanged(boolean z, BiometricSourceType biometricSourceType) {
                Log.d("WalletActivity", "Biometric running state has changed.");
                WalletActivity.this.mWalletScreenController.queryWalletCards();
            }
        };
        walletView.mFalsingCollector = this.mFalsingCollector;
        walletView.mShowWalletAppOnClickListener = new WalletActivity$$ExternalSyntheticLambda0(this, 0);
        walletView.mDeviceLockedActionOnClickListener = new BubbleStackView$$ExternalSyntheticLambda4(this, 4);
    }

    @Override // android.app.Activity
    public final boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(2131689475, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override // android.app.Activity
    public final boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            finish();
            return true;
        } else if (itemId != 2131429241) {
            return super.onOptionsItemSelected(menuItem);
        } else {
            this.mActivityStarter.startActivity(new Intent("android.settings.LOCK_SCREEN_SETTINGS").addFlags(335544320), true);
            return true;
        }
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onPause() {
        super.onPause();
        this.mKeyguardViewManager.requestFp(false);
        StatusBarKeyguardViewManager statusBarKeyguardViewManager = this.mKeyguardViewManager;
        Objects.requireNonNull(statusBarKeyguardViewManager);
        KeyguardUpdateMonitor keyguardUpdateMonitor = statusBarKeyguardViewManager.mKeyguardUpdateManager;
        Objects.requireNonNull(keyguardUpdateMonitor);
        keyguardUpdateMonitor.mOccludingAppRequestingFace = false;
        keyguardUpdateMonitor.updateFaceListeningState(2);
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onResume() {
        super.onResume();
        this.mWalletScreenController.queryWalletCards();
        StatusBarKeyguardViewManager statusBarKeyguardViewManager = this.mKeyguardViewManager;
        Utils.getColorAttrDefaultColor(this, 17956900);
        statusBarKeyguardViewManager.requestFp(true);
        StatusBarKeyguardViewManager statusBarKeyguardViewManager2 = this.mKeyguardViewManager;
        Objects.requireNonNull(statusBarKeyguardViewManager2);
        KeyguardUpdateMonitor keyguardUpdateMonitor = statusBarKeyguardViewManager2.mKeyguardUpdateManager;
        Objects.requireNonNull(keyguardUpdateMonitor);
        keyguardUpdateMonitor.mOccludingAppRequestingFace = true;
        keyguardUpdateMonitor.updateFaceListeningState(2);
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onStart() {
        super.onStart();
        if (!this.mHasRegisteredListener) {
            this.mWalletClient.addWalletServiceEventListener(this);
            this.mHasRegisteredListener = true;
        }
        this.mKeyguardStateController.addCallback(this.mWalletScreenController);
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardUpdateMonitorCallback);
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onStop() {
        super.onStop();
        finish();
    }

    public final void onWalletServiceEvent(WalletServiceEvent walletServiceEvent) {
        int eventType = walletServiceEvent.getEventType();
        if (eventType == 1) {
            return;
        }
        if (eventType != 2) {
            Log.w("WalletActivity", "onWalletServiceEvent: Unknown event type");
        } else {
            this.mWalletScreenController.queryWalletCards();
        }
    }
}
