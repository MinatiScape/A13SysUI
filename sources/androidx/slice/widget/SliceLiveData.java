package androidx.slice.widget;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import androidx.collection.ArraySet;
import androidx.lifecycle.LiveData;
import androidx.slice.Slice;
import androidx.slice.SliceConvert;
import androidx.slice.SliceSpec;
import androidx.slice.SliceSpecs;
import androidx.slice.SliceViewManagerBase;
import androidx.slice.SliceViewManagerWrapper;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SliceLiveData {
    public static final ArraySet SUPPORTED_SPECS = new ArraySet(Arrays.asList(SliceSpecs.BASIC, SliceSpecs.LIST, SliceSpecs.LIST_V2, new SliceSpec("androidx.app.slice.BASIC", 1), new SliceSpec("androidx.app.slice.LIST", 1)));

    /* loaded from: classes.dex */
    public interface OnErrorListener {
        void onSliceError();
    }

    /* loaded from: classes.dex */
    public static class SliceLiveDataImpl extends LiveData<Slice> {
        public final SliceViewManagerWrapper mSliceViewManager;
        public Uri mUri;
        public final AnonymousClass1 mUpdateSlice = new Runnable() { // from class: androidx.slice.widget.SliceLiveData.SliceLiveDataImpl.1
            @Override // java.lang.Runnable
            public final void run() {
                Slice slice;
                try {
                    SliceLiveDataImpl sliceLiveDataImpl = SliceLiveDataImpl.this;
                    Uri uri = sliceLiveDataImpl.mUri;
                    if (uri != null) {
                        SliceViewManagerWrapper sliceViewManagerWrapper = sliceLiveDataImpl.mSliceViewManager;
                        Objects.requireNonNull(sliceViewManagerWrapper);
                        if (sliceViewManagerWrapper.isAuthoritySuspended(uri.getAuthority())) {
                            slice = null;
                        } else {
                            slice = SliceConvert.wrap(sliceViewManagerWrapper.mManager.bindSlice(uri, sliceViewManagerWrapper.mSpecs), sliceViewManagerWrapper.mContext);
                        }
                        SliceLiveDataImpl sliceLiveDataImpl2 = SliceLiveDataImpl.this;
                        if (sliceLiveDataImpl2.mUri == null && slice != null) {
                            sliceLiveDataImpl2.mUri = slice.getUri();
                            SliceLiveDataImpl sliceLiveDataImpl3 = SliceLiveDataImpl.this;
                            sliceLiveDataImpl3.mSliceViewManager.registerSliceCallback(sliceLiveDataImpl3.mUri, sliceLiveDataImpl3.mSliceCallback);
                        }
                        SliceLiveDataImpl.this.postValue(slice);
                        return;
                    }
                    Objects.requireNonNull(sliceLiveDataImpl.mSliceViewManager);
                    throw null;
                } catch (IllegalArgumentException e) {
                    SliceLiveDataImpl sliceLiveDataImpl4 = SliceLiveDataImpl.this;
                    Objects.requireNonNull(sliceLiveDataImpl4);
                    OnErrorListener onErrorListener = sliceLiveDataImpl4.mListener;
                    if (onErrorListener != null) {
                        onErrorListener.onSliceError();
                    } else {
                        Log.e("SliceLiveData", "Error binding slice", e);
                    }
                    SliceLiveDataImpl.this.postValue(null);
                } catch (Exception e2) {
                    SliceLiveDataImpl sliceLiveDataImpl5 = SliceLiveDataImpl.this;
                    Objects.requireNonNull(sliceLiveDataImpl5);
                    OnErrorListener onErrorListener2 = sliceLiveDataImpl5.mListener;
                    if (onErrorListener2 != null) {
                        onErrorListener2.onSliceError();
                    } else {
                        Log.e("SliceLiveData", "Error binding slice", e2);
                    }
                    SliceLiveDataImpl.this.postValue(null);
                }
            }
        };
        public final SliceLiveData$SliceLiveDataImpl$$ExternalSyntheticLambda0 mSliceCallback = new SliceLiveData$SliceLiveDataImpl$$ExternalSyntheticLambda0(this);
        public final OnErrorListener mListener = null;

        @Override // androidx.lifecycle.LiveData
        public final void onActive() {
            AsyncTask.execute(this.mUpdateSlice);
            Uri uri = this.mUri;
            if (uri != null) {
                this.mSliceViewManager.registerSliceCallback(uri, this.mSliceCallback);
            }
        }

        @Override // androidx.lifecycle.LiveData
        public final void onInactive() {
            Uri uri = this.mUri;
            if (uri != null) {
                SliceViewManagerWrapper sliceViewManagerWrapper = this.mSliceViewManager;
                SliceLiveData$SliceLiveDataImpl$$ExternalSyntheticLambda0 sliceLiveData$SliceLiveDataImpl$$ExternalSyntheticLambda0 = this.mSliceCallback;
                Objects.requireNonNull(sliceViewManagerWrapper);
                synchronized (sliceViewManagerWrapper.mListenerLookup) {
                    SliceViewManagerBase.SliceListenerImpl remove = sliceViewManagerWrapper.mListenerLookup.remove(new Pair(uri, sliceLiveData$SliceLiveDataImpl$$ExternalSyntheticLambda0));
                    if (remove != null) {
                        SliceViewManagerBase.this.mContext.getContentResolver().unregisterContentObserver(remove.mObserver);
                        if (remove.mPinned) {
                            SliceViewManagerBase.this.unpinSlice(remove.mUri);
                            remove.mPinned = false;
                        }
                    }
                }
            }
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [androidx.slice.widget.SliceLiveData$SliceLiveDataImpl$1] */
        public SliceLiveDataImpl(Context context, Uri uri) {
            this.mSliceViewManager = new SliceViewManagerWrapper(context);
            this.mUri = uri;
        }
    }
}
