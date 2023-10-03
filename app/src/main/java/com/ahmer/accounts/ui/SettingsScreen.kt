package com.ahmer.accounts.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmer.accounts.R
import com.ahmer.accounts.state.AppBarState
import com.ahmer.accounts.utils.AppVersion
import com.ahmer.accounts.utils.ClearCachesIcon
import com.ahmer.accounts.utils.HelperUtils
import com.ahmer.accounts.utils.ThemeIcon
import com.ahmer.accounts.utils.VersionIcon

@Composable
fun SettingsScreen(appBarState: (AppBarState) -> Unit) {
    val mContext: Context = LocalContext.current.applicationContext
    val mViewModel: SettingsViewModel = hiltViewModel()
    val mAppVersion: AppVersion = HelperUtils.getAppInfo(context = mContext)
    val mTheme by remember { mutableStateOf(value = Theme.System) }
    mViewModel.updateAppTheme(theme = mTheme)

    LaunchedEffect(key1 = true) {
        appBarState(
            AppBarState(
                searchActions = {
                    Text(
                        text = stringResource(id = R.string.label_settings),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {},
                floatingAction = {},
                isMenuNavigationIcon = true,
                isSnackBarRequired = false,
                newSnackBarHost = {}
            )
        )
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PreferenceCategory(title = stringResource(id = R.string.label_pref_category_theme)) {
            TextPreference(title = { Text(text = stringResource(id = R.string.label_pref_text_title_theme)) },
                summary = { Text(text = stringResource(id = R.string.label_pref_text_summery_theme)) },
                icon = { ThemeIcon() },
                onClick = {}
            )
        }

        PreferenceCategory(title = stringResource(id = R.string.label_pref_category_general)) {
            TextPreference(title = { Text(text = stringResource(id = R.string.label_pref_text_title_clear_caches)) },
                summary = {
                    Text(
                        text = stringResource(
                            R.string.label_pref_text_summery_caches,
                            HelperUtils.getCacheSize(mContext)
                        )
                    )
                },
                icon = { ClearCachesIcon() },
                onClick = {}
            )
            TextPreference(title = { Text(text = stringResource(id = R.string.label_pref_text_title_app_version)) },
                summary = { Text(text = "${mAppVersion.versionName} (${mAppVersion.versionCode})") },
                icon = { VersionIcon() },
                onClick = {}
            )
        }
    }
}

@Composable
fun TextPreference(
    title: @Composable (() -> Unit),
    summary: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = {},
    controls: @Composable (() -> Unit)? = null,
    enabled: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 8.dp)
            .alpha(if (enabled) 1f else 0.4f),
    ) {
        if (icon != null) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .padding(start = 4.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                icon()
            }
        } else {
            Box(modifier = Modifier.size(0.dp))
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 16.dp)
        ) {
            ProvideTextStyle(value = MaterialTheme.typography.titleMedium) {
                title()
            }
            if (summary != null) {
                Spacer(modifier = Modifier.height(2.dp))
                ProvideTextStyle(value = MaterialTheme.typography.bodyMedium) {
                    summary()
                }
            }
        }
        if (controls != null) {
            Box(
                modifier = Modifier.padding(start = 24.dp)
            ) {
                controls()
            }
        }
    }
}

@Composable
fun PreferenceCategory(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
        ) {
            Text(
                modifier = Modifier.padding(start = 32.dp),
                text = title,
                color = MaterialTheme.colorScheme.primary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.titleSmall
            )
        }
        content()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
        )
    }
}

sealed class Theme {
    data object Dark : Theme()
    data object Light : Theme()
    data object System : Theme()
}