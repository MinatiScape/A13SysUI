package androidx.core.graphics;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import android.graphics.fonts.FontStyle;
import android.os.ParcelFileDescriptor;
import androidx.collection.LruCache;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.provider.FontsContractCompat$FontInfo;
import androidx.transition.ViewUtilsBase;
import java.io.IOException;
import java.util.Objects;
/* loaded from: classes.dex */
public final class TypefaceCompat {
    public static final TypefaceCompatApi29Impl sTypefaceCompatImpl = new TypefaceCompatApi29Impl();
    public static final LruCache<String, Typeface> sTypefaceCache = new LruCache<>(16);

    /* loaded from: classes.dex */
    public static class ResourcesCallbackAdapter extends ViewUtilsBase {
        public ResourcesCompat.FontCallback mFontCallback;

        public ResourcesCallbackAdapter(ResourcesCompat.FontCallback fontCallback) {
            this.mFontCallback = fontCallback;
        }
    }

    public static void clearCache() {
        LruCache<String, Typeface> lruCache = sTypefaceCache;
        Objects.requireNonNull(lruCache);
        lruCache.trimToSize(-1);
    }

    public static Typeface createFromFontInfo(Context context, FontsContractCompat$FontInfo[] fontsContractCompat$FontInfoArr, int i) {
        int i2;
        ParcelFileDescriptor openFileDescriptor;
        Objects.requireNonNull(sTypefaceCompatImpl);
        ContentResolver contentResolver = context.getContentResolver();
        try {
            int length = fontsContractCompat$FontInfoArr.length;
            int i3 = 0;
            FontFamily.Builder builder = null;
            int i4 = 0;
            while (true) {
                int i5 = 1;
                if (i4 < length) {
                    FontsContractCompat$FontInfo fontsContractCompat$FontInfo = fontsContractCompat$FontInfoArr[i4];
                    try {
                        Objects.requireNonNull(fontsContractCompat$FontInfo);
                        openFileDescriptor = contentResolver.openFileDescriptor(fontsContractCompat$FontInfo.mUri, "r", null);
                    } catch (IOException unused) {
                    }
                    if (openFileDescriptor != null) {
                        try {
                            Font.Builder weight = new Font.Builder(openFileDescriptor).setWeight(fontsContractCompat$FontInfo.mWeight);
                            if (!fontsContractCompat$FontInfo.mItalic) {
                                i5 = 0;
                            }
                            Font build = weight.setSlant(i5).setTtcIndex(fontsContractCompat$FontInfo.mTtcIndex).build();
                            if (builder == null) {
                                builder = new FontFamily.Builder(build);
                            } else {
                                builder.addFont(build);
                            }
                        } catch (Throwable th) {
                            try {
                                openFileDescriptor.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                            throw th;
                            break;
                        }
                    } else if (openFileDescriptor == null) {
                        i4++;
                    }
                    openFileDescriptor.close();
                    i4++;
                } else if (builder == null) {
                    return null;
                } else {
                    if ((i & 1) != 0) {
                        i2 = 700;
                    } else {
                        i2 = 400;
                    }
                    if ((i & 2) != 0) {
                        i3 = 1;
                    }
                    return new Typeface.CustomFallbackBuilder(builder.build()).setStyle(new FontStyle(i2, i3)).build();
                }
            }
        } catch (Exception unused2) {
            return null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0025, code lost:
        if (r0.equals(r5) == false) goto L_0x0029;
     */
    /* JADX WARN: Type inference failed for: r13v3, types: [androidx.core.provider.FontRequestWorker$3] */
    /* JADX WARN: Type inference failed for: r9v11, types: [androidx.core.provider.FontRequestWorker$4] */
    /* JADX WARN: Unknown variable types count: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static android.graphics.Typeface createFromResourcesFamilyXml(final android.content.Context r8, androidx.core.content.res.FontResourcesParserCompat.FamilyResourceEntry r9, android.content.res.Resources r10, int r11, final int r12, androidx.core.content.res.ResourcesCompat.FontCallback r13, boolean r14) {
        /*
            Method dump skipped, instructions count: 478
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.graphics.TypefaceCompat.createFromResourcesFamilyXml(android.content.Context, androidx.core.content.res.FontResourcesParserCompat$FamilyResourceEntry, android.content.res.Resources, int, int, androidx.core.content.res.ResourcesCompat$FontCallback, boolean):android.graphics.Typeface");
    }

    public static Typeface createFromResourcesFontFile(Resources resources, int i, int i2) {
        Typeface typeface;
        Objects.requireNonNull(sTypefaceCompatImpl);
        try {
            Font build = new Font.Builder(resources, i).build();
            typeface = new Typeface.CustomFallbackBuilder(new FontFamily.Builder(build).build()).setStyle(build.getStyle()).build();
        } catch (Exception unused) {
            typeface = null;
        }
        if (typeface != null) {
            sTypefaceCache.put(createResourceUid(resources, i, i2), typeface);
        }
        return typeface;
    }

    public static String createResourceUid(Resources resources, int i, int i2) {
        return resources.getResourcePackageName(i) + "-" + i + "-" + i2;
    }
}
