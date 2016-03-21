package com.example.luis;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class CrimeListFragment extends ListFragment {
	private ArrayList<Crime> mCrimes;
	private boolean isShow;
	private static final int RETURN_CODE = 1;
	@Override
	public void onCreate( Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		setRetainInstance(true);
		isShow = false;
		getActivity().setTitle(R.string.crimes);
		mCrimes = CrimeLab.get(getActivity()).getmCrimes();
		/*ArrayAdapter<Crime> adapter = new ArrayAdapter<Crime>(getActivity(), 
				android.R.layout.simple_list_item_1, mCrimes);*/
		CrimeAdapter adapter = new CrimeAdapter(mCrimes);
		setListAdapter(adapter);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v =  super.onCreateView(inflater, container, savedInstanceState);
		//View v = inflater.inflate(R.layout.list_empty, container);
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB && isShow){
			getActivity().getActionBar().setSubtitle(R.string.subtitle);
		}
		ListView listView = (ListView)v.findViewById(android.R.id.list);
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
			registerForContextMenu(listView);
		}else{
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
				@Override
				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
					return false;
				}
				@Override
				public void onDestroyActionMode(ActionMode mode) {
					
				}
				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					MenuInflater mi = mode.getMenuInflater();
					mi.inflate(R.menu.crime_list_item_context, menu);
					return true;
				}
				@Override
				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
					switch (item.getItemId()) {
					case R.id.menu_item_delete_crime:
						CrimeAdapter ca = (CrimeAdapter)getListAdapter();
						CrimeLab cl = CrimeLab.get(getActivity());
						for(int i=ca.getCount()-1;i>=0;i--){
							if(getListView().isItemChecked(i)){
								cl.delCrime(ca.getItem(i));
							}
						}
						mode.finish();
						ca.notifyDataSetChanged();
						return true;
					}
					return false;
				}
				@Override
				public void onItemCheckedStateChanged(ActionMode mode, int position,
						long id, boolean checked) {
					
				}
			});
		}
		return v;
	}
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Crime c = ((CrimeAdapter)getListAdapter()).getItem(position);
		//Log.d("TAP", c.getmTitle()+" was taped!");
		Intent i = new Intent(getActivity(), CrimePagerActivity.class);
		i.putExtra("crime", c.getmId());
		startActivityForResult(i,RETURN_CODE);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	private class CrimeAdapter extends ArrayAdapter<Crime>{

		public CrimeAdapter(ArrayList<Crime> crimes) {
			super(getActivity(), 0,crimes);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_crime, null);
			}
			Crime c = getItem(position);
			TextView tv1 = (TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
			tv1.setText(c.getmTitle());
			TextView tv2 = (TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
			tv2.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss", c.getmDate()));
			CheckBox cb = (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
			cb.setChecked(c.ismSolved());
			return convertView;
		}
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime_list, menu);
		MenuItem m = menu.findItem(R.id.menu_item_show_subtitle);
		if(isShow && m!=null){
			m.setTitle(R.string.hide_subtitle);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) { 
		switch (item.getItemId()) {
		case R.id.menu_item_new_crime:
			Crime c = new Crime();
			CrimeLab.get(getActivity()).addCrime(c);
			Intent i = new Intent(getActivity(), CrimePagerActivity.class);
			i.putExtra("crime", c.getmId());
			startActivityForResult(i,0);
			return true;
		case R.id.menu_item_show_subtitle:
			if(getActivity().getActionBar().getSubtitle() == null){
				getActivity().getActionBar().setSubtitle(R.string.subtitle);
				item.setTitle(R.string.hide_subtitle);
				isShow = true;
			}else{
				getActivity().getActionBar().setSubtitle(null);
				item.setTitle(R.string.show_subtitle);
				isShow = false;
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		int pos = info.position;
		CrimeAdapter ca = (CrimeAdapter)getListAdapter();
		Crime c = ca.getItem(pos);
		switch (item.getItemId()) {
		case R.id.menu_item_delete_crime:
			CrimeLab.get(getActivity()).delCrime(c);
			ca.notifyDataSetChanged();
			return true;
		}
		return super.onContextItemSelected(item);
	}
}
