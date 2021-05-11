package com.example.myfirebaselogin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseView> {

    ArrayList<Exercise> exerciseList = new ArrayList<>();

    public ExerciseAdapter(ArrayList<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }

    @NonNull
    @Override
    public ExerciseView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_exercise,parent,false);

        return new ExerciseView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseView holder, int position) {

        Exercise exercise = exerciseList.get(position);
        String x = (String) String.valueOf(exercise.getRepetitions());
        holder.textRepetitions.setText(x);
        holder.textName.setText(exercise.getName());


    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public class ExerciseView extends RecyclerView.ViewHolder{

        TextView textName,textRepetitions;
        public ExerciseView(@NonNull View itemView) {
            super(itemView);

            textName = (TextView)itemView.findViewById(R.id.text_name);
            textRepetitions = (TextView)itemView.findViewById(R.id.text_repetitions);

        }
    }

}
