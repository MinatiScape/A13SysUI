package com.android.systemui.navigationbar.buttons;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.hardware.input.InputManager;
import android.media.AudioManager;
import android.metrics.LogMaker;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.systemui.Dependency;
import com.android.systemui.R$styleable;
import com.android.systemui.navigationbar.buttons.KeyButtonRipple;
import com.android.systemui.recents.OverviewProxyService;
import java.util.Objects;
/* loaded from: classes.dex */
public class KeyButtonView extends ImageView implements ButtonInterface {
    public AudioManager mAudioManager;
    public final AnonymousClass1 mCheckLongPress;
    public int mCode;
    public int mContentDescriptionRes;
    public float mDarkIntensity;
    public long mDownTime;
    public boolean mGestureAborted;
    public boolean mHasOvalBg;
    public final InputManager mInputManager;
    @VisibleForTesting
    public boolean mLongClicked;
    public final MetricsLogger mMetricsLogger;
    public View.OnClickListener mOnClickListener;
    public final Paint mOvalBgPaint;
    public final OverviewProxyService mOverviewProxyService;
    public final boolean mPlaySounds;
    public final KeyButtonRipple mRipple;
    public int mTouchDownX;
    public int mTouchDownY;
    public final UiEventLogger mUiEventLogger;

    public KeyButtonView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public final boolean performAccessibilityActionInternal(int i, Bundle bundle) {
        if (i == 16 && this.mCode != 0) {
            sendEvent(0, 0, SystemClock.uptimeMillis());
            sendEvent(1, 0);
            sendAccessibilityEvent(1);
            playSoundEffect(0);
            return true;
        } else if (i != 32 || this.mCode == 0) {
            return super.performAccessibilityActionInternal(i, bundle);
        } else {
            sendEvent(0, 128);
            sendEvent(1, 0);
            sendAccessibilityEvent(2);
            return true;
        }
    }

    public final void sendEvent(int i, int i2) {
        sendEvent(i, i2, SystemClock.uptimeMillis());
    }

    @Override // com.android.systemui.navigationbar.buttons.ButtonInterface
    public final void setVertical(boolean z) {
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public enum NavBarButtonEvent implements UiEventLogger.UiEventEnum {
        NAVBAR_HOME_BUTTON_TAP(533),
        NAVBAR_BACK_BUTTON_TAP(534),
        NAVBAR_OVERVIEW_BUTTON_TAP(535),
        NAVBAR_IME_SWITCHER_BUTTON_TAP(923),
        NAVBAR_HOME_BUTTON_LONGPRESS(536),
        NAVBAR_BACK_BUTTON_LONGPRESS(537),
        NAVBAR_OVERVIEW_BUTTON_LONGPRESS(538),
        NONE(0);
        
        private final int mId;

        NavBarButtonEvent(int i) {
            this.mId = i;
        }

        public final int getId() {
            return this.mId;
        }
    }

    public KeyButtonView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, InputManager.getInstance(), new UiEventLoggerImpl());
    }

    @Override // com.android.systemui.navigationbar.buttons.ButtonInterface
    public final void abortCurrentGesture() {
        Log.d("b/63783866", "KeyButtonView.abortCurrentGesture");
        if (this.mCode != 0) {
            sendEvent(1, 32);
        }
        setPressed(false);
        KeyButtonRipple keyButtonRipple = this.mRipple;
        Objects.requireNonNull(keyButtonRipple);
        keyButtonRipple.mHandler.removeCallbacksAndMessages(null);
        this.mGestureAborted = true;
    }

    @Override // android.view.View
    public final void draw(Canvas canvas) {
        if (this.mHasOvalBg) {
            float min = Math.min(getWidth(), getHeight());
            canvas.drawOval(0.0f, 0.0f, min, min, this.mOvalBgPaint);
        }
        super.draw(canvas);
    }

    @Override // android.view.View
    public final boolean isClickable() {
        if (this.mCode != 0 || super.isClickable()) {
            return true;
        }
        return false;
    }

    @Override // android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z;
        boolean z2;
        View.OnClickListener onClickListener;
        boolean shouldShowSwipeUpUI = this.mOverviewProxyService.shouldShowSwipeUpUI();
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mGestureAborted = false;
        }
        if (this.mGestureAborted) {
            setPressed(false);
            return false;
        }
        if (action == 0) {
            this.mDownTime = SystemClock.uptimeMillis();
            this.mLongClicked = false;
            setPressed(true);
            this.mTouchDownX = (int) motionEvent.getRawX();
            this.mTouchDownY = (int) motionEvent.getRawY();
            if (this.mCode != 0) {
                sendEvent(0, 0, this.mDownTime);
            } else {
                performHapticFeedback(1);
            }
            if (!shouldShowSwipeUpUI) {
                playSoundEffect(0);
            }
            removeCallbacks(this.mCheckLongPress);
            postDelayed(this.mCheckLongPress, ViewConfiguration.getLongPressTimeout());
        } else if (action == 1) {
            if (!isPressed() || this.mLongClicked) {
                z = false;
            } else {
                z = true;
            }
            setPressed(false);
            if (SystemClock.uptimeMillis() - this.mDownTime > 150) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (shouldShowSwipeUpUI) {
                if (z) {
                    performHapticFeedback(1);
                    playSoundEffect(0);
                }
            } else if (z2 && !this.mLongClicked) {
                performHapticFeedback(8);
            }
            if (this.mCode != 0) {
                if (z) {
                    sendEvent(1, 0);
                    sendAccessibilityEvent(1);
                } else {
                    sendEvent(1, 32);
                }
            } else if (z && (onClickListener = this.mOnClickListener) != null) {
                onClickListener.onClick(this);
                sendAccessibilityEvent(1);
            }
            removeCallbacks(this.mCheckLongPress);
        } else if (action == 2) {
            int rawY = (int) motionEvent.getRawY();
            float scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop() * 3.0f;
            if (Math.abs(((int) motionEvent.getRawX()) - this.mTouchDownX) > scaledTouchSlop || Math.abs(rawY - this.mTouchDownY) > scaledTouchSlop) {
                setPressed(false);
                removeCallbacks(this.mCheckLongPress);
            }
        } else if (action == 3) {
            setPressed(false);
            if (this.mCode != 0) {
                sendEvent(1, 32);
            }
            removeCallbacks(this.mCheckLongPress);
        }
        return true;
    }

    @Override // android.view.View
    public final void playSoundEffect(int i) {
        if (this.mPlaySounds) {
            this.mAudioManager.playSoundEffect(i, ActivityManager.getCurrentUser());
        }
    }

    public final void sendEvent(int i, int i2, long j) {
        UiEventLogger.UiEventEnum uiEventEnum;
        this.mMetricsLogger.write(new LogMaker(931).setType(4).setSubtype(this.mCode).addTaggedData(933, Integer.valueOf(i)).addTaggedData(932, Integer.valueOf(i2)));
        int i3 = i2 & 128;
        boolean z = i3 != 0;
        UiEventLogger.UiEventEnum uiEventEnum2 = NavBarButtonEvent.NONE;
        if ((i != 1 || !this.mLongClicked) && ((i != 0 || z) && (i2 & 32) == 0 && (i2 & 256) == 0)) {
            int i4 = this.mCode;
            if (i4 != 3) {
                if (i4 != 4) {
                    if (i4 != 187) {
                        uiEventEnum = uiEventEnum2;
                    } else if (z) {
                        uiEventEnum = NavBarButtonEvent.NAVBAR_OVERVIEW_BUTTON_LONGPRESS;
                    } else {
                        uiEventEnum = NavBarButtonEvent.NAVBAR_OVERVIEW_BUTTON_TAP;
                    }
                } else if (z) {
                    uiEventEnum = NavBarButtonEvent.NAVBAR_BACK_BUTTON_LONGPRESS;
                } else {
                    uiEventEnum = NavBarButtonEvent.NAVBAR_BACK_BUTTON_TAP;
                }
            } else if (z) {
                uiEventEnum = NavBarButtonEvent.NAVBAR_HOME_BUTTON_LONGPRESS;
            } else {
                uiEventEnum = NavBarButtonEvent.NAVBAR_HOME_BUTTON_TAP;
            }
            if (uiEventEnum != uiEventEnum2) {
                this.mUiEventLogger.log(uiEventEnum);
            }
        }
        if (this.mCode == 4 && i2 != 128) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Back button event: ");
            m.append(KeyEvent.actionToString(i));
            Log.i("KeyButtonView", m.toString());
            if (i == 1) {
                this.mOverviewProxyService.notifyBackAction((i2 & 32) == 0, -1, -1, true, false);
            }
        }
        KeyEvent keyEvent = new KeyEvent(this.mDownTime, j, i, this.mCode, i3 != 0 ? 1 : 0, 0, -1, 0, i2 | 8 | 64, 257);
        int displayId = getDisplay() != null ? getDisplay().getDisplayId() : -1;
        if (displayId != -1) {
            keyEvent.setDisplayId(displayId);
        }
        this.mInputManager.injectInputEvent(keyEvent, 0);
    }

    @Override // com.android.systemui.navigationbar.buttons.ButtonInterface
    public final void setDarkIntensity(float f) {
        boolean z;
        this.mDarkIntensity = f;
        Drawable drawable = getDrawable();
        if (drawable != null) {
            ((KeyButtonDrawable) drawable).setDarkIntensity(f);
            invalidate();
        }
        KeyButtonRipple keyButtonRipple = this.mRipple;
        Objects.requireNonNull(keyButtonRipple);
        if (f >= 0.5f) {
            z = true;
        } else {
            z = false;
        }
        keyButtonRipple.mDark = z;
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.navigationbar.buttons.KeyButtonView$1] */
    @VisibleForTesting
    public KeyButtonView(Context context, AttributeSet attributeSet, int i, InputManager inputManager, UiEventLogger uiEventLogger) {
        super(context, attributeSet);
        this.mMetricsLogger = (MetricsLogger) Dependency.get(MetricsLogger.class);
        this.mOvalBgPaint = new Paint(3);
        this.mHasOvalBg = false;
        this.mCheckLongPress = new Runnable() { // from class: com.android.systemui.navigationbar.buttons.KeyButtonView.1
            @Override // java.lang.Runnable
            public final void run() {
                if (!KeyButtonView.this.isPressed()) {
                    return;
                }
                if (KeyButtonView.this.isLongClickable()) {
                    KeyButtonView.this.performLongClick();
                    KeyButtonView.this.mLongClicked = true;
                    return;
                }
                KeyButtonView keyButtonView = KeyButtonView.this;
                if (keyButtonView.mCode != 0) {
                    keyButtonView.sendEvent(0, 128);
                    KeyButtonView.this.sendAccessibilityEvent(2);
                }
                KeyButtonView.this.mLongClicked = true;
            }
        };
        this.mUiEventLogger = uiEventLogger;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.KeyButtonView, i, 0);
        this.mCode = obtainStyledAttributes.getInteger(1, 0);
        this.mPlaySounds = obtainStyledAttributes.getBoolean(2, true);
        TypedValue typedValue = new TypedValue();
        if (obtainStyledAttributes.getValue(0, typedValue)) {
            this.mContentDescriptionRes = typedValue.resourceId;
        }
        obtainStyledAttributes.recycle();
        setClickable(true);
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
        KeyButtonRipple keyButtonRipple = new KeyButtonRipple(context, this);
        this.mRipple = keyButtonRipple;
        this.mOverviewProxyService = (OverviewProxyService) Dependency.get(OverviewProxyService.class);
        this.mInputManager = inputManager;
        setBackground(keyButtonRipple);
        setWillNotDraw(false);
        forceHasOverlappingRendering(false);
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int i = this.mContentDescriptionRes;
        if (i != 0) {
            setContentDescription(((ImageView) this).mContext.getString(i));
        }
    }

    @Override // android.view.View
    public final void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (this.mCode != 0) {
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, null));
            if (isLongClickable()) {
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(32, null));
            }
        }
    }

    @Override // android.view.View
    public final void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        if (i != 0) {
            jumpDrawablesToCurrentState();
        }
    }

    @Override // android.widget.ImageView, com.android.systemui.navigationbar.buttons.ButtonInterface
    public final void setImageDrawable(Drawable drawable) {
        boolean z;
        KeyButtonRipple.Type type;
        super.setImageDrawable(drawable);
        if (drawable != null) {
            KeyButtonDrawable keyButtonDrawable = (KeyButtonDrawable) drawable;
            keyButtonDrawable.setDarkIntensity(this.mDarkIntensity);
            Color color = keyButtonDrawable.mState.mOvalBackgroundColor;
            boolean z2 = true;
            if (color != null) {
                z = true;
            } else {
                z = false;
            }
            this.mHasOvalBg = z;
            if (z) {
                this.mOvalBgPaint.setColor(color.toArgb());
            }
            KeyButtonRipple keyButtonRipple = this.mRipple;
            if (keyButtonDrawable.mState.mOvalBackgroundColor == null) {
                z2 = false;
            }
            if (z2) {
                type = KeyButtonRipple.Type.OVAL;
            } else {
                type = KeyButtonRipple.Type.ROUNDED_RECT;
            }
            Objects.requireNonNull(keyButtonRipple);
            keyButtonRipple.mType = type;
        }
    }

    @Override // android.view.View
    public final void setOnClickListener(View.OnClickListener onClickListener) {
        super.setOnClickListener(onClickListener);
        this.mOnClickListener = onClickListener;
    }
}
