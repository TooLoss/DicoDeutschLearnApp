package com.example.projectdicodeutsch.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.projectdicodeutsch.R;
import com.example.projectdicodeutsch.model.WordModel;
import com.example.projectdicodeutsch.translate_search;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {

    private List<WordModel> wordsList;
    private Activity activity;
    private List<String> inputWordListFrench;
    private List<String> inputWordListGerman;

    public WordAdapter(Activity activity, List<String> WordListFrench, List<String> WordListGerman) {
        this.activity = activity;
        this.inputWordListFrench = WordListFrench;
        this.inputWordListGerman = WordListGerman;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_word_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        holder.upWord.setText(inputWordListFrench.get(position));
        holder.downWord.setText(inputWordListGerman.get(position));
    }

    public int getItemCount(){
        return inputWordListFrench.size();
    }

    public void setWordsList(List<String> frenchWord, List<String> germanWord) {
        this.inputWordListFrench = frenchWord;
        this.inputWordListGerman = germanWord;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView upWord;
        TextView downWord;

        ViewHolder(View view) {
            super(view);
            upWord = view.findViewById(R.id.upWord);
            downWord = view.findViewById(R.id.downWord);
        }
    }
}
