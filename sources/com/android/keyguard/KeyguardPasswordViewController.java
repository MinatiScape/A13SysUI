package com.android.keyguard;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.UserHandle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.TextKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardAbsKeyInputViewController;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.settingslib.Utils;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.doze.DozeScreenState$$ExternalSyntheticLambda0;
import com.android.systemui.qs.tileimpl.QSTileImpl$$ExternalSyntheticLambda0;
import com.android.systemui.qs.tileimpl.QSTileImpl$$ExternalSyntheticLambda1;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda4;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda5;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda6;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class KeyguardPasswordViewController extends KeyguardAbsKeyInputViewController<KeyguardPasswordView> {
    public final InputMethodManager mInputMethodManager;
    public final KeyguardSecurityCallback mKeyguardSecurityCallback;
    public final DelayableExecutor mMainExecutor;
    public EditText mPasswordEntry;
    public final boolean mShowImeAtScreenOn;
    public ImageView mSwitchImeButton;
    public final KeyguardPasswordViewController$$ExternalSyntheticLambda0 mOnEditorActionListener = new TextView.OnEditorActionListener() { // from class: com.android.keyguard.KeyguardPasswordViewController$$ExternalSyntheticLambda0
        @Override // android.widget.TextView.OnEditorActionListener
        public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            boolean z;
            boolean z2;
            KeyguardPasswordViewController keyguardPasswordViewController = KeyguardPasswordViewController.this;
            Objects.requireNonNull(keyguardPasswordViewController);
            if (keyEvent == null && (i == 0 || i == 6 || i == 5)) {
                z = true;
            } else {
                z = false;
            }
            if (keyEvent == null || !KeyEvent.isConfirmKey(keyEvent.getKeyCode()) || keyEvent.getAction() != 0) {
                z2 = false;
            } else {
                z2 = true;
            }
            if (!z && !z2) {
                return false;
            }
            keyguardPasswordViewController.verifyPasswordAndUnlock();
            return true;
        }
    };
    public final AnonymousClass1 mTextWatcher = new TextWatcher() { // from class: com.android.keyguard.KeyguardPasswordViewController.1
        @Override // android.text.TextWatcher
        public final void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        {
            KeyguardPasswordViewController.this = this;
        }

        @Override // android.text.TextWatcher
        public final void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            KeyguardPasswordViewController.this.mKeyguardSecurityCallback.userActivity();
        }

        @Override // android.text.TextWatcher
        public final void afterTextChanged(Editable editable) {
            if (!TextUtils.isEmpty(editable)) {
                KeyguardPasswordViewController.this.onUserInput();
            }
        }
    };

    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.keyguard.KeyguardPasswordViewController$$ExternalSyntheticLambda0] */
    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.keyguard.KeyguardPasswordViewController$1] */
    public KeyguardPasswordViewController(KeyguardPasswordView keyguardPasswordView, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel.SecurityMode securityMode, LockPatternUtils lockPatternUtils, KeyguardSecurityCallback keyguardSecurityCallback, KeyguardMessageAreaController.Factory factory, LatencyTracker latencyTracker, InputMethodManager inputMethodManager, EmergencyButtonController emergencyButtonController, DelayableExecutor delayableExecutor, Resources resources, FalsingCollector falsingCollector) {
        super(keyguardPasswordView, keyguardUpdateMonitor, securityMode, lockPatternUtils, keyguardSecurityCallback, factory, latencyTracker, falsingCollector, emergencyButtonController);
        this.mKeyguardSecurityCallback = keyguardSecurityCallback;
        this.mInputMethodManager = inputMethodManager;
        this.mMainExecutor = delayableExecutor;
        this.mShowImeAtScreenOn = resources.getBoolean(2131034201);
        this.mPasswordEntry = (EditText) keyguardPasswordView.findViewById(2131428570);
        this.mSwitchImeButton = (ImageView) keyguardPasswordView.findViewById(2131428999);
    }

    @Override // com.android.keyguard.KeyguardInputViewController
    public final void onResume(int i) {
        this.mResumed = true;
        if (i != 1 || this.mShowImeAtScreenOn) {
            ((KeyguardPasswordView) this.mView).post(new QSTileImpl$$ExternalSyntheticLambda1(this, 1));
        }
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputViewController, com.android.keyguard.KeyguardInputViewController
    public final void onPause() {
        if (!this.mPasswordEntry.isVisibleToUser()) {
            super.onPause();
        } else {
            KeyguardPasswordView keyguardPasswordView = (KeyguardPasswordView) this.mView;
            QSTileImpl$$ExternalSyntheticLambda0 qSTileImpl$$ExternalSyntheticLambda0 = new QSTileImpl$$ExternalSyntheticLambda0(this, 1);
            Objects.requireNonNull(keyguardPasswordView);
            keyguardPasswordView.mOnFinishImeAnimationRunnable = qSTileImpl$$ExternalSyntheticLambda0;
        }
        if (this.mPasswordEntry.isAttachedToWindow()) {
            this.mPasswordEntry.getWindowInsetsController().hide(WindowInsets.Type.ime());
        }
    }

    @Override // com.android.keyguard.KeyguardSecurityView
    public final void onStartingToHide() {
        if (this.mPasswordEntry.isAttachedToWindow()) {
            this.mPasswordEntry.getWindowInsetsController().hide(WindowInsets.Type.ime());
        }
    }

    @Override // com.android.keyguard.KeyguardInputViewController, com.android.systemui.util.ViewController
    public final void onViewAttached() {
        KeyguardAbsKeyInputView keyguardAbsKeyInputView = (KeyguardAbsKeyInputView) this.mView;
        KeyguardAbsKeyInputViewController$$ExternalSyntheticLambda0 keyguardAbsKeyInputViewController$$ExternalSyntheticLambda0 = this.mKeyDownListener;
        Objects.requireNonNull(keyguardAbsKeyInputView);
        keyguardAbsKeyInputView.mKeyDownListener = keyguardAbsKeyInputViewController$$ExternalSyntheticLambda0;
        EmergencyButtonController emergencyButtonController = ((KeyguardAbsKeyInputViewController) this).mEmergencyButtonController;
        KeyguardAbsKeyInputViewController.AnonymousClass1 r1 = this.mEmergencyButtonCallback;
        Objects.requireNonNull(emergencyButtonController);
        emergencyButtonController.mEmergencyButtonCallback = r1;
        this.mPasswordEntry.setTextOperationUser(UserHandle.of(KeyguardUpdateMonitor.getCurrentUser()));
        this.mPasswordEntry.setKeyListener(TextKeyListener.getInstance());
        this.mPasswordEntry.setInputType(129);
        this.mPasswordEntry.setSelected(true);
        this.mPasswordEntry.setOnEditorActionListener(this.mOnEditorActionListener);
        this.mPasswordEntry.addTextChangedListener(this.mTextWatcher);
        this.mPasswordEntry.setOnClickListener(new BubbleStackView$$ExternalSyntheticLambda4(this, 1));
        this.mSwitchImeButton.setOnClickListener(new BubbleStackView$$ExternalSyntheticLambda5(this, 1));
        View findViewById = ((KeyguardPasswordView) this.mView).findViewById(2131427661);
        if (findViewById != null) {
            findViewById.setOnClickListener(new BubbleStackView$$ExternalSyntheticLambda6(this, 1));
        }
        updateSwitchImeButton();
        this.mMainExecutor.executeDelayed(new DozeScreenState$$ExternalSyntheticLambda0(this, 1), 500L);
    }

    @Override // com.android.keyguard.KeyguardInputViewController, com.android.systemui.util.ViewController
    public final void onViewDetached() {
        this.mPasswordEntry.setOnEditorActionListener(null);
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputViewController
    public final void resetState() {
        this.mPasswordEntry.setTextOperationUser(UserHandle.of(KeyguardUpdateMonitor.getCurrentUser()));
        this.mMessageAreaController.setMessage("");
        boolean isEnabled = this.mPasswordEntry.isEnabled();
        ((KeyguardPasswordView) this.mView).setPasswordEntryEnabled(true);
        ((KeyguardPasswordView) this.mView).setPasswordEntryInputEnabled(true);
        if (this.mResumed && this.mPasswordEntry.isVisibleToUser() && isEnabled) {
            ((KeyguardPasswordView) this.mView).post(new QSTileImpl$$ExternalSyntheticLambda1(this, 1));
        }
    }

    public final void updateSwitchImeButton() {
        boolean z;
        int i;
        boolean z2 = true;
        if (this.mSwitchImeButton.getVisibility() == 0) {
            z = true;
        } else {
            z = false;
        }
        InputMethodManager inputMethodManager = this.mInputMethodManager;
        Iterator it = inputMethodManager.getEnabledInputMethodListAsUser(KeyguardUpdateMonitor.getCurrentUser()).iterator();
        int i2 = 0;
        while (true) {
            if (it.hasNext()) {
                InputMethodInfo inputMethodInfo = (InputMethodInfo) it.next();
                if (i2 > 1) {
                    break;
                }
                List<InputMethodSubtype> enabledInputMethodSubtypeList = inputMethodManager.getEnabledInputMethodSubtypeList(inputMethodInfo, true);
                if (!enabledInputMethodSubtypeList.isEmpty()) {
                    int i3 = 0;
                    for (InputMethodSubtype inputMethodSubtype : enabledInputMethodSubtypeList) {
                        if (inputMethodSubtype.isAuxiliary()) {
                            i3++;
                        }
                    }
                    if (enabledInputMethodSubtypeList.size() - i3 <= 0) {
                    }
                }
                i2++;
            } else if (i2 <= 1 && inputMethodManager.getEnabledInputMethodSubtypeList(null, false).size() <= 1) {
                z2 = false;
            }
        }
        if (z != z2) {
            ImageView imageView = this.mSwitchImeButton;
            if (z2) {
                i = 0;
            } else {
                i = 8;
            }
            imageView.setVisibility(i);
        }
        if (this.mSwitchImeButton.getVisibility() != 0) {
            ViewGroup.LayoutParams layoutParams = this.mPasswordEntry.getLayoutParams();
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ((ViewGroup.MarginLayoutParams) layoutParams).setMarginStart(0);
                this.mPasswordEntry.setLayoutParams(layoutParams);
            }
        }
    }

    /* renamed from: $r8$lambda$jjE1OxCwl-_IFkmzkDD3tZQdL_8 */
    public static /* synthetic */ void m18$r8$lambda$jjE1OxCwl_IFkmzkDD3tZQdL_8(KeyguardPasswordViewController keyguardPasswordViewController) {
        Objects.requireNonNull(keyguardPasswordViewController);
        keyguardPasswordViewController.mPasswordEntry.clearFocus();
        super.onPause();
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputViewController, com.android.keyguard.KeyguardInputViewController
    public final void reloadColors() {
        super.reloadColors();
        int defaultColor = Utils.getColorAttr(((KeyguardPasswordView) this.mView).getContext(), 16842806).getDefaultColor();
        this.mPasswordEntry.setTextColor(defaultColor);
        this.mPasswordEntry.setHighlightColor(defaultColor);
        this.mPasswordEntry.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
        this.mPasswordEntry.setForegroundTintList(ColorStateList.valueOf(defaultColor));
        this.mSwitchImeButton.setImageTintList(ColorStateList.valueOf(defaultColor));
    }
}
