package com.example.yinyang_taengkwa.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.yinyang_taengkwa.Adapter.SearchAdapter;
import com.example.yinyang_taengkwa.Animations.SlideAnimation;
import com.example.yinyang_taengkwa.R;
import com.example.yinyang_taengkwa.activities.DetailActivity;
import com.example.yinyang_taengkwa.api.RetrofitClient;
import com.example.yinyang_taengkwa.models.Menuresponse;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_search extends Fragment {

    RecyclerView recyclerView;
    EditText editText_search;
    ImageView imgView_search;
    SearchAdapter adapter;
    Menuresponse res;
    boolean statusView;
    TextView sort;
    Button button_yin, button_yang, confirmButton;
    CrystalRangeSeekbar rangeSeekbar;
    TextView tvMin, tvMax;


    boolean ck_yin  = false;
    boolean ck_yang = false;
    boolean ck_seek = false;
    boolean confirm = false;

    Double minYinYang = 0.0;
    Double maxYinYang = 0.0;

    public Fragment_search() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Recycler View
        recyclerView = view.findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        //Recycler View

        editText_search = view.findViewById(R.id.editText_search);
        imgView_search = view.findViewById(R.id.imgView_search);
        sort = view.findViewById(R.id.Textview_sort);
        button_yin = view.findViewById(R.id.button_yin);
        button_yang = view.findViewById(R.id.button_yang);
        confirmButton = view.findViewById(R.id.confirm_button);

        rangeSeekbar = view.findViewById(R.id.rangeSeekbar1);

        tvMin = view.findViewById(R.id.textMin1);
        tvMax = view.findViewById(R.id.textMax1);


        initSearch(view);
        setHasOptionsMenu(true);

        // view we want to animate
        final LinearLayout view_sort = view.findViewById(R.id.View_sort);

        // set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {

                tvMin.setText(String.format("%.2f", minValue));
                tvMax.setText(String.format("%.2f", maxValue));

                ck_seek = true;

                minYinYang = Double.parseDouble(String.format("%.2f", minValue));
                maxYinYang = Double.parseDouble(String.format("%.2f", maxValue));
            }
        });

// set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                confirm = true;
                if(ck_yin) {
                    filterByRange(res.getMenu(), "หยิน", minYinYang, maxYinYang);
                } else {
                    filterByRange(res.getMenu(), "หยาง", minYinYang, maxYinYang);
                }

                Animation animation = new SlideAnimation(view_sort, 900, 0);

                animation.setInterpolator(new AccelerateInterpolator());
                animation.setDuration(300);
                view_sort.setAnimation(animation);
                view_sort.startAnimation(animation);
            }
        });

        button_yin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //filter_yin("หยิน");
                if(ck_yang)
                    ck_yang = false;
                ck_yin = true;
            }
        });


        button_yang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //filter_yang("หยาง");
                if(ck_yin)
                    ck_yin = false;
                ck_yang = true;
            }
        });

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (statusView == false) {


                    Animation animation = new SlideAnimation(view_sort, 0, 1000);

                    // this interpolator only speeds up as it keeps going
                    animation.setInterpolator(new AccelerateInterpolator());
                    animation.setDuration(300);
                    view_sort.setAnimation(animation);
                    view_sort.startAnimation(animation);
                    statusView = true;
                } else {

                    Animation animation = new SlideAnimation(view_sort, 900, 0);

                    // this interpolator only speeds up as it keeps going
                    animation.setInterpolator(new AccelerateInterpolator());
                    animation.setDuration(300);
                    view_sort.setAnimation(animation);
                    view_sort.startAnimation(animation);
                    statusView = false;
                }

            }

        });


        imgView_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filter(editText_search.getText().toString());

            }
        });

        editText_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == event.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText_search.getWindowToken(), 0);
                }

                filter(editText_search.getText().toString());
                return false;
            }
        });


        editText_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void initSearch(final View view) {
        Call<Menuresponse> call = RetrofitClient.getInstance().getApi().getmenu();
        call.enqueue(new Callback<Menuresponse>() {
            @Override
            public void onResponse(Call<Menuresponse> call, Response<Menuresponse> response) {
                res = response.body();

                if (res.isStatus() == true) {
                    if (res.getMenu() != null) {

                        adapter = new SearchAdapter(getContext(), res.getMenu());
                        recyclerView.setAdapter(adapter);

                        adapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(com.example.yinyang_taengkwa.models.Menu item) {
                                Intent intent = new Intent(getContext(), DetailActivity.class);
                                intent.putExtra("DETAIL", item);
                                startActivity(intent);
                            }
                        });

                    }
                }
            }

            @Override
            public void onFailure(Call<Menuresponse> call, Throwable t) {

            }
        });
    }


    public void filter(String data) {
        ArrayList<com.example.yinyang_taengkwa.models.Menu> arr = new ArrayList<>();

        for (com.example.yinyang_taengkwa.models.Menu menu : res.getMenu()) {
            if (menu.getName_menu().contains(data)) {
                arr.add(menu);
            }
        }

        if (arr.size() > 0) {
            adapter.filterList(arr);
        } else {
            adapter.filterList((ArrayList) null);
            Toast.makeText(getContext(), "ไม่พบรายการ", Toast.LENGTH_SHORT);
        }

    }


    public void filter_yin(String data) {
        ArrayList<com.example.yinyang_taengkwa.models.Menu> arr = new ArrayList<>();

        for (com.example.yinyang_taengkwa.models.Menu menu : res.getMenu()) {
            if (menu.getCategory_menu().contains(data)) {
                arr.add(menu);
            }
        }

        if (arr.size() > 0) {
            adapter.filterList(arr);
        } else {
            adapter.filterList((ArrayList) null);
            Toast.makeText(getContext(), "ไม่พบรายการ", Toast.LENGTH_SHORT);
        }

    }


    public void filter_yang(String data) {
        ArrayList<com.example.yinyang_taengkwa.models.Menu> arr = new ArrayList<>();

        for (com.example.yinyang_taengkwa.models.Menu menu : res.getMenu()) {
            if (menu.getCategory_menu().contains(data)) {
                arr.add(menu);
            }
        }

        if (arr.size() > 0) {
            adapter.filterList(arr);
        } else {
            adapter.filterList((ArrayList) null);
            Toast.makeText(getContext(), "ไม่พบรายการ", Toast.LENGTH_SHORT);
        }

    }


    public void filter_yin_seek(String data, double min, double max) {
        ArrayList<com.example.yinyang_taengkwa.models.Menu> arr = new ArrayList<>();

        for (com.example.yinyang_taengkwa.models.Menu menu : res.getMenu()) {
            if (menu.getCategory_menu().contains(data) && Double.parseDouble(menu.getNum_yhin()) >= min && Double.parseDouble(menu.getNum_yhang()) <= max) {
                arr.add(menu);
            }
        }

        if (arr.size() > 0) {
            adapter.filterList(arr);
        } else {
            adapter.filterList((ArrayList) null);
            Toast.makeText(getContext(), "ไม่พบรายการ", Toast.LENGTH_SHORT);
        }

    }

    public void filter_yang_seek(String data, double min, double max) {
        ArrayList<com.example.yinyang_taengkwa.models.Menu> arr = new ArrayList<>();

        for (com.example.yinyang_taengkwa.models.Menu menu : res.getMenu()) {
            if (menu.getCategory_menu().contains(data) && Double.parseDouble(menu.getNum_yhin()) >= min && Double.parseDouble(menu.getNum_yhang()) <= max) {
                arr.add(menu);
            }
        }

        if (arr.size() > 0) {
            adapter.filterList(arr);
        } else {
            adapter.filterList((ArrayList) null);
            Toast.makeText(getContext(), "ไม่พบรายการ", Toast.LENGTH_SHORT);
        }

    }


    private void filterByRange(List<com.example.yinyang_taengkwa.models.Menu> menuList, String type, double minRange, double maxRange) {
        ArrayList<com.example.yinyang_taengkwa.models.Menu> arr = new ArrayList<>();

        for (com.example.yinyang_taengkwa.models.Menu menu : menuList) {
            if(menu.getCategory_menu().equals(type) && getNumByType(type, menu) >= minRange && getNumByType(type, menu) <= maxRange) {
                arr.add(menu);
            }
        }

        Log.e("Min Yin", String.valueOf(minRange));
        Log.e("Max Yin", String.valueOf(maxRange));

        if(arr.size() > 0) {
            adapter.filterList(arr);
        } else {
            adapter.filterList(arr);
        }
    }

    private double getNumByType(String type, com.example.yinyang_taengkwa.models.Menu menu) {
        if(type.equals("หยิน")) {
            return Double.parseDouble(menu.getNum_yhin());
        } else {
            return Double.parseDouble(menu.getNum_yhang());
        }
    }


}
