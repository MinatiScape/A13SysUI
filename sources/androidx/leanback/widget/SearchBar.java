package androidx.leanback.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.SoundPool;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import androidx.leanback.widget.SearchEditText;
/* loaded from: classes.dex */
public class SearchBar extends RelativeLayout {
    public boolean mAutoStartRecognition;
    public int mBackgroundAlpha;
    public int mBackgroundSpeechAlpha;
    public Drawable mBarBackground;
    public int mBarHeight;
    public final Context mContext;
    public final Handler mHandler;
    public String mHint;
    public final InputMethodManager mInputMethodManager;
    public String mSearchQuery;
    public SearchEditText mSearchTextEditor;
    public SparseIntArray mSoundMap;
    public SoundPool mSoundPool;
    public SpeechOrbView mSpeechOrbView;
    public final int mTextColor;
    public final int mTextColorSpeechMode;
    public final int mTextHintColor;
    public final int mTextHintColorSpeechMode;

    /* renamed from: androidx.leanback.widget.SearchBar$4  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass4 implements SearchEditText.OnKeyboardDismissListener {
        public AnonymousClass4() {
        }
    }

    public SearchBar(Context context) {
        this(context, null);
    }

    public SearchBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        this.mSoundPool.release();
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    public final void setNextFocusDownId(int i) {
        this.mSpeechOrbView.setNextFocusDownId(i);
        this.mSearchTextEditor.setNextFocusDownId(i);
    }

    public final void updateUi(boolean z) {
        if (z) {
            this.mBarBackground.setAlpha(this.mBackgroundSpeechAlpha);
            if (this.mSpeechOrbView.isFocused()) {
                this.mSearchTextEditor.setTextColor(this.mTextHintColorSpeechMode);
                this.mSearchTextEditor.setHintTextColor(this.mTextHintColorSpeechMode);
            } else {
                this.mSearchTextEditor.setTextColor(this.mTextColorSpeechMode);
                this.mSearchTextEditor.setHintTextColor(this.mTextHintColorSpeechMode);
            }
        } else {
            this.mBarBackground.setAlpha(this.mBackgroundAlpha);
            this.mSearchTextEditor.setTextColor(this.mTextColor);
            this.mSearchTextEditor.setHintTextColor(this.mTextHintColor);
        }
        updateHint();
    }

    public SearchBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mHandler = new Handler();
        this.mAutoStartRecognition = false;
        this.mSoundMap = new SparseIntArray();
        this.mContext = context;
        Resources resources = getResources();
        LayoutInflater.from(getContext()).inflate(2131624226, (ViewGroup) this, true);
        this.mBarHeight = getResources().getDimensionPixelSize(2131166088);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, this.mBarHeight);
        layoutParams.addRule(10, -1);
        setLayoutParams(layoutParams);
        setBackgroundColor(0);
        setClipChildren(false);
        this.mSearchQuery = "";
        this.mInputMethodManager = (InputMethodManager) context.getSystemService("input_method");
        this.mTextColorSpeechMode = resources.getColor(2131099944);
        this.mTextColor = resources.getColor(2131099943);
        this.mBackgroundSpeechAlpha = resources.getInteger(2131492973);
        this.mBackgroundAlpha = resources.getInteger(2131492974);
        this.mTextHintColorSpeechMode = resources.getColor(2131099942);
        this.mTextHintColor = resources.getColor(2131099941);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mSoundPool = new SoundPool(2, 1, 0);
        Context context = this.mContext;
        int[] iArr = {2131886083, 2131886085, 2131886084, 2131886086};
        for (int i = 0; i < 4; i++) {
            int i2 = iArr[i];
            this.mSoundMap.put(i2, this.mSoundPool.load(context, i2, 1));
        }
    }

    /* JADX WARN: Type inference failed for: r0v11, types: [androidx.leanback.widget.SearchBar$2] */
    /* JADX WARN: Unknown variable types count: 1 */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onFinishInflate() {
        /*
            r3 = this;
            super.onFinishInflate()
            r0 = 2131428233(0x7f0b0389, float:1.8478105E38)
            android.view.View r0 = r3.findViewById(r0)
            android.widget.RelativeLayout r0 = (android.widget.RelativeLayout) r0
            android.graphics.drawable.Drawable r0 = r0.getBackground()
            r3.mBarBackground = r0
            r0 = 2131428236(0x7f0b038c, float:1.847811E38)
            android.view.View r0 = r3.findViewById(r0)
            androidx.leanback.widget.SearchEditText r0 = (androidx.leanback.widget.SearchEditText) r0
            r3.mSearchTextEditor = r0
            r0 = 2131428232(0x7f0b0388, float:1.8478103E38)
            android.view.View r0 = r3.findViewById(r0)
            android.widget.ImageView r0 = (android.widget.ImageView) r0
            androidx.leanback.widget.SearchEditText r0 = r3.mSearchTextEditor
            androidx.leanback.widget.SearchBar$1 r1 = new androidx.leanback.widget.SearchBar$1
            r1.<init>()
            r0.setOnFocusChangeListener(r1)
            androidx.leanback.widget.SearchBar$2 r0 = new androidx.leanback.widget.SearchBar$2
            r0.<init>()
            androidx.leanback.widget.SearchEditText r1 = r3.mSearchTextEditor
            androidx.leanback.widget.SearchBar$3 r2 = new androidx.leanback.widget.SearchBar$3
            r2.<init>()
            r1.addTextChangedListener(r2)
            androidx.leanback.widget.SearchEditText r0 = r3.mSearchTextEditor
            androidx.leanback.widget.SearchBar$4 r1 = new androidx.leanback.widget.SearchBar$4
            r1.<init>()
            java.util.Objects.requireNonNull(r0)
            r0.mKeyboardDismissListener = r1
            androidx.leanback.widget.SearchEditText r0 = r3.mSearchTextEditor
            androidx.leanback.widget.SearchBar$5 r1 = new androidx.leanback.widget.SearchBar$5
            r1.<init>()
            r0.setOnEditorActionListener(r1)
            androidx.leanback.widget.SearchEditText r0 = r3.mSearchTextEditor
            java.lang.String r1 = "escapeNorth,voiceDismiss"
            r0.setPrivateImeOptions(r1)
            r0 = 2131428234(0x7f0b038a, float:1.8478107E38)
            android.view.View r0 = r3.findViewById(r0)
            androidx.leanback.widget.SpeechOrbView r0 = (androidx.leanback.widget.SpeechOrbView) r0
            r3.mSpeechOrbView = r0
            androidx.leanback.widget.SearchBar$6 r1 = new androidx.leanback.widget.SearchBar$6
            r1.<init>()
            java.util.Objects.requireNonNull(r0)
            r0.mListener = r1
            androidx.leanback.widget.SpeechOrbView r0 = r3.mSpeechOrbView
            androidx.leanback.widget.SearchBar$7 r1 = new androidx.leanback.widget.SearchBar$7
            r1.<init>()
            r0.setOnFocusChangeListener(r1)
            boolean r0 = r3.hasFocus()
            r3.updateUi(r0)
            r3.updateHint()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.SearchBar.onFinishInflate():void");
    }

    public final void updateHint() {
        String string = getResources().getString(2131952646);
        if (!TextUtils.isEmpty(null)) {
            if (this.mSpeechOrbView.isFocused()) {
                string = getResources().getString(2131952649, null);
            } else {
                string = getResources().getString(2131952648, null);
            }
        } else if (this.mSpeechOrbView.isFocused()) {
            string = getResources().getString(2131952647);
        }
        this.mHint = string;
        SearchEditText searchEditText = this.mSearchTextEditor;
        if (searchEditText != null) {
            searchEditText.setHint(string);
        }
    }
}
