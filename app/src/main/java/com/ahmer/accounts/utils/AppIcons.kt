package com.ahmer.accounts.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.ahmer.accounts.R

@Composable
fun AddIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.Add,
        contentDescription = stringResource(id = R.string.content_description_add),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun CloseIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.Close,
        contentDescription = stringResource(id = R.string.content_description_close),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun DeleteIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.DeleteForever,
        contentDescription = stringResource(id = R.string.content_description_delete),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun EditIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.Edit,
        contentDescription = stringResource(id = R.string.content_description_edit),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun InfoIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.Info,
        contentDescription = stringResource(id = R.string.content_description_info),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun MenuIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.Menu,
        contentDescription = stringResource(id = R.string.content_description_menu),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun PinIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.PushPin,
        contentDescription = stringResource(id = R.string.content_description_pin),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun SearchIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.Search,
        contentDescription = stringResource(id = R.string.content_description_search),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun SortByDateIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.ChangeCircle,
        contentDescription = stringResource(id = R.string.content_description_sort_by_date),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun SortByNameIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.SortByAlpha,
        contentDescription = stringResource(id = R.string.content_description_sort_by_name),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun SortIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.ArrowDropDown,
        contentDescription = stringResource(id = R.string.content_description_sort),
        modifier = modifier,
        tint = tint
    )
}