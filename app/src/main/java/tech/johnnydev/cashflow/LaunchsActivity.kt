package tech.johnnydev.cashflow

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import tech.johnnydev.cashflow.database.DatabaseHandler
import tech.johnnydev.cashflow.databinding.ActivityLaunchsBinding
import tech.johnnydev.cashflow.databinding.ActivityMainBinding

class LaunchsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLaunchsBinding
    private lateinit var dbHandler: DatabaseHandler
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
       binding = ActivityLaunchsBinding.inflate(layoutInflater)
        setContentView(binding.main)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}