package com.ahmer.accounts.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.R
import com.ahmer.accounts.database.model.TransSumModel
import com.ahmer.accounts.utils.HelperUtils

@Composable
fun RowScope.TableCell(text: String, weight: Float, isBold: Boolean = false) {
    if (!isBold) {
        Text(
            modifier = Modifier
                .weight(weight)
                .padding(2.dp),
            text = text,
            style = MaterialTheme.typography.titleSmall
        )
    } else {
        Text(
            modifier = Modifier
                .weight(weight)
                .padding(3.dp),
            text = text,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun NavShape(transSumModel: TransSumModel) {
    val mContentPadding = 5.dp
    val mCornerDp = 100.dp
    val mFirstRowWeight = 1.5f
    val mSecondRowWeight = 2f
    val mTotalCredit: Double = transSumModel.creditSum?.toDouble() ?: 0.0
    val mTotalDebit: Double = transSumModel.debitSum?.toDouble() ?: 0.0
    val mTotalBalance = HelperUtils.getRoundedValue((mTotalCredit.minus(mTotalDebit)))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .wrapContentHeight(Alignment.CenterVertically)
    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = mCornerDp, bottomEnd = mCornerDp))
                .background(MaterialTheme.colorScheme.primary)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(vertical = mContentPadding)
            ) {
                item {
                    Image(
                        modifier = Modifier
                            .padding(top = mContentPadding)
                            .size(size = 48.dp),
                        painter = painterResource(id = R.drawable.ic_logo),
                        contentDescription = stringResource(R.string.content_description_app_logo)
                    )
                    Text(
                        modifier = Modifier.padding(top = mContentPadding),
                        text = stringResource(id = R.string.app_name),
                        fontWeight = FontWeight.W900,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                    )
                    ElevatedCard(
                        modifier = Modifier
                            .padding(
                                top = mContentPadding,
                                start = mContentPadding,
                                end = mContentPadding
                            )
                            .size(width = 200.dp, height = 85.dp),
                        shape = RoundedCornerShape(10.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(Modifier.padding(start = mContentPadding, end = mContentPadding)) {
                                TableCell(
                                    stringResource(id = R.string.label_nav_credit), mFirstRowWeight
                                )
                                TableCell(mTotalCredit.toString(), mSecondRowWeight)
                            }
                            Row(Modifier.padding(start = mContentPadding, end = mContentPadding)) {
                                TableCell(
                                    stringResource(id = R.string.label_nav_debit), mFirstRowWeight
                                )
                                TableCell(mTotalDebit.toString(), mSecondRowWeight)
                            }
                            Row(Modifier.padding(start = mContentPadding, end = mContentPadding)) {
                                HorizontalDivider(
                                    thickness = 2.dp,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                            Row(Modifier.padding(start = mContentPadding, end = mContentPadding)) {
                                TableCell(
                                    stringResource(id = R.string.label_balance),
                                    mFirstRowWeight, isBold = true
                                )
                                TableCell(mTotalBalance, mSecondRowWeight, true)
                            }
                        }
                    }
                }
            }
        }
    }
}