package com.example.roomudemyprep

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomudemyprep.databinding.ActivityMainBinding
import com.example.roomudemyprep.databinding.DialogUpdateBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var binding : ActivityMainBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val employeeDao = (application as EmployeeApp).db.employeeDao()
        binding?.btnAdd?.setOnClickListener {

            addRecord(employeeDao)
        }
lifecycleScope.launch{

    employeeDao.fetchAllEmplye().collect {

val list=ArrayList(it)

        setupListDataintoRv(list,employeeDao)
    }
}


    }

fun addRecord(employdao: EmplyeeDao)
{

    val name =binding?.etName?.text.toString()
    val email =binding?.etEmailId?.text.toString()
    if(name.isNotEmpty() && email.isNotEmpty())
    {
        lifecycleScope.launch{

            employdao.insert(EmployeEntity(name=name, email = email))
            Toast.makeText(applicationContext,"added",Toast.LENGTH_LONG)
            binding?.etName?.text?.clear()
            binding?.etEmailId?.text?.clear()


        }

    }

}
private fun setupListDataintoRv(employeesList:ArrayList<EmployeEntity>,employeeDao: EmplyeeDao)
{
    if (employeesList.isNotEmpty()) {


        // Adapter class is initialized and list is passed in the param.
        val itemAdapter = ItemAdapter(employeesList,
            { updateid ->
updateRecordDialog(updateid,employeeDao)

            },{   deleteid->
                lifecycleScope.launch {
                    employeeDao.fetchEmplyeByID(deleteid).collect {
                        if (it != null) {
                            deleteRecordAlertDialog(deleteid, employeeDao, it)
                        }
                    }
                }
            })


            // Set the LayoutManager that this RecyclerView will use.
            binding?.rvItemsList?.layoutManager = LinearLayoutManager(this)

        // adapter instance is set to the recyclerview to inflate the items.
        binding?.rvItemsList?.adapter = itemAdapter
        binding?.rvItemsList?.visibility = View.VISIBLE
        binding?.tvNoRecordsAvailable?.visibility = View.GONE
    } else {

        binding?.rvItemsList?.visibility = View.GONE
        binding?.tvNoRecordsAvailable?.visibility = View.VISIBLE
    }


}
fun updateRecordDialog(id: Int,employDoa: EmplyeeDao)
{
    val updateDialog = Dialog(this, R.style.Theme_Dialog)
    updateDialog.setCancelable(false)

    val binding = DialogUpdateBinding.inflate(layoutInflater)
    updateDialog.setContentView(binding.root)
    lifecycleScope.launch {
        employDoa.fetchEmplyeByID(id).collect {
            if (it != null) {
                binding.etUpdateName.setText(it.name)
                binding.etUpdateEmailId.setText(it.email)
            }
        }
    }
    binding.tvUpdate.setOnClickListener {

        val name = binding.etUpdateName.text.toString()
        val email = binding.etUpdateEmailId.text.toString()

        if (name.isNotEmpty() && email.isNotEmpty()) {
            lifecycleScope.launch {
                employDoa.update(EmployeEntity(id, name, email))
                Toast.makeText(applicationContext, "Record Updated.", Toast.LENGTH_LONG)
                    .show()
                updateDialog.dismiss() // Dialog will be dismissed
            }
        } else {
            Toast.makeText(
                applicationContext,
                "Name or Email cannot be blank",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    binding.tvCancel.setOnClickListener{
        updateDialog.dismiss()
    }
    //Start the dialog and display it on screen.
    updateDialog.show()
}


    fun deleteRecordAlertDialog(id:Int,employeeDao: EmplyeeDao,employee: EmployeEntity) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Delete Record")
        //set message for alert dialog
        builder.setMessage("Are you sure you wants to delete ${employee.name}.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            lifecycleScope.launch {
                employeeDao.delete(EmployeEntity(id))
                Toast.makeText(
                    applicationContext,
                    "Record deleted successfully.",
                    Toast.LENGTH_LONG
                ).show()

                dialogInterface.dismiss() // Dialog will be dismissed
            }

        }


        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }

}
