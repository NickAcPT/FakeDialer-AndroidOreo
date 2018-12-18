package me.nickac.fakedialer.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import me.nickac.fakedialer.R;

public enum CallScreenButton {
    MUTE(
            R.string.incall_content_description_muted,
            R.string.incall_content_description_unmuted,
            R.string.incall_label_mute,
            true,
            R.drawable.quantum_ic_mic_off_vd_theme_24),
    DIALPAD(
            0,
            0,
            R.string.incall_label_dialpad,
            false,
            R.drawable.quantum_ic_dialpad_vd_theme_24),
    SPEAKER(
            R.string.incall_content_description_speaker,
            R.string.incall_content_description_earpiece,
            R.string.incall_label_speaker,
            true,
            R.drawable.quantum_ic_volume_up_vd_theme_24),
    ADD_CALL(
            0,
            0,
            R.string.incall_label_add_call,
            false,
            R.drawable.quantum_ic_add_call_vd_theme_24),
    HOLD(
            R.string.incall_content_description_unhold,
            R.string.incall_content_description_hold,
            R.string.incall_label_hold,
            true,
            R.drawable.quantum_ic_pause_vd_theme_24);
    private final int contentDescriptionChecked;
    private final int contentDescriptionUnchecked;
    private final int label;
    private final int icon;
    private boolean checkable;

    CallScreenButton(@StringRes int contentDescriptionChecked, @StringRes int contentDescriptionUnchecked, @StringRes int label, boolean checkable, @DrawableRes int icon) {
        this.contentDescriptionChecked = contentDescriptionChecked;
        this.contentDescriptionUnchecked = contentDescriptionUnchecked;
        this.label = label;
        this.checkable = checkable;
        this.icon = icon;
    }

    public boolean isCheckable() {
        return checkable;
    }

    public int getContentDescriptionChecked() {
        return contentDescriptionChecked;
    }

    public int getContentDescriptionUnchecked() {
        return contentDescriptionUnchecked;
    }

    public int getLabel() {
        return label;
    }

    public int getIcon() {
        return icon;
    }
}
