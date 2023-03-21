package org.bmsk.webtoon

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import org.bmsk.webtoon.databinding.FragmentWebviewBinding

class WebViewFragment(private val position: Int) : Fragment() {

    var listener: OnTabLayoutNameChanged? = null

    lateinit var binding: FragmentWebviewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWebviewBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webView.webViewClient = WebtoonWebViewClient(binding.progressBar) { url ->
            activity?.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)?.edit {
                putString("tab$position", url)
            }
        }
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl("https://comic.naver.com/")

        initButton()
    }

    fun canGoBack(): Boolean {
        return binding.webView.canGoBack()
    }

    fun goBack() {
        binding.webView.goBack()
    }

    private fun initButton() {
        initBackToLastViewButton()
        initChangeTabNameButton()
    }

    private fun initBackToLastViewButton() {
        binding.backToLastButton.setOnClickListener {
            val sharedPreferences = activity?.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)
            val url = sharedPreferences?.getString("tab$position", "")
            if(url.isNullOrEmpty()) {
                Toast.makeText(context, "마지막 저장 시점이 없습니다", Toast.LENGTH_SHORT).show()
            } else {
                binding.webView.loadUrl(url)
            }
        }
    }

    private fun initChangeTabNameButton() {
        binding.changeTabNameButton.setOnClickListener {
            with(AlertDialog.Builder(context)) {
                val editText = EditText(context)
                setView(editText)
                setPositiveButton("저장") { _, _ ->
                    activity?.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)?.edit {
                        putString("tab${position}_name", editText.text.toString())
                        listener?.nameChanged(position, editText.text.toString())
                    }
                }
                setNegativeButton("취소") { dialogInterface, _ ->
                    dialogInterface.cancel()
                }

                show()
            }
        }
    }

    companion object {
        const val SHARED_PREFERENCE = "WEB_HISTORY"
    }
}

interface OnTabLayoutNameChanged {
    fun nameChanged(position: Int, name: String)
}