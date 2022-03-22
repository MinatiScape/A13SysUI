package com.android.systemui.flags;

import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline0;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public final class FeatureFlagsRelease implements FeatureFlags, Dumpable {
    public final Resources mResources;
    public SparseBooleanArray mBooleanCache = new SparseBooleanArray();
    public SparseArray<String> mStringCache = new SparseArray<>();

    @Override // com.android.systemui.flags.FlagListenable
    public final void addListener() {
    }

    @Override // com.android.systemui.flags.FeatureFlags
    public final boolean isEnabled(BooleanFlag booleanFlag) {
        int i = booleanFlag.id;
        boolean booleanValue = booleanFlag.getDefault().booleanValue();
        this.mBooleanCache.append(i, booleanValue);
        return booleanValue;
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("can override: false");
        int size = this.mBooleanCache.size();
        printWriter.println("booleans: " + size);
        for (int i = 0; i < size; i++) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("  sysui_flag_");
            m.append(this.mBooleanCache.keyAt(i));
            m.append(": ");
            m.append(this.mBooleanCache.valueAt(i));
            printWriter.println(m.toString());
        }
        int size2 = this.mStringCache.size();
        printWriter.println("Strings: " + size2);
        for (int i2 = 0; i2 < size2; i2++) {
            int keyAt = this.mStringCache.keyAt(i2);
            String valueAt = this.mStringCache.valueAt(i2);
            StringBuilder m2 = GridLayoutManager$$ExternalSyntheticOutline0.m("  sysui_flag_", keyAt, ": [length=", valueAt.length(), "] \"");
            m2.append(valueAt);
            m2.append("\"");
            printWriter.println(m2.toString());
        }
    }

    public FeatureFlagsRelease(Resources resources, DumpManager dumpManager) {
        this.mResources = resources;
        dumpManager.registerDumpable("SysUIFlags", this);
    }

    @Override // com.android.systemui.flags.FeatureFlags
    public final boolean isEnabled(ResourceBooleanFlag resourceBooleanFlag) {
        int indexOfKey = this.mBooleanCache.indexOfKey(resourceBooleanFlag.id);
        if (indexOfKey >= 0) {
            return this.mBooleanCache.valueAt(indexOfKey);
        }
        int i = resourceBooleanFlag.id;
        boolean z = this.mResources.getBoolean(resourceBooleanFlag.resourceId);
        this.mBooleanCache.append(i, z);
        return z;
    }
}
