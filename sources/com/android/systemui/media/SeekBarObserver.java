package com.android.systemui.media;

import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.lifecycle.Observer;
import com.android.systemui.media.SeekBarViewModel;
import java.util.Objects;
/* compiled from: SeekBarObserver.kt */
/* loaded from: classes.dex */
public final class SeekBarObserver implements Observer<SeekBarViewModel.Progress> {
    public final MediaViewHolder holder;
    public final int seekBarDisabledHeight;
    public final int seekBarDisabledVerticalPadding;
    public final int seekBarEnabledMaxHeight;
    public final int seekBarEnabledVerticalPadding;

    @Override // androidx.lifecycle.Observer
    public final void onChanged(SeekBarViewModel.Progress progress) {
        int i;
        SeekBarViewModel.Progress progress2 = progress;
        if (!progress2.enabled) {
            MediaViewHolder mediaViewHolder = this.holder;
            Objects.requireNonNull(mediaViewHolder);
            if (mediaViewHolder.seekBar.getMaxHeight() != this.seekBarDisabledHeight) {
                MediaViewHolder mediaViewHolder2 = this.holder;
                Objects.requireNonNull(mediaViewHolder2);
                mediaViewHolder2.seekBar.setMaxHeight(this.seekBarDisabledHeight);
                setVerticalPadding(this.seekBarDisabledVerticalPadding);
            }
            MediaViewHolder mediaViewHolder3 = this.holder;
            Objects.requireNonNull(mediaViewHolder3);
            mediaViewHolder3.seekBar.setEnabled(false);
            MediaViewHolder mediaViewHolder4 = this.holder;
            Objects.requireNonNull(mediaViewHolder4);
            mediaViewHolder4.seekBar.getThumb().setAlpha(0);
            MediaViewHolder mediaViewHolder5 = this.holder;
            Objects.requireNonNull(mediaViewHolder5);
            mediaViewHolder5.seekBar.setProgress(0);
            TextView elapsedTimeView = this.holder.getElapsedTimeView();
            if (elapsedTimeView != null) {
                elapsedTimeView.setText("");
            }
            TextView totalTimeView = this.holder.getTotalTimeView();
            if (totalTimeView != null) {
                totalTimeView.setText("");
            }
            MediaViewHolder mediaViewHolder6 = this.holder;
            Objects.requireNonNull(mediaViewHolder6);
            mediaViewHolder6.seekBar.setContentDescription("");
            return;
        }
        MediaViewHolder mediaViewHolder7 = this.holder;
        Objects.requireNonNull(mediaViewHolder7);
        Drawable thumb = mediaViewHolder7.seekBar.getThumb();
        if (progress2.seekAvailable) {
            i = 255;
        } else {
            i = 0;
        }
        thumb.setAlpha(i);
        MediaViewHolder mediaViewHolder8 = this.holder;
        Objects.requireNonNull(mediaViewHolder8);
        mediaViewHolder8.seekBar.setEnabled(progress2.seekAvailable);
        MediaViewHolder mediaViewHolder9 = this.holder;
        Objects.requireNonNull(mediaViewHolder9);
        if (mediaViewHolder9.seekBar.getMaxHeight() != this.seekBarEnabledMaxHeight) {
            MediaViewHolder mediaViewHolder10 = this.holder;
            Objects.requireNonNull(mediaViewHolder10);
            mediaViewHolder10.seekBar.setMaxHeight(this.seekBarEnabledMaxHeight);
            setVerticalPadding(this.seekBarEnabledVerticalPadding);
        }
        MediaViewHolder mediaViewHolder11 = this.holder;
        Objects.requireNonNull(mediaViewHolder11);
        mediaViewHolder11.seekBar.setMax(progress2.duration);
        String formatElapsedTime = DateUtils.formatElapsedTime(progress2.duration / 1000);
        TextView totalTimeView2 = this.holder.getTotalTimeView();
        if (totalTimeView2 != null) {
            totalTimeView2.setText(formatElapsedTime);
        }
        Integer num = progress2.elapsedTime;
        if (num != null) {
            int intValue = num.intValue();
            MediaViewHolder mediaViewHolder12 = this.holder;
            Objects.requireNonNull(mediaViewHolder12);
            mediaViewHolder12.seekBar.setProgress(intValue);
            String formatElapsedTime2 = DateUtils.formatElapsedTime(intValue / 1000);
            TextView elapsedTimeView2 = this.holder.getElapsedTimeView();
            if (elapsedTimeView2 != null) {
                elapsedTimeView2.setText(formatElapsedTime2);
            }
            MediaViewHolder mediaViewHolder13 = this.holder;
            Objects.requireNonNull(mediaViewHolder13);
            SeekBar seekBar = mediaViewHolder13.seekBar;
            MediaViewHolder mediaViewHolder14 = this.holder;
            Objects.requireNonNull(mediaViewHolder14);
            seekBar.setContentDescription(mediaViewHolder14.seekBar.getContext().getString(2131952187, formatElapsedTime2, formatElapsedTime));
        }
    }

    public final void setVerticalPadding(int i) {
        MediaViewHolder mediaViewHolder = this.holder;
        Objects.requireNonNull(mediaViewHolder);
        int paddingLeft = mediaViewHolder.seekBar.getPaddingLeft();
        MediaViewHolder mediaViewHolder2 = this.holder;
        Objects.requireNonNull(mediaViewHolder2);
        int paddingRight = mediaViewHolder2.seekBar.getPaddingRight();
        MediaViewHolder mediaViewHolder3 = this.holder;
        Objects.requireNonNull(mediaViewHolder3);
        int paddingBottom = mediaViewHolder3.seekBar.getPaddingBottom();
        MediaViewHolder mediaViewHolder4 = this.holder;
        Objects.requireNonNull(mediaViewHolder4);
        mediaViewHolder4.seekBar.setPadding(paddingLeft, i, paddingRight, paddingBottom);
    }

    public SeekBarObserver(MediaViewHolder mediaViewHolder, boolean z) {
        int i;
        int i2;
        this.holder = mediaViewHolder;
        this.seekBarEnabledMaxHeight = mediaViewHolder.seekBar.getContext().getResources().getDimensionPixelSize(2131166883);
        this.seekBarDisabledHeight = mediaViewHolder.seekBar.getContext().getResources().getDimensionPixelSize(2131166881);
        if (z) {
            i = mediaViewHolder.seekBar.getContext().getResources().getDimensionPixelSize(2131166891);
        } else {
            i = mediaViewHolder.seekBar.getContext().getResources().getDimensionPixelSize(2131166884);
        }
        this.seekBarEnabledVerticalPadding = i;
        if (z) {
            i2 = mediaViewHolder.seekBar.getContext().getResources().getDimensionPixelSize(2131166890);
        } else {
            i2 = mediaViewHolder.seekBar.getContext().getResources().getDimensionPixelSize(2131166882);
        }
        this.seekBarDisabledVerticalPadding = i2;
    }
}
