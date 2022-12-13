package com.darshan09200.employeemanagementsystem;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.darshan09200.employeemanagementsystem.databinding.EmployeeItemBinding;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAdapter extends ArrayAdapter {
    private final LayoutInflater layoutInflater;
    private final int layoutResource;

    private ArrayList<Employee> employees;

    public EmployeeAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Employee> employees) {
        super(context, resource);
        this.layoutInflater = LayoutInflater.from(context);
        this.layoutResource = resource;
        this.employees = employees;
    }

    @Override
    public int getCount() {
        return employees.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        EmployeeItemBinding binding;
        if (v == null) {
            binding = EmployeeItemBinding.inflate(layoutInflater, parent, false);
            v = binding.getRoot();
        } else {
            binding = EmployeeItemBinding.bind(convertView);
        }

        Employee employee = employees.get(position);

        binding.empId.setText(employee.getEmpId());
        binding.name.setText(employee.getName());
        binding.role.setText(employee.getRole().getLabel());
        String filepath = employee.getProfileImage();
        if (filepath.length() > 0)
            binding.profileImage.setImageBitmap(BitmapFactory.decodeFile(filepath));
        String activeEmpId = Database.getInstance().getViewEmpId();
        if (activeEmpId.length() > 0 && activeEmpId.equals(employee.getEmpId())) {
            binding.parent.setBackgroundResource(R.color.secondary);
            binding.name.setTextColor(ContextCompat.getColor(getContext(), R.color.textOnSecondary));
            binding.empId.setTextColor(ContextCompat.getColor(getContext(), R.color.textOnSecondary));
            binding.empId.setAlpha(0.75f);
            binding.role.setTextColor(ContextCompat.getColor(getContext(), R.color.textOnSecondary));
            binding.role.setAlpha(0.75f);
            binding.rightArrow.setColorFilter(ContextCompat.getColor(getContext(), R.color.textOnSecondary));
        } else {
            binding.parent.setBackground(null);
            binding.name.setTextColor(ContextCompat.getColor(getContext(), R.color.text));
            binding.empId.setTextColor(ContextCompat.getColor(getContext(), R.color.textLight));
            binding.empId.setAlpha(1f);
            binding.role.setTextColor(ContextCompat.getColor(getContext(), R.color.textLight));
            binding.role.setAlpha(1f);
            binding.rightArrow.clearColorFilter();
        }

        return v;
    }
}
