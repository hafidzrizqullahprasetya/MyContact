package com.example.mycontact

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycontact.data.Contact
import com.example.mycontact.databinding.ActivityMainBinding
import com.example.mycontact.databinding.DialogAddContactBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ContactViewModel by viewModels()
    private lateinit var adapter: ContactViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeContacts()
        setupFab()
    }

    private fun setupRecyclerView() {
        adapter = ContactViewAdapter(
            onEditClick = { contact ->
                showEditDialog(contact)
            },
            onDeleteClick = { contact ->
                showDeleteDialog(contact)
            }
        )

        binding.recyclerViewContacts.apply {
            this.adapter = this@MainActivity.adapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun observeContacts() {
        lifecycleScope.launch {
            viewModel.contacts.collect { contacts ->
                adapter.submitList(contacts)
            }
        }
    }

    private fun setupFab() {
        binding.fabAddContact.setOnClickListener {
            showAddDialog()
        }
    }

    private fun showAddDialog() {
        val dialogBinding = DialogAddContactBinding.inflate(layoutInflater)

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setPositiveButton("Add") { _, _ ->
                val name = dialogBinding.editName.text.toString()
                val phone = dialogBinding.editPhone.text.toString()

                if (name.isNotBlank() && phone.isNotBlank()) {
                    viewModel.insert(name, phone)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showEditDialog(contact: Contact) {
        val dialogBinding = DialogAddContactBinding.inflate(layoutInflater)

        dialogBinding.apply {
            txtTitle.text = "Edit Contact"
            editName.setText(contact.name)
            editPhone.setText(contact.phone)
        }

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setPositiveButton("Update") { _, _ ->
                val name = dialogBinding.editName.text.toString()
                val phone = dialogBinding.editPhone.text.toString()

                if (name.isNotBlank() && phone.isNotBlank()) {
                    val updatedContact = contact.copy(name = name, phone = phone)
                    viewModel.update(updatedContact)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteDialog(contact: Contact) {
        AlertDialog.Builder(this)
            .setTitle("Delete Contact")
            .setMessage("Apakah Anda yakin untuk menghapusnya? ${contact.name}?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.delete(contact)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}