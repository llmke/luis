package com.example.luis;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class CrimePagerActivity extends FragmentActivity {

	 ViewPager mViewPager;
	private ArrayList<Crime> mCrimes;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate( Bundle arg0) {
		super.onCreate(arg0);
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		mCrimes = CrimeLab.get(this).getmCrimes();
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			
			@Override
			public int getCount() {
				return mCrimes.size();
			}
			
			@Override
			public Fragment getItem(int arg0) {
				Crime c = mCrimes.get(arg0);
				return CrimeFragment.newInstance(c.getmId()); 
			}
		});
		UUID mId = (UUID)getIntent().getSerializableExtra("crime");
		for(int i=0;i<mCrimes.size();i++){
			Crime c = mCrimes.get(i);
			if(mId.equals(c.getmId())){
				mViewPager.setCurrentItem(i);
				setTitle(c.getmTitle());
				break;
			}
		}
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				Crime c = mCrimes.get(arg0);
				if(c.getmTitle() != null){
					setTitle(c.getmTitle());
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}
}
