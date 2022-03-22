package androidx.slice;

import android.app.PendingIntent;
import android.app.slice.Slice;
import android.app.slice.SliceManager;
import android.app.slice.SliceSpec;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Process;
import android.util.Log;
import androidx.core.app.CoreComponentFactory;
import androidx.slice.SliceConvert;
import androidx.slice.SliceProvider;
import androidx.slice.compat.CompatPermissionManager;
import androidx.slice.compat.SliceProviderWrapperContainer$SliceProviderWrapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes.dex */
public abstract class SliceProvider extends ContentProvider implements CoreComponentFactory.CompatWrapped {
    public static Set<SliceSpec> sSpecs;
    public String[] mAuthorities;
    public String mAuthority;
    public ArrayList mPinnedSliceUris;
    public Context mContext = null;
    public final Object mCompatLock = new Object();
    public final Object mPinnedSliceUrisLock = new Object();
    public final String[] mAutoGrantPermissions = new String[0];

    @Override // android.content.ContentProvider
    public final int bulkInsert(Uri uri, ContentValues[] contentValuesArr) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public final Bundle call(String str, String str2, Bundle bundle) {
        return null;
    }

    @Override // android.content.ContentProvider
    public final Uri canonicalize(Uri uri) {
        return null;
    }

    @Override // android.content.ContentProvider
    public final int delete(Uri uri, String str, String[] strArr) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public final String getType(Uri uri) {
        return "vnd.android.slice";
    }

    @Override // android.content.ContentProvider
    public final Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    public abstract Slice onBindSlice();

    public abstract void onCreateSliceProvider();

    @Override // android.content.ContentProvider
    public final Cursor query(Uri uri, String[] strArr, Bundle bundle, CancellationSignal cancellationSignal) {
        return null;
    }

    @Override // android.content.ContentProvider
    public final Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        return null;
    }

    @Override // android.content.ContentProvider
    public final Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2, CancellationSignal cancellationSignal) {
        return null;
    }

    @Override // android.content.ContentProvider
    public final int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return 0;
    }

    public final List<Uri> getPinnedSlices() {
        synchronized (this.mPinnedSliceUrisLock) {
            try {
                if (this.mPinnedSliceUris == null) {
                    this.mPinnedSliceUris = new ArrayList(((SliceManager) getContext().getSystemService(SliceManager.class)).getPinnedSlices());
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return this.mPinnedSliceUris;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [androidx.slice.compat.SliceProviderWrapperContainer$SliceProviderWrapper] */
    @Override // androidx.core.app.CoreComponentFactory.CompatWrapped
    public final SliceProviderWrapperContainer$SliceProviderWrapper getWrapper() {
        return new android.app.slice.SliceProvider(this, this.mAutoGrantPermissions) { // from class: androidx.slice.compat.SliceProviderWrapperContainer$SliceProviderWrapper
            public String[] mAutoGrantPermissions;
            public SliceManager mSliceManager;
            public SliceProvider mSliceProvider;

            @Override // android.content.ContentProvider
            public final boolean onCreate() {
                return true;
            }

            @Override // android.app.slice.SliceProvider, android.content.ContentProvider
            public final void attachInfo(Context context, ProviderInfo providerInfo) {
                this.mSliceProvider.attachInfo(context, providerInfo);
                super.attachInfo(context, providerInfo);
                this.mSliceManager = (SliceManager) context.getSystemService(SliceManager.class);
            }

            @Override // android.app.slice.SliceProvider, android.content.ContentProvider
            public final Bundle call(String str, String str2, Bundle bundle) {
                Intent intent;
                if (this.mAutoGrantPermissions != null) {
                    Uri uri = null;
                    if ("bind_slice".equals(str)) {
                        if (bundle != null) {
                            uri = (Uri) bundle.getParcelable("slice_uri");
                        }
                    } else if ("map_slice".equals(str) && (intent = (Intent) bundle.getParcelable("slice_intent")) != null) {
                        onMapIntentToUri(intent);
                        throw null;
                    }
                    if (!(uri == null || this.mSliceManager.checkSlicePermission(uri, Binder.getCallingPid(), Binder.getCallingUid()) == 0)) {
                        checkPermissions(uri);
                    }
                }
                if ("androidx.remotecallback.method.PROVIDER_CALLBACK".equals(str)) {
                    return this.mSliceProvider.call(str, str2, bundle);
                }
                return super.call(str, str2, bundle);
            }

            public final void checkPermissions(Uri uri) {
                String[] strArr;
                if (uri != null) {
                    for (String str : this.mAutoGrantPermissions) {
                        if (getContext().checkCallingPermission(str) == 0) {
                            this.mSliceManager.grantSlicePermission(str, uri);
                            getContext().getContentResolver().notifyChange(uri, null);
                            return;
                        }
                    }
                }
            }

            @Override // android.app.slice.SliceProvider
            public final PendingIntent onCreatePermissionRequest(Uri uri) {
                if (this.mAutoGrantPermissions != null) {
                    checkPermissions(uri);
                }
                SliceProvider sliceProvider = this.mSliceProvider;
                getCallingPackage();
                Objects.requireNonNull(sliceProvider);
                return super.onCreatePermissionRequest(uri);
            }

            @Override // android.app.slice.SliceProvider
            public final Collection<Uri> onGetSliceDescendants(Uri uri) {
                Objects.requireNonNull(this.mSliceProvider);
                return Collections.emptyList();
            }

            @Override // android.app.slice.SliceProvider
            public final Uri onMapIntentToUri(Intent intent) {
                Objects.requireNonNull(this.mSliceProvider);
                throw new UnsupportedOperationException("This provider has not implemented intent to uri mapping");
            }

            @Override // android.app.slice.SliceProvider
            public final void onSlicePinned(Uri uri) {
                Objects.requireNonNull(this.mSliceProvider);
                SliceProvider sliceProvider = this.mSliceProvider;
                Objects.requireNonNull(sliceProvider);
                ArrayList arrayList = (ArrayList) sliceProvider.getPinnedSlices();
                if (!arrayList.contains(uri)) {
                    arrayList.add(uri);
                }
            }

            @Override // android.app.slice.SliceProvider
            public final void onSliceUnpinned(Uri uri) {
                Objects.requireNonNull(this.mSliceProvider);
                SliceProvider sliceProvider = this.mSliceProvider;
                Objects.requireNonNull(sliceProvider);
                ArrayList arrayList = (ArrayList) sliceProvider.getPinnedSlices();
                if (arrayList.contains(uri)) {
                    arrayList.remove(uri);
                }
            }

            {
                super(r3);
                this.mAutoGrantPermissions = (r3 == null || r3.length == 0) ? null : r3;
                this.mSliceProvider = this;
            }

            @Override // android.app.slice.SliceProvider
            public final Slice onBindSlice(Uri uri, Set<SliceSpec> set) {
                SliceProvider.sSpecs = SliceConvert.wrap(set);
                try {
                    return SliceConvert.unwrap(this.mSliceProvider.onBindSlice());
                } catch (Exception e) {
                    Log.wtf("SliceProviderWrapper", "Slice with URI " + uri.toString() + " is invalid.", e);
                    return null;
                } finally {
                    SliceProvider.sSpecs = null;
                }
            }
        };
    }

    public CompatPermissionManager onCreatePermissionManager(String[] strArr) {
        Context context = getContext();
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("slice_perms_");
        m.append(getClass().getName());
        return new CompatPermissionManager(context, m.toString(), Process.myUid(), strArr);
    }

    @Override // android.content.ContentProvider
    public final void attachInfo(Context context, ProviderInfo providerInfo) {
        String str;
        super.attachInfo(context, providerInfo);
        if (this.mContext == null) {
            this.mContext = context;
            if (providerInfo != null && (str = providerInfo.authority) != null) {
                if (str.indexOf(59) == -1) {
                    this.mAuthority = str;
                    this.mAuthorities = null;
                    return;
                }
                this.mAuthority = null;
                this.mAuthorities = str.split(";");
            }
        }
    }

    @Override // android.content.ContentProvider
    public final boolean onCreate() {
        onCreateSliceProvider();
        return true;
    }
}
