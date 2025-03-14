package com.ahmer.accounts.utils

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.ahmer.accounts.R
import com.ahmer.accounts.ui.theme.colorGreenDark
import com.ahmer.accounts.ui.theme.colorRedDark
import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

object HelperUtils {
    @Composable
    fun AdjustableText(
        modifier: Modifier = Modifier,
        text: String,
        color: Color,
        length: Int = 0,
        isBold: Boolean = true
    ) {
        Text(
            text = text,
            modifier = modifier,
            color = color,
            fontWeight = if (isBold && length <= 17) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1,
            style = when (if (length == 0) text.length else length) {
                in 0..10 -> MaterialTheme.typography.bodyMedium
                in 11..13 -> MaterialTheme.typography.bodySmall
                else -> MaterialTheme.typography.labelSmall
            }
        )
    }

    @Composable
    fun AmountWithSymbolText(
        modifier: Modifier = Modifier,
        modifierSymbol: Modifier = Modifier,
        modifierAmount: Modifier = Modifier,
        context: Context,
        currency: Currency,
        amount: Double,
        color: Color? = null,
        isBold: Boolean = true,
        isExpense: Boolean = false,
        type: String? = null,
    ) {
        Row(
            modifier = modifier, verticalAlignment = Alignment.CenterVertically
        ) {
            val mTextLength: Int =
                currency.symbol.length + roundValue(context = context, value = amount).length - 1
            val mValue: String = roundValue(context = context, value = amount)
            val mAmount: String = if (isExpense) {
                if (type == Constants.TYPE_INCOME) "+$mValue" else "-$mValue"
            } else mValue

            val mColor: Color = color
                ?: if (type == Constants.TYPE_EXPENSE || type == Constants.TYPE_DEBIT) {
                    colorRedDark
                } else colorGreenDark

            AdjustableText(
                text = "${currency.symbol} ",
                modifier = modifierSymbol,
                color = mColor,
                length = mTextLength,
                isBold = isBold,
            )
            AdjustableText(
                text = mAmount,
                modifier = modifierAmount,
                color = mColor,
                length = mTextLength,
                isBold = isBold,
            )
        }
    }

    fun appInfo(context: Context): AppVersion {
        val mAppVersion: AppVersion by lazy {
            val mVersion = AppVersion()
            context.packageManager.getPackageInfo(
                context.packageName, PackageManager.GET_ACTIVITIES
            )?.let { packageInfo ->
                mVersion.versionName = packageInfo.versionName
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    mVersion.versionCode = packageInfo.longVersionCode
                } else {
                    @Suppress("DEPRECATION")
                    mVersion.versionCode = packageInfo.versionCode.toLong()
                }
            }
            mVersion
        }
        return mAppVersion
    }

    private fun contentFileName(context: Context, uri: Uri): String? = runCatching {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            cursor.moveToFirst()
            return@use cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
                .let(cursor::getString)
        }
    }.getOrNull()

    fun fileNameFromDatabase(context: Context, uri: Uri): String? = when (uri.scheme) {
        ContentResolver.SCHEME_CONTENT -> contentFileName(context = context, uri = uri)
        else -> uri.path?.let(::File)?.name
    }

    fun getDateTime(time: Long, pattern: String = ""): String = if (time == 0.toLong()) {
        ""
    } else {
        val mPattern = pattern.ifEmpty { Constants.PATTERN_GENERAL }
        LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern(mPattern))
    }

    fun dirSize(directory: File): Long {
        var mSize = 0.toLong()
        directory.listFiles()?.forEach { file ->
            if (file != null && file.isDirectory) {
                mSize += dirSize(directory = file)
            } else if (file != null && file.isFile) {
                mSize += file.length()
            }
        }
        return mSize
    }

    fun cacheSize(context: Context): String {
        var mSize = 0.toLong()
        val mExternalCacheDir = context.externalCacheDir
        mSize += dirSize(directory = context.cacheDir)
        if (mExternalCacheDir != null) {
            mSize += dirSize(directory = mExternalCacheDir)
        }
        mSize += dirSize(directory = context.codeCacheDir)
        return sizeFormat(size = mSize)
    }

    fun playStoreLink(context: Context): String = Constants.PLAY_STORE_LINK + context.packageName

    fun roundValue(context: Context, value: Double): String {
        val mFormat = DecimalFormat("#,##0.##")
        var mRound: BigDecimal? = null
        try {
            mRound = BigDecimal(value).setScale(2, RoundingMode.HALF_UP)
        } catch (e: NumberFormatException) {
            showToast(context = context, msg = e.localizedMessage ?: Constants.UNKNOWN_ERROR)
        } catch (e: Exception) {
            showToast(context = context, msg = e.localizedMessage ?: Constants.UNKNOWN_ERROR)
        }
        return mFormat.format(mRound)
    }

    fun roundValue(value: Double) = String.format(
        locale = Locale.getDefault(), format = "%.2f", value
    )

    fun sizeFormat(size: Long): String {
        var mResult = size.toDouble() / 1024
        if (mResult < 1024) return "${mResult.roundToInt()} KB"
        mResult /= 1024
        if (mResult < 1024) return String.format(
            locale = Locale.getDefault(), format = "%.2f MB", mResult
        )
        mResult /= 1024
        return String.format(locale = Locale.getDefault(), format = "%.2f GB", mResult)
    }

    fun isGrantedPermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context, permission
        ) == PackageManager.PERMISSION_GRANTED

    }

    fun isGrantedPermissions(context: Context, permissions: Array<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    fun relaunchApp(context: Context) {
        val mPackageManager: PackageManager = context.packageManager
        val mIntent: Intent = mPackageManager.getLaunchIntentForPackage(context.packageName)!!
        val mComponentName: ComponentName = mIntent.component!!
        val mRestartIntent: Intent = Intent.makeRestartActivityTask(mComponentName)
        context.startActivity(mRestartIntent)
        Runtime.getRuntime().exit(0)
    }

    fun moreApps(context: Context, developerId: String = "Ahmer Afzal") {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "market://search?q=pub:$developerId&hl=en".toUri()
                ).apply {
                    // Ensure the intent has the FLAG_ACTIVITY_NEW_TASK if needed
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }, null
            )
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/developer?id=$developerId&hl=en".toUri()
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }, null
            )
        }
    }

    fun runWeb(context: Context, packageName: String) {
        try {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri()).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }, null
            )
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=$packageName".toUri()
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }, null
            )
        }
    }

    fun shareApp(context: Context) {
        try {
            val mIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
                putExtra(
                    Intent.EXTRA_TEXT,
                    "https://play.google.com/store/apps/details?id=${context.packageName}"
                )
            }

            context.startActivity(
                Intent.createChooser(mIntent, "Share app via").apply {
                    // Add flags to the chooser intent itself
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }, null
            )
        } catch (e: Exception) {
            Log.e(Constants.LOG_TAG, "Exception while sharing app: ${e.localizedMessage}", e)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }
}