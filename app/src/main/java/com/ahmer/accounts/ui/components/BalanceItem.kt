package com.ahmer.accounts.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.R
import com.ahmer.accounts.database.model.PersonsEntity
import com.ahmer.accounts.database.model.TransSumModel
import com.ahmer.accounts.ui.theme.colorGreenDark
import com.ahmer.accounts.ui.theme.colorGreenLight
import com.ahmer.accounts.ui.theme.colorRedDark
import com.ahmer.accounts.ui.theme.colorRedLight
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.CreditIcon
import com.ahmer.accounts.utils.Currency
import com.ahmer.accounts.utils.DebitIcon
import com.ahmer.accounts.utils.DeleteIcon
import com.ahmer.accounts.utils.EditIcon
import com.ahmer.accounts.utils.HelperUtils
import com.ahmer.accounts.utils.InfoIcon
import com.ahmer.accounts.utils.PdfIcon

@Composable
fun BalanceItem(
    modifier: Modifier = Modifier,
    transSumModel: TransSumModel,
    currency: Currency,
    personsEntity: PersonsEntity = PersonsEntity(),
    isUsedTrans: Boolean = false,
    onClickDelete: () -> Unit = {},
    onClickInfo: () -> Unit = {},
    onClickPdf: () -> Unit = {},
    onClickEdit: () -> Unit = {},
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 4.dp),
        shape = CardDefaults.outlinedShape,
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.outlinedCardElevation(),
        border = BorderStroke(width = 1.5.dp, color = MaterialTheme.colorScheme.primary),
    ) {
        val mPadding: PaddingValues
        if (isUsedTrans) {
            mPadding = PaddingValues(start = 8.dp, end = 8.dp)
            val isEnable: Boolean = personsEntity.name.isNotEmpty()
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = personsEntity.name,
                        modifier = Modifier.padding(start = 4.dp),
                        fontWeight = FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = { onClickDelete() },
                            modifier = Modifier.size(size = Constants.ICON_SIZE - 6.dp),
                            enabled = isEnable,
                        ) { DeleteIcon(tint = Color.Red) }
                        IconButton(
                            onClick = { onClickInfo() },
                            modifier = Modifier.size(size = Constants.ICON_SIZE - 6.dp),
                            enabled = isEnable,
                        ) { InfoIcon() }
                        IconButton(
                            onClick = { onClickPdf() },
                            modifier = Modifier.size(size = Constants.ICON_SIZE - 6.dp),
                            enabled = isEnable,
                        ) { PdfIcon() }
                        IconButton(
                            onClick = { onClickEdit() },
                            modifier = Modifier.size(size = Constants.ICON_SIZE - 6.dp),
                            enabled = isEnable,
                        ) { EditIcon() }
                    }
                }
                if (personsEntity.phone.isNotEmpty()) {
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = personsEntity.phone,
                        color = Color.Gray,
                        maxLines = 1,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                BalanceItem(
                    transSumModel = transSumModel,
                    currency = currency,
                    paddingValues = mPadding,
                )
            }
        } else {
            mPadding = PaddingValues(start = 8.dp, end = 8.dp, top = 6.dp)
            BalanceItem(
                transSumModel = transSumModel,
                currency = currency,
                paddingValues = mPadding
            )
        }
    }
}

@Composable
private fun BalanceItem(
    transSumModel: TransSumModel,
    currency: Currency,
    paddingValues: PaddingValues
) {
    val mCreditIcon: @Composable () -> Unit = {
        CreditIcon(modifier = Modifier.size(size = 24.dp), tint = colorGreenDark)
    }
    val mDebtIcon: @Composable () -> Unit = {
        DebitIcon(modifier = Modifier.size(size = 24.dp), tint = colorRedDark)
    }

    val mColorBackground: Color
    val mColorText: Color
    val mIcon: @Composable () -> Unit

    if (transSumModel.balance >= 0) {
        mColorBackground = colorGreenLight
        mColorText = colorGreenDark
        mIcon = { mCreditIcon.invoke() }
    } else {
        mColorBackground = colorRedLight
        mColorText = colorRedDark
        mIcon = { mDebtIcon.invoke() }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues = paddingValues),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        BalanceCard(
            modifier = Modifier
                .weight(weight = 0.5f)
                .padding(end = 4.dp),
            currency = currency,
            colorText = colorGreenDark,
            colorBg = colorGreenLight,
            amount = transSumModel.creditSum,
            type = "Total ${Constants.TYPE_CREDIT}",
        )

        BalanceCard(
            modifier = Modifier
                .weight(weight = 0.5f)
                .padding(start = 4.dp),
            currency = currency,
            colorText = colorRedDark,
            colorBg = colorRedLight,
            amount = transSumModel.debitSum,
            type = "Total ${Constants.TYPE_DEBIT}",
        )
    }

    BalanceCard(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 6.dp),
        currency = currency,
        colorText = mColorText,
        colorBg = mColorBackground,
        amount = transSumModel.balance,
        type = stringResource(id = R.string.label_total_balance),
    )
}

@Composable
private fun BalanceCard(
    modifier: Modifier = Modifier,
    currency: Currency,
    colorText: Color,
    colorBg: Color,
    amount: Double,
    type: String,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = colorBg)
            .shadow(elevation = 0.5.dp, shape = RoundedCornerShape(size = 1.dp)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = currency.symbol,
                color = colorText,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = " ${HelperUtils.getRoundedValue(value = amount)}",
                color = colorText,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
            )
        }

        Text(
            text = type.uppercase(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp),
            color = colorText,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}