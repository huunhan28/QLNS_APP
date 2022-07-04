package com.example.food.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.food.R;

import java.util.HashMap;
import java.util.List;

public class ExpandableTextViewAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public ExpandableTextViewAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.description_answer, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.descriptionAnswerView);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.discription_title, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.descriptionTitleView);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
//    Context context;
//
//    String expand="Description";
//
//    String answer="Answer for description";
//
//    public ExpandableTextViewAdapter(Context context, String expand, String answer) {
//        this.context = context;
//        this.expand = expand;
//        this.answer = answer;
//    }
//
//    @Override
//    public int getGroupCount() {
//        return 1;
//    }
//
//    @Override
//    public int getChildrenCount(int i) {
//        return 1;
//    }
//
//    @Override
//    public Object getGroup(int i) {
//        return expand;
//    }
//
//    @Override
//    public Object getChild(int i, int i1) {
//        return answer;
//    }
//
//    @Override
//    public long getGroupId(int i) {
//        return 1;
//    }
//
//    @Override
//    public long getChildId(int i, int i1) {
//        return 1;
//    }
//
//    @Override
//    public boolean hasStableIds() {
//        return false;
//    }
//
//    @Override
//    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
//        String dis= (String) getGroup(i);
//        if(view==null){
//            LayoutInflater inflater= (LayoutInflater) this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
//            view=inflater.inflate(R.layout.discription_title,null);
//        }
//        TextView dis2=view.findViewById(R.id.descriptionTitleView);
//        dis2.setTypeface(null, Typeface.BOLD);
//        dis2.setText(dis);
//        return view;
//    }
//
//    @Override
//    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
//        String ans= (String) getChild(i,i1);
//        if(view==null){
//            LayoutInflater inflater= (LayoutInflater) this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
//            view=inflater.inflate(R.layout.discription_answer,null);
//        }
//        TextView ans2=view.findViewById(R.id.descriptionAnswerView);
//        ans2.setText(ans);
//        return view;
//    }
//
//    @Override
//    public boolean isChildSelectable(int i, int i1) {
//        return false;
//    }
}
