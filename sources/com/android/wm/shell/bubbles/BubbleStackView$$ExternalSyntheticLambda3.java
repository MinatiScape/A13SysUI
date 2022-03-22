package com.android.wm.shell.bubbles;

import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.statusbar.notification.row.ChannelEditorDialog;
import com.android.systemui.statusbar.notification.row.ChannelEditorDialogController;
import com.android.systemui.statusbar.notification.row.OnChannelEditorDialogFinishedListener;
import com.android.systemui.statusbar.notification.row.PartialConversationInfo;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubbleStackView$$ExternalSyntheticLambda3 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ ViewGroup f$0;

    public /* synthetic */ BubbleStackView$$ExternalSyntheticLambda3(ViewGroup viewGroup, int i) {
        this.$r8$classId = i;
        this.f$0 = viewGroup;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        ChannelEditorDialogController channelEditorDialogController;
        switch (this.$r8$classId) {
            case 0:
                BubbleStackView bubbleStackView = (BubbleStackView) this.f$0;
                Objects.requireNonNull(bubbleStackView);
                bubbleStackView.showManageMenu(!bubbleStackView.mShowingManage);
                return;
            default:
                final PartialConversationInfo partialConversationInfo = (PartialConversationInfo) this.f$0;
                int i = PartialConversationInfo.$r8$clinit;
                Objects.requireNonNull(partialConversationInfo);
                if (!partialConversationInfo.mPresentingChannelEditorDialog && (channelEditorDialogController = partialConversationInfo.mChannelEditorDialogController) != null) {
                    partialConversationInfo.mPresentingChannelEditorDialog = true;
                    channelEditorDialogController.prepareDialogForApp(partialConversationInfo.mAppName, partialConversationInfo.mPackageName, partialConversationInfo.mAppUid, partialConversationInfo.mUniqueChannelsInRow, partialConversationInfo.mPkgIcon, partialConversationInfo.mOnSettingsClickListener);
                    ChannelEditorDialogController channelEditorDialogController2 = partialConversationInfo.mChannelEditorDialogController;
                    OnChannelEditorDialogFinishedListener partialConversationInfo$$ExternalSyntheticLambda1 = new OnChannelEditorDialogFinishedListener() { // from class: com.android.systemui.statusbar.notification.row.PartialConversationInfo$$ExternalSyntheticLambda1
                        @Override // com.android.systemui.statusbar.notification.row.OnChannelEditorDialogFinishedListener
                        public final void onChannelEditorDialogFinished() {
                            PartialConversationInfo partialConversationInfo2 = PartialConversationInfo.this;
                            int i2 = PartialConversationInfo.$r8$clinit;
                            Objects.requireNonNull(partialConversationInfo2);
                            partialConversationInfo2.mPresentingChannelEditorDialog = false;
                            partialConversationInfo2.mGutsContainer.closeControls(partialConversationInfo2, false);
                        }
                    };
                    Objects.requireNonNull(channelEditorDialogController2);
                    channelEditorDialogController2.onFinishListener = partialConversationInfo$$ExternalSyntheticLambda1;
                    ChannelEditorDialogController channelEditorDialogController3 = partialConversationInfo.mChannelEditorDialogController;
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
        }
    }
}
