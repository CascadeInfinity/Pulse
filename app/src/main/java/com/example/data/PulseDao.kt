package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PulseDao {
    @Query("SELECT * FROM config WHERE `key` = :key")
    fun getConfig(key: String): Flow<Config?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(config: Config)

    @Query("SELECT * FROM income ORDER BY date DESC")
    fun getIncomes(): Flow<List<Income>>

    @Insert
    suspend fun insertIncome(income: Income)

    @Query("SELECT * FROM expense ORDER BY date DESC")
    fun getExpenses(): Flow<List<Expense>>

    @Insert
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM lead ORDER BY createdAt DESC")
    fun getLeads(): Flow<List<Lead>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLead(lead: Lead)

    @Query("UPDATE lead SET status = :status WHERE id = :id")
    suspend fun updateLeadStatus(id: Int, status: String)

    @Query("SELECT * FROM invoice ORDER BY dueDate ASC")
    fun getInvoices(): Flow<List<Invoice>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvoice(invoice: Invoice)

    @Query("UPDATE invoice SET status = :status WHERE id = :id")
    suspend fun updateInvoiceStatus(id: Int, status: String)
}
