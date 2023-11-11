package com.ahmer.accounts.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.database.model.TransEntity
import com.ahmer.accounts.event.TransEvent
import com.ahmer.accounts.ui.theme.colorGreenDark
import com.ahmer.accounts.ui.theme.colorRedDark
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.CreditIcon
import com.ahmer.accounts.utils.Currency
import com.ahmer.accounts.utils.DebitIcon
import com.ahmer.accounts.utils.DeleteIcon
import com.ahmer.accounts.utils.HelperUtils

@Composable
fun TransItem(
    currency: Currency,
    transEntity: TransEntity,
    onEvent: (TransEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(bottom = 3.dp)
            .fillMaxWidth()
            .clickable { onEvent(TransEvent.OnEditClick(transEntity)) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (transEntity.type == "Debit") {
            DebitIcon(modifier = Modifier.size(size = Constants.ICON_SIZE), tint = colorRedDark)
        } else {
            CreditIcon(modifier = Modifier.size(size = Constants.ICON_SIZE), tint = colorGreenDark)
        }
        Spacer(modifier = Modifier.width(width = 10.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1f)
        ) {
            val mDescription: String = transEntity.description
            Text(
                text = "Rs. ${HelperUtils.getRoundedValue(transEntity.amount.toDouble())}",
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium
            )
            if (mDescription.isNotEmpty()) {
                Text(
                    text = mDescription,
                    color = Color.DarkGray,
                    maxLines = 3,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = transEntity.date,
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }
        IconButton(onClick = { onEvent(TransEvent.OnDeleteClick(transEntity)) }) { DeleteIcon() }
    }
    Divider(thickness = 1.dp)
}