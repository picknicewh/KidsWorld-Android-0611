package net.hongzhang.message.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.hongzhang.baselibrary.util.G;
import net.hongzhang.message.R;
import net.hongzhang.message.bean.RyUserInfo;
import net.hongzhang.message.widget.ConformDialog;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * 作者： wanghua
 * 时间： 2017/3/14
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */


public class PhoneListAdapter extends RecyclerView.Adapter<PhoneListAdapter.ViewHolder> {
    private Activity context;
    // private List<RyUserInfor.AccountInfo> accountInfos;
    private RyUserInfo ryUserInfo;
    private String tsId;

    public PhoneListAdapter(Activity context, RyUserInfo ryUserInfo, String tsId) {
        this.context = context;
        this.ryUserInfo = ryUserInfo;
        this.tsId = tsId;
        //accountInfos = ryUserInfo.getAccount_info();

    }

    private PhoneListAdapter.onItemClickListener itemClickListener = null;

    @Override
    public PhoneListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                context).inflate(R.layout.item_phone_list, parent, false);
        PhoneListAdapter.ViewHolder holder = new PhoneListAdapter.ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final PhoneListAdapter.ViewHolder holder, final int position) {
        // final RyUserInfor.AccountInfo accountInfo = accountInfos.get(position);
        holder.tv_phoneNum.setText(ryUserInfo.getPhone());
        holder.tv_name.setText(ryUserInfo.getName());
        holder.iv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConformDialog dialog = new ConformDialog(context, ryUserInfo.getPhone());
                dialog.initView();
            }
        });
        final UserInfo userInfo = new UserInfo(tsId, ryUserInfo.getTsName(), Uri.parse(ryUserInfo.getImgUrl()));
        holder.iv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!G.isEmteny(tsId) && !G.isEmteny(ryUserInfo.getTsName())) {
                    if (RongIM.getInstance() != null) {
                        RongIM.getInstance().startPrivateChat(context, tsId, ryUserInfo.getTsName());
                        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                            @Override
                            public UserInfo getUserInfo(String userId) {
                                if (null != ryUserInfo.getImgUrl()) {
                                    return userInfo;
                                }
                                return null;
                            }
                        }, true);
                        if (null != ryUserInfo.getImgUrl()) {
                            RongIM.getInstance().refreshUserInfoCache(userInfo);
                        }
                    }
                    context.finish();
                }
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_phone;
        private TextView tv_phoneNum;
        private ImageView iv_message;
        private TextView tv_name;

        public ViewHolder(View view) {
            super(view);
            iv_phone = (ImageView) view.findViewById(R.id.iv_phone);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_phoneNum = (TextView) view.findViewById(R.id.tv_phone);
            iv_message = (ImageView) view.findViewById(R.id.iv_message);
            view.setTag(this);
        }
    }

    public void setOnItemClickListener(PhoneListAdapter.onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface onItemClickListener {
        void OnItemClick(View view, int position);
    }
}
