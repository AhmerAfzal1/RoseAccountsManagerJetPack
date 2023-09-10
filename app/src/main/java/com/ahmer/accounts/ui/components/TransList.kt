package com.ahmer.accounts.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.core.AsyncData
import com.ahmer.accounts.core.GenericError
import com.ahmer.accounts.core.ResultState
import com.ahmer.accounts.database.model.TransModel
import com.ahmer.accounts.database.model.TransSumModel
import com.ahmer.accounts.event.TransEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TransList(
    padding: PaddingValues,
    transListState: ResultState<List<TransModel>>,
    transBalanceState: ResultState<TransSumModel>,
    onEvent: (TransEvent) -> Unit,
    reloadData: () -> Unit
) {
    var mRefreshing by remember { mutableStateOf(false) }
    val mRefreshScope = rememberCoroutineScope()

    fun refresh() = mRefreshScope.launch {
        mRefreshing = true
        reloadData()
        delay(500)
        mRefreshing = false
    }

    val mState = rememberPullRefreshState(mRefreshing, ::refresh)

    Box(modifier = Modifier.padding(padding), contentAlignment = Alignment.BottomCenter) {
        AsyncData(resultState = transListState, errorContent = {
            GenericError(onDismissAction = reloadData)
        }) { transList ->
            transList?.let {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(10.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
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

    PullRefreshIndicator(mRefreshing, mState)
}