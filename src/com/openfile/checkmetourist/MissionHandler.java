package com.openfile.checkmetourist;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MissionHandler extends BaseHandler
{
	private final int		RESOURCE_GRID_MISSION	= R.id.gridViewMissionList;
	private final int		RESOURCE_ITEM_MISSION	= R.layout.mission_item;
	private TextView		tvTitle					= null;
	private ImageView		ivLogo					= null;
	private GridView		gridView				= null;
	private FlipperView		flipper					= null;
	private MissionAdapter	missionAdapter			= null;
	private int				ID_MISSION_ENTER		= -1;
	private TextView		tvMissionEnterTitle		= null;
	private ImageView		ivMissionEnterPic		= null;
	private TextView		tvMissionEnterInfo		= null;
	private TextView		tvMissionEnterReward	= null;
	private ImageView		ivScanBtn				= null;

	public static class MissionData
	{
		public int		nId;
		public String	strTaskId;
		public String	strTitle;
		public String	strInfo;
		public String	strReward;
		public String	strPicUrl;

		public MissionData()
		{

		}

		public MissionData(final int id, final String taskid, final String title, final String info,
				final String reward, final String pic)
		{
			nId = id;
			strTaskId = taskid;
			strTitle = title;
			strInfo = info;
			strReward = reward;
			strPicUrl = pic;
		}

		public void setData(final int id, final String taskid, final String title, final String info,
				final String reward, final String pic)
		{
			nId = id;
			strTaskId = taskid;
			strTitle = title;
			strInfo = info;
			strReward = reward;
			strPicUrl = pic;
		}
	}

	public MissionHandler(Activity activity, Handler handler)
	{
		super(activity, handler);
		missionAdapter = new MissionAdapter();
	}

	public void init(View viewMission)
	{
		if (null == viewMission)
			return;

		gridView = (GridView) viewMission.findViewById(RESOURCE_GRID_MISSION);
		if (null == gridView)
		{
			Logs.showTrace("Get Mission Grid View Fail");
		}
		tvTitle = (TextView) viewMission.findViewById(R.id.textViewMissionTitle);
		ivLogo = (ImageView) viewMission.findViewById(R.id.imageViewMissionLogo);
		flipper = (FlipperView) viewMission.findViewById(R.id.flipperViewMissionEnter);
		if (null != flipper)
		{
			ID_MISSION_ENTER = flipper.addChild(R.layout.mission_enter);
			tvMissionEnterTitle = (TextView) flipper.findViewById(R.id.textViewMissionEnterName);
			tvMissionEnterInfo = (TextView) flipper.findViewById(R.id.textViewMissionEnterInfo);
			ivMissionEnterPic = (ImageView) flipper.findViewById(R.id.imageViewMissionEnterPic);
			tvMissionEnterReward = (TextView) flipper.findViewById(R.id.textViewMissionEnterReward);
			ivScanBtn = (ImageView) flipper.findViewById(R.id.imageViewMissionEnterScanBtn);
		}
	}

	public void setMissionData(final String strChannelId)
	{
		if (null == strChannelId)
			return;
		SparseArray<SqliteHandler.FieldData> listData = new SparseArray<SqliteHandler.FieldData>();
		Global.theApplication.checkmeDB.getFieldDataByChannelId(listData, strChannelId);

		// Set Logo and Title
		if (0 < listData.size())
		{
			if (null != tvTitle)
			{
				tvTitle.setText(listData.get(0).strName);
			}

			if (null != ivLogo)
			{
				Global.theApplication.imageLoader.loadImage(listData.get(0).strSubImage_URL, ivLogo);
			}
			Global.theApplication.submitLog("15", "CheckMe", "MissionHandler", listData.get(0).strName, "");
		}

		missionAdapter.clear();

		SparseArray<SqliteHandler.TaskData> listTaskData = new SparseArray<SqliteHandler.TaskData>();
		Global.theApplication.checkmeDB.getTaskDataByChannelId(listTaskData, strChannelId);
		SqliteHandler.TaskData taskData = null;
		for (int i = 0; i < listTaskData.size(); ++i)
		{
			taskData = listTaskData.get(i);
			missionAdapter.addMissionItem(i, taskData.strId, taskData.strName, taskData.strInfo, taskData.strPoint,
					taskData.strImage_URL);
		}

		if (null != gridView)
		{
			missionAdapter.init(theActivity);
			gridView.setAdapter(missionAdapter);
		}
	}

	public int getMissionData(final int nIndex, MissionData missionData)
	{
		int nResult = 0;
		if (null != missionAdapter)
		{
			nResult = missionAdapter.getMissionData(nIndex, missionData);
		}
		return nResult;
	}

	public void addActionData(MissionData missionData, final String strBarcode)
	{
		if (null == missionData)
			return;
		SimpleDateFormat formatter = null;
		formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault());
		Date curDate = new Date(System.currentTimeMillis());
		String strTime = formatter.format(curDate);

		Global.theApplication.checkmeDB.addActionData(strTime, missionData.strReward, missionData.strTaskId, "I", "N",
				"N", strBarcode, null, null, null);

	}

	public void missionResult(final int nIndex, final String strQrcode)
	{
		if (Utility.checkInvoice(strQrcode))
		{
			MissionData missionData = new MissionData();
			if (0 < getMissionData(nIndex, missionData))
			{
				addActionData(missionData, strQrcode);

				Logs.showTrace("Get Mission Data: " + missionData.strTaskId + " " + missionData.strTitle + " "
						+ missionData.strReward);

				if (Global.theApplication.checkmeApi.runApi(CheckmeApi.API_INVOICE, theHandler, missionData.strTaskId,
						strQrcode))
				{
					Global.theApplication.submitLog("15", "CheckMe", "QR Code Scan", missionData.strTitle, "");
				}

				DialogHandler.showReward(theActivity, missionData.strReward);
				MediaPlayer.create(theActivity, R.raw.crrect_answer3).start();
			}
			else
			{
				DialogHandler.showAlert(theActivity, theActivity.getString(R.string.invalid_mission), false);
			}
		}
		else
		{
			DialogHandler.showAlert(theActivity, theActivity.getString(R.string.invalid_invoice), false);
		}
	}

	private class MissionAdapter extends BaseAdapter
	{
		SparseArray<MissionData>	missionDatas	= null;
		SparseArray<View>			mItems			= null;

		public MissionAdapter()
		{
			missionDatas = new SparseArray<MissionData>();
			mItems = new SparseArray<View>();
		}

		public void clear()
		{
			if (null != missionDatas)
				missionDatas.clear();
			if (null != mItems)
				mItems.clear();
		}

		public void init(Context context)
		{
			TextView tvInfo = null;
			ImageView ivPic = null;
			TextView tvPoint = null;
			for (int i = 0; i < missionDatas.size(); ++i)
			{
				View itemView = createView(RESOURCE_ITEM_MISSION);
				if (null != itemView)
				{
					itemView.setTag(missionDatas.get(i).nId);
					tvInfo = (TextView) itemView.findViewById(R.id.textViewMissionInfo);
					if (null != tvInfo)
					{
						tvInfo.setText(missionDatas.get(i).strTitle);
					}
					tvPoint = (TextView) itemView.findViewById(R.id.textViewMissionReward);
					if (null != tvPoint)
					{
						tvPoint.setText(missionDatas.get(i).strReward);
					}
					ivPic = (ImageView) itemView.findViewById(R.id.imageViewMissionPic);
					if (null != ivPic)
					{
						Global.theApplication.imageLoader.loadImage(missionDatas.get(i).strPicUrl, ivPic);
					}
					itemView.setOnClickListener(itemClickListen);
					mItems.append(i, itemView);
				}
			}
		}

		private void initMissionEnter(final int nIndex)
		{
			final MissionData mData = missionDatas.get(nIndex);
			if (null != tvMissionEnterTitle)
			{
				tvMissionEnterTitle.setText(mData.strTitle);
			}
			if (null != ivMissionEnterPic)
			{
				Global.theApplication.imageLoader.loadImage(mData.strPicUrl, ivMissionEnterPic);
			}
			if (null != tvMissionEnterInfo)
			{
				tvMissionEnterInfo.setText(mData.strInfo);
			}
			if (null != tvMissionEnterReward)
			{
				tvMissionEnterReward.setText(mData.strReward);
			}
			if (null != ivScanBtn)
			{
				ivScanBtn.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						MissionHandler.this.sendMsg(MSG.QR_SCANNER, nIndex, 0, mData.strReward);
					}
				});
			}
			Global.theApplication.submitLog("15", "CheckMe", "Mission List", mData.strTitle, "");
		}

		private OnClickListener itemClickListen = new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				int nIndex = (Integer) v.getTag();
				initMissionEnter(nIndex);
				flipper.showView(ID_MISSION_ENTER);
			}
		};

		@Override
		public int getCount()
		{
			return mItems.size();
		}

		@Override
		public Object getItem(int position)
		{
			return position;
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{

			if (null != mItems)
			{
				return mItems.get(position);
			}
			return null;
		}

		public void addMissionItem(final int id, final String taskid, final String title, final String info,
				final String reward, final String pic)
		{
			if (null != missionDatas)
			{
				MissionData itemData = new MissionData(id, taskid, title, info, reward, pic);
				missionDatas.append(missionDatas.size(), itemData);
			}
		}

		public int getMissionData(final int nIndex, MissionData missionData)
		{
			int nResult = 0;

			if (null != missionDatas)
			{
				MissionData data = missionDatas.get(nIndex);
				if (null != data)
				{
					missionData.setData(nIndex, data.strTaskId, data.strTitle, data.strInfo, data.strReward,
							data.strPicUrl);
					nResult = 1;
				}
			}
			return nResult;
		}
	}

	public boolean isShowMissionEnter()
	{
		if (ID_MISSION_ENTER == flipper.getShowIndex())
			return true;
		return false;
	}

	public void closeMissionEnter()
	{
		flipper.close();
	}
}
