package net.hunme.status.widget;

import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import net.hunme.status.R;
import net.hunme.status.StatusFragement;
import net.hunme.status.adapter.ChooseClassAdapter;

import java.util.List;

/**
 * 作者： Administrator
 * 时间： 2016/7/22
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ChooseClassPopWindow extends PopupWindow {
    private StatusFragement statusFragement;
    private View conentView;
    private ListView lv_classchoose;
    private ChooseClassAdapter adapter;
    private List<String> classlist ;
    public  ChooseClassPopWindow(StatusFragement statusFragement,List<String> classlist){
        this.statusFragement = statusFragement;
        this.classlist =classlist;
        init();
    }
    private void initview(){
        conentView = LayoutInflater.from(statusFragement.getActivity()).inflate(R.layout.pop_chooseclass,null);
        lv_classchoose = (ListView) conentView.findViewById(R.id.lv_classchoose);
        lv_classchoose.setDivider(null);
        adapter = new ChooseClassAdapter(statusFragement.getActivity(),classlist);
        lv_classchoose.setAdapter(adapter);
        lv_classchoose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                statusFragement.setClassname(classlist.get(position));
                statusFragement.setWebView(position);
                dismiss();
            }
        });
    }
    public void init() {
        initview();
        //设置SignPopupWindow的View
        this.setContentView(conentView);
        //设置SignPopupWindow弹出窗体的高
        this.setHeight(300);
        //设置SignPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SignPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setTouchable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        //设置SignPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //防止虚拟软键盘被弹出菜单遮住
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
}
