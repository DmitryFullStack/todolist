package com.arhiser.todolist.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.arhiser.todolist.model.Note;

@Database(entities = {Note.class}, version = 3, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();
}
