package com.seravian.feat_verification.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import java.util.Locale

@Composable
fun LanguageDropDown(
    modifier: Modifier = Modifier
) {
    val availableLanguages = Locale.getAvailableLocales()
    val languages: Map<String, String> = availableLanguages.associate { locale ->
        locale.language to locale.displayName
    }
}

@Preview
@Composable
private fun PreviewLanguageDropDown() {
    LanguageDropDown()
}