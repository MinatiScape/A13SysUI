package com.android.systemui.shortcut;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import com.android.internal.policy.DividerSnapAlgorithm;
import com.android.internal.policy.IShortcutService;
import com.android.wm.shell.legacysplitscreen.DividerView;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import java.util.Objects;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class ShortcutKeyServiceProxy extends IShortcutService.Stub {
    public Callbacks mCallbacks;
    public final Object mLock = new Object();
    public final H mHandler = new H();

    /* loaded from: classes.dex */
    public interface Callbacks {
    }

    /* loaded from: classes.dex */
    public final class H extends Handler {
        public H() {
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            if (message.what == 1) {
                Callbacks callbacks = ShortcutKeyServiceProxy.this.mCallbacks;
                final long longValue = ((Long) message.obj).longValue();
                final ShortcutKeyDispatcher shortcutKeyDispatcher = (ShortcutKeyDispatcher) callbacks;
                Objects.requireNonNull(shortcutKeyDispatcher);
                int i = shortcutKeyDispatcher.mContext.getResources().getConfiguration().orientation;
                if ((longValue == 281474976710727L || longValue == 281474976710728L) && i == 2) {
                    shortcutKeyDispatcher.mSplitScreenOptional.ifPresent(new Consumer() { // from class: com.android.systemui.shortcut.ShortcutKeyDispatcher$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            DividerSnapAlgorithm.SnapTarget snapTarget;
                            ShortcutKeyDispatcher shortcutKeyDispatcher2 = ShortcutKeyDispatcher.this;
                            long j = longValue;
                            LegacySplitScreen legacySplitScreen = (LegacySplitScreen) obj;
                            Objects.requireNonNull(shortcutKeyDispatcher2);
                            if (legacySplitScreen.isDividerVisible()) {
                                DividerView dividerView = legacySplitScreen.getDividerView();
                                DividerSnapAlgorithm snapAlgorithm = dividerView.getSnapAlgorithm();
                                DividerSnapAlgorithm.SnapTarget calculateNonDismissingSnapTarget = snapAlgorithm.calculateNonDismissingSnapTarget(dividerView.getCurrentPosition());
                                if (j == 281474976710727L) {
                                    snapTarget = snapAlgorithm.getPreviousTarget(calculateNonDismissingSnapTarget);
                                } else {
                                    snapTarget = snapAlgorithm.getNextTarget(calculateNonDismissingSnapTarget);
                                }
                                dividerView.startDragging(true, false);
                                dividerView.stopDragging(snapTarget.position, 0.0f);
                                return;
                            }
                            legacySplitScreen.splitPrimaryTask();
                        }
                    });
                }
            }
        }
    }

    public final void notifyShortcutKeyPressed(long j) throws RemoteException {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(1, Long.valueOf(j)).sendToTarget();
        }
    }

    public ShortcutKeyServiceProxy(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }
}
