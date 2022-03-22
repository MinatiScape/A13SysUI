package androidx.leanback.widget;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Property;
import android.view.ActionMode;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;
@SuppressLint({"AppCompatCustomView"})
/* loaded from: classes.dex */
class StreamingTextView extends EditText {
    public static final Pattern SPLIT_PATTERN = Pattern.compile("\\S+");
    public static final AnonymousClass1 STREAM_POSITION_PROPERTY = new Property<StreamingTextView, Integer>() { // from class: androidx.leanback.widget.StreamingTextView.1
        @Override // android.util.Property
        public final Integer get(StreamingTextView streamingTextView) {
            StreamingTextView streamingTextView2 = streamingTextView;
            Objects.requireNonNull(streamingTextView2);
            return Integer.valueOf(streamingTextView2.mStreamPosition);
        }

        @Override // android.util.Property
        public final void set(StreamingTextView streamingTextView, Integer num) {
            StreamingTextView streamingTextView2 = streamingTextView;
            int intValue = num.intValue();
            Objects.requireNonNull(streamingTextView2);
            streamingTextView2.mStreamPosition = intValue;
            streamingTextView2.invalidate();
        }
    };
    public Bitmap mOneDot;
    public final Random mRandom = new Random();
    public int mStreamPosition;
    public ObjectAnimator mStreamingAnimation;
    public Bitmap mTwoDot;

    public StreamingTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public StreamingTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), 2131232400);
        this.mOneDot = Bitmap.createScaledBitmap(decodeResource, (int) (decodeResource.getWidth() * 1.3f), (int) (decodeResource.getHeight() * 1.3f), false);
        Bitmap decodeResource2 = BitmapFactory.decodeResource(getResources(), 2131232402);
        this.mTwoDot = Bitmap.createScaledBitmap(decodeResource2, (int) (decodeResource2.getWidth() * 1.3f), (int) (decodeResource2.getHeight() * 1.3f), false);
        this.mStreamPosition = -1;
        ObjectAnimator objectAnimator = this.mStreamingAnimation;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
        setText("");
    }

    @Override // android.view.View
    public final void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName("androidx.leanback.widget.StreamingTextView");
    }

    @Override // android.widget.TextView
    public final void setCustomSelectionActionModeCallback(ActionMode.Callback callback) {
        super.setCustomSelectionActionModeCallback(callback);
    }
}
