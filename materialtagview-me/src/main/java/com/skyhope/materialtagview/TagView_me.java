package com.skyhope.materialtagview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.skyhope.materialtagview.adapter.TagViewAdapter;
import com.skyhope.materialtagview.enums.TagSeparator;
import com.skyhope.materialtagview.interfaces.TagClickListener;
import com.skyhope.materialtagview.interfaces.TagItemListener;
import com.skyhope.materialtagview.model.TagModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagView_me extends FlexboxLayout implements TagClickListener {

    private List<TagModel> mTagList = new ArrayList<>();
    private List<String> mTagItemList = new ArrayList<>();

    private TagViewAdapter mAdapter;

    private int tagLimit = Integer.MAX_VALUE;

    private String tagSeparator = TagSeparator.valueOf(TagSeparator.COMMA_SEPARATOR.name()).getValue();

    private int tagBackgroundColor;
    private int mTagTextColor;
    private String mTagLimitMessage;

    private Drawable mCrossDrawable;
    private Bitmap mCrossBitmap;

    private TagItemListener mTagItemListener;

    private TextView textLayout, textViewAdd;
    private RecyclerView recyclerView;

    private final String EMPTY_STRING = "";
    private final String DEFAULT_TAG_LIMIT_MESSAGE = "You reach maximum tags";

    public TagView_me(Context context) {
        super(context);
    }

    public TagView_me(Context context, AttributeSet attrs) {
        super(context, attrs);

        setFlexWrap(FlexWrap.WRAP);

        setJustifyContent(JustifyContent.FLEX_START);

        tagBackgroundColor = getResources().getColor(R.color.tag_bg);

        mTagTextColor = getResources().getColor(R.color.white);

        mAdapter = new TagViewAdapter(mTagTextColor, tagBackgroundColor);

        addTextLayout();

        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.TagView);

        mTagTextColor = typedArray.getColor(R.styleable.TagView_tag_text_color, getResources().getColor(R.color.white));

        tagBackgroundColor = typedArray.getColor(R.styleable.TagView_tag_background_color, getResources().getColor(R.color.tag_bg));

        tagLimit = typedArray.getInt(R.styleable.TagView_tag_limit, 1);

        mCrossDrawable = typedArray.getDrawable(R.styleable.TagView_close_icon);

        mTagLimitMessage = typedArray.getString(R.styleable.TagView_limit_error_text);


        int separator = typedArray.getInt(R.styleable.TagView_tag_separator, 0);

        tagSeparator = convertSeparator(separator);

        typedArray.recycle();
    }

    private void addTextLayout() {
        recyclerView = new RecyclerView(getContext());

        textViewAdd = new TextView(getContext());

        textLayout = new TextView(getContext());
        textLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textLayout.setGravity(Gravity.CENTER_VERTICAL);
//        textViewAdd.setText("กรุณาเลือกวัถุดิบที่แพ้");

        this.addView(textLayout);
//        addRecyclerView();
    }

    public void addTagLimit(int limit) {
        if(limit == 0)
            limit = 1;
        tagLimit = limit;
    }

    private void addTagInView() {
        final TagModel model = mTagList.get(mTagList.size() - 1);

        final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View tagView = inflater.inflate(R.layout.tag_layout, null);

        TextView tagTextView = tagView.findViewById(R.id.text_view_tag);

        ImageView imageView = tagView.findViewById(R.id.image_view_cross);

        LinearLayout linearLayout = tagView.findViewById(R.id.tag_container);

        GradientDrawable drawable = (GradientDrawable) linearLayout.getBackground();

        drawable.setColor(tagBackgroundColor);

        tagTextView.setTextColor(mTagTextColor);

        if (mCrossBitmap != null) {
            imageView.setImageBitmap(mCrossBitmap);
        } else if (mCrossDrawable != null) {
            imageView.setImageDrawable(mCrossDrawable);
        }

        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                removeView(tagView);
                mTagList.remove(model);

                if (model.isFromList()) {
                    mAdapter.addItem(model.getTagText());
                }

                if (mTagItemListener != null) {
                    mTagItemListener.onGetRemovedItem(model);
                }

                if(mTagList.size() == 0) {
                    textLayout.setVisibility(VISIBLE);
                }

                invalidate();
            }
        });

        tagTextView.setText(model.getTagText());

        this.addView(tagView, mTagList.indexOf(model));

        if (mTagItemListener != null) {
            mTagItemListener.onGetAddedItem(model);
        }

        textLayout.setVisibility(GONE);

        invalidate();
    }

    public void addTag(String text, boolean isFromList) {
        if (text == null) {
            text = EMPTY_STRING;
        }

        TagModel model = new TagModel();
        model.setTagText(text);
        model.setFromList(isFromList);

        mTagList.add(model);

        addTagInView();
    }

    private void addRecyclerView() {
        recyclerView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        textViewAdd.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textViewAdd.setGravity(Gravity.CENTER);
        textViewAdd.setPadding(4, 0, 4, 0);
        textViewAdd.setBackground(getResources().getDrawable(R.drawable.drawable_tag));

        GradientDrawable drawable = (GradientDrawable) textViewAdd.getBackground();
        drawable.setColor(tagBackgroundColor);

        textViewAdd.setTextColor(mTagTextColor);

//        addTag(textViewAdd.getText().toString().trim(), false);

//        this.addView(textLayout);

//        textViewAdd.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mTagList.size() < tagLimit) {
//                    addTag(textViewAdd.getText().toString(), false);
//                    textViewAdd.setText("");
//                    textViewAdd.setVisibility(GONE);
//                    recyclerView.setVisibility(VISIBLE);
//                    editText.setText("");
//                } else {
//                    showTagLimitMessage();
//                }
//
//            }
//        });

//        this.addView(editText);
        if (mTagItemList != null) {
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());

            layoutManager.setJustifyContent(JustifyContent.FLEX_START);
            layoutManager.setFlexWrap(FlexWrap.WRAP);
            recyclerView.setLayoutManager(layoutManager);

            recyclerView.setAdapter(mAdapter);

            // set adapter click listener
            mAdapter.setTagClickListener(this);

            mAdapter.addItems(mTagItemList);

        }

        this.addView(recyclerView);

        this.addView(textViewAdd);
//        textViewAdd.setVisibility(GONE);


        invalidate();
    }

    private String convertSeparator(int separator) {
        String tagSeparator = "";
        switch (separator) {
            case 1:
                tagSeparator = TagSeparator.valueOf(TagSeparator.COMMA_SEPARATOR.name()).getValue();
                break;
            case 2:
                tagSeparator = TagSeparator.valueOf(TagSeparator.PLUS_SEPARATOR.name()).getValue();
                break;
            case 3:
                tagSeparator = TagSeparator.valueOf(TagSeparator.MINUS_SEPARATOR.name()).getValue();
                break;
            case 4:
                tagSeparator = TagSeparator.valueOf(TagSeparator.SPACE_SEPARATOR.name()).getValue();
                break;
            case 5:
                tagSeparator = TagSeparator.valueOf(TagSeparator.AT_SEPARATOR.name()).getValue();
                break;
            case 6:
                tagSeparator = TagSeparator.valueOf(TagSeparator.HASH_SEPARATOR.name()).getValue();
                break;
        }
        return tagSeparator;
    }

    @Override
    public void onGetSelectTag(int position, String tagText) {

    }

    public List<TagModel> getSelectedTags() {
        return mTagList;
    }

    public void setTagBackgroundColor(int color) {
        tagBackgroundColor = color;

        mAdapter.addTagBackgroundColor(tagBackgroundColor);
    }

    public void setTagBackgroundColor(String color) {
        tagBackgroundColor = Color.parseColor(color);

        mAdapter.addTagBackgroundColor(tagBackgroundColor);
    }

    public void setTagTextColor(int color) {
        mTagTextColor = color;

        mAdapter.addTagTextColor(mTagTextColor);
    }

    public void setTagTextColor(String color) {
        mTagTextColor = Color.parseColor(color);

        mAdapter.addTagTextColor(mTagTextColor);
    }

    public void setMaximumTagLimitMessage(String message) {
        if (TextUtils.isEmpty(message)) {
            mTagLimitMessage = DEFAULT_TAG_LIMIT_MESSAGE;
        } else {
            mTagLimitMessage = message;
        }
    }

    public void setCrossButton(Drawable crossDrawable) {
        mCrossDrawable = crossDrawable;
        mCrossBitmap = null;
    }

    public void setCrossButton(Bitmap crossBitmap) {
        mCrossDrawable = null;
        mCrossBitmap = crossBitmap;
    }

    public void initTagListener(TagItemListener listener) {
        mTagItemListener = listener;
    }

    public void setTagList(List<String> tagList) {
        if(tagList == null)
            tagList = new ArrayList<>();

        mTagItemList = tagList;

        addRecyclerView();
    }

    public void setTagList(String... tagList) {
        List<String> tempList = Arrays.asList(tagList);

        mTagItemList.addAll(tempList);

        addRecyclerView();
    }

    public void setText(String text) {
        textLayout.setText(text);
    }

    public void setDrawable(int left, int top, int right, int bottom) {
        textLayout.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
    }

}
