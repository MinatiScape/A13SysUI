package com.android.systemui.toast;

import android.content.Context;
import android.view.LayoutInflater;
import com.android.keyguard.LockIconView$$ExternalSyntheticOutline0;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.plugins.ToastPlugin;
import com.android.systemui.shared.plugins.PluginManager;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public final class ToastFactory implements Dumpable {
    public final LayoutInflater mLayoutInflater;
    public ToastPlugin mPlugin;

    public final SystemUIToast createToast(Context context, CharSequence charSequence, String str, int i, int i2) {
        boolean z;
        ToastPlugin toastPlugin = this.mPlugin;
        if (toastPlugin != null) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            return new SystemUIToast(this.mLayoutInflater, context, charSequence, toastPlugin.createToast(charSequence, str, i), str, i, i2);
        }
        return new SystemUIToast(this.mLayoutInflater, context, charSequence, null, str, i, i2);
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder m = LockIconView$$ExternalSyntheticOutline0.m(printWriter, "ToastFactory:", "    mAttachedPlugin=");
        m.append(this.mPlugin);
        printWriter.println(m.toString());
    }

    public ToastFactory(LayoutInflater layoutInflater, PluginManager pluginManager, DumpManager dumpManager) {
        this.mLayoutInflater = layoutInflater;
        dumpManager.registerDumpable("ToastFactory", this);
        pluginManager.addPluginListener((PluginListener) new PluginListener<ToastPlugin>() { // from class: com.android.systemui.toast.ToastFactory.1
            @Override // com.android.systemui.plugins.PluginListener
            public final void onPluginConnected(ToastPlugin toastPlugin, Context context) {
                ToastFactory.this.mPlugin = toastPlugin;
            }

            @Override // com.android.systemui.plugins.PluginListener
            public final void onPluginDisconnected(ToastPlugin toastPlugin) {
                if (toastPlugin.equals(ToastFactory.this.mPlugin)) {
                    ToastFactory.this.mPlugin = null;
                }
            }
        }, ToastPlugin.class, false);
    }
}
