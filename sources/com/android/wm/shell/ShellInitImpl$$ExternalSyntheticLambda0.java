package com.android.wm.shell;

import com.android.systemui.statusbar.notification.InstantAppNotifier;
import com.android.systemui.wmshell.WMShell;
import com.android.wm.shell.freeform.FreeformTaskListener;
import com.android.wm.shell.onehanded.OneHanded;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ShellInitImpl$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ ShellInitImpl$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                ShellInitImpl shellInitImpl = (ShellInitImpl) this.f$0;
                Objects.requireNonNull(shellInitImpl);
                shellInitImpl.mShellTaskOrganizer.addListenerForType((FreeformTaskListener) obj, -5);
                return;
            case 1:
                InstantAppNotifier instantAppNotifier = (InstantAppNotifier) this.f$0;
                Objects.requireNonNull(instantAppNotifier);
                instantAppNotifier.mDockedStackExists = ((Boolean) obj).booleanValue();
                instantAppNotifier.updateForegroundInstantApps();
                return;
            default:
                ((WMShell) this.f$0).initOneHanded((OneHanded) obj);
                return;
        }
    }
}
