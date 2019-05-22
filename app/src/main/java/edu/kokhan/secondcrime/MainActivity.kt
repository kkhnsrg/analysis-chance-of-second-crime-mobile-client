package edu.kokhan.secondcrime

import android.graphics.Color
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

private const val API_URL = "http://crime-analysis.herokuapp.com/result"
private const val PARAM_PART_URL = "?"
private const val PARAM_DELIMITER_URL = "&"
private const val PARAM_MOTHER_URL = "hasMother="
private const val PARAM_FATHER_URL = "hasFather="
private const val PARAM_SEVERITY_URL = "firstCrimeSeverity="

private  const val WARNING_MESSAGE = "It's dangerous criminal!"
private  const val OK_MESSAGE = "Don't worry about him"

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
            val requestValue = "$API_URL$PARAM_PART_URL" +
                    "$PARAM_MOTHER_URL${hasMotherCheckbox.isChecked}$PARAM_DELIMITER_URL" +
                    "$PARAM_FATHER_URL${hasMotherCheckbox.isChecked}$PARAM_DELIMITER_URL" +
                    "$PARAM_SEVERITY_URL${severityNumberPicker.value}"
            val objectRequest = JsonObjectRequest(
                Request.Method.GET,
                requestValue,
                null,
                Response.Listener<JSONObject> { response ->
                    val respValue = response.getString("result")
                    if(respValue.toDouble() > 0.5) {
                        resultTextView.text = WARNING_MESSAGE
                        resultTextView.setTextColor(Color.rgb(220,23,23))
                    } else {
                        resultTextView.text = OK_MESSAGE
                        resultTextView.setTextColor(Color.rgb(52,185,79))
                    }
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


