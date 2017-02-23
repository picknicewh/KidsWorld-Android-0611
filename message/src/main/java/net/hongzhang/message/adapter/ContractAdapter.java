package net.hongzhang.message.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.hongzhang.message.R;
import net.hongzhang.message.bean.GroupMemberBean;

import java.util.HashMap;
import java.util.List;
/**
 * 作者： wh
 * 时间： 2016/7/15
 * 名称：联系人列表---适配器
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ContractAdapter extends BaseAdapter implements SectionIndexer {
	private List<GroupMemberBean> list = null;
	private Context mContext;

	private static HashMap<Integer, Boolean> isSelected;
	public ContractAdapter(Context mContext, List<GroupMemberBean> list) {
		this.mContext = mContext;
		this.list = list;

		isSelected = new HashMap<>();
		initDate();

	}
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 *
	 * @param list
	 */
	public void updateListView(List<GroupMemberBean> list) {
		this.list = list;
		notifyDataSetChanged();
	}
	/**
	 * 初始化isSelected的数据
	 */
	private void initDate() {
		for (int i = 0; i < list.size(); i++) {
			getIsSelected().put(i, false);
		}
	}
	public int getCount() {
		return list.size() >0 ? list.size():0;
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final GroupMemberBean mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_activity_contract, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_title_item);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.tv_catalog_item);
            viewHolder.ivimage = (ImageView)view.findViewById(R.id.iv_image_item);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		// 根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getSortLetters());
		} else {
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
		viewHolder.tvTitle.setText(this.list.get(position).getName());
		ImageLoader.getInstance().displayImage(mContent.getImg(),viewHolder.ivimage);
		return view;
	}

	public   class ViewHolder {
		public TextView tvLetter;
		public TextView tvTitle;
        public ImageView ivimage;

	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}
	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}
}