package net.hongzhang.school.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import net.hongzhang.baselibrary.util.G;
import net.hongzhang.school.R;
import net.hongzhang.school.bean.ClassVo;

import java.util.HashMap;
import java.util.List;


public class ChooseMyClassAdapter extends BaseAdapter {
	private List<ClassVo> dynamicList ;
	private Context mContext;
	 /// 用来控制CheckBox的选中状况
	private static HashMap<Integer, Boolean> isSelected;
	public ChooseMyClassAdapter(Context mContext,  List<ClassVo> dynamicList) {
		this.mContext = mContext;
		this.dynamicList = dynamicList;
		isSelected = new HashMap<>();
		initDate();
		G.log("-----------------");
	}
	/**
	 * 初始化isSelected的数据
	 */
	private void initDate() {
		for (int i = 0; i < dynamicList.size(); i++) {
			getIsSelected().put(i, false);
		}
	}
	public int getCount() {
		return  dynamicList.size();
	}

	public Object getItem(int position) {
		return dynamicList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView( int position, View view, ViewGroup arg2) {
		G.log("---------xx--------");
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_choose_class, null);
			viewHolder.tv_class = (TextView) view.findViewById(R.id.tv_class);
			viewHolder.cb_class = (CheckBox) view.findViewById(R.id.cb_class);
			view.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) view.getTag();
		}
		ClassVo dynamicVo = dynamicList.get(position);
		viewHolder.tv_class.setText(dynamicVo.getClass_name());
		// 根据isSelected来设置checkbox的选中状况
		viewHolder.cb_class.setChecked(getIsSelected().get(position));
		return view;
	}
	public class ViewHolder {
		public TextView tv_class;
		public CheckBox cb_class;
	}
	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}
	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		ChooseMyClassAdapter.isSelected = isSelected;
	}
}