package com.google.protobuf;
/* loaded from: classes.dex */
public class LazyFieldLite {
    public volatile ByteString memoizedBytes;
    public volatile MessageLite value;

    public final MessageLite getValue(MessageLite messageLite) {
        if (this.value == null) {
            synchronized (this) {
                if (this.value == null) {
                    try {
                        this.value = messageLite;
                        this.memoizedBytes = ByteString.EMPTY;
                    } catch (InvalidProtocolBufferException unused) {
                        this.value = messageLite;
                        this.memoizedBytes = ByteString.EMPTY;
                    }
                }
            }
        }
        return this.value;
    }

    static {
        ExtensionRegistryLite.getEmptyRegistry();
    }
}
