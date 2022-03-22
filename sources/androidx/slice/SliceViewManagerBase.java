package androidx.slice;

import android.app.slice.SliceManager;
import android.content.ContentProviderClient;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.ArrayMap;
import android.util.Pair;
import androidx.slice.SliceViewManager;
import androidx.slice.widget.SliceLiveData;
import androidx.slice.widget.SliceLiveData$SliceLiveDataImpl$$ExternalSyntheticLambda0;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public abstract class SliceViewManagerBase extends SliceViewManager {
    public final Context mContext;
    public final ArrayMap<Pair<Uri, SliceViewManager.SliceCallback>, SliceListenerImpl> mListenerLookup = new ArrayMap<>();

    /* loaded from: classes.dex */
    public class SliceListenerImpl {
        public final SliceViewManager.SliceCallback mCallback;
        public final Executor mExecutor;
        public boolean mPinned;
        public Uri mUri;
        public final AnonymousClass1 mUpdateSlice = new Runnable() { // from class: androidx.slice.SliceViewManagerBase.SliceListenerImpl.1
            @Override // java.lang.Runnable
            public final void run() {
                SliceListenerImpl sliceListenerImpl = SliceListenerImpl.this;
                Objects.requireNonNull(sliceListenerImpl);
                if (!sliceListenerImpl.mPinned) {
                    try {
                        SliceViewManagerBase.this.pinSlice(sliceListenerImpl.mUri);
                        sliceListenerImpl.mPinned = true;
                    } catch (SecurityException unused) {
                    }
                }
                SliceListenerImpl sliceListenerImpl2 = SliceListenerImpl.this;
                Context context = SliceViewManagerBase.this.mContext;
                final Slice wrap = SliceConvert.wrap(((SliceManager) context.getSystemService(SliceManager.class)).bindSlice(sliceListenerImpl2.mUri, SliceConvert.unwrap(SliceLiveData.SUPPORTED_SPECS)), context);
                SliceListenerImpl.this.mExecutor.execute(new Runnable() { // from class: androidx.slice.SliceViewManagerBase.SliceListenerImpl.1.1
                    @Override // java.lang.Runnable
                    public final void run() {
                        SliceViewManager.SliceCallback sliceCallback = SliceListenerImpl.this.mCallback;
                        Slice slice = wrap;
                        SliceLiveData$SliceLiveDataImpl$$ExternalSyntheticLambda0 sliceLiveData$SliceLiveDataImpl$$ExternalSyntheticLambda0 = (SliceLiveData$SliceLiveDataImpl$$ExternalSyntheticLambda0) sliceCallback;
                        Objects.requireNonNull(sliceLiveData$SliceLiveDataImpl$$ExternalSyntheticLambda0);
                        SliceLiveData.SliceLiveDataImpl sliceLiveDataImpl = sliceLiveData$SliceLiveDataImpl$$ExternalSyntheticLambda0.f$0;
                        Objects.requireNonNull(sliceLiveDataImpl);
                        sliceLiveDataImpl.postValue(slice);
                    }
                });
            }
        };
        public final AnonymousClass2 mObserver = new ContentObserver(new Handler(Looper.getMainLooper())) { // from class: androidx.slice.SliceViewManagerBase.SliceListenerImpl.2
            @Override // android.database.ContentObserver
            public final void onChange(boolean z) {
                AsyncTask.execute(SliceListenerImpl.this.mUpdateSlice);
            }
        };

        /* JADX WARN: Type inference failed for: r3v1, types: [androidx.slice.SliceViewManagerBase$SliceListenerImpl$1] */
        /* JADX WARN: Type inference failed for: r3v2, types: [androidx.slice.SliceViewManagerBase$SliceListenerImpl$2] */
        public SliceListenerImpl(Uri uri, AnonymousClass1 r5, SliceLiveData$SliceLiveDataImpl$$ExternalSyntheticLambda0 sliceLiveData$SliceLiveDataImpl$$ExternalSyntheticLambda0) {
            this.mUri = uri;
            this.mExecutor = r5;
            this.mCallback = sliceLiveData$SliceLiveDataImpl$$ExternalSyntheticLambda0;
        }
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [androidx.slice.SliceViewManagerBase$1] */
    public final void registerSliceCallback(Uri uri, SliceLiveData$SliceLiveDataImpl$$ExternalSyntheticLambda0 sliceLiveData$SliceLiveDataImpl$$ExternalSyntheticLambda0) {
        final Handler handler = new Handler(Looper.getMainLooper());
        SliceListenerImpl sliceListenerImpl = new SliceListenerImpl(uri, new Executor() { // from class: androidx.slice.SliceViewManagerBase.1
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                handler.post(runnable);
            }
        }, sliceLiveData$SliceLiveDataImpl$$ExternalSyntheticLambda0);
        Pair<Uri, SliceViewManager.SliceCallback> pair = new Pair<>(uri, sliceLiveData$SliceLiveDataImpl$$ExternalSyntheticLambda0);
        synchronized (this.mListenerLookup) {
            try {
                SliceListenerImpl put = this.mListenerLookup.put(pair, sliceListenerImpl);
                if (put != null) {
                    SliceViewManagerBase.this.mContext.getContentResolver().unregisterContentObserver(put.mObserver);
                    if (put.mPinned) {
                        SliceViewManagerBase.this.unpinSlice(put.mUri);
                        put.mPinned = false;
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        ContentProviderClient acquireContentProviderClient = this.mContext.getContentResolver().acquireContentProviderClient(sliceListenerImpl.mUri);
        if (acquireContentProviderClient != null) {
            acquireContentProviderClient.release();
            this.mContext.getContentResolver().registerContentObserver(sliceListenerImpl.mUri, true, sliceListenerImpl.mObserver);
            if (!sliceListenerImpl.mPinned) {
                try {
                    pinSlice(sliceListenerImpl.mUri);
                    sliceListenerImpl.mPinned = true;
                } catch (SecurityException unused) {
                }
            }
        }
    }

    public SliceViewManagerBase(Context context) {
        this.mContext = context;
    }
}
