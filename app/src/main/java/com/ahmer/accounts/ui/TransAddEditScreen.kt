package com.ahmer.accounts.ui

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmer.accounts.core.AsyncData
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.state.AppBarState
import com.ahmer.accounts.ui.components.TransAddEditTextFields
import com.ahmer.accounts.utils.BackIcon
import com.ahmer.accounts.utils.HelperUtils
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TransAddEditScreen(onPopBackStack: () -> Unit, appBarState: (AppBarState) -> Unit) {
    val mContext: Context = LocalContext.current
    val mViewModel: TransAddEditViewModel = hiltViewModel()
    val mState by mViewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true) {
        mViewModel.eventFlow.collectLatest { event ->
            when (event) {
                UiEvent.SaveSuccess -> onPopBackStack()
                is UiEvent.ShowToast -> HelperUtils.toastLong(mContext, event.message)
                else -> Unit
            }
        }
    }

    LaunchedEffect(key1 = true) {
        appBarState(
            AppBarState(
                searchActions = {
                    Text(
                        text = mViewModel.titleBar,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {},
                floatingAction = {},
                isMenuNavigationIcon = false,
                newNavigationIcon = { IconButton(onClick = { onPopBackStack() }) { BackIcon() } },
                isSnackBarRequired = true,
                newSnackBarHost = {}
            )
        )
    }

    Box {
        AsyncData(resultState = mState.getTransDetails) {
            mViewModel.currentTransaction?.let { transaction ->
                TransAddEditTextFields(
                    transEntity = transaction,
                    onEvent = mViewModel::onEvent,
                    titleButton = mViewModel.titleButton
                )
            }
        }
    }
}