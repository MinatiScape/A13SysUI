package androidx.emoji2.text;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Trace;
import androidx.emoji2.text.EmojiCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleInitializer;
import androidx.startup.AppInitializer;
import androidx.startup.Initializer;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public class EmojiCompatInitializer implements Initializer<Boolean> {

    /* loaded from: classes.dex */
    public static class BackgroundDefaultConfig extends EmojiCompat.Config {
        public BackgroundDefaultConfig(Context context) {
            super(new BackgroundDefaultLoader(context));
            this.mMetadataLoadStrategy = 1;
        }
    }

    /* loaded from: classes.dex */
    public static class BackgroundDefaultLoader implements EmojiCompat.MetadataRepoLoader {
        public final Context mContext;

        @Override // androidx.emoji2.text.EmojiCompat.MetadataRepoLoader
        public final void load(EmojiCompat.MetadataRepoLoaderCallback metadataRepoLoaderCallback) {
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 1, 15L, TimeUnit.SECONDS, new LinkedBlockingDeque(), new ConcurrencyHelpers$$ExternalSyntheticLambda0("EmojiCompatInitializer"));
            threadPoolExecutor.allowCoreThreadTimeOut(true);
            threadPoolExecutor.execute(new EmojiCompatInitializer$BackgroundDefaultLoader$$ExternalSyntheticLambda0(this, metadataRepoLoaderCallback, threadPoolExecutor, 0));
        }

        public BackgroundDefaultLoader(Context context) {
            this.mContext = context.getApplicationContext();
        }
    }

    /* loaded from: classes.dex */
    public static class LoadEmojiCompatRunnable implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            boolean z;
            try {
                Trace.beginSection("EmojiCompat.EmojiCompatInitializer.run");
                if (EmojiCompat.sInstance != null) {
                    z = true;
                } else {
                    z = false;
                }
                if (z) {
                    EmojiCompat.get().load();
                }
            } finally {
                Trace.endSection();
            }
        }
    }

    @Override // androidx.startup.Initializer
    /* renamed from: create  reason: avoid collision after fix types in other method */
    public final void create2(Context context) {
        BackgroundDefaultConfig backgroundDefaultConfig = new BackgroundDefaultConfig(context);
        if (EmojiCompat.sInstance == null) {
            synchronized (EmojiCompat.INSTANCE_LOCK) {
                if (EmojiCompat.sInstance == null) {
                    EmojiCompat.sInstance = new EmojiCompat(backgroundDefaultConfig);
                }
            }
        }
        if (AppInitializer.sInstance == null) {
            synchronized (AppInitializer.sLock) {
                if (AppInitializer.sInstance == null) {
                    AppInitializer.sInstance = new AppInitializer(context);
                }
            }
        }
        AppInitializer appInitializer = AppInitializer.sInstance;
        Objects.requireNonNull(appInitializer);
        final Lifecycle lifecycle = ((LifecycleOwner) appInitializer.doInitialize(ProcessLifecycleInitializer.class, new HashSet())).getLifecycle();
        lifecycle.addObserver(new LifecycleObserver() { // from class: androidx.emoji2.text.EmojiCompatInitializer.1
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            public void onResume() {
                Objects.requireNonNull(EmojiCompatInitializer.this);
                Handler.createAsync(Looper.getMainLooper()).postDelayed(new LoadEmojiCompatRunnable(), 500L);
                lifecycle.removeObserver(this);
            }
        });
    }

    @Override // androidx.startup.Initializer
    public final List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.singletonList(ProcessLifecycleInitializer.class);
    }
}
