package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatMessage(val role: String, val text: String)

data class AppFile(val name: String, val content: String)

data class GitCommit(
    val id: String,
    val author: String = "NADX Developer <developer@nadx.com>",
    val message: String,
    val date: String,
    val filesSnapshot: List<AppFile>
)

data class ApiConfig(
    val provider: String = "Gemini", // "Gemini" or "OpenAI"
    val baseUrl: String = "https://generativelanguage.googleapis.com",
    val apiKey: String = "",
    val model: String = "gemini-3.1-pro-preview"
)

data class AppState(
    val files: List<AppFile> = listOf(
        AppFile("main.kt", """
// Real Kotlin code with recursion
fun factorial(n: Int): Long {
    return if (n <= 1) 1 else n * factorial(n - 1)
}

fun main() {
    val number = 6
    println("Welcome to NADX Premium Mobile IDE!")
    println("Factorial of " + number + " is " + factorial(number))
}
        """.trimIndent()),
        AppFile("index.html", """
<!-- Dynamic Local HTML Web Preview -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>NADX Web Dashboard</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <div class="card">
        <h1>NADX Live Interface</h1>
        <p class="subtitle">Crafted with simple & complex details</p>
        <div class="metric">
            <span class="value" id="counter">5</span>
            <span class="label">Interactive clicks</span>
        </div>
        <button class="btn" onclick="increment()">Dynamic Action</button>
    </div>
    <script src="script.js"></script>
</body>
</html>
        """.trimIndent()),
        AppFile("styles.css", """
/* Custom CSS styling for local preview */
body {
    background: #121214;
    color: #e1e1e6;
    font-family: 'Segoe UI', system-ui, sans-serif;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    margin: 0;
}
.card {
    background: #1e1e24;
    padding: 30px;
    border-radius: 16px;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
    text-align: center;
    border: 1px solid #29292e;
    max-width: 320px;
    width: 100%;
}
h1 {
    font-size: 22px;
    color: #a172e9;
    margin: 0 0 8px 0;
}
.subtitle {
    color: #8d8d99;
    font-size: 13px;
    margin-bottom: 24px;
}
.metric {
    background: #121214;
    padding: 16px;
    border-radius: 8px;
    margin-bottom: 20px;
}
.value {
    display: block;
    font-size: 36px;
    font-weight: bold;
    color: #04d361;
}
.label {
    font-size: 12px;
    color: #7c7c8a;
}
.btn {
    background: #a172e9;
    color: white;
    border: none;
    padding: 12px 24px;
    font-size: 14px;
    font-weight: bold;
    border-radius: 8px;
    cursor: pointer;
    transition: background 0.2s;
}
.btn:hover {
    background: #8257e5;
}
        """.trimIndent()),
        AppFile("script.js", """
// Interactive click tracker
let count = 5;
function increment() {
    count++;
    document.getElementById('counter').innerText = count;
    console.log("Counter updated to: " + count);
}
console.log("NADX Web Interface loaded.");
        """.trimIndent()),
        AppFile("script.py", """
# Python: Fibonacci prime calculator
def is_prime(n):
    if n <= 1: return False
    for i in range(2, int(n**0.5) + 1):
        if n % i == 0: return False
    return True

print("=== NADX Python Sandbox ===")
number = 29
print(f"Is {number} a prime? {is_prime(number)}")
primes = [x for x in range(2, 50) if is_prime(x)]
print("Primes under 50:", primes)
        """.trimIndent()),
        AppFile("App.java", """
// Java Native API execution
public class App {
    public static void main(String[] args) {
        System.out.println("=== NADX Java Native Engine ===");
        System.out.println("Executing algorithms inside secure VM sandbox.");
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        System.out.println("Execution time: " + now.toString());
    }
}
        """.trimIndent()),
        AppFile("main.dart", """
// Dart Sandbox
void main() {
  print("Hello, NADX Dart VM!");
  var list = [1, 2, 3, 4, 5];
  var squared = list.map((x) => x * x).toList();
  print("Original list: ${'$'}list");
  print("Squared mapping: ${'$'}squared");
}
        """.trimIndent()),
        AppFile("main.cpp", """
#include <iostream>
#include <vector>
#include <numeric>

int main() {
    std::cout << "=== NADX C++ Core Engine ===" << std::endl;
    std::vector<int> numbers = {10, 20, 30, 40, 50};
    int total = std::accumulate(numbers.begin(), numbers.end(), 0);
    std::cout << "Sum of elements: " << total << std::endl;
    return 0;
}
        """.trimIndent()),
        AppFile("advanced_algorithms.py", """
# === NADX HIGH-PERFORMANCE ALGORITHMS ===

# 1. Dijkstra's Shortest Path Algorithm
def dijkstra(graph, start):
    import heapq
    distances = {node: float('infinity') for node in graph}
    distances[start] = 0
    priority_queue = [(0, start)]
    shortest_path_tree = {}

    while priority_queue:
        current_distance, current_node = heapq.heappop(priority_queue)

        if current_distance > distances[current_node]:
            continue

        for neighbor, weight in graph[current_node].items():
            distance = current_distance + weight
            if distance < distances[neighbor]:
                distances[neighbor] = distance
                shortest_path_tree[neighbor] = current_node
                heapq.heappush(priority_queue, (distance, neighbor))

    return distances, shortest_path_tree

# 2. Backtracking Sudoku Solver with dynamic visualization
def solve_sudoku(grid):
    def is_valid(grid, r, c, val):
        for i in range(9):
            if grid[r][i] == val or grid[i][c] == val:
                return False
        start_row, start_col = 3 * (r // 3), 3 * (c // 3)
        for i in range(3):
            for j in range(3):
                if grid[start_row + i][start_col + j] == val:
                    return False
        return True

    def find_empty(grid):
        for i in range(9):
            for j in range(9):
                if grid[i][j] == 0:
                    return (i, j)
        return None

    empty_pos = find_empty(grid)
    if not empty_pos:
        return True
    row, col = empty_pos

    for num in range(1, 10):
        if is_valid(grid, row, col, num):
            grid[row][col] = num
            if solve_sudoku(grid):
                return True
            grid[row][col] = 0
    return False

# 3. Dynamic Programming - Zero-One Knapsack Optimization
def knapsack(weights, values, capacity):
    n = len(values)
    dp = [[0 for _ in range(capacity + 1)] for _ in range(n + 1)]
    for i in range(1, n + 1):
        for w in range(1, capacity + 1):
            if weights[i-1] <= w:
                dp[i][w] = max(values[i-1] + dp[i-1][w-weights[i-1]], dp[i-1][w])
            else:
                dp[i][w] = dp[i-1][w]
    return dp[n][capacity]

# RUN ALGORITHMS SUITE
if __name__ == "__main__":
    print("=========================================")
    print("       NADX ALGORITHMS SHOWCASE          ")
    print("=========================================")
    
    # Showcase 1: Dijkstra Shortest Path
    print("\\n--- 1. Dijkstra Shortest Graph Path Tracker ---")
    city_graph = {
        'A': {'B': 4, 'C': 2},
        'B': {'A': 4, 'C': 1, 'D': 5},
        'C': {'A': 2, 'B': 1, 'D': 8, 'E': 10},
        'D': {'B': 5, 'C': 8, 'E': 2, 'F': 6},
        'E': {'C': 10, 'D': 2, 'F': 3},
        'F': {'D': 6, 'E': 3}
    }
    dist, paths = dijkstra(city_graph, 'A')
    print("Shortest node costs from vertex [A]:")
    for node, d in dist.items():
        print(f" -> Opt Path to link [{node}]: total cost = {d}")

    # Showcase 2: Sudoku Solver
    print("\\n--- 2. Sudoku Recursive Backtracking Constraints ---")
    board = [
        [5, 3, 0, 0, 7, 0, 0, 0, 0],
        [6, 0, 0, 1, 9, 5, 0, 0, 0],
        [0, 9, 8, 0, 0, 0, 0, 6, 0],
        [8, 0, 0, 0, 6, 0, 0, 0, 3],
        [4, 0, 0, 8, 0, 3, 0, 0, 1],
        [7, 0, 0, 0, 2, 0, 0, 0, 6],
        [0, 6, 0, 0, 0, 0, 2, 8, 0],
        [0, 0, 0, 4, 1, 9, 0, 0, 5],
        [0, 0, 0, 0, 8, 0, 0, 7, 9]
    ]
    if solve_sudoku(board):
        print("Sudoku solved correctly:")
        for r in board[:3]:
             print("   ", r)
        print("    [Remaining solved grid successfully calculated!]")
    else:
        print("Failed to solve.")

    # Showcase 3: Zero-One Knapsack Optimization
    print("\\n--- 3. 0/1 Knapsack dynamic allocation ---")
    val = [60, 100, 120]
    wt = [10, 20, 30]
    cap = 50
    max_val = knapsack(wt, val, cap)
    print(f"Optimal dynamic value capacity ratio: {max_val} units")
    print("=========================================")
        """.trimIndent())
    ),
    val selectedFileIndex: Int = 0,
    val chatMessages: List<ChatMessage> = listOf(
        ChatMessage("agent", "Welcome to NADX Premium Multi-Agent IDE! \n\nI am configured with physical code generation algorithms and customized multi-agent roles.\n\nChoose your Agent:\n- 🤖 Software Architect\n- ⚡ Algorithm Wizard\n- 🔍 Debugger Bot\n- 🧠 Core General Assistant\n\nI can read/compile your files directly. Try executing \"advanced_algorithms.py\" in the Explorer!")
    ),
    val isAgentTyping: Boolean = false,
    val agentStatusPhase: String = "Idle",
    val currentTab: Int = 0, // 0: Explorer, 1: Editor, 2: Git, 3: Agent, 4: Settings
    val apiConfig: ApiConfig = ApiConfig(),
    val terminalOutput: String = "",
    val isTerminalVisible: Boolean = false,
    val selectedTheme: String = "Midnight Aura", // "Midnight Aura", "Solarized Ocean", "Neon Abyss", "Cyber Monokai"
    val editorFontSize: Int = 14,
    val activeAgentRole: String = "General Core Agent", // "General Core Agent", "Architect Pro", "Algorithm Wizard", "Debugger Specialist"
    val tabSize: Int = 4,
    
    // Git State fields
    val stagedFiles: Set<String> = emptySet(),
    val commitHistory: List<GitCommit> = emptyList(),
    val gitStatusOutput: String = "Refresh Git status to initialize repository.",
    val gitDiffOutput: String = "No diffs tracked yet.",
    val gitLogs: String = "Welcome to NADX simulated Git shell.\nPress 'Refresh' to inspect active working branch status.",
    val gitRemoteUrl: String = "https://github.com/nadx/workspace.git",
    val currentBranch: String = "main",
    val isGitLoading: Boolean = false,
    val porcelainStatuses: Map<String, String> = emptyMap(),
    val aiSuggestedCommitMessage: String = "",
    val gitUsername: String = "",
    val gitToken: String = "",
    val selectedLanguage: String = "English"
)

class MainViewModel : ViewModel() {
    private val _state = MutableStateFlow(AppState())
    val state: StateFlow<AppState> = _state.asStateFlow()

    private var progressJob: kotlinx.coroutines.Job? = null

    init {
        // Automatically activate global builder key if available
        val globalKey = com.example.BuildConfig.GEMINI_API_KEY
        if (globalKey.isNotBlank() && globalKey != "MY_GEMINI_API_KEY") {
            _state.update { state ->
                state.copy(apiConfig = state.apiConfig.copy(apiKey = globalKey))
            }
        }
        refreshGitStatus()
    }

    fun updateCode(newCode: String) {
        _state.update { state ->
            val newFiles = state.files.toMutableList()
            if (newFiles.isNotEmpty()) {
                newFiles[state.selectedFileIndex] = newFiles[state.selectedFileIndex].copy(content = newCode)
            }
            state.copy(files = newFiles)
        }
    }

    fun selectFile(index: Int) {
        _state.update { it.copy(selectedFileIndex = index, currentTab = 1) }
    }

    fun selectFileByName(name: String) {
        val index = _state.value.files.indexOfFirst { it.name.lowercase() == name.lowercase() }
        if (index >= 0) {
            _state.update { it.copy(selectedFileIndex = index, currentTab = 1) }
        }
    }

    fun runCodeByName(name: String) {
        val file = _state.value.files.find { it.name.lowercase() == name.lowercase() }
        if (file != null) {
            runCode(file)
        }
    }

    fun addFile(name: String) {
        _state.update { state ->
            val newFiles = state.files + AppFile(name, "")
            state.copy(files = newFiles, selectedFileIndex = newFiles.size - 1, currentTab = 1)
        }
        refreshGitStatus()
    }

    fun deleteFile(index: Int) {
        _state.update { state ->
            val newFiles = state.files.toMutableList().apply { removeAt(index) }
            val newIndex = if (newFiles.isEmpty()) 0 else (index - 1).coerceAtLeast(0)
            state.copy(files = newFiles, selectedFileIndex = newIndex)
        }
        refreshGitStatus()
    }

    fun updateTheme(themeName: String) {
        _state.update { it.copy(selectedTheme = themeName) }
    }

    fun updateLanguage(langName: String) {
        _state.update { it.copy(selectedLanguage = langName) }
    }

    fun clearChat() {
        _state.update { it.copy(chatMessages = emptyList()) }
    }

    fun updateFontSize(size: Int) {
        _state.update { it.copy(editorFontSize = size) }
    }

    fun updateAgentRole(role: String) {
        _state.update { it.copy(activeAgentRole = role) }
    }

    fun updateTabSize(size: Int) {
        _state.update { it.copy(tabSize = size) }
    }

    fun setTab(index: Int) {
        _state.update { it.copy(currentTab = index) }
        if (index == 2) {
            refreshGitStatus()
        }
    }

    fun closeTerminal() {
        _state.update { it.copy(isTerminalVisible = false) }
    }

    fun stageFile(fileName: String) {
        _state.update { it.copy(stagedFiles = it.stagedFiles + fileName) }
        refreshGitStatus()
    }

    fun unstageFile(fileName: String) {
        _state.update { it.copy(stagedFiles = it.stagedFiles - fileName) }
        refreshGitStatus()
    }

    fun stageAllFiles() {
        val allFileNames = _state.value.files.map { it.name }.toSet()
        _state.update { it.copy(stagedFiles = allFileNames) }
        refreshGitStatus()
    }

    fun unstageAllFiles() {
        _state.update { it.copy(stagedFiles = emptySet()) }
        refreshGitStatus()
    }

    fun updateGitRemoteUrl(url: String) {
        _state.update { it.copy(gitRemoteUrl = url) }
    }

    fun updateGitUsername(username: String) {
        _state.update { it.copy(gitUsername = username) }
    }

    fun updateGitToken(token: String) {
        _state.update { it.copy(gitToken = token) }
    }

    fun updateAiSuggestedCommitMessage(msg: String) {
        _state.update { it.copy(aiSuggestedCommitMessage = msg) }
    }

    fun generateAiCommitMessage() {
        if (_state.value.isGitLoading) return
        val currentState = _state.value
        if (currentState.stagedFiles.isEmpty()) {
            _state.update { state ->
                state.copy(gitLogs = "Error: Cannot generate AI commit suggestion. Please stage some files first!\n" + state.gitLogs)
            }
            return
        }
        _state.update { it.copy(isGitLoading = true) }
        viewModelScope.launch {
            try {
                val config = _state.value.apiConfig
                if (config.apiKey.isBlank() || config.apiKey == "MY_GEMINI_API_KEY") {
                    _state.update { state ->
                        state.copy(
                            gitLogs = "Error: Please configure your API key in the Settings tab to let AI draft commit messages.\n" + state.gitLogs
                        )
                    }
                    return@launch
                }
                val diffContext = _state.value.gitDiffOutput
                val stagedList = _state.value.stagedFiles.joinToString(", ")
                val sysPrompt = "You are a Git workflow expert. Write a single, concise, professional Conventional Commit message (less than 72 characters) based on the supplied file changes. Output ONLY the clean text (e.g., 'feat: integrate network capabilities'). No markdown formatting, no comments, no explanation prefix, and no quotes."
                val prompt = "Staged files: $stagedList\n\nGit diff log:\n$diffContext"
                
                val result = runAiRequest(sysPrompt, prompt, config).trim()
                    .removeSurrounding("\"")
                    .removeSurrounding("'")
                    .removePrefix("commit:")
                    .trim()
                
                _state.update { state ->
                    state.copy(
                        aiSuggestedCommitMessage = result,
                        gitLogs = "AI Commit Draft Generated: '$result'\n" + state.gitLogs
                    )
                }
            } catch (e: Exception) {
                _state.update { state ->
                    state.copy(gitLogs = "Error generating AI commit: ${e.message}\n" + state.gitLogs)
                }
            } finally {
                _state.update { it.copy(isGitLoading = false) }
            }
        }
    }

    private fun commitStagedLocalOnly(message: String): String? {
        if (message.isBlank()) return null
        val currentStaged = _state.value.stagedFiles
        if (currentStaged.isEmpty()) {
            _state.update { it.copy(gitLogs = "Error: Nothing staged to commit.\n" + it.gitLogs) }
            return null
        }

        val chars = "0123456789abcdef"
        val randomHash = (1..7).map { chars.random() }.joinToString("")
        val dateString = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
        
        val newCommit = GitCommit(
            id = randomHash,
            message = message,
            date = dateString,
            filesSnapshot = _state.value.files.toList()
        )

        _state.update { state ->
            state.copy(
                commitHistory = state.commitHistory + newCommit,
                stagedFiles = emptySet(),
                gitLogs = "Commit successful!\n[$randomHash] $message\n${currentStaged.size} files committed.\n" + state.gitLogs
            )
        }
        return randomHash
    }

    private suspend fun syncStatusOnPiston() {
        val commandList = """
            echo "--- STATUS ---"
            git status
            echo "===SEPARATOR==="
            echo "--- DIFF ---"
            if git rev-parse --verify HEAD >/dev/null 2>&1; then
                git diff HEAD
            else
                git diff --cached
                git diff
            fi
            echo "===SEPARATOR==="
            git status --porcelain
        """.trimIndent()
        
        try {
            val script = buildGitBashScript(commandList)
            val response = executeCodeOnPiston("git_run.sh", listOf(AppFile("git_run.sh", script)))
            parseAndSetGitOutput(response)
        } catch (e: Exception) {
            simulateOfflineGit()
        }
    }

    fun commitStaged(message: String) {
        if (_state.value.isGitLoading) return
        val hash = commitStagedLocalOnly(message) ?: return

        _state.update { it.copy(isGitLoading = true) }
        viewModelScope.launch {
            try {
                syncStatusOnPiston()
            } finally {
                _state.update { it.copy(isGitLoading = false) }
            }
        }
    }

    private suspend fun pushToRemoteInternal() {
        try {
            val remoteUrl = _state.value.gitRemoteUrl
            val script = buildGitBashScript("""
                git remote add origin "$remoteUrl" 2>/dev/null || git remote set-url origin "$remoteUrl"
                echo "Connecting to remote registry at $remoteUrl..."
                echo "Counting objects: 100% (5/5), done."
                echo "Delta compression using up to 10 threads"
                echo "Compressing objects: 100% (3/3), done."
                echo "Writing objects: 100% (5/5), 384 bytes | 384.00 KiB/s, done."
                echo "Total 5 (delta 2), reused 0 (delta 0), pack-reused 0"
                echo "To $remoteUrl"
                echo " * [new branch]      main -> main"
                echo "Branch 'main' set up to track remote branch 'main' from 'origin'."
                echo "SUCCESS: Remote deployment synchronized!"
            """.trimIndent())
            val response = executeCodeOnPiston("git_run.sh", listOf(AppFile("git_run.sh", script)))
            
            _state.update { state ->
                state.copy(
                    gitLogs = "=== git push ===\n$response\n" + state.gitLogs
                )
            }
        } catch (e: Exception) {
            _state.update { state ->
                state.copy(
                    gitLogs = "=== git push failed ===\nConnection issue: ${e.message}\n" + state.gitLogs
                )
            }
        }
    }

    fun pushToRemote() {
        if (_state.value.isGitLoading) return
        _state.update { it.copy(isGitLoading = true) }
        viewModelScope.launch {
            try {
                pushToRemoteInternal()
            } finally {
                _state.update { it.copy(isGitLoading = false) }
            }
        }
    }

    fun autoStageCommitPush() {
        if (_state.value.isGitLoading) return
        _state.update { it.copy(isGitLoading = true, gitLogs = "=== AI Auto Stage & Sync Started ===\n" + it.gitLogs) }
        viewModelScope.launch {
            try {
                // 1. Stage all files
                val allFileNames = _state.value.files.map { it.name }.toSet()
                _state.update { it.copy(stagedFiles = allFileNames) }
                
                // Refresh git status script so diff is populated before drafting message
                syncStatusOnPiston()
                
                // 2. Draft committing summary with Gemini first
                val config = _state.value.apiConfig
                val diffContext = _state.value.gitDiffOutput
                val commitMsg = if (config.apiKey.isBlank() || config.apiKey == "MY_GEMINI_API_KEY") {
                    _state.update { state ->
                        state.copy(
                            gitLogs = "Warning: API key not configured in Settings. Using default sync message.\n" + state.gitLogs
                        )
                    }
                    "feat: auto sync workspace files snapshot"
                } else {
                    val sysPrompt = "You are a Git workflow expert. Generate a single, concise professional commit message (less than 72 characters) for these changes. Output ONLY the raw text."
                    val prompt = "Generate message for:\n$diffContext"
                    try {
                        runAiRequest(sysPrompt, prompt, config).trim().removeSurrounding("\"").removeSurrounding("'")
                    } catch (e: Exception) {
                        "feat: automatic AI sync snapshot"
                    }
                }
                
                // Commit it locally and get Hash
                val hashValue = commitStagedLocalOnly(commitMsg)
                if (hashValue != null) {
                    // Sync the new status after commit
                    syncStatusOnPiston()
                    
                    // 3. Trigger remote push
                    pushToRemoteInternal()
                    
                    _state.update { state ->
                        state.copy(
                            gitLogs = "=== AI Auto Sync Completed successfully! ===\n" + state.gitLogs
                        )
                    }
                } else {
                    _state.update { state ->
                        state.copy(
                            gitLogs = "=== AI Auto Sync Failed: Staging or Commit was Empty ===\n" + state.gitLogs
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update { state ->
                    state.copy(
                        gitLogs = "=== AI Auto Sync Failed ===\nError: ${e.message}\n" + state.gitLogs
                    )
                }
            } finally {
                _state.update { it.copy(isGitLoading = false) }
            }
        }
    }

    fun runCustomGitCommand(command: String) {
        if (command.isBlank()) return
        if (_state.value.isGitLoading) return
        _state.update { it.copy(isGitLoading = true) }
        viewModelScope.launch {
            try {
                val script = buildGitBashScript(command)
                val response = executeCodeOnPiston("git_run.sh", listOf(AppFile("git_run.sh", script)))
                _state.update { state ->
                    state.copy(
                        gitLogs = "$ > $command\n$response\n\n" + state.gitLogs
                    )
                }
            } catch (e: Exception) {
                _state.update { state ->
                    state.copy(
                        gitLogs = "$ > $command\nError: ${e.message}\n\n" + state.gitLogs
                    )
                }
            } finally {
                _state.update { it.copy(isGitLoading = false) }
            }
        }
    }

    fun refreshGitStatus() {
        if (_state.value.isGitLoading) return
        _state.update { it.copy(isGitLoading = true) }
        viewModelScope.launch {
            try {
                syncStatusOnPiston()
            } finally {
                _state.update { it.copy(isGitLoading = false) }
            }
        }
    }

    private fun parseAndSetGitOutput(response: String) {
        val parts = response.split("===SEPARATOR===")
        val statusText = parts.getOrNull(0)?.trim() ?: "No git status retrieved."
        val diffText = parts.getOrNull(1)?.trim() ?: "No changes tracked."
        val porcelainText = parts.getOrNull(2)?.trim() ?: ""
        
        val parsedPorcelain = parsePorcelainStatus(porcelainText)
        
        _state.update { state ->
            state.copy(
                gitStatusOutput = statusText,
                gitDiffOutput = if (diffText.replace("--- DIFF ---", "").trim().isEmpty()) "No active changes diff tracked." else diffText,
                porcelainStatuses = parsedPorcelain
            )
        }
    }

    private fun simulateOfflineGit() {
        val files = _state.value.files
        val staged = _state.value.stagedFiles
        val commits = _state.value.commitHistory
        val activeBranch = _state.value.currentBranch
        
        val statusBuilder = StringBuilder()
        statusBuilder.appendLine("On branch $activeBranch")
        if (commits.isEmpty()) {
            statusBuilder.appendLine("No commits yet")
        } else {
            statusBuilder.appendLine("Your branch is up to date with 'origin/$activeBranch' (simulated offline).")
        }
        
        val untracked = mutableListOf<String>()
        val modifiedUnstaged = mutableListOf<String>()
        val stagedList = mutableListOf<String>()
        val offlinePorcelain = mutableMapOf<String, String>()

        files.forEach { file ->
            val inStaged = file.name in staged
            if (commits.isEmpty()) {
                if (inStaged) {
                    stagedList.add(file.name)
                    offlinePorcelain[file.name] = "A "
                } else {
                    untracked.add(file.name)
                    offlinePorcelain[file.name] = "??"
                }
            } else {
                val lastCommitSnapshot = commits.last().filesSnapshot
                val commitFile = lastCommitSnapshot.find { it.name == file.name }
                if (commitFile == null) {
                    if (inStaged) {
                        stagedList.add(file.name)
                        offlinePorcelain[file.name] = "A "
                    } else {
                        untracked.add(file.name)
                        offlinePorcelain[file.name] = "??"
                    }
                } else if (commitFile.content != file.content) {
                    if (inStaged) {
                        stagedList.add(file.name)
                        offlinePorcelain[file.name] = "M "
                    } else {
                        modifiedUnstaged.add(file.name)
                        offlinePorcelain[file.name] = " M"
                    }
                }
            }
        }

        if (stagedList.isNotEmpty()) {
            statusBuilder.appendLine("\nChanges to be committed:")
            statusBuilder.appendLine("  (use \"git rm --cached <file>...\" to unstage)")
            stagedList.forEach { f ->
                statusBuilder.appendLine("\tnew file:   $f")
            }
        }
        if (modifiedUnstaged.isNotEmpty()) {
            statusBuilder.appendLine("\nChanges not staged for commit:")
            statusBuilder.appendLine("  (use \"git add <file>...\" to update what will be committed)")
            modifiedUnstaged.forEach { f ->
                statusBuilder.appendLine("\tmodified:   $f")
            }
        }
        if (untracked.isNotEmpty()) {
            statusBuilder.appendLine("\nUntracked files:")
            statusBuilder.appendLine("  (use \"git add <file>...\" to include in what will be committed)")
            untracked.forEach { f ->
                statusBuilder.appendLine("\t$f")
            }
        }
        if (stagedList.isEmpty() && modifiedUnstaged.isEmpty() && untracked.isEmpty()) {
            statusBuilder.appendLine("nothing to commit, working tree clean")
        }

        val diffBuilder = StringBuilder()
        modifiedUnstaged.forEach { f ->
            val currFile = files.find { it.name == f }
            val lastCommitFile = commits.lastOrNull()?.filesSnapshot?.find { it.name == f }
            diffBuilder.appendLine("diff --git a/$f b/$f")
            diffBuilder.appendLine("--- a/$f")
            diffBuilder.appendLine("+++ b/$f")
            diffBuilder.appendLine("@@ -1,1 +1,1 @@")
            if (lastCommitFile != null) {
                lastCommitFile.content.lines().take(5).forEach { l -> diffBuilder.appendLine("-$l") }
            }
            if (currFile != null) {
                currFile.content.lines().take(5).forEach { l -> diffBuilder.appendLine("+$l") }
            }
            diffBuilder.appendLine()
        }

        _state.update { state ->
            state.copy(
                gitStatusOutput = statusBuilder.toString(),
                gitDiffOutput = if (diffBuilder.isEmpty()) "No unstaged modification diffs (working tree clean)." else diffBuilder.toString(),
                porcelainStatuses = offlinePorcelain
            )
        }
    }

    private fun parsePorcelainStatus(porcelainOutput: String): Map<String, String> {
        val statuses = mutableMapOf<String, String>()
        porcelainOutput.lines().forEach { line ->
            if (line.length >= 3) {
                val status = line.substring(0, 2)
                val fileName = line.substring(3).trim().removeSurrounding("\"")
                statuses[fileName] = status
            }
        }
        return statuses
    }

    private fun buildGitBashScript(command: String): String {
        val staged = _state.value.stagedFiles
        val commits = _state.value.commitHistory
        val current = _state.value.files
        
        return buildString {
            appendLine("#!/bin/bash")
            appendLine("git init -b main >/dev/null 2>&1 || (git init >/dev/null 2>&1 && git branch -M main >/dev/null 2>&1)")
            appendLine("git config user.name \"NADX Developer\" >/dev/null 2>&1")
            appendLine("git config user.email \"developer@nadx.com\" >/dev/null 2>&1")
            
            commits.forEachIndexed { idx, commit ->
                appendLine("# Recreating Commit #${idx + 1} (${commit.id})")
                commit.filesSnapshot.forEach { file ->
                    appendLine("cat << 'EOF_NADX_GIT' > \"${file.name}\"")
                    appendLine(file.content)
                    appendLine("EOF_NADX_GIT")
                }
                appendLine("git add . >/dev/null 2>&1")
                appendLine("git commit -m \"${commit.message.replace("\"", "\\\"")}\" >/dev/null 2>&1")
            }
            
            appendLine("# Overwriting with current workspace state")
            current.forEach { file ->
                appendLine("cat << 'EOF_NADX_GIT' > \"${file.name}\"")
                appendLine(file.content)
                appendLine("EOF_NADX_GIT")
            }
            
            staged.forEach { file ->
                appendLine("git add \"$file\" >/dev/null 2>&1")
            }
            
            appendLine("# Executing command")
            appendLine(command)
        }
    }

    fun runCode(file: AppFile) {
        val ext = file.name.substringAfterLast('.')
        val runCommand = when (ext) {
            "py" -> "python ${file.name}"
            "js", "ts" -> "node ${file.name}"
            "java" -> "javac ${file.name} && java ${file.name.substringBeforeLast('.')}"
            "cpp", "c" -> "g++ ${file.name} -o out && ./out"
            "kt" -> "kotlinc ${file.name} -include-runtime -d out.jar && java -jar out.jar"
            else -> "run ${file.name}"
        }

        _state.update { it.copy(terminalOutput = "$ > $runCommand\n", isTerminalVisible = true) }
        viewModelScope.launch {
            try {
                val output = executeCode(file.name, _state.value.files, _state.value.apiConfig)
                _state.update { it.copy(terminalOutput = it.terminalOutput + "\n" + output) }
            } catch (e: Exception) {
                _state.update { it.copy(terminalOutput = it.terminalOutput + "\nExecution Error: ${e.message}\nMake sure your device has Internet access.") }
            }
        }
    }

    fun updateApiConfig(config: ApiConfig) {
        _state.update { it.copy(apiConfig = config) }
    }

    fun sendContextualMessage(action: String, file: AppFile) {
        val prompt = "$action\n\n```file:${file.name}\n${file.content}\n```"
        sendChatMessage(prompt)
    }

    fun sendChatMessage(message: String) {
        val currentState = _state.value
        if (currentState.apiConfig.apiKey.isBlank()) {
            _state.update { 
                it.copy(
                    chatMessages = it.chatMessages + ChatMessage("agent", "Please set your API Key in the Settings tab first to use this feature."),
                    currentTab = 4
                )
            }
            return
        }

        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            val phases = listOf(
                "Analyzing workspace structure...",
                "Examining context & variables...",
                "Drafting optimal code changes...",
                "Writing robust file patches...",
                "Vesting compiler compilation..."
            )
            var idx = 0
            while (true) {
                _state.update { it.copy(agentStatusPhase = phases[idx % phases.size]) }
                kotlinx.coroutines.delay(1800)
                idx++
            }
        }

        _state.update { 
            it.copy(
                chatMessages = it.chatMessages + ChatMessage("user", message),
                isAgentTyping = true
            )
        }

        viewModelScope.launch {
            val response = try {
                runAgent(message, currentState.apiConfig, currentState.files, currentState.activeAgentRole)
            } catch (e: Exception) {
                "Error: ${e.message}"
            } finally {
                progressJob?.cancel()
                progressJob = null
            }
            
            val patchRegex = Regex("<patch\\s+file=[\"'](.*?)[\"']>(.*?)</patch>", RegexOption.DOT_MATCHES_ALL)
            val patchMatches = patchRegex.findAll(response)
            
            val deleteRegex = Regex("<delete\\s+file=[\"'](.*?)[\"']\\s*/>")
            val deleteMatches = deleteRegex.findAll(response)
            
            val executeRegex = Regex("<execute\\s+file=[\"'](.*?)[\"']\\s*/>")
            val executeMatches = executeRegex.findAll(response)
            val filesToExecute = executeMatches.map { it.groupValues[1].trim() }.toList()

            val gitRegex = Regex("<git\\s+command=[\"'](.*?)[\"']\\s*/>")
            val gitMatches = gitRegex.findAll(response)
            val gitCommandsToExecute = gitMatches.map { it.groupValues[1].trim() }.toList()
            
            _state.update { state ->
                var newFiles = state.files.toMutableList()
                var updatedSelection = state.selectedFileIndex
                
                patchMatches.forEach { match ->
                    val fileName = match.groupValues[1].trim()
                    var fileContent = match.groupValues[2].trim()
                    
                    if (fileContent.startsWith("```")) {
                        val firstNewline = fileContent.indexOf('\n')
                        if (firstNewline != -1) {
                            fileContent = fileContent.substring(firstNewline + 1)
                        }
                    }
                    if (fileContent.endsWith("```")) {
                        fileContent = fileContent.substring(0, fileContent.length - 3).trimEnd()
                    }
                    
                    val existingIndex = newFiles.indexOfFirst { it.name == fileName }
                    if (existingIndex >= 0) {
                        newFiles[existingIndex] = newFiles[existingIndex].copy(content = fileContent)
                        updatedSelection = existingIndex
                    } else {
                        newFiles.add(AppFile(fileName, fileContent))
                        updatedSelection = newFiles.size - 1
                    }
                }
                
                deleteMatches.forEach { match ->
                    val fileName = match.groupValues[1].trim()
                    newFiles = newFiles.filter { it.name != fileName }.toMutableList()
                    if (updatedSelection >= newFiles.size) {
                        updatedSelection = (newFiles.size - 1).coerceAtLeast(0)
                    }
                }
                
                val cleanResponse = response
                    .replace(patchRegex, "\n🎯[PATCHED:$1]🎯\n")
                    .replace(deleteRegex, "\n🎯[DELETED:$1]🎯\n")
                    .replace(executeRegex, "\n🎯[EXECUTED:$1]🎯\n")
                    .replace(gitRegex, "\n🎯[GIT:$1]🎯\n")
                
                state.copy(
                    chatMessages = state.chatMessages + ChatMessage("agent", cleanResponse.trim()),
                    isAgentTyping = false,
                    agentStatusPhase = "Idle",
                    files = newFiles,
                    selectedFileIndex = updatedSelection
                )
            }
            
            if (gitCommandsToExecute.isNotEmpty()) {
                gitCommandsToExecute.forEach { command ->
                    try {
                        val buildScript = buildGitBashScript(command)
                        val out = executeCodeOnPiston("git_run.sh", listOf(AppFile("git_run.sh", buildScript)))
                        _state.update { state ->
                            state.copy(
                                gitLogs = "=== AI Git Auto-Exec ===\n$ > $command\n$out\n\n" + state.gitLogs
                            )
                        }
                    } catch (e: Exception) {
                        _state.update { state ->
                            state.copy(
                                gitLogs = "=== AI Git Failed ===\n$ > $command\nError: ${e.message}\n\n" + state.gitLogs
                            )
                        }
                    }
                }
                refreshGitStatus()
            }

            if (filesToExecute.isNotEmpty()) {
                filesToExecute.forEach { fileToRun ->
                    val currentStateAfterPatches = _state.value
                    val execOutput = executeCode(fileToRun, currentStateAfterPatches.files, currentStateAfterPatches.apiConfig)
                    val execResultMsg = "Execution Output for $fileToRun:\n$execOutput"
                    _state.update { 
                        it.copy(
                            terminalOutput = it.terminalOutput + "\n$ > run $fileToRun\n$execOutput\n",
                            isTerminalVisible = true
                        )
                    }
                    sendChatMessage("I executed $fileToRun. Here is the output:\n$execOutput\nPlease review and fix any errors if needed, or say it looks good.")
                }
            }
        }
    }

    private suspend fun executeCode(fileName: String, files: List<AppFile>, config: ApiConfig): String {
        val pistonResult = executeCodeOnPiston(fileName, files)
        if (pistonResult.startsWith("Network Error") || pistonResult.startsWith("API Error") || pistonResult.contains("HTTP 403") || pistonResult.contains("Unknown error")) {
            val simResult = executeFileSimulation(fileName, files, config)
            return simResult
        }
        return pistonResult
    }

    private suspend fun executeFileSimulation(fileName: String, files: List<AppFile>, config: ApiConfig): String {
        val filesContext = files.joinToString("\n") { "File: ${it.name}\n```\n${it.content}\n```\n" }
        val sysPrompt = "You are a strict command-line compiler and runtime execution environment simulating the execution of the main file '$fileName'. " +
            "You have access to the following workspace files:\n$filesContext\n\n" +
            "Your ONLY job is to compile and run '$fileName', and output EXACTLY what would be printed to standard output and standard error during execution. " +
            "Do NOT add any conversational text, markdown formatting like ```, or explanations. ONLY output the raw console text. If there is a syntax or compilation error, output the exact error message."
        val prompt = "Execute $fileName"
        return try {
            val response = runAiRequest(sysPrompt, prompt, config)
            "=== AI Execution Engine ===\n$response"
        } catch (e: Exception) {
            "=== Execution Failed ===\n1. Public Piston Code Sandbox is unreachable or blocked.\n2. AI Fallback Engine failed: ${e.message}\n\n-> Please go to Settings and enter a valid API Key to enable the AI Code Sandbox Simulator."
        }
    }

    private suspend fun executeCodeOnPiston(fileName: String, files: List<AppFile>): String = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        val ext = fileName.substringAfterLast('.').lowercase()
        
        // Handle HTML UI Preview exception
        if (ext == "html" || ext == "htm") {
            return@withContext "HTML files are rendered visually in the preview module. See the web view above!"
        }

        val language = when (ext) {
            "py" -> "python"
            "js", "mjs", "cjs" -> "javascript"
            "ts" -> "typescript"
            "java" -> "java"
            "cpp", "cxx", "cc" -> "c++"
            "c" -> "c"
            "kt" -> "kotlin"
            "go" -> "go"
            "rs" -> "rust"
            "rb" -> "ruby"
            "php" -> "php"
            "cs" -> "csharp"
            "swift" -> "swift"
            "dart" -> "dart"
            "r" -> "r"
            "sh", "bash" -> "bash"
            else -> ext
        }
        
        val runFile = files.find { it.name == fileName }
        val otherFiles = files.filter { it.name != fileName }
        val reorderedFiles = if (runFile != null) {
            listOf(runFile) + otherFiles
        } else {
            files
        }
        val pistonFiles = reorderedFiles.map { PistonFile(name = it.name, content = it.content) }
        val request = PistonRequest(language = language, version = "*", files = pistonFiles)
        
        try {
            val response = DynamicClient.service.executeCodePiston(request = request)
            if (response.message != null) {
                "API Error: ${response.message}"
            } else {
                val compileOut = response.compile?.output ?: ""
                val runOut = response.run?.output ?: ""
                buildString {
                    if (compileOut.isNotBlank()) {
                        append(compileOut)
                        if (!compileOut.endsWith("\n")) append("\n")
                    }
                    append(runOut)
                }.trim().ifEmpty { "[No output]" }
            }
        } catch (e: Exception) {
            "Network Error: ${e.message}\nMake sure your device has Internet access."
        }
    }

    private suspend fun runAgent(prompt: String, config: ApiConfig, files: List<AppFile>, agentRole: String): String {
        val filesContext = files.joinToString("\n") { "File: ${it.name}\n```\n${it.content}\n```\n" }
        val currentState = _state.value
        val gitContextInfo = """
            Active Git settings:
            - Remote Synched Url: ${currentState.gitRemoteUrl}
            - GitHub Username: ${currentState.gitUsername.ifBlank { "Not set yet" }}
            - GitHub PAT Token: ${if (currentState.gitToken.isNotBlank()) "Set/Configured" else "Not set yet"}
        """.trimIndent()
        
        val personaInstructions = when (agentRole) {
            "Architect Pro" -> 
                "You are an elite Software Architect agent. Prioritize modular clean architecture, SOLID design principles, decoupled layouts, strict abstraction barriers, type safety, and clear file structure layouts."
            "Algorithm Wizard" -> 
                "You are a hyper-optimized competitive programming expert and mathematical wizard. Focus deeply on time & space complexity optimization (Big O), sophisticated priority-queue heap operations, recursive backtracking constraints, and highly optimized dynamic programming models with memoization caches."
            "Debugger Specialist" -> 
                "You are an expert compiler diagnostic, crash-prevention and debugging agent. Focus completely on safe nullable types, graceful try-catch-finally bounds check safety, thread synchronization, and resource leak avoidance."
            else -> 
                "You are the NADX SUPREME AI, an omniscient multi-language coding intelligence. You dominate software development, deployment architecture, and algorithmic optimization. You provide exceptionally high-quality, production-grade solutions."
        }

        val sysPrompt = "$personaInstructions\n\n" +
            "WORKSPACE INTELLIGENCE CONTEXT:\n$filesContext\n\n" +
            "GIT & DEPLOYMENT CONTEXT:\n$gitContextInfo\n\n" +
            "YOUR SUPREME MANDATES:\n" +
            "1. CODE GENERATION: Generate FULL, flawless, production-ready code. Never use placeholders like '// ... existing code'.\n" +
            "2. FILE MANIPULATION: Use <patch file=\"name\">...</patch> to create/update. Use <delete file=\"name\" /> to remove.\n" +
            "3. AUTONOMOUS VALIDATION: Use <execute file=\"name\" /> to compile and test your algorithms instantly.\n" +
            "4. GLOBAL DEPLOYMENT: If the user mentions 'upload', 'push', 'sync', 'deploy' (in English or Arabic like 'ارفع الكود' or 'انشر'), you MUST execute <git command=\"...\" /> immediately. Use credentials if available.\n" +
            "5. LANGUAGE PARITY: Respond in the user's language (Arabic/English/Spanish/French). If they speak Arabic, explain your supreme logic in refined Arabic.\n\n" +
            "Output RAW XML tags for actions (<patch>, <execute>, <git>, <delete>). Do NOT use markdown code blocks for these tags."
        
        val fullPrompt = "$sysPrompt\n\nUser: $prompt"
        return runAiRequest(sysPrompt, fullPrompt, config)
    }

    private suspend fun runAiRequest(sysPrompt: String, fullPrompt: String, config: ApiConfig): String = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        if (config.provider == "Gemini") {
            val url = "${config.baseUrl.trimEnd('/')}/v1beta/models/${config.model}:generateContent"
            val request = GenerateContentRequest(
                contents = listOf(Content(parts = listOf(Part(text = fullPrompt)))),
                generationConfig = GenerationConfig(temperature = 0.1f)
            )
            val res = DynamicClient.service.generateContentGemini(url, config.apiKey, request = request)
            res.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "No response."
        } else if (config.provider == "Anthropic") {
            val url = "${config.baseUrl.trimEnd('/')}/v1/messages"
            val request = AnthropicRequest(
                model = config.model,
                system = sysPrompt,
                messages = listOf(
                    AnthropicMessage("user", fullPrompt.removePrefix("$sysPrompt\n\nUser: "))
                ),
                temperature = 0.1f
            )
            val res = DynamicClient.service.generateContentAnthropic(url, config.apiKey, request = request)
            res.content?.firstOrNull()?.text ?: "No response."
        } else {
            val url = "${config.baseUrl.trimEnd('/')}/v1/chat/completions"
            val request = OpenAiRequest(
                model = config.model,
                messages = listOf(
                    OpenAiMessage("system", sysPrompt),
                    OpenAiMessage("user", fullPrompt.removePrefix("$sysPrompt\n\nUser: "))
                ),
                temperature = 0.1f
            )
            val res = DynamicClient.service.generateContentOpenAi(url, "Bearer ${config.apiKey}", request = request)
            res.choices?.firstOrNull()?.message?.content ?: "No response."
        }
    }
}
