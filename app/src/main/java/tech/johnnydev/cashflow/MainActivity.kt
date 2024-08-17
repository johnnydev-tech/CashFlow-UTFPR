package tech.johnnydev.cashflow


import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import tech.johnnydev.cashflow.database.DatabaseHandler
import tech.johnnydev.cashflow.databinding.ActivityMainBinding
import tech.johnnydev.cashflow.entity.Transaction
import tech.johnnydev.cashflow.entity.TransactionType
import java.sql.Date

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHandler: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.main)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        setDropdowns()
        onSelectDate()
        createLaunch()
        onOpenLaunch()

    }

    override fun onStart() {
        super.onStart()
        dbHandler = DatabaseHandler(this)
    }

    private fun onSelectDate() {
        binding.etDate.setOnClickListener {
            val calendar = Calendar.getInstance()

            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val date = Date(year - 1900, month, dayOfMonth)
                    binding.etDate.setText(date.toString())
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun createLaunch() {
        binding.buttonLaunch.setOnClickListener {

            if (binding.etValue.text.isEmpty() || binding.etDate.text.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val type = TransactionType.entries[binding.spinnerType.selectedItemPosition]
            val detail = binding.spinnerDetails.selectedItem.toString()
            val value = binding.etValue.text.toString().toDouble()
            val date = binding.etDate.text.toString()

            val transaction = Transaction(type, detail, value, date)


            dbHandler.insert(transaction)
            Toast.makeText(this, "Lançamento inserido com sucesso", Toast.LENGTH_SHORT).show()
            clearFieldsAndUnfocus()
        }

    }


    private fun onOpenLaunch(){
        binding.btGoToList.setOnClickListener {
            val intent = Intent(this, LaunchsActivity::class.java)
            startActivity(intent)
        }
    }


    private fun setDropdowns(){

        val creditDetails = arrayOf("Salário", "Extra")
        val debitDetails = arrayOf("Alimentação", "Transporte", "Saúde", "Moradia")


        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            TransactionType.entries.map { it.label })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerType.adapter = adapter

        binding.spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        val adapterDetail = ArrayAdapter(
                            this@MainActivity,
                            android.R.layout.simple_spinner_item,
                            creditDetails
                        )
                        adapterDetail.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spinnerDetails.adapter = adapterDetail
                    }

                    1 -> {
                        val adapterDetail = ArrayAdapter(
                            this@MainActivity,
                            android.R.layout.simple_spinner_item,
                            debitDetails
                        )
                        adapterDetail.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spinnerDetails.adapter = adapterDetail
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }


    private fun clearFieldsAndUnfocus() {
        binding.etValue.text.clear()
        binding.etDate.text.clear()
        binding.etValue.clearFocus()
        binding.etDate.clearFocus()

        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etValue.windowToken, 0)
    }
}