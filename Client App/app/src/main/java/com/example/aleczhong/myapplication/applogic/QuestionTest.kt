package com.example.aleczhong.myapplication.applogic

data class QuestionTest @JvmOverloads constructor(val content: String, var score: Int = 5)
data class ChatMessageTest(val content: String, val id: Int, val type: MessageType)