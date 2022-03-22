package com.android.wm.shell.common;

import android.graphics.Rect;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.Pair;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$ObjectRef;
/* compiled from: FloatingContentCoordinator.kt */
/* loaded from: classes.dex */
public final class FloatingContentCoordinator {
    public final HashMap allContentBounds = new HashMap();
    public boolean currentlyResolvingConflicts;

    public final void maybeMoveConflictingContent(FloatingContent floatingContent) {
        this.currentlyResolvingConflicts = true;
        Object obj = this.allContentBounds.get(floatingContent);
        Intrinsics.checkNotNull(obj);
        Rect rect = (Rect) obj;
        HashMap hashMap = this.allContentBounds;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        Iterator it = hashMap.entrySet().iterator();
        while (true) {
            boolean z = false;
            if (!it.hasNext()) {
                break;
            }
            Map.Entry entry = (Map.Entry) it.next();
            Rect rect2 = (Rect) entry.getValue();
            if (!Intrinsics.areEqual((FloatingContent) entry.getKey(), floatingContent) && Rect.intersects(rect, rect2)) {
                z = true;
            }
            if (z) {
                linkedHashMap.put(entry.getKey(), entry.getValue());
            }
        }
        for (Map.Entry entry2 : linkedHashMap.entrySet()) {
            FloatingContent floatingContent2 = (FloatingContent) entry2.getKey();
            Rect calculateNewBoundsOnOverlap = floatingContent2.calculateNewBoundsOnOverlap(rect, CollectionsKt___CollectionsKt.minus(CollectionsKt___CollectionsKt.minus(this.allContentBounds.values(), (Rect) entry2.getValue()), rect));
            if (!calculateNewBoundsOnOverlap.isEmpty()) {
                floatingContent2.moveToBounds(calculateNewBoundsOnOverlap);
                this.allContentBounds.put(floatingContent2, floatingContent2.getFloatingBoundsOnScreen());
            }
        }
        this.currentlyResolvingConflicts = false;
    }

    /* compiled from: FloatingContentCoordinator.kt */
    /* loaded from: classes.dex */
    public interface FloatingContent {
        Rect getAllowedFloatingBoundsRegion();

        Rect getFloatingBoundsOnScreen();

        void moveToBounds(Rect rect);

        /* JADX WARN: Type inference failed for: r2v4, types: [T, java.lang.Object] */
        /* JADX WARN: Type inference failed for: r5v2, types: [T, java.lang.Object] */
        default Rect calculateNewBoundsOnOverlap(Rect rect, ArrayList arrayList) {
            boolean z;
            Rect rect2;
            boolean z2;
            boolean z3;
            int i;
            Rect floatingBoundsOnScreen = getFloatingBoundsOnScreen();
            Rect allowedFloatingBoundsRegion = getAllowedFloatingBoundsRegion();
            boolean z4 = true;
            if (rect.centerY() < floatingBoundsOnScreen.centerY()) {
                z = true;
            } else {
                z = false;
            }
            ArrayList arrayList2 = new ArrayList();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                Object next = it.next();
                Rect rect3 = (Rect) next;
                int i2 = rect3.left;
                int i3 = floatingBoundsOnScreen.left;
                if ((i2 < i3 || i2 > floatingBoundsOnScreen.right) && ((i = rect3.right) > floatingBoundsOnScreen.right || i < i3)) {
                    z3 = false;
                } else {
                    z3 = true;
                }
                if (z3) {
                    arrayList2.add(next);
                }
            }
            ArrayList arrayList3 = new ArrayList();
            ArrayList arrayList4 = new ArrayList();
            Iterator it2 = arrayList2.iterator();
            while (it2.hasNext()) {
                Object next2 = it2.next();
                if (((Rect) next2).top < floatingBoundsOnScreen.top) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (z2) {
                    arrayList3.add(next2);
                } else {
                    arrayList4.add(next2);
                }
            }
            Pair pair = new Pair(arrayList3, arrayList4);
            Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
            ref$ObjectRef.element = pair.component1();
            Ref$ObjectRef ref$ObjectRef2 = new Ref$ObjectRef();
            ref$ObjectRef2.element = pair.component2();
            Lazy lazy = LazyKt__LazyJVMKt.lazy(new FloatingContentCoordinator$Companion$findAreaForContentVertically$newContentBoundsAbove$2(floatingBoundsOnScreen, ref$ObjectRef, rect));
            Lazy lazy2 = LazyKt__LazyJVMKt.lazy(new FloatingContentCoordinator$Companion$findAreaForContentVertically$newContentBoundsBelow$2(floatingBoundsOnScreen, ref$ObjectRef2, rect));
            Lazy lazy3 = LazyKt__LazyJVMKt.lazy(new FloatingContentCoordinator$Companion$findAreaForContentVertically$positionAboveInBounds$2(allowedFloatingBoundsRegion, lazy));
            Lazy lazy4 = LazyKt__LazyJVMKt.lazy(new FloatingContentCoordinator$Companion$findAreaForContentVertically$positionBelowInBounds$2(allowedFloatingBoundsRegion, lazy2));
            if ((!z || !((Boolean) lazy4.getValue()).booleanValue()) && (z || ((Boolean) lazy3.getValue()).booleanValue())) {
                z4 = false;
            }
            if (z4) {
                rect2 = (Rect) lazy2.getValue();
            } else {
                rect2 = (Rect) lazy.getValue();
            }
            if (allowedFloatingBoundsRegion.contains(rect2)) {
                return rect2;
            }
            return new Rect();
        }
    }

    public final void onContentMoved(FloatingContent floatingContent) {
        if (!this.currentlyResolvingConflicts) {
            if (!this.allContentBounds.containsKey(floatingContent)) {
                Log.wtf("FloatingCoordinator", "Received onContentMoved call before onContentAdded! This should never happen.");
                return;
            }
            updateContentBounds();
            maybeMoveConflictingContent(floatingContent);
        }
    }

    public final void updateContentBounds() {
        for (FloatingContent floatingContent : this.allContentBounds.keySet()) {
            this.allContentBounds.put(floatingContent, floatingContent.getFloatingBoundsOnScreen());
        }
    }
}
