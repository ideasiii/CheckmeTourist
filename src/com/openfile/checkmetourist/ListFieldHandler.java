package com.openfile.checkmetourist;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ListFieldHandler extends BaseHandler
{
	private final int		RESOURCE_LIST_FIELD	= R.id.listViewStores;
	private final int		RESOURCE_FIELD_ITEM	= R.layout.field_item;
	private ListView		lvField				= null;
	private FieldAdapter	fieldAdapter		= null;

	public static class FieldData
	{
		public int		nId;
		public String	strChannelId;
		public String	strImageURL;
		public String	strSubImageURL;
		public String	strName;
		public String	strInfo;
		public String	strUpdateDate;
		public String	strPoint;

		public FieldData(final int id, final String channelId, final String imageURL, final String subImageURL,
				final String name, final String info, final String updateDate, final String point)
		{
			nId = id;
			strChannelId = channelId;
			strImageURL = imageURL;
			strSubImageURL = subImageURL;
			strName = name;
			strInfo = info;
			strUpdateDate = updateDate;
			strPoint = point;
		}
	}

	public ListFieldHandler(Activity activity, Handler handler)
	{
		super(activity, handler);
	}

	public void init(View view)
	{
		lvField = (ListView) view.findViewById(RESOURCE_LIST_FIELD);
		if (null == lvField)
		{
			Logs.showTrace("Get Field List View Fail");
			return;
		}
		fieldAdapter = new FieldAdapter();

		SparseArray<SqliteHandler.FieldData> listData = new SparseArray<SqliteHandler.FieldData>();
		Global.theApplication.checkmeDB.getFieldData(listData);
		SqliteHandler.FieldData fieldData = null;
		for (int i = 0; i < listData.size(); ++i)
		{
			fieldData = listData.get(i);
			fieldAdapter.addItem(i, fieldData.strId, fieldData.strName, fieldData.strImage_URL,
					fieldData.strSubImage_URL, fieldData.strInfo, fieldData.strPoint, fieldData.strUpdate_Date);
		}

		fieldAdapter.init(theActivity);
		if (null != lvField)
			lvField.setAdapter(fieldAdapter);
	}

	private class FieldAdapter extends BaseAdapter
	{
		SparseArray<FieldData>	fieldDatas	= null;
		SparseArray<View>		mItems		= null;

		public FieldAdapter()
		{
			fieldDatas = new SparseArray<FieldData>();
			mItems = new SparseArray<View>();
		}

		public void init(Context context)
		{
			mItems.clear();
			for (int i = 0; i < fieldDatas.size(); ++i)
			{
				View itemView = createView(RESOURCE_FIELD_ITEM);
				if (null != itemView)
				{
					itemView.setTag(fieldDatas.get(i).strChannelId);
					ImageView imgLogo = (ImageView) itemView.findViewById(R.id.imageViewFieldLogo);
					if (null != imgLogo)
					{
						Global.theApplication.imageLoader.loadImage(fieldDatas.get(i).strImageURL, imgLogo);
					}
					TextView tvName = (TextView) itemView.findViewById(R.id.textViewFieldName);
					if (null != tvName)
					{
						tvName.setText(fieldDatas.get(i).strName);
					}
					TextView imgReward = (TextView) itemView.findViewById(R.id.textViewFieldReward);
					if (null != imgReward)
					{
						imgReward.setText(theActivity.getString(R.string.have) +" "+ fieldDatas.get(i).strPoint +" "+theActivity.getString(R.string.point));
					}
					mItems.append(i, itemView);

					itemView.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							String strChannelId = (String) v.getTag();
							postMsg(MSG.FIELD_SELECTED, 0, 0, strChannelId);
						}
					});
				}
			}
		}

		@Override
		public int getCount()
		{
			return fieldDatas.size();
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
			return mItems.get(position);
		}

		public void addItem(final int nId, final String strChannelId, final String strName, final String strImage_URL,
				final String strSubImage_URL, final String strInfo, final String strPoint, final String strUpdate_Date)
		{
			if (null != fieldDatas)
			{
				FieldData itemData = new FieldData(nId, strChannelId, strImage_URL, strSubImage_URL, strName, strInfo,
						strUpdate_Date, strPoint);
				fieldDatas.append(fieldDatas.size(), itemData);
			}
		}
	}

}
