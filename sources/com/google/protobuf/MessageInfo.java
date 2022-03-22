package com.google.protobuf;
/* loaded from: classes.dex */
public interface MessageInfo {
    MessageLite getDefaultInstance();

    ProtoSyntax getSyntax();

    boolean isMessageSetWireFormat();
}
