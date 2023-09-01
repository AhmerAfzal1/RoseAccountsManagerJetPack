package com.ahmer.accounts.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmer.accounts.R
import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.dialogs.DeleteAlertDialog
import com.ahmer.accounts.dialogs.MoreInfoAlertDialog
import com.ahmer.accounts.drawer.DrawerItems
import com.ahmer.accounts.drawer.MenuSearchBar
import com.ahmer.accounts.drawer.NavShape
import com.ahmer.accounts.drawer.drawerItemsList
import com.ahmer.accounts.event.HomeEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.utils.AddIcon
import com.ahmer.accounts.utils.DeleteIcon
import com.ahmer.accounts.utils.EditIcon
import com.ahmer.accounts.utils.HelperFunctions
import com.ahmer.accounts.utils.InfoIcon
import com.ahmer.accounts.utils.MenuIcon
import com.ahmer.accounts.utils.PinIcon
import com.ahmer.accounts.utils.SearchIcon
import com.ahmer.accounts.utils.SortBy
import com.ahmer.accounts.utils.SortByDateIcon
import com.ahmer.accounts.utils.SortByNameIcon
import com.ahmer.accounts.utils.SortIcon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LoadingProgressBar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.then(Modifier.size(80.dp)), strokeWidth = 8.dp
        )
    }
}

@Composable
private fun UserItem(
    userModel: UserModel,
    onEvent: (HomeEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val mIconSize: Dp = 36.dp
    val mPadding: Dp = 5.dp
    var mShowDeleteDialog by remember { mutableStateOf(false) }
    var mShowInfoDialog by remember { mutableStateOf(false) }

    if (mShowDeleteDialog) {
        DeleteAlertDialog(
            nameAccount = userModel.name!!,
            onConfirmClick = { onEvent(HomeEvent.OnDeleteClick(userModel)) }
        )
    }

    if (mShowInfoDialog) {
        MoreInfoAlertDialog(userModel)
    }

    ElevatedCard(
        modifier = modifier.clickable { /*TODO*/ },
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(end = mPadding)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.then(Modifier.size(mIconSize)),
                ) { PinIcon() }
                IconButton(
                    onClick = { mShowInfoDialog = true },
                    modifier = Modifier.then(Modifier.size(mIconSize)),
                ) { InfoIcon() }
                IconButton(
                    onClick = {
                        onEvent(HomeEvent.OnItemClick(userModel))
                    },
                    modifier = Modifier.then(Modifier.size(mIconSize)),
                ) { EditIcon() }
                IconButton(
                    onClick = { mShowDeleteDialog = true },
                    modifier = Modifier.then(Modifier.size(mIconSize)),
                ) { DeleteIcon() }
            }
        }
        Column(
            modifier = Modifier.padding(start = mPadding, end = mPadding, bottom = mPadding)
        ) {
            Text(
                modifier = Modifier.padding(start = mPadding),
                text = "${userModel.name}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                modifier = Modifier.padding(start = mPadding),
                text = "Phone: ${userModel.phone}  |  Balance: ",
                maxLines = 1,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TopAppBarWithNavigationBar(
    onNavigation: (UiEvent.Navigate) -> Unit
) {
    val mContext: Context = LocalContext.current.applicationContext
    val mCoroutineScope: CoroutineScope = rememberCoroutineScope()
    val mCredit = 10000.00
    val mDebit = 8000.00
    val mDrawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val mHomeViewModel: HomeViewModel = hiltViewModel()
    val mNavItemsList: List<DrawerItems> = drawerItemsList()
    val mScrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val mSnackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val mUserData =
        mHomeViewModel.getAllUsersBySearchAndSort.collectAsStateWithLifecycle(emptyList())
    var mSelectedItems by rememberSaveable { mutableIntStateOf(0) }
    var mShowDropdownMenu by remember { mutableStateOf(false) }
    var mShowSearch by remember { mutableStateOf(false) }
    var mTextSearch by remember { mutableStateOf(mHomeViewModel.searchQuery.value) }

    LaunchedEffect(key1 = true) {
        mHomeViewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigation(event)
                is UiEvent.ShowSnackBar -> {
                    val mResult = mSnackBarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                    if (mResult == SnackbarResult.ActionPerformed) {
                        mHomeViewModel.onEvent(HomeEvent.OnUndoDeleteClick)
                    }
                }

                else -> Unit
            }
        }
    }

    if (mShowSearch) {
        MenuSearchBar(text = mTextSearch, onTextChange = { mTextSearch = it }) {
            mShowSearch = false
        }/*CustomSearchView(
               text = mTextSearch,
               onTextChange = { mTextSearch = it },
               onCloseClick = {
                   mCoroutineScope.launch { delay(200L) }
               },
               onSearchClick = { mTextSearch = it }
           )*/
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                NavShape(mCredit, mDebit)
                Spacer(Modifier.height(12.dp))
                mNavItemsList.forEachIndexed { index, item ->
                    //Spacer(Modifier.height(6.dp))
                    NavigationDrawerItem(
                        label = { Text(text = item.label) },
                        selected = index == mSelectedItems,
                        onClick = {
                            //navController.navigate(item.route)
                            mSelectedItems = index
                            mCoroutineScope.launch { mDrawerState.close() }
                            HelperFunctions.toastLong(mContext, item.label)
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == mSelectedItems) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.contentDescription
                            )
                        },
                        badge = {
                            item.badgeCount?.let { Text(text = item.run { badgeCount.toString() }) }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }, drawerState = mDrawerState
    ) {
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(title = {
                Text(
                    text = stringResource(R.string.app_name),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }, navigationIcon = {
                IconButton(onClick = {
                    mCoroutineScope.launch { mDrawerState.open() }
                }) { MenuIcon() }
            }, actions = {
                IconButton(onClick = { mShowSearch = true }) { SearchIcon() }
                IconButton(onClick = {
                    mShowDropdownMenu = !mShowDropdownMenu
                }) { SortIcon() }
                DropdownMenu(expanded = mShowDropdownMenu,
                    onDismissRequest = { mShowDropdownMenu = false }) {
                    DropdownMenuItem(text = { Text(text = stringResource(R.string.label_sort_by_name)) },
                        onClick = {
                            mHomeViewModel.onEvent(HomeEvent.OnSortBy(SortBy.NAME))
                            mShowDropdownMenu = false
                        },
                        leadingIcon = { SortByNameIcon() })
                    DropdownMenuItem(text = { Text(text = stringResource(R.string.label_sort_by_date_created)) },
                        onClick = {
                            mHomeViewModel.onEvent(HomeEvent.OnSortBy(SortBy.DATE))
                            mShowDropdownMenu = false
                        },
                        leadingIcon = { SortByDateIcon() })
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            ), scrollBehavior = mScrollBehavior
            )
        }, snackbarHost = { SnackbarHost(hostState = mSnackBarHostState) },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    mHomeViewModel.onEvent(HomeEvent.OnAddClick)
                }) { AddIcon() }
            }) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(10.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = mUserData.value,
                        key = { listUser -> listUser.id!! }) { user ->
                        UserItem(
                            userModel = user,
                            onEvent = mHomeViewModel::onEvent,
                        )
                    }
                }
            }
        }
    }
}