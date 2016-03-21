package com.example.luis;

import java.util.UUID;

import android.support.v4.app.Fragment;

public class MainActivity extends SingleFragmentActivity {

	
	@Override
	protected Fragment createFragment() {
		UUID mCrime = (UUID)getIntent().getSerializableExtra("crime");
		return CrimeFragment.newInstance(mCrime); 
	}
}
