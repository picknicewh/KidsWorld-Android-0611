package net.hongzhang.school.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.school.R;
import net.hongzhang.school.bean.DishesVo;
import net.hongzhang.user.util.PublishPhotoUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/9/22
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class FoodListAdapter extends BaseAdapter {
    private Context context;
    private List<DishesVo> dishesVoList;

    public FoodListAdapter(Context context, List<DishesVo> dishesVoList) {
        this.context = context;
        this.dishesVoList = dishesVoList;
    }

    @Override
    public int getCount() {
        return dishesVoList.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHold viewHold;
        if(null==view){
            view= LayoutInflater.from(context).inflate(R.layout.item_food_list,null);
            new ViewHold(view);
        }
        final   DishesVo vo=dishesVoList.get(i);
        viewHold= (ViewHold) view.getTag();
        viewHold.tv_food.setText(vo.getCookName());
        viewHold.tv_dinner.setText(vo.getDinnerTime());
        if (vo.getCookUrl().size()>0){
            ImageCache.imageLoader(vo.getCookUrl().get(0),viewHold.iv_food_image);
            viewHold.iv_food_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PublishPhotoUtil.imageBrowernet(0, (ArrayList<String>) vo.getCookUrl(),context);
                }
            });
        }else {
            viewHold.iv_food_image.setImageResource(R.mipmap.ic_img_error);
        }
        viewHold.iv_food_image.setTag(i);

        return view;
    }

    class ViewHold{
        TextView tv_food;
        TextView tv_dinner;
        ImageView iv_food_image;

        public ViewHold(View view) {
            tv_food = (TextView) view.findViewById(R.id.tv_food);
            tv_dinner= (TextView) view.findViewById(R.id.tv_dinner);
            iv_food_image= (ImageView) view.findViewById(R.id.iv_food_image) ;
            view.setTag(this);
        }

    }
}

