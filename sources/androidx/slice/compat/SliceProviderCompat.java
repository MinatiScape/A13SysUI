package androidx.slice.compat;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.net.Uri;
import androidx.slice.SliceItemHolder;
import kotlinx.coroutines.internal.Symbol;
/* loaded from: classes.dex */
public final class SliceProviderCompat {

    /* renamed from: androidx.slice.compat.SliceProviderCompat$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass2 {
        public static final Symbol NULL = new Symbol("NULL");
        public static final Symbol UNINITIALIZED = new Symbol("UNINITIALIZED");

        public void handle(SliceItemHolder sliceItemHolder) {
            throw null;
        }
    }

    /* loaded from: classes.dex */
    public static class ProviderHolder implements AutoCloseable {
        public final ContentProviderClient mProvider;

        @Override // java.lang.AutoCloseable
        public final void close() {
            ContentProviderClient contentProviderClient = this.mProvider;
            if (contentProviderClient != null) {
                contentProviderClient.close();
            }
        }

        public ProviderHolder(ContentProviderClient contentProviderClient) {
            this.mProvider = contentProviderClient;
        }
    }

    public static ProviderHolder acquireClient(ContentResolver contentResolver, Uri uri) {
        ContentProviderClient acquireUnstableContentProviderClient = contentResolver.acquireUnstableContentProviderClient(uri);
        if (acquireUnstableContentProviderClient != null) {
            return new ProviderHolder(acquireUnstableContentProviderClient);
        }
        throw new IllegalArgumentException("No provider found for " + uri);
    }
}
