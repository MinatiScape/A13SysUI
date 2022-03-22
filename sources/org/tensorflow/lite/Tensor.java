package org.tensorflow.lite;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
/* loaded from: classes.dex */
public final class Tensor {
    public final DataType dtype;
    public long nativeHandle;
    public int[] shapeCopy;

    private static native ByteBuffer buffer(long j);

    public static int computeNumDimensions(Object obj) {
        if (obj == null || !obj.getClass().isArray()) {
            return 0;
        }
        if (Array.getLength(obj) != 0) {
            return computeNumDimensions(Array.get(obj, 0)) + 1;
        }
        throw new IllegalArgumentException("Array lengths cannot be 0.");
    }

    private static native long create(long j, int i, int i2);

    private static native void delete(long j);

    private static native int dtype(long j);

    public static void fillShape(Object obj, int i, int[] iArr) {
        if (i != iArr.length) {
            int length = Array.getLength(obj);
            if (iArr[i] == 0) {
                iArr[i] = length;
            } else if (iArr[i] != length) {
                throw new IllegalArgumentException(String.format("Mismatched lengths (%d and %d) in dimension %d", Integer.valueOf(iArr[i]), Integer.valueOf(length), Integer.valueOf(i)));
            }
            for (int i2 = 0; i2 < length; i2++) {
                fillShape(Array.get(obj, i2), i + 1, iArr);
            }
        }
    }

    private static native boolean hasDelegateBufferHandle(long j);

    private static native String name(long j);

    private static native int numBytes(long j);

    private static native float quantizationScale(long j);

    private static native int quantizationZeroPoint(long j);

    private static native void readMultiDimensionalArray(long j, Object obj);

    private static native int[] shape(long j);

    private static native int[] shapeSignature(long j);

    private static native void writeDirectBuffer(long j, Buffer buffer);

    private static native void writeMultiDimensionalArray(long j, Object obj);

    private static native void writeScalar(long j, Object obj);

    public static Tensor fromIndex(long j, int i) {
        return new Tensor(create(j, i, 0));
    }

    public final ByteBuffer buffer() {
        return buffer(this.nativeHandle).order(ByteOrder.nativeOrder());
    }

    public final void close() {
        delete(this.nativeHandle);
        this.nativeHandle = 0L;
    }

    public final Object copyTo(Object obj) {
        int i;
        if (obj != null) {
            throwIfTypeIsIncompatible(obj);
            boolean z = obj instanceof Buffer;
            if (z) {
                Buffer buffer = (Buffer) obj;
                int numBytes = numBytes(this.nativeHandle);
                if (obj instanceof ByteBuffer) {
                    i = buffer.capacity();
                } else {
                    i = buffer.capacity() * this.dtype.byteSize();
                }
                if (numBytes > i) {
                    throw new IllegalArgumentException(String.format("Cannot copy from a TensorFlowLite tensor (%s) with %d bytes to a Java Buffer with %d bytes.", name(this.nativeHandle), Integer.valueOf(numBytes), Integer.valueOf(i)));
                }
            } else {
                int[] computeShapeOf = computeShapeOf(obj);
                if (!Arrays.equals(computeShapeOf, this.shapeCopy)) {
                    throw new IllegalArgumentException(String.format("Cannot copy from a TensorFlowLite tensor (%s) with shape %s to a Java object with shape %s.", name(this.nativeHandle), Arrays.toString(this.shapeCopy), Arrays.toString(computeShapeOf)));
                }
            }
            if (z) {
                Buffer buffer2 = (Buffer) obj;
                if (buffer2 instanceof ByteBuffer) {
                    ((ByteBuffer) buffer2).put(buffer());
                } else if (buffer2 instanceof FloatBuffer) {
                    ((FloatBuffer) buffer2).put(buffer().asFloatBuffer());
                } else if (buffer2 instanceof LongBuffer) {
                    ((LongBuffer) buffer2).put(buffer().asLongBuffer());
                } else if (buffer2 instanceof IntBuffer) {
                    ((IntBuffer) buffer2).put(buffer().asIntBuffer());
                } else if (buffer2 instanceof ShortBuffer) {
                    ((ShortBuffer) buffer2).put(buffer().asShortBuffer());
                } else {
                    throw new IllegalArgumentException("Unexpected output buffer type: " + buffer2);
                }
            } else {
                readMultiDimensionalArray(this.nativeHandle, obj);
            }
            return obj;
        } else if (hasDelegateBufferHandle(this.nativeHandle)) {
            return obj;
        } else {
            throw new IllegalArgumentException("Null outputs are allowed only if the Tensor is bound to a buffer handle.");
        }
    }

    public final void refreshShape() {
        this.shapeCopy = shape(this.nativeHandle);
    }

    public final void setTo(Object obj) {
        int i;
        if (obj != null) {
            throwIfTypeIsIncompatible(obj);
            boolean z = obj instanceof Buffer;
            if (z) {
                Buffer buffer = (Buffer) obj;
                int numBytes = numBytes(this.nativeHandle);
                if (obj instanceof ByteBuffer) {
                    i = buffer.capacity();
                } else {
                    i = buffer.capacity() * this.dtype.byteSize();
                }
                if (numBytes != i) {
                    throw new IllegalArgumentException(String.format("Cannot copy to a TensorFlowLite tensor (%s) with %d bytes from a Java Buffer with %d bytes.", name(this.nativeHandle), Integer.valueOf(numBytes), Integer.valueOf(i)));
                }
            } else {
                int[] computeShapeOf = computeShapeOf(obj);
                if (!Arrays.equals(computeShapeOf, this.shapeCopy)) {
                    throw new IllegalArgumentException(String.format("Cannot copy to a TensorFlowLite tensor (%s) with shape %s from a Java object with shape %s.", name(this.nativeHandle), Arrays.toString(this.shapeCopy), Arrays.toString(computeShapeOf)));
                }
            }
            if (z) {
                Buffer buffer2 = (Buffer) obj;
                if (buffer2 instanceof ByteBuffer) {
                    ByteBuffer byteBuffer = (ByteBuffer) buffer2;
                    if (!byteBuffer.isDirect() || byteBuffer.order() != ByteOrder.nativeOrder()) {
                        buffer().put(byteBuffer);
                    } else {
                        writeDirectBuffer(this.nativeHandle, buffer2);
                    }
                } else if (buffer2 instanceof LongBuffer) {
                    LongBuffer longBuffer = (LongBuffer) buffer2;
                    if (!longBuffer.isDirect() || longBuffer.order() != ByteOrder.nativeOrder()) {
                        buffer().asLongBuffer().put(longBuffer);
                    } else {
                        writeDirectBuffer(this.nativeHandle, buffer2);
                    }
                } else if (buffer2 instanceof FloatBuffer) {
                    FloatBuffer floatBuffer = (FloatBuffer) buffer2;
                    if (!floatBuffer.isDirect() || floatBuffer.order() != ByteOrder.nativeOrder()) {
                        buffer().asFloatBuffer().put(floatBuffer);
                    } else {
                        writeDirectBuffer(this.nativeHandle, buffer2);
                    }
                } else if (buffer2 instanceof IntBuffer) {
                    IntBuffer intBuffer = (IntBuffer) buffer2;
                    if (!intBuffer.isDirect() || intBuffer.order() != ByteOrder.nativeOrder()) {
                        buffer().asIntBuffer().put(intBuffer);
                    } else {
                        writeDirectBuffer(this.nativeHandle, buffer2);
                    }
                } else if (buffer2 instanceof ShortBuffer) {
                    ShortBuffer shortBuffer = (ShortBuffer) buffer2;
                    if (!shortBuffer.isDirect() || shortBuffer.order() != ByteOrder.nativeOrder()) {
                        buffer().asShortBuffer().put(shortBuffer);
                    } else {
                        writeDirectBuffer(this.nativeHandle, buffer2);
                    }
                } else {
                    throw new IllegalArgumentException("Unexpected input buffer type: " + buffer2);
                }
            } else if (this.dtype == DataType.STRING && this.shapeCopy.length == 0) {
                writeScalar(this.nativeHandle, obj);
            } else if (obj.getClass().isArray()) {
                writeMultiDimensionalArray(this.nativeHandle, obj);
            } else {
                writeScalar(this.nativeHandle, obj);
            }
        } else if (!hasDelegateBufferHandle(this.nativeHandle)) {
            throw new IllegalArgumentException("Null inputs are allowed only if the Tensor is bound to a buffer handle.");
        }
    }

    public final void throwIfTypeIsIncompatible(Object obj) {
        DataType dataType;
        if (!(obj instanceof ByteBuffer)) {
            if (obj != null) {
                Class<?> cls = obj.getClass();
                if (cls.isArray()) {
                    while (cls.isArray()) {
                        cls = cls.getComponentType();
                    }
                    if (Float.TYPE.equals(cls)) {
                        dataType = DataType.FLOAT32;
                    } else if (Integer.TYPE.equals(cls)) {
                        dataType = DataType.INT32;
                    } else if (Short.TYPE.equals(cls)) {
                        dataType = DataType.INT16;
                    } else if (Byte.TYPE.equals(cls)) {
                        DataType dataType2 = this.dtype;
                        DataType dataType3 = DataType.STRING;
                        if (dataType2 == dataType3) {
                            dataType = dataType3;
                        } else {
                            dataType = DataType.UINT8;
                        }
                    } else if (Long.TYPE.equals(cls)) {
                        dataType = DataType.INT64;
                    } else if (Boolean.TYPE.equals(cls)) {
                        dataType = DataType.BOOL;
                    } else if (String.class.equals(cls)) {
                        dataType = DataType.STRING;
                    }
                    if (dataType != this.dtype && !dataType.toStringName().equals(this.dtype.toStringName())) {
                        throw new IllegalArgumentException(String.format("Cannot convert between a TensorFlowLite tensor with type %s and a Java object of type %s (which is compatible with the TensorFlowLite type %s).", this.dtype, obj.getClass().getName(), dataType));
                    }
                    return;
                }
                if (Float.class.equals(cls) || (obj instanceof FloatBuffer)) {
                    dataType = DataType.FLOAT32;
                } else if (Integer.class.equals(cls) || (obj instanceof IntBuffer)) {
                    dataType = DataType.INT32;
                } else if (Short.class.equals(cls) || (obj instanceof ShortBuffer)) {
                    dataType = DataType.INT16;
                } else if (Byte.class.equals(cls)) {
                    dataType = DataType.UINT8;
                } else if (Long.class.equals(cls) || (obj instanceof LongBuffer)) {
                    dataType = DataType.INT64;
                } else if (Boolean.class.equals(cls)) {
                    dataType = DataType.BOOL;
                } else if (String.class.equals(cls)) {
                    dataType = DataType.STRING;
                }
                if (dataType != this.dtype) {
                    return;
                }
                return;
            }
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("DataType error: cannot resolve DataType of ");
            m.append(obj.getClass().getName());
            throw new IllegalArgumentException(m.toString());
        }
    }

    public Tensor(long j) {
        this.nativeHandle = j;
        this.dtype = DataType.fromC(dtype(j));
        this.shapeCopy = shape(j);
        shapeSignature(j);
        quantizationScale(j);
        quantizationZeroPoint(j);
    }

    public final int[] computeShapeOf(Object obj) {
        int computeNumDimensions = computeNumDimensions(obj);
        if (this.dtype == DataType.STRING) {
            Class<?> cls = obj.getClass();
            if (cls.isArray()) {
                while (cls.isArray()) {
                    cls = cls.getComponentType();
                }
                if (Byte.TYPE.equals(cls)) {
                    computeNumDimensions--;
                }
            }
        }
        int[] iArr = new int[computeNumDimensions];
        fillShape(obj, 0, iArr);
        return iArr;
    }
}
