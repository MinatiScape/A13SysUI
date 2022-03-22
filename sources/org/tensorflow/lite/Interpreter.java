package org.tensorflow.lite;

import android.support.annotation.NonNull;
import java.nio.MappedByteBuffer;
/* loaded from: classes.dex */
public final class Interpreter extends InterpreterImpl {
    public Interpreter(@NonNull MappedByteBuffer mappedByteBuffer) {
        super(new NativeInterpreterWrapperExperimental(mappedByteBuffer));
        NativeInterpreterWrapper nativeInterpreterWrapper = this.wrapper;
        if (nativeInterpreterWrapper != null) {
            nativeInterpreterWrapper.getSignatureKeys();
            return;
        }
        throw new IllegalStateException("Internal error: The Interpreter has already been closed.");
    }
}
