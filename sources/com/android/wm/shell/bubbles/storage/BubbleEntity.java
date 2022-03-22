package com.android.wm.shell.bubbles.storage;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.keyguard.FontInterpolator$VarFontKey$$ExternalSyntheticOutline0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: BubbleEntity.kt */
/* loaded from: classes.dex */
public final class BubbleEntity {
    public final int desiredHeight;
    public final int desiredHeightResId;
    public final String key;
    public final String locus;
    public final String packageName;
    public final String shortcutId;
    public final int taskId;
    public final String title;
    public final int userId;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BubbleEntity)) {
            return false;
        }
        BubbleEntity bubbleEntity = (BubbleEntity) obj;
        return this.userId == bubbleEntity.userId && Intrinsics.areEqual(this.packageName, bubbleEntity.packageName) && Intrinsics.areEqual(this.shortcutId, bubbleEntity.shortcutId) && Intrinsics.areEqual(this.key, bubbleEntity.key) && this.desiredHeight == bubbleEntity.desiredHeight && this.desiredHeightResId == bubbleEntity.desiredHeightResId && Intrinsics.areEqual(this.title, bubbleEntity.title) && this.taskId == bubbleEntity.taskId && Intrinsics.areEqual(this.locus, bubbleEntity.locus);
    }

    public final int hashCode() {
        int i;
        int hashCode = this.packageName.hashCode();
        int hashCode2 = this.shortcutId.hashCode();
        int m = FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.desiredHeightResId, FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.desiredHeight, (this.key.hashCode() + ((hashCode2 + ((hashCode + (Integer.hashCode(this.userId) * 31)) * 31)) * 31)) * 31, 31), 31);
        String str = this.title;
        int i2 = 0;
        if (str == null) {
            i = 0;
        } else {
            i = str.hashCode();
        }
        int m2 = FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.taskId, (m + i) * 31, 31);
        String str2 = this.locus;
        if (str2 != null) {
            i2 = str2.hashCode();
        }
        return m2 + i2;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("BubbleEntity(userId=");
        m.append(this.userId);
        m.append(", packageName=");
        m.append(this.packageName);
        m.append(", shortcutId=");
        m.append(this.shortcutId);
        m.append(", key=");
        m.append(this.key);
        m.append(", desiredHeight=");
        m.append(this.desiredHeight);
        m.append(", desiredHeightResId=");
        m.append(this.desiredHeightResId);
        m.append(", title=");
        m.append((Object) this.title);
        m.append(", taskId=");
        m.append(this.taskId);
        m.append(", locus=");
        m.append((Object) this.locus);
        m.append(')');
        return m.toString();
    }

    public BubbleEntity(int i, String str, String str2, String str3, int i2, int i3, String str4, int i4, String str5) {
        this.userId = i;
        this.packageName = str;
        this.shortcutId = str2;
        this.key = str3;
        this.desiredHeight = i2;
        this.desiredHeightResId = i3;
        this.title = str4;
        this.taskId = i4;
        this.locus = str5;
    }
}
