package com.example.ui.screens

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.viewmodel.ApiConfig
import com.example.ui.viewmodel.AppFile
import com.example.ui.viewmodel.AppState
import com.example.ui.viewmodel.MainViewModel

data class EditorThemeColors(
    val mainBg: Color,
    val toolbarBg: Color,
    val sidebarBg: Color,
    val activeLineColor: Color,
    val accentColor: Color,
    val textDefaultColor: Color,
    val isDark: Boolean = true
)

fun getThemeColors(themeName: String): EditorThemeColors {
    return when (themeName) {
        "Solarized Ocean" -> EditorThemeColors(
            mainBg = Color(0xFF002B36),
            toolbarBg = Color(0xFF073642),
            sidebarBg = Color(0xFF001F27),
            activeLineColor = Color(0xFF073642),
            accentColor = Color(0xFF2AA198),
            textDefaultColor = Color(0xFF839496)
        )
        "Neon Abyss" -> EditorThemeColors(
            mainBg = Color(0xFF0A0F1D),
            toolbarBg = Color(0xFF14192F),
            sidebarBg = Color(0xFF050811),
            activeLineColor = Color(0xFF1A223F),
            accentColor = Color(0xFF00E5FF),
            textDefaultColor = Color(0xFFD3D7E6)
        )
        "Cyber Monokai" -> EditorThemeColors(
            mainBg = Color(0xFF272822),
            toolbarBg = Color(0xFF1E1F1C),
            sidebarBg = Color(0xFF141411),
            activeLineColor = Color(0xFF3E3D32),
            accentColor = Color(0xFFF92672),
            textDefaultColor = Color(0xFFF8F8F2)
        )
        else -> EditorThemeColors( // "Midnight Aura"
            mainBg = Color(0xFF1E1E1E),
            toolbarBg = Color(0xFF2D2D2D),
            sidebarBg = Color(0xFF252526),
            activeLineColor = Color(0xFF3E3E42),
            accentColor = Color(0xFF0E639C),
            textDefaultColor = Color(0xFFD4D4D4)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val themeColors = remember(state.selectedTheme) { getThemeColors(state.selectedTheme) }
    val loc = remember(state.selectedLanguage) { getStrings(state.selectedLanguage) }

    CompositionLocalProvider(LocalLayoutDirection provides loc.layoutDirection) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = loc.appName.uppercase(),
                            color = themeColors.textDefaultColor,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            letterSpacing = 2.sp
                        )
                    },
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.Default.Terminal,
                            contentDescription = "Logo",
                            tint = themeColors.accentColor,
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .size(24.dp)
                        )
                    },
                    actions = {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = themeColors.accentColor.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, themeColors.accentColor.copy(alpha = 0.4f)),
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(if (state.isAgentTyping) Color(0xFFFFC107) else Color(0xFF4CAF50), CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = state.activeAgentRole.split(" ").first().uppercase(),
                                    color = themeColors.accentColor,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = themeColors.sidebarBg,
                        titleContentColor = themeColors.textDefaultColor
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = themeColors.sidebarBg,
                    contentColor = themeColors.textDefaultColor,
                    windowInsets = WindowInsets(0.dp)
                ) {
                    val tabs = listOf(
                        Triple(0, loc.explorerTab, Icons.Default.Folder),
                        Triple(1, loc.editorTab, Icons.Default.Edit),
                        Triple(2, loc.gitTab, Icons.AutoMirrored.Filled.CallMerge),
                        Triple(3, loc.agentTab, Icons.Default.AutoAwesome),
                        Triple(4, loc.settingsTab, Icons.Default.Settings)
                    )
                    
                    tabs.forEach { (index, label, icon) ->
                        val isSelected = state.currentTab == index
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.setTab(index) },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    modifier = Modifier.size(if (isSelected) 26.dp else 22.dp)
                                )
                            },
                            label = { 
                                Text(
                                    text = label, 
                                    fontSize = 10.sp, 
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                ) 
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = themeColors.accentColor,
                                selectedTextColor = themeColors.accentColor,
                                unselectedIconColor = themeColors.textDefaultColor.copy(alpha = 0.6f),
                                unselectedTextColor = themeColors.textDefaultColor.copy(alpha = 0.6f),
                                indicatorColor = themeColors.accentColor.copy(alpha = 0.15f)
                            )
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(themeColors.mainBg)
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues)
                    .windowInsetsPadding(WindowInsets.ime)
            ) {
                // Crossfade for smooth tab transitions
                Crossfade(targetState = state.currentTab, label = "Tab Transition", animationSpec = tween(300)) { tab ->
                    when (tab) {
                        0 -> ExplorerScreen(
                            files = state.files,
                            selectedIndex = state.selectedFileIndex,
                            onFileSelect = viewModel::selectFile,
                            onFileAdd = viewModel::addFile,
                            onFileDelete = viewModel::deleteFile,
                            themeName = state.selectedTheme,
                            selectedLanguage = state.selectedLanguage,
                            onAiAction = { prompt ->
                                viewModel.sendChatMessage(prompt)
                                viewModel.setTab(3)
                            }
                        )
                        1 -> if (state.files.isNotEmpty()) {
                            EditorScreen(
                                file = state.files[state.selectedFileIndex],
                                onCodeChange = viewModel::updateCode,
                                onAiClick = { action ->
                                    viewModel.sendContextualMessage(action, state.files[state.selectedFileIndex])
                                    viewModel.setTab(3)
                                },
                                onRunClick = { viewModel.runCode(state.files[state.selectedFileIndex]) },
                                terminalOutput = state.terminalOutput,
                                isTerminalVisible = state.isTerminalVisible,
                                onCloseTerminal = viewModel::closeTerminal,
                                allFiles = state.files,
                                onFileSelect = viewModel::selectFileByName,
                                fontSize = state.editorFontSize,
                                tabSize = state.tabSize,
                                themeName = state.selectedTheme,
                                selectedLanguage = state.selectedLanguage
                            )
                        } else {
                            EmptyStateScreen(themeColors = themeColors, message = loc.noFileSelected, icon = Icons.Default.Description)
                        }
                        2 -> GitScreen(viewModel)
                        3 -> AgentScreen(
                            state = state,
                            onSendMessage = viewModel::sendChatMessage,
                            onSelectFile = viewModel::selectFileByName,
                            onRunFile = viewModel::runCodeByName,
                            onRoleChange = viewModel::updateAgentRole,
                            onClearChat = viewModel::clearChat
                        )
                        4 -> SettingsScreen(
                            apiConfig = state.apiConfig,
                            onConfigChange = viewModel::updateApiConfig,
                            selectedTheme = state.selectedTheme,
                            onThemeChange = viewModel::updateTheme,
                            fontSize = state.editorFontSize,
                            onFontSizeChange = viewModel::updateFontSize,
                            tabSize = state.tabSize,
                            onTabSizeChange = viewModel::updateTabSize,
                            selectedLanguage = state.selectedLanguage,
                            onLanguageChange = viewModel::updateLanguage
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyStateScreen(themeColors: EditorThemeColors, message: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(
        modifier = Modifier.fillMaxSize().background(themeColors.mainBg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = themeColors.textDefaultColor.copy(alpha = 0.2f),
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            color = themeColors.textDefaultColor.copy(alpha = 0.5f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ExplorerScreen(
    files: List<AppFile>,
    selectedIndex: Int,
    onFileSelect: (Int) -> Unit,
    onFileAdd: (String) -> Unit,
    onFileDelete: (Int) -> Unit,
    themeName: String,
    selectedLanguage: String,
    onAiAction: (String) -> Unit
) {
    val themeColors = remember(themeName) { getThemeColors(themeName) }
    val loc = remember(selectedLanguage) { getStrings(selectedLanguage) }
    var showAddDialog by remember { mutableStateOf(false) }
    var newFileName by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().background(themeColors.mainBg).padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = loc.workspaceExplorerTitle,
                    color = themeColors.accentColor,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                IconButton(
                    onClick = { onAiAction("Analyze all workspace files for structural integrity and modularity.") },
                    modifier = Modifier.size(36.dp).background(themeColors.sidebarBg, CircleShape)
                ) {
                    Icon(Icons.Default.Insights, contentDescription = "Scan", tint = themeColors.accentColor, modifier = Modifier.size(18.dp))
                }
            }

            if (files.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.FolderOpen, contentDescription = null, tint = themeColors.textDefaultColor.copy(alpha = 0.3f), modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(loc.emptyWorkspace, color = themeColors.textDefaultColor.copy(alpha = 0.5f))
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    itemsIndexed(files) { index, file ->
                        val isSelected = index == selectedIndex
                        Surface(
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).clickable { onFileSelect(index) },
                            color = if (isSelected) themeColors.accentColor.copy(alpha = 0.15f) else themeColors.sidebarBg,
                            border = if (isSelected) BorderStroke(1.dp, themeColors.accentColor.copy(alpha = 0.5f)) else null
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val ext = file.name.substringAfterLast('.').lowercase()
                                val iconColor = when (ext) {
                                    "kt", "java" -> Color(0xFF7986CB)
                                    "html", "css" -> Color(0xFFE44D26)
                                    "js", "ts" -> Color(0xFFFFCA28)
                                    "py" -> Color(0xFF4B8BBE)
                                    else -> themeColors.textDefaultColor.copy(alpha = 0.7f)
                                }
                                Icon(Icons.Default.Code, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = file.name,
                                    color = if (isSelected) themeColors.accentColor else themeColors.textDefaultColor,
                                    fontSize = 16.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(
                                    onClick = {
                                        val prompt = if (loc.layoutDirection == LayoutDirection.Rtl)
                                            "ارفع الملف '${file.name}' على المضيف."
                                        else "Upload file '${file.name}' to the configured remote repository/platform."
                                        onAiAction(prompt)
                                    },
                                    modifier = Modifier.size(32.dp).background(Color.Black.copy(alpha = 0.2f), CircleShape)
                                ) {
                                    Icon(Icons.Default.CloudUpload, contentDescription = "Deploy", tint = themeColors.accentColor, modifier = Modifier.size(16.dp))
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(
                                    onClick = { onFileDelete(index) },
                                    modifier = Modifier.size(32.dp).background(Color.Black.copy(alpha = 0.2f), CircleShape)
                                ) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Color(0xFFE53935), modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
        
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp),
            containerColor = themeColors.accentColor,
            contentColor = Color.White
        ) {
            Icon(Icons.Default.Add, contentDescription = loc.createFile)
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text(loc.createFile, color = themeColors.textDefaultColor, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = newFileName,
                    onValueChange = { newFileName = it },
                    placeholder = { Text(loc.enterFileName, color = themeColors.textDefaultColor.copy(alpha = 0.5f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = themeColors.textDefaultColor,
                        unfocusedTextColor = themeColors.textDefaultColor,
                        focusedBorderColor = themeColors.accentColor,
                        unfocusedBorderColor = themeColors.activeLineColor,
                        focusedContainerColor = themeColors.sidebarBg,
                        unfocusedContainerColor = themeColors.sidebarBg
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            },
            containerColor = themeColors.sidebarBg,
            shape = RoundedCornerShape(16.dp),
            confirmButton = {
                Button(
                    onClick = {
                        if (newFileName.isNotBlank()) onFileAdd(newFileName)
                        showAddDialog = false
                        newFileName = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = themeColors.accentColor)
                ) {
                    Text(loc.createFile, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text(loc.cancel, color = themeColors.textDefaultColor)
                }
            }
        )
    }
}

@Composable
fun EditorScreen(
    file: AppFile,
    onCodeChange: (String) -> Unit,
    onAiClick: (String) -> Unit,
    onRunClick: () -> Unit,
    terminalOutput: String,
    isTerminalVisible: Boolean,
    onCloseTerminal: () -> Unit,
    allFiles: List<AppFile>,
    onFileSelect: (String) -> Unit,
    fontSize: Int,
    tabSize: Int,
    themeName: String,
    selectedLanguage: String
) {
    val themeColors = remember(themeName) { getThemeColors(themeName) }
    val loc = remember(selectedLanguage) { getStrings(selectedLanguage) }
    var textFieldValue by remember(file.name) { mutableStateOf(TextFieldValue(file.content)) }
    LaunchedEffect(file.content) {
        if (textFieldValue.text != file.content) {
            val safeStart = textFieldValue.selection.start.coerceIn(0, file.content.length)
            val safeEnd = textFieldValue.selection.end.coerceIn(0, file.content.length)
            textFieldValue = TextFieldValue(text = file.content, selection = TextRange(safeStart, safeEnd))
        }
    }

    var showSearchBar by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var replaceText by remember { mutableStateOf("") }
    val extension = file.name.substringAfterLast('.', "")
    val editorScrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize().background(themeColors.mainBg)) {
        // Advanced Horizontal Tabs
        LazyRow(
            modifier = Modifier.fillMaxWidth().background(themeColors.sidebarBg),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(allFiles) { f ->
                val isSelected = f.name == file.name
                Surface(
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable { if (!isSelected) onFileSelect(f.name) },
                    color = if (isSelected) themeColors.activeLineColor else Color.Transparent
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val iconTint = if (isSelected) themeColors.accentColor else themeColors.textDefaultColor.copy(alpha = 0.6f)
                        Icon(Icons.Default.Description, contentDescription = null, tint = iconTint, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = f.name,
                            color = if (isSelected) themeColors.textDefaultColor else themeColors.textDefaultColor.copy(alpha = 0.6f),
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        // Action Toolbar
        Row(
            modifier = Modifier.fillMaxWidth().background(themeColors.mainBg).padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(file.name, color = themeColors.accentColor, fontSize = 14.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            
            val actionModifier = Modifier.size(36.dp).background(themeColors.sidebarBg, RoundedCornerShape(8.dp))
            IconButton(onClick = { showSearchBar = !showSearchBar }, modifier = actionModifier) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = if (showSearchBar) themeColors.accentColor else Color.Gray, modifier = Modifier.size(18.dp))
            }
            IconButton(onClick = onRunClick, modifier = actionModifier) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Run", tint = Color(0xFF4CAF50), modifier = Modifier.size(20.dp))
            }
            IconButton(onClick = { onAiClick("Refactor this file code to premium algorithms standards.") }, modifier = actionModifier) {
                Icon(Icons.Default.AutoAwesome, contentDescription = "Refactor", tint = Color.Gray, modifier = Modifier.size(18.dp))
            }
            IconButton(onClick = { onAiClick("Analyze this file thoroughly for bugs, syntax errors, and fix them.") }, modifier = actionModifier) {
                Icon(Icons.Default.BugReport, contentDescription = "Fix Errors", tint = Color(0xFFFF5252), modifier = Modifier.size(18.dp))
            }
        }

        if (showSearchBar) {
            Column(modifier = Modifier.fillMaxWidth().background(themeColors.sidebarBg).padding(12.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        placeholder = { Text(loc.searchPlaceholder, color = Color.Gray, fontSize = 12.sp) },
                        modifier = Modifier.weight(1f).height(50.dp),
                        textStyle = TextStyle(color = themeColors.textDefaultColor, fontSize = 13.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = themeColors.textDefaultColor,
                            unfocusedTextColor = themeColors.textDefaultColor,
                            focusedBorderColor = themeColors.accentColor,
                            unfocusedBorderColor = themeColors.activeLineColor,
                            focusedContainerColor = themeColors.mainBg,
                            unfocusedContainerColor = themeColors.mainBg
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    OutlinedTextField(
                        value = replaceText,
                        onValueChange = { replaceText = it },
                        placeholder = { Text(loc.replacePlaceholder, color = Color.Gray, fontSize = 12.sp) },
                        modifier = Modifier.weight(1f).height(50.dp),
                        textStyle = TextStyle(color = themeColors.textDefaultColor, fontSize = 13.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = themeColors.textDefaultColor,
                            unfocusedTextColor = themeColors.textDefaultColor,
                            focusedBorderColor = themeColors.accentColor,
                            unfocusedBorderColor = themeColors.activeLineColor,
                            focusedContainerColor = themeColors.mainBg,
                            unfocusedContainerColor = themeColors.mainBg
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = { showSearchBar = false }) {
                        Text(loc.cancel, color = Color.Gray, fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (searchText.isNotEmpty()) {
                                val newText = textFieldValue.text.replace(searchText, replaceText)
                                textFieldValue = TextFieldValue(newText, TextRange(newText.length))
                                onCodeChange(newText)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = themeColors.accentColor),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Replace All", color = Color.White, fontSize = 13.sp)
                    }
                }
            }
        }

        // Live Editor Workspace
        Row(modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(editorScrollState).padding(vertical = 12.dp)) {
            val lineNumbersText = remember(textFieldValue.text) {
                val count = textFieldValue.text.count { it == '\n' } + 1
                (1..count).joinToString("\n")
            }
            Text(
                text = lineNumbersText,
                color = themeColors.textDefaultColor.copy(alpha = 0.4f),
                fontFamily = FontFamily.Monospace,
                fontSize = fontSize.sp,
                lineHeight = (fontSize * 1.5).sp,
                textAlign = TextAlign.End,
                modifier = Modifier.width(48.dp).padding(end = 12.dp)
            )
            BasicTextField(
                value = textFieldValue,
                onValueChange = {
                    textFieldValue = it
                    onCodeChange(it.text)
                },
                modifier = Modifier.weight(1f).fillMaxWidth(),
                textStyle = TextStyle(
                    color = themeColors.textDefaultColor,
                    fontFamily = FontFamily.Monospace,
                    fontSize = fontSize.sp,
                    lineHeight = (fontSize * 1.5).sp
                ),
                cursorBrush = SolidColor(themeColors.accentColor),
                visualTransformation = CodeVisualTransformation(extension),
                decorationBox = { innerTextField -> innerTextField() }
            )
        }

        // Quick Symbol Insertion Bar
        LazyRow(
            modifier = Modifier.fillMaxWidth().background(themeColors.sidebarBg).padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                Surface(
                    color = themeColors.accentColor,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable {
                        onAiClick("Analyze this file's current context and provide a relevant code completion snippet.")
                    }
                ) {
                    Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("CODE GEN", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            val symbols = listOf("Tab", "{", "}", "(", ")", "[", "]", "<", ">", "=", "+", "-", "*", "/", ";", ":", "\"", "'")
            items(symbols) { sym ->
                Surface(
                    color = themeColors.mainBg,
                    border = BorderStroke(1.dp, themeColors.activeLineColor),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable {
                        val toInsert = if (sym == "Tab") " ".repeat(tabSize) else sym
                        val currentText = textFieldValue.text
                        val cursorOpt = textFieldValue.selection.start.coerceIn(0, currentText.length)
                        val newText = currentText.substring(0, cursorOpt) + toInsert + currentText.substring(cursorOpt)
                        val newCursor = cursorOpt + toInsert.length
                        textFieldValue = TextFieldValue(newText, TextRange(newCursor))
                        onCodeChange(newText)
                    }
                ) {
                    Text(text = sym, color = themeColors.textDefaultColor, fontSize = 14.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                }
            }
        }

        // Collapsible Terminal
        if (isTerminalVisible) {
            val isHtml = file.name.endsWith(".html") || file.name.endsWith(".htm")
            Column(modifier = Modifier.fillMaxWidth().weight(0.6f).background(themeColors.toolbarBg)) {
                Row(
                    modifier = Modifier.fillMaxWidth().background(themeColors.sidebarBg).padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isHtml) Icons.Default.Web else Icons.Default.Terminal,
                        contentDescription = null,
                        tint = themeColors.accentColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isHtml) "WEB PREVIEW" else "TERMINAL OUTPUT", color = themeColors.textDefaultColor, fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.weight(1f))
                    if (!isHtml) {
                        IconButton(onClick = {
                            onAiClick("I ran the code in ${file.name} using Piston console and got this terminal execution output:\n$terminalOutput\n\nPlease debug.")
                            onCloseTerminal()
                        }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.AutoFixHigh, contentDescription = "Fix", tint = themeColors.accentColor, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    IconButton(onClick = onCloseTerminal, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray, modifier = Modifier.size(16.dp))
                    }
                }
                
                if (isHtml) {
                    var htmlContent = file.content
                    allFiles.forEach { wkFile ->
                        if (wkFile.name.endsWith(".css")) {
                            htmlContent = htmlContent.replace("<link rel=\"stylesheet\" href=\"${wkFile.name}\">", "<style>\n${wkFile.content}\n</style>")
                        }
                        if (wkFile.name.endsWith(".js")) {
                            htmlContent = htmlContent.replace("<script src=\"${wkFile.name}\"></script>", "<script>\n${wkFile.content}\n</script>")
                        }
                    }
                    var debouncedHtmlContent by remember { mutableStateOf(htmlContent) }
                    LaunchedEffect(htmlContent) {
                        kotlinx.coroutines.delay(800)
                        debouncedHtmlContent = htmlContent
                    }
                    var webViewError by remember { mutableStateOf<String?>(null) }
                    
                    if (webViewError != null) {
                        Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                            Text("Preview Unavailable:\n$webViewError", color = Color(0xFFE57373), textAlign = TextAlign.Center)
                        }
                    } else {
                        AndroidView(
                            factory = { context ->
                                try {
                                    WebView(context).apply {
                                        settings.javaScriptEnabled = true
                                        settings.domStorageEnabled = true
                                        webViewClient = WebViewClient()
                                        loadDataWithBaseURL(null, debouncedHtmlContent, "text/html", "UTF-8", null)
                                    }
                                } catch (e: Throwable) {
                                    android.os.Handler(android.os.Looper.getMainLooper()).post {
                                        webViewError = e.message ?: "WebView missing on this emulator"
                                    }
                                    android.view.View(context)
                                }
                            },
                            update = { webView ->
                                try {
                                    if (webView is WebView) webView.loadDataWithBaseURL(null, debouncedHtmlContent, "text/html", "UTF-8", null)
                                } catch (e: Throwable) {
                                    android.os.Handler(android.os.Looper.getMainLooper()).post {
                                        webViewError = e.message ?: "WebView error"
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxSize().background(Color.White)
                        )
                    }
                } else {
                    Text(
                        text = terminalOutput.ifEmpty { "Executing..." },
                        color = Color(0xFFA6E22E),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())
                    )
                }
            }
        }
    }
}

sealed class BubbleSegment {
    data class Text(val content: String) : BubbleSegment()
    data class Action(val type: String, val fileName: String) : BubbleSegment()
}

fun parseMessageText(text: String): List<BubbleSegment> {
    val segments = mutableListOf<BubbleSegment>()
    val regex = Regex("🎯\\[(PATCHED|DELETED|EXECUTED|GIT):(.*?)\\]🎯")
    var lastIndex = 0
    regex.findAll(text).forEach { match ->
        if (match.range.first > lastIndex) segments.add(BubbleSegment.Text(text.substring(lastIndex, match.range.first)))
        segments.add(BubbleSegment.Action(match.groupValues[1], match.groupValues[2]))
        lastIndex = match.range.last + 1
    }
    if (lastIndex < text.length) segments.add(BubbleSegment.Text(text.substring(lastIndex)))
    return segments.ifEmpty { listOf(BubbleSegment.Text(text)) }
}

@Composable
fun AgentScreen(
    state: AppState,
    onSendMessage: (String) -> Unit,
    onSelectFile: (String) -> Unit,
    onRunFile: (String) -> Unit,
    onRoleChange: (String) -> Unit,
    onClearChat: () -> Unit
) {
    val themeColors = remember(state.selectedTheme) { getThemeColors(state.selectedTheme) }
    var inputText by remember { mutableStateOf("") }
    val chatScrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize().background(themeColors.mainBg)) {
        // Advanced Header
        Surface(
            color = themeColors.sidebarBg,
            shadowElevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(vertical = 12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = CircleShape, color = themeColors.accentColor.copy(alpha = 0.2f), modifier = Modifier.size(40.dp)) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = themeColors.accentColor, modifier = Modifier.padding(10.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("NADX SUPREME SUITE", color = themeColors.textDefaultColor, fontSize = 16.sp, fontWeight = FontWeight.Black)
                            Text(if (state.isAgentTyping) "Processing..." else "Ready for input", color = if (state.isAgentTyping) Color(0xFFFFC107) else Color.Gray, fontSize = 12.sp)
                        }
                    }
                    IconButton(onClick = onClearChat, modifier = Modifier.background(themeColors.mainBg, CircleShape)) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Clear", tint = Color(0xFFE53935))
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val agents = listOf(
                        Triple("General Core Agent", "General", Color(0xFFA172E9)),
                        Triple("Architect Pro", "Architect", Color(0xFF2196F3)),
                        Triple("Algorithm Wizard", "Algo Wizard", Color(0xFFFFC107)),
                        Triple("Debugger Specialist", "Debugger", Color(0xFFF44336))
                    )
                    items(agents) { (roleName, displayName, colorVal) ->
                        val isSelected = state.activeAgentRole == roleName
                        FilterChip(
                            selected = isSelected,
                            onClick = { onRoleChange(roleName) },
                            label = { Text(displayName, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = colorVal.copy(alpha = 0.2f),
                                selectedLabelColor = colorVal,
                                containerColor = themeColors.mainBg,
                                labelColor = themeColors.textDefaultColor.copy(alpha = 0.7f)
                            ),
                            border = FilterChipDefaults.filterChipBorder(enabled = true, selected = isSelected, borderColor = colorVal, disabledBorderColor = Color.Transparent)
                        )
                    }
                }
            }
        }

        // Chat View
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 16.dp).verticalScroll(chatScrollState),
            verticalArrangement = Arrangement.Bottom
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            state.chatMessages.forEach { msg ->
                ChatBubble(role = msg.role, text = msg.text, themeColors = themeColors, onSelectFile = onSelectFile, onRunFile = onRunFile)
                Spacer(modifier = Modifier.height(12.dp))
            }
            if (state.isAgentTyping) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp),
                        color = themeColors.sidebarBg,
                        border = BorderStroke(1.dp, themeColors.activeLineColor)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = themeColors.accentColor, strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = state.agentStatusPhase, color = themeColors.textDefaultColor, fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        // Input Area
        Surface(
            color = themeColors.sidebarBg,
            shadowElevation = 16.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Command active Agent bot...", color = Color.Gray, fontSize = 14.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = themeColors.mainBg,
                        unfocusedContainerColor = themeColors.mainBg,
                        focusedBorderColor = themeColors.accentColor,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = themeColors.textDefaultColor,
                        unfocusedTextColor = themeColors.textDefaultColor
                    ),
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 4
                )
                Spacer(modifier = Modifier.width(12.dp))
                FloatingActionButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            onSendMessage(inputText)
                            inputText = ""
                        }
                    },
                    containerColor = themeColors.accentColor,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                }
            }
        }
    }
}

@Composable
fun ChatBubble(
    role: String,
    text: String,
    themeColors: EditorThemeColors,
    onSelectFile: (String) -> Unit,
    onRunFile: (String) -> Unit
) {
    val isUser = role == "user"
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(if (isUser) 0.85f else 0.95f),
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            if (isUser) {
                Surface(
                    shape = RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp),
                    color = themeColors.accentColor
                ) {
                    Text(
                        text = text,
                        color = Color.White,
                        modifier = Modifier.padding(16.dp),
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    )
                }
            } else {
                val segments = remember(text) { parseMessageText(text) }
                segments.forEach { segment ->
                    when (segment) {
                        is BubbleSegment.Text -> {
                            if (segment.content.isNotBlank()) {
                                Surface(
                                    shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp),
                                    color = themeColors.sidebarBg,
                                    border = BorderStroke(1.dp, themeColors.activeLineColor)
                                ) {
                                    Text(
                                        text = segment.content.trim(),
                                        color = themeColors.textDefaultColor,
                                        modifier = Modifier.padding(16.dp),
                                        fontSize = 15.sp,
                                        lineHeight = 22.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                        is BubbleSegment.Action -> {
                            val cardBg = when (segment.type) {
                                "PATCHED" -> Color(0xFF1B2B20)
                                "DELETED" -> Color(0xFF2C1919)
                                "GIT" -> Color(0xFF261C10)
                                else -> themeColors.activeLineColor
                            }
                            val cardBorder = when (segment.type) {
                                "PATCHED" -> Color(0xFF4CAF50)
                                "DELETED" -> Color(0xFFE53935)
                                "GIT" -> Color(0xFFFF9800)
                                else -> themeColors.accentColor
                            }
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = cardBg,
                                border = BorderStroke(1.dp, cardBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(if (segment.type == "PATCHED") Icons.Default.CheckCircle else Icons.Default.Info, contentDescription = null, tint = cardBorder, modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("${segment.type}: ${segment.fileName}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    }
                                    if (segment.type != "DELETED" && segment.type != "GIT") {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                            Button(
                                                onClick = { onSelectFile(segment.fileName) },
                                                colors = ButtonDefaults.buttonColors(containerColor = cardBorder.copy(alpha = 0.2f), contentColor = cardBorder)
                                            ) {
                                                Text("Open", fontWeight = FontWeight.Bold)
                                            }
                                            if (segment.type == "PATCHED" && !segment.fileName.endsWith(".html") && !segment.fileName.endsWith(".css")) {
                                                Button(
                                                    onClick = { onRunFile(segment.fileName) },
                                                    colors = ButtonDefaults.buttonColors(containerColor = cardBorder, contentColor = Color.White)
                                                ) {
                                                    Text("Run Code")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    apiConfig: ApiConfig,
    onConfigChange: (ApiConfig) -> Unit,
    selectedTheme: String,
    onThemeChange: (String) -> Unit,
    fontSize: Int,
    onFontSizeChange: (Int) -> Unit,
    tabSize: Int,
    onTabSizeChange: (Int) -> Unit,
    selectedLanguage: String,
    onLanguageChange: (String) -> Unit
) {
    val themeColors = remember(selectedTheme) { getThemeColors(selectedTheme) }
    var config by remember(apiConfig) { mutableStateOf(apiConfig) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(themeColors.mainBg)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column {
            Text("PREFERENCES", color = themeColors.accentColor, fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Customize your IDE environment", color = themeColors.textDefaultColor, fontSize = 24.sp, fontWeight = FontWeight.Black)
        }

        // Themes
        Surface(color = themeColors.sidebarBg, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Editor Theme", color = themeColors.textDefaultColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    val themes = listOf(
                        Pair("Midnight Aura", Color(0xFF1E1E1E)),
                        Pair("Solarized Ocean", Color(0xFF002B36)),
                        Pair("Neon Abyss", Color(0xFF0A0F1D)),
                        Pair("Cyber Monokai", Color(0xFF272822))
                    )
                    items(themes) { (tName, bg) ->
                        val isActive = selectedTheme == tName
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onThemeChange(tName) }) {
                            Box(modifier = Modifier.size(60.dp).background(bg, CircleShape).border(3.dp, if (isActive) themeColors.accentColor else Color.Transparent, CircleShape))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(tName, color = if (isActive) themeColors.textDefaultColor else Color.Gray, fontSize = 12.sp, fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
            }
        }

        // Font and Indent Settings
        Surface(color = themeColors.sidebarBg, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Typography & Format", color = themeColors.textDefaultColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Font Size", color = themeColors.textDefaultColor, fontSize = 14.sp)
                    Text("${fontSize}sp", color = themeColors.accentColor, fontWeight = FontWeight.Bold)
                }
                Slider(value = fontSize.toFloat(), onValueChange = { onFontSizeChange(it.toInt()) }, valueRange = 12f..24f, colors = SliderDefaults.colors(thumbColor = themeColors.accentColor, activeTrackColor = themeColors.accentColor))

                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Tab Indent Size", color = themeColors.textDefaultColor, fontSize = 14.sp)
                    SegmentedButton(options = listOf("2", "4"), selectedOption = tabSize.toString(), onOptionSelect = { onTabSizeChange(it.toInt()) }, themeColors = themeColors)
                }
            }
        }

        // Language
        Surface(color = themeColors.sidebarBg, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Display Language", color = themeColors.textDefaultColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                val langs = listOf("English", "العربية", "Español", "Français")
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    langs.forEach { lang ->
                        val isSelected = selectedLanguage == lang
                        FilterChip(
                            selected = isSelected,
                            onClick = { onLanguageChange(lang) },
                            label = { Text(lang) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = themeColors.accentColor, selectedLabelColor = Color.White, containerColor = themeColors.mainBg, labelColor = themeColors.textDefaultColor)
                        )
                    }
                }
            }
        }

        // API settings
        Surface(color = themeColors.sidebarBg, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("AI Provider Configuration", color = themeColors.textDefaultColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = config.apiKey,
                    onValueChange = { config = config.copy(apiKey = it) },
                    label = { Text("API Key") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = themeColors.mainBg,
                        unfocusedContainerColor = themeColors.mainBg,
                        focusedBorderColor = themeColors.accentColor,
                        unfocusedBorderColor = themeColors.activeLineColor,
                        focusedTextColor = themeColors.textDefaultColor,
                        unfocusedTextColor = themeColors.textDefaultColor
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { onConfigChange(config) },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = themeColors.accentColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("SAVE CONFIGURATION", color = Color.White, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun SegmentedButton(options: List<String>, selectedOption: String, onOptionSelect: (String) -> Unit, themeColors: EditorThemeColors) {
    Row(modifier = Modifier.background(themeColors.mainBg, RoundedCornerShape(12.dp)).border(1.dp, themeColors.activeLineColor, RoundedCornerShape(12.dp)).padding(4.dp)) {
        options.forEach { option ->
            val isSelected = option == selectedOption
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) themeColors.accentColor else Color.Transparent)
                    .clickable { onOptionSelect(option) }
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(option, color = if (isSelected) Color.White else themeColors.textDefaultColor, fontWeight = FontWeight.Bold)
            }
        }
    }
}
