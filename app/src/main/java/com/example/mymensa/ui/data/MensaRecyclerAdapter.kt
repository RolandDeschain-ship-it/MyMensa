package com.example.mymensa.ui.data

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mymensa.R
import kotlinx.android.synthetic.main.mensa_chip_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.qualifier.qualifier
import org.koin.java.KoinJavaComponent.inject

class MensaRecyclerAdapter(private val mensa: ArrayList<Mensa>): RecyclerView.Adapter<MensaRecyclerAdapter.MensaChipHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MensaRecyclerAdapter.MensaChipHolder {
        val inflateView = parent.inflate(R.layout.mensa_chip_item)
        return MensaChipHolder(inflateView, parent)
    }

    override fun onBindViewHolder(holder: MensaRecyclerAdapter.MensaChipHolder, position: Int) {
        val itemMensa = mensa[position]
        holder.bindMensa(itemMensa)
    }

    override fun getItemCount() = mensa.size

    class MensaChipHolder(v: View, parent: ViewGroup): RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private lateinit var mensa: Mensa
        private lateinit var parent: ViewGroup

        private val dataStore: DataStorageRepository by inject(DataStorageRepository::class.java)

        init {
            this.parent = parent
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
                v.mensa_chip.setBackgroundColor(ContextCompat.getColor(v.context, R.color.purple_700))
            CoroutineScope(Dispatchers.IO).launch {
                var currentMensa = dataStore.get("mensen")
                if (!v.mensa_chip.isChecked) {
                    dataStore.removeMensa(mensa)
                }else {
                    dataStore.addMensa(mensa)
                }
            }
        }

        fun bindMensa(mensa: Mensa) {
            this.mensa = mensa
            view.mensa_chip.text = mensa.name
        }

        companion object {
            private val MENSA_KEY = "MENSA"
        }

    }

    data class Mensa(
        val name: String = "",
        val id: String = ""
    )
}
