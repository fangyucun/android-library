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
 */package com.hellofyc.applib.helper;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.hellofyc.applib.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yucun Fang
 * @since 2015年5月11日 下午2:49:38
 */
public final class UserAccount {
	static final boolean DEBUG = false;
	
	public static final String ACTION_USER_LOGIN	 = "action.USER_LOGIN";
	public static final String ACTION_USER_LOGOUT	 = "action.USER_LOGOUT";
	public static final String ACTION_USER_CHANGE	 = "action.USER_CHANGE";
	
	public static final String EXTRA_TYPE	 		  = "intent.extra.TYPE";
	public static final String EXTRA_USER			  = "intent.extra.USER";
	
	public static final String TYPE_USER_LOGIN		= "user_login";
	public static final String TYPE_USER_LOGOUT		= "user_logout";
	public static final String TYPE_USER_CHANGE		= "user_change";
	
	private static UserAccount sInstance;
	private Context mContext;
	private User mUser = new User();
	
	private List<OnUserListener> mUserListeners;
    private OnUserListener mUserListener;

	private UserAccount(Context context) {
		mContext = context;
        if (isLogin()) {
        }
	}
	
	public static UserAccount getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new UserAccount(context.getApplicationContext());
		}
		return sInstance;
	}
	
	public void setUser(User user) {
		mUser = user;
	}
	
	public User getUser() {
		return mUser;
	}
	
	public boolean isLogin() {
		return mUser != null && !TextUtils.isEmpty(mUser.qid);
	}
	
	public void addOnUserListener(OnUserListener listener) {
		if (mUserListeners == null) {
			mUserListeners = new ArrayList<>();
		}
		mUserListeners.add(listener);
	}
	
	public void removeOnUserListener(OnUserListener listener) {
		if (mUserListeners != null && mUserListeners.contains(listener)) {
			mUserListeners.remove(listener);
		}
	}

	public void notifyLoginSuccess(User user) {
		if (mUserListeners != null && mUserListeners.size() > 0) {
			for (int i=0; i< mUserListeners.size(); i++) {
				mUserListeners.get(i).onUserLoginSuccess(user);
			}
		}
		notifyToDiffProcesses(TYPE_USER_LOGIN);
	}
	
	public void notifyLogoutSuccess() {
		if (mUserListeners != null && mUserListeners.size() > 0) {
			for (int i=0; i< mUserListeners.size(); i++) {
				mUserListeners.get(i).onUserLogout();
			}
		}
		notifyToDiffProcesses(TYPE_USER_LOGOUT);
	}
	
	public void notifyUserChange(User newUser) {
		mUser = newUser;
		if (mUserListeners != null && mUserListeners.size() > 0) {
			for (int i=0; i< mUserListeners.size(); i++) {
				mUserListeners.get(i).onUserChange(newUser);
			}
		}
		notifyToDiffProcesses(TYPE_USER_CHANGE);
	}
	
	/**
	 * 跨进程通知
	 * 
	 * @param type type
	 */
	void notifyToDiffProcesses(String type) {
		Intent intent = new Intent();
		intent.putExtra(EXTRA_TYPE, type);
		if (TYPE_USER_LOGIN.equals(type)) {
			intent.setAction(UserAccount.ACTION_USER_LOGIN);
			intent.putExtra(EXTRA_USER, mUser);
		} else if (TYPE_USER_LOGOUT.equals(type)) {
			intent.setAction(UserAccount.ACTION_USER_LOGOUT);
		} else if (TYPE_USER_CHANGE.equals(type)) {
			intent.setAction(UserAccount.ACTION_USER_CHANGE);
			intent.putExtra(EXTRA_USER, mUser);
		}
		intent.setPackage(mContext.getPackageName());
		mContext.sendBroadcast(intent, "hellofyc.permission.USER");
	}

	class UserAsyncTask extends AsyncTask<User, Integer, User> {

		@Override
		protected User doInBackground(User... params) {
			return new User();
		}

		@Override
		protected void onPostExecute(User user) {
			super.onPostExecute(user);
			if (user != null) {
				mUser = user;
                notifyLoginSuccess(mUser);
			} else {
                mUser = null;
                if (mUserListener != null) {
                    mUserListener.onUserLoginFailure(-1, "网络错误!");
                }
            }
		}
	}

    public void refreshUserFromUrl(User user) {
        new UserAsyncTask().execute(user);
    }
	

	public void logout() {
		mUser = new User();
		notifyLogoutSuccess();
	}
	
	public static class SimpleOnUserListener implements OnUserListener {

        @Override
		public void onUserLoginSuccess(User user) {
		}
		
		@Override
		public void onUserLoginFailure(int errorCode, String errorMsg) {
		}

		@Override
		public void onUserLogout() {
		}

		@Override
		public void onUserChange(User user) {
		}
		
	}
	
	public interface OnUserListener {
		void onUserLoginSuccess(User user);
		void onUserLoginFailure(int errorCode, String errorMsg);
		void onUserLogout();
		void onUserChange(User user);
	}
}
