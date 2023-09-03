package com.ahmer.accounts.core.event

import com.ahmer.accounts.core.state.ResultState
import com.ahmer.accounts.database.model.UserModel

data class UserState(
    val getAllUsersList: ResultState<List<UserModel>> = ResultState.Loading,
)

