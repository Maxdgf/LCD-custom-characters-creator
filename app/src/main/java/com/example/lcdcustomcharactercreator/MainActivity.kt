package com.example.lcdcustomcharactercreator

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lcdcustomcharactercreator.ui.components.ActionUiDialog
import com.example.lcdcustomcharactercreator.ui.components.AdaptiveUiBox
import com.example.lcdcustomcharactercreator.ui.components.CharacterPixelsUiInputPanel
import com.example.lcdcustomcharactercreator.ui.components.SelectedUiPixelsViewPanel
import com.example.lcdcustomcharactercreator.ui.components.SquaredUiButton

import com.example.lcdcustomcharactercreator.ui.theme.LCDCustomCharacterCreatorTheme
import com.example.lcdcustomcharactercreator.ui.theme.blueLcdColor
import com.example.lcdcustomcharactercreator.ui.theme.greenLcdColor
import com.example.lcdcustomcharactercreator.ui.viewmodels.AppState
import com.example.lcdcustomcharactercreator.utils.ClipBoardManager
import com.example.lcdcustomcharactercreator.utils.SourceCodeGenerator
import com.example.lcdcustomcharactercreator.utils.Toaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LCDCustomCharacterCreatorTheme {
                MainScreen()
            }
        }
    }
}

/**
 * Creates check icon which visible by state
 * @param visibleState visible state.
 */
@Composable
private fun CheckIcon(visibleState: Boolean) {
    AnimatedVisibility(visible = visibleState) {
        Icon(
            painter = painterResource(R.drawable.baseline_check_24),
            tint = Color.Green, // green tint
            contentDescription = null
        )
    }
}

/**Creates app main screen.*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(appState: AppState = viewModel()) {
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

    val orientation = configuration.orientation // current screen orientation

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name), // app name
                        modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        // pattern source code dialog
        ActionUiDialog(
            state = appState.sourceCodeDialogState,
            onDismissRequestFunction = { appState.updateSourceCodeDialogState(false) },
            titleIcon = painterResource(R.drawable.baseline_code_24),
            titleText = "Source code of pattern"
        ) {
            val dataType by appState.dataType.collectAsState()

            // update data type and generate source code by data type when binaryOrHexType state changed
            LaunchedEffect(binaryOrHexType) {
                // generate pattern's source code by data type mode
                val code = withContext(Dispatchers.Default) {
                    when (dataType) {
                        "binary" -> sourceCodeGenerator.generateSourceCppByteArrayCode(pixelsMap, "binary") // binary
                        "hex" -> sourceCodeGenerator.generateSourceCppByteArrayCode(pixelsMap, "hex") // hexadecimal
                        else -> sourceCodeGenerator.generateSourceCppByteArrayCode(pixelsMap, "binary") // (default) binary
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
                    .height(180.dp)
            ) {
                val verticalScrollState = rememberScrollState() // vertical scroll state for source code view

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
                        onCheckedChange = { state -> appState.selectDataType("binary") } // update data type state when state equals false
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
                        onCheckedChange = { state -> appState.selectDataType("hex") } // update data type state when state equals false
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
                    onClick = { clipBoardManager.setTextToClipboard(sourceCode.text) },
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
                        Column(
                            modifier = Modifier.width(135.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // blue lcd skin button
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                CheckIcon(visibleState = appState.lcdPreviewSkinState.first)
                                SquaredUiButton(
                                    onClick = {
                                        appState.setLcdPreviewSkin("blue")
                                        toaster.showToast("blue lcd🔵")
                                    },
                                    modifier = Modifier.fillMaxWidth()
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
                            }

                            // green lcd skin button
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                CheckIcon(visibleState = appState.lcdPreviewSkinState.second)
                                SquaredUiButton(
                                    onClick = {
                                        appState.setLcdPreviewSkin("green")
                                        toaster.showToast("green lcd🟢")
                                    },
                                    modifier = Modifier.fillMaxWidth()
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
                            }

                            SquaredUiButton(
                                onClick = {
                                    // check pattern
                                    if (appState.isPixelsSelected()) appState.updateSourceCodeDialogState(true)
                                    else toaster.showToast("⚠️Empty pattern!")
                                },
                                icon = painterResource(R.drawable.baseline_code_24),
                                modifier = Modifier.fillMaxWidth()
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
                                lcdPreviewSkinState = appState.lcdPreviewSkinState
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