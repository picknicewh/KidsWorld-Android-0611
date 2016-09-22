package net.hunme.school.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.hunme.baselibrary.image.ImageCache;
import net.hunme.school.R;
import net.hunme.school.bean.SyllabusVo;
import net.hunme.school.widget.DeleteCourseDialog;

import java.util.List;

/**
 * ================================================
 * 作    者：wh
 * 时    间：2016/9/19
 * 描    述：课程安排--适配器
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class CourseListAdapter extends BaseAdapter  {
    private Activity context;
    private List<SyllabusVo> syllabusVoList;

    public CourseListAdapter(Activity context, List<SyllabusVo> publishList) {
        this.context = context;
        this.syllabusVoList = publishList;
    }

    @Override
    public int getCount() {
        return syllabusVoList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHold viewHold;
        if(null==view){
            view= LayoutInflater.from(context).inflate(R.layout.item_course_arrange,null);
            new ViewHold(view);
        }
        final SyllabusVo vo=syllabusVoList.get(i);
        viewHold= (ViewHold) view.getTag();
        viewHold.tv_date.setText(vo.getCreationTime().substring(0,10));
        viewHold.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteCourseDialog dialog = new DeleteCourseDialog(context,vo.getSyllabusId());
                dialog.initView();
            }
        });
        viewHold.tv_describle.setText(vo.getTitle());
        ImageCache.imageLoader(vo.getImgs().get(0),viewHold.iv_course);
        return view;
    }

    class ViewHold{
         TextView tv_date;
         ImageView iv_course;
         TextView tv_delete;
         TextView tv_describle;

        public ViewHold(View view) {
            tv_delete = (TextView) view.findViewById(R.id.tv_delete);
            tv_date= (TextView) view.findViewById(R.id.tv_date);
            tv_describle= (TextView) view.findViewById(R.id.tv_desrcible);
            iv_course = (ImageView)view.findViewById(R.id.iv_course);
            view.setTag(this);
        }
     }
}
