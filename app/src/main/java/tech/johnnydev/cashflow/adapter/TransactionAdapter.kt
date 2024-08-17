package tech.johnnydev.cashflow

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tech.johnnydev.cashflow.databinding.ItemTransactionBinding
import tech.johnnydev.cashflow.entity.Transaction
import tech.johnnydev.cashflow.entity.TransactionType

class TransactionAdapter(private val transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding =
            ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.bind(transaction)
    }

    override fun getItemCount(): Int = transactions.size

    inner class TransactionViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) {
            binding.textviewType.text = transaction.type.label
            binding.textviewDetail.text = transaction.detail
            binding.textviewValue.text = transaction.value.toString()
            binding.textviewDate.text = transaction.date


            val arrowResId = if (transaction.type == TransactionType.CREDIT) {
                R.drawable.arrow_up
            } else {
                R.drawable.arrow_down
            }
            binding.imageviewArrow.setImageResource(arrowResId)
        }
    }
}