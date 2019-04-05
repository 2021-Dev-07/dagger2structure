package cof.gof.dagger2structure.mainActivity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import javax.inject.Inject;

import cof.gof.dagger2structure.R;
import cof.gof.dagger2structure.databinding.ActivityMainBinding;
import cof.gof.dagger2structure.secondaryActivity.SecondaryActivity;
import cof.gof.dagger2structure.shared.Flumbolator;
import cof.gof.dagger2structure.shared.User;
import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();

    @Inject
    Flumbolator flumbolator;

    @Inject
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent secondary = new Intent(MainActivity.this, SecondaryActivity.class);
                startActivity(secondary);
            }
        });
        binding.textView.setText(flumbolate());
        binding.textViewSec.setText(getUserName());
    }

    public String flumbolate() {
        return flumbolator.frumbolateMe();
    }

    public String getUserName(){
        return user.getName();
    }

    public String getNameForUser(){
       return "Roshan";
    }
}
