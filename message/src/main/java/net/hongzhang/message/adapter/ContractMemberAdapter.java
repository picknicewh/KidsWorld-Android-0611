package net.hongzhang.message.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.message.R;
import net.hongzhang.message.bean.GroupMemberVo;

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
public class ContractMemberAdapter extends BaseAdapter  {
	private List<GroupMemberVo> list = null;
	private Context mContext;
	private static HashMap<Integer, Boolean> isSelected;
	public ContractMemberAdapter(Context mContext, List<GroupMemberVo> list) {
		this.mContext = mContext;
		this.list = list;
		isSelected = new HashMap<>();
		initDate();

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
		GroupMemberVo mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_member_contract, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_member_title);
            viewHolder.ivimage = (ImageView)view.findViewById(R.id.iv_member_head);
			viewHolder.checkBox = (CheckBox)view.findViewById(R.id.cb_member_choose);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.checkBox.setChecked(getIsSelected().get(position));
		viewHolder.tvTitle.setText(mContent.getTs_name());
		ImageCache.imageLoader(mContent.getImgUrl(),viewHolder.ivimage);
		return view;
	}

	public   class ViewHolder {
	    public TextView tvTitle;
        public ImageView ivimage;
		public  CheckBox checkBox;
	}

	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}
}