package com.darshan09200.employeemanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.darshan09200.employeemanagementsystem.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements OnFabClickListener {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction().replace(R.id.primaryFragment, new HomeFragment()).commit();
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() < 1) {
                hideBackButton();
            } else if (!isSplitLayoutActive()) {
                showBackButton();
            }
        });
    }

    @Override
    public void onFabClick() {
        if (isSplitLayoutActive()) {
            hideBackButton();
            getSupportFragmentManager().beginTransaction().add(R.id.secondaryFragment, new RegistrationFragment()).commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.from_right,
                            R.anim.to_left,
                            R.anim.from_left,
                            R.anim.to_right
                    )
                    .replace(R.id.primaryFragment, new RegistrationFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }

    private boolean isSplitLayoutActive() {
        return getResources().getBoolean(R.bool.isSplitLayoutActive);
    }

    private void showBackButton() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Registration");
    }

    private void hideBackButton() {
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.app_name);
    }

    private void simulateBackPressed() {
        getSupportFragmentManager().popBackStack();
        hideBackButton();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            simulateBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}