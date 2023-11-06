package com.ahmer.accounts.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.database.model.BalanceModel
import com.ahmer.accounts.database.model.PersonsBalanceModel
import com.ahmer.accounts.database.model.PersonsEntity
import com.ahmer.accounts.dialogs.DeleteAlertDialog
import com.ahmer.accounts.dialogs.MoreInfoAlertDialog
import com.ahmer.accounts.event.PersonEvent
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.DeleteIcon
import com.ahmer.accounts.utils.EditIcon
import com.ahmer.accounts.utils.InfoIcon

@Composable
fun PersonItem(
    personsBalanceModel: PersonsBalanceModel,
    onEvent: (PersonEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val mBalanceModel: BalanceModel = personsBalanceModel.balanceModel
    val mPersonsEntity: PersonsEntity = personsBalanceModel.personsEntity
    var mShowDeleteDialog: Boolean by remember { mutableStateOf(value = false) }
    var mShowInfoDialog: Boolean by remember { mutableStateOf(value = false) }
    val mPadding: Dp = 5.dp

    if (mShowDeleteDialog) {
        DeleteAlertDialog(
            nameAccount = mPersonsEntity.name,
            onConfirmClick = { onEvent(PersonEvent.OnDeleteClick(mPersonsEntity)) }
        )
    }

    if (mShowInfoDialog) {
        MoreInfoAlertDialog(mPersonsEntity)
    }

    ElevatedCard(
        modifier = modifier.clickable { onEvent(PersonEvent.OnAddTransactionClick(mPersonsEntity)) },
        shape = RoundedCornerShape(size = 5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = mPersonsEntity.name,
                modifier = Modifier.padding(start = mPadding),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = { mShowInfoDialog = true },
                    modifier = Modifier.then(Modifier.size(size = Constants.ICON_SIZE)),
                ) { InfoIcon() }
                IconButton(
                    onClick = { onEvent(PersonEvent.OnEditClick(mPersonsEntity)) },
                    modifier = Modifier.then(Modifier.size(size = Constants.ICON_SIZE)),
                ) { EditIcon() }
                IconButton(
                    onClick = { mShowDeleteDialog = true },
                    modifier = Modifier.then(Modifier.size(size = Constants.ICON_SIZE)),
                ) { DeleteIcon() }
            }
        }
        Column(
            modifier = Modifier.padding(end = mPadding, bottom = mPadding)
        ) {
            val mText: String = if (mPersonsEntity.phone.isEmpty()) {
                "Balance: ${mBalanceModel.balance}"
            } else {
                "Phone: ${mPersonsEntity.phone}  |  Balance: ${mBalanceModel.balance}"
            }
            Text(
                modifier = Modifier.padding(start = mPadding),
                text = mText,
                maxLines = 1,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}