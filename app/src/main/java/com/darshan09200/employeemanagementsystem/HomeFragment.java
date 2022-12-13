package com.darshan09200.employeemanagementsystem;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.darshan09200.employeemanagementsystem.databinding.FragmentHomeBinding;

import java.util.ArrayList;

interface OnClickListener {
    void onFabClick();

    void onItemClick();
}

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    OnClickListener clickListener;

    ArrayList<Employee> employees = new ArrayList<>();
    EmployeeAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnClickListener) {
            clickListener = (OnClickListener) context;
        } else {
            throw new ClassCastException(context
                    + "must implement OnClickListener interface"
            );
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        adapter = new EmployeeAdapter(getContext(), R.layout.employee_item, employees);
        adapter.setNotifyOnChange(true);
        binding.listView.setAdapter(adapter);
        refreshData();
        binding.listView.setEmptyView(binding.noRecords);
        binding.listView.setOnItemClickListener((parent, view, position, id) -> {
            System.out.println(position);
            Employee employee = employees.get(position);
            Database.getInstance().setViewEmpId(employee.getEmpId());
            HomeFragment.this.clickListener.onItemClick();
        });

        binding.addEmployee.setOnClickListener(v -> clickListener.onFabClick());

        return binding.getRoot();
    }

    public void refreshData() {
        employees.clear();
        employees.addAll(Database.getInstance().getEmployees());
        if (binding != null) {
            if (employees.size() == 0)
                binding.noRecords.setText("Use the '+' icon to add a new employee");
            else binding.noRecords.setText("No Records Found");
            adapter.notifyDataSetChanged();
        }
    }

    public void filterItems(String input, boolean showToast) {
        String text = input.trim().toLowerCase();

        System.out.println(input);

        if (text.length() == 0) {
            refreshData();
            return;
        }
        ArrayList<Employee> allEmployeesData = Database.getInstance().getEmployees();
        ArrayList<Employee> filteredEmployees = new ArrayList<>();
        allEmployeesData.forEach(employee -> {
            if (
                    employee.getFirstName().toLowerCase().contains(text)
                            || employee.getRole().getLabel().toLowerCase().contains(text)
                            || employee.getEmpId().toLowerCase().contains(text)
            )
                filteredEmployees.add(employee);
        });
        employees.clear();
        employees.addAll(filteredEmployees);
        adapter.notifyDataSetChanged();

        if (showToast && filteredEmployees.size() == 0) {
            Toast.makeText(getActivity(), "Not found", Toast.LENGTH_LONG).show();
        }
    }
}
