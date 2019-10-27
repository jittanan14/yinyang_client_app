package com.example.yinyang_taengkwa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.yinyang_taengkwa.R;
import com.example.yinyang_taengkwa.models.Question_choose;

import org.w3c.dom.Text;

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
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(resId, null);

        TextView num = view.findViewById(R.id.num);
        TextView name = view.findViewById(R.id.textview_nameques);
        String ques = questionList.get(position);

        num.setText(String.valueOf(position+1));
        name.setText(ques);



        return view;
    }


}
