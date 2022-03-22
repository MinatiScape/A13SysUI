package com.google.protobuf;

import android.hardware.google.pixel.vendor.PixelAtoms$ReverseDomainNames;
import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.FieldSet;
import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.GeneratedMessageLite.Builder;
import com.google.protobuf.MessageLite;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: classes.dex */
public abstract class GeneratedMessageLite<MessageType extends GeneratedMessageLite<MessageType, BuilderType>, BuilderType extends Builder<MessageType, BuilderType>> extends AbstractMessageLite<MessageType, BuilderType> {
    private static Map<Object, GeneratedMessageLite<?, ?>> defaultInstanceMap = new ConcurrentHashMap();
    public UnknownFieldSetLite unknownFields = UnknownFieldSetLite.DEFAULT_INSTANCE;
    public int memoizedSerializedSize = -1;

    /* loaded from: classes.dex */
    public static abstract class Builder<MessageType extends GeneratedMessageLite<MessageType, BuilderType>, BuilderType extends Builder<MessageType, BuilderType>> extends AbstractMessageLite.Builder<MessageType, BuilderType> {
        public final MessageType defaultInstance;
        public MessageType instance;
        public boolean isBuilt = false;

        public final MessageType buildPartial() {
            if (this.isBuilt) {
                return this.instance;
            }
            MessageType messagetype = this.instance;
            Objects.requireNonNull(messagetype);
            Protobuf protobuf = Protobuf.INSTANCE;
            Objects.requireNonNull(protobuf);
            protobuf.schemaFor(messagetype.getClass()).makeImmutable(messagetype);
            this.isBuilt = true;
            return this.instance;
        }

        public final Object clone() throws CloneNotSupportedException {
            MessageType messagetype = this.defaultInstance;
            Objects.requireNonNull(messagetype);
            Builder builder = (Builder) messagetype.dynamicMethod(MethodToInvoke.NEW_BUILDER);
            builder.mergeFrom((Builder) buildPartial());
            return builder;
        }

        public final BuilderType mergeFrom(MessageType messagetype) {
            if (this.isBuilt) {
                MessageType messagetype2 = this.instance;
                MethodToInvoke methodToInvoke = MethodToInvoke.NEW_MUTABLE_INSTANCE;
                Objects.requireNonNull(messagetype2);
                MessageType messagetype3 = (MessageType) ((GeneratedMessageLite) messagetype2.dynamicMethod(methodToInvoke));
                MessageType messagetype4 = this.instance;
                Protobuf protobuf = Protobuf.INSTANCE;
                Objects.requireNonNull(protobuf);
                protobuf.schemaFor(messagetype3.getClass()).mergeFrom(messagetype3, messagetype4);
                this.instance = messagetype3;
                this.isBuilt = false;
            }
            MessageType messagetype5 = this.instance;
            Protobuf protobuf2 = Protobuf.INSTANCE;
            Objects.requireNonNull(protobuf2);
            protobuf2.schemaFor(messagetype5.getClass()).mergeFrom(messagetype5, messagetype);
            return this;
        }

        public Builder(PixelAtoms$ReverseDomainNames pixelAtoms$ReverseDomainNames) {
            this.defaultInstance = pixelAtoms$ReverseDomainNames;
            MethodToInvoke methodToInvoke = MethodToInvoke.NEW_MUTABLE_INSTANCE;
            Objects.requireNonNull(pixelAtoms$ReverseDomainNames);
            this.instance = (MessageType) ((GeneratedMessageLite) pixelAtoms$ReverseDomainNames.dynamicMethod(methodToInvoke));
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder
        public final GeneratedMessageLite getDefaultInstanceForType$1() {
            return this.defaultInstance;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class ExtendableMessage<MessageType extends ExtendableMessage<MessageType, BuilderType>, BuilderType> extends GeneratedMessageLite<MessageType, BuilderType> implements MessageLiteOrBuilder {
        public FieldSet<ExtensionDescriptor> extensions = FieldSet.DEFAULT_INSTANCE;
    }

    /* loaded from: classes.dex */
    public static final class ExtensionDescriptor implements FieldSet.FieldDescriptorLite<ExtensionDescriptor> {
        @Override // com.google.protobuf.FieldSet.FieldDescriptorLite
        public final void getLiteType() {
        }

        @Override // com.google.protobuf.FieldSet.FieldDescriptorLite
        public final void isRepeated() {
        }

        @Override // java.lang.Comparable
        public final int compareTo(Object obj) {
            Objects.requireNonNull((ExtensionDescriptor) obj);
            return 0;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.protobuf.FieldSet.FieldDescriptorLite
        public final Builder internalMergeFrom(MessageLite.Builder builder, MessageLite messageLite) {
            Builder builder2 = (Builder) builder;
            builder2.mergeFrom((Builder) ((GeneratedMessageLite) messageLite));
            return builder2;
        }

        @Override // com.google.protobuf.FieldSet.FieldDescriptorLite
        public final WireFormat$JavaType getLiteJavaType() {
            throw null;
        }
    }

    /* loaded from: classes.dex */
    public enum MethodToInvoke {
        GET_MEMOIZED_IS_INITIALIZED,
        SET_MEMOIZED_IS_INITIALIZED,
        BUILD_MESSAGE_INFO,
        NEW_MUTABLE_INSTANCE,
        NEW_BUILDER,
        GET_DEFAULT_INSTANCE,
        GET_PARSER
    }

    public abstract Object dynamicMethod(MethodToInvoke methodToInvoke);

    /* loaded from: classes.dex */
    public static class DefaultInstanceBasedParser<T extends GeneratedMessageLite<T, ?>> extends AbstractParser<T> {
        public final T defaultInstance;

        public DefaultInstanceBasedParser(PixelAtoms$ReverseDomainNames pixelAtoms$ReverseDomainNames) {
            this.defaultInstance = pixelAtoms$ReverseDomainNames;
        }
    }

    public static <T extends GeneratedMessageLite<?, ?>> T getDefaultInstance(Class<T> cls) {
        GeneratedMessageLite<?, ?> generatedMessageLite = defaultInstanceMap.get(cls);
        if (generatedMessageLite == null) {
            try {
                Class.forName(cls.getName(), true, cls.getClassLoader());
                generatedMessageLite = defaultInstanceMap.get(cls);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Class initialization cannot fail.", e);
            }
        }
        if (generatedMessageLite == null) {
            GeneratedMessageLite generatedMessageLite2 = (GeneratedMessageLite) UnsafeUtil.allocateInstance(cls);
            Objects.requireNonNull(generatedMessageLite2);
            generatedMessageLite = (T) ((GeneratedMessageLite) generatedMessageLite2.dynamicMethod(MethodToInvoke.GET_DEFAULT_INSTANCE));
            if (generatedMessageLite != null) {
                defaultInstanceMap.put(cls, generatedMessageLite);
            } else {
                throw new IllegalStateException();
            }
        }
        return (T) generatedMessageLite;
    }

    public static void registerDefaultInstance(PixelAtoms$ReverseDomainNames pixelAtoms$ReverseDomainNames) {
        defaultInstanceMap.put(PixelAtoms$ReverseDomainNames.class, pixelAtoms$ReverseDomainNames);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!((GeneratedMessageLite) dynamicMethod(MethodToInvoke.GET_DEFAULT_INSTANCE)).getClass().isInstance(obj)) {
            return false;
        }
        Protobuf protobuf = Protobuf.INSTANCE;
        Objects.requireNonNull(protobuf);
        return protobuf.schemaFor(getClass()).equals(this, (GeneratedMessageLite) obj);
    }

    @Override // com.google.protobuf.MessageLiteOrBuilder
    public final GeneratedMessageLite getDefaultInstanceForType$1() {
        return (GeneratedMessageLite) dynamicMethod(MethodToInvoke.GET_DEFAULT_INSTANCE);
    }

    public final int hashCode() {
        int i = this.memoizedHashCode;
        if (i != 0) {
            return i;
        }
        Protobuf protobuf = Protobuf.INSTANCE;
        Objects.requireNonNull(protobuf);
        int hashCode = protobuf.schemaFor(getClass()).hashCode(this);
        this.memoizedHashCode = hashCode;
        return hashCode;
    }

    @Override // com.google.protobuf.MessageLiteOrBuilder
    public final boolean isInitialized() {
        byte byteValue = ((Byte) dynamicMethod(MethodToInvoke.GET_MEMOIZED_IS_INITIALIZED)).byteValue();
        if (byteValue == 1) {
            return true;
        }
        if (byteValue == 0) {
            return false;
        }
        Protobuf protobuf = Protobuf.INSTANCE;
        Objects.requireNonNull(protobuf);
        boolean isInitialized = protobuf.schemaFor(getClass()).isInitialized(this);
        dynamicMethod(MethodToInvoke.SET_MEMOIZED_IS_INITIALIZED);
        return isInitialized;
    }

    @Override // com.google.protobuf.MessageLite
    public final Builder toBuilder$1() {
        Builder builder = (Builder) dynamicMethod(MethodToInvoke.NEW_BUILDER);
        builder.mergeFrom((Builder) this);
        return builder;
    }

    public static Object invokeOrDie(Method method, Object obj, Object... objArr) {
        try {
            return method.invoke(obj, objArr);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Couldn't use Java reflection to implement protocol message reflection.", e);
        } catch (InvocationTargetException e2) {
            Throwable cause = e2.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            } else if (cause instanceof Error) {
                throw ((Error) cause);
            } else {
                throw new RuntimeException("Unexpected exception thrown by generated accessor method.", cause);
            }
        }
    }

    public final String toString() {
        String obj = super.toString();
        StringBuilder sb = new StringBuilder();
        sb.append("# ");
        sb.append(obj);
        MessageLiteToString.reflectivePrintWithIndent(this, sb, 0);
        return sb.toString();
    }
}
