package com.seravian.feat_verification.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RequestDetailsScreen(
    requestId: Int,
    modifier: Modifier = Modifier
) {

}

@Composable
private fun RequestDetailsContent(
    modifier: Modifier = Modifier
) {

}

@Preview
@Composable
private fun PreviewRequestDetailsScreen() {
    RequestDetailsScreen(
        requestId = 1
    )
}