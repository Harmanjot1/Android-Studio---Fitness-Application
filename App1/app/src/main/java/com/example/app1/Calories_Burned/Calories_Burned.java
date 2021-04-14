package com.example.app1.Calories_Burned;

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

public class Calories_Burned extends Fragment {

    ListView Calories_Burned_ListView;

    Button btn_Add, btn_back,btn_edit,btn_done;

    Calories_Burned_Adapter adapter;
    Calories_Burned_List calories_burned_list;

    TextView totalBurned,header;

    DataBaseHelper dataBaseHelper = null;

    private boolean removeCalories = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        container.removeAllViews();
        final View layout = inflater.inflate(R.layout.fragment_calories__burned, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        // Getting button listeners
        btn_Add = (Button) layout.findViewById(R.id.Calories_burned_Add);
        btn_back = (Button) layout.findViewById(R.id.Calories_Burned_btnBack);
        btn_edit = (Button) layout.findViewById(R.id.calories_burned_editbtn);
        btn_done = (Button) layout.findViewById(R.id.calories_burned_donebtn);

        totalBurned = (TextView) layout.findViewById(R.id.calories_burned_totalcount_Text);
        header = (TextView) layout.findViewById(R.id.calories_burned_Header);
        // Getting ListView Listener
        Calories_Burned_ListView = (ListView) layout.findViewById(R.id.calories_burned_ListView);

        calories_burned_list = ((Activity)getActivity().getApplication()).getCalories_burned_list();

        adapter = new Calories_Burned_Adapter(getActivity(),calories_burned_list);

        // getting database
        dataBaseHelper = new DataBaseHelper(getContext());

        loadCalories();

        btnMetod();


        return layout;
    }
    public void loadCalories(){
        if (dataBaseHelper.getCalories_Burned() != null){
            calories_burned_list.getMyCalories_Burned_List().clear();
            calories_burned_list.getMyCalories_Burned_List().addAll(dataBaseHelper.getCalories_Burned());

            totalBurned.setText("Total Burned: "+calories_burned_list.getTotal() +"");
            showAdapter();
            adapter.notifyDataSetChanged();
        }
    }

    public void btnMetod(){
        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                NewCaloriesBurned_Form newCaloriesBurned_form = new NewCaloriesBurned_Form();
                fragmentManager.beginTransaction().replace(R.id.fragment,newCaloriesBurned_form).addToBackStack(null).commit();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Dashboard_frag dashboard_frag = new Dashboard_frag();
                fragmentManager.beginTransaction().replace(R.id.fragment,dashboard_frag).addToBackStack(null).commit();
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCalories = true;
                btn_done.setVisibility(View.VISIBLE);
                header.setText("Tap on activity to Remove");
                removeCalories();
            }
        });
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calories_Burned_ListView.setOnItemClickListener(null);
                btn_done.setVisibility(View.GONE);
                header.setText("Calories Burned");
            }
        });
    }

    public void showAdapter() {
        adapter = new Calories_Burned_Adapter(getActivity(),calories_burned_list);
        Calories_Burned_ListView.setAdapter(adapter);
    }

    public void removeCalories(){
        if (removeCalories == true){
            Calories_Burned_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Calories_Burned_Object clickeditem = (Calories_Burned_Object) parent.getItemAtPosition(position);

                    dataBaseHelper.deleteCalories_Burned(clickeditem);

                    loadCalories();
                    showAdapter();
                    totalBurned.setText("Total Burned: "+calories_burned_list.getTotal() +"");
                }

            });
        }
    }

}