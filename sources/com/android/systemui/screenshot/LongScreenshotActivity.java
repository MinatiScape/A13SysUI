package com.android.systemui.screenshot;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.HardwareRenderer;
import android.graphics.Matrix;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.RenderNode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ScrollCaptureResponse;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda8;
import com.android.settingslib.widget.LayoutPreference$$ExternalSyntheticLambda0;
import com.android.systemui.biometrics.AuthBiometricView$$ExternalSyntheticLambda5;
import com.android.systemui.qs.QSPanel$$ExternalSyntheticLambda0;
import com.android.systemui.qs.tiles.ScreenRecordTile$$ExternalSyntheticLambda1;
import com.android.systemui.screenshot.CropView;
import com.android.systemui.screenshot.ImageExporter;
import com.android.systemui.screenshot.LongScreenshotActivity;
import com.android.systemui.screenshot.ScrollCaptureController;
import com.android.systemui.wallet.ui.WalletView$$ExternalSyntheticLambda0;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class LongScreenshotActivity extends Activity {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final Executor mBackgroundExecutor;
    public CallbackToFutureAdapter.SafeFuture mCacheLoadFuture;
    public CallbackToFutureAdapter.SafeFuture mCacheSaveFuture;
    public View mCancel;
    public CropView mCropView;
    public View mEdit;
    public ImageView mEnterTransitionView;
    public final ImageExporter mImageExporter;
    public ScrollCaptureController.LongScreenshot mLongScreenshot;
    public final LongScreenshotData mLongScreenshotHolder;
    public MagnifierView mMagnifierView;
    public Bitmap mOutputBitmap;
    public ImageView mPreview;
    public View mSave;
    public File mSavedImagePath;
    public ScrollCaptureResponse mScrollCaptureResponse;
    public View mShare;
    public boolean mTransitionStarted;
    public ImageView mTransitionView;
    public final UiEventLogger mUiEventLogger;
    public final Executor mUiExecutor;

    /* renamed from: com.android.systemui.screenshot.LongScreenshotActivity$1 */
    /* loaded from: classes.dex */
    public final class AnonymousClass1 implements ViewTreeObserver.OnPreDrawListener {
        public final /* synthetic */ float val$bottomFraction;
        public final /* synthetic */ float val$topFraction;

        public AnonymousClass1(float f, float f2) {
            LongScreenshotActivity.this = r1;
            this.val$topFraction = f;
            this.val$bottomFraction = f2;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public final boolean onPreDraw() {
            LongScreenshotActivity.this.mEnterTransitionView.getViewTreeObserver().removeOnPreDrawListener(this);
            LongScreenshotActivity.this.updateImageDimensions();
            ImageView imageView = LongScreenshotActivity.this.mEnterTransitionView;
            final float f = this.val$topFraction;
            final float f2 = this.val$bottomFraction;
            imageView.post(new Runnable() { // from class: com.android.systemui.screenshot.LongScreenshotActivity$1$$ExternalSyntheticLambda1
                /* JADX WARN: Multi-variable type inference failed */
                /* JADX WARN: Type inference failed for: r4v1, types: [com.android.systemui.screenshot.LongScreenshotActivity$1$$ExternalSyntheticLambda0] */
                @Override // java.lang.Runnable
                public final void run() {
                    final LongScreenshotActivity.AnonymousClass1 r0 = LongScreenshotActivity.AnonymousClass1.this;
                    final float f3 = f;
                    final float f4 = f2;
                    Objects.requireNonNull(r0);
                    Rect rect = new Rect();
                    LongScreenshotActivity.this.mEnterTransitionView.getBoundsOnScreen(rect);
                    LongScreenshotData longScreenshotData = LongScreenshotActivity.this.mLongScreenshotHolder;
                    Objects.requireNonNull(longScreenshotData);
                    longScreenshotData.mTransitionDestinationCallback.getAndSet(null).setTransitionDestination(rect, new Runnable() { // from class: com.android.systemui.screenshot.LongScreenshotActivity$1$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            LongScreenshotActivity.AnonymousClass1 r02 = LongScreenshotActivity.AnonymousClass1.this;
                            float f5 = f3;
                            float f6 = f4;
                            Objects.requireNonNull(r02);
                            LongScreenshotActivity.this.mPreview.animate().alpha(1.0f);
                            LongScreenshotActivity.this.mCropView.setBoundaryPosition(CropView.CropBoundary.TOP, f5);
                            LongScreenshotActivity.this.mCropView.setBoundaryPosition(CropView.CropBoundary.BOTTOM, f6);
                            final CropView cropView = LongScreenshotActivity.this.mCropView;
                            Objects.requireNonNull(cropView);
                            cropView.mEntranceInterpolation = 0.0f;
                            ValueAnimator valueAnimator = new ValueAnimator();
                            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.screenshot.CropView$$ExternalSyntheticLambda0
                                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                                    CropView cropView2 = CropView.this;
                                    int i = CropView.$r8$clinit;
                                    Objects.requireNonNull(cropView2);
                                    cropView2.mEntranceInterpolation = valueAnimator2.getAnimatedFraction();
                                    cropView2.invalidate();
                                }
                            });
                            valueAnimator.setFloatValues(0.0f, 1.0f);
                            valueAnimator.setDuration(750L);
                            valueAnimator.setInterpolator(new FastOutSlowInInterpolator());
                            valueAnimator.start();
                            LongScreenshotActivity.this.mCropView.setVisibility(0);
                            LongScreenshotActivity.this.setButtonsEnabled(true);
                        }
                    });
                }
            });
            return true;
        }
    }

    /* loaded from: classes.dex */
    public enum PendingAction {
        SHARE,
        EDIT,
        SAVE
    }

    public final void onCachedImageLoaded(ImageLoader$Result imageLoader$Result) {
        this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_ACTIVITY_CACHED_IMAGE_LOADED);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), imageLoader$Result.bitmap);
        this.mPreview.setImageDrawable(bitmapDrawable);
        this.mPreview.setAlpha(1.0f);
        MagnifierView magnifierView = this.mMagnifierView;
        int width = imageLoader$Result.bitmap.getWidth();
        int height = imageLoader$Result.bitmap.getHeight();
        Objects.requireNonNull(magnifierView);
        magnifierView.mDrawable = bitmapDrawable;
        bitmapDrawable.setBounds(0, 0, width, height);
        magnifierView.invalidate();
        this.mCropView.setVisibility(0);
        this.mSavedImagePath = imageLoader$Result.fileName;
        setButtonsEnabled(true);
    }

    public final void setButtonsEnabled(boolean z) {
        this.mSave.setEnabled(z);
        this.mEdit.setEnabled(z);
        this.mShare.setEnabled(z);
    }

    public final void startExport(final PendingAction pendingAction) {
        Drawable drawable = this.mPreview.getDrawable();
        if (drawable == null) {
            Log.e("Screenshot", "No drawable, skipping export!");
            return;
        }
        CropView cropView = this.mCropView;
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Objects.requireNonNull(cropView);
        RectF rectF = cropView.mCrop;
        float f = intrinsicWidth;
        float f2 = intrinsicHeight;
        Rect rect = new Rect((int) (rectF.left * f), (int) (rectF.top * f2), (int) (rectF.right * f), (int) (rectF.bottom * f2));
        if (rect.isEmpty()) {
            Log.w("Screenshot", "Crop bounds empty, skipping export.");
            return;
        }
        updateImageDimensions();
        RenderNode renderNode = new RenderNode("Bitmap Export");
        renderNode.setPosition(0, 0, rect.width(), rect.height());
        RecordingCanvas beginRecording = renderNode.beginRecording();
        beginRecording.translate(-rect.left, -rect.top);
        beginRecording.clipRect(rect);
        drawable.draw(beginRecording);
        renderNode.endRecording();
        this.mOutputBitmap = HardwareRenderer.createHardwareBitmap(renderNode, rect.width(), rect.height());
        ImageExporter imageExporter = this.mImageExporter;
        Executor executor = this.mBackgroundExecutor;
        UUID randomUUID = UUID.randomUUID();
        Bitmap bitmap = this.mOutputBitmap;
        ZonedDateTime now = ZonedDateTime.now();
        Objects.requireNonNull(imageExporter);
        final CallbackToFutureAdapter.SafeFuture future = CallbackToFutureAdapter.getFuture(new ImageExporter$$ExternalSyntheticLambda1(executor, new ImageExporter.Task(imageExporter.mResolver, randomUUID, bitmap, now, imageExporter.mCompressFormat, imageExporter.mQuality)));
        future.delegate.addListener(new Runnable() { // from class: com.android.systemui.screenshot.LongScreenshotActivity$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                LongScreenshotActivity longScreenshotActivity = LongScreenshotActivity.this;
                LongScreenshotActivity.PendingAction pendingAction2 = pendingAction;
                ListenableFuture listenableFuture = future;
                int i = LongScreenshotActivity.$r8$clinit;
                Objects.requireNonNull(longScreenshotActivity);
                longScreenshotActivity.setButtonsEnabled(true);
                try {
                    ImageExporter.Result result = (ImageExporter.Result) listenableFuture.get();
                    int ordinal = pendingAction2.ordinal();
                    if (ordinal == 0) {
                        Uri uri = result.uri;
                        Intent intent = new Intent("android.intent.action.SEND");
                        intent.setType("image/png");
                        intent.putExtra("android.intent.extra.STREAM", uri);
                        intent.addFlags(268468225);
                        longScreenshotActivity.startActivityAsUser(Intent.createChooser(intent, null).addFlags(1), UserHandle.CURRENT);
                    } else if (ordinal == 1) {
                        Uri uri2 = result.uri;
                        String string = longScreenshotActivity.getString(2131952146);
                        Intent intent2 = new Intent("android.intent.action.EDIT");
                        if (!TextUtils.isEmpty(string)) {
                            intent2.setComponent(ComponentName.unflattenFromString(string));
                        }
                        intent2.setDataAndType(uri2, "image/png");
                        intent2.addFlags(3);
                        longScreenshotActivity.mTransitionView.setImageBitmap(longScreenshotActivity.mOutputBitmap);
                        longScreenshotActivity.mTransitionView.setVisibility(0);
                        longScreenshotActivity.mTransitionView.setTransitionName("screenshot_preview_image");
                        longScreenshotActivity.mTransitionStarted = true;
                        longScreenshotActivity.startActivity(intent2, ActivityOptions.makeSceneTransitionAnimation(longScreenshotActivity, longScreenshotActivity.mTransitionView, "screenshot_preview_image").toBundle());
                    } else if (ordinal == 2) {
                        longScreenshotActivity.finishAndRemoveTask();
                    }
                } catch (InterruptedException | CancellationException | ExecutionException e) {
                    Log.e("Screenshot", "failed to export", e);
                }
            }
        }, this.mUiExecutor);
    }

    public final void updateImageDimensions() {
        float f;
        ImageTileSet imageTileSet;
        ScrollCaptureController.LongScreenshot longScreenshot;
        ScrollCaptureController.LongScreenshot longScreenshot2;
        Drawable drawable = this.mPreview.getDrawable();
        if (drawable != null) {
            Rect bounds = drawable.getBounds();
            float width = bounds.width() / bounds.height();
            int width2 = (this.mPreview.getWidth() - this.mPreview.getPaddingLeft()) - this.mPreview.getPaddingRight();
            int height = (this.mPreview.getHeight() - this.mPreview.getPaddingTop()) - this.mPreview.getPaddingBottom();
            float f2 = width2;
            float f3 = height;
            float f4 = f2 / f3;
            int paddingLeft = this.mPreview.getPaddingLeft();
            int paddingTop = this.mPreview.getPaddingTop();
            int i = 0;
            if (width > f4) {
                int i2 = (int) ((f4 * f3) / width);
                i = (height - i2) / 2;
                CropView cropView = this.mCropView;
                Objects.requireNonNull(cropView);
                cropView.mExtraTopPadding = this.mPreview.getPaddingTop() + i;
                cropView.mExtraBottomPadding = this.mPreview.getPaddingBottom() + i;
                cropView.invalidate();
                paddingTop += i;
                CropView cropView2 = this.mCropView;
                Objects.requireNonNull(cropView2);
                cropView2.mExtraTopPadding = i;
                cropView2.mExtraBottomPadding = i;
                cropView2.invalidate();
                CropView cropView3 = this.mCropView;
                Objects.requireNonNull(cropView3);
                cropView3.mImageWidth = width2;
                cropView3.invalidate();
                f = f2 / this.mPreview.getDrawable().getIntrinsicWidth();
                height = i2;
            } else {
                int i3 = (int) ((f2 * width) / f4);
                paddingLeft += (width2 - i3) / 2;
                CropView cropView4 = this.mCropView;
                int paddingTop2 = this.mPreview.getPaddingTop();
                int paddingBottom = this.mPreview.getPaddingBottom();
                Objects.requireNonNull(cropView4);
                cropView4.mExtraTopPadding = paddingTop2;
                cropView4.mExtraBottomPadding = paddingBottom;
                cropView4.invalidate();
                CropView cropView5 = this.mCropView;
                Objects.requireNonNull(cropView5);
                cropView5.mImageWidth = (int) (width * f3);
                cropView5.invalidate();
                f = f3 / this.mPreview.getDrawable().getIntrinsicHeight();
                width2 = i3;
            }
            CropView cropView6 = this.mCropView;
            Objects.requireNonNull(cropView6);
            RectF rectF = cropView6.mCrop;
            float f5 = width2;
            float f6 = height;
            Rect rect = new Rect((int) (rectF.left * f5), (int) (rectF.top * f6), (int) (rectF.right * f5), (int) (rectF.bottom * f6));
            this.mTransitionView.setTranslationX(paddingLeft + rect.left);
            this.mTransitionView.setTranslationY(paddingTop + rect.top);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mTransitionView.getLayoutParams();
            ((ViewGroup.MarginLayoutParams) layoutParams).width = rect.width();
            ((ViewGroup.MarginLayoutParams) layoutParams).height = rect.height();
            this.mTransitionView.setLayoutParams(layoutParams);
            if (this.mLongScreenshot != null) {
                ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) this.mEnterTransitionView.getLayoutParams();
                ScrollCaptureController.LongScreenshot longScreenshot3 = this.mLongScreenshot;
                Objects.requireNonNull(longScreenshot3);
                Objects.requireNonNull(longScreenshot3.mImageTileSet);
                Objects.requireNonNull(this.mLongScreenshot);
                float max = Math.max(0.0f, (-imageTileSet.mRegion.getBounds().top) / longScreenshot.mImageTileSet.getHeight());
                ((ViewGroup.MarginLayoutParams) layoutParams2).width = (int) (drawable.getIntrinsicWidth() * f);
                Objects.requireNonNull(this.mLongScreenshot);
                ((ViewGroup.MarginLayoutParams) layoutParams2).height = (int) (longScreenshot2.mSession.getPageHeight() * f);
                this.mEnterTransitionView.setLayoutParams(layoutParams2);
                Matrix matrix = new Matrix();
                matrix.setScale(f, f);
                matrix.postTranslate(0.0f, (-f) * drawable.getIntrinsicHeight() * max);
                this.mEnterTransitionView.setImageMatrix(matrix);
                this.mEnterTransitionView.setTranslationY((max * f3) + this.mPreview.getPaddingTop() + i);
            }
        }
    }

    public static void $r8$lambda$qOUpLbBDnQlAC3CzBI4dsyuYNs4(LongScreenshotActivity longScreenshotActivity, View view) {
        Objects.requireNonNull(longScreenshotActivity);
        int id = view.getId();
        view.setPressed(true);
        longScreenshotActivity.setButtonsEnabled(false);
        if (id == 2131428727) {
            longScreenshotActivity.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_SAVED);
            longScreenshotActivity.startExport(PendingAction.SAVE);
        } else if (id == 2131427894) {
            longScreenshotActivity.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_EDIT);
            longScreenshotActivity.startExport(PendingAction.EDIT);
        } else if (id == 2131428854) {
            longScreenshotActivity.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_SHARE);
            longScreenshotActivity.startExport(PendingAction.SHARE);
        } else if (id == 2131427659) {
            longScreenshotActivity.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_EXIT);
            longScreenshotActivity.finishAndRemoveTask();
        }
    }

    public LongScreenshotActivity(UiEventLogger uiEventLogger, ImageExporter imageExporter, Executor executor, Executor executor2, LongScreenshotData longScreenshotData) {
        this.mUiEventLogger = uiEventLogger;
        this.mUiExecutor = executor;
        this.mBackgroundExecutor = executor2;
        this.mImageExporter = imageExporter;
        this.mLongScreenshotHolder = longScreenshotData;
    }

    @Override // android.app.Activity
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(2131624238);
        this.mPreview = (ImageView) requireViewById(2131428607);
        this.mSave = requireViewById(2131428727);
        this.mEdit = requireViewById(2131427894);
        this.mShare = requireViewById(2131428854);
        this.mCancel = requireViewById(2131427659);
        this.mCropView = (CropView) requireViewById(2131427784);
        MagnifierView magnifierView = (MagnifierView) requireViewById(2131428292);
        this.mMagnifierView = magnifierView;
        CropView cropView = this.mCropView;
        Objects.requireNonNull(cropView);
        cropView.mCropInteractionListener = magnifierView;
        this.mTransitionView = (ImageView) requireViewById(2131429091);
        this.mEnterTransitionView = (ImageView) requireViewById(2131427925);
        this.mSave.setOnClickListener(new WalletView$$ExternalSyntheticLambda0(this, 2));
        this.mCancel.setOnClickListener(new AuthBiometricView$$ExternalSyntheticLambda5(this, 3));
        this.mEdit.setOnClickListener(new LayoutPreference$$ExternalSyntheticLambda0(this, 3));
        this.mShare.setOnClickListener(new LongScreenshotActivity$$ExternalSyntheticLambda0(this, 0));
        this.mPreview.addOnLayoutChangeListener(new QSPanel$$ExternalSyntheticLambda0(this, 1));
        this.mScrollCaptureResponse = getIntent().getParcelableExtra("capture-response");
        if (bundle != null) {
            String string = bundle.getString("saved-image-path");
            if (string == null) {
                Log.e("Screenshot", "Missing saved state entry with key 'saved-image-path'!");
                finishAndRemoveTask();
                return;
            }
            this.mSavedImagePath = new File(string);
            getContentResolver();
            this.mCacheLoadFuture = CallbackToFutureAdapter.getFuture(new ImageLoader$$ExternalSyntheticLambda0(this.mSavedImagePath));
        }
    }

    @Override // android.app.Activity
    public final void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        File file = this.mSavedImagePath;
        if (file != null) {
            bundle.putString("saved-image-path", file.getPath());
        }
    }

    @Override // android.app.Activity
    public final void onStart() {
        super.onStart();
        this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_ACTIVITY_STARTED);
        if (this.mPreview.getDrawable() == null) {
            if (this.mCacheLoadFuture != null) {
                Log.d("Screenshot", "mCacheLoadFuture != null");
                CallbackToFutureAdapter.SafeFuture safeFuture = this.mCacheLoadFuture;
                ScreenRecordTile$$ExternalSyntheticLambda1 screenRecordTile$$ExternalSyntheticLambda1 = new ScreenRecordTile$$ExternalSyntheticLambda1(this, safeFuture, 2);
                Executor executor = this.mUiExecutor;
                Objects.requireNonNull(safeFuture);
                safeFuture.delegate.addListener(screenRecordTile$$ExternalSyntheticLambda1, executor);
                this.mCacheLoadFuture = null;
                return;
            }
            LongScreenshotData longScreenshotData = this.mLongScreenshotHolder;
            Objects.requireNonNull(longScreenshotData);
            ScrollCaptureController.LongScreenshot andSet = longScreenshotData.mLongScreenshot.getAndSet(null);
            if (andSet != null) {
                Log.i("Screenshot", "Completed: " + andSet);
                this.mLongScreenshot = andSet;
                ImageTileSet imageTileSet = andSet.mImageTileSet;
                Objects.requireNonNull(imageTileSet);
                TiledImageDrawable tiledImageDrawable = new TiledImageDrawable(imageTileSet);
                this.mPreview.setImageDrawable(tiledImageDrawable);
                MagnifierView magnifierView = this.mMagnifierView;
                ScrollCaptureController.LongScreenshot longScreenshot = this.mLongScreenshot;
                Objects.requireNonNull(longScreenshot);
                ImageTileSet imageTileSet2 = longScreenshot.mImageTileSet;
                Objects.requireNonNull(imageTileSet2);
                TiledImageDrawable tiledImageDrawable2 = new TiledImageDrawable(imageTileSet2);
                ScrollCaptureController.LongScreenshot longScreenshot2 = this.mLongScreenshot;
                Objects.requireNonNull(longScreenshot2);
                int width = longScreenshot2.mImageTileSet.getWidth();
                ScrollCaptureController.LongScreenshot longScreenshot3 = this.mLongScreenshot;
                Objects.requireNonNull(longScreenshot3);
                int height = longScreenshot3.mImageTileSet.getHeight();
                Objects.requireNonNull(magnifierView);
                magnifierView.mDrawable = tiledImageDrawable2;
                tiledImageDrawable2.setBounds(0, 0, width, height);
                magnifierView.invalidate();
                ScrollCaptureController.LongScreenshot longScreenshot4 = this.mLongScreenshot;
                Objects.requireNonNull(longScreenshot4);
                ImageTileSet imageTileSet3 = longScreenshot4.mImageTileSet;
                Objects.requireNonNull(imageTileSet3);
                ScrollCaptureController.LongScreenshot longScreenshot5 = this.mLongScreenshot;
                Objects.requireNonNull(longScreenshot5);
                float max = Math.max(0.0f, (-imageTileSet3.mRegion.getBounds().top) / longScreenshot5.mImageTileSet.getHeight());
                ScrollCaptureController.LongScreenshot longScreenshot6 = this.mLongScreenshot;
                Objects.requireNonNull(longScreenshot6);
                ImageTileSet imageTileSet4 = longScreenshot6.mImageTileSet;
                Objects.requireNonNull(imageTileSet4);
                int i = imageTileSet4.mRegion.getBounds().bottom;
                ScrollCaptureController.LongScreenshot longScreenshot7 = this.mLongScreenshot;
                Objects.requireNonNull(longScreenshot7);
                ScrollCaptureController.LongScreenshot longScreenshot8 = this.mLongScreenshot;
                Objects.requireNonNull(longScreenshot8);
                float min = Math.min(1.0f, 1.0f - ((i - longScreenshot7.mSession.getPageHeight()) / longScreenshot8.mImageTileSet.getHeight()));
                this.mEnterTransitionView.setImageDrawable(tiledImageDrawable);
                this.mEnterTransitionView.getViewTreeObserver().addOnPreDrawListener(new AnonymousClass1(max, min));
                final ImageExporter imageExporter = this.mImageExporter;
                final Executor executor2 = this.mBackgroundExecutor;
                final Bitmap bitmap = this.mLongScreenshot.toBitmap();
                final File file = new File(getCacheDir(), "long_screenshot_cache.png");
                Objects.requireNonNull(imageExporter);
                CallbackToFutureAdapter.SafeFuture future = CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: com.android.systemui.screenshot.ImageExporter$$ExternalSyntheticLambda0
                    @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
                    public final Object attachCompleter(final CallbackToFutureAdapter.Completer completer) {
                        final ImageExporter imageExporter2 = ImageExporter.this;
                        Executor executor3 = executor2;
                        final File file2 = file;
                        final Bitmap bitmap2 = bitmap;
                        Objects.requireNonNull(imageExporter2);
                        executor3.execute(new Runnable() { // from class: com.android.systemui.screenshot.ImageExporter$$ExternalSyntheticLambda2
                            @Override // java.lang.Runnable
                            public final void run() {
                                ImageExporter imageExporter3 = ImageExporter.this;
                                File file3 = file2;
                                Bitmap bitmap3 = bitmap2;
                                CallbackToFutureAdapter.Completer completer2 = completer;
                                Objects.requireNonNull(imageExporter3);
                                try {
                                    FileOutputStream fileOutputStream = new FileOutputStream(file3);
                                    bitmap3.compress(imageExporter3.mCompressFormat, imageExporter3.mQuality, fileOutputStream);
                                    completer2.set(file3);
                                    fileOutputStream.close();
                                } catch (IOException e) {
                                    if (file3.exists()) {
                                        file3.delete();
                                    }
                                    completer2.setException(e);
                                }
                            }
                        });
                        return "Bitmap#compress";
                    }
                });
                this.mCacheSaveFuture = future;
                future.delegate.addListener(new KeyguardUpdateMonitor$$ExternalSyntheticLambda8(this, 1), this.mUiExecutor);
                return;
            }
            Log.e("Screenshot", "No long screenshot available!");
            finishAndRemoveTask();
        }
    }

    @Override // android.app.Activity
    public final void onStop() {
        super.onStop();
        if (this.mTransitionStarted) {
            finish();
        }
        if (isFinishing()) {
            this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_ACTIVITY_FINISHED);
            ScrollCaptureResponse scrollCaptureResponse = this.mScrollCaptureResponse;
            if (scrollCaptureResponse != null) {
                scrollCaptureResponse.close();
            }
            CallbackToFutureAdapter.SafeFuture safeFuture = this.mCacheSaveFuture;
            if (safeFuture != null) {
                safeFuture.cancel(true);
            }
            File file = this.mSavedImagePath;
            if (file != null) {
                file.delete();
                this.mSavedImagePath = null;
            }
            ScrollCaptureController.LongScreenshot longScreenshot = this.mLongScreenshot;
            if (longScreenshot != null) {
                longScreenshot.mImageTileSet.clear();
                longScreenshot.mSession.release();
            }
        }
    }
}
