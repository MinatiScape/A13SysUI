package com.google.protobuf;

import com.google.protobuf.Internal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public abstract class ListFieldSchema {
    public static final ListFieldSchemaFull FULL_INSTANCE = new ListFieldSchemaFull();
    public static final ListFieldSchemaLite LITE_INSTANCE = new ListFieldSchemaLite();

    public abstract void makeImmutableListAt(Object obj, long j);

    public abstract <L> void mergeListsAt(Object obj, Object obj2, long j);

    /* loaded from: classes.dex */
    public static final class ListFieldSchemaFull extends ListFieldSchema {
        public static final Class<?> UNMODIFIABLE_LIST_CLASS = Collections.unmodifiableList(Collections.emptyList()).getClass();

        public static <L> List<L> mutableListAt(Object obj, long j, int i) {
            ArrayList arrayList;
            List<L> list;
            List<L> list2 = (List) UnsafeUtil.getObject(obj, j);
            if (list2.isEmpty()) {
                if (list2 instanceof LazyStringList) {
                    list = new LazyStringArrayList(i);
                } else if (!(list2 instanceof PrimitiveNonBoxingCollection) || !(list2 instanceof Internal.ProtobufList)) {
                    list = new ArrayList<>(i);
                } else {
                    list = ((Internal.ProtobufList) list2).mutableCopyWithCapacity(i);
                }
                UnsafeUtil.putObject(obj, j, list);
                return list;
            }
            if (UNMODIFIABLE_LIST_CLASS.isAssignableFrom(list2.getClass())) {
                ArrayList arrayList2 = new ArrayList(list2.size() + i);
                arrayList2.addAll(list2);
                UnsafeUtil.putObject(obj, j, arrayList2);
                arrayList = arrayList2;
            } else if (list2 instanceof UnmodifiableLazyStringList) {
                LazyStringArrayList lazyStringArrayList = new LazyStringArrayList(list2.size() + i);
                lazyStringArrayList.addAll((UnmodifiableLazyStringList) list2);
                UnsafeUtil.putObject(obj, j, lazyStringArrayList);
                arrayList = lazyStringArrayList;
            } else if (!(list2 instanceof PrimitiveNonBoxingCollection) || !(list2 instanceof Internal.ProtobufList)) {
                return list2;
            } else {
                Internal.ProtobufList protobufList = (Internal.ProtobufList) list2;
                if (protobufList.isModifiable()) {
                    return list2;
                }
                Internal.ProtobufList mutableCopyWithCapacity = protobufList.mutableCopyWithCapacity(list2.size() + i);
                UnsafeUtil.putObject(obj, j, mutableCopyWithCapacity);
                return mutableCopyWithCapacity;
            }
            return arrayList;
        }

        @Override // com.google.protobuf.ListFieldSchema
        public final void makeImmutableListAt(Object obj, long j) {
            Object obj2;
            List list = (List) UnsafeUtil.getObject(obj, j);
            if (list instanceof LazyStringList) {
                obj2 = ((LazyStringList) list).getUnmodifiableView();
            } else if (!UNMODIFIABLE_LIST_CLASS.isAssignableFrom(list.getClass())) {
                if (!(list instanceof PrimitiveNonBoxingCollection) || !(list instanceof Internal.ProtobufList)) {
                    obj2 = Collections.unmodifiableList(list);
                } else {
                    Internal.ProtobufList protobufList = (Internal.ProtobufList) list;
                    if (protobufList.isModifiable()) {
                        protobufList.makeImmutable();
                        return;
                    }
                    return;
                }
            } else {
                return;
            }
            UnsafeUtil.putObject(obj, j, obj2);
        }

        @Override // com.google.protobuf.ListFieldSchema
        public final <E> void mergeListsAt(Object obj, Object obj2, long j) {
            List list = (List) UnsafeUtil.getObject(obj2, j);
            List mutableListAt = mutableListAt(obj, j, list.size());
            int size = mutableListAt.size();
            int size2 = list.size();
            if (size > 0 && size2 > 0) {
                mutableListAt.addAll(list);
            }
            if (size > 0) {
                list = mutableListAt;
            }
            UnsafeUtil.putObject(obj, j, list);
        }
    }

    /* loaded from: classes.dex */
    public static final class ListFieldSchemaLite extends ListFieldSchema {
        @Override // com.google.protobuf.ListFieldSchema
        public final void makeImmutableListAt(Object obj, long j) {
            ((Internal.ProtobufList) UnsafeUtil.getObject(obj, j)).makeImmutable();
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r3v4, types: [java.util.List] */
        @Override // com.google.protobuf.ListFieldSchema
        public final <E> void mergeListsAt(Object obj, Object obj2, long j) {
            Internal.ProtobufList<E> protobufList = (Internal.ProtobufList) UnsafeUtil.getObject(obj, j);
            Internal.ProtobufList<E> protobufList2 = (Internal.ProtobufList) UnsafeUtil.getObject(obj2, j);
            int size = protobufList.size();
            int size2 = protobufList2.size();
            Internal.ProtobufList<E> protobufList3 = protobufList;
            protobufList3 = protobufList;
            if (size > 0 && size2 > 0) {
                boolean isModifiable = protobufList.isModifiable();
                Internal.ProtobufList<E> protobufList4 = protobufList;
                if (!isModifiable) {
                    protobufList4 = protobufList.mutableCopyWithCapacity(size2 + size);
                }
                protobufList4.addAll(protobufList2);
                protobufList3 = protobufList4;
            }
            if (size > 0) {
                protobufList2 = protobufList3;
            }
            UnsafeUtil.putObject(obj, j, protobufList2);
        }
    }
}
