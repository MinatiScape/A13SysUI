package com.android.systemui.statusbar.policy;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.BlendMode;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.UserHandle;
import android.text.Editable;
import android.text.SpannedString;
import android.text.TextWatcher;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.ContentInfo;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.OnReceiveContentListener;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowInsets;
import android.view.WindowInsetsAnimation;
import android.view.WindowInsetsController;
import android.view.accessibility.AccessibilityEvent;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.graphics.ColorUtils;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.Dependency;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.policy.RemoteInputView;
import com.android.wm.shell.animation.Interpolators;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class RemoteInputView extends LinearLayout implements View.OnClickListener {
    public static final Object VIEW_TAG = new Object();
    public boolean mColorized;
    public GradientDrawable mContentBackground;
    public RemoteInputController mController;
    public ImageView mDelete;
    public ImageView mDeleteBg;
    public RemoteEditText mEditText;
    public NotificationEntry mEntry;
    public PendingIntent mPendingIntent;
    public ProgressBar mProgressBar;
    public RemoteInput mRemoteInput;
    public RemoteInput[] mRemoteInputs;
    public boolean mRemoved;
    public boolean mResetting;
    public int mRevealCx;
    public int mRevealCy;
    public int mRevealR;
    public ImageButton mSendButton;
    public int mTint;
    public RemoteInputViewController mViewController;
    public NotificationViewWrapper mWrapper;
    public final Object mToken = new Object();
    public final ArrayList<Runnable> mOnSendListeners = new ArrayList<>();
    public final ArrayList<Consumer<Boolean>> mOnVisibilityChangedListeners = new ArrayList<>();
    public final ArrayList<View.OnFocusChangeListener> mEditTextFocusChangeListeners = new ArrayList<>();
    public final SendButtonTextWatcher mTextWatcher = new SendButtonTextWatcher();
    public final EditorActionHandler mEditorActionHandler = new EditorActionHandler();
    public final UiEventLogger mUiEventLogger = (UiEventLogger) Dependency.get(UiEventLogger.class);

    /* loaded from: classes.dex */
    public class EditorActionHandler implements TextView.OnEditorActionListener {
        @Override // android.widget.TextView.OnEditorActionListener
        public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            boolean z;
            boolean z2;
            if (keyEvent == null && (i == 6 || i == 5 || i == 4)) {
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
            if (RemoteInputView.this.mEditText.length() > 0 || RemoteInputView.this.mEntry.remoteInputAttachment != null) {
                RemoteInputView remoteInputView = RemoteInputView.this;
                Objects.requireNonNull(remoteInputView);
                Iterator it = new ArrayList(remoteInputView.mOnSendListeners).iterator();
                while (it.hasNext()) {
                    ((Runnable) it.next()).run();
                }
            }
            return true;
        }

        public EditorActionHandler() {
        }
    }

    /* loaded from: classes.dex */
    public static class RemoteEditText extends EditText {
        public static final /* synthetic */ int $r8$clinit = 0;
        public InputMethodManager mInputMethodManager;
        public RemoteInputView mRemoteInputView;
        public boolean mShowImeOnInputConnection;
        public UserHandle mUser;
        public final RemoteInputView$RemoteEditText$$ExternalSyntheticLambda0 mOnReceiveContentListener = new OnReceiveContentListener() { // from class: com.android.systemui.statusbar.policy.RemoteInputView$RemoteEditText$$ExternalSyntheticLambda0
            public final ContentInfo onReceiveContent(View view, ContentInfo contentInfo) {
                RemoteInputView.RemoteEditText remoteEditText = RemoteInputView.RemoteEditText.this;
                int i = RemoteInputView.RemoteEditText.$r8$clinit;
                Objects.requireNonNull(remoteEditText);
                Pair partition = contentInfo.partition(RemoteInputView$RemoteEditText$$ExternalSyntheticLambda1.INSTANCE);
                ContentInfo contentInfo2 = (ContentInfo) partition.first;
                ContentInfo contentInfo3 = (ContentInfo) partition.second;
                if (contentInfo2 != null) {
                    remoteEditText.mRemoteInputView.setAttachment(contentInfo2);
                }
                return contentInfo3;
            }
        };
        public ArraySet<String> mSupportedMimes = new ArraySet<>();
        public LightBarController mLightBarController = (LightBarController) Dependency.get(LightBarController.class);

        @Override // android.widget.TextView, android.view.View, android.view.KeyEvent.Callback
        public final boolean onKeyDown(int i, KeyEvent keyEvent) {
            if (i == 4) {
                return true;
            }
            return super.onKeyDown(i, keyEvent);
        }

        @Override // android.widget.TextView, android.view.View, android.view.KeyEvent.Callback
        public final boolean onKeyUp(int i, KeyEvent keyEvent) {
            if (i != 4) {
                return super.onKeyUp(i, keyEvent);
            }
            defocusIfNeeded(true);
            return true;
        }

        /* JADX WARN: Code restructure failed: missing block: B:5:0x0010, code lost:
            if (r0.mChangingPosition == false) goto L_0x0012;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void defocusIfNeeded(boolean r5) {
            /*
                r4 = this;
                com.android.systemui.statusbar.policy.RemoteInputView r0 = r4.mRemoteInputView
                if (r0 == 0) goto L_0x0012
                com.android.systemui.statusbar.notification.collection.NotificationEntry r0 = r0.mEntry
                java.util.Objects.requireNonNull(r0)
                com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r0 = r0.row
                java.util.Objects.requireNonNull(r0)
                boolean r0 = r0.mChangingPosition
                if (r0 != 0) goto L_0x0018
            L_0x0012:
                boolean r0 = r4.isTemporarilyDetached()
                if (r0 == 0) goto L_0x002b
            L_0x0018:
                boolean r5 = r4.isTemporarilyDetached()
                if (r5 == 0) goto L_0x002a
                com.android.systemui.statusbar.policy.RemoteInputView r5 = r4.mRemoteInputView
                if (r5 == 0) goto L_0x002a
                com.android.systemui.statusbar.notification.collection.NotificationEntry r5 = r5.mEntry
                android.text.Editable r4 = r4.getText()
                r5.remoteInputText = r4
            L_0x002a:
                return
            L_0x002b:
                boolean r0 = r4.isFocusable()
                if (r0 == 0) goto L_0x004d
                boolean r0 = r4.isEnabled()
                if (r0 == 0) goto L_0x004d
                r0 = 0
                r4.setFocusableInTouchMode(r0)
                r4.setFocusable(r0)
                r4.setCursorVisible(r0)
                com.android.systemui.statusbar.policy.RemoteInputView r1 = r4.mRemoteInputView
                if (r1 == 0) goto L_0x004b
                r2 = 1
                java.lang.Object r3 = com.android.systemui.statusbar.policy.RemoteInputView.VIEW_TAG
                r1.onDefocus(r5, r2)
            L_0x004b:
                r4.mShowImeOnInputConnection = r0
            L_0x004d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.RemoteInputView.RemoteEditText.defocusIfNeeded(boolean):void");
        }

        @Override // android.widget.TextView, android.view.View
        public final boolean onCheckIsTextEditor() {
            boolean z;
            RemoteInputView remoteInputView = this.mRemoteInputView;
            if (remoteInputView == null || !remoteInputView.mRemoved) {
                z = false;
            } else {
                z = true;
            }
            if (z || !super.onCheckIsTextEditor()) {
                return false;
            }
            return true;
        }

        @Override // android.view.View
        public final boolean requestRectangleOnScreen(Rect rect) {
            RemoteInputView remoteInputView = this.mRemoteInputView;
            Objects.requireNonNull(remoteInputView);
            RemoteInputController remoteInputController = remoteInputView.mController;
            NotificationEntry notificationEntry = remoteInputView.mEntry;
            Objects.requireNonNull(remoteInputController);
            NotificationStackScrollLayoutController.AnonymousClass14 r2 = (NotificationStackScrollLayoutController.AnonymousClass14) remoteInputController.mDelegate;
            Objects.requireNonNull(r2);
            NotificationStackScrollLayout notificationStackScrollLayout = NotificationStackScrollLayoutController.this.mView;
            Objects.requireNonNull(notificationEntry);
            ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
            Objects.requireNonNull(notificationStackScrollLayout);
            if (notificationStackScrollLayout.mForcedScroll == expandableNotificationRow) {
                return true;
            }
            notificationStackScrollLayout.mForcedScroll = expandableNotificationRow;
            notificationStackScrollLayout.scrollTo(expandableNotificationRow);
            return true;
        }

        public RemoteEditText(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        @Override // android.widget.TextView, android.view.View
        public final void getFocusedRect(Rect rect) {
            super.getFocusedRect(rect);
            int i = ((EditText) this).mScrollY;
            rect.top = i;
            rect.bottom = (((EditText) this).mBottom - ((EditText) this).mTop) + i;
        }

        @Override // android.widget.TextView
        public final void onCommitCompletion(CompletionInfo completionInfo) {
            clearComposingText();
            setText(completionInfo.getText());
            setSelection(getText().length());
        }

        @Override // android.widget.TextView, android.view.View
        public final InputConnection onCreateInputConnection(EditorInfo editorInfo) {
            Context context;
            InputConnection onCreateInputConnection = super.onCreateInputConnection(editorInfo);
            try {
                Context context2 = ((EditText) this).mContext;
                context = context2.createPackageContextAsUser(context2.getPackageName(), 0, this.mUser);
            } catch (PackageManager.NameNotFoundException e) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Unable to create user context:");
                m.append(e.getMessage());
                Log.e("RemoteInput", m.toString(), e);
                context = null;
            }
            if (this.mShowImeOnInputConnection && onCreateInputConnection != null) {
                if (context == null) {
                    context = getContext();
                }
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(InputMethodManager.class);
                this.mInputMethodManager = inputMethodManager;
                if (inputMethodManager != null) {
                    post(new Runnable() { // from class: com.android.systemui.statusbar.policy.RemoteInputView.RemoteEditText.1
                        @Override // java.lang.Runnable
                        public final void run() {
                            RemoteEditText remoteEditText = RemoteEditText.this;
                            remoteEditText.mInputMethodManager.viewClicked(remoteEditText);
                            RemoteEditText remoteEditText2 = RemoteEditText.this;
                            remoteEditText2.mInputMethodManager.showSoftInput(remoteEditText2, 0);
                        }
                    });
                }
            }
            return onCreateInputConnection;
        }

        @Override // android.widget.TextView, android.view.View
        public final void onFocusChanged(boolean z, int i, Rect rect) {
            super.onFocusChanged(z, i, rect);
            RemoteInputView remoteInputView = this.mRemoteInputView;
            if (remoteInputView != null) {
                Object obj = RemoteInputView.VIEW_TAG;
                Objects.requireNonNull(remoteInputView);
                Iterator it = new ArrayList(remoteInputView.mEditTextFocusChangeListeners).iterator();
                while (it.hasNext()) {
                    ((View.OnFocusChangeListener) it.next()).onFocusChange(this, z);
                }
            }
            if (!z) {
                defocusIfNeeded(true);
            }
            RemoteInputView remoteInputView2 = this.mRemoteInputView;
            if (remoteInputView2 != null && !remoteInputView2.mRemoved) {
                LightBarController lightBarController = this.mLightBarController;
                Objects.requireNonNull(lightBarController);
                if (lightBarController.mDirectReplying != z) {
                    lightBarController.mDirectReplying = z;
                    lightBarController.reevaluate();
                }
            }
        }

        @Override // android.widget.TextView, android.view.View
        public final boolean onKeyPreIme(int i, KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() == 4 && keyEvent.getAction() == 1) {
                defocusIfNeeded(true);
            }
            return super.onKeyPreIme(i, keyEvent);
        }

        @Override // android.widget.TextView, android.view.View
        public final void onVisibilityChanged(View view, int i) {
            super.onVisibilityChanged(view, i);
            if (!isShown()) {
                defocusIfNeeded(false);
            }
        }
    }

    /* loaded from: classes.dex */
    public class SendButtonTextWatcher implements TextWatcher {
        @Override // android.text.TextWatcher
        public final void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public final void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public SendButtonTextWatcher() {
        }

        @Override // android.text.TextWatcher
        public final void afterTextChanged(Editable editable) {
            RemoteInputView remoteInputView = RemoteInputView.this;
            Object obj = RemoteInputView.VIEW_TAG;
            remoteInputView.updateSendButton();
        }
    }

    /* loaded from: classes.dex */
    public enum NotificationRemoteInputEvent implements UiEventLogger.UiEventEnum {
        NOTIFICATION_REMOTE_INPUT_OPEN(795),
        NOTIFICATION_REMOTE_INPUT_CLOSE(796),
        NOTIFICATION_REMOTE_INPUT_SEND(797),
        NOTIFICATION_REMOTE_INPUT_FAILURE(798),
        NOTIFICATION_REMOTE_INPUT_ATTACH_IMAGE(825);
        
        private final int mId;

        NotificationRemoteInputEvent(int i) {
            this.mId = i;
        }

        public final int getId() {
            return this.mId;
        }
    }

    public static ColorStateList colorStateListWithDisabledAlpha(int i, int i2) {
        return new ColorStateList(new int[][]{new int[]{-16842910}, new int[0]}, new int[]{ColorUtils.setAlphaComponent(i, i2), i});
    }

    public final void focus() {
        UiEventLogger uiEventLogger = this.mUiEventLogger;
        NotificationRemoteInputEvent notificationRemoteInputEvent = NotificationRemoteInputEvent.NOTIFICATION_REMOTE_INPUT_OPEN;
        NotificationEntry notificationEntry = this.mEntry;
        Objects.requireNonNull(notificationEntry);
        int uid = notificationEntry.mSbn.getUid();
        NotificationEntry notificationEntry2 = this.mEntry;
        Objects.requireNonNull(notificationEntry2);
        String packageName = notificationEntry2.mSbn.getPackageName();
        NotificationEntry notificationEntry3 = this.mEntry;
        Objects.requireNonNull(notificationEntry3);
        uiEventLogger.logWithInstanceId(notificationRemoteInputEvent, uid, packageName, notificationEntry3.mSbn.getInstanceId());
        setVisibility(0);
        NotificationViewWrapper notificationViewWrapper = this.mWrapper;
        if (notificationViewWrapper != null) {
            notificationViewWrapper.setRemoteInputVisible(true);
        }
        RemoteEditText remoteEditText = this.mEditText;
        Objects.requireNonNull(remoteEditText);
        remoteEditText.setFocusableInTouchMode(true);
        remoteEditText.setFocusable(true);
        remoteEditText.setCursorVisible(true);
        remoteEditText.requestFocus();
        RemoteEditText remoteEditText2 = this.mEditText;
        remoteEditText2.mShowImeOnInputConnection = true;
        remoteEditText2.setText(this.mEntry.remoteInputText);
        RemoteEditText remoteEditText3 = this.mEditText;
        remoteEditText3.setSelection(remoteEditText3.length());
        this.mEditText.requestFocus();
        RemoteInputController remoteInputController = this.mController;
        NotificationEntry notificationEntry4 = this.mEntry;
        Object obj = this.mToken;
        Objects.requireNonNull(remoteInputController);
        Objects.requireNonNull(notificationEntry4);
        Objects.requireNonNull(obj);
        if (!remoteInputController.pruneWeakThenRemoveAndContains(notificationEntry4, null, obj)) {
            remoteInputController.mOpen.add(new Pair<>(new WeakReference(notificationEntry4), obj));
        }
        remoteInputController.apply(notificationEntry4);
        setAttachment(this.mEntry.remoteInputAttachment);
        updateSendButton();
    }

    public final Editable getText() {
        return this.mEditText.getText();
    }

    public final boolean isActive() {
        if (!this.mEditText.isFocused() || !this.mEditText.isEnabled()) {
            return false;
        }
        return true;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        if (view == this.mSendButton) {
            Iterator it = new ArrayList(this.mOnSendListeners).iterator();
            while (it.hasNext()) {
                ((Runnable) it.next()).run();
            }
        }
    }

    public final void onDefocus(boolean z, boolean z2) {
        int i;
        this.mController.removeRemoteInput(this.mEntry, this.mToken);
        this.mEntry.remoteInputText = this.mEditText.getText();
        if (!this.mRemoved) {
            if (!z || (i = this.mRevealR) <= 0) {
                setVisibility(8);
                NotificationViewWrapper notificationViewWrapper = this.mWrapper;
                if (notificationViewWrapper != null) {
                    notificationViewWrapper.setRemoteInputVisible(false);
                }
            } else {
                Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(this, this.mRevealCx, this.mRevealCy, i, 0.0f);
                createCircularReveal.setInterpolator(Interpolators.FAST_OUT_LINEAR_IN);
                createCircularReveal.setDuration(150L);
                createCircularReveal.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.policy.RemoteInputView.2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        RemoteInputView.this.setVisibility(8);
                        NotificationViewWrapper notificationViewWrapper2 = RemoteInputView.this.mWrapper;
                        if (notificationViewWrapper2 != null) {
                            notificationViewWrapper2.setRemoteInputVisible(false);
                        }
                    }
                });
                createCircularReveal.start();
            }
        }
        if (z2) {
            UiEventLogger uiEventLogger = this.mUiEventLogger;
            NotificationRemoteInputEvent notificationRemoteInputEvent = NotificationRemoteInputEvent.NOTIFICATION_REMOTE_INPUT_CLOSE;
            NotificationEntry notificationEntry = this.mEntry;
            Objects.requireNonNull(notificationEntry);
            int uid = notificationEntry.mSbn.getUid();
            NotificationEntry notificationEntry2 = this.mEntry;
            Objects.requireNonNull(notificationEntry2);
            String packageName = notificationEntry2.mSbn.getPackageName();
            NotificationEntry notificationEntry3 = this.mEntry;
            Objects.requireNonNull(notificationEntry3);
            uiEventLogger.logWithInstanceId(notificationRemoteInputEvent, uid, packageName, notificationEntry3.mSbn.getInstanceId());
        }
    }

    public final void onNotificationUpdateOrReset() {
        boolean z;
        NotificationViewWrapper notificationViewWrapper;
        if (this.mProgressBar.getVisibility() == 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            this.mResetting = true;
            this.mEntry.remoteInputTextWhenReset = SpannedString.valueOf(this.mEditText.getText());
            this.mEditText.getText().clear();
            this.mEditText.setEnabled(true);
            this.mSendButton.setVisibility(0);
            this.mProgressBar.setVisibility(4);
            RemoteInputController remoteInputController = this.mController;
            NotificationEntry notificationEntry = this.mEntry;
            Objects.requireNonNull(notificationEntry);
            String str = notificationEntry.mKey;
            Object obj = this.mToken;
            Objects.requireNonNull(remoteInputController);
            Objects.requireNonNull(str);
            if (obj == null || remoteInputController.mSpinning.get(str) == obj) {
                remoteInputController.mSpinning.remove(str);
            }
            updateSendButton();
            onDefocus(false, false);
            setAttachment(null);
            this.mResetting = false;
        }
        if (isActive() && (notificationViewWrapper = this.mWrapper) != null) {
            notificationViewWrapper.setRemoteInputVisible(true);
        }
    }

    @Override // android.view.ViewGroup
    public final boolean onRequestSendAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        if (!this.mResetting || view != this.mEditText) {
            return super.onRequestSendAccessibilityEvent(view, accessibilityEvent);
        }
        return false;
    }

    @VisibleForTesting
    public void setAttachment(ContentInfo contentInfo) {
        ContentInfo contentInfo2 = this.mEntry.remoteInputAttachment;
        if (!(contentInfo2 == null || contentInfo2 == contentInfo)) {
            contentInfo2.releasePermissions();
        }
        NotificationEntry notificationEntry = this.mEntry;
        notificationEntry.remoteInputAttachment = contentInfo;
        if (contentInfo != null) {
            notificationEntry.remoteInputUri = contentInfo.getClip().getItemAt(0).getUri();
            this.mEntry.remoteInputMimeType = contentInfo.getClip().getDescription().getMimeType(0);
        }
        View findViewById = findViewById(2131428684);
        ImageView imageView = (ImageView) findViewById(2131428682);
        imageView.setImageDrawable(null);
        if (contentInfo == null) {
            findViewById.setVisibility(8);
            return;
        }
        imageView.setImageURI(contentInfo.getClip().getItemAt(0).getUri());
        if (imageView.getDrawable() == null) {
            findViewById.setVisibility(8);
        } else {
            findViewById.setVisibility(0);
            UiEventLogger uiEventLogger = this.mUiEventLogger;
            NotificationRemoteInputEvent notificationRemoteInputEvent = NotificationRemoteInputEvent.NOTIFICATION_REMOTE_INPUT_ATTACH_IMAGE;
            NotificationEntry notificationEntry2 = this.mEntry;
            Objects.requireNonNull(notificationEntry2);
            int uid = notificationEntry2.mSbn.getUid();
            NotificationEntry notificationEntry3 = this.mEntry;
            Objects.requireNonNull(notificationEntry3);
            String packageName = notificationEntry3.mSbn.getPackageName();
            NotificationEntry notificationEntry4 = this.mEntry;
            Objects.requireNonNull(notificationEntry4);
            uiEventLogger.logWithInstanceId(notificationRemoteInputEvent, uid, packageName, notificationEntry4.mSbn.getInstanceId());
        }
        updateSendButton();
    }

    public final void setBackgroundTintColor(int i, boolean z) {
        int i2;
        ColorStateList colorStateList;
        int i3;
        ColorStateList colorStateList2;
        int i4;
        int i5;
        int i6;
        int i7;
        if (z != this.mColorized || i != this.mTint) {
            this.mColorized = z;
            this.mTint = i;
            if (z) {
                i2 = ((LinearLayout) this).mContext.getResources().getDimensionPixelSize(2131166928);
            } else {
                i2 = 0;
            }
            if (z) {
                boolean isColorDark = Notification.Builder.isColorDark(i);
                int i8 = -1;
                if (isColorDark) {
                    i7 = -1;
                } else {
                    i7 = -16777216;
                }
                if (isColorDark) {
                    i8 = -16777216;
                }
                colorStateList = colorStateListWithDisabledAlpha(i7, 77);
                colorStateList2 = colorStateListWithDisabledAlpha(i7, 153);
                i5 = ColorUtils.setAlphaComponent(i7, 153);
                i3 = i7;
                i4 = i8;
                i6 = i;
            } else {
                colorStateList = ((LinearLayout) this).mContext.getColorStateList(2131100523);
                colorStateList2 = ((LinearLayout) this).mContext.getColorStateList(2131100524);
                i5 = ((LinearLayout) this).mContext.getColor(2131100522);
                i4 = colorStateList2.getDefaultColor();
                TypedArray obtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(new int[]{17956911, 17956912});
                try {
                    i6 = obtainStyledAttributes.getColor(0, i);
                    i3 = obtainStyledAttributes.getColor(1, -7829368);
                    obtainStyledAttributes.close();
                } catch (Throwable th) {
                    if (obtainStyledAttributes != null) {
                        try {
                            obtainStyledAttributes.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            }
            this.mEditText.setTextColor(colorStateList2);
            this.mEditText.setHintTextColor(i5);
            this.mEditText.getTextCursorDrawable().setColorFilter(colorStateList.getDefaultColor(), PorterDuff.Mode.SRC_IN);
            this.mContentBackground.setColor(i6);
            this.mContentBackground.setStroke(i2, colorStateList);
            this.mDelete.setImageTintList(ColorStateList.valueOf(i4));
            this.mDeleteBg.setImageTintList(ColorStateList.valueOf(i3));
            this.mSendButton.setImageTintList(colorStateList);
            this.mProgressBar.setProgressTintList(colorStateList);
            this.mProgressBar.setIndeterminateTintList(colorStateList);
            this.mProgressBar.setSecondaryProgressTintList(colorStateList);
            setBackgroundColor(i);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:12:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void setRemoteInput(android.app.RemoteInput[] r4, android.app.RemoteInput r5, com.android.systemui.statusbar.notification.collection.NotificationEntry.EditedSuggestionInfo r6) {
        /*
            r3 = this;
            r3.mRemoteInputs = r4
            r3.mRemoteInput = r5
            com.android.systemui.statusbar.policy.RemoteInputView$RemoteEditText r4 = r3.mEditText
            java.lang.CharSequence r0 = r5.getLabel()
            r4.setHint(r0)
            com.android.systemui.statusbar.policy.RemoteInputView$RemoteEditText r4 = r3.mEditText
            java.util.Set r5 = r5.getAllowedDataTypes()
            r0 = 0
            if (r5 == 0) goto L_0x002b
            java.util.Objects.requireNonNull(r4)
            boolean r1 = r5.isEmpty()
            if (r1 != 0) goto L_0x002b
            r1 = 0
            java.lang.String[] r1 = new java.lang.String[r1]
            java.lang.Object[] r1 = r5.toArray(r1)
            java.lang.String[] r1 = (java.lang.String[]) r1
            com.android.systemui.statusbar.policy.RemoteInputView$RemoteEditText$$ExternalSyntheticLambda0 r2 = r4.mOnReceiveContentListener
            goto L_0x002d
        L_0x002b:
            r1 = r0
            r2 = r1
        L_0x002d:
            r4.setOnReceiveContentListener(r1, r2)
            android.util.ArraySet<java.lang.String> r1 = r4.mSupportedMimes
            r1.clear()
            android.util.ArraySet<java.lang.String> r4 = r4.mSupportedMimes
            r4.addAll(r5)
            com.android.systemui.statusbar.notification.collection.NotificationEntry r3 = r3.mEntry
            r3.editedSuggestionInfo = r6
            if (r6 == 0) goto L_0x0046
            java.lang.CharSequence r4 = r6.originalText
            r3.remoteInputText = r4
            r3.remoteInputAttachment = r0
        L_0x0046:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.RemoteInputView.setRemoteInput(android.app.RemoteInput[], android.app.RemoteInput, com.android.systemui.statusbar.notification.collection.NotificationEntry$EditedSuggestionInfo):void");
    }

    public final void updateSendButton() {
        boolean z;
        ImageButton imageButton = this.mSendButton;
        if (this.mEditText.length() == 0 && this.mEntry.remoteInputAttachment == null) {
            z = false;
        } else {
            z = true;
        }
        imageButton.setEnabled(z);
    }

    public RemoteInputView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(new int[]{16843829, 17956909});
        this.mTint = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void dispatchFinishTemporaryDetach() {
        if (isAttachedToWindow()) {
            RemoteEditText remoteEditText = this.mEditText;
            attachViewToParent(remoteEditText, 0, remoteEditText.getLayoutParams());
        } else {
            removeDetachedView(this.mEditText, false);
        }
        super.dispatchFinishTemporaryDetach();
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void dispatchStartTemporaryDetach() {
        super.dispatchStartTemporaryDetach();
        int indexOfChild = indexOfChild(this.mEditText);
        if (indexOfChild != -1) {
            detachViewFromParent(indexOfChild);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        RemoteEditText remoteEditText = this.mEditText;
        remoteEditText.mRemoteInputView = this;
        remoteEditText.setOnEditorActionListener(this.mEditorActionHandler);
        this.mEditText.addTextChangedListener(this.mTextWatcher);
        NotificationEntry notificationEntry = this.mEntry;
        Objects.requireNonNull(notificationEntry);
        ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
        Objects.requireNonNull(expandableNotificationRow);
        if (expandableNotificationRow.mChangingPosition && getVisibility() == 0 && this.mEditText.isFocusable()) {
            this.mEditText.requestFocus();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mEditText.removeTextChangedListener(this.mTextWatcher);
        this.mEditText.setOnEditorActionListener(null);
        this.mEditText.mRemoteInputView = null;
        NotificationEntry notificationEntry = this.mEntry;
        Objects.requireNonNull(notificationEntry);
        ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
        Objects.requireNonNull(expandableNotificationRow);
        if (!expandableNotificationRow.mChangingPosition && !isTemporarilyDetached()) {
            this.mController.removeRemoteInput(this.mEntry, this.mToken);
            RemoteInputController remoteInputController = this.mController;
            NotificationEntry notificationEntry2 = this.mEntry;
            Objects.requireNonNull(notificationEntry2);
            String str = notificationEntry2.mKey;
            Object obj = this.mToken;
            Objects.requireNonNull(remoteInputController);
            Objects.requireNonNull(str);
            if (obj == null || remoteInputController.mSpinning.get(str) == obj) {
                remoteInputController.mSpinning.remove(str);
            }
        }
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mProgressBar = (ProgressBar) findViewById(2131428687);
        ImageButton imageButton = (ImageButton) findViewById(2131428688);
        this.mSendButton = imageButton;
        imageButton.setOnClickListener(this);
        this.mContentBackground = (GradientDrawable) ((LinearLayout) this).mContext.getDrawable(2131232608).mutate();
        this.mDelete = (ImageView) findViewById(2131428685);
        ImageView imageView = (ImageView) findViewById(2131428686);
        this.mDeleteBg = imageView;
        imageView.setImageTintBlendMode(BlendMode.SRC_IN);
        this.mDelete.setImageTintBlendMode(BlendMode.SRC_IN);
        this.mDelete.setOnClickListener(new RemoteInputView$$ExternalSyntheticLambda0(this, 0));
        ((LinearLayout) findViewById(2131428683)).setBackground(this.mContentBackground);
        RemoteEditText remoteEditText = (RemoteEditText) findViewById(2131428689);
        this.mEditText = remoteEditText;
        Objects.requireNonNull(remoteEditText);
        remoteEditText.setFocusableInTouchMode(false);
        remoteEditText.setFocusable(false);
        remoteEditText.setCursorVisible(false);
        this.mEditText.setWindowInsetsAnimationCallback(new WindowInsetsAnimation.Callback() { // from class: com.android.systemui.statusbar.policy.RemoteInputView.1
            @Override // android.view.WindowInsetsAnimation.Callback
            public final WindowInsets onProgress(WindowInsets windowInsets, List<WindowInsetsAnimation> list) {
                return windowInsets;
            }

            @Override // android.view.WindowInsetsAnimation.Callback
            public final void onEnd(WindowInsetsAnimation windowInsetsAnimation) {
                super.onEnd(windowInsetsAnimation);
                if (windowInsetsAnimation.getTypeMask() == WindowInsets.Type.ime()) {
                    RemoteInputView remoteInputView = RemoteInputView.this;
                    NotificationEntry notificationEntry = remoteInputView.mEntry;
                    notificationEntry.mRemoteEditImeAnimatingAway = false;
                    notificationEntry.mRemoteEditImeVisible = remoteInputView.mEditText.getRootWindowInsets().isVisible(WindowInsets.Type.ime());
                    RemoteInputView remoteInputView2 = RemoteInputView.this;
                    NotificationEntry notificationEntry2 = remoteInputView2.mEntry;
                    if (!notificationEntry2.mRemoteEditImeVisible && !remoteInputView2.mEditText.mShowImeOnInputConnection) {
                        remoteInputView2.mController.removeRemoteInput(notificationEntry2, remoteInputView2.mToken);
                    }
                }
            }
        });
    }

    @Override // android.view.ViewGroup
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            RemoteInputController remoteInputController = this.mController;
            Objects.requireNonNull(remoteInputController);
            NotificationStackScrollLayoutController.AnonymousClass14 r0 = (NotificationStackScrollLayoutController.AnonymousClass14) remoteInputController.mDelegate;
            Objects.requireNonNull(r0);
            NotificationStackScrollLayout notificationStackScrollLayout = NotificationStackScrollLayoutController.this.mView;
            Objects.requireNonNull(notificationStackScrollLayout);
            notificationStackScrollLayout.cancelLongPress();
            NotificationStackScrollLayout notificationStackScrollLayout2 = NotificationStackScrollLayoutController.this.mView;
            Objects.requireNonNull(notificationStackScrollLayout2);
            notificationStackScrollLayout2.mDisallowDismissInThisMotion = true;
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        return true;
    }

    @Override // android.view.View
    public final void onVisibilityChanged(View view, int i) {
        boolean z;
        super.onVisibilityChanged(view, i);
        if (view == this) {
            Iterator it = new ArrayList(this.mOnVisibilityChangedListeners).iterator();
            while (it.hasNext()) {
                Consumer consumer = (Consumer) it.next();
                if (i == 0) {
                    z = true;
                } else {
                    z = false;
                }
                consumer.accept(Boolean.valueOf(z));
            }
            if (i != 0 && !this.mController.isRemoteInputActive()) {
                RemoteEditText remoteEditText = this.mEditText;
                int i2 = RemoteEditText.$r8$clinit;
                Objects.requireNonNull(remoteEditText);
                WindowInsetsController windowInsetsController = remoteEditText.getWindowInsetsController();
                if (windowInsetsController != null) {
                    windowInsetsController.hide(WindowInsets.Type.ime());
                }
            }
        }
    }

    public final void stealFocusFrom(RemoteInputView remoteInputView) {
        Objects.requireNonNull(remoteInputView);
        RemoteEditText remoteEditText = remoteInputView.mEditText;
        int i = RemoteEditText.$r8$clinit;
        remoteEditText.defocusIfNeeded(false);
        this.mPendingIntent = remoteInputView.mPendingIntent;
        setRemoteInput(remoteInputView.mRemoteInputs, remoteInputView.mRemoteInput, this.mEntry.editedSuggestionInfo);
        int i2 = remoteInputView.mRevealCx;
        int i3 = remoteInputView.mRevealCy;
        int i4 = remoteInputView.mRevealR;
        this.mRevealCx = i2;
        this.mRevealCy = i3;
        this.mRevealR = i4;
        this.mViewController.setPendingIntent(remoteInputView.mPendingIntent);
        this.mViewController.setRemoteInput(remoteInputView.mRemoteInput);
        this.mViewController.setRemoteInputs(remoteInputView.mRemoteInputs);
        focus();
    }
}
