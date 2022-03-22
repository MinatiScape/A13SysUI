package androidx.arch.core.executor;

import android.os.Handler;
import android.os.Looper;
import androidx.fragment.app.FragmentContainer;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ArchTaskExecutor extends FragmentContainer {
    public static volatile ArchTaskExecutor sInstance;
    public DefaultTaskExecutor mDefaultTaskExecutor;
    public DefaultTaskExecutor mDelegate;

    public static ArchTaskExecutor getInstance() {
        if (sInstance != null) {
            return sInstance;
        }
        synchronized (ArchTaskExecutor.class) {
            if (sInstance == null) {
                sInstance = new ArchTaskExecutor();
            }
        }
        return sInstance;
    }

    public final void postToMainThread(Runnable runnable) {
        DefaultTaskExecutor defaultTaskExecutor = this.mDelegate;
        Objects.requireNonNull(defaultTaskExecutor);
        if (defaultTaskExecutor.mMainHandler == null) {
            synchronized (defaultTaskExecutor.mLock) {
                if (defaultTaskExecutor.mMainHandler == null) {
                    defaultTaskExecutor.mMainHandler = Handler.createAsync(Looper.getMainLooper());
                }
            }
        }
        defaultTaskExecutor.mMainHandler.post(runnable);
    }

    public ArchTaskExecutor() {
        DefaultTaskExecutor defaultTaskExecutor = new DefaultTaskExecutor();
        this.mDefaultTaskExecutor = defaultTaskExecutor;
        this.mDelegate = defaultTaskExecutor;
    }
}
