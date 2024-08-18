package tech.johnnydev.cashflow


import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import tech.johnnydev.cashflow.adapter.TransactionAdapter
import tech.johnnydev.cashflow.database.DatabaseHandler
import tech.johnnydev.cashflow.databinding.ActivityLaunchsBinding

class LaunchsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLaunchsBinding
    private lateinit var dbHandler: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLaunchsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHandler = DatabaseHandler(this)

        val transactions = dbHandler.list()
        val adapter = TransactionAdapter(transactions)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }
}