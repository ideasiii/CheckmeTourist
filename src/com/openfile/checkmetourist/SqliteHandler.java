package com.openfile.checkmetourist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.SparseArray;

public class SqliteHandler extends SQLiteOpenHelper
{
	private final static String	DATABASE_NAME		= "checkme.db";
	private final static int	DATABASE_VERSION	= 2;

	public static class GiftData
	{
		public String	strId;
		public String	strName;
		public String	strPoint;
		public String	strInfo;
		public String	strImage_URL;
		public String	strFavourite;
		public String	strStart_Date;
		public String	strEnd_Date;
		public String	strLink;
		public String	strVer;
	}

	public static class BannerData
	{
		public String	strId;
		public String	strImageUrl;
		public String	strStartDate;
		public String	strEndDate;
		public String	strContentUrl;
	}

	public static class FieldData
	{
		public String	strId;
		public String	strName;
		public String	strImage_URL;
		public String	strInfo;
		public String	strPoint;
		public String	strSubImage_URL;
		public String	strUpdate_Date;
	}

	public static class TaskData
	{
		public String	strId;
		public String	strName;
		public String	strType;
		public String	strImage_URL;
		public String	strPoint;
		public String	strInfo;
		public String	strStart_Date;
		public String	strEnd_Date;
		public String	strBeacon_Key;
		public String	strBarcode;
		public String	strComplete_Time;
		public String	strField_ID;
		public String	strBrand_ID;
		public String	strUpdate_Date;
		public String	strFreq;
		public String	strMaxTime;
		public String	strLast_Time;
		public String	strPlayed_Count;
		public String	strFixed;
		public String	strAmount;
		public String	strDaily_Amount;
	}

	public static class ActionData
	{
		public String	strTime;
		public String	strPoint;
		public String	strID;
		public String	strType;
		public String	strComplete;
		public String	strUpload;
		public String	strBarcode;
		public String	strBeaconID;
		public String	strBeacon;
		public String	strError_Message;
	}

	public static class CouponData
	{
		public String	strID;
		public String	strCode;
		public String	strTime;
	}

	public SqliteHandler(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public SqliteHandler(Context context, String name, CursorFactory factory, int version)
	{
		super(context, name, factory, version);
	}

	public SqliteHandler(Context context, String name, CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler)
	{
		super(context, name, factory, version, errorHandler);
	}

	/**
	 * 如果資料庫不存在 則呼叫onCreate
	 */
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		try
		{
			db.execSQL("CREATE TABLE Banner (ID TEXT, Image_URL TEXT, Start_Date TEXT, End_Date TEXT, URL TEXT, Update_Time TEXT)");
			db.execSQL("CREATE TABLE Brand (ID TEXT, Name TEXT, Image_URL TEXT, Info TEXT, Point TEXT, UpDate_Date TEXT)");
			db.execSQL("CREATE TABLE Field (ID TEXT, Name TEXT, Image_URL TEXT, Info TEXT, Point TEXT, SubImage_URL TEXT, Update_Date TEXT)");
			db.execSQL("CREATE TABLE Gift (ID TEXT, Name TEXT, Point TEXT, Info TEXT, Image_URL TEXT, Favourite TEXT, Start_Date TEXT, End_Date TEXT, Link TEXT, Ver TEXT)");
			db.execSQL("CREATE TABLE SubField (ID TEXT, Name TEXT, Address TEXT, GPS TEXT, Tel TEXT, FieldID TEXT)");
			db.execSQL("CREATE TABLE Task (ID TEXT, Name TEXT, Type TEXT, Image_URL TEXT, Point TEXT, Info TEXT, Start_Date TEXT, End_Date TEXT, Beacon_Key TEXT, Barcode TEXT, Complete_Time TEXT, Field_ID TEXT, Brand_ID TEXT, Update_Date TEXT, Freq TEXT, MaxTime TEXT, Last_Time TEXT, Played_Count TEXT, Fixed TEXT, Amount TEXT, Daily_Amount TEXT)");
			db.execSQL("CREATE TABLE Version (Name TEXT, Code TEXT)");
			db.execSQL("CREATE TABLE Action (Time TEXT, Point TEXT, ID TEXT, Type TEXT, Complete TEXT, Upload TEXT, Barcode TEXT, BeaconID TEXT, Beacon TEXT, Error_Message TEXT)");
			db.execSQL("CREATE TABLE Coupon (ID TEXT, Code TEXT, Time TEXT)");
			Logs.showTrace("Create Database Success!!");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * 版本更新時被呼叫 oldVersion=舊的資料庫版本；newVersion=新的資料庫版本
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS Banner");
		db.execSQL("DROP TABLE IF EXISTS Brand");
		db.execSQL("DROP TABLE IF EXISTS Field");
		db.execSQL("DROP TABLE IF EXISTS Gift");
		db.execSQL("DROP TABLE IF EXISTS SubField");
		db.execSQL("DROP TABLE IF EXISTS Task");
		db.execSQL("DROP TABLE IF EXISTS Version");
		onCreate(db);
	}

	/**
	 * 每次成功打開數據庫後首先被執行
	 */
	@Override
	public void onOpen(SQLiteDatabase db)
	{
		super.onOpen(db);
	}

	@Override
	public synchronized void close()
	{
		super.close();
	}

	public void clearBannerData()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		int nRow = db.delete("Banner", "1", null);
		db.close();
		Logs.showTrace("Delete Banner Record:" + String.valueOf(nRow));
	}

	public void addBannerData(final String strId, final String strImageUrl, final String strStartDate,
			final String strEndDate, final String strContentUrl)
	{
		long nRet = -1;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("ID", strId);
		values.put("Image_URL", strImageUrl);
		values.put("Start_Date", strStartDate);
		values.put("End_Date", strEndDate);
		values.put("URL", strContentUrl);
		nRet = db.insert("Banner", null, values);
		db.close();
		if (-1 == nRet)
		{
			Logs.showTrace("Add Banner Data Fail");
		}
		else
		{
			Logs.showTrace("Add Banner Data: " + strId + "," + strImageUrl + "," + strStartDate + "," + strEndDate
					+ "," + strContentUrl);
		}
	}

	public void clearGiftData()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		int nRow = db.delete("Gift", "1", null);
		db.close();
		Logs.showTrace("Delete Gift Record:" + String.valueOf(nRow));
	}

	public void addGiftData(final String strId, final String strName, final String strPoint, final String strInfo,
			final String strImage_URL, final String strFavourite, final String strStart_Date, final String strEnd_Date,
			final String strLink, final String strVer)
	{
		long nRet = -1;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("ID", strId);
		values.put("Name", strName);
		values.put("Image_URL", strImage_URL);
		values.put("End_Date", strEnd_Date);
		values.put("Point", strPoint);
		values.put("Favourite", strFavourite);
		values.put("Start_Date", strStart_Date);
		values.put("Info", strInfo);
		values.put("Ver", strVer);
		values.put("Link", strLink);
		nRet = db.insert("Gift", null, values);
		db.close();
		if (-1 == nRet)
		{
			Logs.showTrace("Add Gift Data Fail");
		}
		else
		{
			Logs.showTrace("Add Gift Data: " + strId + "," + strName + "," + strImage_URL + "," + strEnd_Date + ","
					+ strPoint + "," + strFavourite + "," + strStart_Date + "," + strInfo + "," + strVer + ","
					+ strLink);
		}
	}

	public void clearFieldData()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		int nRow = db.delete("Field", "1", null);
		db.close();
		Logs.showTrace("Delete Field Record:" + String.valueOf(nRow));
	}

	public void addFieldData(final String strId, final String strName, final String strImage_URL, final String strInfo,
			final String strPoint, final String strSubImage_URL, final String strUpdate_Date)
	{
		long nRet = -1;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("ID", strId);
		values.put("Name", strName);
		values.put("Image_URL", strImage_URL);
		values.put("Info", strInfo);
		values.put("Point", strPoint);
		values.put("SubImage_URL", strSubImage_URL);
		values.put("Update_Date", strUpdate_Date);
		nRet = db.insert("Field", null, values);
		db.close();
		if (-1 == nRet)
		{
			Logs.showTrace("Add Field Data Fail");
		}
		else
		{
			Logs.showTrace("Add Field Data: " + strId + "," + strName + "," + strImage_URL + "," + strPoint + strInfo
					+ "," + strSubImage_URL + "," + strUpdate_Date);
		}
	}

	public int getGiftData(SparseArray<GiftData> listData)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT ID , Name , Point , Info , Image_URL , Favourite , Start_Date , End_Date , Link , Ver FROM Gift",
						null);
		int rows_num = cursor.getCount();
		if (0 != rows_num)
		{
			GiftData giftData = null;
			cursor.moveToFirst();
			int nIndex = 0;
			for (int i = 0; i < rows_num; ++i)
			{
				nIndex = 0;
				giftData = new GiftData();
				giftData.strId = cursor.getString(nIndex);
				giftData.strName = cursor.getString(++nIndex);
				giftData.strPoint = cursor.getString(++nIndex);
				giftData.strInfo = cursor.getString(++nIndex);
				giftData.strImage_URL = cursor.getString(++nIndex);
				giftData.strFavourite = cursor.getString(++nIndex);
				giftData.strStart_Date = cursor.getString(++nIndex);
				giftData.strEnd_Date = cursor.getString(++nIndex);
				giftData.strLink = cursor.getString(++nIndex);
				giftData.strVer = cursor.getString(++nIndex);
				listData.put(listData.size(), giftData);
				giftData = null;
				cursor.moveToNext();
			}
		}
		cursor.close();
		return rows_num;
	}

	public int getBannerData(SparseArray<BannerData> listData)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT ID , Image_URL , Start_Date , End_Date , URL , Update_Time FROM Banner",
				null);
		int rows_num = cursor.getCount();
		if (0 != rows_num)
		{
			BannerData bannerData = null;
			cursor.moveToFirst();
			int nIndex = 0;
			for (int i = 0; i < rows_num; ++i)
			{
				nIndex = 0;
				bannerData = new BannerData();
				bannerData.strId = cursor.getString(nIndex);
				bannerData.strImageUrl = cursor.getString(++nIndex);
				bannerData.strStartDate = cursor.getString(++nIndex);
				bannerData.strEndDate = cursor.getString(++nIndex);
				bannerData.strContentUrl = cursor.getString(++nIndex);
				listData.put(listData.size(), bannerData);
				bannerData = null;
				cursor.moveToNext();
			}
		}
		cursor.close();
		return rows_num;
	}

	public int getFieldData(SparseArray<FieldData> listData)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT ID , Name , Image_URL , Info , Point , SubImage_URL , Update_Date FROM Field", null);
		int rows_num = cursor.getCount();
		if (0 != rows_num)
		{
			FieldData fieldData = null;
			cursor.moveToFirst();
			int nIndex = 0;
			for (int i = 0; i < rows_num; ++i)
			{
				nIndex = 0;
				fieldData = new FieldData();
				fieldData.strId = cursor.getString(nIndex);
				fieldData.strName = cursor.getString(++nIndex);
				fieldData.strImage_URL = cursor.getString(++nIndex);
				fieldData.strInfo = cursor.getString(++nIndex);
				fieldData.strPoint = cursor.getString(++nIndex);
				fieldData.strSubImage_URL = cursor.getString(++nIndex);
				fieldData.strUpdate_Date = cursor.getString(++nIndex);
				listData.put(listData.size(), fieldData);
				fieldData = null;
				cursor.moveToNext();
			}
		}
		cursor.close();
		return rows_num;
	}

	public int getFieldDataByChannelId(SparseArray<FieldData> listData, final String strChannelId)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT ID , Name , Image_URL , Info , Point , SubImage_URL , Update_Date FROM Field WHERE ID = '"
						+ strChannelId + "'", null);
		int rows_num = cursor.getCount();
		if (0 != rows_num)
		{
			FieldData fieldData = null;
			cursor.moveToFirst();
			int nIndex = 0;
			for (int i = 0; i < rows_num; ++i)
			{
				nIndex = 0;
				fieldData = new FieldData();
				fieldData.strId = cursor.getString(nIndex);
				fieldData.strName = cursor.getString(++nIndex);
				fieldData.strImage_URL = cursor.getString(++nIndex);
				fieldData.strInfo = cursor.getString(++nIndex);
				fieldData.strPoint = cursor.getString(++nIndex);
				fieldData.strSubImage_URL = cursor.getString(++nIndex);
				fieldData.strUpdate_Date = cursor.getString(++nIndex);
				listData.put(listData.size(), fieldData);
				fieldData = null;
				cursor.moveToNext();
			}
		}
		cursor.close();
		return rows_num;
	}

	public void clearTaskData()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		int nRow = db.delete("Task", "1", null);
		db.close();
		Logs.showTrace("Delete Task Record:" + String.valueOf(nRow));
	}

	public void addTaskData(final String strId, final String strName, final String strType, final String strImage_URL,
			final String strPoint, final String strInfo, final String strStart_Date, final String strEnd_Date,
			final String strBeacon_Key, final String strBarcode, final String strComplete_Time,
			final String strField_ID, final String strBrand_ID, final String strUpdate_Date, final String strFreq,
			final String strMaxTime, final String strLast_Time, final String strPlayed_Count, final String strFixed,
			final String strAmount, final String strDaily_Amount)
	{
		long nRet = -1;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put("ID", strId);
		values.put("Name", strName);
		values.put("Type", strType);
		values.put("Image_URL", strImage_URL);
		values.put("Point", strPoint);
		values.put("Info", strInfo);
		values.put("Start_Date", strStart_Date);
		values.put("End_Date", strEnd_Date);
		values.put("Beacon_Key", strBeacon_Key);
		values.put("Barcode", strBarcode);
		values.put("Complete_Time", strComplete_Time);
		values.put("Field_ID", strField_ID);
		values.put("Brand_ID", strBrand_ID);
		values.put("Update_Date", strUpdate_Date);
		values.put("Freq", strFreq);
		values.put("MaxTime", strMaxTime);
		values.put("Last_Time", strLast_Time);
		values.put("Played_Count", strPlayed_Count);
		values.put("Fixed", strFixed);
		values.put("Amount", strAmount);
		values.put("Daily_Amount", strDaily_Amount);

		nRet = db.insert("Task", null, values);
		db.close();
		if (-1 == nRet)
		{
			Logs.showTrace("Add Task Data Fail");
		}
		else
		{
			Logs.showTrace("Add Task Data: " + strId + "," + strName + "," + strImage_URL + "," + strPoint + ","
					+ strInfo);
		}
	}

	public int getTaskDataByChannelId(SparseArray<TaskData> listData, final String strChannelId)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT ID , Name, Type, Image_URL, Point, Info, Start_Date, End_Date, Beacon_Key, Barcode, Complete_Time, Field_ID, Brand_ID, Update_Date , Freq, MaxTime, Last_Time, Played_Count, Fixed, Amount, Daily_Amount FROM Task WHERE Field_ID = '"
								+ strChannelId + "'", null);
		int rows_num = cursor.getCount();
		if (0 != rows_num)
		{
			TaskData taskData = null;
			cursor.moveToFirst();
			int nIndex = 0;
			for (int i = 0; i < rows_num; ++i)
			{
				nIndex = 0;
				taskData = new TaskData();
				taskData.strId = cursor.getString(nIndex);
				taskData.strName = cursor.getString(++nIndex);
				taskData.strType = cursor.getString(++nIndex);
				taskData.strImage_URL = cursor.getString(++nIndex);
				taskData.strPoint = cursor.getString(++nIndex);
				taskData.strInfo = cursor.getString(++nIndex);
				taskData.strStart_Date = cursor.getString(++nIndex);
				taskData.strEnd_Date = cursor.getString(++nIndex);
				taskData.strBeacon_Key = cursor.getString(++nIndex);
				taskData.strBarcode = cursor.getString(++nIndex);
				taskData.strComplete_Time = cursor.getString(++nIndex);
				taskData.strField_ID = cursor.getString(++nIndex);
				taskData.strBrand_ID = cursor.getString(++nIndex);
				taskData.strUpdate_Date = cursor.getString(++nIndex);
				taskData.strFreq = cursor.getString(++nIndex);
				taskData.strMaxTime = cursor.getString(++nIndex);
				taskData.strLast_Time = cursor.getString(++nIndex);
				taskData.strPlayed_Count = cursor.getString(++nIndex);
				taskData.strFixed = cursor.getString(++nIndex);
				taskData.strAmount = cursor.getString(++nIndex);
				taskData.strDaily_Amount = cursor.getString(++nIndex);
				listData.put(listData.size(), taskData);
				taskData = null;
				cursor.moveToNext();
			}
		}
		cursor.close();
		return rows_num;
	}

	public int getTaskDataByTaskId(SparseArray<TaskData> listData, final String strTaskId)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT ID , Name, Type, Image_URL, Point, Info, Start_Date, End_Date, Beacon_Key, Barcode, Complete_Time, Field_ID, Brand_ID, Update_Date , Freq, MaxTime, Last_Time, Played_Count, Fixed, Amount, Daily_Amount FROM Task WHERE ID = '"
								+ strTaskId + "'", null);
		int rows_num = cursor.getCount();
		if (0 != rows_num)
		{
			TaskData taskData = null;
			cursor.moveToFirst();
			int nIndex = 0;
			for (int i = 0; i < rows_num; ++i)
			{
				nIndex = 0;
				taskData = new TaskData();
				taskData.strId = cursor.getString(nIndex);
				taskData.strName = cursor.getString(++nIndex);
				taskData.strType = cursor.getString(++nIndex);
				taskData.strImage_URL = cursor.getString(++nIndex);
				taskData.strPoint = cursor.getString(++nIndex);
				taskData.strInfo = cursor.getString(++nIndex);
				taskData.strStart_Date = cursor.getString(++nIndex);
				taskData.strEnd_Date = cursor.getString(++nIndex);
				taskData.strBeacon_Key = cursor.getString(++nIndex);
				taskData.strBarcode = cursor.getString(++nIndex);
				taskData.strComplete_Time = cursor.getString(++nIndex);
				taskData.strField_ID = cursor.getString(++nIndex);
				taskData.strBrand_ID = cursor.getString(++nIndex);
				taskData.strUpdate_Date = cursor.getString(++nIndex);
				taskData.strFreq = cursor.getString(++nIndex);
				taskData.strMaxTime = cursor.getString(++nIndex);
				taskData.strLast_Time = cursor.getString(++nIndex);
				taskData.strPlayed_Count = cursor.getString(++nIndex);
				taskData.strFixed = cursor.getString(++nIndex);
				taskData.strAmount = cursor.getString(++nIndex);
				taskData.strDaily_Amount = cursor.getString(++nIndex);
				listData.put(listData.size(), taskData);
				taskData = null;
				cursor.moveToNext();
			}
		}
		cursor.close();
		return rows_num;
	}

	public void clearActionData()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		int nRow = db.delete("Action", "1", null);
		db.close();
		Logs.showTrace("Delete Action Record:" + String.valueOf(nRow));
	}

	public void addActionData(final String strTime, final String strPoint, final String strID, final String strType,
			final String strComplete, final String strUpload, final String strBarcode, final String strBeaconID,
			final String strBeacon, final String strError_Message)
	{
		long nRet = -1;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put("Time", strTime);
		values.put("Point", strPoint);
		values.put("ID", strID);
		values.put("Type", strType);
		values.put("Complete", strComplete);
		values.put("Upload", strUpload);
		values.put("Barcode", strBarcode);
		values.put("BeaconID", strBeaconID);
		values.put("Beacon", strBeacon);
		values.put("Error_Message", strError_Message);

		nRet = db.insert("Action", null, values);
		db.close();
		if (-1 == nRet)
		{
			Logs.showTrace("Add Action Data Fail");
		}
		else
		{
			Logs.showTrace("Add Action Data: " + strTime + "," + strPoint + "," + strID + "," + strPoint + ","
					+ strType);
		}
	}

	public int getActionData(SparseArray<ActionData> listData)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT Time, Point, ID, Type, Complete, Upload, Barcode, BeaconID, Beacon, Error_Message FROM Action",
				null);
		int rows_num = cursor.getCount();
		if (0 != rows_num)
		{
			ActionData actionData = null;
			cursor.moveToFirst();
			int nIndex = 0;
			for (int i = 0; i < rows_num; ++i)
			{
				nIndex = 0;
				actionData = new ActionData();
				actionData.strTime = cursor.getString(nIndex);
				actionData.strPoint = cursor.getString(++nIndex);
				actionData.strID = cursor.getString(++nIndex);
				actionData.strType = cursor.getString(++nIndex);
				actionData.strComplete = cursor.getString(++nIndex);
				actionData.strUpload = cursor.getString(++nIndex);
				actionData.strBarcode = cursor.getString(++nIndex);
				actionData.strBeaconID = cursor.getString(++nIndex);
				actionData.strBeacon = cursor.getString(++nIndex);
				actionData.strError_Message = cursor.getString(++nIndex);
				listData.put(listData.size(), actionData);
				actionData = null;
				cursor.moveToNext();
			}
		}
		cursor.close();
		return rows_num;
	}

	public int getActionDataByTaskId(SparseArray<ActionData> listData, final String strTaskId)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT Time, Point, ID, Type, Complete, Upload, Barcode, BeaconID, Beacon, Error_Message FROM Action WHERE ID = '"
						+ strTaskId + "'", null);
		int rows_num = cursor.getCount();
		if (0 != rows_num)
		{
			ActionData actionData = null;
			cursor.moveToFirst();
			int nIndex = 0;
			for (int i = 0; i < rows_num; ++i)
			{
				nIndex = 0;
				actionData = new ActionData();
				actionData.strTime = cursor.getString(nIndex);
				actionData.strPoint = cursor.getString(++nIndex);
				actionData.strID = cursor.getString(++nIndex);
				actionData.strType = cursor.getString(++nIndex);
				actionData.strComplete = cursor.getString(++nIndex);
				actionData.strUpload = cursor.getString(++nIndex);
				actionData.strBarcode = cursor.getString(++nIndex);
				actionData.strBeaconID = cursor.getString(++nIndex);
				actionData.strBeacon = cursor.getString(++nIndex);
				actionData.strError_Message = cursor.getString(++nIndex);
				listData.put(listData.size(), actionData);
				actionData = null;
				cursor.moveToNext();
			}
		}
		cursor.close();
		return rows_num;
	}

	public void clearCouponData()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		int nRow = db.delete("Coupon", "1", null);
		db.close();
		Logs.showTrace("Delete Coupon Record:" + String.valueOf(nRow));
	}

	public void addCouponData(final String strID, final String strCode, final String strTime)
	{
		long nRet = -1;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put("ID", strID);
		values.put("Time", strTime);
		values.put("Code", strCode);

		nRet = db.insert("Coupon", null, values);
		db.close();
		if (-1 == nRet)
		{
			Logs.showTrace("Add Coupon Data Fail");
		}
		else
		{
			Logs.showTrace("Add Coupon Data: " + strID + "," + strCode + "," + strTime);
		}
	}

	public int getCouponData(SparseArray<CouponData> listData)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT ID, Code, Time FROM Coupon", null);
		int rows_num = cursor.getCount();
		if (0 != rows_num)
		{
			CouponData couponData = null;
			cursor.moveToFirst();
			int nIndex = 0;
			for (int i = 0; i < rows_num; ++i)
			{
				nIndex = 0;
				couponData = new CouponData();
				couponData.strID = cursor.getString(nIndex);
				couponData.strCode = cursor.getString(++nIndex);
				couponData.strTime = cursor.getString(++nIndex);
				couponData = null;
				cursor.moveToNext();
			}
		}
		cursor.close();
		return rows_num;
	}
}
