package com.ahmer.accounts.event

import com.ahmer.accounts.database.model.PersonsEntity
import com.ahmer.accounts.database.model.TransEntity

sealed class TransEvent {
    data class OnEditClick(val transEntity: TransEntity) : TransEvent()
    data class OnPersonEditClick(val personsEntity: PersonsEntity) : TransEvent()
    data class OnSearchTextChange(val searchQuery: String) : TransEvent()
    data object OnAddClick : TransEvent()
    data object OnUndoDeleteClick : TransEvent()
}