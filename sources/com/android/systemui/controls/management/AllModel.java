package com.android.systemui.controls.management;

import android.service.controls.Control;
import android.text.TextUtils;
import android.util.ArrayMap;
import com.android.systemui.controls.ControlStatus;
import com.android.systemui.controls.controller.ControlInfo;
import com.android.systemui.controls.management.ControlsModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import kotlin.collections.CollectionsKt__ReversedViewsKt;
import kotlin.collections.CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1;
import kotlin.collections.MapsKt___MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import kotlin.sequences.TransformingSequence;
/* compiled from: AllModel.kt */
/* loaded from: classes.dex */
public final class AllModel implements ControlsModel {
    public final List<ControlStatus> controls;
    public final ControlsModel.ControlsModelCallback controlsModelCallback;
    public final ArrayList elements;
    public final CharSequence emptyZoneString;
    public final ArrayList favoriteIds;
    public boolean modified;

    /* compiled from: AllModel.kt */
    /* loaded from: classes.dex */
    public static final class OrderedMap<K, V> implements Map<K, V>, KMappedMarker {
        public final Map<K, V> map;
        public final ArrayList orderedKeys = new ArrayList();

        @Override // java.util.Map
        public final boolean containsKey(Object obj) {
            return this.map.containsKey(obj);
        }

        @Override // java.util.Map
        public final boolean containsValue(Object obj) {
            return this.map.containsValue(obj);
        }

        @Override // java.util.Map
        public final V get(Object obj) {
            return this.map.get(obj);
        }

        @Override // java.util.Map
        public final boolean isEmpty() {
            return this.map.isEmpty();
        }

        @Override // java.util.Map
        public final void putAll(Map<? extends K, ? extends V> map) {
            this.map.putAll(map);
        }

        @Override // java.util.Map
        public final void clear() {
            this.orderedKeys.clear();
            this.map.clear();
        }

        @Override // java.util.Map
        public final Set<Map.Entry<K, V>> entrySet() {
            return this.map.entrySet();
        }

        @Override // java.util.Map
        public final Set<K> keySet() {
            return this.map.keySet();
        }

        @Override // java.util.Map
        public final V put(K k, V v) {
            if (!this.map.containsKey(k)) {
                this.orderedKeys.add(k);
            }
            return this.map.put(k, v);
        }

        @Override // java.util.Map
        public final V remove(Object obj) {
            V remove = this.map.remove(obj);
            if (remove != null) {
                this.orderedKeys.remove(obj);
            }
            return remove;
        }

        @Override // java.util.Map
        public final int size() {
            return this.map.size();
        }

        @Override // java.util.Map
        public final Collection<V> values() {
            return this.map.values();
        }

        public OrderedMap(ArrayMap arrayMap) {
            this.map = arrayMap;
        }
    }

    @Override // com.android.systemui.controls.management.ControlsModel
    public final /* bridge */ /* synthetic */ ControlsModel.MoveHelper getMoveHelper() {
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x0036 A[EDGE_INSN: B:32:0x0036->B:13:0x0036 ?: BREAK  , SYNTHETIC] */
    @Override // com.android.systemui.controls.management.ControlsModel
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void changeFavoriteStatus(java.lang.String r7, boolean r8) {
        /*
            r6 = this;
            java.util.ArrayList r0 = r6.elements
            java.util.Iterator r0 = r0.iterator()
        L_0x0006:
            boolean r1 = r0.hasNext()
            r2 = 1
            r3 = 0
            if (r1 == 0) goto L_0x0035
            java.lang.Object r1 = r0.next()
            r4 = r1
            com.android.systemui.controls.management.ElementWrapper r4 = (com.android.systemui.controls.management.ElementWrapper) r4
            boolean r5 = r4 instanceof com.android.systemui.controls.management.ControlStatusWrapper
            if (r5 == 0) goto L_0x0031
            com.android.systemui.controls.management.ControlStatusWrapper r4 = (com.android.systemui.controls.management.ControlStatusWrapper) r4
            java.util.Objects.requireNonNull(r4)
            com.android.systemui.controls.ControlStatus r4 = r4.controlStatus
            java.util.Objects.requireNonNull(r4)
            android.service.controls.Control r4 = r4.control
            java.lang.String r4 = r4.getControlId()
            boolean r4 = kotlin.jvm.internal.Intrinsics.areEqual(r4, r7)
            if (r4 == 0) goto L_0x0031
            r4 = r2
            goto L_0x0032
        L_0x0031:
            r4 = r3
        L_0x0032:
            if (r4 == 0) goto L_0x0006
            goto L_0x0036
        L_0x0035:
            r1 = 0
        L_0x0036:
            com.android.systemui.controls.management.ControlStatusWrapper r1 = (com.android.systemui.controls.management.ControlStatusWrapper) r1
            if (r1 != 0) goto L_0x003b
            goto L_0x0045
        L_0x003b:
            com.android.systemui.controls.ControlStatus r0 = r1.controlStatus
            if (r0 != 0) goto L_0x0040
            goto L_0x0045
        L_0x0040:
            boolean r0 = r0.favorite
            if (r8 != r0) goto L_0x0045
            r3 = r2
        L_0x0045:
            if (r3 == 0) goto L_0x0048
            return
        L_0x0048:
            if (r8 == 0) goto L_0x0051
            java.util.ArrayList r0 = r6.favoriteIds
            boolean r7 = r0.add(r7)
            goto L_0x0057
        L_0x0051:
            java.util.ArrayList r0 = r6.favoriteIds
            boolean r7 = r0.remove(r7)
        L_0x0057:
            if (r7 == 0) goto L_0x0064
            boolean r7 = r6.modified
            if (r7 != 0) goto L_0x0064
            r6.modified = r2
            com.android.systemui.controls.management.ControlsModel$ControlsModelCallback r6 = r6.controlsModelCallback
            r6.onFirstChange()
        L_0x0064:
            if (r1 != 0) goto L_0x0067
            goto L_0x006e
        L_0x0067:
            com.android.systemui.controls.ControlStatus r6 = r1.controlStatus
            java.util.Objects.requireNonNull(r6)
            r6.favorite = r8
        L_0x006e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.management.AllModel.changeFavoriteStatus(java.lang.String, boolean):void");
    }

    @Override // com.android.systemui.controls.management.ControlsModel
    public final ArrayList getFavorites() {
        ControlInfo controlInfo;
        Object obj;
        Control control;
        ArrayList arrayList = this.favoriteIds;
        ArrayList arrayList2 = new ArrayList();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            Iterator<T> it2 = this.controls.iterator();
            while (true) {
                controlInfo = null;
                if (!it2.hasNext()) {
                    obj = null;
                    break;
                }
                obj = it2.next();
                ControlStatus controlStatus = (ControlStatus) obj;
                Objects.requireNonNull(controlStatus);
                if (Intrinsics.areEqual(controlStatus.control.getControlId(), str)) {
                    break;
                }
            }
            ControlStatus controlStatus2 = (ControlStatus) obj;
            if (controlStatus2 == null) {
                control = null;
            } else {
                control = controlStatus2.control;
            }
            if (control != null) {
                controlInfo = new ControlInfo(control.getControlId(), control.getTitle(), control.getSubtitle(), control.getDeviceType());
            }
            if (controlInfo != null) {
                arrayList2.add(controlInfo);
            }
        }
        return arrayList2;
    }

    public AllModel(List list, List list2, CharSequence charSequence, ControlsFavoritingActivity$controlsModelCallback$1 controlsFavoritingActivity$controlsModelCallback$1) {
        this.controls = list;
        this.emptyZoneString = charSequence;
        this.controlsModelCallback = controlsFavoritingActivity$controlsModelCallback$1;
        HashSet hashSet = new HashSet();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            ControlStatus controlStatus = (ControlStatus) it.next();
            Objects.requireNonNull(controlStatus);
            hashSet.add(controlStatus.control.getControlId());
        }
        ArrayList arrayList = new ArrayList();
        for (Object obj : list2) {
            if (hashSet.contains((String) obj)) {
                arrayList.add(obj);
            }
        }
        this.favoriteIds = new ArrayList(arrayList);
        List<ControlStatus> list3 = this.controls;
        OrderedMap orderedMap = new OrderedMap(new ArrayMap());
        for (Object obj2 : list3) {
            ControlStatus controlStatus2 = (ControlStatus) obj2;
            Objects.requireNonNull(controlStatus2);
            String zone = controlStatus2.control.getZone();
            zone = zone == null ? "" : zone;
            Object obj3 = orderedMap.get(zone);
            if (obj3 == null) {
                obj3 = new ArrayList();
                orderedMap.put(zone, obj3);
            }
            ((List) obj3).add(obj2);
        }
        ArrayList arrayList2 = new ArrayList();
        TransformingSequence transformingSequence = null;
        Iterator it2 = orderedMap.orderedKeys.iterator();
        while (it2.hasNext()) {
            CharSequence charSequence2 = (CharSequence) it2.next();
            TransformingSequence transformingSequence2 = new TransformingSequence(new CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1((Iterable) MapsKt___MapsKt.getValue(orderedMap, charSequence2)), AllModel$createWrappers$values$1.INSTANCE);
            if (TextUtils.isEmpty(charSequence2)) {
                transformingSequence = transformingSequence2;
            } else {
                arrayList2.add(new ZoneNameWrapper(charSequence2));
                CollectionsKt__ReversedViewsKt.addAll(arrayList2, transformingSequence2);
            }
        }
        if (transformingSequence != null) {
            if (orderedMap.size() != 1) {
                arrayList2.add(new ZoneNameWrapper(this.emptyZoneString));
            }
            CollectionsKt__ReversedViewsKt.addAll(arrayList2, transformingSequence);
        }
        this.elements = arrayList2;
    }

    @Override // com.android.systemui.controls.management.ControlsModel
    public final List<ElementWrapper> getElements() {
        return this.elements;
    }
}
