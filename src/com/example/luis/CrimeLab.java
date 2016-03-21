package com.example.luis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.json.JSONException;

import android.content.Context;
import android.util.Log;

public class CrimeLab {
	private static final String TAG = "CrimeLab";
    private static final String FILENAME = "crimes.json";
	private static CrimeLab sCrimeLab;
	@SuppressWarnings("unused")
	private Context mAppContext;
	private ArrayList<Crime> mCrimes;
	private CriminalIntentJSONSerializer mSerializer;
	
	private CrimeLab(Context context) {
		mAppContext = context;
		//mCrimes = new ArrayList<Crime>();
		mSerializer = new CriminalIntentJSONSerializer(context, FILENAME);
		try {
			mCrimes = mSerializer.loadCrimes();
		} catch (Exception e) {
			Log.d(TAG, "load error");
		} 
		/*for(int i=0;i<5;i++){
			Crime c = new Crime();
			c.setmTitle("llmke #"+i);
			c.setmSolved(i%2 == 0);
			mCrimes.add(c);
		}*/
	}
	
	public void addCrime(Crime c){
		mCrimes.add(c);
	}
	
	public void delCrime(Crime c){
		mCrimes.remove(c); 
		saveCrimes();
	}
	
	public static CrimeLab get(Context c){
		if(sCrimeLab == null){
			sCrimeLab = new CrimeLab(c.getApplicationContext());
		}
		return sCrimeLab;
	}

	public ArrayList<Crime> getmCrimes() {
		return mCrimes;
	}
	
	public Crime get(UUID id){
		for(Crime c:mCrimes){
			if(c.getmId().equals(id)){
				return c;
			}
		}
		return null;
	};
	public boolean saveCrimes(){
		try {
			mSerializer.saveCrimes(mCrimes);
			Log.d(TAG, "save succ");
			return true;
		} catch (Exception e) {
			Log.d(TAG, "save fail");
			return false;
		}
	}
}
