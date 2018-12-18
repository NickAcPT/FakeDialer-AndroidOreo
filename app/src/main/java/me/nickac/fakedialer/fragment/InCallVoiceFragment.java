package me.nickac.fakedialer.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import me.nickac.fakedialer.R;
import me.nickac.fakedialer.model.Call;
import me.nickac.fakedialer.model.CallScreenButton;
import me.nickac.fakedialer.utils.SharedObjects;
import me.nickac.fakedialer.utils.animation.AnimUtils;
import me.nickac.fakedialer.utils.animation.AnimationListenerAdapter;
import me.nickac.fakedialer.views.CheckableLabeledButton;
import me.nickac.fakedialer.views.LetterTileDrawable;
import me.nickac.fakedialer.views.LockableViewPager;

public class InCallVoiceFragment extends Fragment {
    private static final String ARG_CONTACT = "call";
    LockableViewPager pager;
    private Call call;
    private OnFragmentInteractionListener mListener;
    private TextView contactName, bottomText;
    private Chronometer bottomTimer;
    private ImageView hdIcon;
    private ImageView workIcon;
    private ImageView forwardIcon;
    private ImageView forwardedNumber;
    private ImageView spamIcon;
    private ImageButton endCallButton;
    private ImageView avatarImage;
    private boolean dialpadVisible;
    private LetterTileDrawable letterTile;
    private DialpadFragment dialPadFragment;
    private Animation dialpadSlideInAnimation;
    private Animation dialpadSlideOutAnimation;
    private InCallButtonGridFragment buttonGridFragment;

    public InCallVoiceFragment() {
        // Required empty public constructor
    }

    public static InCallVoiceFragment createInstance(Call call) {
        InCallVoiceFragment fragment = new InCallVoiceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CONTACT, SharedObjects.INSTANCE.getGson().toJson(call));
        fragment.setArguments(args);
        fragment.attachContactToFields();
        return fragment;
    }

    public static boolean isRtl() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            call = SharedObjects.INSTANCE.getGson().fromJson(getArguments().getString(ARG_CONTACT), Call.class);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_incall_voice, container, false);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        addDialpadFragment(transaction);
        transaction.commitNow();

        attachFieldsToFragment(view);
        return view;
    }

    public boolean isDialpadVisible() {
        return dialpadVisible;
    }

    public void initAnimations(Context ctx) {
        boolean isLandscape =
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        boolean isRtl = isRtl();
        if (isLandscape) {
            dialpadSlideInAnimation =
                    AnimationUtils.loadAnimation(
                            ctx, isRtl ? R.anim.dialpad_slide_in_left : R.anim.dialpad_slide_in_right);
            dialpadSlideOutAnimation =
                    AnimationUtils.loadAnimation(
                            ctx, isRtl ? R.anim.dialpad_slide_out_left : R.anim.dialpad_slide_out_right);
        } else {
            dialpadSlideInAnimation = AnimationUtils.loadAnimation(ctx, R.anim.dialpad_slide_in_bottom);
            dialpadSlideOutAnimation =
                    AnimationUtils.loadAnimation(ctx, R.anim.dialpad_slide_out_bottom);
        }
        dialpadSlideInAnimation.setInterpolator(AnimUtils.EASE_IN);
        dialpadSlideOutAnimation.setInterpolator(AnimUtils.EASE_OUT);

        dialpadSlideInAnimation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                dialpadVisible = true;
            }
        });
        dialpadSlideOutAnimation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                dialpadVisible = false;
            }
        });
    }

    public void attachButtonsToFragment() {
        if (getFragmentManager() == null /*&& SharedObjects.INSTANCE.getFragmentTransaction() == null*/)
            return;
        buttonGridFragment = new InCallButtonGridFragment().withCallVoiceFragment(this);
        getChildFragmentManager().beginTransaction()
                .add(R.id.incall_pager, buttonGridFragment)
                .commitAllowingStateLoss();
        getFragmentManager().executePendingTransactions();
    }

    private void attachFieldsToFragment(View view) {
        view.findViewById(R.id.contactgrid_connection_icon).setVisibility(View.GONE);
        view.findViewById(R.id.contactgrid_status_text).setVisibility(View.GONE);
        contactName = view.findViewById(R.id.contactgrid_contact_name);
        avatarImage = view.findViewById(R.id.contactgrid_avatar);
        bottomText = view.findViewById(R.id.contactgrid_bottom_text);
        bottomTimer = view.findViewById(R.id.contactgrid_bottom_timer);
        hdIcon = view.findViewById(R.id.contactgrid_hdIcon);
        workIcon = view.findViewById(R.id.contactgrid_workIcon);
        forwardIcon = view.findViewById(R.id.contactgrid_forwardIcon);
        spamIcon = view.findViewById(R.id.contactgrid_spamIcon);
        endCallButton = view.findViewById(R.id.incall_end_call);
        pager = view.findViewById(R.id.incall_pager);
        attachContactToFields();
    }

    private void setShown(View v, boolean shown) {
        if (v != null) {
            v.setVisibility(shown ? View.VISIBLE : View.GONE);
        }
    }

    private void attachContactToFields() {
        if (call != null) {
            contactName.setText(call.getContact().getName());
            setShown(hdIcon, call.isHdCall());
            setShown(spamIcon, call.isSpamCall());
            setShown(workIcon, call.isWorkProfileCall());

            hdIcon.setBackground(getResources().getDrawable(R.drawable.asd_hd_icon, getContext().getTheme()));

            letterTile.setCanonicalDialerLetterTileDetails(
                    call.getContact().getName(),
                    call.getContact().getNumber(),
                    LetterTileDrawable.SHAPE_CIRCLE,
                    LetterTileDrawable.getContactTypeFromPrimitives(
                            false,
                            call.isSpamCall(),
                            false,
                            TelecomManager.PRESENTATION_ALLOWED,
                            false));

            avatarImage.invalidate();
            avatarImage.setBackground(letterTile);
            endCallButton.setOnClickListener(v -> onEndCallButtonPress((ImageButton) v));
            startTimer();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            letterTile = new LetterTileDrawable(context.getResources());
            mListener = (OnFragmentInteractionListener) context;
            initAnimations(context);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void startTimer() {
        if (bottomTimer == null) return;
        Log.d("InVoiceCallFragment", "Started timer.");
        bottomTimer.setVisibility(View.VISIBLE);
        bottomTimer.start();
    }

    public void stopTimer() {
        if (bottomTimer == null) return;
        Log.d("InVoiceCallFragment", "Stopped timer.");
        bottomTimer.stop();
    }

    public void onButtonPress(CheckableLabeledButton v, CallScreenButton button) {
        if (mListener != null) mListener.onButtonPress(v, button);
    }

    public void onEndCallButtonPress(ImageButton v) {
        if (mListener != null) {
            mListener.onEndCallButtonPress(v);
        }
    }

    private int getDialpadContainerId() {
        return R.id.incall_dialpad_container;
    }

    private void addDialpadFragment(FragmentTransaction transaction) {
        if (dialPadFragment != null) {
            return;
        }
        dialPadFragment = new DialpadFragment();
        transaction.add(getDialpadContainerId(), dialPadFragment);
        transaction.hide(dialPadFragment);
    }

    private void animateDialpad(boolean show) {
        if (show)
            dialPadFragment.animateShowDialpad();
        View view = dialPadFragment
                .getView();
        if (view != null) {
            Animation animation = show ? dialpadSlideInAnimation : dialpadSlideOutAnimation;
            if (animation != null) {
                view.startAnimation(animation);
            }
        }
    }

    public void showDialpad() {
        if (dialPadFragment == null)
            return;
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.show(dialPadFragment);
        transaction.commitAllowingStateLoss();
        manager.executePendingTransactions();
        dialPadFragment.setUserVisibleHint(false);

        animateDialpad(true);
    }

    public void hideDialpad() {
        if (dialPadFragment == null)
            return;
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.hide(dialPadFragment);
        transaction.commitAllowingStateLoss();
        manager.executePendingTransactions();
        dialPadFragment.setUserVisibleHint(false);

        //Uncheck the dialpad button
        if (buttonGridFragment != null) {
            CheckableLabeledButton button = buttonGridFragment.getButton(CallScreenButton.DIALPAD);
            if (button != null) {
                button.setChecked(false);
            }
        }

        animateDialpad(false);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onButtonPress(CheckableLabeledButton v, CallScreenButton button);

        void onEndCallButtonPress(ImageButton v);

    }
}
