package com.example.app1.Exercise;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app1.R;

import java.util.ArrayList;

public class Exercise_Adapter extends RecyclerView.Adapter<Exercise_Adapter.ExerciseViewHolder>{

    private final ArrayList<Exercise_Item> mExerciseList;


    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public ImageView mImageView2;
        public TextView mText1;
        public TextView mText2;
        ConstraintLayout mainContrainLayout;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageView);
            mImageView2 = itemView.findViewById(R.id.imageView2);
            mText1 = itemView.findViewById(R.id.TextView1);
            mText2 = itemView.findViewById(R.id.TextView2);
            mainContrainLayout = itemView.findViewById(R.id.exercise_item);

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
        final Exercise_Item currentItem = mExerciseList.get(position);

        holder.mImageView.setImageResource(currentItem.getmImageResource1());
        holder.mImageView2.setImageResource(currentItem.getmImageResource2());
        holder.mText1.setText(currentItem.getmText1());
        holder.mText2.setText(currentItem.getmText2());

        holder.mainContrainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CardView.class);
                intent.putExtra("image", currentItem.getmImageResource1());
                intent.putExtra("image2", currentItem.getmImageResource2());
                intent.putExtra("text1", currentItem.getmText1());
                intent.putExtra("text2", currentItem.getmText2());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mExerciseList.size();
    }
}
