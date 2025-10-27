package com.example.mycontact

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycontact.data.AppDatabase
import com.example.mycontact.data.Contact
import com.example.mycontact.data.ContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ContactRepository
    val contacts: Flow<List<Contact>>

    init {
        val contactDao = AppDatabase.get(application).contactDao()
        repository = ContactRepository(contactDao)
        contacts = repository.getAllContacts()
    }

    fun insert(name: String, phone: String) = viewModelScope.launch {
        val contact = Contact(id = 0, name = name, phone = phone)
        repository.insert(contact)
    }

    fun update(contact: Contact) = viewModelScope.launch {
        repository.update(contact)
    }

    fun delete(contact: Contact) = viewModelScope.launch {
        repository.delete(contact)
    }
}