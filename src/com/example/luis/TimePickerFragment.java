package com.example.luis;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class TimePickerFragment extends DialogFragment{
	protected static final String EXTRA_TIME = "TimePickerFragment.time";
	
	private Date date;
	private Calendar cal;
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		date = (Date)getArguments().getSerializable(EXTRA_TIME);
		cal = Calendar.getInstance();
		cal.setTime(date);
		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_time,null);
		TimePicker tp = (TimePicker)v.findViewById(R.id.dialog_time);
		tp.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
		tp.setCurrentMinute(cal.get(Calendar.MINUTE));
		tp.setOnTimeChangedListener(new OnTimeChangedListener() {
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
				cal.set(Calendar.MINUTE, minute);
				date = cal.getTime();
				getArguments().putSerializable(EXTRA_TIME, date);
			}
		});
		return new AlertDialog.Builder(getActivity()).setTitle(R.string.time_title)
				.setPositiveButton(android.R.string.ok, new OnClickListener() {
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
		i.putExtra(EXTRA_TIME, date);
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultOk, i);
		
	}
	public static TimePickerFragment getInstance(Date d){
		Bundle b = new Bundle();
		b.putSerializable(EXTRA_TIME, d);
		TimePickerFragment tpf = new TimePickerFragment();
		tpf.setArguments(b);
		return tpf;
	}
}
