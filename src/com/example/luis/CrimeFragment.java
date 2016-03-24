package com.example.luis;

import java.util.Date;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class CrimeFragment extends Fragment {

	private Crime mCrime;
	private EditText mTitleText;
	private Button mButton;
	private CheckBox mCheckBox;
	private ImageButton mImageButton;
	
	private static final String ALERT_DATE = "date";
	
	private static final String ALERT_TIME = "time";
	
	private static final int REQUEST_CODE = 9;
	
	private static final int REQUEST_TIME_CODE = 19;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		//mCrime = (Crime)getActivity().getIntent().getSerializableExtra("crime");
		mCrime = CrimeLab.get(getActivity()).get((UUID)getArguments().getSerializable("crime"));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB){
			if(NavUtils.getParentActivityName(getActivity()) != null){
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		View v = inflater.inflate(R.layout.fragment_crime, container, false);
		mTitleText = (EditText)v.findViewById(R.id.crime_title);
		mTitleText.setText(mCrime.getmTitle());
		mTitleText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mCrime.setmTitle(s.toString());
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		this.mButton = (Button)v.findViewById(R.id.crime_date);
		setText();
		//mButton.setEnabled(false);
		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog ad = new AlertDialog.Builder(getActivity())
					.setTitle("请选择")
					.setPositiveButton("选择日期", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							FragmentManager fm = getActivity().getSupportFragmentManager();
							DatePickerFragment dpf = DatePickerFragment.getInstance(mCrime.getmDate()); 
							dpf.setTargetFragment(CrimeFragment.this, REQUEST_CODE);
							dpf.show(fm, ALERT_DATE);
						}
					}) 
					.setNegativeButton("选择时间", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							FragmentManager fm = getActivity().getSupportFragmentManager();
							TimePickerFragment tpf = TimePickerFragment.getInstance(mCrime.getmDate()); 
							tpf.setTargetFragment(CrimeFragment.this, REQUEST_TIME_CODE);
							tpf.show(fm, ALERT_TIME);
						}
					})
					.create();
				ad.show();
				/*FragmentManager fm = getActivity().getSupportFragmentManager();
				DatePickerFragment dpf = DatePickerFragment.getInstance(mCrime.getmDate()); 
				dpf.setTargetFragment(CrimeFragment.this, REQUEST_CODE);
				dpf.show(fm, ALERT_DATE);*/
				/*TimePickerFragment tpf = TimePickerFragment.getInstance(mCrime.getmDate()); 
				tpf.setTargetFragment(CrimeFragment.this, REQUEST_TIME_CODE);
				tpf.show(fm, ALERT_TIME);*/
			}
		});
		this.mCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
		mCheckBox.setChecked(mCrime.ismSolved());
		mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mCrime.setmSolved(isChecked);
				
			}
		});
		PackageManager pm = getActivity().getPackageManager();
		boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
				pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
				Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD ||
				Camera.getNumberOfCameras() > 0;
		mImageButton = (ImageButton)v.findViewById(R.id.imageButton1);
		if(!hasACamera){
			mImageButton.setEnabled(false);
		}
		mImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
				startActivity(i);
			}
		});
		return v;
	}
	public static CrimeFragment newInstance(UUID c){
		Bundle args = new Bundle();
		args.putSerializable("crime", c);
		CrimeFragment cf = new CrimeFragment();
		cf.setArguments(args);
		return cf;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != Activity.RESULT_OK){
			return;
		}
		if(requestCode == REQUEST_CODE){
			Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mCrime.setmDate(date);
			setText();
		}
		if(requestCode == REQUEST_TIME_CODE){
			Date date = (Date)data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
			mCrime.setmDate(date);
			setText();
		}
	}
	private void setText() {
		mButton.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss", mCrime.getmDate()));
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) { 
		switch (item.getItemId()) {
		case android.R.id.home:
			if(NavUtils.getParentActivityName(getActivity()) != null){
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			return true;
		case R.id.menu_item_del_crime:
			CrimeLab.get(getActivity()).delCrime(mCrime);
			if(NavUtils.getParentActivityName(getActivity()) != null){
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			return true;	
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		CrimeLab.get(getActivity()).saveCrimes();
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime, menu);
	}
}
