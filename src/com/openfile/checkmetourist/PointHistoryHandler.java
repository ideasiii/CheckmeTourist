package com.openfile.checkmetourist;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PointHistoryHandler extends BaseHandler
{
	private final static int	RESOURCE_LIST_ITEM	= R.layout.point_history_item;
	private ListView			lvPointHistory		= null;
	private HistoryAdapter		historyAdapter		= null;

	public static class PointHistoryData
	{
		public int		nId;
		public String	strTime;
		public String	strPoint;
		public String	strInfo;
		public boolean	bSubmited;

		public PointHistoryData(final int id, final String time, final String point, final String info, boolean submited)
		{
			nId = id;
			strTime = time;
			strPoint = point;
			strInfo = info;
			bSubmited = submited;
		}
	}

	public PointHistoryHandler(Activity activity, Handler handler)
	{
		super(activity, handler);
	}

	public void init(View view)
	{
		lvPointHistory = (ListView) view.findViewById(R.id.listViewPointHistory);
		if (null != lvPointHistory)
		{
			historyAdapter = new HistoryAdapter();
			updateData();
		}
	}

	public void updateData()
	{
		if (null == historyAdapter)
			return;

		historyAdapter.clear();

		SparseArray<SqliteHandler.ActionData> listData = new SparseArray<SqliteHandler.ActionData>();
		Global.theApplication.checkmeDB.getActionData(listData);
		boolean bUploaded = false;

		for (int i = 0; i < listData.size(); ++i)
		{
			if (listData.get(i).strUpload.trim().equals("Y"))
			{
				bUploaded = true;
			}
			else
			{
				bUploaded = false;
			}

			SparseArray<SqliteHandler.TaskData> listTaskData = new SparseArray<SqliteHandler.TaskData>();
			Global.theApplication.checkmeDB.getTaskDataByTaskId(listTaskData, listData.get(i).strID);

			String strInfo = null;
			for (int j = 0; j < listTaskData.size(); ++j)
			{
				strInfo = listTaskData.get(j).strName;
			}
			historyAdapter.addItem(i, listData.get(i).strTime, listData.get(i).strPoint, strInfo, bUploaded);
		}
		historyAdapter.init(theActivity);
		lvPointHistory.setAdapter(historyAdapter);
	}

	private class HistoryAdapter extends BaseAdapter
	{
		SparseArray<PointHistoryData>	Datas	= null;
		SparseArray<View>				Items	= null;

		public HistoryAdapter()
		{
			Datas = new SparseArray<PointHistoryData>();
			Items = new SparseArray<View>();
		}

		public void init(Context context)
		{
			Items.clear();

			for (int i = 0; i < Datas.size(); ++i)
			{
				View itemView = createView(RESOURCE_LIST_ITEM);

				if (null != itemView)
				{
					((TextView) itemView.findViewById(R.id.textViewPointHistoryDate)).setText(Datas.get(i).strTime
							.substring(0, 10));

					((TextView) itemView.findViewById(R.id.textViewPointHistoryTime)).setText(Datas.get(i).strTime
							.substring(11));

					((TextView) itemView.findViewById(R.id.textViewPointHistoryPoint)).setText("+"
							+ Datas.get(i).strPoint);

					((TextView) itemView.findViewById(R.id.textViewPointHistoryInfo)).setText(Datas.get(i).strInfo);

					Items.append(i, itemView);
				}
			}
		}

		@Override
		public int getCount()
		{
			return Items.size();
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
			return Items.get(position);
		}

		public void clear()
		{
			Datas.clear();
			Items.clear();
		}

		public void addItem(final int id, final String time, final String point, final String info, boolean submited)
		{
			if (null != Datas)
			{
				PointHistoryData itemData = new PointHistoryData(id, time, point, info, submited);
				Datas.append(Datas.size(), itemData);
			}
		}

	}
}
