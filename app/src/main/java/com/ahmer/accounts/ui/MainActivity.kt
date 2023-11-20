package com.ahmer.accounts.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ahmer.accounts.navigation.BottomNav
import com.ahmer.accounts.navigation.MainNavigation
import com.ahmer.accounts.navigation.NavItems
import com.ahmer.accounts.state.MainState
import com.ahmer.accounts.ui.theme.RoseAccountsManagerTheme
import com.ahmer.accounts.utils.ThemeMode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mViewModel: MainViewModel by viewModels()
    private val mSettingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition { mViewModel.isLoadingSplash.value }
        }
        setContent {
            val mCurrentTheme: ThemeMode by mSettingsViewModel.currentTheme.collectAsStateWithLifecycle()
            val isDarkTheme: Boolean = when (mCurrentTheme) {
                ThemeMode.Dark -> true
                ThemeMode.Light -> false
                ThemeMode.System -> isSystemInDarkTheme()
            }
            RoseAccountsManagerTheme(darkTheme = isDarkTheme) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    MainFunction(viewModel = mViewModel)
                }
            }
        }
    }
}

@Composable
fun MainFunction(viewModel: MainViewModel) {
    val mNavController: NavHostController = rememberNavController()
    val mNavBackStackEntry: NavBackStackEntry? by mNavController.currentBackStackEntryAsState()
    val mCurrentRoute: String? = mNavBackStackEntry?.destination?.route
    val mState: MainState by viewModel.uiState.collectAsState()
    var mBottomBarState: Boolean by rememberSaveable { (mutableStateOf(value = false)) }

    Scaffold(modifier = Modifier.navigationBarsPadding(), bottomBar = {
        mBottomBarState = mCurrentRoute != NavItems.PersonAddEdit.fullRoute
                && mCurrentRoute != NavItems.Transactions.fullRoute
                && mCurrentRoute != NavItems.TransactionsAddEdit.fullRoute
        BottomNav(
            navController = mNavController, bottomBarState = mBottomBarState
        )
    }) { innerPadding ->
        MainNavigation(
            modifier = Modifier.padding(paddingValues = innerPadding),
            navController = mNavController,
            transSumModel = mState.accountsBalance
        )
    }
}

fun Modifier.bottomBorder(strokeWidth: Dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height - strokeWidthPx / 2

            drawLine(
                color = color,
                start = Offset(x = 0f, y = height),
                end = Offset(x = width, y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

@Composable
fun ColorScheme.isLight() = this.background.luminance() > 0.5