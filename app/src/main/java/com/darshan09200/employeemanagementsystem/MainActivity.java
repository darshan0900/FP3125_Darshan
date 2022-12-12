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

        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.primaryFragment, new HomeFragment())
                .addToBackStack("primary")
                .commit();
        if (Database.getInstance().isEditActive()) {
            onFabClick();
        }
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (isSplitLayoutActive()) {
                hideBackButton();
            } else {
                int primaryCount = 0;
                for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                    FragmentManager.BackStackEntry backStackEntry = getSupportFragmentManager().getBackStackEntryAt(i);
                    if (backStackEntry.getName().equals("primary")) primaryCount++;
                }
                if (primaryCount > 1) {
                    showBackButton();
                } else {
                    hideBackButton();
                }
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
        closeRegistration();
        closeEmpView();
        getSupportFragmentManager().popBackStack(isSplitLayoutActive() ? "secondary" : "primary", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onBackPressed() {
        closeRegistration();
        closeEmpView();
        super.onBackPressed();
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
        simulateBackPressed();
    }

    private void closeRegistration() {
        Database.getInstance().setEditActive(false);
    }

    private void closeEmpView() {
        Database.getInstance().setViewEmpId("");
    }
}