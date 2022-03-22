package androidx.emoji2.text;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.util.Log;
import androidx.core.provider.FontRequest;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.emoji2.text.EmojiCompat;
import androidx.emoji2.text.EmojiCompatInitializer;
import androidx.emoji2.text.FontRequestEmojiCompatConfig;
import com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class EmojiCompatInitializer$BackgroundDefaultLoader$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ Object f$2;

    public /* synthetic */ EmojiCompatInitializer$BackgroundDefaultLoader$$ExternalSyntheticLambda0(Object obj, Object obj2, Object obj3, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
        this.f$2 = obj3;
    }

    /* JADX WARN: Type inference failed for: r3v3, types: [androidx.emoji2.text.DefaultEmojiCompatConfig$DefaultEmojiCompatConfigFactory] */
    @Override // java.lang.Runnable
    public final void run() {
        FontRequestEmojiCompatConfig fontRequestEmojiCompatConfig = null;
        switch (this.$r8$classId) {
            case 0:
                EmojiCompatInitializer.BackgroundDefaultLoader backgroundDefaultLoader = (EmojiCompatInitializer.BackgroundDefaultLoader) this.f$0;
                final EmojiCompat.MetadataRepoLoaderCallback metadataRepoLoaderCallback = (EmojiCompat.MetadataRepoLoaderCallback) this.f$1;
                final ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) this.f$2;
                Objects.requireNonNull(backgroundDefaultLoader);
                try {
                    Context context = backgroundDefaultLoader.mContext;
                    FontRequest queryForDefaultFontRequest = new Object() { // from class: androidx.emoji2.text.DefaultEmojiCompatConfig$DefaultEmojiCompatConfigFactory
                        public final DefaultEmojiCompatConfig$DefaultEmojiCompatConfigHelper mHelper = new DefaultEmojiCompatConfig$DefaultEmojiCompatConfigHelper_API19() { // from class: androidx.emoji2.text.DefaultEmojiCompatConfig$DefaultEmojiCompatConfigHelper_API28
                            @Override // androidx.emoji2.text.DefaultEmojiCompatConfig$DefaultEmojiCompatConfigHelper
                            public final Signature[] getSigningSignatures(PackageManager packageManager, String str) throws PackageManager.NameNotFoundException {
                                return packageManager.getPackageInfo(str, 64).signatures;
                            }
                        };

                        public FontRequest queryForDefaultFontRequest(Context context2) {
                            ProviderInfo providerInfo;
                            ApplicationInfo applicationInfo;
                            PackageManager packageManager = context2.getPackageManager();
                            Objects.requireNonNull(packageManager, "Package manager required to locate emoji font provider");
                            Iterator it = this.mHelper.queryIntentContentProviders(packageManager, new Intent("androidx.content.action.LOAD_EMOJI_FONT")).iterator();
                            while (true) {
                                if (!it.hasNext()) {
                                    providerInfo = null;
                                    break;
                                }
                                providerInfo = this.mHelper.getProviderInfo((ResolveInfo) it.next());
                                boolean z = true;
                                if (providerInfo == null || (applicationInfo = providerInfo.applicationInfo) == null || (applicationInfo.flags & 1) != 1) {
                                    z = false;
                                    continue;
                                }
                                if (z) {
                                    break;
                                }
                            }
                            if (providerInfo == null) {
                                return null;
                            }
                            try {
                                String str = providerInfo.authority;
                                String str2 = providerInfo.packageName;
                                Signature[] signingSignatures = this.mHelper.getSigningSignatures(packageManager, str2);
                                ArrayList arrayList = new ArrayList();
                                for (Signature signature : signingSignatures) {
                                    arrayList.add(signature.toByteArray());
                                }
                                return new FontRequest(str, str2, "emojicompat-emoji-font", Collections.singletonList(arrayList));
                            } catch (PackageManager.NameNotFoundException e) {
                                Log.wtf("emoji2.text.DefaultEmojiConfig", e);
                                return null;
                            }
                        }
                    }.queryForDefaultFontRequest(context);
                    if (queryForDefaultFontRequest != null) {
                        fontRequestEmojiCompatConfig = new FontRequestEmojiCompatConfig(context, queryForDefaultFontRequest);
                    }
                    if (fontRequestEmojiCompatConfig != null) {
                        FontRequestEmojiCompatConfig.FontRequestMetadataLoader fontRequestMetadataLoader = (FontRequestEmojiCompatConfig.FontRequestMetadataLoader) fontRequestEmojiCompatConfig.mMetadataLoader;
                        Objects.requireNonNull(fontRequestMetadataLoader);
                        synchronized (fontRequestMetadataLoader.mLock) {
                            fontRequestMetadataLoader.mExecutor = threadPoolExecutor;
                        }
                        fontRequestEmojiCompatConfig.mMetadataLoader.load(new EmojiCompat.MetadataRepoLoaderCallback() { // from class: androidx.emoji2.text.EmojiCompatInitializer.BackgroundDefaultLoader.1
                            @Override // androidx.emoji2.text.EmojiCompat.MetadataRepoLoaderCallback
                            public final void onFailed(Throwable th) {
                                try {
                                    EmojiCompat.MetadataRepoLoaderCallback.this.onFailed(th);
                                } finally {
                                    threadPoolExecutor.shutdown();
                                }
                            }

                            @Override // androidx.emoji2.text.EmojiCompat.MetadataRepoLoaderCallback
                            public final void onLoaded(MetadataRepo metadataRepo) {
                                try {
                                    EmojiCompat.MetadataRepoLoaderCallback.this.onLoaded(metadataRepo);
                                } finally {
                                    threadPoolExecutor.shutdown();
                                }
                            }
                        });
                        return;
                    }
                    throw new RuntimeException("EmojiCompat font provider not available on this device.");
                } catch (Throwable th) {
                    metadataRepoLoaderCallback.onFailed(th);
                    threadPoolExecutor.shutdown();
                    return;
                }
            default:
                PhysicsAnimationLayout.PhysicsPropertyAnimator physicsPropertyAnimator = (PhysicsAnimationLayout.PhysicsPropertyAnimator) this.f$0;
                SpringAnimation springAnimation = (SpringAnimation) this.f$1;
                SpringAnimation springAnimation2 = (SpringAnimation) this.f$2;
                Objects.requireNonNull(physicsPropertyAnimator);
                Objects.requireNonNull(springAnimation);
                if (!springAnimation.mRunning) {
                    Objects.requireNonNull(springAnimation2);
                    if (!springAnimation2.mRunning) {
                        Runnable[] runnableArr = physicsPropertyAnimator.mPositionEndActions;
                        if (runnableArr != null) {
                            for (Runnable runnable : runnableArr) {
                                runnable.run();
                            }
                        }
                        physicsPropertyAnimator.mPositionEndActions = null;
                        return;
                    }
                    return;
                }
                return;
        }
    }
}
