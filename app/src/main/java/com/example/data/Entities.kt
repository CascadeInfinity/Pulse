package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "config")
data class Config(
    @PrimaryKey val key: String,
    val value: String
)

@Entity(tableName = "income")
data class Income(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val date: Long,
    val note: String? = null
)

@Entity(tableName = "expense")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val date: Long,
    val category: String,
    val note: String? = null
)

@Entity(tableName = "lead")
data class Lead(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val status: String,
    val value: Double,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "invoice")
data class Invoice(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val number: String,
    val client: String,
    val description: String,
    val amount: Double,
    val dueDate: Long,
    val status: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "outreach")
data class Outreach(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val contact: String,
    val channel: String,
    val dateSent: Long,
    val followUpDate: Long,
    val status: String
)
