package org.tensorflow.lite;
/* loaded from: classes.dex */
public final class TensorFlowLite {
    public static final UnsatisfiedLinkError LOAD_LIBRARY_EXCEPTION;
    public static volatile boolean isInit = false;

    private static native String nativeRuntimeVersion();

    public static void init() {
        if (!isInit) {
            try {
                nativeRuntimeVersion();
                isInit = true;
            } catch (UnsatisfiedLinkError e) {
                e = e;
                UnsatisfiedLinkError unsatisfiedLinkError = LOAD_LIBRARY_EXCEPTION;
                if (unsatisfiedLinkError != null) {
                    e = unsatisfiedLinkError;
                }
                throw new UnsatisfiedLinkError("Failed to load native TensorFlow Lite methods. Check that the correct native libraries are present, and, if using a custom native library, have been properly loaded via System.loadLibrary():\n  " + e);
            }
        }
    }

    static {
        UnsatisfiedLinkError e;
        try {
            System.loadLibrary("tensorflowlite_jni");
            e = null;
        } catch (UnsatisfiedLinkError e2) {
            e = e2;
        }
        LOAD_LIBRARY_EXCEPTION = e;
    }

    public static String runtimeVersion() {
        init();
        return nativeRuntimeVersion();
    }
}
