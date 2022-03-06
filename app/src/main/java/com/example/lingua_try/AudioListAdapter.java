package com.example.lingua_try;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.Arrays;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.AudioViewHolder>{
    // adapter guides the way the information(data model) shown in the UI. -> must need a class called RecyclerView.Adapter that implements ViewHolder to contain all your views in the layout.

    private File[] allFiles;
    private OnItemListClickListener onItemListClickListener;
    private OnItemListClickListener a;
    private PracticePage p = new PracticePage();

    public AudioListAdapter(File[] allFiles, OnItemListClickListener onItemListClickListener) {
        this.allFiles = allFiles;
        this.onItemListClickListener = onItemListClickListener;
    }

    public class AudioViewHolder extends RecyclerView.ViewHolder{
        // Provide a reference to the type of views that you are using
        // Each individual element in the list is defined by a view holder object. When the view holder is created, it doesn't have any data associated with it.
        // After the view holder is created, the RecyclerView binds it to its data. You define the view holder by extending RecyclerView.ViewHolder


        // This class is then further overriden by onCreateViewHolder, so that to create a View ( now this class didnt render/inflate a view, but just define the content)

        private TextView listTitle;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);

            listTitle = itemView.findViewById(R.id.audio_list_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("play_log", "playing");
                    onItemListClickListener.onItemClick(allFiles[getAdapterPosition()], getAdapterPosition());  // callback method


            }
        });
    }
    }

    public interface OnItemListClickListener{                     // interface (class), define onItemListClick method, be implemented later
        void onItemClick(File file, int position);
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Create a new view, which defines the UI of the list item

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_audio_list, parent, false);
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        // Replace the contents of a view
        // Get element from your dataset at this position and replace the contents of the view with that element

        holder.listTitle.setText(allFiles[position].getName());
    }

    @Override
    public int getItemCount() {
        return allFiles.length;
    }

}
