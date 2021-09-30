package com.example.mytest.ui

import android.app.Activity
import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter

import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.mytest.R

import com.example.mytest.Status
import com.example.mytest.databinding.ActivityMainBinding
import com.example.mytest.response.LanguagesText

import com.example.mytest.response.TranslatedText
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.async

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val arrayLangName: ArrayList<String> = arrayListOf()
    private val arrayLangCode: ArrayList<String> = arrayListOf()
    private var valueofSource: String? = null
    private var valueofTarget: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUp()

    }

    fun setUp() {
        getRequestLang()
        switchLang()
        binding.SourceET.doOnTextChanged { text, start, before, count ->
            getRequest(
                binding.SourceET.text.toString(),
                valueofSource.toString(),
                valueofTarget.toString()
            )
        }
        share()
        copy()
        remove()


    }

    //Translate: to collect data flow from repository By Coroutine
    private fun getRequest(q: String, sourceText: String, targetText: String) {
        lifecycleScope.launch {
            TranslateRepository.getInfoTranslate(q, sourceText, targetText)
                .collect { getResultRequest(it) }
        }
    }

    //Translate: to get response and show result in UI at the same time
    fun getResultRequest(response: Status<TranslatedText>) {
        return when (response) {
            is Status.Fail -> {
                Toast.makeText(
                    this@MainActivity,
                    "error can't access sorry ",
                    Toast.LENGTH_SHORT
                ).show()
            }
            is Status.Loading -> {
                Toast.makeText(
                    this@MainActivity,
                    "Loading access  ",
                    Toast.LENGTH_SHORT
                ).show()
            }
            is Status.Success -> {
                Toast.makeText(
                    this@MainActivity,
                    "Translate Done ",
                    Toast.LENGTH_SHORT
                ).show()
                binding.TargetTV.text = response.data.translatedText

            }
        }

    }

    //Languages: to collect data flow from repository By Coroutine
    private fun getRequestLang() {
        lifecycleScope.launch {
            TranslateRepository.getInfoLanguages().collect { getResultRequestLang(it) }
        }
    }

    //Languages: to get response
    fun getResultRequestLang(response: Status<LanguagesText>) {
        return when (response) {
            is Status.Success -> {
                setDataResult(response.data)

            }
            else -> Toast.makeText(
                this@MainActivity,
                "error can't access sorry ",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    //Languages: to show result in UI from response
    private fun setDataResult(response: LanguagesText) {
        response.forEach {
            arrayLangCode.add(it.code.toString())
            arrayLangName.add(it.name.toString())
        }
        val arrayAdapter = ArrayAdapter(this, R.layout.spinner_items, arrayLangName)
        binding.SourceSP.apply {
            setAdapter(arrayAdapter)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected( p0: AdapterView<*>?,  p1: View?, position: Int, p3: Long) {
                    valueofSource = arrayLangCode[position]
                    setSourceLang()
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
            binding.TragerSP.apply {
                setAdapter(arrayAdapter)
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                        valueofTarget = arrayLangCode[position]
                        setTargetLang()
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
            } }
    }

    fun switchLang() {

    binding.SwitchLanguages.setOnClickListener{

        val cacheLang = valueofSource
        valueofSource = valueofTarget
        setSourceLang()
        valueofTarget = cacheLang
        setTargetLang()


    } }

    private fun setSourceLang() {
        val sourceLang = valueofSource
        binding.SourceLanguageTop.text = sourceLang

    }

    private fun setTargetLang() {
        val targetLang = valueofTarget
        binding.TargetLanguageTop.text = targetLang

    }

    fun  share(){
        binding.ShareTranslation.setOnClickListener {
            if (binding.TargetTV.text.toString() != "") {
                //create share intent
                val share = Intent.createChooser(Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, binding.TargetTV.text)
                }, null)
                //share translation
                startActivity(share)
            }
        }
    }
    fun copy(){
        binding.CopyTranslation.setOnClickListener {
            if (binding.TargetTV.text.toString() != "") {
                //copy translated text to clipboard
                val clipboard: ClipboardManager =
                    getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData =
                    ClipData.newPlainText("translated text", binding.TargetTV.text)
                clipboard.setPrimaryClip(clip)
                //Message to inform the user
                Snackbar.make(
                    binding.CopyTranslation,
                    getString(R.string.copiedClipboard),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
    fun remove(){
        binding.RemoveSourceText.setOnClickListener {
            if (binding.TargetTV.text.toString() != "") {
                //Ask if the text really should be removed

                Snackbar.make(binding.RemoveSourceText, getString(R.string.rlyRemoveText), Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.remove) {
                        //remove text
                        binding.SourceET.text = null
                        binding.TargetTV.text = null
                    }
                    .show()
                //Hide keyboard
                val imm: InputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                val view: View? = currentFocus
                imm.hideSoftInputFromWindow(view?.windowToken, 0)
            }
        }
    }

}