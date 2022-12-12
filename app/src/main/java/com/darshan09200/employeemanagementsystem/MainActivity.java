package com.darshan09200.employeemanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.darshan09200.employeemanagementsystem.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnClickListener, OnRegistrationActionListener, OnDetailsActionListener {

    ActivityMainBinding binding;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        emptyFragmentStack(null);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.primaryFragment, new HomeFragment())
                .commit();
        if (Database.getInstance().isEditActive()) {
            onFabClick();
        } else if (Database.getInstance().getViewEmpId().length() > 0) {
            onItemClick();
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
                if (primaryCount > 0) {
                    showBackButton();
                } else {
                    hideBackButton();
                }
            }
        });
    }

    private void emptyFragmentStack(String name) {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            FragmentManager.BackStackEntry backStackEntry = fm.getBackStackEntryAt(i);
            if (name != null) {
                if (backStackEntry.getName().equals(name)) fm.popBackStack();
            } else {
                fm.popBackStack();
            }
        }

    }

    @Override
    public void onFabClick() {
        if (isSplitLayoutActive()) {
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

    @Override
    public void onItemClick() {
        if (isSplitLayoutActive()) {
            refreshViewData(true);
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.from_right,
                            R.anim.to_left,
                            R.anim.from_left,
                            R.anim.to_right
                    )
                    .add(R.id.primaryFragment, new DetailsFragment())
                    .addToBackStack("primary")
                    .commit();
        }
    }

    private void refreshViewData(boolean navigate) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.secondaryFragment);
        if (fragment instanceof DetailsFragment) {
            DetailsFragment detailsFragment = (DetailsFragment) fragment;
            detailsFragment.setupData();
        } else if (navigate) {
            emptyFragmentStack("secondary");
            getSupportFragmentManager().beginTransaction().add(R.id.secondaryFragment, new DetailsFragment()).addToBackStack("secondary").commit();
        }
    }

    private boolean isSplitLayoutActive() {
        return getResources().getBoolean(R.bool.isSplitLayoutActive);
    }

    private void showSearch() {
        searchView.setVisibility(View.VISIBLE);
    }

    private void hideSearch() {
        searchView.setVisibility(View.GONE);
    }

    private void showBackButton() {
        hideSearch();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Database.getInstance().isEditActive()) {
            if (Registration.getInstance().isEdit())
                getSupportActionBar().setTitle("Edit");
            else
                getSupportActionBar().setTitle("Registration");
        } else {
            getSupportActionBar().setTitle(Database.getInstance().getViewEmpId());
        }
    }

    private void hideBackButton() {
        showSearch();
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.app_name);
    }

    private void simulateBackPressed() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        closeRegistration();
        if (Database.getInstance().getViewEmpId().length() > 0) {
            Database.getInstance().setViewEmpId("");
        }
        super.onBackPressed();
        refreshViewData(false);
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
    public void onEdit() {
        onFabClick();
    }

    @Override
    public void onClose() {
        simulateBackPressed();
    }

    private void closeRegistration() {
        Database.getInstance().setEditActive(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.search_bar);
        searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.primaryFragment);
                if (fragment instanceof HomeFragment) {
                    HomeFragment homeFragment = (HomeFragment) fragment;
                    homeFragment.filterItems(query, true);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.primaryFragment);
                if (fragment instanceof HomeFragment) {
                    HomeFragment homeFragment = (HomeFragment) fragment;
                    homeFragment.filterItems(newText, false);
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}