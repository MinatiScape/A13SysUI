package com.android.systemui.shortcut;

import android.content.Context;
import android.os.RemoteException;
import android.view.IWindowManager;
import android.view.WindowManagerGlobal;
import com.android.systemui.CoreStartable;
import com.android.systemui.shortcut.ShortcutKeyServiceProxy;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import java.util.Optional;
/* loaded from: classes.dex */
public class ShortcutKeyDispatcher extends CoreStartable implements ShortcutKeyServiceProxy.Callbacks {
    public final Optional<LegacySplitScreen> mSplitScreenOptional;
    public ShortcutKeyServiceProxy mShortcutKeyServiceProxy = new ShortcutKeyServiceProxy(this);
    public IWindowManager mWindowManagerService = WindowManagerGlobal.getWindowManagerService();

    @Override // com.android.systemui.CoreStartable
    public final void start() {
        try {
            this.mWindowManagerService.registerShortcutKey(281474976710727L, this.mShortcutKeyServiceProxy);
        } catch (RemoteException unused) {
        }
        try {
            this.mWindowManagerService.registerShortcutKey(281474976710728L, this.mShortcutKeyServiceProxy);
        } catch (RemoteException unused2) {
        }
    }

    public ShortcutKeyDispatcher(Context context, Optional<LegacySplitScreen> optional) {
        super(context);
        this.mSplitScreenOptional = optional;
    }
}
