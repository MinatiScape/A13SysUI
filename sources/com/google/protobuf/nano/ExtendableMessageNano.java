package com.google.protobuf.nano;

import com.google.protobuf.nano.ExtendableMessageNano;
import java.io.IOException;
import java.nio.charset.Charset;
/* loaded from: classes.dex */
public abstract class ExtendableMessageNano<M extends ExtendableMessageNano<M>> extends MessageNano {
    @Override // com.google.protobuf.nano.MessageNano
    /* renamed from: clone */
    public final MessageNano mo180clone() throws CloneNotSupportedException {
        ExtendableMessageNano extendableMessageNano = (ExtendableMessageNano) super.clone();
        Charset charset = InternalNano.UTF_8;
        return extendableMessageNano;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final int computeSerializedSize() {
        return 0;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
    }

    @Override // com.google.protobuf.nano.MessageNano
    /* renamed from: clone  reason: collision with other method in class */
    public final Object mo180clone() throws CloneNotSupportedException {
        ExtendableMessageNano extendableMessageNano = (ExtendableMessageNano) super.clone();
        Charset charset = InternalNano.UTF_8;
        return extendableMessageNano;
    }
}
