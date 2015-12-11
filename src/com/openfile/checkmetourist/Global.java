package com.openfile.checkmetourist;

import android.graphics.Bitmap;

public abstract class Global
{
	public final static String		Version					= "2.3.5";
	public final static String		TENCENT_APP_ID			= "1104865422"; // test is: 222222
	public final static int			SCANNER_REQUEST_CODE	= 1025;
	public static MainApplication	theApplication			= null;

	public static String			qq_openid				= null;
	public static String			qq_nickname				= null;
	public static String			qq_token				= null;
	public static Bitmap			qq_picture				= null;

	// Checkme API Parameters
	public static String			mMobileBonus			= null;
	public static String			mNeedUpdate				= null;
	public static String			mShowMsg				= null;
	public static String			mUpdateURL				= null;
	public static String			mInviteCode				= null;
	public static String			mAccountId				= null;
	public static String			mAndroidSerial			= null;
	public static String			mGiftDataVer			= null;
	public static String			mSensorDataVer			= null;
	public static int				mPoint					= 0;
	
}
