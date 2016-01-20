/*
 *  Copyright (C) 2012-2015 Jason Fang ( ifangyucun@gmail.com )
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */package com.hellofyc.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Create on 2014年12月24日 上午11:58:34
 *
 * @author Jason Fang
 */
public class BaseFragmentPagerAdapter extends FragmentPagerAdapter {

	private String[] mTitles;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
	
	public BaseFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
		this(fm, null, fragments);
	}
	
	public BaseFragmentPagerAdapter(FragmentManager fm, String[] titles, ArrayList<Fragment> fragments) {
		super(fm);
		mTitles = titles;
		mFragments = fragments;
	}

    @Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}

	@Override
	public int getCount() {
		return mFragments == null ? 0 : mFragments.size();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		if (mTitles == null || mTitles.length == 0) {
			return super.getPageTitle(position);
		} else {
			return mTitles[position];
		}
	}

}
