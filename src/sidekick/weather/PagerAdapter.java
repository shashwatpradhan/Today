package sidekick.weather;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter{

	public PagerAdapter(FragmentManager fm) {
		super(fm);
		
	}

	@Override
	public Fragment getItem(int index) {
		
		switch (index) {
		
		case 0: return new Home();
		
		case 1: return new List();
		
//		case 2: return new Map();
			
		}
		return null;
		
	}

	@Override
	public int getCount() {
		return 2;
	}

}
