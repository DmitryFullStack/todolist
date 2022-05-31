package com.arhiser.todolist.screens.main;

import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.arhiser.todolist.App;
import com.arhiser.todolist.R;
import com.arhiser.todolist.model.Note;
import com.arhiser.todolist.screens.details.NoteDetailsActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Adapter extends ListAdapter<Note, Adapter.NoteViewHolder> {

    protected Adapter() {
        super(new MainActivity.NoteItemCallback());
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        int size = getItemCount();
        Date now = new Date(System.currentTimeMillis());
        for (int i = 0; i < size; i++) {
            if(getItem(i).daily && getItem(i).done){
                Date date = new Date(getItem(i).timestamp);
                if(now.getDay() != date.getDay()){
                    getItem(i).done = false;
                }
            }
        }
        holder.bind(getItem(position));
    }

    public void setItems(List<Note> notes) {
//        g.addAll(notes);
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView noteText;
        CheckBox completed;
        View delete;
        ImageView daily;

        Note note;

        boolean silentUpdate;

        public NoteViewHolder(@NonNull final View itemView) {
            super(itemView);

            noteText = itemView.findViewById(R.id.note_text);
            completed = itemView.findViewById(R.id.completed);
            delete = itemView.findViewById(R.id.delete);
            daily = itemView.findViewById(R.id.daily);

            itemView.setOnClickListener(view -> NoteDetailsActivity.start((Activity) itemView.getContext(), note));

            delete.setOnClickListener(view -> App.getInstance().getNoteDao().delete(note));

            completed.setOnCheckedChangeListener((compoundButton, checked) -> {
                if (!silentUpdate) {
                    note.done = checked;
                    note.timestamp = System.currentTimeMillis();
                    App.getInstance().getNoteDao().update(note);
                }
                updateStrokeOut();
            });

        }

        public void bind(Note note) {
            this.note = note;

            noteText.setText(note.text);
            updateStrokeOut();

            silentUpdate = true;
            completed.setChecked(note.done);
            silentUpdate = false;

            if(note.color != null){
                noteText.setTextColor(note.color);
            }
            if(note.daily){
                daily.setVisibility(View.VISIBLE);
            }
        }

        private void  updateStrokeOut() {
            if (note.done) {
                noteText.setPaintFlags(noteText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                noteText.setPaintFlags(noteText.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
    }
}
