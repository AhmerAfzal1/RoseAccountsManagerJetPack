package com.ahmer.accounts.utils

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object ConstantsChart {
    const val TODAY = "Today"
    const val YESTERDAY = "Yesterday"
    const val THIS_WEEK = "This week"
    const val LAST_7_DAYS = "Last 7 days"
    const val THIS_MONTH = "This month"
    const val ALL = "All"
}

object Constants {
    const val ADS_WAIT_TIME: Long = 250L
    const val ANIMATE_DURATION: Int = 500
    const val ANIMATE_FADE_IN_DURATION: Int = 250
    const val ANIMATE_FADE_OUT_DURATION: Int = 150
    const val BUTTON_SAVE: String = "SAVE"
    const val BUTTON_UPDATE: String = "UPDATE"
    const val DATA_STORE_KEY_CURRENCY: String = "Currency"
    const val DATA_STORE_KEY_SORT_BY: String = "SortBy"
    const val DATA_STORE_KEY_SORT_ORDER: String = "SortOrder"
    const val DATA_STORE_KEY_THEME: String = "Theme"
    const val DATA_STORE_NAME: String = "RoseDataStore"
    const val DB_NAME: String = "RoseDatabase.db"
    const val DB_TABLE_PERSON: String = "Persons"
    const val DB_TABLE_TRANSACTION: String = "Transactions"
    const val LEN_ADDRESS: Int = 64
    const val LEN_DESCRIPTION: Int = 64
    const val LEN_EMAIL: Int = 32
    const val LEN_NAME: Int = 24
    const val LEN_NOTES: Int = 128
    const val LEN_PHONE: Int = 15
    const val LOG_TAG: String = "RoseAccounts"
    const val PATTERN_CHART: String = "dd/MM/yyyy"
    const val PATTERN_FILE_NAME: String = "ddMMyyHHmmss"
    const val PATTERN_GENERAL: String = "dd MMM yyyy hh:mm:ss a"
    const val PATTERN_PDF: String = "EEEE, dd MMMM yyyy - hh:mm:ss a"
    const val PATTERN_SHORT: String = "dd MMM yy"
    const val PATTERN_TEXT_FIELD: String = "EEE, dd MMM yyyy h:mm a"
    const val PATTERN_TRANSACTION_ITEM: String = "dd MMM yyyy, h:mm a"
    const val PLAY_STORE_LINK: String = "https://play.google.com/store/apps/details?id="
    const val STATE_IN_STARTED_TIME: Long = 5000L
    const val TYPE_CREDIT: String = "Credit"
    const val TYPE_CREDIT_SUFFIX: String = "$TYPE_CREDIT (+)"
    const val TYPE_DEBIT: String = "Debit"
    const val TYPE_DEBIT_SUFFIX: String = "$TYPE_DEBIT (-)"
    const val TYPE_EXPENSE: String = "Expense"
    const val TYPE_INCOME: String = "Income"
    const val UNKNOWN_ERROR: String = "Unknown error"
    val ICON_SIZE: Dp = 36.dp
}