package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.collection.LongSparseArray;
import androidx.collection.LruCache;
import androidx.collection.SimpleArrayMap;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.R$dimen;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public final class ResourceManagerInternal {
    public static ResourceManagerInternal INSTANCE;
    public SimpleArrayMap<String, InflateDelegate> mDelegates;
    public final WeakHashMap<Context, LongSparseArray<WeakReference<Drawable.ConstantState>>> mDrawableCaches = new WeakHashMap<>(0);
    public boolean mHasCheckedVectorDrawableSetup;
    public ResourceManagerHooks mHooks;
    public SparseArrayCompat<String> mKnownDrawableIdTags;
    public WeakHashMap<Context, SparseArrayCompat<ColorStateList>> mTintLists;
    public TypedValue mTypedValue;
    public static final PorterDuff.Mode DEFAULT_MODE = PorterDuff.Mode.SRC_IN;
    public static final ColorFilterLruCache COLOR_FILTER_CACHE = new ColorFilterLruCache();

    /* loaded from: classes.dex */
    public static class ColorFilterLruCache extends LruCache<Integer, PorterDuffColorFilter> {
        public ColorFilterLruCache() {
            super(6);
        }
    }

    /* loaded from: classes.dex */
    public interface InflateDelegate {
        Drawable createFromXmlInner(Context context, XmlResourceParser xmlResourceParser, AttributeSet attributeSet, Resources.Theme theme);
    }

    /* loaded from: classes.dex */
    public interface ResourceManagerHooks {
    }

    public final synchronized boolean addDrawableToCache(Context context, long j, Drawable drawable) {
        Drawable.ConstantState constantState = drawable.getConstantState();
        if (constantState == null) {
            return false;
        }
        LongSparseArray<WeakReference<Drawable.ConstantState>> longSparseArray = this.mDrawableCaches.get(context);
        if (longSparseArray == null) {
            longSparseArray = new LongSparseArray<>();
            this.mDrawableCaches.put(context, longSparseArray);
        }
        longSparseArray.put(j, new WeakReference<>(constantState));
        return true;
    }

    public final synchronized Drawable getCachedDrawable(Context context, long j) {
        LongSparseArray<WeakReference<Drawable.ConstantState>> longSparseArray = this.mDrawableCaches.get(context);
        if (longSparseArray == null) {
            return null;
        }
        WeakReference weakReference = (WeakReference) longSparseArray.get(j, null);
        if (weakReference != null) {
            Drawable.ConstantState constantState = (Drawable.ConstantState) weakReference.get();
            if (constantState != null) {
                return constantState.newDrawable(context.getResources());
            }
            int binarySearch = R$dimen.binarySearch(longSparseArray.mKeys, longSparseArray.mSize, j);
            if (binarySearch >= 0) {
                Object[] objArr = longSparseArray.mValues;
                Object obj = objArr[binarySearch];
                Object obj2 = LongSparseArray.DELETED;
                if (obj != obj2) {
                    objArr[binarySearch] = obj2;
                    longSparseArray.mGarbage = true;
                }
            }
        }
        return null;
    }

    public final synchronized Drawable getDrawable(Context context, int i) {
        return getDrawable(context, i, false);
    }

    public final synchronized ColorStateList getTintList(Context context, int i) {
        ColorStateList colorStateList;
        SparseArrayCompat<ColorStateList> sparseArrayCompat;
        try {
            WeakHashMap<Context, SparseArrayCompat<ColorStateList>> weakHashMap = this.mTintLists;
            ColorStateList colorStateList2 = null;
            if (weakHashMap == null || (sparseArrayCompat = weakHashMap.get(context)) == null) {
                colorStateList = null;
            } else {
                colorStateList = (ColorStateList) sparseArrayCompat.get(i, null);
            }
            if (colorStateList == null) {
                ResourceManagerHooks resourceManagerHooks = this.mHooks;
                if (resourceManagerHooks != null) {
                    colorStateList2 = ((AppCompatDrawableManager.AnonymousClass1) resourceManagerHooks).getTintListForDrawableRes(context, i);
                }
                if (colorStateList2 != null) {
                    if (this.mTintLists == null) {
                        this.mTintLists = new WeakHashMap<>();
                    }
                    SparseArrayCompat<ColorStateList> sparseArrayCompat2 = this.mTintLists.get(context);
                    if (sparseArrayCompat2 == null) {
                        sparseArrayCompat2 = new SparseArrayCompat<>();
                        this.mTintLists.put(context, sparseArrayCompat2);
                    }
                    sparseArrayCompat2.append(i, colorStateList2);
                }
                colorStateList = colorStateList2;
            }
        } catch (Throwable th) {
            throw th;
        }
        return colorStateList;
    }

    public static synchronized ResourceManagerInternal get() {
        ResourceManagerInternal resourceManagerInternal;
        synchronized (ResourceManagerInternal.class) {
            if (INSTANCE == null) {
                INSTANCE = new ResourceManagerInternal();
            }
            resourceManagerInternal = INSTANCE;
        }
        return resourceManagerInternal;
    }

    public static synchronized PorterDuffColorFilter getPorterDuffColorFilter(int i, PorterDuff.Mode mode) {
        PorterDuffColorFilter porterDuffColorFilter;
        synchronized (ResourceManagerInternal.class) {
            ColorFilterLruCache colorFilterLruCache = COLOR_FILTER_CACHE;
            Objects.requireNonNull(colorFilterLruCache);
            int i2 = (i + 31) * 31;
            porterDuffColorFilter = colorFilterLruCache.get(Integer.valueOf(mode.hashCode() + i2));
            if (porterDuffColorFilter == null) {
                porterDuffColorFilter = new PorterDuffColorFilter(i, mode);
                Objects.requireNonNull(colorFilterLruCache);
                colorFilterLruCache.put(Integer.valueOf(mode.hashCode() + i2), porterDuffColorFilter);
            }
        }
        return porterDuffColorFilter;
    }

    public final Drawable createDrawableIfNeeded(Context context, int i) {
        if (this.mTypedValue == null) {
            this.mTypedValue = new TypedValue();
        }
        TypedValue typedValue = this.mTypedValue;
        context.getResources().getValue(i, typedValue, true);
        long j = (typedValue.assetCookie << 32) | typedValue.data;
        Drawable cachedDrawable = getCachedDrawable(context, j);
        if (cachedDrawable != null) {
            return cachedDrawable;
        }
        LayerDrawable layerDrawable = null;
        if (this.mHooks != null) {
            if (i == 2131231517) {
                layerDrawable = new LayerDrawable(new Drawable[]{getDrawable(context, 2131231516), getDrawable(context, 2131231518)});
            } else if (i == 2131231555) {
                layerDrawable = AppCompatDrawableManager.AnonymousClass1.getRatingBarLayerDrawable(this, context, 2131165274);
            } else if (i == 2131231554) {
                layerDrawable = AppCompatDrawableManager.AnonymousClass1.getRatingBarLayerDrawable(this, context, 2131165275);
            } else if (i == 2131231556) {
                layerDrawable = AppCompatDrawableManager.AnonymousClass1.getRatingBarLayerDrawable(this, context, 2131165276);
            }
        }
        if (layerDrawable != null) {
            layerDrawable.setChangingConfigurations(typedValue.changingConfigurations);
            addDrawableToCache(context, j, layerDrawable);
        }
        return layerDrawable;
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x002b, code lost:
        if (r0 == null) goto L_0x01ac;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x011f, code lost:
        r8.setTintMode(r3);
     */
    /* JADX WARN: Removed duplicated region for block: B:60:0x00f0 A[Catch: all -> 0x00d2, TryCatch #0 {all -> 0x00d2, blocks: (B:3:0x0001, B:6:0x0008, B:8:0x0013, B:10:0x0017, B:16:0x002d, B:18:0x0032, B:20:0x0038, B:22:0x003e, B:25:0x004c, B:28:0x0059, B:29:0x0060, B:31:0x0064, B:32:0x006b, B:35:0x0085, B:37:0x0089, B:39:0x0095, B:40:0x009d, B:45:0x00a9, B:47:0x00bf, B:49:0x00c9, B:52:0x00d5, B:53:0x00dc, B:55:0x00de, B:57:0x00e7, B:60:0x00f0, B:62:0x00f6, B:64:0x00fe, B:66:0x0104, B:68:0x010a, B:69:0x010e, B:74:0x011b, B:76:0x011f, B:77:0x0124, B:81:0x013b, B:88:0x0171, B:92:0x019b, B:99:0x01a8, B:102:0x01ac, B:103:0x01b5), top: B:105:0x0001, inners: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x00f6 A[Catch: all -> 0x00d2, TryCatch #0 {all -> 0x00d2, blocks: (B:3:0x0001, B:6:0x0008, B:8:0x0013, B:10:0x0017, B:16:0x002d, B:18:0x0032, B:20:0x0038, B:22:0x003e, B:25:0x004c, B:28:0x0059, B:29:0x0060, B:31:0x0064, B:32:0x006b, B:35:0x0085, B:37:0x0089, B:39:0x0095, B:40:0x009d, B:45:0x00a9, B:47:0x00bf, B:49:0x00c9, B:52:0x00d5, B:53:0x00dc, B:55:0x00de, B:57:0x00e7, B:60:0x00f0, B:62:0x00f6, B:64:0x00fe, B:66:0x0104, B:68:0x010a, B:69:0x010e, B:74:0x011b, B:76:0x011f, B:77:0x0124, B:81:0x013b, B:88:0x0171, B:92:0x019b, B:99:0x01a8, B:102:0x01ac, B:103:0x01b5), top: B:105:0x0001, inners: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x00fe A[Catch: all -> 0x00d2, TryCatch #0 {all -> 0x00d2, blocks: (B:3:0x0001, B:6:0x0008, B:8:0x0013, B:10:0x0017, B:16:0x002d, B:18:0x0032, B:20:0x0038, B:22:0x003e, B:25:0x004c, B:28:0x0059, B:29:0x0060, B:31:0x0064, B:32:0x006b, B:35:0x0085, B:37:0x0089, B:39:0x0095, B:40:0x009d, B:45:0x00a9, B:47:0x00bf, B:49:0x00c9, B:52:0x00d5, B:53:0x00dc, B:55:0x00de, B:57:0x00e7, B:60:0x00f0, B:62:0x00f6, B:64:0x00fe, B:66:0x0104, B:68:0x010a, B:69:0x010e, B:74:0x011b, B:76:0x011f, B:77:0x0124, B:81:0x013b, B:88:0x0171, B:92:0x019b, B:99:0x01a8, B:102:0x01ac, B:103:0x01b5), top: B:105:0x0001, inners: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:91:0x019a  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x01a8 A[Catch: all -> 0x00d2, TRY_LEAVE, TryCatch #0 {all -> 0x00d2, blocks: (B:3:0x0001, B:6:0x0008, B:8:0x0013, B:10:0x0017, B:16:0x002d, B:18:0x0032, B:20:0x0038, B:22:0x003e, B:25:0x004c, B:28:0x0059, B:29:0x0060, B:31:0x0064, B:32:0x006b, B:35:0x0085, B:37:0x0089, B:39:0x0095, B:40:0x009d, B:45:0x00a9, B:47:0x00bf, B:49:0x00c9, B:52:0x00d5, B:53:0x00dc, B:55:0x00de, B:57:0x00e7, B:60:0x00f0, B:62:0x00f6, B:64:0x00fe, B:66:0x0104, B:68:0x010a, B:69:0x010e, B:74:0x011b, B:76:0x011f, B:77:0x0124, B:81:0x013b, B:88:0x0171, B:92:0x019b, B:99:0x01a8, B:102:0x01ac, B:103:0x01b5), top: B:105:0x0001, inners: #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final synchronized android.graphics.drawable.Drawable getDrawable(android.content.Context r13, int r14, boolean r15) {
        /*
            Method dump skipped, instructions count: 440
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.ResourceManagerInternal.getDrawable(android.content.Context, int, boolean):android.graphics.drawable.Drawable");
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x004f  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x006b  */
    /* JADX WARN: Removed duplicated region for block: B:33:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean tintDrawableUsingColorFilter(android.content.Context r7, int r8, android.graphics.drawable.Drawable r9) {
        /*
            r6 = this;
            androidx.appcompat.widget.ResourceManagerInternal$ResourceManagerHooks r6 = r6.mHooks
            r0 = 1
            r1 = 0
            if (r6 == 0) goto L_0x006f
            androidx.appcompat.widget.AppCompatDrawableManager$1 r6 = (androidx.appcompat.widget.AppCompatDrawableManager.AnonymousClass1) r6
            android.graphics.PorterDuff$Mode r2 = androidx.appcompat.widget.AppCompatDrawableManager.DEFAULT_MODE
            int[] r3 = r6.COLORFILTER_TINT_COLOR_CONTROL_NORMAL
            boolean r3 = androidx.appcompat.widget.AppCompatDrawableManager.AnonymousClass1.arrayContains(r3, r8)
            r4 = 16842801(0x1010031, float:2.3693695E-38)
            r5 = -1
            if (r3 == 0) goto L_0x001a
            r4 = 2130968820(0x7f0400f4, float:1.7546304E38)
            goto L_0x0046
        L_0x001a:
            int[] r3 = r6.COLORFILTER_COLOR_CONTROL_ACTIVATED
            boolean r3 = androidx.appcompat.widget.AppCompatDrawableManager.AnonymousClass1.arrayContains(r3, r8)
            if (r3 == 0) goto L_0x0026
            r4 = 2130968818(0x7f0400f2, float:1.75463E38)
            goto L_0x0046
        L_0x0026:
            int[] r6 = r6.COLORFILTER_COLOR_BACKGROUND_MULTIPLY
            boolean r6 = androidx.appcompat.widget.AppCompatDrawableManager.AnonymousClass1.arrayContains(r6, r8)
            if (r6 == 0) goto L_0x0031
            android.graphics.PorterDuff$Mode r2 = android.graphics.PorterDuff.Mode.MULTIPLY
            goto L_0x0046
        L_0x0031:
            r6 = 2131231541(0x7f080335, float:1.8079166E38)
            if (r8 != r6) goto L_0x0041
            r6 = 16842800(0x1010030, float:2.3693693E-38)
            r8 = 1109603123(0x42233333, float:40.8)
            int r8 = java.lang.Math.round(r8)
            goto L_0x0048
        L_0x0041:
            r6 = 2131231520(0x7f080320, float:1.8079123E38)
            if (r8 != r6) goto L_0x004a
        L_0x0046:
            r6 = r4
            r8 = r5
        L_0x0048:
            r3 = r0
            goto L_0x004d
        L_0x004a:
            r6 = r1
            r3 = r6
            r8 = r5
        L_0x004d:
            if (r3 == 0) goto L_0x006b
            boolean r3 = androidx.appcompat.widget.DrawableUtils.canSafelyMutateDrawable(r9)
            if (r3 == 0) goto L_0x0059
            android.graphics.drawable.Drawable r9 = r9.mutate()
        L_0x0059:
            int r6 = androidx.appcompat.widget.ThemeUtils.getThemeAttrColor(r7, r6)
            android.graphics.PorterDuffColorFilter r6 = androidx.appcompat.widget.AppCompatDrawableManager.getPorterDuffColorFilter(r6, r2)
            r9.setColorFilter(r6)
            if (r8 == r5) goto L_0x0069
            r9.setAlpha(r8)
        L_0x0069:
            r6 = r0
            goto L_0x006c
        L_0x006b:
            r6 = r1
        L_0x006c:
            if (r6 == 0) goto L_0x006f
            goto L_0x0070
        L_0x006f:
            r0 = r1
        L_0x0070:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.ResourceManagerInternal.tintDrawableUsingColorFilter(android.content.Context, int, android.graphics.drawable.Drawable):boolean");
    }
}
