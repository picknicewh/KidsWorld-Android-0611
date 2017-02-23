package net.hongzhang.message.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.mode.ContractInfoVo;
import net.hongzhang.message.R;
import net.hongzhang.message.activity.ContractMemberActivity;
import net.hongzhang.message.bean.GroupMemberVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/9/9
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MemberAdapter  extends BaseAdapter{
    private Context context;
    private List<GroupMemberVo> memberJsonList;
    private String targetGroupId;
    private String targetGroupName;
    public  MemberAdapter(Context context,List<GroupMemberVo> memberJsonList,String targetGroupId, String targetGroupName){
        this.context = context;
        this.memberJsonList = memberJsonList;
        this.targetGroupId = targetGroupId;
        this.targetGroupName = targetGroupName;
    }

    @Override
    public int getCount() {
        return memberJsonList.size();
    }

    @Override
    public Object getItem(int position) {
        return memberJsonList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view==null){
            view = LayoutInflater.from(context).inflate(R.layout.item_member,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = (TextView)view.findViewById(R.id.tv_mname);
            viewHolder.iv_head = (ImageView)view.findViewById(R.id.iv_mhead);
            viewHolder.tv_role = (TextView)view.findViewById(R.id.tv_role);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        GroupMemberVo memberVo = memberJsonList.get(position);

        if (memberVo.getTs_id().equals("add")){
            viewHolder.tv_name.setText("");
            viewHolder.iv_head.setImageResource(R.mipmap.ic_addvalue);
            viewHolder.tv_role.setVisibility(View.GONE);
            viewHolder.iv_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(context, ContractMemberActivity.class);
                    intent.putExtra("title","添加群成员");
                    intent.putExtra("type",1);
                    intent.putExtra("targetGroupId",targetGroupId);
                    intent.putExtra("targetGroupName",targetGroupName);
                    context.startActivity(intent);
                }
            });
        }else if (memberVo.getTs_id().equals("del")){
            viewHolder.tv_name.setText("");
            viewHolder.iv_head.setImageResource(R.mipmap.ic_delvalue);
            viewHolder.tv_role.setVisibility(View.GONE);
            viewHolder.iv_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(context, ContractMemberActivity.class);
                    intent.putExtra("title","删除群成员");
                    intent.putExtra("type",2);
                    intent.putExtra("targetGroupId",targetGroupId);
                    intent.putExtra("targetGroupName",targetGroupName);
                    context.startActivity(intent);
                }
            });
        } else {
            viewHolder.tv_name.setText(memberVo.getTs_name());
            ImageCache.imageLoader(memberVo.getImgUrl(),viewHolder.iv_head);
            viewHolder.tv_role.setVisibility(View.VISIBLE);
            if (memberVo.getRole_type()==1){
                viewHolder.tv_role.setBackgroundResource(R.drawable.user_study_selecter);
                viewHolder.tv_role.setText("学");
            }else {
                viewHolder.tv_role.setBackgroundResource(R.drawable.user_teach_selecter);
                viewHolder.tv_role.setText("师");
            }
        }
        return view;
    }

    private ArrayList<String>  getTargetids(List<ContractInfoVo> memberJsonList){
        ArrayList<String> targetids = new ArrayList<>();
        for (int i = 0 ; i <memberJsonList.size();i++){
            ContractInfoVo memberJson = memberJsonList.get(i);
            targetids.add(memberJson.getTsId());
        }
        return targetids;
    }
    private  class ViewHolder{
        ImageView iv_head;
        TextView tv_name;
        TextView tv_role;
    }
}
