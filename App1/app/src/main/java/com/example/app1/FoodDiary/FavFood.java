package com.example.app1.FoodDiary;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.app1.Database.DataBaseHelper;
import com.example.app1.Activity;
import com.example.app1.R;

import java.util.ArrayList;
import java.util.List;

public class FavFood extends Fragment {

    private static final String FAV_FOOD_DIARY = "" ;
    Button fav_edit, fav_back, fav_addFood, fav_Header, fav_Done;
    ListView favListView;

    FoodAdapter adapter;
    Food_List food_list;

    DataBaseHelper dataBaseHelper = null;
    private boolean removeFoodActive = false;
    private  List<Food> favlist = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        container.removeAllViews();
        final View layout = inflater.inflate(R.layout.fragment_fav_food, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        fav_edit = (Button) layout.findViewById(R.id.fav_food_edit_food_button);
        fav_back = (Button) layout.findViewById(R.id.fav_food_back_btn);
        fav_addFood = (Button) layout.findViewById(R.id.fav_food_btn_add);
        fav_Header = (Button) layout.findViewById(R.id.header);
        fav_Done = (Button) layout.findViewById(R.id.fav_food_edit_cancel_button);

        favListView = (ListView) layout.findViewById(R.id.fav_food_listView);

        food_list = ((Activity) this.getActivity().getApplication()).getFood_list();
        adapter = new FoodAdapter((android.app.Activity) getContext(), food_list);

        dataBaseHelper = new DataBaseHelper(getContext());

        favListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Food clickedFood = (Food) parent.getItemAtPosition(position);
                favlist.add(clickedFood);

                fav_addFood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dataBaseHelper.addFood(clickedFood);
                        FragmentManager fragmentManager = getFragmentManager();
                        DietPlan_frag dietPlan_frag = new DietPlan_frag();
                        fragmentManager.beginTransaction().replace(R.id.fragment,dietPlan_frag).commit();

                    }
                });
            }
        });


        loadFood();
        btnMethod();
        return layout;
    }
    public void loadFood() {

        if (dataBaseHelper.getSavedFavFood() != null) {

            food_list.getMyFoodList().clear();
            food_list.getMyFoodList().addAll(dataBaseHelper.getSavedFavFood());

            showAdapter();
            adapter.notifyDataSetChanged();
        }
    }
    public void showAdapter() {
        adapter = new FoodAdapter((android.app.Activity) getContext(), food_list);
        favListView.setAdapter(adapter);
    }

    public void btnMethod(){
        fav_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFoodActive = true;
                fav_Header.setText("Tap on food to remove");
                removeFood();
                fav_Done.setVisibility(View.VISIBLE);
            }
        });

        fav_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                DietPlan_frag dietPlan_frag = new DietPlan_frag();
                fragmentManager.beginTransaction().replace(R.id.fragment,dietPlan_frag).commit();

            }
        });
    }

    public void removeFood() {

        if (removeFoodActive == true) {
            favListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Food clickedFood = (Food) parent.getItemAtPosition(position);
                    dataBaseHelper.fav_deleteFood(clickedFood);

                    //////////// dataBaseHelper.fav_deleteFood(clickedFood); Change fav_deletefood to add to take food into normal list
                    loadFood();
                    showAdapter();

                }

            });
        }
    }



}