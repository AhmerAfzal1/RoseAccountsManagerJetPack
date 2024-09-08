package com.ahmer.accounts.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmer.accounts.R
import com.ahmer.accounts.database.entity.TransEntity
import com.ahmer.accounts.dialogs.DateTimePickerDialog
import com.ahmer.accounts.event.TransAddEditEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.state.TransAddEditState
import com.ahmer.accounts.utils.BackIcon
import com.ahmer.accounts.utils.CloseIcon
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.Currency
import com.ahmer.accounts.utils.CurrencyIcon
import com.ahmer.accounts.utils.DateIcon
import com.ahmer.accounts.utils.HelperUtils
import com.ahmer.accounts.utils.MyTextField
import com.ahmer.accounts.utils.NotesIcon
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransAddEditScreen(
    viewModel: TransAddEditViewModel,
    viewModelSettings: SettingsViewModel,
    onPopBackStack: () -> Unit
) {
    val isEditMode: Boolean = viewModel.isEditMode
    val mContext: Context = LocalContext.current
    val mCurrency: Currency by viewModelSettings.currentCurrency.collectAsStateWithLifecycle()
    val mFocusManager: FocusManager = LocalFocusManager.current
    val mFocusRequester: FocusRequester = remember { FocusRequester() }
    val mKeyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
    val mState: TransAddEditState by viewModel.uiState.collectAsStateWithLifecycle()
    val mSurfaceColor: Color = if (isSystemInDarkTheme()) Color.Black else Color.Yellow
    val mSurfaceElevation: Dp = 4.dp
    val mTransEntity: TransEntity = mState.transaction ?: TransEntity()
    var mDatePickerDialog: Boolean by rememberSaveable { mutableStateOf(value = false) }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                UiEvent.PopBackStack -> onPopBackStack()
                is UiEvent.ShowToast -> HelperUtils.showToast(
                    context = mContext, msg = event.message
                )

                else -> Unit
            }
        }
    }

    LaunchedEffect(Unit) {
        mFocusRequester.requestFocus()
    }

    if (mDatePickerDialog) {
        DateTimePickerDialog(selectedDate = mTransEntity.date) {
            viewModel.onEvent(TransAddEditEvent.OnDateChange(it))
        }
    }

    fun clear() {
        mFocusManager.clearFocus()
        mKeyboardController?.hide()
    }

    Scaffold(modifier = Modifier, topBar = {
        Surface(
            modifier = Modifier.shadow(
                elevation = mSurfaceElevation,
                ambientColor = mSurfaceColor,
                spotColor = mSurfaceColor,
            )
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = if (!isEditMode) stringResource(id = R.string.label_add_transaction)
                        else stringResource(id = R.string.label_edit_transaction)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            clear()
                            onPopBackStack()
                        },
                        modifier = Modifier.size(size = Constants.ICON_SIZE)
                    ) { BackIcon() }
                },
            )
        }
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues = innerPadding)
                .padding(start = 16.dp, end = 16.dp, top = 8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val mOptions: List<String> = listOf(Constants.TYPE_CREDIT, Constants.TYPE_DEBIT)
            MyTextField(value = HelperUtils.getDateTime(
                time = mTransEntity.date, pattern = Constants.PATTERN_TEXT_FIELD
            ),
                onValueChange = {},
                modifier = Modifier.onFocusChanged { mDatePickerDialog = it.isFocused },
                readOnly = true,
                label = { Text(stringResource(id = R.string.label_date)) },
                leadingIcon = { DateIcon() },
                trailingIcon = {},
                supportingText = {})

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
            ) {
                mOptions.forEach { text ->
                    val mType = if (text == Constants.TYPE_CREDIT) {
                        Constants.TYPE_CREDIT_SUFFIX
                    } else Constants.TYPE_DEBIT_SUFFIX
                    Box(contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 5.dp)
                            .background(
                                color = if (text == mTransEntity.type) {
                                    MaterialTheme.colorScheme.primary
                                } else Color.LightGray
                            )
                            .clickable { viewModel.onEvent(TransAddEditEvent.OnTypeChange(text)) }) {
                        Text(
                            text = mType.uppercase(),
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Color.White,
                            fontWeight = if (text == mTransEntity.type) FontWeight.Bold else FontWeight.Normal,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            MyTextField(
                value = mTransEntity.amount,
                onValueChange = {
                    viewModel.onEvent(TransAddEditEvent.OnAmountChange(it))
                },
                modifier = Modifier
                    .focusRequester(focusRequester = mFocusRequester)
                    .onFocusChanged { focus ->
                        if (focus.isFocused) {
                            mKeyboardController?.show()
                        }
                    },
                label = { Text(stringResource(id = R.string.label_by_amount)) },
                leadingIcon = { CurrencyIcon() },
                trailingIcon = {
                    if (mTransEntity.amount.isNotEmpty()) {
                        CloseIcon(modifier = Modifier.clickable {
                            if (mTransEntity.amount.isNotEmpty()) {
                                viewModel.onEvent(TransAddEditEvent.OnAmountChange(amount = ""))
                            }
                        })
                    }
                },
                prefix = { Text(text = mCurrency.symbol) },
                supportingText = { },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = {
                    mFocusManager.moveFocus(FocusDirection.Down)
                })
            )

            MyTextField(
                value = mTransEntity.description,
                onValueChange = {
                    if (it.length <= Constants.LEN_DESCRIPTION) {
                        viewModel.onEvent(TransAddEditEvent.OnDescriptionChange(it))
                    }
                },
                label = { Text(stringResource(id = R.string.label_description)) },
                leadingIcon = { NotesIcon() },
                trailingIcon = {
                    if (mTransEntity.description.isNotEmpty()) {
                        CloseIcon(modifier = Modifier.clickable {
                            if (mTransEntity.description.isNotEmpty()) {
                                viewModel.onEvent(
                                    TransAddEditEvent.OnDescriptionChange(description = "")
                                )
                            }
                        })
                    }
                },
                supportingText = {
                    Text(
                        text = "${mTransEntity.description.length} / ${Constants.LEN_DESCRIPTION}",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { clear() }),
                maxLines = 3
            )

            OutlinedButton(
                onClick = {
                    clear()
                    viewModel.onEvent(TransAddEditEvent.OnSaveClick)
                }, enabled = mTransEntity.amount.isNotEmpty()
            ) {
                Text(text = if (!isEditMode) Constants.BUTTON_SAVE else Constants.BUTTON_UPDATE)
            }
        }
    }
}