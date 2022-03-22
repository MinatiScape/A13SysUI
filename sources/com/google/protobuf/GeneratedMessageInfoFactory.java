package com.google.protobuf;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.google.protobuf.GeneratedMessageLite;
/* loaded from: classes.dex */
public final class GeneratedMessageInfoFactory implements MessageInfoFactory {
    public static final GeneratedMessageInfoFactory instance = new GeneratedMessageInfoFactory();

    @Override // com.google.protobuf.MessageInfoFactory
    public final boolean isSupported(Class<?> cls) {
        return GeneratedMessageLite.class.isAssignableFrom(cls);
    }

    @Override // com.google.protobuf.MessageInfoFactory
    public final MessageInfo messageInfoFor(Class<?> cls) {
        if (GeneratedMessageLite.class.isAssignableFrom(cls)) {
            try {
                return (MessageInfo) GeneratedMessageLite.getDefaultInstance(cls.asSubclass(GeneratedMessageLite.class)).dynamicMethod(GeneratedMessageLite.MethodToInvoke.BUILD_MESSAGE_INFO);
            } catch (Exception e) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Unable to get message info for ");
                m.append(cls.getName());
                throw new RuntimeException(m.toString(), e);
            }
        } else {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Unsupported message type: ");
            m2.append(cls.getName());
            throw new IllegalArgumentException(m2.toString());
        }
    }
}
