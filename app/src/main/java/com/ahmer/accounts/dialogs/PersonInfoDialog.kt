package com.ahmer.accounts.dialogs

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahmer.accounts.R
import com.ahmer.accounts.database.model.PersonsEntity
import com.ahmer.accounts.utils.HelperUtils
import com.ahmer.accounts.utils.InfoIcon

@Composable
fun RowScope.InfoText(text: String, weight: Float, isTitle: Boolean = false) {
    if (isTitle) {
        Text(
            modifier = Modifier
                .weight(weight = weight)
                .padding(top = 3.dp),
            text = text,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
    } else {
        Text(
            modifier = Modifier
                .weight(weight = weight)
                .padding(start = 2.dp, top = 3.dp),
            text = text,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreInfoAlertDialog(personsEntity: PersonsEntity) {
    val mDataList = listOf(
        personsEntity.name,
        personsEntity.address,
        personsEntity.phone,
        personsEntity.email,
        personsEntity.notes,
        HelperUtils.getDateTime(personsEntity.created),
        HelperUtils.getDateTime(personsEntity.updated),
    )
    val mTitleList = listOf(
        "Name:",
        "Address:",
        "Phone:",
        "Email:",
        "Notes:",
        "Created:",
        "Updated:",
    )
    val mOpenDialog = remember { mutableStateOf(value = true) }

    if (mOpenDialog.value) {
        AlertDialog(
            onDismissRequest = { mOpenDialog.value = false }) {
            ElevatedCard(
                shape = RoundedCornerShape(size = 10.dp),
                modifier = Modifier.padding(all = 10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            ) {
                InfoIcon(
                    modifier = Modifier
                        .size(size = 48.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                        .padding(top = 5.dp),
                    tint = AlertDialogDefaults.iconContentColor
                )

                LazyColumn(
                    modifier = Modifier.padding(
                        start = 10.dp, end = 10.dp, top = 5.dp, bottom = 10.dp
                    )
                ) {
                    itemsIndexed(items = mTitleList) { index, title ->
                        Row {
                            InfoText(text = title, weight = 1f, isTitle = true)
                            InfoText(text = mDataList[index], weight = 3f, isTitle = false)
                        }
                    }
                }

                TextButton(
                    onClick = { mOpenDialog.value = false },
                    modifier = Modifier
                        .align(alignment = Alignment.End)
                        .padding(end = 10.dp, bottom = 5.dp),
                ) {
                    Text(text = stringResource(id = R.string.label_ok), fontSize = 14.sp)
                }
            }
        }
    }
}