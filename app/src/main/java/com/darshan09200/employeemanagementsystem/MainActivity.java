package com.darshan09200.employeemanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.darshan09200.employeemanagementsystem.databinding.ActivityMainBinding;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity implements OnClickListener, OnRegistrationActionListener, OnDetailsActionListener {

    ActivityMainBinding binding;

    MenuItem searchViewItem;
    SearchView searchView;

    private int searchVisibility = -1;

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

        if (Database.getInstance().getEmployees().size() == 0) {
            Database.getInstance().addEmployee(
                    new Manager(
                            "EMP-001",
                            "Darshan Jain",
                            LocalDate.of(2006, 12, 11),
                            68.0,
                            1000.0,
                            98,
                            new Motorcycle(
                                    VehicleMake.KAWASAKI,
                                    "567-098",
                                    VehicleColor.BLACK,
                                    VehicleCategory.RACE_MOTORCYCLE,
                                    true
                            )
                    )
            );
            Database.getInstance().addEmployee(
                    new Manager(
                            "EMP-002",
                            "Test2 Jain",
                            LocalDate.of(2006, 12, 11),
                            68.0,
                            1000.0,
                            98,
                            new Motorcycle(
                                    VehicleMake.KAWASAKI,
                                    "567-098",
                                    VehicleColor.BLACK,
                                    VehicleCategory.RACE_MOTORCYCLE,
                                    true
                            )
                    )
            );
            Database.getInstance().addEmployee(
                    new Manager(
                            "EMP-003",
                            "Test1 Jain",
                            LocalDate.of(2006, 12, 11),
                            68.0,
                            1000.0,
                            98,
                            new Motorcycle(
                                    VehicleMake.KAWASAKI,
                                    "567-098",
                                    VehicleColor.BLACK,
                                    VehicleCategory.RACE_MOTORCYCLE,
                                    true
                            )
                    )
            );
        }
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
        clearSearch();

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
        clearSearch();
        if (isSplitLayoutActive()) {
            refreshEmployeeListData();
            refreshEmployeeViewData(true);
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

    private void refreshEmployeeViewData(boolean navigate) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.secondaryFragment);
        if (fragment instanceof DetailsFragment) {
            DetailsFragment detailsFragment = (DetailsFragment) fragment;
            detailsFragment.setupData();
        } else if (navigate) {
            emptyFragmentStack("secondary");
            getSupportFragmentManager().beginTransaction().add(R.id.secondaryFragment, new DetailsFragment()).addToBackStack("secondary").commit();
        }
    }

    private void refreshEmployeeListData() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.primaryFragment);
        if (fragment instanceof HomeFragment) {
            HomeFragment homeFragment = (HomeFragment) fragment;
            homeFragment.refreshData();
        }
    }

    private boolean isSplitLayoutActive() {
        return getResources().getBoolean(R.bool.isSplitLayoutActive);
    }

    private void showSearch() {
        System.out.println("called show");
        if (searchViewItem != null)
            searchViewItem.setVisible(true);
        else
            searchVisibility = View.VISIBLE;
    }

    private void hideSearch() {
        System.out.println("called hide");
        if (searchViewItem != null)
            searchViewItem.setVisible(false);
        else
            searchVisibility = View.GONE;
    }

    private void clearSearch() {
        if (searchView != null) {
            searchView.setIconified(true);
            searchView.setIconified(true);
        }
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
        refreshEmployeeListData();
        refreshEmployeeViewData(false);
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
        refreshEmployeeListData();
    }

    @Override
    public void onDelete() {
        searchView.setIconified(true);
        onSubmit();
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
        System.out.println("menu called");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        searchViewItem = menu.findItem(R.id.search_bar);
        searchView = (SearchView) searchViewItem.getActionView();

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

        if (searchVisibility != -1) {
            searchViewItem.setVisible(searchVisibility == View.VISIBLE);
            searchVisibility = -1;
        }

        return super.onCreateOptionsMenu(menu);
    }
}