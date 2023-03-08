package com.h_alqatawi.noteapp.feature_note.presntation.note

import com.h_alqatawi.noteapp.feature_note.domain.model.Note
import com.h_alqatawi.noteapp.feature_note.domain.util.NoteOrder
import com.h_alqatawi.noteapp.feature_note.domain.util.OrderType

data class NotesState(
    val notes : List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible :Boolean = false

)
