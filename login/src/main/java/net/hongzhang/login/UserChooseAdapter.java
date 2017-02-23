package net.hongzhang.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.widget.CircleImageView;
import net.hongzhang.login.mode.CharacterSeleteVo;

import java.util.List;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/26
 * 描    述：
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class UserChooseAdapter extends BaseAdapter {
    private Context context;
    private List<CharacterSeleteVo> seleteList;
    private View lastView;
    private int selectPosition=0;
    public UserChooseAdapter(Context context, List<CharacterSeleteVo> seleteList) {
        this.context = context;
        this.seleteList = seleteList;
    }

    @Override
    public int getCount() {
        return seleteList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHold hold;
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.item_user_choose,null);
            new ViewHold(view);
        }
        hold= (ViewHold) view.getTag();
        CharacterSeleteVo selete=seleteList.get(i);
        hold.tv_name.setText(selete.getName());
        ImageCache.imageLoader(selete.getImg(),hold.cv_head);
        String clsssName=selete.getClassName();
        String schoolName= selete.getSchoolName();
        if(G.isEmteny(clsssName)){
            clsssName="";
        }
        if(G.isEmteny(schoolName)){
            schoolName="";
        }
        hold.tv_address.setText(schoolName+" "+clsssName);
        if(i==selectPosition){
            changItemImage(view,i);
        }
//        if("0".equals(selete.getType())){
//            hold.tv_id.setText("师");
//            hold.tv_id.setBackgroundResource(R.drawable.user_teach_selecter);
//        }else
        if("1".equals(selete.getType())){
            hold.tv_id.setText("学");
            hold.tv_id.setBackgroundResource(R.drawable.user_study_selecter);
        }else{
            hold.tv_id.setText("师");
            hold.tv_id.setBackgroundResource(R.drawable.user_teach_selecter);
        }
//        }else if("2".equals(selete.getType())){
//            hold.tv_id.setText("校");
//            hold.tv_id.setBackgroundResource(R.drawable.user_director_selectet);
//        }
        return view;
    }

    class ViewHold{
        CircleImageView cv_head;
        TextView tv_name;
        TextView tv_id;
        TextView tv_address;
        ImageView iv_choose;

        public ViewHold(View view) {
            cv_head= (CircleImageView) view.findViewById(R.id.cv_head);
            tv_name= (TextView) view.findViewById(R.id.tv_name);
            tv_id= (TextView) view.findViewById(R.id.tv_id);
            tv_address= (TextView) view.findViewById(R.id.tv_address);
            iv_choose= (ImageView) view.findViewById(R.id.iv_choose);
            view.setTag(this);
        }
    }

    public void changItemImage(View view,int position){
        if(lastView!=null&&selectPosition!=position){
            ViewHold hold= (ViewHold) lastView.getTag();
            if(hold.iv_choose.getVisibility()==View.VISIBLE){
                hold.iv_choose.setVisibility(View.INVISIBLE);
            }
        }
        this.lastView=view;
        this.selectPosition=position;
        ViewHold hold= (ViewHold) view.getTag();
        if(hold.iv_choose.getVisibility()==View.INVISIBLE) hold.iv_choose.setVisibility(View.VISIBLE);
    }

    public void setSelectPosition(int position){
        this.selectPosition=position;
    }
}
