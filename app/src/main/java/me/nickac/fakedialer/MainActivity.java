package me.nickac.fakedialer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import me.nickac.fakedialer.fragment.DialpadFragment;

public class MainActivity extends AppCompatActivity implements DialpadFragment.OnFragmentEventListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DialpadFragment fragment = new DialpadFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_screen_dialpad_container, fragment)
        .commitNow();
        fragment.setCanDigitsBeEdited(true);
    }

    @Override
    public void closeDialpad(View v) {

    }
}
