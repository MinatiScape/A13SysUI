package com.google.common.collect;

import com.google.common.base.Equivalence;
import com.google.common.base.MoreObjects$ToStringHelper;
import com.google.common.collect.MapMakerInternalMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import kotlin.io.ExceptionsKt;
/* loaded from: classes.dex */
public final class MapMaker {
    public Equivalence<Object> keyEquivalence;
    public MapMakerInternalMap.Strength keyStrength;
    public boolean useCustomMap;
    public MapMakerInternalMap.Strength valueStrength;
    public int initialCapacity = -1;
    public int concurrencyLevel = -1;

    public final MapMakerInternalMap.Strength getKeyStrength() {
        MapMakerInternalMap.Strength strength = this.keyStrength;
        MapMakerInternalMap.Strength.AnonymousClass1 r0 = MapMakerInternalMap.Strength.STRONG;
        if (strength != null) {
            return strength;
        }
        return r0;
    }

    public final <K, V> ConcurrentMap<K, V> makeMap() {
        if (!this.useCustomMap) {
            int i = this.initialCapacity;
            if (i == -1) {
                i = 16;
            }
            int i2 = this.concurrencyLevel;
            if (i2 == -1) {
                i2 = 4;
            }
            return new ConcurrentHashMap(i, 0.75f, i2);
        }
        MapMakerInternalMap.AnonymousClass1 r0 = MapMakerInternalMap.UNSET_WEAK_VALUE_REFERENCE;
        MapMakerInternalMap.Strength.AnonymousClass2 r02 = MapMakerInternalMap.Strength.WEAK;
        MapMakerInternalMap.Strength keyStrength = getKeyStrength();
        MapMakerInternalMap.Strength strength = MapMakerInternalMap.Strength.STRONG;
        if (keyStrength == strength) {
            MapMakerInternalMap.Strength strength2 = this.valueStrength;
            if (strength2 == null) {
                strength2 = strength;
            }
            if (strength2 == strength) {
                return new MapMakerInternalMap(this, MapMakerInternalMap.StrongKeyStrongValueEntry.Helper.INSTANCE);
            }
        }
        if (getKeyStrength() == strength) {
            MapMakerInternalMap.Strength strength3 = this.valueStrength;
            if (strength3 == null) {
                strength3 = strength;
            }
            if (strength3 == r02) {
                return new MapMakerInternalMap(this, MapMakerInternalMap.StrongKeyWeakValueEntry.Helper.INSTANCE);
            }
        }
        if (getKeyStrength() == r02) {
            MapMakerInternalMap.Strength strength4 = this.valueStrength;
            if (strength4 == null) {
                strength4 = strength;
            }
            if (strength4 == strength) {
                return new MapMakerInternalMap(this, MapMakerInternalMap.WeakKeyStrongValueEntry.Helper.INSTANCE);
            }
        }
        if (getKeyStrength() == r02) {
            MapMakerInternalMap.Strength strength5 = this.valueStrength;
            if (strength5 != null) {
                strength = strength5;
            }
            if (strength == r02) {
                return new MapMakerInternalMap(this, MapMakerInternalMap.WeakKeyWeakValueEntry.Helper.INSTANCE);
            }
        }
        throw new AssertionError();
    }

    public final String toString() {
        MoreObjects$ToStringHelper moreObjects$ToStringHelper = new MoreObjects$ToStringHelper("MapMaker");
        int i = this.initialCapacity;
        if (i != -1) {
            String valueOf = String.valueOf(i);
            MoreObjects$ToStringHelper.UnconditionalValueHolder unconditionalValueHolder = new MoreObjects$ToStringHelper.UnconditionalValueHolder();
            moreObjects$ToStringHelper.holderTail.next = unconditionalValueHolder;
            moreObjects$ToStringHelper.holderTail = unconditionalValueHolder;
            unconditionalValueHolder.value = valueOf;
            unconditionalValueHolder.name = "initialCapacity";
        }
        int i2 = this.concurrencyLevel;
        if (i2 != -1) {
            String valueOf2 = String.valueOf(i2);
            MoreObjects$ToStringHelper.UnconditionalValueHolder unconditionalValueHolder2 = new MoreObjects$ToStringHelper.UnconditionalValueHolder();
            moreObjects$ToStringHelper.holderTail.next = unconditionalValueHolder2;
            moreObjects$ToStringHelper.holderTail = unconditionalValueHolder2;
            unconditionalValueHolder2.value = valueOf2;
            unconditionalValueHolder2.name = "concurrencyLevel";
        }
        MapMakerInternalMap.Strength strength = this.keyStrength;
        if (strength != null) {
            String lowerCase = ExceptionsKt.toLowerCase(strength.toString());
            MoreObjects$ToStringHelper.ValueHolder valueHolder = new MoreObjects$ToStringHelper.ValueHolder();
            moreObjects$ToStringHelper.holderTail.next = valueHolder;
            moreObjects$ToStringHelper.holderTail = valueHolder;
            valueHolder.value = lowerCase;
            valueHolder.name = "keyStrength";
        }
        MapMakerInternalMap.Strength strength2 = this.valueStrength;
        if (strength2 != null) {
            String lowerCase2 = ExceptionsKt.toLowerCase(strength2.toString());
            MoreObjects$ToStringHelper.ValueHolder valueHolder2 = new MoreObjects$ToStringHelper.ValueHolder();
            moreObjects$ToStringHelper.holderTail.next = valueHolder2;
            moreObjects$ToStringHelper.holderTail = valueHolder2;
            valueHolder2.value = lowerCase2;
            valueHolder2.name = "valueStrength";
        }
        if (this.keyEquivalence != null) {
            MoreObjects$ToStringHelper.ValueHolder valueHolder3 = new MoreObjects$ToStringHelper.ValueHolder();
            moreObjects$ToStringHelper.holderTail.next = valueHolder3;
            moreObjects$ToStringHelper.holderTail = valueHolder3;
            valueHolder3.value = "keyEquivalence";
        }
        return moreObjects$ToStringHelper.toString();
    }
}
