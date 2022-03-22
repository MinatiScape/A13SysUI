package com.android.wm.shell.pip;

import android.app.RemoteAction;
import android.content.ComponentName;
import android.content.pm.ParceledListSlice;
import android.view.IPinnedTaskListener;
import com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda1;
import com.android.systemui.statusbar.phone.NotificationIconAreaController$$ExternalSyntheticLambda0;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda22;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.pip.PinnedStackListenerForwarder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class PinnedStackListenerForwarder {
    public final PinnedTaskListenerImpl mListenerImpl = new PinnedTaskListenerImpl();
    public final ArrayList<PinnedTaskListener> mListeners = new ArrayList<>();
    public final ShellExecutor mMainExecutor;

    /* loaded from: classes.dex */
    public static class PinnedTaskListener {
        public void onActionsChanged(ParceledListSlice<RemoteAction> parceledListSlice) {
        }

        public void onActivityHidden(ComponentName componentName) {
        }

        public void onAspectRatioChanged(float f) {
        }

        public void onExpandedAspectRatioChanged(float f) {
        }

        public void onImeVisibilityChanged(boolean z, int i) {
        }

        public void onMovementBoundsChanged(boolean z) {
        }
    }

    /* loaded from: classes.dex */
    public class PinnedTaskListenerImpl extends IPinnedTaskListener.Stub {
        public static final /* synthetic */ int $r8$clinit = 0;

        public PinnedTaskListenerImpl() {
        }

        public final void onActionsChanged(ParceledListSlice<RemoteAction> parceledListSlice) {
            PinnedStackListenerForwarder.this.mMainExecutor.execute(new NotificationIconAreaController$$ExternalSyntheticLambda0(this, parceledListSlice, 3));
        }

        public final void onActivityHidden(ComponentName componentName) {
            PinnedStackListenerForwarder.this.mMainExecutor.execute(new BubbleStackView$$ExternalSyntheticLambda22(this, componentName, 3));
        }

        /* JADX WARN: Multi-variable type inference failed */
        public final void onAspectRatioChanged(float f) {
            PinnedStackListenerForwarder.this.mMainExecutor.execute(new OverviewProxyService$1$$ExternalSyntheticLambda1(this, f, 1));
        }

        public final void onExpandedAspectRatioChanged(final float f) {
            PinnedStackListenerForwarder.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.pip.PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PinnedStackListenerForwarder.PinnedTaskListenerImpl pinnedTaskListenerImpl = PinnedStackListenerForwarder.PinnedTaskListenerImpl.this;
                    float f2 = f;
                    Objects.requireNonNull(pinnedTaskListenerImpl);
                    PinnedStackListenerForwarder pinnedStackListenerForwarder = PinnedStackListenerForwarder.this;
                    Objects.requireNonNull(pinnedStackListenerForwarder);
                    Iterator<PinnedStackListenerForwarder.PinnedTaskListener> it = pinnedStackListenerForwarder.mListeners.iterator();
                    while (it.hasNext()) {
                        it.next().onExpandedAspectRatioChanged(f2);
                    }
                }
            });
        }

        public final void onImeVisibilityChanged(final boolean z, final int i) {
            PinnedStackListenerForwarder.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.pip.PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    PinnedStackListenerForwarder.PinnedTaskListenerImpl pinnedTaskListenerImpl = PinnedStackListenerForwarder.PinnedTaskListenerImpl.this;
                    boolean z2 = z;
                    int i2 = i;
                    Objects.requireNonNull(pinnedTaskListenerImpl);
                    PinnedStackListenerForwarder pinnedStackListenerForwarder = PinnedStackListenerForwarder.this;
                    Objects.requireNonNull(pinnedStackListenerForwarder);
                    Iterator<PinnedStackListenerForwarder.PinnedTaskListener> it = pinnedStackListenerForwarder.mListeners.iterator();
                    while (it.hasNext()) {
                        it.next().onImeVisibilityChanged(z2, i2);
                    }
                }
            });
        }

        public final void onMovementBoundsChanged(final boolean z) {
            PinnedStackListenerForwarder.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.pip.PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    PinnedStackListenerForwarder.PinnedTaskListenerImpl pinnedTaskListenerImpl = PinnedStackListenerForwarder.PinnedTaskListenerImpl.this;
                    boolean z2 = z;
                    Objects.requireNonNull(pinnedTaskListenerImpl);
                    PinnedStackListenerForwarder pinnedStackListenerForwarder = PinnedStackListenerForwarder.this;
                    Objects.requireNonNull(pinnedStackListenerForwarder);
                    Iterator<PinnedStackListenerForwarder.PinnedTaskListener> it = pinnedStackListenerForwarder.mListeners.iterator();
                    while (it.hasNext()) {
                        it.next().onMovementBoundsChanged(z2);
                    }
                }
            });
        }
    }

    public PinnedStackListenerForwarder(ShellExecutor shellExecutor) {
        this.mMainExecutor = shellExecutor;
    }
}
