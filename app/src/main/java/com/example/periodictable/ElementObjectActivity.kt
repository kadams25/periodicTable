package com.example.periodictable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.periodictable.databinding.ActivityElementObjectBinding
import java.util.*

class ElementObjectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityElementObjectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityElementObjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = getIntent()
        val data: Element = intent.getSerializableExtra(
            getString(R.string.intent_data_key)
        ) as Element

        val widgets = listOf<TextView>(
            binding.atomicNumberTextView, binding.symbolTextView, binding.nameTextView,
            binding.atomicMassTextView, binding.electronicConfigTextView,
            binding.atomicRadiusTextView, binding.standardStateTextView,
            binding.bondingTypeTextView, binding.meltingPointTextView, binding.boilingPointTextView,
            binding.densityTextView, binding.groupBlockTextView, binding.yearDiscoveredTextView,
            binding.blockTextView, binding.periodTextView, binding.groupTextView
        )

        val stringIds = listOf<Int>(
            R.string.atomicNum, R.string.symbol, R.string.element, R.string.atomicMass,
            R.string.electronicConfig, R.string.atomicRadius, R.string.standardState,
            R.string.bondingType, R.string.meltingPoint, R.string.boilingPoint, R.string.density,
            R.string.groupBlock, R.string.yearDiscovered, R.string.block, R.string.period,
            R.string.group
        )

        val elementData = listOf<String>(
            data.atomicNumber, data.symbol, data.name, data.atomicMass,
            data.electronicConfiguration, data.atomicRadius, data.standardState, data.bondingType,
            data.meltingPoint, data.boilingPoint, data.density, data.groupBlock,
            data.yearDiscovered, data.block, data.period, data.group
        )

        for (index in widgets.indices) {
            if (elementData[index].contains("unknown")) {
                widgets[index].setText(getResources().getString(stringIds[index], "Unknown"))
            } else {
                widgets[index].setText(
                    getResources().getString(
                        stringIds[index],
                        elementData[index].replaceFirstChar { letter ->
                            if (letter.isLowerCase()) letter.titlecase(Locale.getDefault())
                            else letter.toString()
                        })
                )
            }
        }
    }
}