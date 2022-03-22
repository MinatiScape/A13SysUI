package com.android.keyguard.clock;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
/* loaded from: classes.dex */
public class ImageClock extends FrameLayout {
    public String mDescFormat;
    public ImageView mHourHand;
    public ImageView mMinuteHand;
    public final Calendar mTime;

    public ImageClock(Context context) {
        this(context, null);
    }

    public ImageClock(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ImageClock(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mTime = Calendar.getInstance(TimeZone.getDefault());
        this.mDescFormat = ((SimpleDateFormat) DateFormat.getTimeFormat(context)).toLocalizedPattern();
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mTime.setTimeZone(TimeZone.getDefault());
        this.mTime.setTimeInMillis(System.currentTimeMillis());
        this.mHourHand.setRotation((this.mTime.get(12) * 0.5f) + (this.mTime.get(10) * 30.0f));
        this.mMinuteHand.setRotation(this.mTime.get(12) * 6.0f);
        setContentDescription(DateFormat.format(this.mDescFormat, this.mTime));
        invalidate();
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mHourHand = (ImageView) findViewById(2131428100);
        this.mMinuteHand = (ImageView) findViewById(2131428373);
    }
}
