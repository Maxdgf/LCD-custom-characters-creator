package com.example.lcdcustomcharactercreator.ui.screens

import android.content.res.Configuration
import android.util.Base64
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.lcdcustomcharactercreator.R
import com.example.lcdcustomcharactercreator.ui.components.ActionUiDialog
import com.example.lcdcustomcharactercreator.ui.components.AdaptiveUiBox
import com.example.lcdcustomcharactercreator.ui.components.CharacterPixelsUiInputPanel
import com.example.lcdcustomcharactercreator.ui.components.SavedPatternUiItem
import com.example.lcdcustomcharactercreator.ui.components.SelectedUiPixelsViewPanel
import com.example.lcdcustomcharactercreator.ui.components.SquaredUiButton
import com.example.lcdcustomcharactercreator.ui.theme.blueLcdColor
import com.example.lcdcustomcharactercreator.ui.theme.greenLcdColor
import com.example.lcdcustomcharactercreator.ui.viewmodels.AppState
import com.example.lcdcustomcharactercreator.ui.viewmodels.SavedPatternViewModel
import com.example.lcdcustomcharactercreator.utils.ClipBoardManager
import com.example.lcdcustomcharactercreator.utils.SourceCodeGenerator
import com.example.lcdcustomcharactercreator.utils.Toaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.BitSet

/**Creates app main screen.*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(
    appState: AppState = viewModel(),
    savedPatternViewModel: SavedPatternViewModel = hiltViewModel()
) {
    val context = LocalContext.current // context
    val configuration = LocalConfiguration.current // configuration

    // utils
    val sourceCodeGenerator = remember { SourceCodeGenerator() }
    val toaster = remember { Toaster(context) }
    val clipBoardManager = remember { ClipBoardManager(context) }

    // states
    val pixelsMap by appState.selectedPixelsMap.collectAsState()
    val sourceCode by appState.generatedSourceCodeState.collectAsState()
    val binaryOrHexType by appState.binaryOrHexType.collectAsState()
    val savedPatterns by savedPatternViewModel.allSavedPatterns.collectAsState()

    val orientation = configuration.orientation // current screen orientation

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // modal drawer
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // displays saved patterns
            ModalDrawerSheet {
                Column(
                    modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        itemsIndexed(
                            items = savedPatterns
                        ) { index, pattern ->
                            SavedPatternUiItem(
                                name = pattern.name,
                                description = pattern.description,
                                creationDatetime = pattern.creationDate,
                                onClick = {
                                    val encodedPixelsMap = BitSet.valueOf(Base64.decode(pattern.source, Base64.DEFAULT))
                                    appState.setPixelsMap(encodedPixelsMap)
                                    coroutineScope.launch { drawerState.close() } // close drawer sheet
                                },
                                onDelete = {
                                    savedPatternViewModel.deletePatternById(pattern.id)
                                    coroutineScope.launch {
                                        delay(1000) // delay 1000 ms
                                        drawerState.close()
                                    } // close drawer sheet
                                }
                            )

                            if (index < savedPatterns.lastIndex) HorizontalDivider() // set divider
                        }
                    }

                    // delete all button
                    SquaredUiButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (savedPatterns.isNotEmpty()) savedPatternViewModel.deleteAllPatterns()
                            else toaster.showToast("nothing to delete!")
                        },
                        icon = painterResource(R.drawable.outline_delete_24)
                    ) { Text(text = "delete all") }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.app_name), // app name
                            modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE)
                        )
                    },
                    actions = {
                        Box {
                            // menu button
                            IconButton(onClick = { appState.updateDropDownMenuState(true) }) {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_more_vert_24),
                                    contentDescription = null
                                )
                            }

                            // dropdown menu
                            DropdownMenu(
                                expanded = appState.dropDownMenuState,
                                onDismissRequest = { appState.updateDropDownMenuState(false) }
                            ) {
                                DropdownMenuItem(
                                    onClick = {
                                        appState.updateSavePatternDialogState(true)
                                        appState.updateDropDownMenuState(false)
                                    },
                                    text = {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.baseline_save_24),
                                                contentDescription = null
                                            )

                                            Text(text = "save")
                                        }
                                    }
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { innerPadding ->
            // save pattern dialog
            ActionUiDialog(
                state = appState.savePatternDialogState,
                onDismissRequestFunction = {
                    appState.updateSavePatternDialogState(false) // close dialog
                },
                titleIcon = painterResource(R.drawable.baseline_save_24),
                titleText = "Save pattern"
            ) {
                // pattern name input field
                OutlinedTextField(
                    value = appState.saveableNameOfPattern,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = { value -> appState.updateSaveableNameOfPatternState(value) },
                    placeholder = { Text(text = "enter pattern name...") },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { appState.updateSaveableNameOfPatternState("") }) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_clear_24),
                                contentDescription = null
                            )
                        }
                    }
                )

                // pattern name input field
                OutlinedTextField(
                    value = appState.saveableDescriptionOfPattern,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = { value -> appState.updateSaveableDescriptionOfPattern(value) },
                    placeholder = { Text(text = "enter pattern description... (optional)") },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { appState.updateSaveableDescriptionOfPattern("") }) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_clear_24),
                                contentDescription = null
                            )
                        }
                    }
                )

                // save pattern button
                SquaredUiButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val patternSource = Base64.encodeToString(pixelsMap.toByteArray(), Base64.DEFAULT)

                        savedPatternViewModel.addPattern(
                            appState.saveableNameOfPattern,
                            if (appState.saveableDescriptionOfPattern.isEmpty()) null
                            else appState.saveableDescriptionOfPattern, // pattern description (optional)
                            patternSource,
                            appState.isBlueDisplayState
                        )

                        appState.apply {
                            updateSavePatternDialogState(false) // close dialog
                            updateSaveableDescriptionOfPattern("")
                            updateSaveableNameOfPatternState("")
                        }
                    },
                    icon = painterResource(R.drawable.baseline_save_24)
                ) { Text(text = "save") }
            }

            // edit pattern name dialog
            ActionUiDialog(
                state = appState.editPatternNameDialogState,
                onDismissRequestFunction = {
                    // check pattern name
                    if (appState.patternName.isNotEmpty()) appState.updateEditPatternNameDialogState(false)
                    else toaster.showToast("⚠️Name is empty!")
                },
                titleIcon = painterResource(R.drawable.baseline_edit_24),
                titleText = "Edit pattern name"
            ) {
                // pattern name input field
                OutlinedTextField(
                    value = appState.patternName,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = { value -> appState.updatePatternName(value) },
                    placeholder = { Text(text = "enter pattern name...") },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { appState.updatePatternName("") }) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_clear_24),
                                contentDescription = null
                            )
                        }
                    }
                )

                // dismiss button
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.weight(1f))
                    SquaredUiButton(
                        onClick = {
                            // check pattern name
                            if (appState.patternName.isNotEmpty()) appState.updateEditPatternNameDialogState(false)
                            else toaster.showToast("⚠️Name is empty!")
                        }
                    ) { Text(text = "Ok") }
                }
            }

            // pattern source code dialog
            ActionUiDialog(
                state = appState.sourceCodeDialogState,
                onDismissRequestFunction = { appState.updateSourceCodeDialogState(false) },
                titleIcon = painterResource(R.drawable.baseline_code_24),
                titleText = "Source code of pattern ${appState.patternName}"
            ) {
                val dataType by appState.dataType.collectAsState()

                // update data type and generate source code by data type when binaryOrHexType state changed
                LaunchedEffect(binaryOrHexType) {
                    // generate pattern's source code by data type mode
                    val code = withContext(Dispatchers.Default) {
                        when (dataType) {
                            "binary" -> sourceCodeGenerator.generateSourceCppByteArrayCode(pixelsMap, appState.patternName, "binary") // binary
                            "hex" -> sourceCodeGenerator.generateSourceCppByteArrayCode(pixelsMap, appState.patternName, "hex") // hexadecimal
                            else -> sourceCodeGenerator.generateSourceCppByteArrayCode(pixelsMap, appState.patternName, "binary") // (default) binary
                        }
                    }
                    appState.setGeneratedSourceCode(code) // set source code
                    delay(10) // delay 10 ms
                }

                // scrollable code view
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.Black,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .height(200.dp)
                ) {
                    val verticalScrollState = rememberScrollState()
                    Text(
                        text = sourceCode,
                        modifier = Modifier
                            .padding(10.dp)
                            .verticalScroll(verticalScrollState)
                    )
                }

                // data type selection row
                Row(modifier = Modifier.fillMaxWidth()) {
                    // binary type
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = binaryOrHexType.first,
                            onCheckedChange = { state -> appState.selectDataType(1) } // update data type state when state equals false
                        )
                        Text(
                            text = "Binary",
                            modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE)
                        )
                    }

                    // hex type
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = binaryOrHexType.second,
                            onCheckedChange = { state -> appState.selectDataType(2) } // update data type state when state equals false
                        )
                        Text(
                            text = "Hex",
                            modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE)
                        )
                    }
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    // copy source code button
                    SquaredUiButton(
                        onClick = { clipBoardManager.setTextToClipboard(sourceCode) },
                        icon = painterResource(R.drawable.baseline_content_copy_24)
                    ) { Text(text = "copy code") }

                    Spacer(modifier = Modifier.weight(1f))

                    // close dialog button
                    SquaredUiButton(onClick = { appState.updateSourceCodeDialogState(false) }) {
                        Text(text = "close")
                    }
                }
            }

            AdaptiveUiBox(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Box(
                    modifier =
                        if (orientation == Configuration.ORIENTATION_PORTRAIT) Modifier.fillMaxWidth().padding(top = 10.dp)
                        else Modifier.fillMaxHeight().padding(start = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                SquaredUiButton(
                                    onClick = { appState.updateIsBlueDisplayState(true) },
                                    modifier = Modifier.width(135.dp)
                                ) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .background(color = blueLcdColor)
                                                .align(Alignment.CenterVertically)
                                        )
                                        Text(
                                            text = "blue LCD",
                                            modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE)
                                        )
                                    }
                                }

                                SquaredUiButton(
                                    onClick = { appState.updateIsBlueDisplayState(false) },
                                    modifier = Modifier.width(135.dp)
                                ) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .background(color = greenLcdColor)
                                                .align(Alignment.CenterVertically)
                                        )
                                        Text(
                                            text = "green LCD",
                                            modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE)
                                        )
                                    }
                                }

                                SquaredUiButton(
                                    onClick = {
                                        // check pattern
                                        if (appState.isPixelsSelected()) appState.updateSourceCodeDialogState(true)
                                        else toaster.showToast("⚠️Empty pattern!")
                                    },
                                    icon = painterResource(R.drawable.baseline_code_24),
                                    modifier = Modifier.width(135.dp)
                                ) {
                                    Text(
                                        text = "source code",
                                        modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE)
                                    )
                                }

                                Text(
                                    text = "${appState.getActivePixels()} active pixels",
                                    fontWeight = FontWeight.Light
                                )
                            }

                            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                                SelectedUiPixelsViewPanel(
                                    pixelsMap = pixelsMap,
                                    isDisplayBlue = appState.isBlueDisplayState
                                )

                                Text(
                                    text = appState.patternName,
                                    fontWeight = FontWeight.Light,
                                    modifier = Modifier
                                        .clickable(onClick = { appState.updateEditPatternNameDialogState(true) })
                                        .width(100.dp)
                                        .basicMarquee(iterations = Int.MAX_VALUE)
                                )
                            }
                        }

                        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                            Row(
                                modifier = Modifier.width(250.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                SquaredUiButton(
                                    onClick = {
                                        appState.clearSelectedPixelsMap()
                                        toaster.showToast("Pattern cleared!")
                                    },
                                    modifier = Modifier.weight(1f),
                                    icon = painterResource(R.drawable.baseline_clear_24)
                                ) { Text(text = "clear") }

                                SquaredUiButton(
                                    onClick = { appState.invertPixelsMap() },
                                    modifier = Modifier.weight(1f),
                                    icon = painterResource(R.drawable.baseline_invert_colors_24)
                                ) { Text(text = "invert") }
                            }
                    }
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        // pattern input panel
                        CharacterPixelsUiInputPanel(
                            pixelsMap = pixelsMap,
                            updatePixelStateByIndex = appState::updateSelectedPixelsMap
                        )

                        if (orientation == Configuration.ORIENTATION_PORTRAIT)
                        // modify pattern buttons
                            Row(
                                modifier = Modifier.width(250.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // clear pattern button
                                SquaredUiButton(
                                    onClick = {
                                        appState.clearSelectedPixelsMap()
                                        toaster.showToast("Pattern cleared!")
                                    },
                                    modifier = Modifier.weight(1f),
                                    icon = painterResource(R.drawable.baseline_clear_24)
                                ) { Text(text = "clear") }

                                // invert pixels button
                                SquaredUiButton(
                                    onClick = { appState.invertPixelsMap() },
                                    modifier = Modifier.weight(1f),
                                    icon = painterResource(R.drawable.baseline_invert_colors_24)
                                ) { Text(text = "invert") }
                            }
                    }
                }
            }
        }
    }
}