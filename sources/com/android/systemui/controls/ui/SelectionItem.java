package com.android.systemui.controls.ui;

import android.content.ComponentName;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.Insets$$ExternalSyntheticOutline0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsUiControllerImpl.kt */
/* loaded from: classes.dex */
public final class SelectionItem {
    public final CharSequence appName;
    public final ComponentName componentName;
    public final Drawable icon;
    public final CharSequence structure;
    public final int uid;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SelectionItem)) {
            return false;
        }
        SelectionItem selectionItem = (SelectionItem) obj;
        return Intrinsics.areEqual(this.appName, selectionItem.appName) && Intrinsics.areEqual(this.structure, selectionItem.structure) && Intrinsics.areEqual(this.icon, selectionItem.icon) && Intrinsics.areEqual(this.componentName, selectionItem.componentName) && this.uid == selectionItem.uid;
    }

    public final int hashCode() {
        int hashCode = this.structure.hashCode();
        int hashCode2 = this.icon.hashCode();
        int hashCode3 = this.componentName.hashCode();
        return Integer.hashCode(this.uid) + ((hashCode3 + ((hashCode2 + ((hashCode + (this.appName.hashCode() * 31)) * 31)) * 31)) * 31);
    }

    public final CharSequence getTitle() {
        boolean z;
        if (this.structure.length() == 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            return this.appName;
        }
        return this.structure;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("SelectionItem(appName=");
        m.append((Object) this.appName);
        m.append(", structure=");
        m.append((Object) this.structure);
        m.append(", icon=");
        m.append(this.icon);
        m.append(", componentName=");
        m.append(this.componentName);
        m.append(", uid=");
        return Insets$$ExternalSyntheticOutline0.m(m, this.uid, ')');
    }

    public SelectionItem(CharSequence charSequence, CharSequence charSequence2, Drawable drawable, ComponentName componentName, int i) {
        this.appName = charSequence;
        this.structure = charSequence2;
        this.icon = drawable;
        this.componentName = componentName;
        this.uid = i;
    }
}
