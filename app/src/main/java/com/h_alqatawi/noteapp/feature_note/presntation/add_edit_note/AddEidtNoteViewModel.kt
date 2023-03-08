package com.h_alqatawi.noteapp.feature_note.presntation.add_edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h_alqatawi.noteapp.feature_note.domain.model.InvalidNoteException
import com.h_alqatawi.noteapp.feature_note.domain.model.Note
import com.h_alqatawi.noteapp.feature_note.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _noteTittle = mutableStateOf(NoteTextFieldState(
        hilt = "Enter Tittle"
    ))
    val noteTittle: State<NoteTextFieldState> = _noteTittle

    private val _noteContent = mutableStateOf(NoteTextFieldState(
        hilt = "Enter Some Content"
    ))
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _noteColor = mutableStateOf(Note.noteColor.random().toArgb())
    val noteColor: State<Int> = _noteColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null


    init {
        savedStateHandle.get<Int>("noteId")?.let {noteId ->
            if (noteId != -1){
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteTittle.value = noteTittle.value.copy(
                            text = note.titLe,
                            isHiltVisible = false
                        )
                        _noteContent.value = noteContent.value.copy(
                            text = note.content,
                            isHiltVisible = false
                        )
                        _noteColor.value = note.color
                    }
                }
            }

        }
    }



    fun onEvent(event: AddEditNoteEvent){
        when(event){
            is AddEditNoteEvent.EnteredTittle ->{
                _noteTittle.value = noteTittle.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeTittleFocus ->{
                _noteTittle.value = noteTittle.value.copy(
                    isHiltVisible = !event.focusState.isFocused &&
                            noteTittle.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.EnteredContent ->{
                _noteContent.value = noteContent.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeContentFocus ->{
                _noteContent.value = noteContent.value.copy(
                    isHiltVisible = !event.focusState.isFocused &&
                            noteContent.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.ChangeColor ->{
                _noteColor.value = event.color
            }
            is AddEditNoteEvent.SaveNote ->{
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                titLe = noteTittle.value.text,
                                content = noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                id = currentNoteId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    }catch (e: InvalidNoteException){
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message?: "couldn't save note"
                            )
                        )
                    }
                }
            }
        }
    }


    sealed class UiEvent{
        data class ShowSnackbar(val message:String): UiEvent()
        object SaveNote: UiEvent()
    }

}