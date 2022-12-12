package com.darshan09200.employeemanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.darshan09200.employeemanagementsystem.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements OnFabClickListener, OnRegistrationActionListener {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction().replace(R.id.primaryFragment, new HomeFragment()).commit();
        if (Database.getInstance().isEditActive()) {
            onFabClick();
        }
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
            getSupportFragmentManager().beginTransaction().add(R.id.secondaryFragment, new RegistrationFragment()).addToBackStack("secondary").commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.from_right,
                            R.anim.to_left,
                            R.anim.from_left,
                            R.anim.to_right
                    )
                    .add(R.id.primaryFragment, new RegistrationFragment())
                    .addToBackStack("primary")
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
        Database.getInstance().setEditActive(false);
        Database.getInstance().setViewEmpId("");
        getSupportFragmentManager().popBackStack("primary", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        hideBackButton();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            simulateBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSubmit() {
        onClose();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.primaryFragment);
        if (fragment instanceof HomeFragment) {
            HomeFragment homeFragment = (HomeFragment) fragment;
            homeFragment.refreshData();
        }
    }

    @Override
    public void onClose() {
        Database.getInstance().setEditActive(false);
        if (isSplitLayoutActive()) {
            getSupportFragmentManager().popBackStack("secondary", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            simulateBackPressed();
        }
    }
}