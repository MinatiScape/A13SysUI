package com.google.protobuf;

import com.google.protobuf.Internal;
import com.google.protobuf.Utf8;
import java.nio.charset.Charset;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
/* loaded from: classes.dex */
public final class LazyStringArrayList extends AbstractProtobufList<String> implements LazyStringList, RandomAccess {
    public final ArrayList list;

    public LazyStringArrayList(int i) {
        this(new ArrayList(i));
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean addAll(Collection<? extends String> collection) {
        return addAll(size(), collection);
    }

    static {
        new LazyStringArrayList(10).isMutable = false;
    }

    public LazyStringArrayList(ArrayList<Object> arrayList) {
        this.list = arrayList;
    }

    @Override // java.util.AbstractList, java.util.List
    public final void add(int i, Object obj) {
        ensureIsMutable();
        this.list.add(i, (String) obj);
        ((AbstractList) this).modCount++;
    }

    @Override // java.util.AbstractList, java.util.List
    public final boolean addAll(int i, Collection<? extends String> collection) {
        ensureIsMutable();
        if (collection instanceof LazyStringList) {
            collection = ((LazyStringList) collection).getUnderlyingElements();
        }
        boolean addAll = this.list.addAll(i, collection);
        ((AbstractList) this).modCount++;
        return addAll;
    }

    @Override // java.util.AbstractList, java.util.List
    public final Object get(int i) {
        String str;
        Object obj = this.list.get(i);
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof ByteString) {
            ByteString byteString = (ByteString) obj;
            Objects.requireNonNull(byteString);
            Charset charset = Internal.UTF_8;
            if (byteString.size() == 0) {
                str = "";
            } else {
                str = byteString.toStringInternal(charset);
            }
            if (byteString.isValidUtf8()) {
                this.list.set(i, str);
            }
        } else {
            byte[] bArr = (byte[]) obj;
            str = new String(bArr, Internal.UTF_8);
            Utf8.Processor processor = Utf8.processor;
            int length = bArr.length;
            Objects.requireNonNull(processor);
            boolean z = false;
            if (processor.partialIsValidUtf8(bArr, 0, length) == 0) {
                z = true;
            }
            if (z) {
                this.list.set(i, str);
            }
        }
        return str;
    }

    @Override // com.google.protobuf.LazyStringList
    public final List<?> getUnderlyingElements() {
        return Collections.unmodifiableList(this.list);
    }

    @Override // com.google.protobuf.LazyStringList
    public final LazyStringList getUnmodifiableView() {
        if (this.isMutable) {
            return new UnmodifiableLazyStringList(this);
        }
        return this;
    }

    @Override // java.util.AbstractList, java.util.List
    public final Object set(int i, Object obj) {
        ensureIsMutable();
        Object obj2 = this.list.set(i, (String) obj);
        if (obj2 instanceof String) {
            return (String) obj2;
        }
        if (!(obj2 instanceof ByteString)) {
            return new String((byte[]) obj2, Internal.UTF_8);
        }
        ByteString byteString = (ByteString) obj2;
        Objects.requireNonNull(byteString);
        Charset charset = Internal.UTF_8;
        if (byteString.size() == 0) {
            return "";
        }
        return byteString.toStringInternal(charset);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.list.size();
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final void clear() {
        ensureIsMutable();
        this.list.clear();
        ((AbstractList) this).modCount++;
    }

    @Override // com.google.protobuf.Internal.ProtobufList
    public final Internal.ProtobufList mutableCopyWithCapacity(int i) {
        if (i >= size()) {
            ArrayList arrayList = new ArrayList(i);
            arrayList.addAll(this.list);
            return new LazyStringArrayList(arrayList);
        }
        throw new IllegalArgumentException();
    }

    @Override // java.util.AbstractList, java.util.List
    public final Object remove(int i) {
        String str;
        ensureIsMutable();
        Object remove = this.list.remove(i);
        ((AbstractList) this).modCount++;
        if (remove instanceof String) {
            return (String) remove;
        }
        if (remove instanceof ByteString) {
            ByteString byteString = (ByteString) remove;
            Objects.requireNonNull(byteString);
            Charset charset = Internal.UTF_8;
            if (byteString.size() == 0) {
                str = "";
            } else {
                str = byteString.toStringInternal(charset);
            }
        } else {
            str = new String((byte[]) remove, Internal.UTF_8);
        }
        return str;
    }
}
