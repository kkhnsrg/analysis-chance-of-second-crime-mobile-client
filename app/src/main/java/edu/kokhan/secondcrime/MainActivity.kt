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

class MainActivity : AppCompatActivity() {

    val API_URL = "http://localhost:8080/result"
    val PARAM_PART_URL = "?"
    val PARAM_DELIMETER_URL = "&"
    val PARAM_MOTHER_URL = "hasMother="
    val PARAM_FATHER_URL = "hasFather="
    val PARAM_SEVERITY_URL = "firstCrimeSeverity="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        severityNumberPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        severityNumberPicker.minValue = 1
        severityNumberPicker.maxValue = 5

        buttonHandler()
        changeResultTv()
    }

    private fun buttonHandler() {
        val button = submitButton.setOnClickListener {
            val request = Volley.newRequestQueue(this)
            val requestValue = API_URL.plus(PARAM_PART_URL)
                .plus(PARAM_MOTHER_URL.plus(hasMotherCheckbox.isChecked)).plus(PARAM_DELIMETER_URL)
                .plus(PARAM_FATHER_URL.plus(hasFatherCheckbox.isChecked)).plus(PARAM_DELIMETER_URL)
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
        severityNumberPicker.setOnScrollListener { view, scrollState -> clearResult() }
        hasMotherCheckbox.setOnClickListener { clearResult() }
        hasFatherCheckbox.setOnClickListener { clearResult() }
    }

    private fun clearResult() {
        if (resultTextView.text != "") resultTextView.text = ""
    }

}


