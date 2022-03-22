package com.android.wm.shell.bubbles;

import android.os.Bundle;
import android.view.InsetsSourceControl;
import android.view.InsetsState;
import com.android.systemui.wmshell.BubblesManager;
import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.bubbles.Bubbles;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.ShellExecutor$$ExternalSyntheticLambda0;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubbleController$5$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ Object f$2;

    public /* synthetic */ BubbleController$5$$ExternalSyntheticLambda0(Object obj, Object obj2, Object obj3, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
        this.f$2 = obj3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                BubbleController.AnonymousClass5 r0 = (BubbleController.AnonymousClass5) this.f$0;
                BubbleEntry bubbleEntry = (BubbleEntry) this.f$1;
                Bubble bubble = (Bubble) this.f$2;
                Objects.requireNonNull(r0);
                if (bubbleEntry != null) {
                    if (BubbleController.this.getBubblesInGroup(bubbleEntry.mSbn.getGroupKey()).isEmpty()) {
                        Bubbles.SysuiProxy sysuiProxy = BubbleController.this.mSysuiProxy;
                        Objects.requireNonNull(bubble);
                        String str = bubble.mKey;
                        BubblesManager.AnonymousClass5 r02 = (BubblesManager.AnonymousClass5) sysuiProxy;
                        Objects.requireNonNull(r02);
                        r02.val$sysuiMainExecutor.execute(new ShellExecutor$$ExternalSyntheticLambda0(r02, str, 3));
                        return;
                    }
                    return;
                }
                return;
            case 1:
                DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl displayWindowInsetsControllerImpl = (DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl) this.f$0;
                InsetsState insetsState = (InsetsState) this.f$1;
                InsetsSourceControl[] insetsSourceControlArr = (InsetsSourceControl[]) this.f$2;
                int i = DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl.$r8$clinit;
                Objects.requireNonNull(displayWindowInsetsControllerImpl);
                DisplayInsetsController.PerDisplay perDisplay = DisplayInsetsController.PerDisplay.this;
                Objects.requireNonNull(perDisplay);
                CopyOnWriteArrayList<DisplayInsetsController.OnInsetsChangedListener> copyOnWriteArrayList = DisplayInsetsController.this.mListeners.get(perDisplay.mDisplayId);
                if (copyOnWriteArrayList != null) {
                    Iterator<DisplayInsetsController.OnInsetsChangedListener> it = copyOnWriteArrayList.iterator();
                    while (it.hasNext()) {
                        it.next().insetsControlChanged(insetsState, insetsSourceControlArr);
                    }
                    return;
                }
                return;
            default:
                NgaMessageHandler ngaMessageHandler = (NgaMessageHandler) this.f$0;
                boolean z = NgaMessageHandler.VERBOSE;
                Objects.requireNonNull(ngaMessageHandler);
                ngaMessageHandler.processBundle((Bundle) this.f$1, (Runnable) this.f$2);
                return;
        }
    }
}
