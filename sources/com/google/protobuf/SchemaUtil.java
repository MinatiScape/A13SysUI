package com.google.protobuf;

import com.google.protobuf.FieldSet;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SchemaUtil {
    public static final Class<?> GENERATED_MESSAGE_CLASS;
    public static final UnknownFieldSchema<?, ?> PROTO2_UNKNOWN_FIELD_SET_SCHEMA;
    public static final UnknownFieldSchema<?, ?> PROTO3_UNKNOWN_FIELD_SET_SCHEMA;
    public static final UnknownFieldSetLiteSchema UNKNOWN_FIELD_SET_LITE_SCHEMA;

    public static UnknownFieldSchema<?, ?> getUnknownFieldSetSchema(boolean z) {
        Class<?> cls;
        try {
            cls = Class.forName("com.google.protobuf.UnknownFieldSetSchema");
        } catch (Throwable unused) {
            cls = null;
        }
        if (cls == null) {
            return null;
        }
        try {
            return (UnknownFieldSchema) cls.getConstructor(Boolean.TYPE).newInstance(Boolean.valueOf(z));
        } catch (Throwable unused2) {
            return null;
        }
    }

    static {
        Class<?> cls;
        try {
            cls = Class.forName("com.google.protobuf.GeneratedMessageV3");
        } catch (Throwable unused) {
            cls = null;
        }
        GENERATED_MESSAGE_CLASS = cls;
        PROTO2_UNKNOWN_FIELD_SET_SCHEMA = getUnknownFieldSetSchema(false);
        PROTO3_UNKNOWN_FIELD_SET_SCHEMA = getUnknownFieldSetSchema(true);
        UNKNOWN_FIELD_SET_LITE_SCHEMA = new UnknownFieldSetLiteSchema();
    }

    public static boolean safeEquals(Object obj, Object obj2) {
        if (obj == obj2 || (obj != null && obj.equals(obj2))) {
            return true;
        }
        return false;
    }

    public static <T, FT extends FieldSet.FieldDescriptorLite<FT>> void mergeExtensions(ExtensionSchema<FT> extensionSchema, T t, T t2) {
        FieldSet<FT> extensions = extensionSchema.getExtensions(t2);
        Objects.requireNonNull(extensions);
        if (!extensions.fields.isEmpty()) {
            FieldSet<FT> mutableExtensions = extensionSchema.getMutableExtensions(t);
            Objects.requireNonNull(mutableExtensions);
            for (int i = 0; i < extensions.fields.getNumArrayEntries(); i++) {
                mutableExtensions.mergeFromField(extensions.fields.getArrayEntryAt(i));
            }
            for (Map.Entry<Object, Object> entry : extensions.fields.getOverflowEntries()) {
                mutableExtensions.mergeFromField(entry);
            }
        }
    }
}
