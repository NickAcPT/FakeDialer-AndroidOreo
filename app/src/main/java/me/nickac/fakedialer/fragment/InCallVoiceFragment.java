package me.nickac.fakedialer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import me.nickac.fakedialer.R;
import me.nickac.fakedialer.model.Call;
import me.nickac.fakedialer.model.CallScreenButton;
import me.nickac.fakedialer.utils.SharedObjects;
import me.nickac.fakedialer.views.CheckableLabeledButton;
import me.nickac.fakedialer.views.LetterTileDrawable;
import me.nickac.fakedialer.views.LockableViewPager;

public class InCallVoiceFragment extends Fragment {
    private static final String ARG_CONTACT = "call";
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

    LockableViewPager pager;
    private ImageView avatarImage;
    private LetterTileDrawable letterTile;

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
        attachFieldsToFragment(view);
        return view;
    }

    public void attachButtonsToFragment() {
        if (getFragmentManager() == null /*&& SharedObjects.INSTANCE.getFragmentTransaction() == null*/) return;
        getFragmentManager().beginTransaction()
                .add(R.id.incall_pager, new InCallButtonGridFragment().withCallVoiceFragment(this)).commit();
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
            setShown(forwardIcon, call.isForwardedCall());
            setShown(workIcon, call.isWorkProfileCall());

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
        if (mListener != null) {
            mListener.onButtonPress(v, button);
        }
    }

    public void onEndCallButtonPress(ImageButton v) {
        if (mListener != null) {
            mListener.onEndCallButtonPress(v);
        }
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
