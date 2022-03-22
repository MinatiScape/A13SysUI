package com.android.systemui.shared.system.smartspace;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.internal.PropertyReference1Impl;
/* compiled from: SmartspaceState.kt */
/* loaded from: classes.dex */
public final class SmartspaceState implements Parcelable {
    public static final CREATOR CREATOR = new CREATOR();
    public Rect boundsOnScreen = new Rect();
    public int selectedPage;
    public boolean visibleOnScreen;

    /* compiled from: SmartspaceState.kt */
    /* loaded from: classes.dex */
    public static final class CREATOR implements Parcelable.Creator<SmartspaceState> {
        @Override // android.os.Parcelable.Creator
        public final SmartspaceState createFromParcel(Parcel parcel) {
            SmartspaceState smartspaceState = new SmartspaceState();
            smartspaceState.boundsOnScreen = (Rect) parcel.readParcelable(AnonymousClass1.INSTANCE.getClass().getClassLoader());
            smartspaceState.selectedPage = parcel.readInt();
            smartspaceState.visibleOnScreen = parcel.readBoolean();
            return smartspaceState;
        }

        @Override // android.os.Parcelable.Creator
        public final SmartspaceState[] newArray(int i) {
            return new SmartspaceState[i];
        }
    }

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    /* compiled from: SmartspaceState.kt */
    /* renamed from: com.android.systemui.shared.system.smartspace.SmartspaceState$1  reason: invalid class name */
    /* loaded from: classes.dex */
    final /* synthetic */ class AnonymousClass1 extends PropertyReference1Impl {
        public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

        public AnonymousClass1() {
            super(JvmClassMappingKt.class, "javaClass", "getJavaClass(Ljava/lang/Object;)Ljava/lang/Class;", 1);
        }

        @Override // kotlin.jvm.internal.PropertyReference1Impl, kotlin.reflect.KProperty1
        public final Object get(Object obj) {
            return obj.getClass();
        }
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("boundsOnScreen: ");
        m.append(this.boundsOnScreen);
        m.append(", selectedPage: ");
        m.append(this.selectedPage);
        m.append(", visibleOnScreen: ");
        m.append(this.visibleOnScreen);
        return m.toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        if (parcel != null) {
            parcel.writeParcelable(this.boundsOnScreen, 0);
        }
        if (parcel != null) {
            parcel.writeInt(this.selectedPage);
        }
        if (parcel != null) {
            parcel.writeBoolean(this.visibleOnScreen);
        }
    }
}
