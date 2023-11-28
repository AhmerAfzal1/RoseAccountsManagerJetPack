package com.ahmer.accounts.utils

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Constants {
    const val ADS_WAIT_TIME: Long = 250L
    const val ANIMATE_ITEM_DURATION: Int = 500
    const val DATA_STORE_KEY_CURRENCY: String = "Currency"
    const val DATA_STORE_KEY_SORT_BY: String = "SortBy"
    const val DATA_STORE_KEY_SORT_ORDER: String = "SortOrder"
    const val DATA_STORE_KEY_THEME: String = "Theme"
    const val DATA_STORE_NAME: String = "RoseDataStore"
    const val DATABASE_NAME: String = "RoseDatabase.db"
    const val DATABASE_TABLE_PERSONS: String = "Persons"
    const val DATABASE_TABLE_TRANSACTION: String = "Transactions"
    const val LOG_TAG: String = "RoseAccounts"
    const val PATTERN_FILE_NAME: String = "ddMMyyHHmmss"
    const val PATTERN_GENERAL: String = "dd MMM yyyy hh:mm:ss a"
    const val PATTERN_PDF: String = "EEEE, dd MMMM yyyy - hh:mm:ss a"
    const val PATTERN_SHORT: String = "dd MMM yy"
    const val PATTERN_TEXT_FIELD: String = "EEE, dd MMM yyyy  h:mm a"
    const val PATTERN_TRANSACTION_ITEM: String = "dd MMM yyyy, h:mm a"
    const val PLAY_STORE_LINK: String = "https://play.google.com/store/apps/details?id="
    const val STATE_IN_STARTED_TIME: Long = 5000L
    const val TYPE_CREDIT: String = "Credit"
    const val TYPE_CREDIT_SUFFIX: String = "$TYPE_CREDIT (+)"
    const val TYPE_DEBIT: String = "Debit"
    const val TYPE_DEBIT_SUFFIX: String = "$TYPE_DEBIT (-)"
    val ICON_SIZE: Dp = 36.dp
    val TOP_APP_BAR_HEIGHT: Dp = 64.dp
}