package com.gits.sami.carmaintenance.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.gits.sami.carmaintenance.R;
import com.gits.sami.carmaintenance.db.DbHelper;
import com.gits.sami.carmaintenance.model.Bill;
import com.gits.sami.carmaintenance.others.Utility;

import java.util.ArrayList;
import java.util.Date;

import static com.gits.sami.carmaintenance.others.Utility.getDate;
import static com.gits.sami.carmaintenance.others.Utility.getMyText;
import static com.gits.sami.carmaintenance.others.Utility.isEmpty;
import static com.gits.sami.carmaintenance.others.Utility.myDateFormat;


public class ReportsFragment extends Fragment implements View.OnClickListener {

    private TableLayout tableLayout;
    private DbHelper dbHelper;
    private EditText froEditText;
    private EditText toEditText;
    private Button fromButton;
    private Button toButton;
    private Button searchButton;

    public ReportsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        Init(view);
        dbHelper = new DbHelper(getContext());
        tableLayout = (TableLayout) view.findViewById(R.id.reportTable);
        getBillDate();
        return view;
    }

    private void Init(View view) {
        froEditText = (EditText) view.findViewById(R.id.fromDateEditText);
        toEditText = (EditText) view.findViewById(R.id.toDateEditText);
        searchButton = (Button) view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);
    }

    private void populateReportTable() {
        TableRow tr = new TableRow(getContext());
        tr.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT));
        //tr.setPadding(10,10,10,10);

        tr.addView(getTextView("Job Name", true));
        tr.addView(getTextView("Mileage", true));
        tr.addView(getTextView("Description", true));
        tr.addView(getTextView("Cost", true));
        tr.addView(getTextView("Date", true));
        tr.addView(getTextView("Garage Name", true));
        tr.addView(getTextView("Garage Address", true));

        tableLayout.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));

    }

    private void createReportTable(ArrayList<Bill> content) {
        tableLayout.removeAllViews();
        if (!content.isEmpty()) {
            populateReportTable();
            for (Bill bill : content) {
                TableRow tr = getARow(bill);
                tableLayout.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
            }
        } else {
            tableLayout.removeAllViews();
            Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
        }
    }

    private TableRow getARow(Bill bill) {
        TableRow tr = new TableRow(getContext());
        tr.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT));
        //tr.setPadding(10,10,10,10);
        tr.addView(getTextView(bill.jobName, false));
        tr.addView(getTextView(bill.mileage, false));
        tr.addView(getTextView(bill.description, false));
        tr.addView(getTextView(String.valueOf(bill.cost), false));
        tr.addView(getTextView(Utility.getDateAsString(bill.date, myDateFormat.dd_MMM_yyyy), false));
        tr.addView(getTextView(bill.garageName, false));
        tr.addView(getTextView(bill.garageAddress, false));
        return tr;
    }

    private TextView getTextView(String value, boolean head) {
        TextView tvName = new TextView(getContext());
        tvName.setPadding(10, 10, 10, 10);
        tvName.setText(value);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (head) {
                tvName.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.cell_head_shape));
                tvName.setTextColor(Color.WHITE);
            } else {
                tvName.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.cell_shape));
            }
        }
        return tvName;
    }

    @Override
    public void onClick(View v) {
        getBillDate();
    }

    public void getBillDate() {
        ArrayList<Bill> content;
        Date fromDate,toDate;

        if (!isEmpty(froEditText)) {
            if (getDate(getMyText(froEditText), myDateFormat.dd_MMM_yyyy).equals(Utility.ErrorDate)) {
                froEditText.setError("Date Format Error");
                return;
            } else {
                fromDate = getDate(getMyText(froEditText), myDateFormat.dd_MMM_yyyy);
            }
        } else {
            fromDate = Utility.ErrorDate;
        }
        if (!isEmpty(toEditText)) {
            if (getDate(getMyText(toEditText), myDateFormat.dd_MMM_yyyy).equals(Utility.ErrorDate)) {
                toEditText.setError("Date Format Error");
                return;
            } else {
                toDate = getDate(getMyText(toEditText), myDateFormat.dd_MMM_yyyy);
            }
        } else {
            toDate = new Date();
        }
        content = dbHelper.getBill(fromDate, toDate);
        createReportTable(content);
    }
}
