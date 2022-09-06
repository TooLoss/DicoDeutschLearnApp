package com.example.projectdicodeutsch

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment


class NewUpdateDialogFragment: DialogFragment() {
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.fragment_update_dialogue, container, false)

        var updateAppButton: Button = rootView.findViewById(R.id.updateAppButton)
        updateAppButton.setOnClickListener {
            val uri: Uri =
                Uri.parse("https://github.com/TooLoss/DicoDeutschLearnApp/releases/latest") // missing 'http://' will cause crashed

            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        var updateSkipButton: Button = rootView.findViewById(R.id.updateSkipButton)
        updateSkipButton.setOnClickListener {
            dismiss()
        }

        return rootView
    }
}