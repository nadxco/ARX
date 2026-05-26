package com.example.ui.screens

import androidx.compose.ui.unit.LayoutDirection

enum class SupportedLanguage(val displayName: String) {
    ENGLISH("English"),
    ARABIC("العربية"),
    SPANISH("Español"),
    FRENCH("Français")
}

interface LocaleStrings {
    val layoutDirection: LayoutDirection
    val appName: String
    val explorerTab: String
    val editorTab: String
    val gitTab: String
    val agentTab: String
    val settingsTab: String
    
    // Explorer Screen
    val workspaceExplorerTitle: String
    val emptyWorkspace: String
    val noFileSelected: String
    val enterFileName: String
    val fileEmptyError: String
    val createFile: String
    val cancel: String
    val deleteFileConfirm: String
    
    // Editor Screen
    val runCode: String
    val aiActions: String
    val explainCode: String
    val fixBugs: String
    val optimizeCode: String
    val transComments: String
    val terminalOutputTitle: String
    val charactersLabel: String
    val linesLabel: String
    val searchPlaceholder: String
    val replacePlaceholder: String
    val formatCode: String
    
    // Git Screen
    val aiGitAssistant: String
    val gitAssistantDesc: String
    val suggestedCommit: String
    val applyCommit: String
    val draftMessage: String
    val autoStagePush: String
    val analyzeDiffChat: String
    val commitCardTitle: String
    val commitMsgPlaceholder: String
    val commitButton: String
    val refreshGitBtn: String
    val gitTerminalOutput: String
    val activeBranchLabel: String
    val stagedFilesLabel: String
    val unstagedFilesLabel: String
    val remoteRepoUrl: String
    val customGitCmd: String
    val runCmdBtn: String
    val gitLogsLabel: String
    val gitUsernameLabel: String
    val gitTokenLabel: String
    val projectStatsTitle: String
    val totalFilesLabel: String
    val totalLinesLabel: String
    val lastSyncLabel: String
    
    // Agent Screen
    val aiCopilotTitle: String
    val agentStatusIdle: String
    val agentStatusTyping: String
    val enterPromptPlaceholder: String
    val sendBtn: String
    val agentRoleLabel: String
    
    // Settings Screen
    val settingsTitle: String
    val settingsSubtitle: String
    val editorThemeLabel: String
    val interactivePreferences: String
    val fontSizeLabel: String
    val fontPreviewHeading: String
    val indentSizeLabel: String
    val indentSizeSub: String
    val spacesSuffix: String
    val aiBackendConfig: String
    val apiKeyPlaceholder: String
    val saveConfigBtn: String
    val selectLanguageLabel: String
    
    // Additional features
    val translateExplanation: String
    val cleanTerminal: String
    val copySuccess: String
    val themeMidnight: String
    val themeSolarized: String
    val themeNeon: String
    val themeCyber: String
}

object EnglishStrings : LocaleStrings {
    override val layoutDirection = LayoutDirection.Ltr
    override val appName = "NADX Supreme Pro IDE"
    override val explorerTab = "Explorer"
    override val editorTab = "Editor"
    override val gitTab = "Git"
    override val agentTab = "Agent"
    override val settingsTab = "Settings"
    
    override val workspaceExplorerTitle = "WORKSPACE EXPLORER"
    override val emptyWorkspace = "Workspace is empty. Add files manually."
    override val noFileSelected = "No active file selected."
    override val enterFileName = "Enter new file name (e.g., app.py, index.html)"
    override val fileEmptyError = "File name cannot be empty!"
    override val createFile = "Create File"
    override val cancel = "Cancel"
    override val deleteFileConfirm = "Are you sure you want to delete this file?"
    
    override val runCode = "RUN CODE"
    override val aiActions = "AI COPILOT ACTIONS"
    override val explainCode = "Explain Code"
    override val fixBugs = "Auto-Fix Bugs"
    override val optimizeCode = "Optimize Big-O"
    override val transComments = "Translate Comments to Arabic & English"
    override val terminalOutputTitle = "Terminal Engine Output"
    override val charactersLabel = "Chars"
    override val linesLabel = "Lines"
    override val searchPlaceholder = "Search text..."
    override val replacePlaceholder = "Replace with..."
    override val formatCode = "Auto Format Code"
    
    override val aiGitAssistant = "AI Git & Export Assistant"
    override val gitAssistantDesc = "Let the AI Agent analyze changes, write conventional commits, or automatically stage and export commits."
    override val suggestedCommit = "Suggested Commit Message:"
    override val applyCommit = "Apply to Commit Field"
    override val draftMessage = "Draft Message"
    override val autoStagePush = "Auto Stage & Push"
    override val analyzeDiffChat = "Analyze Diff with Agent (In Chat Tab)"
    override val commitCardTitle = "Commit & Push Suite"
    override val commitMsgPlaceholder = "Enter commit description..."
    override val commitButton = "Commit Staged Files"
    override val refreshGitBtn = "Refresh Repository Status"
    override val gitTerminalOutput = "Active Git Console Logs"
    override val activeBranchLabel = "ACTIVE BRANCH:"
    override val stagedFilesLabel = "STAGED FOR DEPLOYMENT"
    override val unstagedFilesLabel = "UNTRACKED / MODIFIED FILES"
    override val remoteRepoUrl = "Git Remote Synchronization Endpoint"
    override val customGitCmd = "Execute Autonomous Custom Git Shell Command"
    override val runCmdBtn = "Run Shell Cmd"
    override val gitLogsLabel = "Live Action Logging Stream:"
    override val gitUsernameLabel = "GitHub Username profile"
    override val gitTokenLabel = "GitHub Personal Access Token"
    override val projectStatsTitle = "Project Intelligence Dashboard"
    override val totalFilesLabel = "Workspace Files"
    override val totalLinesLabel = "Total Code Lines"
    override val lastSyncLabel = "Last Remote Sync"
    
    override val aiCopilotTitle = "NADX Multi-Agent Chat Console"
    override val agentStatusIdle = "Idle - Waiting for prompts"
    override val agentStatusTyping = "Agent is crafting response..."
    override val enterPromptPlaceholder = "Ask the AI builder to write code, design layout, or explain algorithms..."
    override val sendBtn = "Send"
    override val agentRoleLabel = "Select Expert Agent Role"
    
    override val settingsTitle = "Premium System Settings"
    override val settingsSubtitle = "Customize the IDE theme, fonts size, indentation, and active languages localization."
    override val editorThemeLabel = "EDITOR SCHEME & VISUAL THEME"
    override val interactivePreferences = "EDITOR INTERACTIVE PREFERENCES"
    override val fontSizeLabel = "Editor Font Size"
    override val fontPreviewHeading = "Typography Preview:"
    override val indentSizeLabel = "Indentation Size"
    override val indentSizeSub = "Virtual spaces inserted on Tab click"
    override val spacesSuffix = "Spaces"
    override val aiBackendConfig = "AI DEVELOPER BACKEND CONFIGURATION"
    override val apiKeyPlaceholder = "Enter private API API key..."
    override val saveConfigBtn = "Save Technical Config"
    override val selectLanguageLabel = "LOCALIZATION & DISPLAY LANGUAGE"
    
    override val translateExplanation = "Translate whole codebase summary and explanations."
    override val cleanTerminal = "Clear Output"
    override val copySuccess = "Code copied to clipboard successfully!"
    override val themeMidnight = "VS Code Midnight Aura"
    override val themeSolarized = "Solarized Ocean Theme"
    override val themeNeon = "Neon Abyss Retro Sci-Fi"
    override val themeCyber = "Cyber Monokai Hacker Theme"
}

object ArabicStrings : LocaleStrings {
    override val layoutDirection = LayoutDirection.Rtl
    override val appName = "نظام NADX Supreme Pro المتكامل"
    override val explorerTab = "مستكشف الملفات"
    override val editorTab = "محرر الأكواد"
    override val gitTab = "إدارة Git"
    override val agentTab = "مساعد الذكاء الاصطناعي"
    override val settingsTab = "الإعدادات"
    
    override val workspaceExplorerTitle = "مستكشف مساحة العمل"
    override val emptyWorkspace = "مساحة العمل فارغة. قم بإضافة ملف يدويًا."
    override val noFileSelected = "لم يتم تحديد ملف نشط حاليًا."
    override val enterFileName = "أدخل اسم الملف الجديد (مثل: app.py، index.html)"
    override val fileEmptyError = "لا يمكن أن يكون اسم الملف فارغًا!"
    override val createFile = "إنشاء ملف"
    override val cancel = "إلغاء"
    override val deleteFileConfirm = "هل أنت متأكد تمامًا من رغبتك في حذف هذا الملف؟"
    
    override val runCode = "تشغيل الكود"
    override val aiActions = "إجراءات مساعد الذكاء الاصطناعي"
    override val explainCode = "شرح الكود البرمجي"
    override val fixBugs = "إصلاح الأخطاء تلقائيًا"
    override val optimizeCode = "تحسين الأداء (Big-O)"
    override val transComments = "ترجمة التعليقات للغة العربية والإنجليزية"
    override val terminalOutputTitle = "مخرجات مشغل الأكواد والبيئة"
    override val charactersLabel = "حرف"
    override val linesLabel = "سطر"
    override val searchPlaceholder = "بحث عن نص..."
    override val replacePlaceholder = "استبدال بـ..."
    override val formatCode = "تنسيق الكود تلقائيًا"
    
    override val aiGitAssistant = "مساعد إدارة Git البرمجي الذكي"
    override val gitAssistantDesc = "دع عميل الذكاء الاصطناعي يحلل التغييرات الحالية، ويكتب رسائل الالتزام القياسية، أو يسحب ويرفع تلقائيًا للمستودعات البعيدة."
    override val suggestedCommit = "رسالة الالتزام المقترحة بالذكاء الاصطناعي:"
    override val applyCommit = "تطبيق على حقل رسالة الالتزام"
    override val draftMessage = "صياغة الرسالة"
    override val autoStagePush = "تجهيز التغييرات ورفعها تلقائيًا"
    override val analyzeDiffChat = "تحليل الفروقات مع العميل الذكي (في تبويب الدردشة)"
    override val commitCardTitle = "منظومة تسجيل ورفع الأكواد"
    override val commitMsgPlaceholder = "أدخل وصفًا لرسالة الحفظ والالتزام..."
    override val commitButton = "حفظ الملفات المجهزة"
    override val refreshGitBtn = "تحديث حالة المستودع"
    override val gitTerminalOutput = "سجلات وحدة تحكم Git والشبكة"
    override val activeBranchLabel = "الفرع النشط الحالي:"
    override val stagedFilesLabel = "الملفات المجهزة للنشر والتصدير"
    override val unstagedFilesLabel = "الملفات المعدلة أو غير المتعقبة"
    override val remoteRepoUrl = "عنوان مستودع الرفع والتزامن البعيد"
    override val customGitCmd = "تنفيذ أمر Git يدوي مخصص في الطرفية"
    override val runCmdBtn = "تشغيل الأمر"
    override val gitLogsLabel = "تدفق السجلات المباشر للعمليات:"
    override val gitUsernameLabel = "اسم مستخدم جيت هاب (GitHub Username)"
    override val gitTokenLabel = "رمز الوصول الشخصي لجيت هاب (PAT Token)"
    override val projectStatsTitle = "لوحة معلومات ذكاء المشروع"
    override val totalFilesLabel = "ملفات مساحة العمل"
    override val totalLinesLabel = "إجمالي أسطر الكود"
    override val lastSyncLabel = "آخر مزامنة عن بعد"
    
    override val aiCopilotTitle = "منصة دردشة NADX متعددة العملاء"
    override val agentStatusIdle = "خامل - بانتظار استفساراتك البرمجية"
    override val agentStatusTyping = "مساعد الذكاء الاصطناعي يقوم بصياغة الحل المبتكر..."
    override val enterPromptPlaceholder = "اطلب من المساعد البرمجي كتابة كود، تخطيط واجهة، أو شرح الخوارزميات وصياغتها..."
    override val sendBtn = "إرسال"
    override val agentRoleLabel = "تحديد تخصص ودور العميل الذكي"
    
    override val settingsTitle = "الإعدادات المتقدمة للمنظومة"
    override val settingsSubtitle = "تخصيص السمة المرئية ومظهر المحرر، وأحجام الخطوط، وحجم المسافات والترميز اللغوي للمنصة."
    override val editorThemeLabel = "السمة المرئية وتصميم الألوان للمحرر"
    override val interactivePreferences = "التفضيلات التفاعلية لمحرر النصوص"
    override val fontSizeLabel = "حجم خط الأكواد في المحرر"
    override val fontPreviewHeading = "معاينة تنسيق النصوص والخطوط:"
    override val indentSizeLabel = "حجم المسافة البادئة للأكواد"
    override val indentSizeSub = "المسافات الافتراضية المضافة عند النقر على مفتاح Tab"
    override val spacesSuffix = "مسافات"
    override val aiBackendConfig = "تكوين إعدادات محرك الحوسبة والذكاء الاصطناعي"
    override val apiKeyPlaceholder = "أدخل مفتاح واجهة برمجة التطبيقات الخاص بك..."
    override val saveConfigBtn = "حفظ وإعداد تهيئة النظام"
    override val selectLanguageLabel = "تحديد اللغة والترميز الإقليمي (LOCALIZATION)"
    
    override val translateExplanation = "ترجمة ملخص الكود والشروحات كاملة."
    override val cleanTerminal = "مسح المخرجات"
    override val copySuccess = "تم نسخ الكود بنجاح إلى الحافظة!"
    override val themeMidnight = "محيط الفضاء الفخم (VS Code)"
    override val themeSolarized = "السمة البحرية الهادئة"
    override val themeNeon = "هاكر النيون الحديثة"
    override val themeCyber = "سمة السايبر والأساطير"
}

object SpanishStrings : LocaleStrings {
    override val layoutDirection = LayoutDirection.Ltr
    override val appName = "NADX Supreme Pro IDE"
    override val explorerTab = "Explorador"
    override val editorTab = "Editor"
    override val gitTab = "Git"
    override val agentTab = "Agente"
    override val settingsTab = "Ajustes"
    
    override val workspaceExplorerTitle = "EXPLORADOR DE PROYECTOS"
    override val emptyWorkspace = "El espacio de trabajo está vacío. Crea archivos."
    override val noFileSelected = "Ningún archivo activo seleccionado."
    override val enterFileName = "Nombre del archivo (ej. app.py, index.html)"
    override val fileEmptyError = "¡El nombre del archivo no puede estar vacío!"
    override val createFile = "Crear Archivo"
    override val cancel = "Cancelar"
    override val deleteFileConfirm = "¿Está seguro de que desea eliminar este archivo?"
    
    override val runCode = "EJECUTAR"
    override val aiActions = "ACCIONES DEL COPILOTO IA"
    override val explainCode = "Explicar Código"
    override val fixBugs = "Corregir Errores"
    override val optimizeCode = "Optimizar Código"
    override val transComments = "Traducir comentarios a árabe y español"
    override val terminalOutputTitle = "Salida de Consola y Ejecución"
    override val charactersLabel = "Caract."
    override val linesLabel = "Líneas"
    override val searchPlaceholder = "Buscar texto..."
    override val replacePlaceholder = "Reemplazar con..."
    override val formatCode = "Formatear Código"
    
    override val aiGitAssistant = "Asistente IA de Git & Exportación"
    override val gitAssistantDesc = "Permita que el Agente IA analice los cambios, escriba confirmaciones convencionales o implemente automáticamente."
    override val suggestedCommit = "Mensaje de confirmación sugerido por la IA:"
    override val applyCommit = "Aplicar al campo de mensaje"
    override val draftMessage = "Redactar Mensaje"
    override val autoStagePush = "Autopreparar & Publicar"
    override val analyzeDiffChat = "Analizar diferencias con el agente (en chat)"
    override val commitCardTitle = "Suite de Confirmación y Envío"
    override val commitMsgPlaceholder = "Escribir mensaje de confirmación..."
    override val commitButton = "Confirmar Cambios Preparados"
    override val refreshGitBtn = "Actualizar Estado del Repositorio"
    override val gitTerminalOutput = "Registros de la Consola de Git"
    override val activeBranchLabel = "RAMA ACTIVA:"
    override val stagedFilesLabel = "ARCHIVOS PREPARADOS (STAGED)"
    override val unstagedFilesLabel = "ARCHIVOS MODIFICADOS / NO RASTREADOS"
    override val remoteRepoUrl = "Dirección de Repositorio Remoto"
    override val customGitCmd = "Ejecutar Comando Git Personalizado"
    override val runCmdBtn = "Ejecutar Cmd"
    override val gitLogsLabel = "Flujo de Registros en Tiempo Real:"
    override val gitUsernameLabel = "Nombre de usuario de GitHub"
    override val gitTokenLabel = "Token de Acceso Personal (PAT)"
    override val projectStatsTitle = "Panel de Inteligencia del Proyecto"
    override val totalFilesLabel = "Archivos del Espacio de Trabajo"
    override val totalLinesLabel = "Líneas de Código Totales"
    override val lastSyncLabel = "Última Sincronización Remota"
    
    override val aiCopilotTitle = "Consola de Chat NADX Multi-Agente"
    override val agentStatusIdle = "Inactivo - Esperando consulta"
    override val agentStatusTyping = "El agente está redactando la respuesta..."
    override val enterPromptPlaceholder = "Pídale al constructor IA que escriba código, diseñe o explique algoritmos..."
    override val sendBtn = "Enviar"
    override val agentRoleLabel = "Seleccionar rol del agente experto"
    
    override val settingsTitle = "Configuración del Sistema"
    override val settingsSubtitle = "Personalice los temas visuales del editor, el tamaño de tipografía, sangría e idiomas."
    override val editorThemeLabel = "ESQUEMA Y TEMA VISUAL DEL EDITOR"
    override val interactivePreferences = "PREFERENCIAS INTERACTIVAS"
    override val fontSizeLabel = "Tamaño de Fuente del Editor"
    override val fontPreviewHeading = "Vista Previa de la Tipografía:"
    override val indentSizeLabel = "Tamaño de Sangría"
    override val indentSizeSub = "Espacios virtuales insertados al presionar Tab"
    override val spacesSuffix = "Espacio"
    override val aiBackendConfig = "CONFIGURACIÓN DEL BACKEND DE IA"
    override val apiKeyPlaceholder = "Introduzca la clave API oficial..."
    override val saveConfigBtn = "Guardar Configuración Técnica"
    override val selectLanguageLabel = "REPRESENTACIÓN DE IDIOMA E INTERFAZ"
    
    override val translateExplanation = "Traducir resumen completo."
    override val cleanTerminal = "Limpiar"
    override val copySuccess = "¡Código copiado al portapapeles con éxito!"
    override val themeMidnight = "Noche Cósmica (VS Code)"
    override val themeSolarized = "Océano Calmado"
    override val themeNeon = "Abismo de Neón"
    override val themeCyber = "Cyberpunk Hacker"
}

object FrenchStrings : LocaleStrings {
    override val layoutDirection = LayoutDirection.Ltr
    override val appName = "NADX Supreme Pro IDE"
    override val explorerTab = "Explorateur"
    override val editorTab = "Éditeur"
    override val gitTab = "Git"
    override val agentTab = "Agent"
    override val settingsTab = "Options"
    
    override val workspaceExplorerTitle = "EXPLORATEUR DU PROJET"
    override val emptyWorkspace = "Espace de travail vide. Ajoutez des fichiers."
    override val noFileSelected = "Aucun fichier actif sélectionné."
    override val enterFileName = "Entrez le nom du fichier (ex. app.py, index.html)"
    override val fileEmptyError = "Le nom du fichier ne peut pas être vide !"
    override val createFile = "Créer le fichier"
    override val cancel = "Annuler"
    override val deleteFileConfirm = "Êtes-vous sûr de vouloir supprimer ce fichier ?"
    
    override val runCode = "EXÉCUTER"
    override val aiActions = "ACTIONS DE L'ASSISTANT IA"
    override val explainCode = "Expliquer le Code"
    override val fixBugs = "Réparer les Erreurs"
    override val optimizeCode = "Optimiser la Complexité"
    override val transComments = "Traduire les commentaires"
    override val terminalOutputTitle = "Sortie de la Console de Code"
    override val charactersLabel = "Caract"
    override val linesLabel = "Lignes"
    override val searchPlaceholder = "Chercher texte..."
    override val replacePlaceholder = "Remplacer par..."
    override val formatCode = "Formater le Code"
    
    override val aiGitAssistant = "Assistant IA de Git & Exportation"
    override val gitAssistantDesc = "Permettez à l'agent IA d'analyser vos modifications, de rédiger vos commits ou de publier automatiquement."
    override val suggestedCommit = "Message de commit suggéré par l'IA :"
    override val applyCommit = "Appliquer au champ de commit"
    override val draftMessage = "Créer Message"
    override val autoStagePush = "Stage & Push Auto"
    override val analyzeDiffChat = "Analyser les différences avec l'assistant (dans le chat)"
    override val commitCardTitle = "Gestion des Commits & Publication"
    override val commitMsgPlaceholder = "Saisissez la description du commit..."
    override val commitButton = "Commiter les fichiers préparés"
    override val refreshGitBtn = "Actualiser le statut du dépôt"
    override val gitTerminalOutput = "Console d'activité Git"
    override val activeBranchLabel = "BRANCHE ACTIVE :"
    override val stagedFilesLabel = "FICHIERS PRÉPARÉS (STAGED)"
    override val unstagedFilesLabel = "FICHIERS MODIFIÉS / NON RASTREADOS"
    override val remoteRepoUrl = "Adresse de synchronisation du dépôt distant"
    override val customGitCmd = "Exécuter une commande Git personnalisée"
    override val runCmdBtn = "Lancer Cmd"
    override val gitLogsLabel = "Journal des activités en temps réel :"
    override val gitUsernameLabel = "Nom d'utilisateur GitHub"
    override val gitTokenLabel = "Jeton d'accès personnel GitHub (PAT)"
    override val projectStatsTitle = "Tableau de bord de l'intelligence du projet"
    override val totalFilesLabel = "Fichiers de l'espace de travail"
    override val totalLinesLabel = "Total des lignes de code"
    override val lastSyncLabel = "Dernière synchronisation à distance"
    
    override val aiCopilotTitle = "NADX Chat Console Multi-Agent"
    override val agentStatusIdle = "Inactif - Prêt à répondre"
    override val agentStatusTyping = "L'agent rédige sa réponse..."
    override val enterPromptPlaceholder = "Demandez à l'assistant d'écrire du code, de concevoir une interface..."
    override val sendBtn = "Envoyer"
    override val agentRoleLabel = "Sélectionner le rôle de l'expert IA"
    
    override val settingsTitle = "Options et Configuration"
    override val settingsSubtitle = "Personnalisez l'apparence de l'éditeur, la police, l'indentation et les langues."
    override val editorThemeLabel = "SCHÉMA DE COULEURS DE L'ÉDITEUR"
    override val interactivePreferences = "PRÉFÉRENCES INTERACTIVES"
    override val fontSizeLabel = "Taille de police"
    override val fontPreviewHeading = "Aperçu de la Typographie :"
    override val indentSizeLabel = "Taille de tabulation"
    override val indentSizeSub = "Espaces insérés lors de l'appui sur Tab"
    override val spacesSuffix = "Espaces"
    override val aiBackendConfig = "CONFIGURATION TECHNIQUES DE L'IA"
    override val apiKeyPlaceholder = "Entrez votre clé API privée..."
    override val saveConfigBtn = "Sauvegarder la configuration"
    override val selectLanguageLabel = "LOCALISATION & LANGUES DISPONIBLES"
    
    override val translateExplanation = "Traduire le résumé complet."
    override val cleanTerminal = "Vider la console"
    override val copySuccess = "Code copié dans le presse-papiers avec succès !"
    override val themeMidnight = "Aura de Minuit"
    override val themeSolarized = "Océan Solarisé"
    override val themeNeon = "Abîme de Néon"
    override val themeCyber = "Cyber Monokai"
}

fun getStrings(languageName: String): LocaleStrings {
    return when (languageName) {
        "العربية" -> ArabicStrings
        "Español" -> SpanishStrings
        "Français" -> FrenchStrings
        else -> EnglishStrings
    }
}
