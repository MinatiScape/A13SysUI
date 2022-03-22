package org.tensorflow.lite;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class NativeInterpreterWrapper implements AutoCloseable {
    public long errorHandle;
    public Tensor[] inputTensors;
    public long interpreterHandle;
    public ByteBuffer modelByteBuffer;
    public long modelHandle;
    public Tensor[] outputTensors;
    public HashMap memoryAllocated = new HashMap();
    public final ArrayList delegates = new ArrayList();
    public final ArrayList ownedDelegates = new ArrayList();

    private static native long allocateTensors(long j, long j2, int i);

    private static native void applyDelegate(long j, long j2, long j3);

    private static native long createErrorReporter(int i);

    private static native long createInterpreter(long j, long j2, int i);

    private static native long createModelWithBuffer(ByteBuffer byteBuffer, long j);

    private static native void delete(long j, long j2, long j3);

    private static native long deleteCancellationFlag(long j);

    private static native int getInputCount(long j);

    private static native int getInputTensorIndex(long j, int i);

    private static native int getOutputCount(long j);

    private static native int getOutputTensorIndex(long j, int i);

    private static native String[] getSignatureKeys(long j);

    private static native boolean hasUnresolvedFlexOp(long j);

    private static native boolean resizeInput(long j, long j2, int i, int[] iArr, boolean z, int i2);

    private static native void run(long j, long j2);

    @Override // java.lang.AutoCloseable
    public final void close() {
        int i = 0;
        int i2 = 0;
        while (true) {
            Tensor[] tensorArr = this.inputTensors;
            if (i2 < tensorArr.length) {
                if (tensorArr[i2] != null) {
                    tensorArr[i2].close();
                    this.inputTensors[i2] = null;
                }
                i2++;
            }
        }
        while (true) {
            Tensor[] tensorArr2 = this.outputTensors;
            if (i >= tensorArr2.length) {
                break;
            }
            if (tensorArr2[i] != null) {
                tensorArr2[i].close();
                this.outputTensors[i] = null;
            }
            i++;
        }
        delete(this.errorHandle, this.modelHandle, this.interpreterHandle);
        deleteCancellationFlag(0L);
        this.errorHandle = 0L;
        this.modelHandle = 0L;
        this.interpreterHandle = 0L;
        this.modelByteBuffer = null;
        this.memoryAllocated = null;
        this.delegates.clear();
        Iterator it = this.ownedDelegates.iterator();
        while (it.hasNext()) {
            try {
                ((AutoCloseable) it.next()).close();
            } catch (Exception e) {
                System.err.println("Failed to close flex delegate: " + e);
            }
        }
        this.ownedDelegates.clear();
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x004b  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0050 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void run(java.lang.Object[] r11, java.util.HashMap r12) {
        /*
            Method dump skipped, instructions count: 301
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.tensorflow.lite.NativeInterpreterWrapper.run(java.lang.Object[], java.util.HashMap):void");
    }

    public final Tensor getInputTensor(int i) {
        if (i >= 0) {
            Tensor[] tensorArr = this.inputTensors;
            if (i < tensorArr.length) {
                Tensor tensor = tensorArr[i];
                if (tensor != null) {
                    return tensor;
                }
                long j = this.interpreterHandle;
                Tensor fromIndex = Tensor.fromIndex(j, getInputTensorIndex(j, i));
                tensorArr[i] = fromIndex;
                return fromIndex;
            }
        }
        throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("Invalid input Tensor index: ", i));
    }

    public final String[] getSignatureKeys() {
        return getSignatureKeys(this.interpreterHandle);
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0082, code lost:
        r2 = (org.tensorflow.lite.Delegate) r4.getConstructor(new java.lang.Class[0]).newInstance(new java.lang.Object[0]);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public NativeInterpreterWrapper(java.nio.MappedByteBuffer r13) {
        /*
            r12 = this;
            r12.<init>()
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            r12.memoryAllocated = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r12.delegates = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r12.ownedDelegates = r0
            org.tensorflow.lite.TensorFlowLite.init()
            if (r13 == 0) goto L_0x00ca
            r12.modelByteBuffer = r13
            r13 = 512(0x200, float:7.175E-43)
            long r0 = createErrorReporter(r13)
            java.nio.ByteBuffer r13 = r12.modelByteBuffer
            long r2 = createModelWithBuffer(r13, r0)
            java.util.ArrayList r13 = new java.util.ArrayList
            r13.<init>()
            r12.errorHandle = r0
            r12.modelHandle = r2
            r4 = -1
            long r2 = createInterpreter(r2, r0, r4)
            r12.interpreterHandle = r2
            int r2 = getInputCount(r2)
            org.tensorflow.lite.Tensor[] r2 = new org.tensorflow.lite.Tensor[r2]
            r12.inputTensors = r2
            long r2 = r12.interpreterHandle
            int r2 = getOutputCount(r2)
            org.tensorflow.lite.Tensor[] r2 = new org.tensorflow.lite.Tensor[r2]
            r12.outputTensors = r2
            long r2 = r12.interpreterHandle
            boolean r2 = hasUnresolvedFlexOp(r2)
            r3 = 0
            if (r2 == 0) goto L_0x0098
            r2 = 0
            java.lang.String r4 = "org.tensorflow.lite.flex.FlexDelegate"
            java.lang.Class r4 = java.lang.Class.forName(r4)     // Catch: Exception -> 0x0083
            java.util.Iterator r5 = r13.iterator()     // Catch: Exception -> 0x0083
        L_0x0061:
            boolean r6 = r5.hasNext()     // Catch: Exception -> 0x0083
            if (r6 == 0) goto L_0x0074
            java.lang.Object r6 = r5.next()     // Catch: Exception -> 0x0083
            org.tensorflow.lite.Delegate r6 = (org.tensorflow.lite.Delegate) r6     // Catch: Exception -> 0x0083
            boolean r6 = r4.isInstance(r6)     // Catch: Exception -> 0x0083
            if (r6 == 0) goto L_0x0061
            goto L_0x0083
        L_0x0074:
            java.lang.Class[] r5 = new java.lang.Class[r3]     // Catch: Exception -> 0x0083
            java.lang.reflect.Constructor r4 = r4.getConstructor(r5)     // Catch: Exception -> 0x0083
            java.lang.Object[] r5 = new java.lang.Object[r3]     // Catch: Exception -> 0x0083
            java.lang.Object r4 = r4.newInstance(r5)     // Catch: Exception -> 0x0083
            org.tensorflow.lite.Delegate r4 = (org.tensorflow.lite.Delegate) r4     // Catch: Exception -> 0x0083
            r2 = r4
        L_0x0083:
            if (r2 == 0) goto L_0x0098
            java.util.ArrayList r4 = r12.ownedDelegates
            r5 = r2
            java.lang.AutoCloseable r5 = (java.lang.AutoCloseable) r5
            r4.add(r5)
            long r6 = r12.interpreterHandle
            long r8 = r12.errorHandle
            long r10 = r2.getNativeHandle()
            applyDelegate(r6, r8, r10)
        L_0x0098:
            java.util.Iterator r13 = r13.iterator()
        L_0x009c:
            boolean r2 = r13.hasNext()
            if (r2 == 0) goto L_0x00b9
            java.lang.Object r2 = r13.next()
            org.tensorflow.lite.Delegate r2 = (org.tensorflow.lite.Delegate) r2
            long r4 = r12.interpreterHandle
            long r6 = r12.errorHandle
            long r8 = r2.getNativeHandle()
            applyDelegate(r4, r6, r8)
            java.util.ArrayList r4 = r12.delegates
            r4.add(r2)
            goto L_0x009c
        L_0x00b9:
            long r4 = r12.interpreterHandle
            allocateTensors(r4, r0, r3)
            java.util.HashMap r12 = r12.memoryAllocated
            java.lang.Integer r13 = java.lang.Integer.valueOf(r3)
            java.lang.Boolean r0 = java.lang.Boolean.TRUE
            r12.put(r13, r0)
            return
        L_0x00ca:
            java.lang.IllegalArgumentException r12 = new java.lang.IllegalArgumentException
            java.lang.String r13 = "Model ByteBuffer should be either a MappedByteBuffer of the model file, or a direct ByteBuffer using ByteOrder.nativeOrder() which contains bytes of model content."
            r12.<init>(r13)
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: org.tensorflow.lite.NativeInterpreterWrapper.<init>(java.nio.MappedByteBuffer):void");
    }
}
