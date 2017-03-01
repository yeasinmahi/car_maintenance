package com.gits.sami.carmaintenance.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gits.sami.carmaintenance.R;
import com.gits.sami.carmaintenance.db.DbHelper;
import com.gits.sami.carmaintenance.model.Bill;
import com.gits.sami.carmaintenance.others.Utility;

import static com.gits.sami.carmaintenance.others.Utility.getDate;
import static com.gits.sami.carmaintenance.others.Utility.getMyText;
import static com.gits.sami.carmaintenance.others.Utility.isEmpty;


public class EntryFragment extends Fragment implements View.OnClickListener {

    DbHelper dbHelper;
    private EditText jobNameEditText;
    private EditText mileageEditText;
    private EditText descriptionEditText;
    private EditText costEditText;
    private EditText dateEditText;
    private EditText garageNameEditText;
    private EditText garageAddressEditText;
    private Button saveButton;

    public EntryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void Init(View rootView) {
        dbHelper = new DbHelper(rootView.getContext());
        jobNameEditText = (EditText) rootView.findViewById(R.id.jobNameEditText);
        mileageEditText = (EditText) rootView.findViewById(R.id.mileageEditText);
        descriptionEditText = (EditText) rootView.findViewById(R.id.descriptionEditText);
        costEditText = (EditText) rootView.findViewById(R.id.costEditText);
        dateEditText = (EditText) rootView.findViewById(R.id.dateEditText);
        garageNameEditText = (EditText) rootView.findViewById(R.id.garageNameEditText);
        garageAddressEditText = (EditText) rootView.findViewById(R.id.garageAddressEditText);
        saveButton = (Button) rootView.findViewById(R.id.eSaveButton);
        saveButton.setOnClickListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_entry, container, false);
        Init(rootView);
        return rootView;
    }

    private boolean validateEntry() {

        if (isEmpty(jobNameEditText)) {
            jobNameEditText.setError(Utility.getErrorMsg());
            return false;
        }
        if (isEmpty(mileageEditText)) {
            mileageEditText.setError(Utility.getErrorMsg());
            return false;
        }
        if (isEmpty(descriptionEditText)) {
            descriptionEditText.setError(Utility.getErrorMsg());
            return false;
        }
        if (isEmpty(costEditText)) {
            costEditText.setError(Utility.getErrorMsg());
            return false;
        } else {
            try {
                Double.parseDouble(getMyText(costEditText));
            } catch (Exception e) {
                costEditText.setError("Check amount properly");
                return false;
            }
        }
        if (isEmpty(dateEditText)) {
            dateEditText.setError(Utility.getErrorMsg());
            return false;
        }
        if (getDate(getMyText(dateEditText), Utility.myDateFormat.dd_MMM_yyyy).equals(Utility.ErrorDate)) {
            dateEditText.setError("Date format error");
            return false;
        }
        return true;
    }

    private Bill getElectricity() {
        if (validateEntry()) {
            Bill bill = new Bill();
            bill.jobName = getMyText(jobNameEditText);
            bill.mileage = getMyText(mileageEditText);
            bill.description = getMyText(descriptionEditText);
            bill.cost = Double.parseDouble(getMyText(costEditText));
            bill.date = getDate(getMyText(dateEditText), Utility.myDateFormat.dd_MMM_yyyy);
            bill.garageName = getMyText(garageNameEditText);
            bill.garageAddress = getMyText(garageAddressEditText);
            return bill;
        } else {
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        Bill bill = getElectricity();
        if (bill != null) {
            if (dbHelper.insertBill(bill)) {
                Toast.makeText(v.getContext(), "Insertion Successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(v.getContext(), "Insertion failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
