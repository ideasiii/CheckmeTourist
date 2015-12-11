package com.openfile.checkmetourist;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ListGiftHandler extends BaseHandler
{
	private final int	RESOURCE_GIFT_ITEM	= R.layout.gift_item;
	private GridView	giftList			= null;
	private GiftAdapter	giftAdapter			= null;

	public static class GiftData
	{
		public int		nId;
		public String	strName;
		public String	strInfo;
		public String	strPoint;
		public String	strPicUrl;

		public GiftData(final int id, final String name, final String info, final String point, final String pic)
		{
			nId = id;
			strName = name;
			strInfo = info;
			strPoint = point;
			strPicUrl = pic;
		}
	}

	public ListGiftHandler(Activity activity, Handler handler)
	{
		super(activity, handler);
	}

	@Override
	protected void finalize() throws Throwable
	{
		giftAdapter = null;
		super.finalize();
	}

	public void init(View view)
	{
		giftList = (GridView) view.findViewById(R.id.gridViewGift);
		if (null == giftList)
		{
			Logs.showTrace("Get Gift List View Fail");
			return;
		}

		giftAdapter = new GiftAdapter();
		SparseArray<SqliteHandler.GiftData> listData = new SparseArray<SqliteHandler.GiftData>();
		Global.theApplication.checkmeDB.getGiftData(listData);
		SqliteHandler.GiftData giftData = null;
		for (int i = 0; i < listData.size(); ++i)
		{
			giftData = listData.get(i);
			giftAdapter.addGiftItem(i, giftData.strName, giftData.strInfo, giftData.strPoint, giftData.strImage_URL);
		}

		giftAdapter.init(theActivity);
		giftList.setAdapter(giftAdapter);
	}

	/*
	 * public void start() { if (null == giftAdapter) return;
	 * giftAdapter.init(theActivity); giftList.setAdapter(giftAdapter); }
	 */
	private class GiftAdapter extends BaseAdapter
	{
		SparseArray<GiftData>	giftData	= null;
		SparseArray<View>		mItems		= null;

		public GiftAdapter()
		{
			giftData = new SparseArray<GiftData>();
			mItems = new SparseArray<View>();
		}

		public void init(Context context)
		{
			TextView tvName = null;
			ImageView ivPic = null;
			TextView tvPoint = null;

			for (int i = 0; i < giftData.size(); ++i)
			{
				View itemView = createView(RESOURCE_GIFT_ITEM);
				if (null != itemView)
				{
					itemView.setTag(i);
					tvName = (TextView) itemView.findViewById(R.id.TextViewGiftInfo);
					if (null != tvName)
					{
						tvName.setText(giftData.get(i).strName);
					}
					ivPic = (ImageView) itemView.findViewById(R.id.ImageViewGift);
					if (null != ivPic)
					{
						Global.theApplication.imageLoader.loadImage(giftData.get(i).strPicUrl, ivPic);
					}

					tvPoint = (TextView) itemView.findViewById(R.id.TextViewGiftPoint);
					if (null != tvPoint)
					{
						tvPoint.setText(giftData.get(i).strPoint);
					}
					itemView.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							int nIndex = (Integer) v.getTag();
							sendMsg(MSG.GIFT_SELECTED, 0, 0, giftData.get(nIndex));
						}
					});
					mItems.append(i, itemView);
					Logs.showTrace("Add Gift View:" + String.valueOf(i));
				}
			}
		}

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
			return mItems.get(position);
		}

		public void addGiftItem(final int id, final String name, final String info, final String point, final String pic)
		{
			if (null != giftData)
			{
				GiftData itemData = new GiftData(id, name, info, point, pic);
				giftData.append(giftData.size(), itemData);
			}
		}

	}
}
