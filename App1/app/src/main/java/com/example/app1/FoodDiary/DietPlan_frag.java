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
import android.widget.TextView;

import com.example.app1.Dashboard_frag;
import com.example.app1.Database.DataBaseHelper;
import com.example.app1.Activity;
import com.example.app1.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DietPlan_frag extends Fragment {

    Button breakfast_addFood, edit_food_button, back_btn;
    TextView header_text_button;
    Button edit_cancel_btn, add_from_fav_btn;
    TextView calories_Set_Text;
    TextView protein_Set_Text;



    ListView breakfast_FoodList;

    private String todaysDate;
    private SimpleDateFormat dateFormat;

    private boolean removeFoodActive = false;



    FoodAdapter adapter;
    Food_List food_list;


    DataBaseHelper dataBaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        container.removeAllViews();
        final View layout = inflater.inflate(R.layout.fragment_diet_plan_frag, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        breakfast_addFood = layout.findViewById(R.id.breakfast_btn_add);
        edit_food_button = layout.findViewById(R.id.edit_food_button);
        header_text_button = layout.findViewById(R.id.foodText);
        back_btn = layout.findViewById(R.id.food_diary_back_btn);
        add_from_fav_btn = layout.findViewById(R.id.add_from_fav_btn);


        //
        breakfast_FoodList = layout.findViewById(R.id.breakfast_listView);

        //
        calories_Set_Text = layout.findViewById(R.id.calories_set_text);
        protein_Set_Text = layout.findViewById(R.id.protein_set_text);
        edit_cancel_btn = layout.findViewById(R.id.edit_cancel_button);

        // --------------------------------------------------

        food_list = ((Activity) getActivity().getApplication()).getFood_list();
        adapter = new FoodAdapter(getActivity(), food_list);

        dataBaseHelper = new DataBaseHelper(getContext());

        loadFood();

        btnMethod();



        return layout;
    }
    public void btnMethod() {
        breakfast_addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                NewFoodForm_frag newFoodForm_frag = new NewFoodForm_frag();
                fragmentManager.beginTransaction().replace(R.id.fragment,newFoodForm_frag).commit();
            }
        });

        edit_food_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFoodActive = true;
                header_text_button.setText("Tap on food to remove");
                removeFood();
                edit_cancel_btn.setVisibility(View.VISIBLE);


            }
        });
        edit_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                header_text_button.setText("Food Diary");
                breakfast_FoodList.setOnItemClickListener(null);
                edit_cancel_btn.setVisibility(View.GONE);
            }
        });
        add_from_fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FavFood favFood = new FavFood();
                fragmentManager.beginTransaction().replace(R.id.fragment,favFood).commit();

            }
        });


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Dashboard_frag dashboard_frag = new Dashboard_frag();
                fragmentManager.beginTransaction().replace(R.id.fragment,dashboard_frag).commit();
            }
        });
    }
    public void showAdapter() {
        adapter = new FoodAdapter(getActivity(), food_list);
        breakfast_FoodList.setAdapter(adapter);
    }

    public void loadFood() {

        if (dataBaseHelper.getSavedFood() != null) {

            food_list.getMyFoodList().clear();
            food_list.getMyFoodList().addAll(dataBaseHelper.getSavedFood());

            showAdapter();
            resetNewDay();
            getTotal();
            adapter.notifyDataSetChanged();
        }
    }

    public void getTotal() {
        getDate();
        calories_Set_Text.setText(food_list.getTotalcal() + "");
        protein_Set_Text.setText(food_list.getTotalprotein() + "");

    }

    public void getDate() {
        Calendar calendar;

        calendar = Calendar.getInstance();

        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        todaysDate = dateFormat.format(calendar.getTime());

    }


    public void removeFood() {

        if (removeFoodActive == true) {
            breakfast_FoodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Food clickedFood = (Food) parent.getItemAtPosition(position);

                    dataBaseHelper.deleteFood(clickedFood);

                    loadFood();
                    showAdapter();
                    getTotal();
                }

            });
        }
    }

    public void resetNewDay() {

        getDate();

        if (food_list.getDateAdded().equals(todaysDate)){

        }else {
            food_list.getMyFoodList().clear();
            dataBaseHelper.deleteAllFood();
        }
    }




}