package com.h_alqatawi.noteapp.di

import android.app.Application
import androidx.room.Room
import com.h_alqatawi.noteapp.feature_note.data.data_source.NoteDatabase
import com.h_alqatawi.noteapp.feature_note.data.repository.NoteRepositoryImpI
import com.h_alqatawi.noteapp.feature_note.domain.repository.NoteRepository
import com.h_alqatawi.noteapp.feature_note.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app : Application): NoteDatabase{
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db : NoteDatabase): NoteRepository{
        return NoteRepositoryImpI(db.noteDao)
    }

    @Provides
    @Singleton
    fun providerNoteUseCase(repository: NoteRepository):NoteUseCases{
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository),
            getNote = GetNote(repository)
        )
    }

}