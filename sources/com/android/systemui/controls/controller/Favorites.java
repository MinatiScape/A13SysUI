package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.service.controls.Control;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import kotlin.Pair;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.collections.CollectionsKt__ReversedViewsKt;
import kotlin.collections.EmptyList;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.collections.MapsKt___MapsKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsControllerImpl.kt */
/* loaded from: classes.dex */
public final class Favorites {
    public static Map<ComponentName, ? extends List<StructureInfo>> favMap = MapsKt___MapsKt.emptyMap();

    public static ArrayList getAllStructures() {
        Map<ComponentName, ? extends List<StructureInfo>> map = favMap;
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<ComponentName, ? extends List<StructureInfo>> entry : map.entrySet()) {
            CollectionsKt__ReversedViewsKt.addAll(arrayList, (List) entry.getValue());
        }
        return arrayList;
    }

    public static List getStructuresForComponent(ComponentName componentName) {
        List list = (List) favMap.get(componentName);
        if (list == null) {
            return EmptyList.INSTANCE;
        }
        return list;
    }

    public static void replaceControls(StructureInfo structureInfo) {
        LinkedHashMap linkedHashMap = new LinkedHashMap(favMap);
        ArrayList arrayList = new ArrayList();
        ComponentName componentName = structureInfo.componentName;
        boolean z = false;
        for (StructureInfo structureInfo2 : getStructuresForComponent(componentName)) {
            Objects.requireNonNull(structureInfo2);
            if (Intrinsics.areEqual(structureInfo2.structure, structureInfo.structure)) {
                z = true;
                structureInfo2 = structureInfo;
            }
            if (!structureInfo2.controls.isEmpty()) {
                arrayList.add(structureInfo2);
            }
        }
        if (!z && !structureInfo.controls.isEmpty()) {
            arrayList.add(structureInfo);
        }
        linkedHashMap.put(componentName, arrayList);
        favMap = linkedHashMap;
    }

    public static boolean updateControls(ComponentName componentName, List list) {
        Pair pair;
        ControlInfo controlInfo;
        int mapCapacity = MapsKt__MapsJVMKt.mapCapacity(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(list, 10));
        if (mapCapacity < 16) {
            mapCapacity = 16;
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap(mapCapacity);
        for (Object obj : list) {
            linkedHashMap.put(((Control) obj).getControlId(), obj);
        }
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        boolean z = false;
        for (StructureInfo structureInfo : getStructuresForComponent(componentName)) {
            Objects.requireNonNull(structureInfo);
            for (ControlInfo controlInfo2 : structureInfo.controls) {
                Objects.requireNonNull(controlInfo2);
                Control control = (Control) linkedHashMap.get(controlInfo2.controlId);
                if (control == null) {
                    pair = null;
                } else {
                    if (!Intrinsics.areEqual(control.getTitle(), controlInfo2.controlTitle) || !Intrinsics.areEqual(control.getSubtitle(), controlInfo2.controlSubtitle) || control.getDeviceType() != controlInfo2.deviceType) {
                        controlInfo = new ControlInfo(controlInfo2.controlId, control.getTitle(), control.getSubtitle(), control.getDeviceType());
                        z = true;
                    } else {
                        controlInfo = controlInfo2;
                    }
                    CharSequence structure = control.getStructure();
                    if (structure == null) {
                        structure = "";
                    }
                    if (!Intrinsics.areEqual(structureInfo.structure, structure)) {
                        z = true;
                    }
                    pair = new Pair(structure, controlInfo);
                }
                if (pair == null) {
                    pair = new Pair(structureInfo.structure, controlInfo2);
                }
                CharSequence charSequence = (CharSequence) pair.component1();
                ControlInfo controlInfo3 = (ControlInfo) pair.component2();
                Object obj2 = linkedHashMap2.get(charSequence);
                if (obj2 == null) {
                    obj2 = new ArrayList();
                    linkedHashMap2.put(charSequence, obj2);
                }
                ((List) obj2).add(controlInfo3);
            }
        }
        if (!z) {
            return false;
        }
        ArrayList arrayList = new ArrayList(linkedHashMap2.size());
        for (Map.Entry entry : linkedHashMap2.entrySet()) {
            arrayList.add(new StructureInfo(componentName, (CharSequence) entry.getKey(), (List) entry.getValue()));
        }
        LinkedHashMap linkedHashMap3 = new LinkedHashMap(favMap);
        linkedHashMap3.put(componentName, arrayList);
        favMap = linkedHashMap3;
        return true;
    }

    public static ArrayList getControlsForComponent(ComponentName componentName) {
        List<StructureInfo> structuresForComponent = getStructuresForComponent(componentName);
        ArrayList arrayList = new ArrayList();
        for (StructureInfo structureInfo : structuresForComponent) {
            Objects.requireNonNull(structureInfo);
            CollectionsKt__ReversedViewsKt.addAll(arrayList, structureInfo.controls);
        }
        return arrayList;
    }
}
