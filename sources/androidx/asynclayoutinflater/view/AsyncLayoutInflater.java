package androidx.asynclayoutinflater.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.util.Pools$SynchronizedPool;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
/* loaded from: classes.dex */
public final class AsyncLayoutInflater {
    public BasicInflater mInflater;
    public AnonymousClass1 mHandlerCallback = new Handler.Callback() { // from class: androidx.asynclayoutinflater.view.AsyncLayoutInflater.1
        @Override // android.os.Handler.Callback
        public final boolean handleMessage(Message message) {
            InflateRequest inflateRequest = (InflateRequest) message.obj;
            if (inflateRequest.view == null) {
                inflateRequest.view = AsyncLayoutInflater.this.mInflater.inflate(inflateRequest.resid, inflateRequest.parent, false);
            }
            inflateRequest.callback.onInflateFinished(inflateRequest.view, inflateRequest.parent);
            InflateThread inflateThread = AsyncLayoutInflater.this.mInflateThread;
            Objects.requireNonNull(inflateThread);
            inflateRequest.callback = null;
            inflateRequest.inflater = null;
            inflateRequest.parent = null;
            inflateRequest.resid = 0;
            inflateRequest.view = null;
            inflateThread.mRequestPool.release(inflateRequest);
            return true;
        }
    };
    public Handler mHandler = new Handler(this.mHandlerCallback);
    public InflateThread mInflateThread = InflateThread.sInstance;

    /* loaded from: classes.dex */
    public static class BasicInflater extends LayoutInflater {
        public static final String[] sClassPrefixList = {"android.widget.", "android.webkit.", "android.app."};

        @Override // android.view.LayoutInflater
        public final LayoutInflater cloneInContext(Context context) {
            return new BasicInflater(context);
        }

        @Override // android.view.LayoutInflater
        public final View onCreateView(String str, AttributeSet attributeSet) throws ClassNotFoundException {
            View createView;
            String[] strArr = sClassPrefixList;
            for (int i = 0; i < 3; i++) {
                try {
                    createView = createView(str, strArr[i], attributeSet);
                } catch (ClassNotFoundException unused) {
                }
                if (createView != null) {
                    return createView;
                }
            }
            return super.onCreateView(str, attributeSet);
        }

        public BasicInflater(Context context) {
            super(context);
        }
    }

    /* loaded from: classes.dex */
    public static class InflateRequest {
        public OnInflateFinishedListener callback;
        public AsyncLayoutInflater inflater;
        public ViewGroup parent;
        public int resid;
        public View view;
    }

    /* loaded from: classes.dex */
    public static class InflateThread extends Thread {
        public static final InflateThread sInstance;
        public ArrayBlockingQueue<InflateRequest> mQueue = new ArrayBlockingQueue<>(10);
        public Pools$SynchronizedPool<InflateRequest> mRequestPool = new Pools$SynchronizedPool<>(10);

        static {
            InflateThread inflateThread = new InflateThread();
            sInstance = inflateThread;
            inflateThread.setName("AsyncLayoutInflator");
            inflateThread.start();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public final void run() {
            while (true) {
                try {
                    InflateRequest take = this.mQueue.take();
                    try {
                        take.view = take.inflater.mInflater.inflate(take.resid, take.parent, false);
                    } catch (RuntimeException e) {
                        Log.w("AsyncLayoutInflater", "Failed to inflate resource in the background! Retrying on the UI thread", e);
                    }
                    Message.obtain(take.inflater.mHandler, 0, take).sendToTarget();
                } catch (InterruptedException e2) {
                    Log.w("AsyncLayoutInflater", e2);
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public interface OnInflateFinishedListener {
        void onInflateFinished(View view, ViewGroup viewGroup);
    }

    public final void inflate(int i, ViewGroup viewGroup, OnInflateFinishedListener onInflateFinishedListener) {
        Objects.requireNonNull(onInflateFinishedListener, "callback argument may not be null!");
        InflateThread inflateThread = this.mInflateThread;
        Objects.requireNonNull(inflateThread);
        InflateRequest acquire = inflateThread.mRequestPool.acquire();
        if (acquire == null) {
            acquire = new InflateRequest();
        }
        acquire.inflater = this;
        acquire.resid = i;
        acquire.parent = viewGroup;
        acquire.callback = onInflateFinishedListener;
        InflateThread inflateThread2 = this.mInflateThread;
        Objects.requireNonNull(inflateThread2);
        try {
            inflateThread2.mQueue.put(acquire);
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed to enqueue async inflate request", e);
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [androidx.asynclayoutinflater.view.AsyncLayoutInflater$1] */
    public AsyncLayoutInflater(Context context) {
        this.mInflater = new BasicInflater(context);
    }
}
