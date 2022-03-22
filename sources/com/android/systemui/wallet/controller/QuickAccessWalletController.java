package com.android.systemui.wallet.controller;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.provider.Settings;
import android.service.quickaccesswallet.GetWalletCardsRequest;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import android.util.Log;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.util.time.SystemClock;
import com.android.systemui.wallet.ui.WalletActivity;
import com.android.systemui.wmshell.WMShell$8$$ExternalSyntheticLambda0;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda22;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public final class QuickAccessWalletController {
    public static final long RECREATION_TIME_WINDOW = TimeUnit.MINUTES.toMillis(10);
    public final Executor mCallbackExecutor;
    public final SystemClock mClock;
    public final Context mContext;
    public AnonymousClass1 mDefaultPaymentAppObserver;
    public final Executor mExecutor;
    public long mQawClientCreatedTimeMillis;
    public QuickAccessWalletClient mQuickAccessWalletClient;
    public final SecureSettings mSecureSettings;
    public AnonymousClass2 mWalletPreferenceObserver;
    public int mWalletPreferenceChangeEvents = 0;
    public int mDefaultPaymentAppChangeEvents = 0;
    public boolean mWalletEnabled = false;

    /* renamed from: com.android.systemui.wallet.controller.QuickAccessWalletController$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass1 extends ContentObserver {
        public static final /* synthetic */ int $r8$clinit = 0;
        public final /* synthetic */ QuickAccessWalletClient.OnWalletCardsRetrievedCallback val$cardsRetriever;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass1(QuickAccessWalletClient.OnWalletCardsRetrievedCallback onWalletCardsRetrievedCallback) {
            super(null);
            this.val$cardsRetriever = onWalletCardsRetrievedCallback;
        }

        @Override // android.database.ContentObserver
        public final void onChange(boolean z) {
            QuickAccessWalletController.this.mExecutor.execute(new BubbleStackView$$ExternalSyntheticLambda22(this, this.val$cardsRetriever, 1));
        }
    }

    /* renamed from: com.android.systemui.wallet.controller.QuickAccessWalletController$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass2 extends ContentObserver {
        public static final /* synthetic */ int $r8$clinit = 0;

        public AnonymousClass2() {
            super(null);
        }

        @Override // android.database.ContentObserver
        public final void onChange(boolean z) {
            QuickAccessWalletController.this.mExecutor.execute(new WMShell$8$$ExternalSyntheticLambda0(this, 5));
        }
    }

    /* loaded from: classes.dex */
    public enum WalletChangeEvent {
        DEFAULT_PAYMENT_APP_CHANGE,
        WALLET_PREFERENCE_CHANGE
    }

    public final void setupWalletChangeObservers(QuickAccessWalletClient.OnWalletCardsRetrievedCallback onWalletCardsRetrievedCallback, WalletChangeEvent... walletChangeEventArr) {
        for (WalletChangeEvent walletChangeEvent : walletChangeEventArr) {
            if (walletChangeEvent == WalletChangeEvent.WALLET_PREFERENCE_CHANGE) {
                if (this.mWalletPreferenceObserver == null) {
                    this.mWalletPreferenceObserver = new AnonymousClass2();
                    this.mSecureSettings.registerContentObserver(Settings.Secure.getUriFor("lockscreen_show_wallet"), false, this.mWalletPreferenceObserver);
                }
                this.mWalletPreferenceChangeEvents++;
            } else if (walletChangeEvent == WalletChangeEvent.DEFAULT_PAYMENT_APP_CHANGE) {
                if (this.mDefaultPaymentAppObserver == null) {
                    this.mDefaultPaymentAppObserver = new AnonymousClass1(onWalletCardsRetrievedCallback);
                    this.mSecureSettings.registerContentObserver(Settings.Secure.getUriFor("nfc_payment_default_component"), false, this.mDefaultPaymentAppObserver);
                }
                this.mDefaultPaymentAppChangeEvents++;
            }
        }
    }

    public final void unregisterWalletChangeObservers(WalletChangeEvent... walletChangeEventArr) {
        AnonymousClass1 r2;
        AnonymousClass2 r3;
        for (WalletChangeEvent walletChangeEvent : walletChangeEventArr) {
            if (walletChangeEvent == WalletChangeEvent.WALLET_PREFERENCE_CHANGE && (r3 = this.mWalletPreferenceObserver) != null) {
                int i = this.mWalletPreferenceChangeEvents - 1;
                this.mWalletPreferenceChangeEvents = i;
                if (i == 0) {
                    this.mSecureSettings.unregisterContentObserver(r3);
                }
            } else if (walletChangeEvent == WalletChangeEvent.DEFAULT_PAYMENT_APP_CHANGE && (r2 = this.mDefaultPaymentAppObserver) != null) {
                int i2 = this.mDefaultPaymentAppChangeEvents - 1;
                this.mDefaultPaymentAppChangeEvents = i2;
                if (i2 == 0) {
                    this.mSecureSettings.unregisterContentObserver(r2);
                }
            }
        }
    }

    public final void queryWalletCards(QuickAccessWalletClient.OnWalletCardsRetrievedCallback onWalletCardsRetrievedCallback) {
        if (this.mClock.elapsedRealtime() - this.mQawClientCreatedTimeMillis > RECREATION_TIME_WINDOW) {
            Log.i("QAWController", "Re-creating the QAW client to avoid stale.");
            reCreateWalletClient();
        }
        if (!this.mQuickAccessWalletClient.isWalletFeatureAvailable()) {
            Log.d("QAWController", "QuickAccessWallet feature is not available.");
            return;
        }
        this.mQuickAccessWalletClient.getWalletCards(this.mExecutor, new GetWalletCardsRequest(this.mContext.getResources().getDimensionPixelSize(2131167322), this.mContext.getResources().getDimensionPixelSize(2131167321), this.mContext.getResources().getDimensionPixelSize(2131167318), 1), onWalletCardsRetrievedCallback);
    }

    public final void reCreateWalletClient() {
        this.mQuickAccessWalletClient = QuickAccessWalletClient.create(this.mContext);
        this.mQawClientCreatedTimeMillis = this.mClock.elapsedRealtime();
    }

    public final void startQuickAccessUiIntent(final ActivityStarter activityStarter, final ActivityLaunchAnimator.Controller controller, final boolean z) {
        if (this.mQuickAccessWalletClient.useTargetActivityForQuickAccess() || !z) {
            this.mQuickAccessWalletClient.getWalletPendingIntent(this.mCallbackExecutor, new QuickAccessWalletClient.WalletPendingIntentCallback() { // from class: com.android.systemui.wallet.controller.QuickAccessWalletController$$ExternalSyntheticLambda0
                public final void onWalletPendingIntentRetrieved(PendingIntent pendingIntent) {
                    QuickAccessWalletController quickAccessWalletController = QuickAccessWalletController.this;
                    boolean z2 = z;
                    ActivityStarter activityStarter2 = activityStarter;
                    ActivityLaunchAnimator.Controller controller2 = controller;
                    if (pendingIntent == null) {
                        Intent createWalletIntent = quickAccessWalletController.mQuickAccessWalletClient.createWalletIntent();
                        if (createWalletIntent == null) {
                            createWalletIntent = new Intent(quickAccessWalletController.mContext, WalletActivity.class).setAction("android.intent.action.VIEW");
                        }
                        if (z2) {
                            activityStarter2.startActivity(createWalletIntent, true, controller2, true);
                        } else {
                            activityStarter2.postStartActivityDismissingKeyguard(createWalletIntent, 0, controller2);
                        }
                    } else {
                        Objects.requireNonNull(quickAccessWalletController);
                        activityStarter2.postStartActivityDismissingKeyguard(pendingIntent, controller2);
                    }
                }
            });
            return;
        }
        Intent action = new Intent(this.mContext, WalletActivity.class).setAction("android.intent.action.VIEW");
        if (z) {
            activityStarter.startActivity(action, true, controller, true);
        } else {
            activityStarter.postStartActivityDismissingKeyguard(action, 0, controller);
        }
    }

    public final void updateWalletPreference() {
        boolean z;
        if (!this.mQuickAccessWalletClient.isWalletServiceAvailable() || !this.mQuickAccessWalletClient.isWalletFeatureAvailable() || !this.mQuickAccessWalletClient.isWalletFeatureAvailableWhenDeviceLocked()) {
            z = false;
        } else {
            z = true;
        }
        this.mWalletEnabled = z;
    }

    public QuickAccessWalletController(Context context, Executor executor, Executor executor2, SecureSettings secureSettings, QuickAccessWalletClient quickAccessWalletClient, SystemClock systemClock) {
        this.mContext = context;
        this.mExecutor = executor;
        this.mCallbackExecutor = executor2;
        this.mSecureSettings = secureSettings;
        this.mQuickAccessWalletClient = quickAccessWalletClient;
        this.mClock = systemClock;
        this.mQawClientCreatedTimeMillis = systemClock.elapsedRealtime();
    }
}
