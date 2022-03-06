package com.example.lingua_try;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupWindow;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelfLearningMain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelfLearningMain extends Fragment {

    public SelfLearningMain() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_self_learning_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton menu = view.findViewById(R.id.menu_but);

        RecyclerView mainExerciseList = view.findViewById(R.id.exercise_main_recyclerview);


        MainExerciseListAdaptor mainExerciseListAdaptor = new MainExerciseListAdaptor();

        mainExerciseList.setHasFixedSize(true);
        mainExerciseList.setLayoutManager(new LinearLayoutManager(getContext()));
        mainExerciseList.setAdapter(mainExerciseListAdaptor);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View popupview = LayoutInflater.from(getContext()).inflate(R.layout.fragment_pop_up_window_menu, null);

                final PopupWindow popupWindow = new PopupWindow(popupview, 500, WindowManager.LayoutParams.MATCH_PARENT);

                ColorDrawable drawable = new ColorDrawable(0xb0080000);

                popupWindow.setBackgroundDrawable(drawable);
                popupWindow.setOutsideTouchable(true);

                popupWindow.showAsDropDown(popupview, 0, 0);
            }
        });
    }

}

