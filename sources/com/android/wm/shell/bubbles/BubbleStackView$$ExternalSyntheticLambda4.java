package com.android.wm.shell.bubbles;

import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import com.android.keyguard.KeyguardPasswordViewController;
import com.android.systemui.qs.customize.QSCustomizerController;
import com.android.systemui.statusbar.notification.row.ChannelEditorDialog;
import com.android.systemui.statusbar.notification.row.ChannelEditorDialogController;
import com.android.systemui.statusbar.notification.row.NotificationInfo;
import com.android.systemui.statusbar.notification.row.OnChannelEditorDialogFinishedListener;
import com.android.systemui.wallet.ui.WalletActivity;
import com.android.systemui.wallet.ui.WalletActivity$$ExternalSyntheticLambda2;
import com.android.systemui.wallet.ui.WalletUiEvent;
import com.google.android.systemui.gamedashboard.GameMenuActivity;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubbleStackView$$ExternalSyntheticLambda4 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ BubbleStackView$$ExternalSyntheticLambda4(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        ChannelEditorDialogController channelEditorDialogController;
        switch (this.$r8$classId) {
            case 0:
                BubbleStackView bubbleStackView = (BubbleStackView) this.f$0;
                Objects.requireNonNull(bubbleStackView);
                bubbleStackView.showManageMenu(false);
                BubbleData bubbleData = bubbleStackView.mBubbleData;
                Objects.requireNonNull(bubbleData);
                BubbleViewProvider bubbleViewProvider = bubbleData.mSelectedBubble;
                if (bubbleViewProvider != null && bubbleStackView.mBubbleData.hasBubbleInStackWithKey(bubbleViewProvider.getKey())) {
                    bubbleStackView.mBubbleData.dismissBubbleWithKey(bubbleViewProvider.getKey(), 1);
                    return;
                }
                return;
            case 1:
                KeyguardPasswordViewController keyguardPasswordViewController = (KeyguardPasswordViewController) this.f$0;
                Objects.requireNonNull(keyguardPasswordViewController);
                keyguardPasswordViewController.mKeyguardSecurityCallback.userActivity();
                return;
            case 2:
                QSCustomizerController qSCustomizerController = (QSCustomizerController) this.f$0;
                Objects.requireNonNull(qSCustomizerController);
                qSCustomizerController.hide();
                return;
            case 3:
                final NotificationInfo notificationInfo = (NotificationInfo) this.f$0;
                int i = NotificationInfo.$r8$clinit;
                Objects.requireNonNull(notificationInfo);
                if (!notificationInfo.mPresentingChannelEditorDialog && (channelEditorDialogController = notificationInfo.mChannelEditorDialogController) != null) {
                    notificationInfo.mPresentingChannelEditorDialog = true;
                    channelEditorDialogController.prepareDialogForApp(notificationInfo.mAppName, notificationInfo.mPackageName, notificationInfo.mAppUid, notificationInfo.mUniqueChannelsInRow, notificationInfo.mPkgIcon, notificationInfo.mOnSettingsClickListener);
                    ChannelEditorDialogController channelEditorDialogController2 = notificationInfo.mChannelEditorDialogController;
                    OnChannelEditorDialogFinishedListener notificationInfo$$ExternalSyntheticLambda1 = new OnChannelEditorDialogFinishedListener() { // from class: com.android.systemui.statusbar.notification.row.NotificationInfo$$ExternalSyntheticLambda1
                        @Override // com.android.systemui.statusbar.notification.row.OnChannelEditorDialogFinishedListener
                        public final void onChannelEditorDialogFinished() {
                            NotificationInfo notificationInfo2 = NotificationInfo.this;
                            int i2 = NotificationInfo.$r8$clinit;
                            Objects.requireNonNull(notificationInfo2);
                            notificationInfo2.mPresentingChannelEditorDialog = false;
                            notificationInfo2.mGutsContainer.closeControls(notificationInfo2, false);
                        }
                    };
                    Objects.requireNonNull(channelEditorDialogController2);
                    channelEditorDialogController2.onFinishListener = notificationInfo$$ExternalSyntheticLambda1;
                    ChannelEditorDialogController channelEditorDialogController3 = notificationInfo.mChannelEditorDialogController;
                    Objects.requireNonNull(channelEditorDialogController3);
                    if (channelEditorDialogController3.prepared) {
                        ChannelEditorDialog channelEditorDialog = channelEditorDialogController3.dialog;
                        if (channelEditorDialog == null) {
                            channelEditorDialog = null;
                        }
                        channelEditorDialog.show();
                        return;
                    }
                    throw new IllegalStateException("Must call prepareDialogForApp() before calling show()");
                }
                return;
            case 4:
                WalletActivity walletActivity = (WalletActivity) this.f$0;
                int i2 = WalletActivity.$r8$clinit;
                Objects.requireNonNull(walletActivity);
                Log.d("WalletActivity", "Wallet action button is clicked.");
                if (walletActivity.mFalsingManager.isFalseTap(1)) {
                    Log.d("WalletActivity", "False tap detected on wallet action button.");
                    return;
                }
                walletActivity.mUiEventLogger.log(WalletUiEvent.QAW_UNLOCK_FROM_UNLOCK_BUTTON);
                walletActivity.mKeyguardDismissUtil.executeWhenUnlocked(WalletActivity$$ExternalSyntheticLambda2.INSTANCE, false, false);
                return;
            default:
                GameMenuActivity gameMenuActivity = (GameMenuActivity) this.f$0;
                IntentFilter intentFilter = GameMenuActivity.DND_FILTER;
                Objects.requireNonNull(gameMenuActivity);
                gameMenuActivity.navigateToGameModeView();
                return;
        }
    }
}
