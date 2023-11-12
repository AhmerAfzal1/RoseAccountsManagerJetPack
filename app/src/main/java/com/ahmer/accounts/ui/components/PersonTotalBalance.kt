package com.ahmer.accounts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.R
import com.ahmer.accounts.database.model.TransSumModel
import com.ahmer.accounts.ui.theme.colorGreenDark
import com.ahmer.accounts.ui.theme.colorGreenLight
import com.ahmer.accounts.ui.theme.colorRedDark
import com.ahmer.accounts.ui.theme.colorRedLight
import com.ahmer.accounts.utils.Currency
import com.ahmer.accounts.utils.HelperUtils

@Composable
fun PersonTotalBalance(
    modifier: Modifier = Modifier,
    transSumModel: TransSumModel,
    currency: Currency,
) {
    val mColorBackground: Color
    val mColorText: Color
    val mHorizontalSpace: Dp = 2.dp

    if (transSumModel.balance >= 0) {
        mColorBackground = colorGreenLight
        mColorText = colorGreenDark
    } else {
        mColorBackground = colorRedLight
        mColorText = colorRedDark
    }

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(size = 5.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(modifier = Modifier.padding(all = 5.dp)) {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .weight(weight = 1f)
                    .padding(horizontal = mHorizontalSpace)
                    .background(color = colorGreenLight)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HelperUtils.AmountWithSymbolText(
                        modifierTextAmount = Modifier.padding(top = 5.dp, bottom = 5.dp),
                        currency = currency,
                        amount = transSumModel.creditSum,
                        color = colorGreenDark,
                    )

                    Text(
                        text = stringResource(id = R.string.label_total_credit),
                        modifier = Modifier.padding(bottom = 5.dp),
                        color = colorGreenDark,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .weight(weight = 1f)
                    .padding(horizontal = mHorizontalSpace)
                    .background(color = colorRedLight)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HelperUtils.AmountWithSymbolText(
                        modifierTextAmount = Modifier.padding(top = 5.dp, bottom = 5.dp),
                        currency = currency,
                        amount = transSumModel.debitSum,
                        color = colorRedDark,
                    )

                    Text(
                        text = stringResource(id = R.string.label_total_debit),
                        modifier = Modifier.padding(bottom = 5.dp),
                        color = colorRedDark,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .weight(weight = 1f)
                    .padding(horizontal = mHorizontalSpace)
                    .background(color = mColorBackground)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HelperUtils.AmountWithSymbolText(
                        modifierTextAmount = Modifier.padding(top = 5.dp, bottom = 5.dp),
                        currency = currency,
                        amount = transSumModel.balance,
                        color = mColorText,
                    )

                    Text(
                        text = stringResource(id = R.string.label_total_balance),
                        modifier = Modifier.padding(bottom = 5.dp),
                        color = mColorText,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}