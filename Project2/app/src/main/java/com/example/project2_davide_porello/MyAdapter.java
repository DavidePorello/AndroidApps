package com.example.project2_davide_porello;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private final ArrayList<Integer> images;
    private final ArrayList<String> texts;
    private final RVClickListener listener;
    private final boolean grid; //layout: true for grid and false for list

    public MyAdapter(ArrayList<String> txt, ArrayList<Integer> img, RVClickListener lis, boolean layout) {

        images = img;
        texts = txt;
        listener = lis;
        grid = layout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        //get inflater and inflate XML layout file
        LayoutInflater inflater = LayoutInflater.from(context);
        //based on the layout choose the grid or list item
        View itemView = inflater.inflate(grid ? R.layout.grid_item : R.layout.list_item, parent, false);
        //create ViewHolder passing the view that it will wrap and the listener on the view
        return new ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //populate the item at the input position
        holder.name.setText(texts.get(position));
        holder.image.setImageResource(images.get(position));
    }

    @Override
    public int getItemCount() {

        if(images.size() == texts.size())
            return images.size();
        return -1;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

        public TextView name; //name of the animal
        public ImageView image; //image of the animal
        private final RVClickListener listener;

        public ViewHolder(@NonNull View itemView,  RVClickListener lis) {

            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textView);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            this.listener = lis;

            itemView.setOnCreateContextMenuListener(this); //set long click listener (context menu)
            itemView.setOnClickListener(this); //set short click listener
        }

        @Override
        public void onClick(View v) {

            //use the listener to launch the web page when an item is clicked
            listener.onClick(true, getAdapterPosition());
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            //inflate menu from xml
            MenuInflater inflater = new MenuInflater(v.getContext());
            inflater.inflate(R.menu.context_menu, menu);
            //set clicks on the context menu items
            menu.getItem(0).setOnMenuItemClickListener((item) -> {
                //use the listener to launch the web page when the first item in the context menu is clicked
                listener.onClick(true, getAdapterPosition());
                return true;
            });
            menu.getItem(1).setOnMenuItemClickListener((item) -> {
                //use the listener to display the full image when the second item in the context menu is clicked
                listener.onClick(false, getAdapterPosition());
                return true;
            });
        }
    }
}