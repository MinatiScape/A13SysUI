package com.android.systemui.doze;

import android.graphics.Rect;
import android.os.Bundle;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.doze.DozeTriggers;
import com.android.systemui.navigationbar.TaskbarDelegate;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.phone.HeadsUpAppearanceController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.util.Assert;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.recents.IRecentTasks;
import com.android.wm.shell.recents.RecentTasks;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class DozeTriggers$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ DozeTriggers$$ExternalSyntheticLambda2(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                ((UiEventLogger) this.f$0).log((DozeTriggers.DozingUpdateUiEvent) obj);
                return;
            case 1:
                KeyguardUpdateMonitor keyguardUpdateMonitor = (KeyguardUpdateMonitor) this.f$0;
                int intValue = ((Integer) obj).intValue();
                boolean z = KeyguardUpdateMonitor.DEBUG;
                Objects.requireNonNull(keyguardUpdateMonitor);
                Assert.isMainThread();
                for (int i = 0; i < keyguardUpdateMonitor.mCallbacks.size(); i++) {
                    KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = keyguardUpdateMonitor.mCallbacks.get(i).get();
                    if (keyguardUpdateMonitorCallback != null) {
                        keyguardUpdateMonitorCallback.onStrongAuthStateChanged(intValue);
                    }
                }
                return;
            case 2:
                TaskbarDelegate taskbarDelegate = (TaskbarDelegate) this.f$0;
                Objects.requireNonNull(taskbarDelegate);
                ((Pip) obj).removePipExclusionBoundsChangeListener(taskbarDelegate.mPipListener);
                return;
            case 3:
                IRecentTasks.Stub stub = (IRecentTasks.Stub) ((RecentTasks) obj).createExternalInterface();
                Objects.requireNonNull(stub);
                ((Bundle) this.f$0).putBinder("recent_tasks", stub);
                return;
            case 4:
                HeadsUpAppearanceController headsUpAppearanceController = (HeadsUpAppearanceController) this.f$0;
                Objects.requireNonNull(headsUpAppearanceController);
                headsUpAppearanceController.updateHeader((NotificationEntry) obj);
                return;
            default:
                NotificationPanelViewController notificationPanelViewController = (NotificationPanelViewController) this.f$0;
                ((Integer) obj).intValue();
                Rect rect = NotificationPanelViewController.M_DUMMY_DIRTY_RECT;
                Objects.requireNonNull(notificationPanelViewController);
                notificationPanelViewController.updateQSExpansionEnabledAmbient();
                return;
        }
    }
}
