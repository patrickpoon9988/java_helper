package com.example.lingua_try;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class MainExerciseListAdaptor extends RecyclerView.Adapter<MainExerciseListAdaptor.MainExerciseViewHolder> {

    @NonNull
    @Override
    public MainExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_exercise_list, parent, false);
        return new MainExerciseViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MainExerciseViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MainExerciseViewHolder extends RecyclerView.ViewHolder {

        private TextView listTitle;

        public MainExerciseViewHolder(@NonNull View itemView) {
            super(itemView);

            listTitle = itemView.findViewById(R.id.main_exercise_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.action_selfLearningMain_to_exerciseFragment);
                }
            });
        }
    }
}
