package com.android.framework.protobuf.nano;

import com.android.framework.protobuf.nano.ExtendableMessageNano;
import java.io.IOException;
/* loaded from: classes.dex */
public abstract class ExtendableMessageNano<M extends ExtendableMessageNano<M>> extends MessageNano {
    @Override // com.android.framework.protobuf.nano.MessageNano
    /* renamed from: clone */
    public final MessageNano mo16clone() throws CloneNotSupportedException {
        ExtendableMessageNano extendableMessageNano = (ExtendableMessageNano) super.clone();
        int i = InternalNano.$r8$clinit;
        return extendableMessageNano;
    }

    @Override // com.android.framework.protobuf.nano.MessageNano
    public final void computeSerializedSize() {
    }

    @Override // com.android.framework.protobuf.nano.MessageNano
    public final void writeTo() throws IOException {
    }

    @Override // com.android.framework.protobuf.nano.MessageNano
    /* renamed from: clone  reason: collision with other method in class */
    public final Object mo16clone() throws CloneNotSupportedException {
        ExtendableMessageNano extendableMessageNano = (ExtendableMessageNano) super.clone();
        int i = InternalNano.$r8$clinit;
        return extendableMessageNano;
    }
}
