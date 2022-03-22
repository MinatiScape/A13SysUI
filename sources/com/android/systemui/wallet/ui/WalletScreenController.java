package com.android.systemui.wallet.ui;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Handler;
import android.service.quickaccesswallet.GetWalletCardsError;
import android.service.quickaccesswallet.GetWalletCardsRequest;
import android.service.quickaccesswallet.GetWalletCardsResponse;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import android.service.quickaccesswallet.SelectWalletCardRequest;
import android.service.quickaccesswallet.WalletCard;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.power.PowerUI$$ExternalSyntheticLambda0;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.wallet.ui.WalletCardCarousel;
import com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda5;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public final class WalletScreenController implements WalletCardCarousel.OnSelectionListener, QuickAccessWalletClient.OnWalletCardsRetrievedCallback, KeyguardStateController.Callback {
    public static final /* synthetic */ int $r8$clinit = 0;
    public static final long SELECTION_DELAY_MILLIS = TimeUnit.SECONDS.toMillis(30);
    public final ActivityStarter mActivityStarter;
    public final WalletCardCarousel mCardCarousel;
    public Context mContext;
    public final Executor mExecutor;
    public final FalsingManager mFalsingManager;
    public final Handler mHandler;
    @VisibleForTesting
    public boolean mIsDismissed;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final SharedPreferences mPrefs;
    @VisibleForTesting
    public String mSelectedCardId;
    public final PowerUI$$ExternalSyntheticLambda0 mSelectionRunnable = new PowerUI$$ExternalSyntheticLambda0(this, 3);
    public final UiEventLogger mUiEventLogger;
    public final QuickAccessWalletClient mWalletClient;
    public final WalletView mWalletView;

    @VisibleForTesting
    /* loaded from: classes.dex */
    public static class QAWalletCardViewInfo implements WalletCardViewInfo {
        public final Drawable mCardDrawable;
        public final Drawable mIconDrawable;
        public final WalletCard mWalletCard;

        @Override // com.android.systemui.wallet.ui.WalletCardViewInfo
        public final String getCardId() {
            return this.mWalletCard.getCardId();
        }

        @Override // com.android.systemui.wallet.ui.WalletCardViewInfo
        public final CharSequence getContentDescription() {
            return this.mWalletCard.getContentDescription();
        }

        @Override // com.android.systemui.wallet.ui.WalletCardViewInfo
        public final CharSequence getLabel() {
            CharSequence cardLabel = this.mWalletCard.getCardLabel();
            if (cardLabel == null) {
                return "";
            }
            return cardLabel;
        }

        @Override // com.android.systemui.wallet.ui.WalletCardViewInfo
        public final PendingIntent getPendingIntent() {
            return this.mWalletCard.getPendingIntent();
        }

        public QAWalletCardViewInfo(Context context, WalletCard walletCard) {
            Drawable drawable;
            this.mWalletCard = walletCard;
            this.mCardDrawable = walletCard.getCardImage().loadDrawable(context);
            Icon cardIcon = walletCard.getCardIcon();
            if (cardIcon == null) {
                drawable = null;
            } else {
                drawable = cardIcon.loadDrawable(context);
            }
            this.mIconDrawable = drawable;
        }

        @Override // com.android.systemui.wallet.ui.WalletCardViewInfo
        public final Drawable getCardDrawable() {
            return this.mCardDrawable;
        }

        @Override // com.android.systemui.wallet.ui.WalletCardViewInfo
        public final Drawable getIcon() {
            return this.mIconDrawable;
        }
    }

    public final void onCardSelected(WalletCardViewInfo walletCardViewInfo) {
        if (!this.mIsDismissed) {
            String str = this.mSelectedCardId;
            if (str != null && !str.equals(walletCardViewInfo.getCardId())) {
                this.mUiEventLogger.log(WalletUiEvent.QAW_CHANGE_CARD);
            }
            this.mSelectedCardId = walletCardViewInfo.getCardId();
            selectCard();
        }
    }

    public final void onWalletCardRetrievalError(GetWalletCardsError getWalletCardsError) {
        this.mHandler.post(new BubbleController$$ExternalSyntheticLambda5(this, getWalletCardsError, 2));
    }

    public final void onWalletCardsRetrieved(final GetWalletCardsResponse getWalletCardsResponse) {
        if (!this.mIsDismissed) {
            Log.i("WalletScreenCtrl", "Successfully retrieved wallet cards.");
            List<WalletCard> walletCards = getWalletCardsResponse.getWalletCards();
            final ArrayList arrayList = new ArrayList(walletCards.size());
            for (WalletCard walletCard : walletCards) {
                arrayList.add(new QAWalletCardViewInfo(this.mContext, walletCard));
            }
            this.mHandler.post(new Runnable() { // from class: com.android.systemui.wallet.ui.WalletScreenController$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    boolean z;
                    final WalletScreenController walletScreenController = WalletScreenController.this;
                    List<WalletCardViewInfo> list = arrayList;
                    GetWalletCardsResponse getWalletCardsResponse2 = getWalletCardsResponse;
                    Objects.requireNonNull(walletScreenController);
                    if (!walletScreenController.mIsDismissed) {
                        if (list.isEmpty()) {
                            walletScreenController.showEmptyStateView();
                        } else {
                            int selectedIndex = getWalletCardsResponse2.getSelectedIndex();
                            if (selectedIndex >= list.size()) {
                                Log.w("WalletScreenCtrl", "Invalid selected card index, showing empty state.");
                                walletScreenController.showEmptyStateView();
                            } else {
                                if (!walletScreenController.mKeyguardUpdateMonitor.isUdfpsEnrolled() || !walletScreenController.mKeyguardUpdateMonitor.isFingerprintDetectionRunning()) {
                                    z = false;
                                } else {
                                    z = true;
                                }
                                walletScreenController.mWalletView.showCardCarousel(list, selectedIndex, true ^ walletScreenController.mKeyguardStateController.isUnlocked(), z);
                            }
                        }
                        walletScreenController.mUiEventLogger.log(WalletUiEvent.QAW_IMPRESSION);
                        walletScreenController.mWalletView.setMinimumHeight(0);
                        walletScreenController.mWalletView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.android.systemui.wallet.ui.WalletScreenController.1
                            @Override // android.view.View.OnLayoutChangeListener
                            public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                                WalletScreenController.this.mWalletView.removeOnLayoutChangeListener(this);
                                WalletScreenController.this.mPrefs.edit().putInt("wallet_view_height", i4 - i2).apply();
                            }
                        });
                    }
                }
            });
        }
    }

    public final void queryWalletCards() {
        if (!this.mIsDismissed) {
            WalletCardCarousel walletCardCarousel = this.mCardCarousel;
            Objects.requireNonNull(walletCardCarousel);
            int i = walletCardCarousel.mCardWidthPx;
            WalletCardCarousel walletCardCarousel2 = this.mCardCarousel;
            Objects.requireNonNull(walletCardCarousel2);
            int i2 = walletCardCarousel2.mCardHeightPx;
            if (i != 0 && i2 != 0) {
                WalletView walletView = this.mWalletView;
                Objects.requireNonNull(walletView);
                walletView.setVisibility(0);
                WalletView walletView2 = this.mWalletView;
                Objects.requireNonNull(walletView2);
                walletView2.mErrorView.setVisibility(8);
                this.mWalletClient.getWalletCards(this.mExecutor, new GetWalletCardsRequest(i, i2, this.mContext.getResources().getDimensionPixelSize(2131167319), 10), this);
            }
        }
    }

    public final void selectCard() {
        this.mHandler.removeCallbacks(this.mSelectionRunnable);
        String str = this.mSelectedCardId;
        if (!this.mIsDismissed && str != null) {
            this.mWalletClient.selectWalletCard(new SelectWalletCardRequest(str));
            this.mHandler.postDelayed(this.mSelectionRunnable, SELECTION_DELAY_MILLIS);
        }
    }

    public final void showEmptyStateView() {
        Drawable logo = this.mWalletClient.getLogo();
        CharSequence serviceLabel = this.mWalletClient.getServiceLabel();
        CharSequence shortcutLongLabel = this.mWalletClient.getShortcutLongLabel();
        Intent createWalletIntent = this.mWalletClient.createWalletIntent();
        if (logo == null || TextUtils.isEmpty(serviceLabel) || TextUtils.isEmpty(shortcutLongLabel) || createWalletIntent == null) {
            Log.w("WalletScreenCtrl", "QuickAccessWalletService manifest entry mis-configured");
            WalletView walletView = this.mWalletView;
            Objects.requireNonNull(walletView);
            walletView.setVisibility(8);
            this.mPrefs.edit().putInt("wallet_view_height", 0).apply();
            return;
        }
        this.mWalletView.showEmptyStateView(logo, serviceLabel, shortcutLongLabel, new WalletScreenController$$ExternalSyntheticLambda0(this, createWalletIntent, 0));
    }

    public WalletScreenController(Context context, WalletView walletView, QuickAccessWalletClient quickAccessWalletClient, ActivityStarter activityStarter, Executor executor, Handler handler, UserTracker userTracker, FalsingManager falsingManager, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardStateController keyguardStateController, UiEventLogger uiEventLogger) {
        this.mContext = context;
        this.mWalletClient = quickAccessWalletClient;
        this.mActivityStarter = activityStarter;
        this.mExecutor = executor;
        this.mHandler = handler;
        this.mFalsingManager = falsingManager;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mKeyguardStateController = keyguardStateController;
        this.mUiEventLogger = uiEventLogger;
        SharedPreferences sharedPreferences = userTracker.getUserContext().getSharedPreferences("WalletScreenCtrl", 0);
        this.mPrefs = sharedPreferences;
        this.mWalletView = walletView;
        int i = sharedPreferences.getInt("wallet_view_height", -1);
        walletView.setMinimumHeight(i == -1 ? this.mContext.getResources().getDimensionPixelSize(2131166368) : i);
        walletView.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
        WalletCardCarousel walletCardCarousel = walletView.mCardCarousel;
        this.mCardCarousel = walletCardCarousel;
        if (walletCardCarousel != null) {
            walletCardCarousel.mSelectionListener = this;
        }
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
    public final void onKeyguardFadingAwayChanged() {
        queryWalletCards();
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
    public final void onUnlockedChanged() {
        queryWalletCards();
    }
}
