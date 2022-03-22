package com.android.wm.shell.pip.phone;

import android.util.IndentingPrintWriter;
import androidx.fragment.R$id;
import com.android.systemui.doze.DozeScreenBrightness$$ExternalSyntheticOutline0;
import com.android.systemui.statusbar.notification.row.FooterView;
import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.bubbles.Bubbles;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.phone.PipController;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PipController$PipImpl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ PipController$PipImpl$$ExternalSyntheticLambda0(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                PipController.PipImpl pipImpl = (PipController.PipImpl) this.f$0;
                Objects.requireNonNull(pipImpl);
                PipBoundsState pipBoundsState = PipController.this.mPipBoundsState;
                Objects.requireNonNull(pipBoundsState);
                pipBoundsState.mOnPipExclusionBoundsChangeCallbacks.add((Consumer) this.f$1);
                Iterator it = pipBoundsState.mOnPipExclusionBoundsChangeCallbacks.iterator();
                while (it.hasNext()) {
                    ((Consumer) it.next()).accept(pipBoundsState.getBounds());
                }
                return;
            case 1:
                FooterView footerView = (FooterView) this.f$0;
                IndentingPrintWriter indentingPrintWriter = (IndentingPrintWriter) this.f$1;
                int i = FooterView.$r8$clinit;
                Objects.requireNonNull(footerView);
                indentingPrintWriter.println("visibility: " + R$id.visibilityString(footerView.getVisibility()));
                StringBuilder sb = new StringBuilder();
                sb.append("manageButton showHistory: ");
                StringBuilder m = DozeScreenBrightness$$ExternalSyntheticOutline0.m(sb, footerView.mShowHistory, indentingPrintWriter, "manageButton visibility: ");
                m.append(R$id.visibilityString(footerView.mClearAllButton.getVisibility()));
                indentingPrintWriter.println(m.toString());
                indentingPrintWriter.println("dismissButton visibility: " + R$id.visibilityString(footerView.mClearAllButton.getVisibility()));
                return;
            default:
                BubbleController.BubblesImpl bubblesImpl = (BubbleController.BubblesImpl) this.f$0;
                Objects.requireNonNull(bubblesImpl);
                BubbleController bubbleController = BubbleController.this;
                Objects.requireNonNull(bubbleController);
                bubbleController.mSysuiProxy = (Bubbles.SysuiProxy) this.f$1;
                return;
        }
    }
}
