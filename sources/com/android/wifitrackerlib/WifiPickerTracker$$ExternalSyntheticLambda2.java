package com.android.wifitrackerlib;

import com.android.wifitrackerlib.StandardWifiEntry;
import com.android.wm.shell.bubbles.Bubble;
import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.bubbles.BubbleController$5$$ExternalSyntheticLambda0;
import com.android.wm.shell.bubbles.BubbleEntry;
import com.android.wm.shell.pip.IPipAnimationListener;
import com.android.wm.shell.pip.phone.PipController;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda2(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                StandardWifiEntry standardWifiEntry = (StandardWifiEntry) obj;
                Objects.requireNonNull(standardWifiEntry);
                StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey = standardWifiEntry.mKey;
                Objects.requireNonNull(standardWifiEntryKey);
                StandardWifiEntry.ScanResultKey scanResultKey = standardWifiEntryKey.mScanResultKey;
                ((Set) this.f$0).remove(scanResultKey);
                standardWifiEntry.updateScanResultInfo((List) ((Map) this.f$1).get(scanResultKey));
                return;
            case 1:
                BubbleController.AnonymousClass5 r0 = (BubbleController.AnonymousClass5) this.f$0;
                Objects.requireNonNull(r0);
                BubbleController.this.mMainExecutor.execute(new BubbleController$5$$ExternalSyntheticLambda0(r0, (BubbleEntry) obj, (Bubble) this.f$1, 0));
                return;
            default:
                PipController.IPipImpl iPipImpl = (PipController.IPipImpl) this.f$0;
                IPipAnimationListener iPipAnimationListener = (IPipAnimationListener) this.f$1;
                PipController pipController = (PipController) obj;
                int i = PipController.IPipImpl.$r8$clinit;
                Objects.requireNonNull(iPipImpl);
                if (iPipAnimationListener != null) {
                    iPipImpl.mListener.register(iPipAnimationListener);
                    return;
                } else {
                    iPipImpl.mListener.unregister();
                    return;
                }
        }
    }
}
