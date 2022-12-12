package com.darshan09200.employeemanagementsystem;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
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

    ArrayList<Employee> employees;
    ArrayAdapter<Employee> adapter;

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

        employees = new ArrayList<>();
        adapter = new ArrayAdapter<Employee>(getContext(), android.R.layout.simple_list_item_2, android.R.id.text1, employees) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);
                Employee employee = getItem(position);
                text1.setText(employee.getName());
                text2.setText(employee.getRole().getLabel());
                return view;
            }
        };
        adapter.setNotifyOnChange(true);
        binding.listView.setAdapter(adapter);
        refreshData();
        binding.listView.setEmptyView(binding.noRecords);
        binding.listView.setOnItemClickListener((parent, view, position, id) -> {
            Employee employee = (Employee) parent.getItemAtPosition(position);
            Database.getInstance().setViewEmpId(employee.getEmpId());
            clickListener.onItemClick();
        });

        binding.addEmployee.setOnClickListener(v -> clickListener.onFabClick());

        return binding.getRoot();
    }

    public void refreshData() {
        employees.clear();
        employees.addAll(Database.getInstance().getEmployees());
        if (employees.size() == 0)
            binding.noRecords.setText("Use the '+' icon to add a new employee");
        else binding.noRecords.setText("No Records Found");
        adapter.notifyDataSetChanged();
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
            if (employee.getName().toLowerCase().indexOf(text) > -1 || employee.getRole().getLabel().toLowerCase().indexOf(text) > -1)
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
