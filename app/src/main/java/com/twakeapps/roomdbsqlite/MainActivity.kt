package com.twakeapps.roomdbsqlite

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.twakeapps.roomdbsqlite.databinding.ActivityMainBinding
import com.twakeapps.roomdbsqlite.databinding.AlertInsertDataBinding
import com.twakeapps.roomdbsqlite.db.ContactEntity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val items: ArrayList<ContactEntity> = arrayListOf()
    private val mAdapter = ContractAdapter(items)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

        binding.btnAddContract.setOnClickListener {
            addContractAlert(true, null)
        }
    }

    private fun initUI() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recyclerView.adapter = mAdapter

        mAdapter.setEditClick {
            addContractAlert(false, it)
            loadData() // Just reload data after editing, no need to reinitialize the UI
        }

        mAdapter.setDeleteClick {
            showDeleteAlert(it)
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            val allData = App.db.contractDao().getAllContract()
            Log.d("<ALL_DATA>", "loadData: $allData")

            items.clear()
            items.addAll(allData)
            mAdapter.notifyDataSetChanged() // Notify adapter to reflect the new data
        }
    }

    private fun addContractAlert(isAdd: Boolean, contactEntity: ContactEntity?) {
        AlertDialog.Builder(this).create().apply {
            val bindingAlert = AlertInsertDataBinding.inflate(layoutInflater)
            setView(bindingAlert.root)

            if (!isAdd) {
                bindingAlert.etFirstName.setText(contactEntity?.firstName ?: "")
                bindingAlert.etLastName.setText(contactEntity?.lastName ?: "")
                bindingAlert.etPhoneNumber.setText(contactEntity?.phoneNumber ?: "")
                bindingAlert.btnSave.text = "Update"
            }

            bindingAlert.btnCancel.setOnClickListener { dismiss() }

            bindingAlert.btnSave.setOnClickListener {
                dismiss()

                val fName = bindingAlert.etFirstName.text.toString()
                val lName = bindingAlert.etLastName.text.toString()
                val pNumber = bindingAlert.etPhoneNumber.text.toString()

                if (isAdd) {
                    insertData(fName, lName, pNumber)
                } else {
                    contactEntity?.id?.let { updateData(it, fName, lName, pNumber) }
                }
            }
        }.show()
    }

    private fun insertData(fName: String, lName: String, pNumber: String) {
        lifecycleScope.launch {
            App.db.contractDao().insertAll(ContactEntity(0, firstName = fName, lastName = lName, phoneNumber = pNumber))
            loadData() // Reload data after insertion
        }
    }

    private fun updateData(id: Int, fName: String, lName: String, pNumber: String) {
        val cont = ContactEntity(id, fName, lName, pNumber)

        lifecycleScope.launch {
            App.db.contractDao().updateData(cont)
            loadData() // Reload data after update
        }
    }

    private fun showDeleteAlert(mContract: ContactEntity) {
        AlertDialog.Builder(this)
                .setTitle("Delete Alert")
                .setMessage("Are you sure you want to delete ${mContract.firstName} ${mContract.lastName}?")
                .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                .setPositiveButton("Yes") { dialog, _ ->
                    dialog.dismiss()
                    deleteContractData(mContract)
                }
                .show()
    }

    private fun deleteContractData(mContract: ContactEntity) {
        lifecycleScope.launch {
            App.db.contractDao().deleteData(mContract)
            loadData() // Reload data after deletion
        }
    }
}
