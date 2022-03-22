package com.android.systemui.statusbar.phone;

import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
/* loaded from: classes.dex */
public final class SystemUIDialogManager implements Dumpable {
    public final StatusBarKeyguardViewManager mKeyguardViewManager;
    public final HashSet mDialogsShowing = new HashSet();
    public final HashSet mListeners = new HashSet();

    /* loaded from: classes.dex */
    public interface Listener {
        void shouldHideAffordances();
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("listeners:");
        Iterator it = this.mListeners.iterator();
        while (it.hasNext()) {
            printWriter.println("\t" + ((Listener) it.next()));
        }
        printWriter.println("dialogs tracked:");
        Iterator it2 = this.mDialogsShowing.iterator();
        while (it2.hasNext()) {
            printWriter.println("\t" + ((SystemUIDialog) it2.next()));
        }
    }

    public final boolean shouldHideAffordance() {
        return !this.mDialogsShowing.isEmpty();
    }

    public SystemUIDialogManager(DumpManager dumpManager, StatusBarKeyguardViewManager statusBarKeyguardViewManager) {
        dumpManager.registerDumpable(this);
        this.mKeyguardViewManager = statusBarKeyguardViewManager;
    }

    public final void setShowing(SystemUIDialog systemUIDialog, boolean z) {
        boolean shouldHideAffordance = shouldHideAffordance();
        if (z) {
            this.mDialogsShowing.add(systemUIDialog);
        } else {
            this.mDialogsShowing.remove(systemUIDialog);
        }
        if (shouldHideAffordance != shouldHideAffordance()) {
            if (shouldHideAffordance()) {
                this.mKeyguardViewManager.resetAlternateAuth(true);
            }
            Iterator it = this.mListeners.iterator();
            while (it.hasNext()) {
                shouldHideAffordance();
                ((Listener) it.next()).shouldHideAffordances();
            }
        }
    }
}
