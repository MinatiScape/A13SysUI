package com.android.wm.shell.bubbles.storage;

import android.content.pm.LauncherApps;
import android.os.UserHandle;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.bubbles.ShortcutKey;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: BubbleVolatileRepository.kt */
/* loaded from: classes.dex */
public final class BubbleVolatileRepository {
    public final LauncherApps launcherApps;
    public SparseArray<List<BubbleEntity>> entitiesByUser = new SparseArray<>();
    public int capacity = 16;

    @VisibleForTesting
    public static /* synthetic */ void getCapacity$annotations() {
    }

    public final synchronized void addBubbles(int i, List<BubbleEntity> list) {
        if (!list.isEmpty()) {
            synchronized (this) {
                List<BubbleEntity> list2 = this.entitiesByUser.get(i);
                if (list2 == null) {
                    list2 = new ArrayList();
                    this.entitiesByUser.put(i, list2);
                }
                List takeLast = CollectionsKt___CollectionsKt.takeLast(list, this.capacity);
                ArrayList arrayList = new ArrayList();
                for (Object obj : takeLast) {
                    final BubbleEntity bubbleEntity = (BubbleEntity) obj;
                    if (!list2.removeIf(new Predicate() { // from class: com.android.wm.shell.bubbles.storage.BubbleVolatileRepository$addBubbles$uniqueBubbles$1$1
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj2) {
                            BubbleEntity bubbleEntity2 = BubbleEntity.this;
                            Objects.requireNonNull(bubbleEntity2);
                            return Intrinsics.areEqual(bubbleEntity2.key, ((BubbleEntity) obj2).key);
                        }
                    })) {
                        arrayList.add(obj);
                    }
                }
                int size = (list2.size() + takeLast.size()) - this.capacity;
                if (size > 0) {
                    uncache(CollectionsKt___CollectionsKt.take(list2, size));
                    list2 = new ArrayList(CollectionsKt___CollectionsKt.drop(list2, size));
                }
                list2.addAll(takeLast);
                this.entitiesByUser.put(i, list2);
                cache(arrayList);
            }
        }
    }

    public final void cache(ArrayList arrayList) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Object next = it.next();
            BubbleEntity bubbleEntity = (BubbleEntity) next;
            Objects.requireNonNull(bubbleEntity);
            ShortcutKey shortcutKey = new ShortcutKey(bubbleEntity.userId, bubbleEntity.packageName);
            Object obj = linkedHashMap.get(shortcutKey);
            if (obj == null) {
                obj = new ArrayList();
                linkedHashMap.put(shortcutKey, obj);
            }
            ((List) obj).add(next);
        }
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            ShortcutKey shortcutKey2 = (ShortcutKey) entry.getKey();
            List<BubbleEntity> list = (List) entry.getValue();
            LauncherApps launcherApps = this.launcherApps;
            Objects.requireNonNull(shortcutKey2);
            String str = shortcutKey2.pkg;
            ArrayList arrayList2 = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(list, 10));
            for (BubbleEntity bubbleEntity2 : list) {
                Objects.requireNonNull(bubbleEntity2);
                arrayList2.add(bubbleEntity2.shortcutId);
            }
            launcherApps.cacheShortcuts(str, arrayList2, UserHandle.of(shortcutKey2.userId), 1);
        }
    }

    public final void uncache(List<BubbleEntity> list) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Object obj : list) {
            BubbleEntity bubbleEntity = (BubbleEntity) obj;
            Objects.requireNonNull(bubbleEntity);
            ShortcutKey shortcutKey = new ShortcutKey(bubbleEntity.userId, bubbleEntity.packageName);
            Object obj2 = linkedHashMap.get(shortcutKey);
            if (obj2 == null) {
                obj2 = new ArrayList();
                linkedHashMap.put(shortcutKey, obj2);
            }
            ((List) obj2).add(obj);
        }
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            ShortcutKey shortcutKey2 = (ShortcutKey) entry.getKey();
            List<BubbleEntity> list2 = (List) entry.getValue();
            LauncherApps launcherApps = this.launcherApps;
            Objects.requireNonNull(shortcutKey2);
            String str = shortcutKey2.pkg;
            ArrayList arrayList = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(list2, 10));
            for (BubbleEntity bubbleEntity2 : list2) {
                Objects.requireNonNull(bubbleEntity2);
                arrayList.add(bubbleEntity2.shortcutId);
            }
            launcherApps.uncacheShortcuts(str, arrayList, UserHandle.of(shortcutKey2.userId), 1);
        }
    }

    public BubbleVolatileRepository(LauncherApps launcherApps) {
        this.launcherApps = launcherApps;
    }
}
