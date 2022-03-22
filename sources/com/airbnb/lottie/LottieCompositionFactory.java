package com.airbnb.lottie;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PathMeasure;
import com.airbnb.lottie.model.LottieCompositionCache;
import com.airbnb.lottie.parser.LottieCompositionMoshiParser;
import com.airbnb.lottie.parser.moshi.JsonReader;
import com.airbnb.lottie.parser.moshi.JsonUtf8Reader;
import com.airbnb.lottie.utils.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import okio.InputStreamSource;
import okio.Okio__JvmOkioKt;
import okio.RealBufferedSource;
import okio.Timeout;
/* loaded from: classes.dex */
public final class LottieCompositionFactory {
    public static final HashMap taskCache = new HashMap();

    public static LottieTask<LottieComposition> cache(final String str, Callable<LottieResult<LottieComposition>> callable) {
        final LottieComposition lottieComposition;
        if (str == null) {
            lottieComposition = null;
        } else {
            lottieComposition = LottieCompositionCache.INSTANCE.cache.get(str);
        }
        if (lottieComposition != null) {
            return new LottieTask<>(new Callable<LottieResult<LottieComposition>>() { // from class: com.airbnb.lottie.LottieCompositionFactory.9
                @Override // java.util.concurrent.Callable
                public final LottieResult<LottieComposition> call() throws Exception {
                    return new LottieResult<>(LottieComposition.this);
                }
            });
        }
        if (str != null) {
            HashMap hashMap = taskCache;
            if (hashMap.containsKey(str)) {
                return (LottieTask) hashMap.get(str);
            }
        }
        LottieTask<LottieComposition> lottieTask = new LottieTask<>(callable);
        if (str != null) {
            lottieTask.addListener(new LottieListener<LottieComposition>() { // from class: com.airbnb.lottie.LottieCompositionFactory.10
                @Override // com.airbnb.lottie.LottieListener
                public final void onResult(LottieComposition lottieComposition2) {
                    LottieCompositionFactory.taskCache.remove(str);
                }
            });
            lottieTask.addFailureListener(new LottieListener<Throwable>() { // from class: com.airbnb.lottie.LottieCompositionFactory.11
                @Override // com.airbnb.lottie.LottieListener
                public final void onResult(Throwable th) {
                    LottieCompositionFactory.taskCache.remove(str);
                }
            });
            taskCache.put(str, lottieTask);
        }
        return lottieTask;
    }

    public static LottieResult<LottieComposition> fromJsonInputStreamSync(InputStream inputStream, String str) {
        try {
            int i = Okio__JvmOkioKt.$r8$clinit;
            RealBufferedSource realBufferedSource = new RealBufferedSource(new InputStreamSource(inputStream, new Timeout()));
            String[] strArr = JsonReader.REPLACEMENT_CHARS;
            return fromJsonReaderSyncInternal(new JsonUtf8Reader(realBufferedSource), str, true);
        } finally {
            Utils.closeQuietly(inputStream);
        }
    }

    public static LottieResult<LottieComposition> fromZipStreamSyncInternal(ZipInputStream zipInputStream, String str) {
        LottieImageAsset lottieImageAsset;
        String[] split;
        HashMap hashMap = new HashMap();
        try {
            ZipEntry nextEntry = zipInputStream.getNextEntry();
            LottieComposition lottieComposition = null;
            while (nextEntry != null) {
                String name = nextEntry.getName();
                if (name.contains("__MACOSX")) {
                    zipInputStream.closeEntry();
                } else if (nextEntry.getName().contains(".json")) {
                    int i = Okio__JvmOkioKt.$r8$clinit;
                    RealBufferedSource realBufferedSource = new RealBufferedSource(new InputStreamSource(zipInputStream, new Timeout()));
                    String[] strArr = JsonReader.REPLACEMENT_CHARS;
                    lottieComposition = (LottieComposition) fromJsonReaderSyncInternal(new JsonUtf8Reader(realBufferedSource), null, false).value;
                } else {
                    if (!name.contains(".png") && !name.contains(".webp")) {
                        zipInputStream.closeEntry();
                    }
                    hashMap.put(name.split("/")[split.length - 1], BitmapFactory.decodeStream(zipInputStream));
                }
                nextEntry = zipInputStream.getNextEntry();
            }
            if (lottieComposition == null) {
                return new LottieResult<>(new IllegalArgumentException("Unable to parse composition"));
            }
            for (Map.Entry entry : hashMap.entrySet()) {
                String str2 = (String) entry.getKey();
                Iterator<LottieImageAsset> it = lottieComposition.images.values().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        lottieImageAsset = null;
                        break;
                    }
                    lottieImageAsset = it.next();
                    Objects.requireNonNull(lottieImageAsset);
                    if (lottieImageAsset.fileName.equals(str2)) {
                        break;
                    }
                }
                if (lottieImageAsset != null) {
                    Bitmap bitmap = (Bitmap) entry.getValue();
                    int i2 = lottieImageAsset.width;
                    int i3 = lottieImageAsset.height;
                    PathMeasure pathMeasure = Utils.pathMeasure;
                    if (!(bitmap.getWidth() == i2 && bitmap.getHeight() == i3)) {
                        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, i2, i3, true);
                        bitmap.recycle();
                        bitmap = createScaledBitmap;
                    }
                    lottieImageAsset.bitmap = bitmap;
                }
            }
            for (Map.Entry<String, LottieImageAsset> entry2 : lottieComposition.images.entrySet()) {
                LottieImageAsset value = entry2.getValue();
                Objects.requireNonNull(value);
                if (value.bitmap == null) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("There is no image for ");
                    LottieImageAsset value2 = entry2.getValue();
                    Objects.requireNonNull(value2);
                    m.append(value2.fileName);
                    return new LottieResult<>(new IllegalStateException(m.toString()));
                }
            }
            if (str != null) {
                LottieCompositionCache.INSTANCE.cache.put(str, lottieComposition);
            }
            return new LottieResult<>(lottieComposition);
        } catch (IOException e) {
            return new LottieResult<>(e);
        }
    }

    public static LottieResult fromJsonReaderSyncInternal(JsonUtf8Reader jsonUtf8Reader, String str, boolean z) {
        try {
            try {
                LottieComposition parse = LottieCompositionMoshiParser.parse(jsonUtf8Reader);
                if (str != null) {
                    LottieCompositionCache.INSTANCE.cache.put(str, parse);
                }
                LottieResult lottieResult = new LottieResult(parse);
                if (z) {
                    Utils.closeQuietly(jsonUtf8Reader);
                }
                return lottieResult;
            } catch (Exception e) {
                LottieResult lottieResult2 = new LottieResult(e);
                if (z) {
                    Utils.closeQuietly(jsonUtf8Reader);
                }
                return lottieResult2;
            }
        } catch (Throwable th) {
            if (z) {
                Utils.closeQuietly(jsonUtf8Reader);
            }
            throw th;
        }
    }

    public static String rawResCacheKey(Context context, int i) {
        boolean z;
        String str;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("rawRes");
        if ((context.getResources().getConfiguration().uiMode & 48) == 32) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            str = "_night_";
        } else {
            str = "_day_";
        }
        m.append(str);
        m.append(i);
        return m.toString();
    }
}
