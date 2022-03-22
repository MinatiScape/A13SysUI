package com.google.protobuf;

import com.google.protobuf.MessageLite;
/* loaded from: classes.dex */
public abstract class AbstractParser<MessageType extends MessageLite> implements Parser<MessageType> {
    static {
        ExtensionRegistryLite.getEmptyRegistry();
    }
}
