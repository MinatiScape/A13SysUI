package com.google.protobuf;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import java.nio.charset.Charset;
/* loaded from: classes.dex */
public final class ManifestSchemaFactory {
    public static final AnonymousClass1 EMPTY_FACTORY = new MessageInfoFactory() { // from class: com.google.protobuf.ManifestSchemaFactory.1
        @Override // com.google.protobuf.MessageInfoFactory
        public final boolean isSupported(Class<?> cls) {
            return false;
        }

        @Override // com.google.protobuf.MessageInfoFactory
        public final MessageInfo messageInfoFor(Class<?> cls) {
            throw new IllegalStateException("This should never be called.");
        }
    };
    public final MessageInfoFactory messageInfoFactory;

    /* loaded from: classes.dex */
    public static class CompositeMessageInfoFactory implements MessageInfoFactory {
        public MessageInfoFactory[] factories;

        @Override // com.google.protobuf.MessageInfoFactory
        public final boolean isSupported(Class<?> cls) {
            for (MessageInfoFactory messageInfoFactory : this.factories) {
                if (messageInfoFactory.isSupported(cls)) {
                    return true;
                }
            }
            return false;
        }

        @Override // com.google.protobuf.MessageInfoFactory
        public final MessageInfo messageInfoFor(Class<?> cls) {
            MessageInfoFactory[] messageInfoFactoryArr;
            for (MessageInfoFactory messageInfoFactory : this.factories) {
                if (messageInfoFactory.isSupported(cls)) {
                    return messageInfoFactory.messageInfoFor(cls);
                }
            }
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("No factory is available for message type: ");
            m.append(cls.getName());
            throw new UnsupportedOperationException(m.toString());
        }

        public CompositeMessageInfoFactory(MessageInfoFactory... messageInfoFactoryArr) {
            this.factories = messageInfoFactoryArr;
        }
    }

    public ManifestSchemaFactory() {
        MessageInfoFactory messageInfoFactory;
        MessageInfoFactory[] messageInfoFactoryArr = new MessageInfoFactory[2];
        messageInfoFactoryArr[0] = GeneratedMessageInfoFactory.instance;
        try {
            messageInfoFactory = (MessageInfoFactory) Class.forName("com.google.protobuf.DescriptorMessageInfoFactory").getDeclaredMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
        } catch (Exception unused) {
            messageInfoFactory = EMPTY_FACTORY;
        }
        messageInfoFactoryArr[1] = messageInfoFactory;
        CompositeMessageInfoFactory compositeMessageInfoFactory = new CompositeMessageInfoFactory(messageInfoFactoryArr);
        Charset charset = Internal.UTF_8;
        this.messageInfoFactory = compositeMessageInfoFactory;
    }
}
