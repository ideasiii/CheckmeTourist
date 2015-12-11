package com.openfile.checkmetourist;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AdBannerHandler extends BaseHandler
{
	private final int		RESOURCE_BANNER			= R.id.ViewPagerBanner;
	private final int		RESOURCE_BANNER_ITEM	= R.layout.banner_item;
	private ViewPager		viewPager				= null;
	private BannerAdapter	bannerAdapter			= null;

	public static class BannerData
	{
		public int		nId;
		public String	strImageURL;
		public String	strContentURL;
		public String	strStartDate;
		public String	strEndDate;

		public BannerData(final int id, final String imageURL, final String contentURL, final String startDate,
				final String endDate)
		{
			nId = id;
			strImageURL = imageURL;
			strContentURL = contentURL;
			strStartDate = startDate;
			strEndDate = endDate;
		}
	}

	public AdBannerHandler(Activity activity, Handler handler)
	{
		super(activity, handler);
	}

	public void init(View view)
	{
		viewPager = (ViewPager) view.findViewById(RESOURCE_BANNER);
		if (null == viewPager)
		{
			Logs.showTrace("Get AD Banner ViewPager Fail");
			return;
		}
		viewPager.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				return true;
			}
		});
		bannerAdapter = new BannerAdapter();

		SparseArray<SqliteHandler.BannerData> listData = new SparseArray<SqliteHandler.BannerData>();
		Global.theApplication.checkmeDB.getBannerData(listData);
		SqliteHandler.BannerData bannerData = null;
		for (int i = 0; i < listData.size(); ++i)
		{
			bannerData = listData.get(i);
			bannerAdapter.addItem(i, bannerData.strImageUrl, bannerData.strContentUrl, bannerData.strStartDate,
					bannerData.strEndDate);
		}

		bannerAdapter.init(theActivity);
		viewPager.setAdapter(bannerAdapter);
		bannerHandler.sendEmptyMessageDelayed(666, 3000);
	}

	private Handler	bannerHandler	= new Handler()
									{
										@Override
										public void handleMessage(Message msg)
										{
											int nTotal = bannerAdapter.getCount();
											int nCurrent = viewPager.getCurrentItem();
											boolean bShowAnim = true;

											if ((--nTotal) <= nCurrent)
											{
												nCurrent = -1;
												bShowAnim = false;
											}

											viewPager.setCurrentItem(++nCurrent, bShowAnim);

											bannerHandler.sendEmptyMessageDelayed(666, 3000);
										}
									};

	private class BannerAdapter extends PagerAdapter
	{
		SparseArray<BannerData>	bannerDatas	= null;
		SparseArray<View>		mItems		= null;

		public BannerAdapter()
		{
			bannerDatas = new SparseArray<BannerData>();
			mItems = new SparseArray<View>();
		}

		public void init(Context context)
		{
			ImageView ivPic = null;
			mItems.clear();
			for (int i = 0; i < bannerDatas.size(); ++i)
			{
				View itemView = createView(RESOURCE_BANNER_ITEM);
				if (null != itemView)
				{
					ivPic = (ImageView) itemView.findViewById(R.id.imageViewAD);
					if (null != ivPic)
					{
						ivPic.setTag(bannerDatas.get(i).strContentURL);
						Global.theApplication.imageLoader.loadImage(bannerDatas.get(i).strImageURL, ivPic);
						ivPic.setOnClickListener(new OnClickListener()
						{
							@Override
							public void onClick(View v)
							{
								Logs.showTrace("Banner Click Go:" + (String) v.getTag());
								AdBannerHandler.this.postMsg(MSG.BANNER_SELECTED, 0, 0, v.getTag());
							}
						});
					}
					mItems.append(i, itemView);
				}
			}
		}

		@Override
		public int getCount()
		{
			return mItems.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			((ViewPager) container).addView(mItems.get(position), 0);
			return mItems.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1)
		{
			return (arg0 == arg1);
		}

		@Override
		public int getItemPosition(Object object)
		{
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			container.removeView(mItems.get(position));
		}

		public void addItem(final int id, final String imageURL, final String contentURL, final String startDate,
				final String endDate)
		{
			if (null != bannerDatas)
			{
				BannerData itemData = new BannerData(id, imageURL, contentURL, startDate, endDate);
				bannerDatas.append(bannerDatas.size(), itemData);
			}
		}

	}
}
