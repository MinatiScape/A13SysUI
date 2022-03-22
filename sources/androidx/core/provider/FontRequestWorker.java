package androidx.core.provider;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Process;
import androidx.collection.LruCache;
import androidx.collection.SimpleArrayMap;
import androidx.core.util.Consumer;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public final class FontRequestWorker {
    public static final ThreadPoolExecutor DEFAULT_EXECUTOR_SERVICE;
    public static final LruCache<String, Typeface> sTypefaceCache = new LruCache<>(16);
    public static final Object LOCK = new Object();
    public static final SimpleArrayMap<String, ArrayList<Consumer<TypefaceResult>>> PENDING_REPLIES = new SimpleArrayMap<>();

    static {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 1, 10000, TimeUnit.MILLISECONDS, new LinkedBlockingDeque(), new ThreadFactory() { // from class: androidx.core.provider.RequestExecutor$DefaultThreadFactory
            public String mThreadName = "fonts-androidx";
            public int mPriority = 10;

            /* loaded from: classes.dex */
            public static class ProcessPriorityThread extends Thread {
                public final int mPriority;

                @Override // java.lang.Thread, java.lang.Runnable
                public final void run() {
                    Process.setThreadPriority(this.mPriority);
                    super.run();
                }

                public ProcessPriorityThread(Runnable runnable, String str, int i) {
                    super(runnable, str);
                    this.mPriority = i;
                }
            }

            @Override // java.util.concurrent.ThreadFactory
            public final Thread newThread(Runnable runnable) {
                return new ProcessPriorityThread(runnable, this.mThreadName, this.mPriority);
            }
        });
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        DEFAULT_EXECUTOR_SERVICE = threadPoolExecutor;
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x0042  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0048  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static androidx.core.provider.FontRequestWorker.TypefaceResult getFontSync(java.lang.String r6, android.content.Context r7, androidx.core.provider.FontRequest r8, int r9) {
        /*
            androidx.collection.LruCache<java.lang.String, android.graphics.Typeface> r0 = androidx.core.provider.FontRequestWorker.sTypefaceCache
            java.lang.Object r0 = r0.get(r6)
            android.graphics.Typeface r0 = (android.graphics.Typeface) r0
            if (r0 == 0) goto L_0x0010
            androidx.core.provider.FontRequestWorker$TypefaceResult r6 = new androidx.core.provider.FontRequestWorker$TypefaceResult
            r6.<init>(r0)
            return r6
        L_0x0010:
            androidx.core.provider.FontsContractCompat$FontFamilyResult r8 = androidx.core.provider.FontProvider.getFontFamilyResult(r7, r8)     // Catch: NameNotFoundException -> 0x0061
            int r0 = r8.mStatusCode
            r1 = -3
            r2 = 1
            if (r0 == 0) goto L_0x001f
            if (r0 == r2) goto L_0x001d
            goto L_0x0037
        L_0x001d:
            r0 = -2
            goto L_0x0040
        L_0x001f:
            androidx.core.provider.FontsContractCompat$FontInfo[] r0 = r8.mFonts
            if (r0 == 0) goto L_0x003f
            int r3 = r0.length
            if (r3 != 0) goto L_0x0027
            goto L_0x003f
        L_0x0027:
            int r2 = r0.length
            r3 = 0
            r4 = r3
        L_0x002a:
            if (r4 >= r2) goto L_0x003e
            r5 = r0[r4]
            java.util.Objects.requireNonNull(r5)
            int r5 = r5.mResultCode
            if (r5 == 0) goto L_0x003b
            if (r5 >= 0) goto L_0x0039
        L_0x0037:
            r0 = r1
            goto L_0x0040
        L_0x0039:
            r0 = r5
            goto L_0x0040
        L_0x003b:
            int r4 = r4 + 1
            goto L_0x002a
        L_0x003e:
            r2 = r3
        L_0x003f:
            r0 = r2
        L_0x0040:
            if (r0 == 0) goto L_0x0048
            androidx.core.provider.FontRequestWorker$TypefaceResult r6 = new androidx.core.provider.FontRequestWorker$TypefaceResult
            r6.<init>(r0)
            return r6
        L_0x0048:
            androidx.core.provider.FontsContractCompat$FontInfo[] r8 = r8.mFonts
            android.graphics.Typeface r7 = androidx.core.graphics.TypefaceCompat.createFromFontInfo(r7, r8, r9)
            if (r7 == 0) goto L_0x005b
            androidx.collection.LruCache<java.lang.String, android.graphics.Typeface> r8 = androidx.core.provider.FontRequestWorker.sTypefaceCache
            r8.put(r6, r7)
            androidx.core.provider.FontRequestWorker$TypefaceResult r6 = new androidx.core.provider.FontRequestWorker$TypefaceResult
            r6.<init>(r7)
            return r6
        L_0x005b:
            androidx.core.provider.FontRequestWorker$TypefaceResult r6 = new androidx.core.provider.FontRequestWorker$TypefaceResult
            r6.<init>(r1)
            return r6
        L_0x0061:
            androidx.core.provider.FontRequestWorker$TypefaceResult r6 = new androidx.core.provider.FontRequestWorker$TypefaceResult
            r7 = -1
            r6.<init>(r7)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.provider.FontRequestWorker.getFontSync(java.lang.String, android.content.Context, androidx.core.provider.FontRequest, int):androidx.core.provider.FontRequestWorker$TypefaceResult");
    }

    /* loaded from: classes.dex */
    public static final class TypefaceResult {
        public final int mResult;
        public final Typeface mTypeface;

        public TypefaceResult(int i) {
            this.mTypeface = null;
            this.mResult = i;
        }

        @SuppressLint({"WrongConstant"})
        public TypefaceResult(Typeface typeface) {
            this.mTypeface = typeface;
            this.mResult = 0;
        }
    }
}
