package net.hongzhang.school.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.hongzhang.school.R;
import net.hongzhang.school.bean.VacationVo;

import java.util.ArrayList;
import java.util.List;


/**
 * 作者：Restring
 * 时间：2017/4/20
 * 描述：
 * 版本：
 */

public class MulLeaveListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<VacationVo> vacationVoListAfter;
    private List<VacationVo> vacationVoListBefore;
    private List<String> titleList;
    private List<Integer> resIdList;
    private List<VacationVo> vacationVoListTotle;

    public enum ViewType {
        PARENT,
        CHILD
    }

    private Context context;


    public MulLeaveListAdapter(Context context, List<VacationVo> vacationVoListAfter, List<VacationVo> vacationVoListBefore, List<String> titleList, List<Integer> resIdList) {
        this.vacationVoListAfter = vacationVoListAfter;
        this.vacationVoListBefore = vacationVoListBefore;
        vacationVoListTotle = new ArrayList<>();
        vacationVoListTotle.addAll(vacationVoListBefore);
        vacationVoListTotle.addAll(vacationVoListAfter);
        this.titleList = titleList;
        this.resIdList = resIdList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (vacationVoListBefore.size() > 0 && vacationVoListAfter.size() > 0) {
            if (position == 0 || position == vacationVoListBefore.size() + 1) {
                return ViewType.PARENT.ordinal();
            } else {
                return ViewType.CHILD.ordinal();
            }
        } else {
            if (position == 0) {
                return ViewType.PARENT.ordinal();
            } else {
                return ViewType.CHILD.ordinal();
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ViewType.CHILD.ordinal()) {
            return new ChildViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_expend_leave_list_c, parent, false));
        } else {
            return new GroupViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_expend_leave_list, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChildViewHolder) {
            ChildViewHolder childViewHolder = (ChildViewHolder) holder;
            if (vacationVoListTotle.size() > 0) {//列表总量大于零，分三种情况，before>0 && after>0 ||  before<0 && after>0  || before>0 && after<0
                VacationVo vo = null;
                if (vacationVoListBefore.size() > 0) {
                    if (position < vacationVoListBefore.size()) {
                        vo = vacationVoListBefore.get(position - 1);//before>0 && after<0
                    } else {
                        if (position < vacationVoListBefore.size()) {
                            vo = vacationVoListBefore.get(position - 1);
                        } else {
                            vo = vacationVoListAfter.get(position - vacationVoListBefore.size() - 1);//before>0 && after>0
                        }
                    }
                } else {
                    vo = vacationVoListAfter.get(position - 1);// before<0 && after>0
                }
                childViewHolder.tv_days.setText(vo.getDay_number() + "天");
                childViewHolder.tv_back_date.setText(vo.getBack_school_date().substring(0, 11));
                childViewHolder.tv_course.setText(vo.getVa_content());
                childViewHolder.tv_name.setText(vo.getTs_name());
            }
        } else if (holder instanceof GroupViewHolder) {
            GroupViewHolder groupViewHolder = (GroupViewHolder) holder;
            if (titleList.size() > 0) {
                String title = "";
                int resId = 0;
                if (titleList.size() == 1) {
                    title = titleList.get(0);
                    resId = resIdList.get(0);
                } else {
                    if (position == 0) {
                        title = titleList.get(0);
                        resId = resIdList.get(0);
                    } else if (position == vacationVoListBefore.size() + 1) {
                        title = titleList.get(1);
                        resId = resIdList.get(1);
                    }
                }
                groupViewHolder.textView.setText(title);
                Drawable drawableLeft = ContextCompat.getDrawable(context, resId);
                drawableLeft.setBounds(0, 0, drawableLeft.getIntrinsicWidth(), drawableLeft.getIntrinsicHeight());
                groupViewHolder.textView.setCompoundDrawables(drawableLeft, null, null, null);
            }
        }
    }

    @Override
    public int getItemCount() {
        return vacationVoListTotle.size() + titleList.size();
    }

    private class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public GroupViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.tv_title);
            view.setTag(this);
        }
    }

    private class ChildViewHolder extends RecyclerView.ViewHolder {
        TextView tv_back_date;
        TextView tv_name;
        TextView tv_days;
        TextView tv_course;

        public ChildViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_back_date = (TextView) view.findViewById(R.id.tv_back_date);
            tv_days = (TextView) view.findViewById(R.id.tv_days);
            tv_course = (TextView) view.findViewById(R.id.tv_course);
            view.setTag(this);
        }
    }

}
