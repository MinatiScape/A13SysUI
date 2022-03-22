package com.android.systemui.wmshell;

import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline0;
import com.android.systemui.accessibility.WindowMagnificationController;
import com.android.systemui.statusbar.notification.InstantAppNotifier;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.statusbar.policy.ZenModeControllerImpl;
import com.android.wifitrackerlib.StandardWifiEntry;
import com.android.wm.shell.ShellInitImpl$$ExternalSyntheticLambda0;
import com.android.wm.shell.bubbles.Bubble;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.compatui.CompatUIWindowManagerAbstract;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.splitscreen.SplitScreen;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WMShell$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ WMShell$$ExternalSyntheticLambda2(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        Object obj2;
        switch (this.$r8$classId) {
            case 0:
                ((WMShell) this.f$0).initSplitScreen((SplitScreen) obj);
                return;
            case 1:
                PrintWriter printWriter = (PrintWriter) this.f$0;
                WindowMagnificationController windowMagnificationController = (WindowMagnificationController) obj;
                Objects.requireNonNull(windowMagnificationController);
                printWriter.println("WindowMagnificationController (displayId=" + windowMagnificationController.mDisplayId + "):");
                StringBuilder sb = new StringBuilder();
                sb.append("      mOverlapWithGestureInsets:");
                StringBuilder m = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(sb, windowMagnificationController.mOverlapWithGestureInsets, printWriter, "      mScale:");
                m.append(windowMagnificationController.mScale);
                printWriter.println(m.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("      mMirrorViewBounds:");
                Object obj3 = "empty";
                if (windowMagnificationController.isWindowVisible()) {
                    obj2 = windowMagnificationController.mMirrorViewBounds;
                } else {
                    obj2 = obj3;
                }
                sb2.append(obj2);
                printWriter.println(sb2.toString());
                StringBuilder sb3 = new StringBuilder();
                sb3.append("      mSourceBounds:");
                if (windowMagnificationController.isWindowVisible()) {
                    obj3 = windowMagnificationController.mSourceBounds;
                }
                sb3.append(obj3);
                printWriter.println(sb3.toString());
                printWriter.println("      mSystemGestureTop:" + windowMagnificationController.mSystemGestureTop);
                return;
            case 2:
                InstantAppNotifier instantAppNotifier = (InstantAppNotifier) this.f$0;
                Objects.requireNonNull(instantAppNotifier);
                ((LegacySplitScreen) obj).registerInSplitScreenListener(new ShellInitImpl$$ExternalSyntheticLambda0(instantAppNotifier, 1));
                return;
            case 3:
                int i = ZenModeControllerImpl.$r8$clinit;
                Objects.requireNonNull((ZenModeController.Callback) obj);
                return;
            case 4:
                StandardWifiEntry standardWifiEntry = (StandardWifiEntry) obj;
                Objects.requireNonNull(standardWifiEntry);
                StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey = standardWifiEntry.mKey;
                Objects.requireNonNull(standardWifiEntryKey);
                standardWifiEntry.updateScanResultInfo((List) ((Map) this.f$0).get(standardWifiEntryKey.mScanResultKey));
                return;
            case 5:
                ((List) this.f$0).add((Bubble) obj);
                return;
            default:
                ((CompatUIWindowManagerAbstract) obj).updateDisplayLayout((DisplayLayout) this.f$0);
                return;
        }
    }
}
