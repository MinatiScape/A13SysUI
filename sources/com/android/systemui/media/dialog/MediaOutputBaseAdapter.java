package com.android.systemui.media.dialog;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.recyclerview.R$dimen;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settingslib.media.MediaDevice;
import com.android.systemui.wmshell.BubblesManager$5$$ExternalSyntheticLambda0;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class MediaOutputBaseAdapter extends RecyclerView.Adapter<MediaDeviceBaseViewHolder> {
    public Context mContext;
    public final MediaOutputController mController;
    public View mHolderView;
    public boolean mIsDragging = false;
    public int mCurrentActivePosition = -1;

    /* loaded from: classes.dex */
    public abstract class MediaDeviceBaseViewHolder extends RecyclerView.ViewHolder {
        public static final /* synthetic */ int $r8$clinit = 0;
        public final CheckBox mCheckBox;
        public final LinearLayout mContainerLayout;
        public String mDeviceId;
        public final FrameLayout mItemLayout;
        public final ProgressBar mProgressBar;
        public final SeekBar mSeekBar;
        public final ImageView mStatusIcon;
        public final TextView mSubTitleText;
        public final ImageView mTitleIcon;
        public final TextView mTitleText;
        public final RelativeLayout mTwoLineLayout;
        public final TextView mTwoLineTitleText;

        public abstract void onBind();

        public void onBind(MediaDevice mediaDevice, boolean z, boolean z2, int i) {
            this.mDeviceId = mediaDevice.getId();
            R$dimen.postOnBackgroundThread(new BubblesManager$5$$ExternalSyntheticLambda0(this, mediaDevice, 2));
        }

        public MediaDeviceBaseViewHolder(View view) {
            super(view);
            this.mContainerLayout = (LinearLayout) view.requireViewById(2131427834);
            this.mItemLayout = (FrameLayout) view.requireViewById(2131428144);
            this.mTitleText = (TextView) view.requireViewById(2131429057);
            this.mSubTitleText = (TextView) view.requireViewById(2131428947);
            this.mTwoLineLayout = (RelativeLayout) view.requireViewById(2131429132);
            this.mTwoLineTitleText = (TextView) view.requireViewById(2131429133);
            this.mTitleIcon = (ImageView) view.requireViewById(2131429061);
            this.mProgressBar = (ProgressBar) view.requireViewById(2131429220);
            this.mSeekBar = (SeekBar) view.requireViewById(2131429230);
            this.mStatusIcon = (ImageView) view.requireViewById(2131428351);
            this.mCheckBox = (CheckBox) view.requireViewById(2131427686);
        }

        public final void setSingleLineLayout(CharSequence charSequence, boolean z, boolean z2, boolean z3) {
            Drawable drawable;
            int i;
            int i2;
            int i3 = 8;
            this.mTwoLineLayout.setVisibility(8);
            if (z || z2) {
                drawable = MediaOutputBaseAdapter.this.mContext.getDrawable(2131232444).mutate();
            } else {
                drawable = MediaOutputBaseAdapter.this.mContext.getDrawable(2131232443).mutate();
            }
            MediaOutputController mediaOutputController = MediaOutputBaseAdapter.this.mController;
            Objects.requireNonNull(mediaOutputController);
            drawable.setColorFilter(new PorterDuffColorFilter(mediaOutputController.mColorItemBackground, PorterDuff.Mode.SRC_IN));
            this.mItemLayout.setBackground(drawable);
            ProgressBar progressBar = this.mProgressBar;
            if (z2) {
                i = 0;
            } else {
                i = 8;
            }
            progressBar.setVisibility(i);
            this.mSeekBar.setAlpha(1.0f);
            SeekBar seekBar = this.mSeekBar;
            if (z) {
                i2 = 0;
            } else {
                i2 = 8;
            }
            seekBar.setVisibility(i2);
            ImageView imageView = this.mStatusIcon;
            if (z3) {
                i3 = 0;
            }
            imageView.setVisibility(i3);
            this.mTitleText.setText(charSequence);
            this.mTitleText.setVisibility(0);
        }
    }

    public MediaOutputBaseAdapter(MediaOutputController mediaOutputController) {
        this.mController = mediaOutputController;
    }
}
