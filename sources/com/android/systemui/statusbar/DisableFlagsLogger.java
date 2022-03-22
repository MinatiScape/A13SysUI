package com.android.systemui.statusbar;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.core.graphics.Insets$$ExternalSyntheticOutline0;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DisableFlagsLogger.kt */
/* loaded from: classes.dex */
public final class DisableFlagsLogger {
    public final List<DisableFlag> disable1FlagsList;
    public final List<DisableFlag> disable2FlagsList;

    /* compiled from: DisableFlagsLogger.kt */
    /* loaded from: classes.dex */
    public static final class DisableFlag {
        public final int bitMask;
        public final char flagIsSetSymbol;
        public final char flagNotSetSymbol;

        public final char getFlagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core(int i) {
            if ((i & this.bitMask) != 0) {
                return this.flagIsSetSymbol;
            }
            return this.flagNotSetSymbol;
        }

        public DisableFlag(int i, char c, char c2) {
            this.bitMask = i;
            this.flagIsSetSymbol = c;
            this.flagNotSetSymbol = c2;
        }
    }

    /* compiled from: DisableFlagsLogger.kt */
    /* loaded from: classes.dex */
    public static final class DisableState {
        public final int disable1;
        public final int disable2;

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof DisableState)) {
                return false;
            }
            DisableState disableState = (DisableState) obj;
            return this.disable1 == disableState.disable1 && this.disable2 == disableState.disable2;
        }

        public final int hashCode() {
            return Integer.hashCode(this.disable2) + (Integer.hashCode(this.disable1) * 31);
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("DisableState(disable1=");
            m.append(this.disable1);
            m.append(", disable2=");
            return Insets$$ExternalSyntheticOutline0.m(m, this.disable2, ')');
        }

        public DisableState(int i, int i2) {
            this.disable1 = i;
            this.disable2 = i2;
        }
    }

    public DisableFlagsLogger() {
        List<DisableFlag> list = DisableFlagsLoggerKt.defaultDisable1FlagsList;
        List<DisableFlag> list2 = DisableFlagsLoggerKt.defaultDisable2FlagsList;
        this.disable1FlagsList = list;
        this.disable2FlagsList = list2;
        if (flagsListHasDuplicateSymbols(list)) {
            throw new IllegalArgumentException("disable1 flags must have unique symbols");
        } else if (flagsListHasDuplicateSymbols(list2)) {
            throw new IllegalArgumentException("disable2 flags must have unique symbols");
        }
    }

    public static boolean flagsListHasDuplicateSymbols(List list) {
        ArrayList arrayList = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(list, 10));
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(Character.valueOf(((DisableFlag) it.next()).getFlagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core(0)));
        }
        int size = CollectionsKt___CollectionsKt.toList(CollectionsKt___CollectionsKt.toMutableSet(arrayList)).size();
        ArrayList arrayList2 = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(list, 10));
        Iterator it2 = list.iterator();
        while (it2.hasNext()) {
            arrayList2.add(Character.valueOf(((DisableFlag) it2.next()).getFlagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core(Integer.MAX_VALUE)));
        }
        int size2 = CollectionsKt___CollectionsKt.toList(CollectionsKt___CollectionsKt.toMutableSet(arrayList2)).size();
        if (size < list.size() || size2 < list.size()) {
            return true;
        }
        return false;
    }

    public final String getDisableFlagsString(DisableState disableState, DisableState disableState2, DisableState disableState3) {
        StringBuilder sb = new StringBuilder("Received new disable state: ");
        if (disableState != null && !Intrinsics.areEqual(disableState, disableState2)) {
            sb.append("Old: ");
            sb.append(getFlagsString(disableState));
            sb.append(" | ");
            sb.append("New: ");
            sb.append(getFlagsString(disableState2));
            sb.append(" ");
            sb.append(getDiffString(disableState, disableState2));
        } else if (disableState == null || !Intrinsics.areEqual(disableState, disableState2)) {
            sb.append(getFlagsString(disableState2));
        } else {
            sb.append(getFlagsString(disableState2));
            sb.append(" ");
            sb.append(getDiffString(disableState, disableState2));
        }
        if (!Intrinsics.areEqual(disableState2, disableState3)) {
            sb.append(" | New after local modification: ");
            sb.append(getFlagsString(disableState3));
            sb.append(" ");
            sb.append(getDiffString(disableState2, disableState3));
        }
        return sb.toString();
    }

    public final String getFlagsString(DisableState disableState) {
        StringBuilder sb = new StringBuilder("");
        for (DisableFlag disableFlag : this.disable1FlagsList) {
            Objects.requireNonNull(disableState);
            sb.append(disableFlag.getFlagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core(disableState.disable1));
        }
        sb.append(".");
        for (DisableFlag disableFlag2 : this.disable2FlagsList) {
            Objects.requireNonNull(disableState);
            sb.append(disableFlag2.getFlagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core(disableState.disable2));
        }
        return sb.toString();
    }

    public final String getDiffString(DisableState disableState, DisableState disableState2) {
        if (Intrinsics.areEqual(disableState, disableState2)) {
            return "(unchanged)";
        }
        StringBuilder sb = new StringBuilder("(");
        sb.append("changed: ");
        for (DisableFlag disableFlag : this.disable1FlagsList) {
            char flagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core = disableFlag.getFlagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core(disableState2.disable1);
            Objects.requireNonNull(disableState);
            if (disableFlag.getFlagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core(disableState.disable1) != flagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core) {
                sb.append(flagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core);
            }
        }
        sb.append(".");
        for (DisableFlag disableFlag2 : this.disable2FlagsList) {
            char flagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core2 = disableFlag2.getFlagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core(disableState2.disable2);
            Objects.requireNonNull(disableState);
            if (disableFlag2.getFlagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core(disableState.disable2) != flagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core2) {
                sb.append(flagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core2);
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
