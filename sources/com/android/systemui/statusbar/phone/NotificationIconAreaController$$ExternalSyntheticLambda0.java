package com.android.systemui.statusbar.phone;

import android.app.RemoteAction;
import android.content.pm.ParceledListSlice;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.pip.PinnedStackListenerForwarder;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiConsumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationIconAreaController$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ NotificationIconAreaController$$ExternalSyntheticLambda0(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                NotificationIconAreaController notificationIconAreaController = (NotificationIconAreaController) this.f$0;
                Objects.requireNonNull(notificationIconAreaController);
                notificationIconAreaController.updateTintForIcon((StatusBarIconView) this.f$1, notificationIconAreaController.mIconTint);
                return;
            case 1:
                StatusBarNotificationPresenter statusBarNotificationPresenter = (StatusBarNotificationPresenter) this.f$0;
                Objects.requireNonNull(statusBarNotificationPresenter);
                statusBarNotificationPresenter.updateNotificationViews((String) this.f$1);
                return;
            case 2:
                LegacySplitScreenController.SplitScreenImpl splitScreenImpl = (LegacySplitScreenController.SplitScreenImpl) this.f$0;
                BiConsumer biConsumer = (BiConsumer) this.f$1;
                Objects.requireNonNull(splitScreenImpl);
                LegacySplitScreenController legacySplitScreenController = LegacySplitScreenController.this;
                Objects.requireNonNull(legacySplitScreenController);
                synchronized (legacySplitScreenController.mBoundsChangedListeners) {
                    legacySplitScreenController.mBoundsChangedListeners.add(new WeakReference<>(biConsumer));
                }
                return;
            default:
                PinnedStackListenerForwarder.PinnedTaskListenerImpl pinnedTaskListenerImpl = (PinnedStackListenerForwarder.PinnedTaskListenerImpl) this.f$0;
                ParceledListSlice<RemoteAction> parceledListSlice = (ParceledListSlice) this.f$1;
                int i = PinnedStackListenerForwarder.PinnedTaskListenerImpl.$r8$clinit;
                Objects.requireNonNull(pinnedTaskListenerImpl);
                PinnedStackListenerForwarder pinnedStackListenerForwarder = PinnedStackListenerForwarder.this;
                Objects.requireNonNull(pinnedStackListenerForwarder);
                Iterator<PinnedStackListenerForwarder.PinnedTaskListener> it = pinnedStackListenerForwarder.mListeners.iterator();
                while (it.hasNext()) {
                    it.next().onActionsChanged(parceledListSlice);
                }
                return;
        }
    }
}
