package androidx.appcompat.content.res;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import androidx.appcompat.widget.ResourceManagerInternal;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ColorStateListInflaterCompat;
import androidx.core.content.res.ResourcesCompat;
import java.util.WeakHashMap;
@SuppressLint({"RestrictedAPI"})
/* loaded from: classes.dex */
public final class AppCompatResources {
    public static ColorStateList getColorStateList(Context context, int i) {
        ColorStateList colorStateList;
        ColorStateList colorStateList2;
        ResourcesCompat.ColorStateListCacheEntry colorStateListCacheEntry;
        Object obj = ContextCompat.sLock;
        Resources resources = context.getResources();
        Resources.Theme theme = context.getTheme();
        ResourcesCompat.ColorStateListCacheKey colorStateListCacheKey = new ResourcesCompat.ColorStateListCacheKey(resources, theme);
        synchronized (ResourcesCompat.sColorStateCacheLock) {
            SparseArray<ResourcesCompat.ColorStateListCacheEntry> sparseArray = ResourcesCompat.sColorStateCaches.get(colorStateListCacheKey);
            colorStateList = null;
            if (!(sparseArray == null || sparseArray.size() <= 0 || (colorStateListCacheEntry = sparseArray.get(i)) == null)) {
                if (colorStateListCacheEntry.mConfiguration.equals(resources.getConfiguration())) {
                    colorStateList2 = colorStateListCacheEntry.mValue;
                } else {
                    sparseArray.remove(i);
                }
            }
            colorStateList2 = null;
        }
        if (colorStateList2 != null) {
            return colorStateList2;
        }
        ThreadLocal<TypedValue> threadLocal = ResourcesCompat.sTempTypedValue;
        TypedValue typedValue = threadLocal.get();
        if (typedValue == null) {
            typedValue = new TypedValue();
            threadLocal.set(typedValue);
        }
        boolean z = true;
        resources.getValue(i, typedValue, true);
        int i2 = typedValue.type;
        if (i2 < 28 || i2 > 31) {
            z = false;
        }
        if (!z) {
            try {
                colorStateList = ColorStateListInflaterCompat.createFromXml(resources, resources.getXml(i), theme);
            } catch (Exception e) {
                Log.e("ResourcesCompat", "Failed to inflate ColorStateList, leaving it to the framework", e);
            }
        }
        if (colorStateList == null) {
            return resources.getColorStateList(i, theme);
        }
        synchronized (ResourcesCompat.sColorStateCacheLock) {
            WeakHashMap<ResourcesCompat.ColorStateListCacheKey, SparseArray<ResourcesCompat.ColorStateListCacheEntry>> weakHashMap = ResourcesCompat.sColorStateCaches;
            SparseArray<ResourcesCompat.ColorStateListCacheEntry> sparseArray2 = weakHashMap.get(colorStateListCacheKey);
            if (sparseArray2 == null) {
                sparseArray2 = new SparseArray<>();
                weakHashMap.put(colorStateListCacheKey, sparseArray2);
            }
            sparseArray2.append(i, new ResourcesCompat.ColorStateListCacheEntry(colorStateList, colorStateListCacheKey.mResources.getConfiguration()));
        }
        return colorStateList;
    }

    public static Drawable getDrawable(Context context, int i) {
        return ResourceManagerInternal.get().getDrawable(context, i);
    }
}
