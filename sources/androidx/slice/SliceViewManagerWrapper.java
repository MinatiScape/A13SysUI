package androidx.slice;

import android.app.slice.SliceManager;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import androidx.collection.ArrayMap;
import androidx.collection.ArraySet;
import androidx.slice.widget.SliceLiveData;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SliceViewManagerWrapper extends SliceViewManagerBase {
    public final SliceManager mManager;
    public final ArrayMap<String, Boolean> mCachedSuspendFlags = new ArrayMap<>();
    public final ArrayMap<String, String> mCachedAuthorities = new ArrayMap<>();
    public final ArraySet mSpecs = SliceConvert.unwrap(SliceLiveData.SUPPORTED_SPECS);

    public SliceViewManagerWrapper(Context context) {
        super(context);
        this.mManager = (SliceManager) context.getSystemService(SliceManager.class);
    }

    public final boolean isAuthoritySuspended(String str) {
        ArrayMap<String, String> arrayMap = this.mCachedAuthorities;
        Objects.requireNonNull(arrayMap);
        String orDefault = arrayMap.getOrDefault(str, null);
        if (orDefault == null) {
            ProviderInfo resolveContentProvider = this.mContext.getPackageManager().resolveContentProvider(str, 0);
            if (resolveContentProvider == null) {
                return false;
            }
            orDefault = resolveContentProvider.packageName;
            this.mCachedAuthorities.put(str, orDefault);
        }
        return isPackageSuspended(orDefault);
    }

    public final boolean isPackageSuspended(String str) {
        boolean z;
        ArrayMap<String, Boolean> arrayMap = this.mCachedSuspendFlags;
        Objects.requireNonNull(arrayMap);
        Boolean orDefault = arrayMap.getOrDefault(str, null);
        if (orDefault == null) {
            try {
                if ((this.mContext.getPackageManager().getApplicationInfo(str, 0).flags & 1073741824) != 0) {
                    z = true;
                } else {
                    z = false;
                }
                Boolean valueOf = Boolean.valueOf(z);
                this.mCachedSuspendFlags.put(str, valueOf);
                orDefault = valueOf;
            } catch (PackageManager.NameNotFoundException unused) {
                return false;
            }
        }
        return orDefault.booleanValue();
    }

    @Override // androidx.slice.SliceViewManager
    public final void pinSlice(Uri uri) {
        try {
            this.mManager.pinSlice(uri, this.mSpecs);
        } catch (RuntimeException e) {
            ContentProviderClient acquireContentProviderClient = this.mContext.getContentResolver().acquireContentProviderClient(uri);
            if (acquireContentProviderClient == null) {
                throw new IllegalArgumentException("No provider found for " + uri);
            }
            acquireContentProviderClient.release();
            throw e;
        }
    }

    @Override // androidx.slice.SliceViewManager
    public final void unpinSlice(Uri uri) {
        try {
            this.mManager.unpinSlice(uri);
        } catch (IllegalStateException unused) {
        }
    }
}
