package com.ahmer.accounts.state

import com.ahmer.accounts.database.model.PersonsEntity

data class PersonState(
    val getAllPersonsList: List<PersonsEntity> = emptyList()
)