package com.google.protobuf;

import com.google.protobuf.ListFieldSchema;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: classes.dex */
public final class Protobuf {
    public static final Protobuf INSTANCE = new Protobuf();
    public final ConcurrentHashMap schemaCache = new ConcurrentHashMap();
    public final ManifestSchemaFactory schemaFactory = new ManifestSchemaFactory();

    public final <T> Schema<T> schemaFor(Class<T> cls) {
        MessageSetSchema messageSetSchema;
        MessageSchema messageSchema;
        MessageSetSchema messageSetSchema2;
        Class<?> cls2;
        ProtoSyntax protoSyntax = ProtoSyntax.PROTO2;
        Charset charset = Internal.UTF_8;
        Objects.requireNonNull(cls, "messageType");
        Schema<T> schema = (Schema) this.schemaCache.get(cls);
        if (schema != null) {
            return schema;
        }
        ManifestSchemaFactory manifestSchemaFactory = this.schemaFactory;
        Objects.requireNonNull(manifestSchemaFactory);
        Class<?> cls3 = SchemaUtil.GENERATED_MESSAGE_CLASS;
        if (GeneratedMessageLite.class.isAssignableFrom(cls) || (cls2 = SchemaUtil.GENERATED_MESSAGE_CLASS) == null || cls2.isAssignableFrom(cls)) {
            MessageInfo messageInfoFor = manifestSchemaFactory.messageInfoFactory.messageInfoFor(cls);
            if (messageInfoFor.isMessageSetWireFormat()) {
                if (GeneratedMessageLite.class.isAssignableFrom(cls)) {
                    messageSetSchema2 = new MessageSetSchema(SchemaUtil.UNKNOWN_FIELD_SET_LITE_SCHEMA, ExtensionSchemas.LITE_SCHEMA, messageInfoFor.getDefaultInstance());
                } else {
                    UnknownFieldSchema<?, ?> unknownFieldSchema = SchemaUtil.PROTO2_UNKNOWN_FIELD_SET_SCHEMA;
                    ExtensionSchema<?> extensionSchema = ExtensionSchemas.FULL_SCHEMA;
                    if (extensionSchema != null) {
                        messageSetSchema2 = new MessageSetSchema(unknownFieldSchema, extensionSchema, messageInfoFor.getDefaultInstance());
                    } else {
                        throw new IllegalStateException("Protobuf runtime is not correctly loaded.");
                    }
                }
                messageSetSchema = messageSetSchema2;
            } else {
                boolean z = true;
                if (GeneratedMessageLite.class.isAssignableFrom(cls)) {
                    if (messageInfoFor.getSyntax() != protoSyntax) {
                        z = false;
                    }
                    if (z) {
                        messageSchema = MessageSchema.newSchema(messageInfoFor, NewInstanceSchemas.LITE_SCHEMA, ListFieldSchema.LITE_INSTANCE, SchemaUtil.UNKNOWN_FIELD_SET_LITE_SCHEMA, ExtensionSchemas.LITE_SCHEMA, MapFieldSchemas.LITE_SCHEMA);
                    } else {
                        messageSchema = MessageSchema.newSchema(messageInfoFor, NewInstanceSchemas.LITE_SCHEMA, ListFieldSchema.LITE_INSTANCE, SchemaUtil.UNKNOWN_FIELD_SET_LITE_SCHEMA, null, MapFieldSchemas.LITE_SCHEMA);
                    }
                } else {
                    if (messageInfoFor.getSyntax() != protoSyntax) {
                        z = false;
                    }
                    if (z) {
                        NewInstanceSchema newInstanceSchema = NewInstanceSchemas.FULL_SCHEMA;
                        ListFieldSchema.ListFieldSchemaFull listFieldSchemaFull = ListFieldSchema.FULL_INSTANCE;
                        UnknownFieldSchema<?, ?> unknownFieldSchema2 = SchemaUtil.PROTO2_UNKNOWN_FIELD_SET_SCHEMA;
                        ExtensionSchema<?> extensionSchema2 = ExtensionSchemas.FULL_SCHEMA;
                        if (extensionSchema2 != null) {
                            messageSchema = MessageSchema.newSchema(messageInfoFor, newInstanceSchema, listFieldSchemaFull, unknownFieldSchema2, extensionSchema2, MapFieldSchemas.FULL_SCHEMA);
                        } else {
                            throw new IllegalStateException("Protobuf runtime is not correctly loaded.");
                        }
                    } else {
                        messageSchema = MessageSchema.newSchema(messageInfoFor, NewInstanceSchemas.FULL_SCHEMA, ListFieldSchema.FULL_INSTANCE, SchemaUtil.PROTO3_UNKNOWN_FIELD_SET_SCHEMA, null, MapFieldSchemas.FULL_SCHEMA);
                    }
                }
                messageSetSchema = messageSchema;
            }
            Schema<T> schema2 = (Schema) this.schemaCache.putIfAbsent(cls, messageSetSchema);
            if (schema2 != null) {
                return schema2;
            }
            return messageSetSchema;
        }
        throw new IllegalArgumentException("Message classes must extend GeneratedMessage or GeneratedMessageLite");
    }
}
