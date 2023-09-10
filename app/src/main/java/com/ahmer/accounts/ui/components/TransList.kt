package com.ahmer.accounts.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.core.AsyncData
import com.ahmer.accounts.core.GenericError
import com.ahmer.accounts.core.ResultState
import com.ahmer.accounts.database.model.TransModel
import com.ahmer.accounts.database.model.TransSumModel
import com.ahmer.accounts.event.TransEvent

@Composable
fun TransList(
    padding: PaddingValues,
    transListState: ResultState<List<TransModel>>,
    transBalanceState: ResultState<TransSumModel>,
    onEvent: (TransEvent) -> Unit,
    reloadData: () -> Unit
) {
    Box(modifier = Modifier.padding(padding), contentAlignment = Alignment.BottomCenter) {
        AsyncData(resultState = transListState, errorContent = {
            GenericError(onDismissAction = reloadData)
        }) { transList ->
            transList?.let {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 10.dp, end = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    items(
                        items = transList,
                        key = { listTrans -> listTrans.id }) { transaction ->
                        TransItem(transModel = transaction, onEvent = onEvent)
                    }
                }
            }
        }
        AsyncData(resultState = transBalanceState) { balanceSum ->
            balanceSum?.let { transSum ->
                LazyColumn {
                    item { TransTotal(transSumModel = transSum) }
                }
            }
        }
    }
}