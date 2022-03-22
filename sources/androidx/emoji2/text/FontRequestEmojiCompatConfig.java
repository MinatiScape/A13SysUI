package androidx.emoji2.text;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Handler;
import androidx.core.provider.FontProvider;
import androidx.core.provider.FontRequest;
import androidx.core.provider.FontsContractCompat$FontFamilyResult;
import androidx.core.provider.FontsContractCompat$FontInfo;
import androidx.emoji2.text.EmojiCompat;
import com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda0;
import com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda10;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public final class FontRequestEmojiCompatConfig extends EmojiCompat.Config {
    public static final FontProviderHelper DEFAULT_FONTS_CONTRACT = new FontProviderHelper();

    /* loaded from: classes.dex */
    public static class FontProviderHelper {
    }

    /* loaded from: classes.dex */
    public static class FontRequestMetadataLoader implements EmojiCompat.MetadataRepoLoader {
        public EmojiCompat.MetadataRepoLoaderCallback mCallback;
        public final Context mContext;
        public Executor mExecutor;
        public final FontProviderHelper mFontProviderHelper;
        public final Object mLock = new Object();
        public Handler mMainHandler;
        public VolumeDialogImpl$$ExternalSyntheticLambda10 mMainHandlerLoadCallback;
        public ThreadPoolExecutor mMyThreadPoolExecutor;
        public AnonymousClass1 mObserver;
        public final FontRequest mRequest;

        /* renamed from: androidx.emoji2.text.FontRequestEmojiCompatConfig$FontRequestMetadataLoader$1  reason: invalid class name */
        /* loaded from: classes.dex */
        public final class AnonymousClass1 extends ContentObserver {
        }

        public FontRequestMetadataLoader(Context context, FontRequest fontRequest) {
            FontProviderHelper fontProviderHelper = FontRequestEmojiCompatConfig.DEFAULT_FONTS_CONTRACT;
            Objects.requireNonNull(context, "Context cannot be null");
            this.mContext = context.getApplicationContext();
            this.mRequest = fontRequest;
            this.mFontProviderHelper = fontProviderHelper;
        }

        public final void cleanUp() {
            synchronized (this.mLock) {
                this.mCallback = null;
                AnonymousClass1 r2 = this.mObserver;
                if (r2 != null) {
                    FontProviderHelper fontProviderHelper = this.mFontProviderHelper;
                    Context context = this.mContext;
                    Objects.requireNonNull(fontProviderHelper);
                    context.getContentResolver().unregisterContentObserver(r2);
                    this.mObserver = null;
                }
                Handler handler = this.mMainHandler;
                if (handler != null) {
                    handler.removeCallbacks(this.mMainHandlerLoadCallback);
                }
                this.mMainHandler = null;
                ThreadPoolExecutor threadPoolExecutor = this.mMyThreadPoolExecutor;
                if (threadPoolExecutor != null) {
                    threadPoolExecutor.shutdown();
                }
                this.mExecutor = null;
                this.mMyThreadPoolExecutor = null;
            }
        }

        @Override // androidx.emoji2.text.EmojiCompat.MetadataRepoLoader
        public final void load(EmojiCompat.MetadataRepoLoaderCallback metadataRepoLoaderCallback) {
            synchronized (this.mLock) {
                this.mCallback = metadataRepoLoaderCallback;
            }
            loadInternal();
        }

        public final void loadInternal() {
            synchronized (this.mLock) {
                if (this.mCallback != null) {
                    if (this.mExecutor == null) {
                        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 1, 15L, TimeUnit.SECONDS, new LinkedBlockingDeque(), new ConcurrencyHelpers$$ExternalSyntheticLambda0("emojiCompat"));
                        threadPoolExecutor.allowCoreThreadTimeOut(true);
                        this.mMyThreadPoolExecutor = threadPoolExecutor;
                        this.mExecutor = threadPoolExecutor;
                    }
                    this.mExecutor.execute(new AccessPoint$$ExternalSyntheticLambda0(this, 1));
                }
            }
        }

        public final FontsContractCompat$FontInfo retrieveFontInfo() {
            try {
                FontProviderHelper fontProviderHelper = this.mFontProviderHelper;
                Context context = this.mContext;
                FontRequest fontRequest = this.mRequest;
                Objects.requireNonNull(fontProviderHelper);
                FontsContractCompat$FontFamilyResult fontFamilyResult = FontProvider.getFontFamilyResult(context, fontRequest);
                if (fontFamilyResult.mStatusCode == 0) {
                    FontsContractCompat$FontInfo[] fontsContractCompat$FontInfoArr = fontFamilyResult.mFonts;
                    if (fontsContractCompat$FontInfoArr != null && fontsContractCompat$FontInfoArr.length != 0) {
                        return fontsContractCompat$FontInfoArr[0];
                    }
                    throw new RuntimeException("fetchFonts failed (empty result)");
                }
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("fetchFonts failed (");
                m.append(fontFamilyResult.mStatusCode);
                m.append(")");
                throw new RuntimeException(m.toString());
            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException("provider not found", e);
            }
        }
    }

    public FontRequestEmojiCompatConfig(Context context, FontRequest fontRequest) {
        super(new FontRequestMetadataLoader(context, fontRequest));
    }
}
