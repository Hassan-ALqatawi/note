package com.h_alqatawi.noteapp.feature_note.domain.use_case

import com.h_alqatawi.noteapp.feature_note.domain.model.InvalidNoteException
import com.h_alqatawi.noteapp.feature_note.domain.model.Note
import com.h_alqatawi.noteapp.feature_note.domain.repository.NoteRepository

class AddNote(
    private val repository: NoteRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note){
        if (note.titLe.isBlank()){
            throw InvalidNoteException("The title of the note can't be empty !.")
        }
        if (note.content.isBlank()){
            throw InvalidNoteException("The content of the note can't be empty !.")
        }
        repository.insertNote(note)
    }
}