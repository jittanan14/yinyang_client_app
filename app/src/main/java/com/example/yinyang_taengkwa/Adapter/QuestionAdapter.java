package com.example.jittanan.yhinyhang.Adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class QuestionAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private List<String> questionList;
    private int resId;


    public QuestionAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.resId = resource;
        this.questionList = objects;
    }


}
