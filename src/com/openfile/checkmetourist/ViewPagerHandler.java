package com.openfile.checkmetourist;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerHandler extends BaseHandler
{
	private ViewPager			viewPager		= null;
	private PagerTabStrip		pagerTab		= null;
	private ViewPagerAdapter	pagerAdapter	= null;
	public static int			PAGE_GIFT;
	public static int			PAGE_FIELD;
	public static int			PAGE_ACCOUNT;
	public static int			PAGE_ABOUT;
	
	public ViewPagerHandler(Activity activity, Handler handler)
	{
		super(activity, handler);
		
	}

	public boolean init()
	{
		viewPager = (ViewPager) theActivity.findViewById(R.id.ViewPager);
		pagerTab = (PagerTabStrip) theActivity.findViewById(R.id.PagerTab);

		if (null == viewPager || null == pagerTab)
		{
			return false;
		}

		pagerTab.setDrawFullUnderline(false);
		pagerTab.setTextSpacing(50);
		pagerTab.setTextColor(Color.parseColor("#FFFFFF"));
		pagerTab.setAnimationCacheEnabled(false);
		pagerTab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

		LayoutInflater Inflater = LayoutInflater.from(theActivity);
		pagerAdapter = new ViewPagerAdapter();

		PAGE_FIELD = pagerAdapter.addPage(Inflater.inflate(R.layout.page_field, null),theActivity.getString(R.string.cooperative_store));
		PAGE_GIFT = pagerAdapter.addPage(Inflater.inflate(R.layout.page_gifts, null), theActivity.getString(R.string.exchange));
		PAGE_ACCOUNT = pagerAdapter.addPage(Inflater.inflate(R.layout.page_account, null), theActivity.getString(R.string.checkme_account));
		PAGE_ABOUT = pagerAdapter.addPage(Inflater.inflate(R.layout.page_about, null), theActivity.getString(R.string.about_checkme));

		viewPager.setAdapter(pagerAdapter);
		return true;
	}

	public View getView(final int nId)
	{
		return pagerAdapter.getView(nId);
	}

	public void showPage(final int nPageIndex)
	{
		if (0 > nPageIndex || pagerAdapter.getCount() <= nPageIndex)
			return;

		viewPager.setCurrentItem(nPageIndex, true);
	}

	private class ViewPagerAdapter extends PagerAdapter
	{

		private class Page
		{
			public View		view		= null;
			public String	strTitle	= null;
		}

		private SparseArray<Page>	Pages	= null;

		public ViewPagerAdapter()
		{
			Pages = new SparseArray<Page>();
		}

		@Override
		public int getCount()
		{
			return Pages.size();
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
		public CharSequence getPageTitle(int position)
		{
			return Pages.get(position).strTitle;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			container.removeView(Pages.get(position).view);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			((ViewPager) container).addView(Pages.get(position).view, 0);
			return Pages.get(position).view;
		}

		public int addPage(final View view, final String strTitle)
		{
			Page page = new Page();
			page.view = view;
			page.strTitle = strTitle;
			Pages.put(Pages.size(), page);
			return (Pages.size() - 1);
		}

		public View getView(final int nId)
		{
			return Pages.get(nId).view;
		}

	}
}
