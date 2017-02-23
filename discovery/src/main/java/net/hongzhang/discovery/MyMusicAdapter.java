package net.hongzhang.discovery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.UserMessage;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wh
 * 时间： 2016/11/11
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MyMusicAdapter extends BaseAdapter implements OkHttpListener{
    private Context context;
    private List<ResourceVo> resourceVos;
    private int mposition;
    /**
     * 点赞集合
     */
    private  Map<Integer,Integer> attentionids;
    public MyMusicAdapter(Context context, List<ResourceVo> resourceVos, int mposition){
        this.context = context;
        this.resourceVos = resourceVos;
        this.mposition = mposition;
    }
    @Override
    public int getCount() {
        return resourceVos.size();
    }

    @Override
    public Object getItem(int position) {
        return resourceVos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view==null){
            view = LayoutInflater.from(context).inflate(R.layout.item_poplist,null);
             viewHolder = new ViewHolder(view);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final ResourceVo resourceVo = resourceVos.get(position);
        //setData(resourceVos);
        viewHolder.tv_name.setText(resourceVo.getResourceName());
       /* if (attentionids.get(position)==2){
            viewHolder.iv_collect.setImageResource(R.mipmap.star_dark);
        }else {
            viewHolder.iv_collect.setImageResource(R.mipmap.star_dark_full);
        }
        if (position==mposition){
            viewHolder.tv_name.setTextColor(context.getResources().getColor(R.color.main_green));
        }else {
            viewHolder.tv_name.setTextColor(context.getResources().getColor(R.color.default_grey));
        }
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.iv_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCollect(position, finalViewHolder);
            }
        });*/
        return view;
    }
    private void subAttention(int cancel, ResourceVo resourceVo){
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(context).getTsId());
        params.put("albumId",resourceVo.getAlbumId());
        params.put("type",cancel);
        Type type = new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.SUBATTENTION,params,this);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        if (date!=null){
            Result<String> data  = (Result<String>) date;
            String result = data.getData();
            Toast.makeText(context,result,Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onError(String uri, String error) {
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }
  /*  private void setCollect(int position,ViewHolder viewHolder){
        PlayListPopuWindow.isEdit = true;
        int  cancel =attentionids.get(position);
        if (cancel==1){//收藏，取消收藏
            cancel=2;
            viewHolder.iv_collect.setImageResource(R.mipmap.star_dark);
        }else if (cancel==2){//没收藏，收藏
            cancel =1;
            viewHolder.iv_collect.setImageResource(R.mipmap.star_dark_full);
        }
        attentionids.put(position,cancel);
        subAttention(cancel,resourceVos.get(position));
    }*/
    private  class ViewHolder{
        TextView tv_name;
      // ImageView iv_collect;
        public ViewHolder(View view){
            tv_name = (TextView)view.findViewById(R.id.tv_song_name);
          //  iv_collect = (ImageView)view.findViewById(R.id.iv_collect);
            view.setTag(this);
        }
    }
}
