package edu.kokhan.secondcrime

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.NumberPicker
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

private const val API_URL = "http://10.6.177.78:8080/result"
private const val PARAM_PART_URL = "?"
private const val PARAM_DELIMITER_URL = "&"
private const val PARAM_MOTHER_URL = "hasMother="
private const val PARAM_FATHER_URL = "hasFather="
private const val PARAM_SEVERITY_URL = "firstCrimeSeverity="

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        numberPickerConfig()
        buttonHandler()
        changeResultTv()
    }

    private fun buttonHandler() {
        submitButton.setOnClickListener {
            val request = Volley.newRequestQueue(this)
            val requestValue = API_URL.plus(PARAM_PART_URL)
                .plus(PARAM_MOTHER_URL.plus(hasMotherCheckbox.isChecked)).plus(PARAM_DELIMITER_URL)
                .plus(PARAM_FATHER_URL.plus(hasFatherCheckbox.isChecked)).plus(PARAM_DELIMITER_URL)
                .plus(PARAM_SEVERITY_URL.plus(severityNumberPicker.value))
            val objectRequest = JsonObjectRequest(
                Request.Method.GET,
                requestValue,
                null,
                Response.Listener<JSONObject> { response ->
                    val strResp = response.toString()
                    resultTextView.text = response.getString("result")
                    Log.e("Request", requestValue)
                    Log.e("Response", strResp)
                    Log.e("REST", "OK")
                },
                Response.ErrorListener { Log.e("REST", "ERROR") }
            )
            request.add(objectRequest)
        }
    }

    private fun changeResultTv() {
        severityNumberPicker.setOnScrollListener { _, _ -> clearResult() }
        hasMotherCheckbox.setOnClickListener { clearResult() }
        hasFatherCheckbox.setOnClickListener { clearResult() }
    }

    private fun clearResult() {
        if (resultTextView.text != "") resultTextView.text = ""
    }

    private fun numberPickerConfig(){
        severityNumberPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        severityNumberPicker.minValue = 1
        severityNumberPicker.maxValue = 5
    }
}


