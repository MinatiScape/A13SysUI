package androidx.emoji2.text;

import android.os.Handler;
import android.os.Looper;
import androidx.collection.ArraySet;
import androidx.emoji2.text.EmojiCompatInitializer;
import androidx.emoji2.text.EmojiProcessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/* loaded from: classes.dex */
public final class EmojiCompat {
    public static final Object INSTANCE_LOCK = new Object();
    public static volatile EmojiCompat sInstance;
    public final EmojiProcessor.DefaultGlyphChecker mGlyphChecker;
    public final CompatInternal19 mHelper;
    public final ReentrantReadWriteLock mInitLock;
    public volatile int mLoadState;
    public final int mMetadataLoadStrategy;
    public final MetadataRepoLoader mMetadataLoader;
    public final Handler mMainHandler = new Handler(Looper.getMainLooper());
    public final ArraySet mInitCallbacks = new ArraySet(0);

    /* loaded from: classes.dex */
    public interface GlyphChecker {
    }

    /* loaded from: classes.dex */
    public static abstract class InitCallback {
        public void onFailed() {
        }

        public void onInitialized() {
        }
    }

    /* loaded from: classes.dex */
    public static class ListenerDispatcher implements Runnable {
        public final ArrayList mInitCallbacks;
        public final int mLoadState;
        public final Throwable mThrowable;

        @Override // java.lang.Runnable
        public final void run() {
            int size = this.mInitCallbacks.size();
            int i = 0;
            if (this.mLoadState != 1) {
                while (i < size) {
                    ((InitCallback) this.mInitCallbacks.get(i)).onFailed();
                    i++;
                }
                return;
            }
            while (i < size) {
                ((InitCallback) this.mInitCallbacks.get(i)).onInitialized();
                i++;
            }
        }

        public ListenerDispatcher(List list, int i, Throwable th) {
            Objects.requireNonNull(list, "initCallbacks cannot be null");
            this.mInitCallbacks = new ArrayList(list);
            this.mLoadState = i;
            this.mThrowable = th;
        }
    }

    /* loaded from: classes.dex */
    public interface MetadataRepoLoader {
        void load(MetadataRepoLoaderCallback metadataRepoLoaderCallback);
    }

    /* loaded from: classes.dex */
    public static abstract class MetadataRepoLoaderCallback {
        public abstract void onFailed(Throwable th);

        public abstract void onLoaded(MetadataRepo metadataRepo);
    }

    /* loaded from: classes.dex */
    public static class SpanFactory {
    }

    /* loaded from: classes.dex */
    public static class CompatInternal {
        public final EmojiCompat mEmojiCompat;

        public CompatInternal(EmojiCompat emojiCompat) {
            this.mEmojiCompat = emojiCompat;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Config {
        public final MetadataRepoLoader mMetadataLoader;
        public int mMetadataLoadStrategy = 0;
        public EmojiProcessor.DefaultGlyphChecker mGlyphChecker = new EmojiProcessor.DefaultGlyphChecker();

        public Config(MetadataRepoLoader metadataRepoLoader) {
            this.mMetadataLoader = metadataRepoLoader;
        }
    }

    public static EmojiCompat get() {
        EmojiCompat emojiCompat;
        boolean z;
        synchronized (INSTANCE_LOCK) {
            emojiCompat = sInstance;
            if (emojiCompat != null) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                throw new IllegalStateException("EmojiCompat is not initialized.\n\nYou must initialize EmojiCompat prior to referencing the EmojiCompat instance.\n\nThe most likely cause of this error is disabling the EmojiCompatInitializer\neither explicitly in AndroidManifest.xml, or by including\nandroidx.emoji2:emoji2-bundled.\n\nAutomatic initialization is typically performed by EmojiCompatInitializer. If\nyou are not expecting to initialize EmojiCompat manually in your application,\nplease check to ensure it has not been removed from your APK's manifest. You can\ndo this in Android Studio using Build > Analyze APK.\n\nIn the APK Analyzer, ensure that the startup entry for\nEmojiCompatInitializer and InitializationProvider is present in\n AndroidManifest.xml. If it is missing or contains tools:node=\"remove\", and you\nintend to use automatic configuration, verify:\n\n  1. Your application does not include emoji2-bundled\n  2. All modules do not contain an exclusion manifest rule for\n     EmojiCompatInitializer or InitializationProvider. For more information\n     about manifest exclusions see the documentation for the androidx startup\n     library.\n\nIf you intend to use emoji2-bundled, please call EmojiCompat.init. You can\nlearn more in the documentation for BundledEmojiCompatConfig.\n\nIf you intended to perform manual configuration, it is recommended that you call\nEmojiCompat.init immediately on application startup.\n\nIf you still cannot resolve this issue, please open a bug with your specific\nconfiguration to help improve error message.");
            }
        }
        return emojiCompat;
    }

    public final int getLoadState() {
        this.mInitLock.readLock().lock();
        try {
            return this.mLoadState;
        } finally {
            this.mInitLock.readLock().unlock();
        }
    }

    public final void load() {
        boolean z;
        boolean z2 = true;
        if (this.mMetadataLoadStrategy == 1) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            if (getLoadState() != 1) {
                z2 = false;
            }
            if (!z2) {
                this.mInitLock.writeLock().lock();
                try {
                    if (this.mLoadState != 0) {
                        this.mLoadState = 0;
                        this.mInitLock.writeLock().unlock();
                        CompatInternal19 compatInternal19 = this.mHelper;
                        Objects.requireNonNull(compatInternal19);
                        try {
                            compatInternal19.mEmojiCompat.mMetadataLoader.load(new CompatInternal19.AnonymousClass1());
                        } catch (Throwable th) {
                            compatInternal19.mEmojiCompat.onMetadataLoadFailed(th);
                        }
                    }
                } finally {
                    this.mInitLock.writeLock().unlock();
                }
            }
        } else {
            throw new IllegalStateException("Set metadataLoadStrategy to LOAD_STRATEGY_MANUAL to execute manual loading");
        }
    }

    /* JADX WARN: Finally extract failed */
    public final void onMetadataLoadFailed(Throwable th) {
        ArrayList arrayList = new ArrayList();
        this.mInitLock.writeLock().lock();
        try {
            this.mLoadState = 2;
            arrayList.addAll(this.mInitCallbacks);
            this.mInitCallbacks.clear();
            this.mInitLock.writeLock().unlock();
            this.mMainHandler.post(new ListenerDispatcher(arrayList, this.mLoadState, th));
        } catch (Throwable th2) {
            this.mInitLock.writeLock().unlock();
            throw th2;
        }
    }

    /* JADX WARN: Finally extract failed */
    public final void onMetadataLoadSuccess() {
        ArrayList arrayList = new ArrayList();
        this.mInitLock.writeLock().lock();
        try {
            this.mLoadState = 1;
            arrayList.addAll(this.mInitCallbacks);
            this.mInitCallbacks.clear();
            this.mInitLock.writeLock().unlock();
            this.mMainHandler.post(new ListenerDispatcher(arrayList, this.mLoadState, null));
        } catch (Throwable th) {
            this.mInitLock.writeLock().unlock();
            throw th;
        }
    }

    public final void registerInitCallback(InitCallback initCallback) {
        Objects.requireNonNull(initCallback, "initCallback cannot be null");
        this.mInitLock.writeLock().lock();
        try {
            if (!(this.mLoadState == 1 || this.mLoadState == 2)) {
                this.mInitCallbacks.add(initCallback);
            }
            this.mMainHandler.post(new ListenerDispatcher(Arrays.asList(initCallback), this.mLoadState, null));
        } finally {
            this.mInitLock.writeLock().unlock();
        }
    }

    public EmojiCompat(EmojiCompatInitializer.BackgroundDefaultConfig backgroundDefaultConfig) {
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        this.mInitLock = reentrantReadWriteLock;
        this.mLoadState = 3;
        MetadataRepoLoader metadataRepoLoader = backgroundDefaultConfig.mMetadataLoader;
        this.mMetadataLoader = metadataRepoLoader;
        int i = backgroundDefaultConfig.mMetadataLoadStrategy;
        this.mMetadataLoadStrategy = i;
        this.mGlyphChecker = backgroundDefaultConfig.mGlyphChecker;
        CompatInternal19 compatInternal19 = new CompatInternal19(this);
        this.mHelper = compatInternal19;
        reentrantReadWriteLock.writeLock().lock();
        if (i == 0) {
            try {
                this.mLoadState = 0;
            } catch (Throwable th) {
                this.mInitLock.writeLock().unlock();
                throw th;
            }
        }
        reentrantReadWriteLock.writeLock().unlock();
        if (getLoadState() == 0) {
            try {
                metadataRepoLoader.load(new CompatInternal19.AnonymousClass1());
            } catch (Throwable th2) {
                compatInternal19.mEmojiCompat.onMetadataLoadFailed(th2);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:101:0x0180, code lost:
        return r13;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0116 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:127:0x00d8 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0091 A[Catch: all -> 0x0181, TryCatch #0 {all -> 0x0181, blocks: (B:30:0x005d, B:33:0x0062, B:35:0x0066, B:37:0x0075, B:38:0x007b, B:40:0x0080, B:42:0x008a, B:44:0x008d, B:46:0x0091, B:48:0x009d, B:49:0x00a0, B:51:0x00ad, B:54:0x00b5, B:59:0x00d2, B:65:0x00de, B:68:0x00ed, B:69:0x00f2, B:71:0x010a, B:73:0x0111, B:74:0x0116, B:76:0x0121, B:78:0x0127, B:80:0x012b, B:82:0x0134, B:84:0x0138, B:89:0x0143, B:92:0x0152, B:93:0x0158), top: B:113:0x005d }] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x012b A[Catch: all -> 0x0181, TryCatch #0 {all -> 0x0181, blocks: (B:30:0x005d, B:33:0x0062, B:35:0x0066, B:37:0x0075, B:38:0x007b, B:40:0x0080, B:42:0x008a, B:44:0x008d, B:46:0x0091, B:48:0x009d, B:49:0x00a0, B:51:0x00ad, B:54:0x00b5, B:59:0x00d2, B:65:0x00de, B:68:0x00ed, B:69:0x00f2, B:71:0x010a, B:73:0x0111, B:74:0x0116, B:76:0x0121, B:78:0x0127, B:80:0x012b, B:82:0x0134, B:84:0x0138, B:89:0x0143, B:92:0x0152, B:93:0x0158), top: B:113:0x005d }] */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0150  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x016e  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0171  */
    /* JADX WARN: Type inference failed for: r0v8, types: [java.lang.CharSequence] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.CharSequence process(java.lang.CharSequence r13, int r14, int r15) {
        /*
            Method dump skipped, instructions count: 420
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.emoji2.text.EmojiCompat.process(java.lang.CharSequence, int, int):java.lang.CharSequence");
    }

    /* loaded from: classes.dex */
    public static final class CompatInternal19 extends CompatInternal {
        public volatile MetadataRepo mMetadataRepo;
        public volatile EmojiProcessor mProcessor;

        /* renamed from: androidx.emoji2.text.EmojiCompat$CompatInternal19$1  reason: invalid class name */
        /* loaded from: classes.dex */
        public final class AnonymousClass1 extends MetadataRepoLoaderCallback {
            public AnonymousClass1() {
            }

            @Override // androidx.emoji2.text.EmojiCompat.MetadataRepoLoaderCallback
            public final void onFailed(Throwable th) {
                CompatInternal19.this.mEmojiCompat.onMetadataLoadFailed(th);
            }

            @Override // androidx.emoji2.text.EmojiCompat.MetadataRepoLoaderCallback
            public final void onLoaded(MetadataRepo metadataRepo) {
                CompatInternal19 compatInternal19 = CompatInternal19.this;
                Objects.requireNonNull(compatInternal19);
                compatInternal19.mMetadataRepo = metadataRepo;
                compatInternal19.mProcessor = new EmojiProcessor(compatInternal19.mMetadataRepo, new SpanFactory(), compatInternal19.mEmojiCompat.mGlyphChecker);
                compatInternal19.mEmojiCompat.onMetadataLoadSuccess();
            }
        }

        public CompatInternal19(EmojiCompat emojiCompat) {
            super(emojiCompat);
        }
    }
}
