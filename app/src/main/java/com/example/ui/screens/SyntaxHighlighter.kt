package com.example.ui.screens

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CodeVisualTransformation(private val extension: String = "kt") : VisualTransformation {
    
    companion object {
        private val numberRegex = Regex("\\b\\d+\\b")
        private val funRegex = Regex("\\b([A-Za-z0-9_]+)\\s*\\(")
        private val stringRegex = Regex("\".*?\"")
        private val commentRegex = Regex("//.*")
        
        private val kotlinKeywords = listOf("fun", "val", "var", "class", "interface", "object", "return", "if", "else", "for", "while", "true", "false", "import", "package", "private", "public", "protected", "suspend", "null")
        private val pythonKeywords = listOf("def", "class", "if", "elif", "else", "for", "while", "import", "from", "return", "True", "False", "None", "pass", "and", "or", "not", "as", "try", "except")
        private val jsKeywords = listOf("function", "const", "let", "var", "if", "else", "for", "while", "return", "true", "false", "null", "undefined", "import", "export", "class", "new", "this")
        private val cppKeywords = listOf("int", "float", "double", "char", "void", "return", "if", "else", "for", "while", "class", "public", "private", "protected", "include", "using", "namespace", "struct")
        private val javaKeywords = listOf("class", "public", "private", "protected", "static", "void", "int", "boolean", "if", "else", "for", "while", "return", "true", "false", "null", "new", "import", "package", "interface", "extends", "implements")
        private val dartKeywords = listOf("class", "extends", "with", "implements", "final", "const", "var", "void", "int", "double", "String", "bool", "if", "else", "for", "while", "return", "true", "false", "this", "super", "new", "import", "export", "async", "await", "Future", "static")
        private val htmlKeywords = listOf("html", "head", "body", "div", "span", "a", "script", "style", "link", "meta", "title", "h1", "h2", "h3", "h4", "p", "button", "input", "form")
        private val cssKeywords = listOf("color", "background", "margin", "padding", "border", "display", "flex", "grid", "width", "height", "font", "text", "position", "top", "left", "right", "bottom")

        private val keywordRegexCache = java.util.concurrent.ConcurrentHashMap<String, Regex>()

        fun getKeywordRegex(ext: String): Regex {
            return keywordRegexCache.getOrPut(ext) {
                val keywords = when(ext) {
                    "py" -> pythonKeywords
                    "js", "ts", "jsx", "tsx" -> jsKeywords
                    "cpp", "c", "h", "hpp" -> cppKeywords
                    "java" -> javaKeywords
                    "dart" -> dartKeywords
                    "html", "htm" -> htmlKeywords
                    "css" -> cssKeywords
                    else -> kotlinKeywords
                }
                Regex("\\b(${keywords.joinToString("|")})\\b")
            }
        }
    }

    override fun filter(text: AnnotatedString): TransformedText {
        val code = text.text
        if (code.length > 500) {
            // Skip highlighting for very large files to prevent ANRs
            return TransformedText(
                AnnotatedString.Builder(code).apply {
                    addStyle(SpanStyle(color = Color(0xFFD4D4D4)), 0, code.length)
                }.toAnnotatedString(), 
                OffsetMapping.Identity
            )
        }
        val annotatedString = AnnotatedString.Builder(code)

        val keywordRegex = getKeywordRegex(extension)

        val stringColor = Color(0xFFCE9178) 
        val commentColor = Color(0xFF6A9955) 
        val keywordColor = Color(0xFF569CD6) 
        val numberColor = Color(0xFFB5CEA8) 
        val funColor = Color(0xFFDCDCAA) 

        // Numbers
        numberRegex.findAll(code).forEach { match ->
            annotatedString.addStyle(SpanStyle(color = numberColor), match.range.first, match.range.last + 1)
        }

        // Keywords
        keywordRegex.findAll(code).forEach { match ->
            annotatedString.addStyle(SpanStyle(color = keywordColor), match.range.first, match.range.last + 1)
        }

        // Functions
        funRegex.findAll(code).forEach { match ->
            val funNameGroup = match.groups[1]
            if (funNameGroup != null && funNameGroup.value !in listOf("if", "for", "while", "catch", "switch", "when")) {
                annotatedString.addStyle(SpanStyle(color = funColor), funNameGroup.range.first, funNameGroup.range.last + 1)
            }
        }

        // Strings
        stringRegex.findAll(code).forEach { match ->
            annotatedString.addStyle(SpanStyle(color = stringColor), match.range.first, match.range.last + 1)
        }

        // Comments
        commentRegex.findAll(code).forEach { match ->
            annotatedString.addStyle(SpanStyle(color = commentColor), match.range.first, match.range.last + 1)
        }
        
        return TransformedText(annotatedString.toAnnotatedString(), OffsetMapping.Identity)
    }
}
