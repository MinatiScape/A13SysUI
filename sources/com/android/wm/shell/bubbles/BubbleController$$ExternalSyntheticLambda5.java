package com.android.wm.shell.bubbles;

import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BlendMode;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.service.quickaccesswallet.GetWalletCardsError;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.ScrollCaptureResponse;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.systemui.screenshot.ScreenshotController;
import com.android.systemui.screenshot.ScreenshotController$$ExternalSyntheticLambda6;
import com.android.systemui.screenshot.ScreenshotView;
import com.android.systemui.wallet.ui.WalletScreenController;
import com.android.systemui.wallet.ui.WalletView;
import com.android.wm.shell.compatui.letterboxedu.LetterboxEduAnimationController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubbleController$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ BubbleController$$ExternalSyntheticLambda5(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        boolean z;
        int i;
        int i2;
        switch (this.$r8$classId) {
            case 0:
                BubbleController bubbleController = (BubbleController) this.f$0;
                Bubble bubble = (Bubble) this.f$1;
                Objects.requireNonNull(bubbleController);
                Objects.requireNonNull(bubble);
                bubbleController.removeBubble(bubble.mKey, 10);
                return;
            case 1:
                ScreenshotController screenshotController = (ScreenshotController) this.f$0;
                ScrollCaptureResponse scrollCaptureResponse = (ScrollCaptureResponse) this.f$1;
                ScreenshotController.AnonymousClass1 r3 = ScreenshotController.SCREENSHOT_REMOTE_RUNNER;
                Objects.requireNonNull(screenshotController);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                screenshotController.mDisplayManager.getDisplay(0).getRealMetrics(displayMetrics);
                Bitmap captureScreenshot = screenshotController.captureScreenshot(new Rect(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels));
                final ScreenshotView screenshotView = screenshotController.mScreenshotView;
                Bitmap bitmap = screenshotController.mScreenBitmap;
                boolean z2 = screenshotController.mScreenshotTakenInPortrait;
                Objects.requireNonNull(screenshotView);
                if (z2 == screenshotView.mOrientationPortrait) {
                    z = true;
                } else {
                    z = false;
                }
                screenshotView.mShowScrollablePreview = z;
                screenshotView.mScrollingScrim.setImageBitmap(captureScreenshot);
                screenshotView.mScrollingScrim.setVisibility(0);
                if (screenshotView.mShowScrollablePreview) {
                    Rect rect = new Rect(scrollCaptureResponse.getBoundsInWindow());
                    Rect windowBounds = scrollCaptureResponse.getWindowBounds();
                    rect.offset(windowBounds.left, windowBounds.top);
                    DisplayMetrics displayMetrics2 = screenshotView.mDisplayMetrics;
                    rect.intersect(new Rect(0, 0, displayMetrics2.widthPixels, displayMetrics2.heightPixels));
                    float f = screenshotView.mFixedSize;
                    if (screenshotView.mOrientationPortrait) {
                        i = bitmap.getWidth();
                    } else {
                        i = bitmap.getHeight();
                    }
                    float f2 = f / i;
                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) screenshotView.mScrollablePreview.getLayoutParams();
                    ((ViewGroup.MarginLayoutParams) layoutParams).width = (int) (rect.width() * f2);
                    ((ViewGroup.MarginLayoutParams) layoutParams).height = (int) (rect.height() * f2);
                    Matrix matrix = new Matrix();
                    matrix.setScale(f2, f2);
                    matrix.postTranslate((-rect.left) * f2, (-rect.top) * f2);
                    ImageView imageView = screenshotView.mScrollablePreview;
                    if (screenshotView.mDirectionLTR) {
                        i2 = rect.left;
                    } else {
                        i2 = rect.right - screenshotView.getWidth();
                    }
                    imageView.setTranslationX(i2 * f2);
                    screenshotView.mScrollablePreview.setTranslationY(f2 * rect.top);
                    screenshotView.mScrollablePreview.setImageMatrix(matrix);
                    screenshotView.mScrollablePreview.setImageBitmap(bitmap);
                    screenshotView.mScrollablePreview.setVisibility(0);
                }
                screenshotView.mDismissButton.setVisibility(8);
                screenshotView.mActionsContainer.setVisibility(8);
                screenshotView.mBackgroundProtection.setVisibility(8);
                screenshotView.mActionsContainerBackground.setVisibility(4);
                screenshotView.mScreenshotPreviewBorder.setVisibility(4);
                screenshotView.mScreenshotPreview.setVisibility(4);
                screenshotView.mScrollingScrim.setImageTintBlendMode(BlendMode.SRC_ATOP);
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 0.3f);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda5
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        ScreenshotView screenshotView2 = ScreenshotView.this;
                        int i3 = ScreenshotView.$r8$clinit;
                        Objects.requireNonNull(screenshotView2);
                        screenshotView2.mScrollingScrim.setImageTintList(ColorStateList.valueOf(Color.argb(((Float) valueAnimator.getAnimatedValue()).floatValue(), 0.0f, 0.0f, 0.0f)));
                    }
                });
                ofFloat.setDuration(200L);
                ofFloat.start();
                screenshotController.mScreenshotView.post(new ScreenshotController$$ExternalSyntheticLambda6(screenshotController, scrollCaptureResponse, 0));
                return;
            case 2:
                WalletScreenController walletScreenController = (WalletScreenController) this.f$0;
                GetWalletCardsError getWalletCardsError = (GetWalletCardsError) this.f$1;
                int i3 = WalletScreenController.$r8$clinit;
                Objects.requireNonNull(walletScreenController);
                if (!walletScreenController.mIsDismissed) {
                    WalletView walletView = walletScreenController.mWalletView;
                    CharSequence message = getWalletCardsError.getMessage();
                    Objects.requireNonNull(walletView);
                    if (TextUtils.isEmpty(message)) {
                        message = walletView.getResources().getText(2131953525);
                    }
                    walletView.mErrorView.setText(message);
                    walletView.mErrorView.setVisibility(0);
                    walletView.mCardCarouselContainer.setVisibility(8);
                    walletView.mEmptyStateView.setVisibility(8);
                    return;
                }
                return;
            default:
                LetterboxEduAnimationController letterboxEduAnimationController = (LetterboxEduAnimationController) this.f$0;
                LetterboxEduAnimationController.AnonymousClass3 r1 = LetterboxEduAnimationController.DRAWABLE_ALPHA;
                Objects.requireNonNull(letterboxEduAnimationController);
                letterboxEduAnimationController.mDialogAnimation = null;
                ((Runnable) this.f$1).run();
                return;
        }
    }
}
