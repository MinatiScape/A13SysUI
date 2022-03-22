package com.android.systemui.privacy;

import android.content.Context;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import kotlin.Pair;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.EmptyList;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt$compareBy$1;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: PrivacyChipBuilder.kt */
/* loaded from: classes.dex */
public final class PrivacyChipBuilder {
    public final List<Pair<PrivacyApplication, List<PrivacyType>>> appsAndTypes;
    public final Context context;
    public final String lastSeparator;
    public final String separator;
    public final List<PrivacyType> types;

    /* compiled from: PrivacyChipBuilder.kt */
    /* renamed from: com.android.systemui.privacy.PrivacyChipBuilder$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass3 extends Lambda implements Function1<Pair<? extends PrivacyApplication, ? extends List<? extends PrivacyType>>, Comparable<?>> {
        public static final AnonymousClass3 INSTANCE = new AnonymousClass3();

        public AnonymousClass3() {
            super(1);
        }

        @Override // kotlin.jvm.functions.Function1
        public final Comparable<?> invoke(Pair<? extends PrivacyApplication, ? extends List<? extends PrivacyType>> pair) {
            return Integer.valueOf(-((List) pair.getSecond()).size());
        }
    }

    /* compiled from: PrivacyChipBuilder.kt */
    /* renamed from: com.android.systemui.privacy.PrivacyChipBuilder$4  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass4 extends Lambda implements Function1<Pair<? extends PrivacyApplication, ? extends List<? extends PrivacyType>>, Comparable<?>> {
        public static final AnonymousClass4 INSTANCE = new AnonymousClass4();

        public AnonymousClass4() {
            super(1);
        }

        @Override // kotlin.jvm.functions.Function1
        public final Comparable<?> invoke(Pair<? extends PrivacyApplication, ? extends List<? extends PrivacyType>> pair) {
            return CollectionsKt___CollectionsKt.minOrNull((Iterable) pair.getSecond());
        }
    }

    public final ArrayList generateIcons() {
        List<PrivacyType> list = this.types;
        ArrayList arrayList = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(list, 10));
        for (PrivacyType privacyType : list) {
            arrayList.add(privacyType.getIcon(this.context));
        }
        return arrayList;
    }

    public final String joinTypes() {
        int size = this.types.size();
        if (size == 0) {
            return "";
        }
        if (size == 1) {
            return this.types.get(0).getName(this.context);
        }
        List<PrivacyType> list = this.types;
        ArrayList arrayList = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(list, 10));
        for (PrivacyType privacyType : list) {
            arrayList.add(privacyType.getName(this.context));
        }
        List subList = arrayList.subList(0, arrayList.size() - 1);
        StringBuilder sb = new StringBuilder();
        CollectionsKt___CollectionsKt.joinTo(subList, sb, this.separator, "", "", -1, "...", null);
        sb.append(this.lastSeparator);
        sb.append(CollectionsKt___CollectionsKt.last(arrayList));
        return sb.toString();
    }

    public PrivacyChipBuilder(Context context, List<PrivacyItem> list) {
        Collection collection;
        List<PrivacyType> list2;
        this.context = context;
        this.separator = context.getString(2131952948);
        this.lastSeparator = context.getString(2131952945);
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (PrivacyItem privacyItem : list) {
            Objects.requireNonNull(privacyItem);
            PrivacyApplication privacyApplication = privacyItem.application;
            Object obj = linkedHashMap.get(privacyApplication);
            if (obj == null) {
                obj = new ArrayList();
                linkedHashMap.put(privacyApplication, obj);
            }
            ((List) obj).add(privacyItem.privacyType);
        }
        if (linkedHashMap.size() == 0) {
            collection = EmptyList.INSTANCE;
        } else {
            Iterator it = linkedHashMap.entrySet().iterator();
            if (!it.hasNext()) {
                collection = EmptyList.INSTANCE;
            } else {
                Map.Entry entry = (Map.Entry) it.next();
                if (!it.hasNext()) {
                    collection = Collections.singletonList(new Pair(entry.getKey(), entry.getValue()));
                } else {
                    ArrayList arrayList = new ArrayList(linkedHashMap.size());
                    arrayList.add(new Pair(entry.getKey(), entry.getValue()));
                    do {
                        Map.Entry entry2 = (Map.Entry) it.next();
                        arrayList.add(new Pair(entry2.getKey(), entry2.getValue()));
                    } while (it.hasNext());
                    collection = arrayList;
                }
            }
        }
        this.appsAndTypes = CollectionsKt___CollectionsKt.sortedWith(collection, new ComparisonsKt__ComparisonsKt$compareBy$1(new Function1[]{AnonymousClass3.INSTANCE, AnonymousClass4.INSTANCE}));
        ArrayList arrayList2 = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(list, 10));
        for (PrivacyItem privacyItem2 : list) {
            Objects.requireNonNull(privacyItem2);
            arrayList2.add(privacyItem2.privacyType);
        }
        List list3 = CollectionsKt___CollectionsKt.toList(CollectionsKt___CollectionsKt.toMutableSet(arrayList2));
        if (!(list3 instanceof Collection)) {
            list2 = CollectionsKt___CollectionsKt.toMutableList(list3);
            if (((ArrayList) list2).size() > 1) {
                Collections.sort(list2);
            }
        } else if (list3.size() <= 1) {
            list2 = CollectionsKt___CollectionsKt.toList(list3);
        } else {
            Object[] array = list3.toArray(new Comparable[0]);
            Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
            Comparable[] comparableArr = (Comparable[]) array;
            if (comparableArr.length > 1) {
                Arrays.sort(comparableArr);
            }
            list2 = Arrays.asList(array);
        }
        this.types = list2;
    }
}
