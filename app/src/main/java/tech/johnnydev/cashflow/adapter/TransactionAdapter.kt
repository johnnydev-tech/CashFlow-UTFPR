package tech.johnnydev.cashflow

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tech.johnnydev.cashflow.databinding.ItemTransactionBinding
import tech.johnnydev.cashflow.entity.Transaction

class TransactionAdapter(private val transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding =
            ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        with(holder.binding) {
            textviewType.text = transaction.type.label
            textviewDetail.text = transaction.detail
            textviewValue.text = transaction.value.toString()
            textviewDate.text = transaction.date
        }
        Log.d("TransactionAdapter", "Binding transaction at position $position")
    }

    override fun getItemCount(): Int = transactions.size
}