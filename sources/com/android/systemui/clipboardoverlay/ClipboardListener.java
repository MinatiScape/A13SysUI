package com.android.systemui.clipboardoverlay;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.view.View;
import com.android.systemui.CoreStartable;
import com.android.systemui.screenshot.TimeoutHandler;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda18;
import java.io.IOException;
import java.util.Objects;
/* loaded from: classes.dex */
public class ClipboardListener extends CoreStartable implements ClipboardManager.OnPrimaryClipChangedListener {
    public final ClipboardManager mClipboardManager;
    public ClipboardOverlayController mClipboardOverlayController;
    public final ClipboardOverlayControllerFactory mOverlayFactory;

    @Override // com.android.systemui.CoreStartable
    public final void start() {
    }

    @Override // android.content.ClipboardManager.OnPrimaryClipChangedListener
    public final void onPrimaryClipChanged() {
        if (this.mClipboardManager.hasPrimaryClip()) {
            if (this.mClipboardOverlayController == null) {
                ClipboardOverlayControllerFactory clipboardOverlayControllerFactory = this.mOverlayFactory;
                Context context = this.mContext;
                Objects.requireNonNull(clipboardOverlayControllerFactory);
                this.mClipboardOverlayController = new ClipboardOverlayController(context, new TimeoutHandler(context));
            }
            final ClipboardOverlayController clipboardOverlayController = this.mClipboardOverlayController;
            ClipData primaryClip = this.mClipboardManager.getPrimaryClip();
            String primaryClipSource = this.mClipboardManager.getPrimaryClipSource();
            Objects.requireNonNull(clipboardOverlayController);
            clipboardOverlayController.mView.setTranslationX(0.0f);
            clipboardOverlayController.mContainer.setAlpha(0.0f);
            clipboardOverlayController.resetActionChips();
            TimeoutHandler timeoutHandler = clipboardOverlayController.mTimeoutHandler;
            Objects.requireNonNull(timeoutHandler);
            timeoutHandler.removeMessages(2);
            if (primaryClip == null || primaryClip.getItemCount() == 0) {
                clipboardOverlayController.showTextPreview(clipboardOverlayController.mContext.getResources().getString(2131952117));
            } else if (!TextUtils.isEmpty(primaryClip.getItemAt(0).getText())) {
                ClipData.Item itemAt = primaryClip.getItemAt(0);
                if (itemAt.getTextLinks() != null) {
                    AsyncTask.execute(new ClipboardOverlayController$$ExternalSyntheticLambda4(clipboardOverlayController, primaryClip, primaryClipSource, 0));
                }
                clipboardOverlayController.showTextPreview(itemAt.getText());
                clipboardOverlayController.mEditChip.setVisibility(0);
                clipboardOverlayController.mEditChip.setAlpha(1.0f);
                clipboardOverlayController.mEditChip.setContentDescription(clipboardOverlayController.mContext.getString(2131952116));
                View.OnClickListener clipboardOverlayController$$ExternalSyntheticLambda3 = new View.OnClickListener() { // from class: com.android.systemui.clipboardoverlay.ClipboardOverlayController$$ExternalSyntheticLambda3
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        ClipboardOverlayController clipboardOverlayController2 = ClipboardOverlayController.this;
                        Objects.requireNonNull(clipboardOverlayController2);
                        Intent intent = new Intent(clipboardOverlayController2.mContext, EditTextActivity.class);
                        intent.addFlags(268468224);
                        clipboardOverlayController2.mContext.startActivity(intent);
                        clipboardOverlayController2.animateOut();
                    }
                };
                clipboardOverlayController.mEditChip.setOnClickListener(clipboardOverlayController$$ExternalSyntheticLambda3);
                clipboardOverlayController.mTextPreview.setOnClickListener(clipboardOverlayController$$ExternalSyntheticLambda3);
            } else if (primaryClip.getItemAt(0).getUri() != null) {
                Uri uri = primaryClip.getItemAt(0).getUri();
                clipboardOverlayController.mTextPreview.setVisibility(8);
                clipboardOverlayController.mImagePreview.setVisibility(0);
                clipboardOverlayController.mEditChip.setAlpha(1.0f);
                ContentResolver contentResolver = clipboardOverlayController.mContext.getContentResolver();
                try {
                    int dimensionPixelSize = clipboardOverlayController.mContext.getResources().getDimensionPixelSize(2131166750);
                    clipboardOverlayController.mImagePreview.setImageBitmap(contentResolver.loadThumbnail(uri, new Size(dimensionPixelSize, dimensionPixelSize * 4), null));
                } catch (IOException e) {
                    Log.e("ClipboardOverlayCtrlr", "Thumbnail loading failed", e);
                }
                ClipboardOverlayController$$ExternalSyntheticLambda1 clipboardOverlayController$$ExternalSyntheticLambda1 = new ClipboardOverlayController$$ExternalSyntheticLambda1(clipboardOverlayController, uri, 0);
                clipboardOverlayController.mEditChip.setOnClickListener(clipboardOverlayController$$ExternalSyntheticLambda1);
                clipboardOverlayController.mEditChip.setContentDescription(clipboardOverlayController.mContext.getString(2131952113));
                clipboardOverlayController.mImagePreview.setOnClickListener(clipboardOverlayController$$ExternalSyntheticLambda1);
            } else {
                clipboardOverlayController.showTextPreview(clipboardOverlayController.mContext.getResources().getString(2131952117));
            }
            clipboardOverlayController.mTimeoutHandler.resetTimeout();
            ClipboardOverlayController clipboardOverlayController2 = this.mClipboardOverlayController;
            BubbleStackView$$ExternalSyntheticLambda18 bubbleStackView$$ExternalSyntheticLambda18 = new BubbleStackView$$ExternalSyntheticLambda18(this, 1);
            Objects.requireNonNull(clipboardOverlayController2);
            clipboardOverlayController2.mOnSessionCompleteListener = bubbleStackView$$ExternalSyntheticLambda18;
        }
    }

    public ClipboardListener(Context context, ClipboardOverlayControllerFactory clipboardOverlayControllerFactory, ClipboardManager clipboardManager) {
        super(context);
        this.mOverlayFactory = clipboardOverlayControllerFactory;
        this.mClipboardManager = clipboardManager;
    }
}
