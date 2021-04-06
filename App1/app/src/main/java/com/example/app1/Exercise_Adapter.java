package com.example.app1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Exercise_Adapter extends RecyclerView.Adapter<Exercise_Adapter.ExerciseViewHolder> {

    private final ArrayList<Exercise_Item> mExerciseList;

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mText1;
        public TextView mText2;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageView);
            mText1 = itemView.findViewById(R.id.TextView1);
            mText2 = itemView.findViewById(R.id.TextView2);

        }
    }

    public Exercise_Adapter(ArrayList<Exercise_Item> exercise_list) {
        mExerciseList = exercise_list;

    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item, parent, false);
        ExerciseViewHolder evh = new ExerciseViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise_Item currentItem = mExerciseList.get(position);

        holder.mImageView.setImageResource(currentItem.getmImageResource());
        holder.mText1.setText(currentItem.getmText1());
        holder.mText2.setText(currentItem.getmText2());

    }

    @Override
    public int getItemCount() {
        return mExerciseList.size();
    }
}
