package me.nickac.fakedialer;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import me.nickac.fakedialer.fragment.DialpadFragment;
import me.nickac.fakedialer.fragment.InCallVoiceFragment;
import me.nickac.fakedialer.model.Call;
import me.nickac.fakedialer.model.CallScreenButton;
import me.nickac.fakedialer.model.Contact;
import me.nickac.fakedialer.views.CheckableLabeledButton;

public class InScreenDialerActivity extends AppCompatActivity implements InCallVoiceFragment.OnFragmentInteractionListener, DialpadFragment.OnFragmentEventListener {

    private InCallVoiceFragment voiceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideSystemUI();
        createFragment();
    }

    private void createFragment() {
        Call call = new Call(new Contact("122", "Mike"));
        prepareForCall(call);
        voiceFragment = InCallVoiceFragment.createInstance(call);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction
                .add(
                        R.id.incall_fragment_container,
                        voiceFragment
                )
                .commitNow();
        voiceFragment.attachButtonsToFragment();
        voiceFragment.initAnimations(this);

    }

    private void prepareForCall(Call call) {
        View rootView = findViewById(R.id.incall_fragment_container).getRootView();
        rootView.setBackground(getDrawable(call.isSpamCall() ? R.drawable.incall_spam_background_gradient : R.drawable.incall_background_gradient));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    }

    @Override
    public void onButtonPress(CheckableLabeledButton v, CallScreenButton button) {
        if (button.isCheckable())
            v.setChecked(!v.isChecked());
        if (button == CallScreenButton.DIALPAD) {
            if (v.isChecked()) {
                voiceFragment.showDialpad();
            }
        }
    }

    @Override
    public void onEndCallButtonPress(ImageButton v) {
        finish();
    }

    @Override
    public void onBackPressed() {
        if (voiceFragment != null && voiceFragment.isDialpadVisible()) {
            voiceFragment.hideDialpad();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void closeDialpad(View v) {
        voiceFragment.hideDialpad();
    }
}
