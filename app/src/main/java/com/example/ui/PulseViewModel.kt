package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first

class PulseViewModel(private val dao: PulseDao) : ViewModel() {
    val incomes = dao.getIncomes().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val expenses = dao.getExpenses().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val leads = dao.getLeads().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val invoices = dao.getInvoices().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val outreach = dao.getOutreach().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalIncome = incomes.map { list -> list.sumOf { it.amount } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalExpense = expenses.map { list -> list.sumOf { it.amount } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val netProfit = combine(totalIncome, totalExpense) { i, e -> i - e }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val earningsGoal = dao.getConfig("earnings_goal").map { it?.value?.toDoubleOrNull() ?: 10000.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 10000.0)

    val goalProgress = combine(netProfit, earningsGoal) { profit, goal ->
        if (goal > 0) ((profit / goal).coerceIn(0.0, 1.0)).toFloat() else 0f
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)

    val themeMode = dao.getConfig("theme_mode").map { it?.value?.toIntOrNull() ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun setThemeMode(mode: Int) {
        viewModelScope.launch {
            dao.insertConfig(Config("theme_mode", mode.toString()))
        }
    }

    fun setGoal(goal: Double) {
        viewModelScope.launch {
            dao.insertConfig(Config("earnings_goal", goal.toString()))
        }
    }

    fun addIncome(amount: Double, note: String) {
        viewModelScope.launch {
            dao.insertIncome(Income(amount = amount, date = System.currentTimeMillis(), note = note))
        }
    }

    fun addExpense(amount: Double, category: String, note: String) {
        viewModelScope.launch {
            dao.insertExpense(Expense(amount = amount, date = System.currentTimeMillis(), category = category, note = note))
        }
    }

    fun addLead(name: String, value: Double) {
        viewModelScope.launch {
            dao.insertLead(Lead(name = name, status = "Lead", value = value))
        }
    }

    fun addOutreach(contact: String, channel: String, status: String = "Sent") {
        viewModelScope.launch {
            val followUp = System.currentTimeMillis() + 3 * 86400000L // 3 days
            dao.insertOutreach(Outreach(contact = contact, channel = channel, dateSent = System.currentTimeMillis(), followUpDate = followUp, status = status))
        }
    }

    fun updateLeadStatus(id: Int, status: String) {
        viewModelScope.launch {
            dao.updateLeadStatus(id, status)
        }
    }

    fun addInvoice(number: String, client: String, amount: Double, daysDue: Int, desc: String) {
        viewModelScope.launch {
            val due = System.currentTimeMillis() + daysDue * 86400000L
            dao.insertInvoice(Invoice(number = number, client = client, amount = amount, dueDate = due, description = desc, status = "Draft"))
        }
    }

    fun updateInvoiceStatus(id: Int, status: String) {
        viewModelScope.launch {
            dao.updateInvoiceStatus(id, status)
            if (status == "Paid") {
                val invoice = dao.getInvoices().first().find { it.id == id }
                if (invoice != null) {
                    addIncome(invoice.amount, "Invoice ${invoice.number} - ${invoice.client}")
                }
            }
        }
    }
    private val _generatedIdea = kotlinx.coroutines.flow.MutableStateFlow<String?>(null)
    val generatedIdea: StateFlow<String?> = _generatedIdea

    fun generateContentIdea(topic: String) {
        viewModelScope.launch {
            _generatedIdea.value = "Generating..."
            val prompt = "You are a marketing assistant for a business CRM app. Generate 3 short social media post ideas about $topic. Make them engaging."
            val response = generateIdea(prompt)
            _generatedIdea.value = response
        }
    }
}

class PulseViewModelFactory(private val dao: PulseDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PulseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PulseViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
