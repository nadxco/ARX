package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.viewmodel.AppFile
import com.example.ui.viewmodel.GitCommit
import com.example.ui.viewmodel.MainViewModel

@Composable
fun GitScreen(viewModel: MainViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val themeColors = remember(state.selectedTheme) { getThemeColors(state.selectedTheme) }
    val loc = remember(state.selectedLanguage) { getStrings(state.selectedLanguage) }

    var commitMessage by remember { mutableStateOf("") }
    var rawGitCommand by remember { mutableStateOf("") }
    var remoteUrlInput by remember { mutableStateOf(state.gitRemoteUrl) }
    
    var isStatusExpanded by remember { mutableStateOf(true) }
    var isDiffExpanded by remember { mutableStateOf(false) }
    var isHistoryExpanded by remember { mutableStateOf(false) }
    var isPushExpanded by remember { mutableStateOf(false) }
    var isShellExpanded by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(themeColors.mainBg)
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "VERSION CONTROLLER",
                    color = themeColors.accentColor,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.AutoMirrored.Filled.CallMerge,
                        contentDescription = "Branch",
                        tint = themeColors.textDefaultColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${loc.activeBranchLabel} ${state.currentBranch}",
                        color = themeColors.textDefaultColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                if (state.isGitLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = themeColors.accentColor,
                        strokeWidth = 2.dp
                    )
                }
                IconButton(
                    onClick = { viewModel.refreshGitStatus() },
                    modifier = Modifier.size(36.dp).background(themeColors.sidebarBg, CircleShape)
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = loc.refreshGitBtn,
                        tint = themeColors.accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Section 1: AI Git Assistant
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = themeColors.sidebarBg,
            border = BorderStroke(1.dp, Color(0xFFD08770).copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFFD08770), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(loc.aiGitAssistant, color = themeColors.textDefaultColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.background(Color(0xFFD08770).copy(alpha = 0.15f), RoundedCornerShape(8.dp))) {
                        Text("AI COPILOT", color = Color(0xFFD08770), fontSize = 10.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(loc.gitAssistantDesc, color = themeColors.textDefaultColor.copy(alpha = 0.7f), fontSize = 13.sp, lineHeight = 20.sp)

                if (state.aiSuggestedCommitMessage.isNotBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(
                        color = themeColors.mainBg,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, themeColors.activeLineColor)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text(loc.suggestedCommit, color = themeColors.textDefaultColor.copy(alpha = 0.6f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.Gray, modifier = Modifier.size(16.dp).clickable { viewModel.updateAiSuggestedCommitMessage("") })
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(state.aiSuggestedCommitMessage, color = themeColors.textDefaultColor, fontSize = 13.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    commitMessage = state.aiSuggestedCommitMessage
                                    viewModel.updateAiSuggestedCommitMessage("")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD08770)),
                                modifier = Modifier.align(Alignment.End).height(32.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp)
                            ) {
                                Text(loc.applyCommit, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilledTonalButton(
                        onClick = { viewModel.generateAiCommitMessage() },
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = themeColors.activeLineColor, contentColor = themeColors.textDefaultColor),
                        modifier = Modifier.weight(1f).height(44.dp)
                    ) {
                        Text(loc.draftMessage, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { viewModel.autoStageCommitPush() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD08770)),
                        modifier = Modifier.weight(1.5f).height(44.dp)
                    ) {
                        Text(loc.autoStagePush, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        
        // Stats
        Surface(color = themeColors.sidebarBg, shape = RoundedCornerShape(16.dp)) {
            Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                val totalFiles = remember(state.files) { state.files.size.toString() }
                val totalLines = remember(state.files) { state.files.sumOf { it.content.count { c -> c == '\n' } + 1 }.toString() }
                StatBox(label = loc.totalFilesLabel, value = totalFiles, accent = themeColors.accentColor, themeColors)
                Spacer(modifier = Modifier.width(16.dp))
                StatBox(label = loc.totalLinesLabel, value = totalLines, accent = themeColors.accentColor, themeColors)
            }
        }

        // Section 2: Stage & Status
        ExpandableCard("Status & Stage", Icons.AutoMirrored.Filled.List, isStatusExpanded, { isStatusExpanded = it }, themeColors) {
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { viewModel.stageAllFiles() }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = themeColors.accentColor)) {
                        Text("Stage All")
                    }
                    Button(onClick = { viewModel.unstageAllFiles() }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = themeColors.activeLineColor, contentColor = themeColors.textDefaultColor)) {
                        Text("Unstage All")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (state.files.isEmpty()) {
                    Text(loc.emptyWorkspace, color = Color.Gray, fontSize = 13.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                } else {
                    state.files.forEach { file ->
                        val isStaged = file.name in state.stagedFiles
                        val statusCode = state.porcelainStatuses[file.name] ?: "??"
                        val bg = if (isStaged) themeColors.accentColor.copy(alpha = 0.1f) else themeColors.mainBg
                        Surface(color = bg, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(file.name, color = themeColors.textDefaultColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(if (isStaged) "Staged" else "Unstaged ($statusCode)", color = if(isStaged) themeColors.accentColor else Color.Gray, fontSize = 11.sp)
                                }
                                IconButton(onClick = { if (isStaged) viewModel.unstageFile(file.name) else viewModel.stageFile(file.name) }) {
                                    Icon(
                                        imageVector = if (isStaged) Icons.Default.RemoveCircleOutline else Icons.Default.AddCircleOutline,
                                        contentDescription = null, tint = if (isStaged) Color(0xFFE53935) else themeColors.accentColor
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Section 3: Commit
        Surface(color = themeColors.sidebarBg, shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Commit, contentDescription = null, tint = themeColors.accentColor)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Commit Changes", color = themeColors.textDefaultColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = commitMessage,
                    onValueChange = { commitMessage = it },
                    placeholder = { Text(loc.commitMsgPlaceholder) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = themeColors.mainBg,
                        unfocusedContainerColor = themeColors.mainBg,
                        focusedBorderColor = themeColors.accentColor,
                        unfocusedBorderColor = themeColors.activeLineColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { if (commitMessage.isNotBlank()) { viewModel.commitStaged(commitMessage); commitMessage = "" } },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = themeColors.accentColor),
                    enabled = state.stagedFiles.isNotEmpty() && commitMessage.isNotBlank()
                ) {
                    Text("${loc.commitButton} (${state.stagedFiles.size})", fontWeight = FontWeight.Bold)
                }
            }
        }
        
        // Section 4: History & Remote sync
        ExpandableCard("Remote & Export", Icons.Default.CloudUpload, isPushExpanded, { isPushExpanded = it }, themeColors) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(value = state.gitUsername, onValueChange = { viewModel.updateGitUsername(it) }, label = { Text("Username") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = state.gitToken, onValueChange = { viewModel.updateGitToken(it) }, label = { Text("Token") }, modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation())
                OutlinedTextField(value = remoteUrlInput, onValueChange = { remoteUrlInput = it; viewModel.updateGitRemoteUrl(it) }, label = { Text("Remote Repo URL") }, modifier = Modifier.fillMaxWidth())
                Button(onClick = { viewModel.pushToRemote() }, modifier = Modifier.fillMaxWidth().height(48.dp), colors = ButtonDefaults.buttonColors(containerColor = themeColors.accentColor)) {
                    Text("Push logic to Repo")
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun ExpandableCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, expanded: Boolean, onExpandToggle: (Boolean) -> Unit, themeColors: EditorThemeColors, content: @Composable () -> Unit) {
    Surface(color = themeColors.sidebarBg, shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth().clickable { onExpandToggle(!expanded) }.padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(icon, contentDescription = null, tint = themeColors.accentColor)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(title, color = themeColors.textDefaultColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, contentDescription = null, tint = Color.Gray)
            }
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) { content() }
            }
        }
    }
}

@Composable
fun RowScope.StatBox(label: String, value: String, accent: Color, themeColors: EditorThemeColors) {
    Box(modifier = Modifier.weight(1f).background(themeColors.mainBg, RoundedCornerShape(12.dp)).padding(16.dp)) {
        Column {
            Text(text = label, color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, color = accent, fontSize = 24.sp, fontWeight = FontWeight.Black)
        }
    }
}

fun highlightGitDiff(text: String): androidx.compose.ui.text.AnnotatedString {
    return buildAnnotatedString {
        text.lines().forEach { line ->
            when {
                line.startsWith("+") && !line.startsWith("+++") -> withStyle(SpanStyle(color = Color(0xFF81C784), fontWeight = FontWeight.SemiBold)) { append(line) }
                line.startsWith("-") && !line.startsWith("---") -> withStyle(SpanStyle(color = Color(0xFFE57373), fontWeight = FontWeight.SemiBold)) { append(line) }
                line.startsWith("@@") -> withStyle(SpanStyle(color = Color(0xFF4FC3F7))) { append(line) }
                line.startsWith("diff ") -> withStyle(SpanStyle(color = Color(0xFFFFD54F), fontWeight = FontWeight.Bold)) { append(line) }
                line.startsWith("--- ") || line.startsWith("+++ ") -> withStyle(SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) { append(line) }
                else -> append(line)
            }
            append("\n")
        }
    }
}
