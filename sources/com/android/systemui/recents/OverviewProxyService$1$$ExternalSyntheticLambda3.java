package com.android.systemui.recents;

import android.content.res.Resources;
import android.view.accessibility.AccessibilityManager;
import androidx.recyclerview.R$dimen;
import com.android.internal.util.UserIcons;
import com.android.settingslib.users.EditUserPhotoController;
import com.android.systemui.qs.tiles.QuickAccessWalletTile$$ExternalSyntheticLambda0;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.util.concurrency.MessageRouter;
import com.android.systemui.util.concurrency.MessageRouterImpl;
import java.util.List;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class OverviewProxyService$1$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ OverviewProxyService$1$$ExternalSyntheticLambda3(Object obj, int i, int i2) {
        this.$r8$classId = i2;
        this.f$0 = obj;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                OverviewProxyService.AnonymousClass1 r0 = (OverviewProxyService.AnonymousClass1) this.f$0;
                int i = this.f$1;
                Objects.requireNonNull(r0);
                AccessibilityManager.getInstance(OverviewProxyService.this.mContext).notifyAccessibilityButtonClicked(i);
                return;
            case 1:
                EditUserPhotoController editUserPhotoController = (EditUserPhotoController) this.f$0;
                int i2 = this.f$1;
                Objects.requireNonNull(editUserPhotoController);
                Resources resources = editUserPhotoController.mActivity.getResources();
                R$dimen.postOnMainThread(new QuickAccessWalletTile$$ExternalSyntheticLambda0(editUserPhotoController, UserIcons.convertToBitmapAtUserIconSize(resources, UserIcons.getDefaultUserIconInColor(resources, i2)), 1));
                return;
            default:
                MessageRouterImpl messageRouterImpl = (MessageRouterImpl) this.f$0;
                int i3 = this.f$1;
                Objects.requireNonNull(messageRouterImpl);
                synchronized (messageRouterImpl.mSimpleMessageListenerMap) {
                    if (messageRouterImpl.mSimpleMessageListenerMap.containsKey(Integer.valueOf(i3))) {
                        for (MessageRouter.SimpleMessageListener simpleMessageListener : (List) messageRouterImpl.mSimpleMessageListenerMap.get(Integer.valueOf(i3))) {
                            simpleMessageListener.onMessage();
                        }
                    }
                }
                synchronized (messageRouterImpl.mIdMessageCancelers) {
                    if (messageRouterImpl.mIdMessageCancelers.containsKey(Integer.valueOf(i3)) && !((List) messageRouterImpl.mIdMessageCancelers.get(Integer.valueOf(i3))).isEmpty()) {
                        ((List) messageRouterImpl.mIdMessageCancelers.get(Integer.valueOf(i3))).remove(0);
                        if (((List) messageRouterImpl.mIdMessageCancelers.get(Integer.valueOf(i3))).isEmpty()) {
                            messageRouterImpl.mIdMessageCancelers.remove(Integer.valueOf(i3));
                        }
                    }
                }
                return;
        }
    }
}
