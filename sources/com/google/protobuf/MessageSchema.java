package com.google.protobuf;

import androidx.activity.result.ActivityResultRegistry$3$$ExternalSyntheticOutline0;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.google.android.setupcompat.logging.CustomEvent;
import com.google.protobuf.UnsafeUtil;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import sun.misc.Unsafe;
/* loaded from: classes.dex */
public final class MessageSchema<T> implements Schema<T> {
    public static final int[] EMPTY_INT_ARRAY = new int[0];
    public static final Unsafe UNSAFE;
    public final int[] buffer;
    public final int checkInitializedCount;
    public final MessageLite defaultInstance;
    public final ExtensionSchema<?> extensionSchema;
    public final boolean hasExtensions;
    public final int[] intArray;
    public final ListFieldSchema listFieldSchema;
    public final boolean lite;
    public final MapFieldSchema mapFieldSchema;
    public final int maxFieldNumber;
    public final int minFieldNumber;
    public final NewInstanceSchema newInstanceSchema;
    public final Object[] objects;
    public final boolean proto3;
    public final int repeatedFieldOffsetStart;
    public final UnknownFieldSchema<?, ?> unknownFieldSchema;
    public final boolean useCachedSizeField;

    static {
        Unsafe unsafe;
        try {
            unsafe = (Unsafe) AccessController.doPrivileged(new UnsafeUtil.AnonymousClass1());
        } catch (Throwable unused) {
            unsafe = null;
        }
        UNSAFE = unsafe;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.protobuf.Schema
    public final boolean isInitialized(T t) {
        int i;
        boolean z;
        boolean z2;
        int i2 = -1;
        int i3 = 0;
        int i4 = 0;
        while (true) {
            boolean z3 = true;
            if (i3 >= this.checkInitializedCount) {
                return !this.hasExtensions || this.extensionSchema.getExtensions(t).isInitialized();
            }
            int i5 = this.intArray[i3];
            int i6 = this.buffer[i5];
            int typeAndOffsetAt = typeAndOffsetAt(i5);
            if (!this.proto3) {
                int i7 = this.buffer[i5 + 2];
                int i8 = i7 & 1048575;
                i = 1 << (i7 >>> 20);
                if (i8 != i2) {
                    i4 = UNSAFE.getInt(t, i8);
                    i2 = i8;
                }
            } else {
                i = 0;
            }
            if ((268435456 & typeAndOffsetAt) != 0) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                if (this.proto3) {
                    z2 = isFieldPresent(t, i5);
                } else if ((i4 & i) != 0) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (!z2) {
                    return false;
                }
            }
            int i9 = (267386880 & typeAndOffsetAt) >>> 20;
            if (i9 == 9 || i9 == 17) {
                if (this.proto3) {
                    z3 = isFieldPresent(t, i5);
                } else if ((i4 & i) == 0) {
                    z3 = false;
                }
                if (z3 && !getMessageFieldSchema(i5).isInitialized(UnsafeUtil.getObject(t, typeAndOffsetAt & 1048575))) {
                    return false;
                }
            } else {
                if (i9 != 27) {
                    if (i9 == 60 || i9 == 68) {
                        if (isOneofPresent(t, i6, i5) && !getMessageFieldSchema(i5).isInitialized(UnsafeUtil.getObject(t, typeAndOffsetAt & 1048575))) {
                            return false;
                        }
                    } else if (i9 != 49) {
                        if (i9 == 50 && !this.mapFieldSchema.forMapData(UnsafeUtil.getObject(t, typeAndOffsetAt & 1048575)).isEmpty()) {
                            this.mapFieldSchema.forMapMetadata(getMapFieldDefaultEntry(i5));
                            throw null;
                        }
                    }
                }
                List list = (List) UnsafeUtil.getObject(t, typeAndOffsetAt & 1048575);
                if (!list.isEmpty()) {
                    Schema messageFieldSchema = getMessageFieldSchema(i5);
                    int i10 = 0;
                    while (true) {
                        if (i10 >= list.size()) {
                            break;
                        } else if (!messageFieldSchema.isInitialized(list.get(i10))) {
                            z3 = false;
                            break;
                        } else {
                            i10++;
                        }
                    }
                }
                if (!z3) {
                    return false;
                }
            }
            i3++;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:127:0x0296  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0299  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x02b0  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x02b3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.google.protobuf.MessageSchema newSchema(com.google.protobuf.MessageInfo r35, com.google.protobuf.NewInstanceSchema r36, com.google.protobuf.ListFieldSchema r37, com.google.protobuf.UnknownFieldSchema r38, com.google.protobuf.ExtensionSchema r39, com.google.protobuf.MapFieldSchema r40) {
        /*
            Method dump skipped, instructions count: 1081
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.MessageSchema.newSchema(com.google.protobuf.MessageInfo, com.google.protobuf.NewInstanceSchema, com.google.protobuf.ListFieldSchema, com.google.protobuf.UnknownFieldSchema, com.google.protobuf.ExtensionSchema, com.google.protobuf.MapFieldSchema):com.google.protobuf.MessageSchema");
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x003f, code lost:
        if (com.google.protobuf.SchemaUtil.safeEquals(com.google.protobuf.UnsafeUtil.getObject(r10, r6), com.google.protobuf.UnsafeUtil.getObject(r11, r6)) != false) goto L_0x01cb;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0071, code lost:
        if (com.google.protobuf.SchemaUtil.safeEquals(com.google.protobuf.UnsafeUtil.getObject(r10, r6), com.google.protobuf.UnsafeUtil.getObject(r11, r6)) != false) goto L_0x01cb;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0085, code lost:
        if (com.google.protobuf.UnsafeUtil.getLong(r10, r6) == com.google.protobuf.UnsafeUtil.getLong(r11, r6)) goto L_0x01cb;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0097, code lost:
        if (com.google.protobuf.UnsafeUtil.getInt(r10, r6) == com.google.protobuf.UnsafeUtil.getInt(r11, r6)) goto L_0x01cb;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00ab, code lost:
        if (com.google.protobuf.UnsafeUtil.getLong(r10, r6) == com.google.protobuf.UnsafeUtil.getLong(r11, r6)) goto L_0x01cb;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00bd, code lost:
        if (com.google.protobuf.UnsafeUtil.getInt(r10, r6) == com.google.protobuf.UnsafeUtil.getInt(r11, r6)) goto L_0x01cb;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00cf, code lost:
        if (com.google.protobuf.UnsafeUtil.getInt(r10, r6) == com.google.protobuf.UnsafeUtil.getInt(r11, r6)) goto L_0x01cb;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00e1, code lost:
        if (com.google.protobuf.UnsafeUtil.getInt(r10, r6) == com.google.protobuf.UnsafeUtil.getInt(r11, r6)) goto L_0x01cb;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00f7, code lost:
        if (com.google.protobuf.SchemaUtil.safeEquals(com.google.protobuf.UnsafeUtil.getObject(r10, r6), com.google.protobuf.UnsafeUtil.getObject(r11, r6)) != false) goto L_0x01cb;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x010d, code lost:
        if (com.google.protobuf.SchemaUtil.safeEquals(com.google.protobuf.UnsafeUtil.getObject(r10, r6), com.google.protobuf.UnsafeUtil.getObject(r11, r6)) != false) goto L_0x01cb;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0123, code lost:
        if (com.google.protobuf.SchemaUtil.safeEquals(com.google.protobuf.UnsafeUtil.getObject(r10, r6), com.google.protobuf.UnsafeUtil.getObject(r11, r6)) != false) goto L_0x01cb;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0135, code lost:
        if (com.google.protobuf.UnsafeUtil.getBoolean(r10, r6) == com.google.protobuf.UnsafeUtil.getBoolean(r11, r6)) goto L_0x01cb;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0147, code lost:
        if (com.google.protobuf.UnsafeUtil.getInt(r10, r6) == com.google.protobuf.UnsafeUtil.getInt(r11, r6)) goto L_0x01cb;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x015b, code lost:
        if (com.google.protobuf.UnsafeUtil.getLong(r10, r6) == com.google.protobuf.UnsafeUtil.getLong(r11, r6)) goto L_0x01cb;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x016d, code lost:
        if (com.google.protobuf.UnsafeUtil.getInt(r10, r6) == com.google.protobuf.UnsafeUtil.getInt(r11, r6)) goto L_0x01cb;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0180, code lost:
        if (com.google.protobuf.UnsafeUtil.getLong(r10, r6) == com.google.protobuf.UnsafeUtil.getLong(r11, r6)) goto L_0x01cb;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0193, code lost:
        if (com.google.protobuf.UnsafeUtil.getLong(r10, r6) == com.google.protobuf.UnsafeUtil.getLong(r11, r6)) goto L_0x01cb;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x01ac, code lost:
        if (java.lang.Float.floatToIntBits(com.google.protobuf.UnsafeUtil.getFloat(r10, r6)) == java.lang.Float.floatToIntBits(com.google.protobuf.UnsafeUtil.getFloat(r11, r6))) goto L_0x01cb;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x01c7, code lost:
        if (java.lang.Double.doubleToLongBits(com.google.protobuf.UnsafeUtil.getDouble(r10, r6)) == java.lang.Double.doubleToLongBits(com.google.protobuf.UnsafeUtil.getDouble(r11, r6))) goto L_0x01cb;
     */
    @Override // com.google.protobuf.Schema
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean equals(T r10, T r11) {
        /*
            Method dump skipped, instructions count: 650
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.MessageSchema.equals(java.lang.Object, java.lang.Object):boolean");
    }

    public final Object getMapFieldDefaultEntry(int i) {
        return this.objects[(i / 3) * 2];
    }

    public final Schema getMessageFieldSchema(int i) {
        int i2 = (i / 3) * 2;
        Object[] objArr = this.objects;
        Schema schema = (Schema) objArr[i2];
        if (schema != null) {
            return schema;
        }
        Schema<T> schemaFor = Protobuf.INSTANCE.schemaFor((Class) objArr[i2 + 1]);
        this.objects[i2] = schemaFor;
        return schemaFor;
    }

    /* JADX WARN: Code restructure failed: missing block: B:39:0x00de, code lost:
        if (r3 != false) goto L_0x01f7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x01f3, code lost:
        if (r3 != false) goto L_0x01f7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x01f6, code lost:
        r8 = 1237;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x01f7, code lost:
        r3 = r8;
     */
    @Override // com.google.protobuf.Schema
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int hashCode(T r11) {
        /*
            Method dump skipped, instructions count: 756
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.MessageSchema.hashCode(java.lang.Object):int");
    }

    public final boolean isFieldPresent(T t, int i) {
        int i2;
        boolean equals;
        if (this.proto3) {
            int typeAndOffsetAt = typeAndOffsetAt(i);
            long j = typeAndOffsetAt & 1048575;
            switch ((typeAndOffsetAt & 267386880) >>> 20) {
                case 0:
                    if (UnsafeUtil.getDouble(t, j) != 0.0d) {
                        return true;
                    }
                    return false;
                case 1:
                    if (UnsafeUtil.getFloat(t, j) != 0.0f) {
                        return true;
                    }
                    return false;
                case 2:
                    if (UnsafeUtil.getLong(t, j) != 0) {
                        return true;
                    }
                    return false;
                case 3:
                    if (UnsafeUtil.getLong(t, j) != 0) {
                        return true;
                    }
                    return false;
                case 4:
                    if (UnsafeUtil.getInt(t, j) != 0) {
                        return true;
                    }
                    return false;
                case 5:
                    if (UnsafeUtil.getLong(t, j) != 0) {
                        return true;
                    }
                    return false;
                case FalsingManager.VERSION /* 6 */:
                    if (UnsafeUtil.getInt(t, j) != 0) {
                        return true;
                    }
                    return false;
                case 7:
                    return UnsafeUtil.getBoolean(t, j);
                case 8:
                    Object object = UnsafeUtil.getObject(t, j);
                    if (object instanceof String) {
                        equals = ((String) object).isEmpty();
                        break;
                    } else if (object instanceof ByteString) {
                        equals = ByteString.EMPTY.equals(object);
                        break;
                    } else {
                        throw new IllegalArgumentException();
                    }
                case 9:
                    if (UnsafeUtil.getObject(t, j) != null) {
                        return true;
                    }
                    return false;
                case 10:
                    equals = ByteString.EMPTY.equals(UnsafeUtil.getObject(t, j));
                    break;
                case QSTileImpl.H.STALE /* 11 */:
                    if (UnsafeUtil.getInt(t, j) != 0) {
                        return true;
                    }
                    return false;
                case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
                    if (UnsafeUtil.getInt(t, j) != 0) {
                        return true;
                    }
                    return false;
                case QS.VERSION /* 13 */:
                    if (UnsafeUtil.getInt(t, j) != 0) {
                        return true;
                    }
                    return false;
                case 14:
                    if (UnsafeUtil.getLong(t, j) != 0) {
                        return true;
                    }
                    return false;
                case 15:
                    if (UnsafeUtil.getInt(t, j) != 0) {
                        return true;
                    }
                    return false;
                case 16:
                    if (UnsafeUtil.getLong(t, j) != 0) {
                        return true;
                    }
                    return false;
                case 17:
                    if (UnsafeUtil.getObject(t, j) != null) {
                        return true;
                    }
                    return false;
                default:
                    throw new IllegalArgumentException();
            }
            return !equals;
        }
        if ((UnsafeUtil.getInt(t, i2 & 1048575) & (1 << (this.buffer[i + 2] >>> 20))) != 0) {
            return true;
        }
        return false;
    }

    public final boolean isOneofPresent(T t, int i, int i2) {
        if (UnsafeUtil.getInt(t, this.buffer[i2 + 2] & 1048575) == i) {
            return true;
        }
        return false;
    }

    @Override // com.google.protobuf.Schema
    public final void makeImmutable(T t) {
        int i;
        int i2 = this.checkInitializedCount;
        while (true) {
            i = this.repeatedFieldOffsetStart;
            if (i2 >= i) {
                break;
            }
            long typeAndOffsetAt = typeAndOffsetAt(this.intArray[i2]) & 1048575;
            Object object = UnsafeUtil.getObject(t, typeAndOffsetAt);
            if (object != null) {
                UnsafeUtil.putObject(t, typeAndOffsetAt, this.mapFieldSchema.toImmutable(object));
            }
            i2++;
        }
        int length = this.intArray.length;
        while (i < length) {
            this.listFieldSchema.makeImmutableListAt(t, this.intArray[i]);
            i++;
        }
        this.unknownFieldSchema.makeImmutable(t);
        if (this.hasExtensions) {
            this.extensionSchema.makeImmutable(t);
        }
    }

    public final void setFieldPresent(T t, int i) {
        if (!this.proto3) {
            int i2 = this.buffer[i + 2];
            long j = i2 & 1048575;
            UnsafeUtil.putInt(t, j, UnsafeUtil.getInt(t, j) | (1 << (i2 >>> 20)));
        }
    }

    public final void setOneofPresent(T t, int i, int i2) {
        UnsafeUtil.putInt(t, this.buffer[i2 + 2] & 1048575, i);
    }

    public final int typeAndOffsetAt(int i) {
        return this.buffer[i + 1];
    }

    public MessageSchema(int[] iArr, Object[] objArr, int i, int i2, MessageLite messageLite, boolean z, int[] iArr2, int i3, int i4, NewInstanceSchema newInstanceSchema, ListFieldSchema listFieldSchema, UnknownFieldSchema unknownFieldSchema, ExtensionSchema extensionSchema, MapFieldSchema mapFieldSchema) {
        boolean z2;
        this.buffer = iArr;
        this.objects = objArr;
        this.minFieldNumber = i;
        this.maxFieldNumber = i2;
        this.lite = messageLite instanceof GeneratedMessageLite;
        this.proto3 = z;
        if (extensionSchema == null || !extensionSchema.hasExtensions(messageLite)) {
            z2 = false;
        } else {
            z2 = true;
        }
        this.hasExtensions = z2;
        this.useCachedSizeField = false;
        this.intArray = iArr2;
        this.checkInitializedCount = i3;
        this.repeatedFieldOffsetStart = i4;
        this.newInstanceSchema = newInstanceSchema;
        this.listFieldSchema = listFieldSchema;
        this.unknownFieldSchema = unknownFieldSchema;
        this.extensionSchema = extensionSchema;
        this.defaultInstance = messageLite;
        this.mapFieldSchema = mapFieldSchema;
    }

    public static <T> int oneofIntAt(T t, long j) {
        return ((Integer) UnsafeUtil.getObject(t, j)).intValue();
    }

    public static <T> long oneofLongAt(T t, long j) {
        return ((Long) UnsafeUtil.getObject(t, j)).longValue();
    }

    public static Field reflectField(Class<?> cls, String str) {
        try {
            return cls.getDeclaredField(str);
        } catch (NoSuchFieldException unused) {
            Field[] declaredFields = cls.getDeclaredFields();
            for (Field field : declaredFields) {
                if (str.equals(field.getName())) {
                    return field;
                }
            }
            StringBuilder m = ActivityResultRegistry$3$$ExternalSyntheticOutline0.m("Field ", str, " for ");
            m.append(cls.getName());
            m.append(" not found. Known fields are ");
            m.append(Arrays.toString(declaredFields));
            throw new RuntimeException(m.toString());
        }
    }

    public final boolean arePresentForEquals(T t, T t2, int i) {
        if (isFieldPresent(t, i) == isFieldPresent(t2, i)) {
            return true;
        }
        return false;
    }

    @Override // com.google.protobuf.Schema
    public final void mergeFrom(T t, T t2) {
        Objects.requireNonNull(t2);
        for (int i = 0; i < this.buffer.length; i += 3) {
            int typeAndOffsetAt = typeAndOffsetAt(i);
            long j = 1048575 & typeAndOffsetAt;
            int i2 = this.buffer[i];
            switch ((typeAndOffsetAt & 267386880) >>> 20) {
                case 0:
                    if (isFieldPresent(t2, i)) {
                        double d = UnsafeUtil.getDouble(t2, j);
                        UnsafeUtil.JvmMemoryAccessor jvmMemoryAccessor = UnsafeUtil.MEMORY_ACCESSOR;
                        Objects.requireNonNull(jvmMemoryAccessor);
                        jvmMemoryAccessor.unsafe.putDouble(t, j, d);
                        setFieldPresent(t, i);
                        break;
                    } else {
                        break;
                    }
                case 1:
                    if (isFieldPresent(t2, i)) {
                        float f = UnsafeUtil.getFloat(t2, j);
                        UnsafeUtil.JvmMemoryAccessor jvmMemoryAccessor2 = UnsafeUtil.MEMORY_ACCESSOR;
                        Objects.requireNonNull(jvmMemoryAccessor2);
                        jvmMemoryAccessor2.unsafe.putFloat(t, j, f);
                        setFieldPresent(t, i);
                        break;
                    } else {
                        break;
                    }
                case 2:
                    if (isFieldPresent(t2, i)) {
                        UnsafeUtil.putLong(t, j, UnsafeUtil.getLong(t2, j));
                        setFieldPresent(t, i);
                        break;
                    } else {
                        break;
                    }
                case 3:
                    if (isFieldPresent(t2, i)) {
                        UnsafeUtil.putLong(t, j, UnsafeUtil.getLong(t2, j));
                        setFieldPresent(t, i);
                        break;
                    } else {
                        break;
                    }
                case 4:
                    if (isFieldPresent(t2, i)) {
                        UnsafeUtil.putInt(t, j, UnsafeUtil.getInt(t2, j));
                        setFieldPresent(t, i);
                        break;
                    } else {
                        break;
                    }
                case 5:
                    if (isFieldPresent(t2, i)) {
                        UnsafeUtil.putLong(t, j, UnsafeUtil.getLong(t2, j));
                        setFieldPresent(t, i);
                        break;
                    } else {
                        break;
                    }
                case FalsingManager.VERSION /* 6 */:
                    if (isFieldPresent(t2, i)) {
                        UnsafeUtil.putInt(t, j, UnsafeUtil.getInt(t2, j));
                        setFieldPresent(t, i);
                        break;
                    } else {
                        break;
                    }
                case 7:
                    if (isFieldPresent(t2, i)) {
                        boolean z = UnsafeUtil.getBoolean(t2, j);
                        UnsafeUtil.JvmMemoryAccessor jvmMemoryAccessor3 = UnsafeUtil.MEMORY_ACCESSOR;
                        Objects.requireNonNull(jvmMemoryAccessor3);
                        jvmMemoryAccessor3.unsafe.putBoolean(t, j, z);
                        setFieldPresent(t, i);
                        break;
                    } else {
                        break;
                    }
                case 8:
                    if (isFieldPresent(t2, i)) {
                        UnsafeUtil.putObject(t, j, UnsafeUtil.getObject(t2, j));
                        setFieldPresent(t, i);
                        break;
                    } else {
                        break;
                    }
                case 9:
                    mergeMessage(t, t2, i);
                    break;
                case 10:
                    if (isFieldPresent(t2, i)) {
                        UnsafeUtil.putObject(t, j, UnsafeUtil.getObject(t2, j));
                        setFieldPresent(t, i);
                        break;
                    } else {
                        break;
                    }
                case QSTileImpl.H.STALE /* 11 */:
                    if (isFieldPresent(t2, i)) {
                        UnsafeUtil.putInt(t, j, UnsafeUtil.getInt(t2, j));
                        setFieldPresent(t, i);
                        break;
                    } else {
                        break;
                    }
                case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
                    if (isFieldPresent(t2, i)) {
                        UnsafeUtil.putInt(t, j, UnsafeUtil.getInt(t2, j));
                        setFieldPresent(t, i);
                        break;
                    } else {
                        break;
                    }
                case QS.VERSION /* 13 */:
                    if (isFieldPresent(t2, i)) {
                        UnsafeUtil.putInt(t, j, UnsafeUtil.getInt(t2, j));
                        setFieldPresent(t, i);
                        break;
                    } else {
                        break;
                    }
                case 14:
                    if (isFieldPresent(t2, i)) {
                        UnsafeUtil.putLong(t, j, UnsafeUtil.getLong(t2, j));
                        setFieldPresent(t, i);
                        break;
                    } else {
                        break;
                    }
                case 15:
                    if (isFieldPresent(t2, i)) {
                        UnsafeUtil.putInt(t, j, UnsafeUtil.getInt(t2, j));
                        setFieldPresent(t, i);
                        break;
                    } else {
                        break;
                    }
                case 16:
                    if (isFieldPresent(t2, i)) {
                        UnsafeUtil.putLong(t, j, UnsafeUtil.getLong(t2, j));
                        setFieldPresent(t, i);
                        break;
                    } else {
                        break;
                    }
                case 17:
                    mergeMessage(t, t2, i);
                    break;
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case SwipeRefreshLayout.CIRCLE_DIAMETER /* 40 */:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                    this.listFieldSchema.mergeListsAt(t, t2, j);
                    break;
                case CustomEvent.MAX_STR_LENGTH /* 50 */:
                    MapFieldSchema mapFieldSchema = this.mapFieldSchema;
                    Class<?> cls = SchemaUtil.GENERATED_MESSAGE_CLASS;
                    UnsafeUtil.putObject(t, j, mapFieldSchema.mergeFrom(UnsafeUtil.getObject(t, j), UnsafeUtil.getObject(t2, j)));
                    break;
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case SwipeRefreshLayout.CIRCLE_DIAMETER_LARGE /* 56 */:
                case 57:
                case 58:
                case 59:
                    if (isOneofPresent(t2, i2, i)) {
                        UnsafeUtil.putObject(t, j, UnsafeUtil.getObject(t2, j));
                        setOneofPresent(t, i2, i);
                        break;
                    } else {
                        break;
                    }
                case 60:
                    mergeOneofMessage(t, t2, i);
                    break;
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                    if (isOneofPresent(t2, i2, i)) {
                        UnsafeUtil.putObject(t, j, UnsafeUtil.getObject(t2, j));
                        setOneofPresent(t, i2, i);
                        break;
                    } else {
                        break;
                    }
                case 68:
                    mergeOneofMessage(t, t2, i);
                    break;
            }
        }
        if (!this.proto3) {
            UnknownFieldSchema<?, ?> unknownFieldSchema = this.unknownFieldSchema;
            Class<?> cls2 = SchemaUtil.GENERATED_MESSAGE_CLASS;
            unknownFieldSchema.setToMessage(t, unknownFieldSchema.merge(unknownFieldSchema.getFromMessage(t), unknownFieldSchema.getFromMessage(t2)));
            if (this.hasExtensions) {
                SchemaUtil.mergeExtensions(this.extensionSchema, t, t2);
            }
        }
    }

    public final void mergeMessage(T t, T t2, int i) {
        long typeAndOffsetAt = typeAndOffsetAt(i) & 1048575;
        if (isFieldPresent(t2, i)) {
            Object object = UnsafeUtil.getObject(t, typeAndOffsetAt);
            Object object2 = UnsafeUtil.getObject(t2, typeAndOffsetAt);
            if (object != null && object2 != null) {
                UnsafeUtil.putObject(t, typeAndOffsetAt, Internal.mergeMessage(object, object2));
                setFieldPresent(t, i);
            } else if (object2 != null) {
                UnsafeUtil.putObject(t, typeAndOffsetAt, object2);
                setFieldPresent(t, i);
            }
        }
    }

    public final void mergeOneofMessage(T t, T t2, int i) {
        int typeAndOffsetAt = typeAndOffsetAt(i);
        int i2 = this.buffer[i];
        long j = typeAndOffsetAt & 1048575;
        if (isOneofPresent(t2, i2, i)) {
            Object object = UnsafeUtil.getObject(t, j);
            Object object2 = UnsafeUtil.getObject(t2, j);
            if (object != null && object2 != null) {
                UnsafeUtil.putObject(t, j, Internal.mergeMessage(object, object2));
                setOneofPresent(t, i2, i);
            } else if (object2 != null) {
                UnsafeUtil.putObject(t, j, object2);
                setOneofPresent(t, i2, i);
            }
        }
    }
}
