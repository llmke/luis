package com.example.luis;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

public class DatePickerFragment extends DialogFragment {

	protected static final String EXTRA_DATE = "DatePickerFragment.date";
	
	private Date date;
	Calendar cal;
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		date = (Date)getArguments().getSerializable(EXTRA_DATE);
		cal = Calendar.getInstance();
		cal.setTime(date);
		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_date,null);
		DatePicker dp = (DatePicker)v.findViewById(R.id.date_picker);
		dp.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 
				new OnDateChangedListener() {
					@Override
					public void onDateChanged(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						cal.set(Calendar.YEAR, year);
						cal.set(Calendar.MONTH, monthOfYear);
						cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						date = cal.getTime(); 
						getArguments().putSerializable(EXTRA_DATE, date);
					}
				});
		return new AlertDialog.Builder(getActivity()).setTitle(R.string.date_title)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						sendResult(Activity.RESULT_OK);
					}
				}).setView(v).create(); 
	}
	
	protected void sendResult(int resultOk) {
		if(getTargetFragment() == null){
			return;
		}
		Intent i = new Intent();
		i.putExtra(EXTRA_DATE, date);
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultOk, i);
	}

	public static DatePickerFragment getInstance(Date d){
		Bundle b = new Bundle();
		b.putSerializable(EXTRA_DATE, d);
		DatePickerFragment dpf = new DatePickerFragment();
		dpf.setArguments(b);
		return dpf;
	}
}
