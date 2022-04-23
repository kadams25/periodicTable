package com.example.periodictable

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import com.example.periodictable.databinding.ActivityAcknowledgmentBinding

class AcknowledgmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAcknowledgmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAcknowledgmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = getIntent()
        val data: Element = intent.getSerializableExtra(
            getString(R.string.intent_data_key)
        ) as Element

        val imageUrl = "<a href=\"https://images-of-elements.com/${data.name.lowercase()}.jpg\">Link</a>"

        binding.titleTextView.setText(getResources().getString(R.string.title, data.name.lowercase()))

        binding.creatorTextView.setText(R.string.creator)
        binding.creatorTextView.setMovementMethod(LinkMovementMethod.getInstance())


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.sourceTextView.setText(Html.fromHtml("Source: ${imageUrl}", Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.sourceTextView.setText(Html.fromHtml("Source: ${imageUrl}"));
        }

        binding.sourceTextView.setMovementMethod(LinkMovementMethod.getInstance())

        binding.licenseTextView.setText(R.string.license)
        binding.licenseTextView.setMovementMethod(LinkMovementMethod.getInstance())
    }
}