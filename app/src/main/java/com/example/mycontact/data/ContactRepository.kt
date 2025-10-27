package com.example.mycontact.data

import kotlinx.coroutines.flow.Flow

class ContactRepository(private val contactDao: ContactDao) {

    fun getAllContacts(): Flow<List<Contact>> = contactDao.getAllContacts()

    suspend fun insert(contact: Contact): Long = contactDao.insert(contact)

    suspend fun update(contact: Contact): Int = contactDao.update(contact)

    suspend fun delete(contact: Contact): Int = contactDao.delete(contact)

    suspend fun getContactById(id: Long): Contact? = contactDao.getContactById(id)
}