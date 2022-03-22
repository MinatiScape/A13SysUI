package com.airbnb.lottie;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.airbnb.lottie.utils.LogcatLogger;
import com.airbnb.lottie.utils.Logger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
/* loaded from: classes.dex */
public final class LottieTask<T> {
    public static ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    public final LinkedHashSet failureListeners;
    public final Handler handler;
    public volatile LottieResult<T> result;
    public final LinkedHashSet successListeners;

    /* loaded from: classes.dex */
    public class LottieFutureTask extends FutureTask<LottieResult<T>> {
        public LottieFutureTask(Callable<LottieResult<T>> callable) {
            super(callable);
        }

        @Override // java.util.concurrent.FutureTask
        public final void done() {
            if (!isCancelled()) {
                try {
                    LottieTask.this.setResult(get());
                } catch (InterruptedException | ExecutionException e) {
                    LottieTask.this.setResult(new LottieResult<>(e));
                }
            }
        }
    }

    public LottieTask() {
        throw null;
    }

    public LottieTask(Callable<LottieResult<T>> callable) {
        this.successListeners = new LinkedHashSet(1);
        this.failureListeners = new LinkedHashSet(1);
        this.handler = new Handler(Looper.getMainLooper());
        this.result = null;
        EXECUTOR.execute(new LottieFutureTask(callable));
    }

    public final synchronized LottieTask<T> addFailureListener(LottieListener<Throwable> lottieListener) {
        if (this.result != null) {
            LottieResult<T> lottieResult = this.result;
            Objects.requireNonNull(lottieResult);
            if (lottieResult.exception != null) {
                LottieResult<T> lottieResult2 = this.result;
                Objects.requireNonNull(lottieResult2);
                lottieListener.onResult(lottieResult2.exception);
            }
        }
        this.failureListeners.add(lottieListener);
        return this;
    }

    public final synchronized LottieTask<T> addListener(LottieListener<T> lottieListener) {
        if (this.result != null) {
            LottieResult<T> lottieResult = this.result;
            Objects.requireNonNull(lottieResult);
            if (lottieResult.value != null) {
                LottieResult<T> lottieResult2 = this.result;
                Objects.requireNonNull(lottieResult2);
                lottieListener.onResult(lottieResult2.value);
            }
        }
        this.successListeners.add(lottieListener);
        return this;
    }

    public final void setResult(LottieResult<T> lottieResult) {
        if (this.result == null) {
            this.result = lottieResult;
            this.handler.post(new Runnable() { // from class: com.airbnb.lottie.LottieTask.1
                @Override // java.lang.Runnable
                public final void run() {
                    if (LottieTask.this.result != null) {
                        LottieResult<T> lottieResult2 = LottieTask.this.result;
                        Objects.requireNonNull(lottieResult2);
                        T t = lottieResult2.value;
                        if (t != null) {
                            LottieTask lottieTask = LottieTask.this;
                            Objects.requireNonNull(lottieTask);
                            synchronized (lottieTask) {
                                Iterator it = new ArrayList(lottieTask.successListeners).iterator();
                                while (it.hasNext()) {
                                    ((LottieListener) it.next()).onResult(t);
                                }
                            }
                            return;
                        }
                        LottieTask lottieTask2 = LottieTask.this;
                        Throwable th = lottieResult2.exception;
                        Objects.requireNonNull(lottieTask2);
                        synchronized (lottieTask2) {
                            ArrayList arrayList = new ArrayList(lottieTask2.failureListeners);
                            if (arrayList.isEmpty()) {
                                Objects.requireNonNull(Logger.INSTANCE);
                                HashSet hashSet = LogcatLogger.loggedMessages;
                                if (!hashSet.contains("Lottie encountered an error but no failure listener was added:")) {
                                    Log.w("LOTTIE", "Lottie encountered an error but no failure listener was added:", th);
                                    hashSet.add("Lottie encountered an error but no failure listener was added:");
                                }
                                return;
                            }
                            Iterator it2 = arrayList.iterator();
                            while (it2.hasNext()) {
                                ((LottieListener) it2.next()).onResult(th);
                            }
                        }
                    }
                }
            });
            return;
        }
        throw new IllegalStateException("A task may only be set once.");
    }
}
