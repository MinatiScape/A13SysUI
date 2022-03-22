package com.android.systemui.statusbar.notification.row;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.metrics.LogMaker;
import android.os.Bundle;
import android.provider.Settings;
import android.service.notification.SnoozeCriterion;
import android.service.notification.StatusBarNotification;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.KeyValueListParser;
import android.util.Log;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.PathInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda9;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public class NotificationSnooze extends LinearLayout implements NotificationGuts.GutsContent, View.OnClickListener {
    public int mCollapsedHeight;
    public NotificationSnoozeOption mDefaultOption;
    public View mDivider;
    public AnimatorSet mExpandAnimation;
    public ImageView mExpandButton;
    public boolean mExpanded;
    public NotificationGuts mGutsContainer;
    public MetricsLogger mMetricsLogger = new MetricsLogger();
    public KeyValueListParser mParser = new KeyValueListParser(',');
    public StatusBarNotification mSbn;
    public NotificationSwipeActionHelper.SnoozeOption mSelectedOption;
    public TextView mSelectedOptionText;
    public NotificationSwipeActionHelper mSnoozeListener;
    public ViewGroup mSnoozeOptionContainer;
    public ArrayList mSnoozeOptions;
    public View mSnoozeView;
    public boolean mSnoozing;
    public TextView mUndoButton;
    public static final LogMaker OPTIONS_OPEN_LOG = new LogMaker(1142).setType(1);
    public static final LogMaker OPTIONS_CLOSE_LOG = new LogMaker(1142).setType(2);
    public static final LogMaker UNDO_LOG = new LogMaker(1141).setType(4);
    public static final int[] sAccessibilityActions = {2131427451, 2131427450, 2131427448, 2131427449};

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final boolean isLeavebehind() {
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final boolean needsFalsingProtection() {
        return false;
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final boolean shouldBeSaved() {
        return true;
    }

    /* loaded from: classes.dex */
    public class NotificationSnoozeOption implements NotificationSwipeActionHelper.SnoozeOption {
        public AccessibilityNodeInfo.AccessibilityAction mAction;
        public CharSequence mConfirmation;
        public SnoozeCriterion mCriterion;
        public CharSequence mDescription;
        public int mMinutesToSnoozeFor;

        public NotificationSnoozeOption(SnoozeCriterion snoozeCriterion, int i, CharSequence charSequence, CharSequence charSequence2, AccessibilityNodeInfo.AccessibilityAction accessibilityAction) {
            this.mCriterion = snoozeCriterion;
            this.mMinutesToSnoozeFor = i;
            this.mDescription = charSequence;
            this.mConfirmation = charSequence2;
            this.mAction = accessibilityAction;
        }

        @Override // com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper.SnoozeOption
        public final AccessibilityNodeInfo.AccessibilityAction getAccessibilityAction() {
            return this.mAction;
        }

        @Override // com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper.SnoozeOption
        public final CharSequence getConfirmation() {
            return this.mConfirmation;
        }

        @Override // com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper.SnoozeOption
        public final CharSequence getDescription() {
            return this.mDescription;
        }

        @Override // com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper.SnoozeOption
        public final int getMinutesToSnoozeFor() {
            return this.mMinutesToSnoozeFor;
        }

        @Override // com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper.SnoozeOption
        public final SnoozeCriterion getSnoozeCriterion() {
            return this.mCriterion;
        }
    }

    public final void createOptionViews() {
        this.mSnoozeOptionContainer.removeAllViews();
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
        for (int i = 0; i < this.mSnoozeOptions.size(); i++) {
            NotificationSwipeActionHelper.SnoozeOption snoozeOption = (NotificationSwipeActionHelper.SnoozeOption) this.mSnoozeOptions.get(i);
            TextView textView = (TextView) layoutInflater.inflate(2131624334, this.mSnoozeOptionContainer, false);
            this.mSnoozeOptionContainer.addView(textView);
            textView.setText(snoozeOption.getDescription());
            textView.setTag(snoozeOption);
            textView.setOnClickListener(this);
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final int getActualHeight() {
        if (this.mExpanded) {
            return getHeight();
        }
        return this.mCollapsedHeight;
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final View getContentView() {
        setSelected(this.mDefaultOption, false);
        return this;
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final boolean handleCloseControls(boolean z, boolean z2) {
        NotificationSwipeActionHelper.SnoozeOption snoozeOption;
        if (!this.mExpanded || z2) {
            NotificationSwipeActionHelper notificationSwipeActionHelper = this.mSnoozeListener;
            if (notificationSwipeActionHelper == null || (snoozeOption = this.mSelectedOption) == null) {
                setSelected((NotificationSwipeActionHelper.SnoozeOption) this.mSnoozeOptions.get(0), false);
                return false;
            }
            this.mSnoozing = true;
            notificationSwipeActionHelper.snooze(this.mSbn, snoozeOption);
            return true;
        }
        showSnoozeOptions(false);
        return true;
    }

    public final void logOptionSelection(int i, NotificationSwipeActionHelper.SnoozeOption snoozeOption) {
        this.mMetricsLogger.write(new LogMaker(i).setType(4).addTaggedData(1140, Integer.valueOf(this.mSnoozeOptions.indexOf(snoozeOption))).addTaggedData(1139, Long.valueOf(TimeUnit.MINUTES.toMillis(snoozeOption.getMinutesToSnoozeFor()))));
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        LogMaker logMaker;
        NotificationGuts notificationGuts = this.mGutsContainer;
        if (notificationGuts != null) {
            notificationGuts.resetFalsingCheck();
        }
        int id = view.getId();
        NotificationSwipeActionHelper.SnoozeOption snoozeOption = (NotificationSwipeActionHelper.SnoozeOption) view.getTag();
        if (snoozeOption != null) {
            setSelected(snoozeOption, true);
        } else if (id == 2131428520) {
            showSnoozeOptions(!this.mExpanded);
            MetricsLogger metricsLogger = this.mMetricsLogger;
            if (!this.mExpanded) {
                logMaker = OPTIONS_OPEN_LOG;
            } else {
                logMaker = OPTIONS_CLOSE_LOG;
            }
            metricsLogger.write(logMaker);
        } else {
            this.mSelectedOption = null;
            showSnoozeOptions(false);
            this.mGutsContainer.closeControls(view, false);
            this.mMetricsLogger.write(UNDO_LOG);
        }
    }

    public final boolean requestAccessibilityFocus() {
        if (this.mExpanded) {
            return super.requestAccessibilityFocus();
        }
        this.mSnoozeView.requestAccessibilityFocus();
        return false;
    }

    public final void setSelected(NotificationSwipeActionHelper.SnoozeOption snoozeOption, boolean z) {
        this.mSelectedOption = snoozeOption;
        this.mSelectedOptionText.setText(snoozeOption.getConfirmation());
        showSnoozeOptions(false);
        int childCount = this.mSnoozeOptionContainer.getChildCount();
        int i = 0;
        while (true) {
            int i2 = 8;
            if (i >= childCount) {
                break;
            }
            View childAt = this.mSnoozeOptionContainer.getChildAt(i);
            if (childAt.getTag() != this.mSelectedOption) {
                i2 = 0;
            }
            childAt.setVisibility(i2);
            i++;
        }
        if (z) {
            this.mSnoozeView.sendAccessibilityEvent(8);
            logOptionSelection(1138, snoozeOption);
        }
    }

    public final void showSnoozeOptions(final boolean z) {
        int i;
        float f;
        PathInterpolator pathInterpolator;
        NotificationGuts.OnHeightChangedListener onHeightChangedListener;
        if (z) {
            i = 17302379;
        } else {
            i = 17302438;
        }
        this.mExpandButton.setImageResource(i);
        if (this.mExpanded != z) {
            this.mExpanded = z;
            AnimatorSet animatorSet = this.mExpandAnimation;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            View view = this.mDivider;
            Property property = View.ALPHA;
            float[] fArr = new float[2];
            fArr[0] = view.getAlpha();
            float f2 = 1.0f;
            if (z) {
                f = 1.0f;
            } else {
                f = 0.0f;
            }
            fArr[1] = f;
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, property, fArr);
            ViewGroup viewGroup = this.mSnoozeOptionContainer;
            Property property2 = View.ALPHA;
            float[] fArr2 = new float[2];
            fArr2[0] = viewGroup.getAlpha();
            if (!z) {
                f2 = 0.0f;
            }
            fArr2[1] = f2;
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(viewGroup, property2, fArr2);
            this.mSnoozeOptionContainer.setVisibility(0);
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.mExpandAnimation = animatorSet2;
            animatorSet2.playTogether(ofFloat, ofFloat2);
            this.mExpandAnimation.setDuration(150L);
            AnimatorSet animatorSet3 = this.mExpandAnimation;
            if (z) {
                pathInterpolator = Interpolators.ALPHA_IN;
            } else {
                pathInterpolator = Interpolators.ALPHA_OUT;
            }
            animatorSet3.setInterpolator(pathInterpolator);
            this.mExpandAnimation.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.notification.row.NotificationSnooze.1
                public boolean cancelled = false;

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationCancel(Animator animator) {
                    this.cancelled = true;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    if (!z && !this.cancelled) {
                        NotificationSnooze.this.mSnoozeOptionContainer.setVisibility(4);
                        NotificationSnooze.this.mSnoozeOptionContainer.setAlpha(0.0f);
                    }
                }
            });
            this.mExpandAnimation.start();
            NotificationGuts notificationGuts = this.mGutsContainer;
            if (notificationGuts != null && (onHeightChangedListener = notificationGuts.mHeightListener) != null) {
                StatusBar$$ExternalSyntheticLambda9 statusBar$$ExternalSyntheticLambda9 = (StatusBar$$ExternalSyntheticLambda9) onHeightChangedListener;
                NotificationGutsManager notificationGutsManager = (NotificationGutsManager) statusBar$$ExternalSyntheticLambda9.f$0;
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) statusBar$$ExternalSyntheticLambda9.f$1;
                Objects.requireNonNull(notificationGutsManager);
                notificationGutsManager.mListContainer.onHeightChanged(expandableNotificationRow, expandableNotificationRow.isShown());
            }
        }
    }

    public NotificationSnooze(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @VisibleForTesting
    public ArrayList<NotificationSwipeActionHelper.SnoozeOption> getDefaultSnoozeOptions() {
        boolean z;
        int i;
        int i2;
        NotificationSnoozeOption notificationSnoozeOption;
        Resources resources = getContext().getResources();
        ArrayList<NotificationSwipeActionHelper.SnoozeOption> arrayList = new ArrayList<>();
        try {
            this.mParser.setString(Settings.Global.getString(getContext().getContentResolver(), "notification_snooze_options"));
        } catch (IllegalArgumentException unused) {
            Log.e("NotificationSnooze", "Bad snooze constants");
        }
        int i3 = this.mParser.getInt("default", resources.getInteger(2131492897));
        int[] intArray = this.mParser.getIntArray("options_array", resources.getIntArray(2130903096));
        for (int i4 = 0; i4 < intArray.length; i4++) {
            int[] iArr = sAccessibilityActions;
            if (i4 >= iArr.length) {
                break;
            }
            int i5 = intArray[i4];
            int i6 = iArr[i4];
            Resources resources2 = getResources();
            if (i5 >= 60) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                i = 2131820558;
            } else {
                i = 2131820559;
            }
            if (z) {
                i2 = i5 / 60;
            } else {
                i2 = i5;
            }
            String quantityString = resources2.getQuantityString(i, i2, Integer.valueOf(i2));
            String format = String.format(resources2.getString(2131953302), quantityString);
            AccessibilityNodeInfo.AccessibilityAction accessibilityAction = new AccessibilityNodeInfo.AccessibilityAction(i6, quantityString);
            int indexOf = format.indexOf(quantityString);
            if (indexOf == -1) {
                notificationSnoozeOption = new NotificationSnoozeOption(null, i5, quantityString, format, accessibilityAction);
            } else {
                SpannableString spannableString = new SpannableString(format);
                spannableString.setSpan(new StyleSpan(1, resources2.getConfiguration().fontWeightAdjustment), indexOf, quantityString.length() + indexOf, 0);
                notificationSnoozeOption = new NotificationSnoozeOption(null, i5, quantityString, spannableString, accessibilityAction);
            }
            if (i4 == 0 || i5 == i3) {
                this.mDefaultOption = notificationSnoozeOption;
            }
            arrayList.add(notificationSnoozeOption);
        }
        return arrayList;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        logOptionSelection(1137, this.mDefaultOption);
        dispatchConfigurationChanged(getResources().getConfiguration());
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mCollapsedHeight = getResources().getDimensionPixelSize(2131167034);
        View findViewById = findViewById(2131428520);
        this.mSnoozeView = findViewById;
        findViewById.setOnClickListener(this);
        this.mSelectedOptionText = (TextView) findViewById(2131428887);
        TextView textView = (TextView) findViewById(2131429146);
        this.mUndoButton = textView;
        textView.setOnClickListener(this);
        this.mExpandButton = (ImageView) findViewById(2131427944);
        View findViewById2 = findViewById(2131427855);
        this.mDivider = findViewById2;
        findViewById2.setAlpha(0.0f);
        ViewGroup viewGroup = (ViewGroup) findViewById(2131428888);
        this.mSnoozeOptionContainer = viewGroup;
        viewGroup.setVisibility(4);
        this.mSnoozeOptionContainer.setAlpha(0.0f);
        this.mSnoozeOptions = getDefaultSnoozeOptions();
        createOptionViews();
        setSelected(this.mDefaultOption, false);
    }

    @Override // android.view.View
    public final void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(2131427452, getResources().getString(2131953301)));
        int size = this.mSnoozeOptions.size();
        for (int i = 0; i < size; i++) {
            AccessibilityNodeInfo.AccessibilityAction accessibilityAction = ((NotificationSwipeActionHelper.SnoozeOption) this.mSnoozeOptions.get(i)).getAccessibilityAction();
            if (accessibilityAction != null) {
                accessibilityNodeInfo.addAction(accessibilityAction);
            }
        }
    }

    public final boolean performAccessibilityActionInternal(int i, Bundle bundle) {
        if (super.performAccessibilityActionInternal(i, bundle)) {
            return true;
        }
        if (i == 2131427452) {
            TextView textView = this.mUndoButton;
            this.mSelectedOption = null;
            showSnoozeOptions(false);
            this.mGutsContainer.closeControls(textView, false);
            return true;
        }
        for (int i2 = 0; i2 < this.mSnoozeOptions.size(); i2++) {
            NotificationSwipeActionHelper.SnoozeOption snoozeOption = (NotificationSwipeActionHelper.SnoozeOption) this.mSnoozeOptions.get(i2);
            if (snoozeOption.getAccessibilityAction() != null && snoozeOption.getAccessibilityAction().getId() == i) {
                setSelected(snoozeOption, true);
                return true;
            }
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final void setGutsParent(NotificationGuts notificationGuts) {
        this.mGutsContainer = notificationGuts;
    }

    @VisibleForTesting
    public void setKeyValueListParser(KeyValueListParser keyValueListParser) {
        this.mParser = keyValueListParser;
    }

    @VisibleForTesting
    public NotificationSwipeActionHelper.SnoozeOption getDefaultOption() {
        return this.mDefaultOption;
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public final boolean willBeRemoved() {
        return this.mSnoozing;
    }
}
