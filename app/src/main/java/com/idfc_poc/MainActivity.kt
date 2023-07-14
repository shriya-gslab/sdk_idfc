package com.idfc_poc

import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.montran.cli.NPCICommonLib
import com.montran.cli.data.*
import com.montran.cli.exceptions.Failure
import com.montran.cli.services.UpiServiceCallback
import com.montran.cli.services.UpiServiceStatus
import org.npci.upi.security.services.CLServices
import org.npci.upi.security.services.ServiceConnectionStatusNotifier
import java.util.*

class MainActivity : AppCompatActivity() {
    private var npciCommonlib: NPCICommonLib = NPCICommonLib()
    private var challenge: String? = null
    private var device_id = ""
    private var clServices: CLServices? = null
    var rawtoken = ""
    var token = ""

    companion object {
        const val TAG: String = "MainActivity"
    }

    var buttonGetChallenge: TextView? = null
    var editTextDeviceID: TextView? = null
    var textviewChallenege: TextView? = null
    var buttonSetPin: Button? = null
    var buttonChangepin: Button? = null
    var buttonPay: Button? = null
    var buttonCheckBalance: Button? = null
    var buttonRegister: TextView? = null
    var textviewRegistrationStatus: TextView? = null
    var editTextMobile: EditText? = null
    var editTextToken: EditText? = null
    var editTextAppId: EditText? = null
    var editTextlistKey: EditText? = null
    var editTextOTPLength: EditText? = null
    var editTextMPinLength: EditText? = null
    var editTextATMPinLength: EditText? = null

    var textviewSetPin: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeIDFC()

        Log.e("token--- ", token.toString())
        textviewSetPin = findViewById<TextView>(R.id.textviewSetPin)

        buttonGetChallenge = findViewById<Button>(R.id.buttonGetChallenge)
        editTextDeviceID = findViewById<EditText>(R.id.editTextDeviceID)
        textviewChallenege = findViewById<TextView>(R.id.textviewChallenege)
        buttonSetPin = findViewById<Button>(R.id.buttonSetPin)
        buttonChangepin = findViewById<Button>(R.id.buttonChangepin)
        buttonPay = findViewById<Button>(R.id.buttonPay)
        buttonCheckBalance = findViewById<Button>(R.id.buttonCheckBalance)
        buttonRegister = findViewById<Button>(R.id.buttonRegister)
        textviewRegistrationStatus = findViewById<TextView>(R.id.textviewRegistrationStatus)
        editTextMobile = findViewById<EditText>(R.id.editTextMobile)
        editTextToken = findViewById<EditText>(R.id.editTextToken)
        editTextAppId = findViewById<EditText>(R.id.editTextAppId)
        editTextlistKey = findViewById<EditText>(R.id.editTextlistKey)
        editTextOTPLength = findViewById<EditText>(R.id.editTextOTPLength)
        editTextMPinLength = findViewById<EditText>(R.id.editTextMPinLength)
        editTextATMPinLength = findViewById<EditText>(R.id.editTextATMPinLength)
        rawtoken = editTextToken!!.text.toString()
        token = getConvertedHexToken()!!
        buttonRegister!!.setOnClickListener {
            var eToken = editTextToken!!.text.toString()
            var eMobile = editTextMobile!!.text.toString()
            var eAppID = editTextAppId!!.text.toString()
            var eDeviceID = editTextDeviceID!!.text.toString()
            if (eToken.isEmpty()) {
                Toast.makeText(this@MainActivity, "Enter Token", Toast.LENGTH_SHORT).show()
            } else if (eMobile.isEmpty()) {
                Toast.makeText(this@MainActivity, "Enter Mobile Number", Toast.LENGTH_SHORT).show()
            } else if (eAppID.isEmpty()) {
                Toast.makeText(this@MainActivity, "Enter Ap ID", Toast.LENGTH_SHORT).show()
            } else {
                rawtoken = editTextToken!!.text.toString()
                token = getConvertedHexToken()!!
                if (token.isNotEmpty()) {
                    Log.e(TAG, "registerIDFC-token-- " + token)
                    try {
                        npciCommonlib.register(eAppID,
                            eMobile,
                            token,
                            eDeviceID,
                            object : UpiServiceCallback<Boolean>(Boolean::class.java) {
                                override fun onSuccess(result: Boolean) {
                                    Log.e(TAG, "registerIDFC: result: $result")

                                    textviewRegistrationStatus!!.text = ""
                                    textviewRegistrationStatus!!.text = "Regitration response success: $result"
                                }
                                override fun onError(status: UpiServiceStatus, errors: Failure) {
                                    Log.e(TAG, "registerIDFC: status name:-- " + status.name)
                                    Log.e(TAG, "registerIDFC: status: original-- " + status.ordinal)
                                    Log.e(TAG, "registerIDFC: errors: errorMessage-- " + errors.errorMessage())
                                    Log.e(TAG, "registerIDFC: errors: errorKey-- " + errors.errorKey)
                                    textviewRegistrationStatus!!.text = ""
                                    textviewRegistrationStatus!!.text = "Regitration response failed: " + errors.errorMessage()
                                }
                            })
                    } catch (e: Exception) {
                        println("This is not a number");
                        println(e);
                    }
                } else {
                    textviewRegistrationStatus!!.text = ""
                    textviewRegistrationStatus!!.text = "Invalid token"
                }
            }
        }


        buttonSetPin!!.setOnClickListener {
            getCredentails("setpin")
        }

        buttonChangepin!!.setOnClickListener {
            getCredentails("changepin")
        }

        buttonPay!!.setOnClickListener {
            getCredentails("pay")
        }

        buttonCheckBalance!!.setOnClickListener {
            getCredentails("checkbalance")
        }


    }

    fun getCredentails(credType :String){

        var eMobile = editTextMobile!!.text.toString()
        var eAppID = editTextAppId!!.text.toString()
        var eDeviceId = editTextDeviceID!!.text.toString()
        var respListKeys = editTextlistKey!!.text.toString()
        var mpinlength = editTextMPinLength!!.text.toString()
        var otplength = editTextOTPLength!!.text.toString()
        var atmpinlength = editTextATMPinLength!!.text.toString()
        rawtoken = editTextToken!!.text.toString()
        token = getConvertedHexToken()!!

        if (token.isEmpty()) {
            Toast.makeText(this@MainActivity, "Enter Token", Toast.LENGTH_SHORT).show()
        } else if (eMobile.isEmpty()) {
            Toast.makeText(this@MainActivity, "Enter Mobile Number", Toast.LENGTH_SHORT).show()
        } else if (eAppID.isEmpty()) {
            Toast.makeText(this@MainActivity, "Enter App ID", Toast.LENGTH_SHORT).show()
        } else if (eDeviceId.isEmpty()) {
            Toast.makeText(this@MainActivity, "Enter deviceid", Toast.LENGTH_SHORT).show()
        } else if (respListKeys.isEmpty()) {
            Toast.makeText(this@MainActivity, "Enter listkey", Toast.LENGTH_SHORT).show()
        } else if (mpinlength.isEmpty()) {
            Toast.makeText(this@MainActivity, "Enter MPIN length", Toast.LENGTH_SHORT).show()
        } else if (otplength.isEmpty()) {
            Toast.makeText(this@MainActivity, "Enter OTP length", Toast.LENGTH_SHORT).show()
        } else if (atmpinlength.isEmpty()) {
            Toast.makeText(this@MainActivity, "Enter ATM pin length", Toast.LENGTH_SHORT).show()
        } else {
            val udir = UUID.randomUUID().toString()
            val completeString = udir.replace("-", "")

            var _credtype = ""
            var _flowType: FlowType? = null

            when (credType) {
                "setpin" -> {
                    _credtype = CredType.SET_PIN
                    _flowType = FlowType.SET_PIN
                }
                "changepin" -> {
                    _credtype = CredType.CHANGE_PIN
                    _flowType = FlowType.CHANGE_PIN
                }
                "pay" -> {
                    _credtype = CredType.PAY
                    _flowType = FlowType.PAY
                }
                "checkbalance" -> {
                    _credtype = CredType.REQ_BAL_CHK
                    _flowType = FlowType.REQ_BAL_CHK
                }
            }
            Log.e("_credtype- ",_credtype.toString())
            Log.e("_flowType- ",_flowType.toString())
            npciCommonlib.getCredential(
                CredKeyCode.NPCI,
                CLRequestParams(
                    credType = arrayListOf(_credtype),
                    flowType = _flowType!!,
                    mPinLength = mpinlength,
                    listKeysXmlPayload = respListKeys,
                    txnID = listOf("IDFAB"+completeString.dropLast(2).uppercase()),
                    deviceId = eDeviceId,
                    appId = eAppID,
                    mobileNo = eMobile,
                    token = token,
                    payerBankName = "Idfc",
                    atmPinLength = atmpinlength,
                    otpLength = otplength,
//                        resendIssuerOTPFeature = "true",
//                        aadhaarResendOTPLimit = 2,
                ),
                callbackUPIService
            )
        }
    }

    private val callbackUPIService =
        object : UpiServiceCallback<CLResponseParams>(CLResponseParams::class.java) {

            override fun onSuccess(result: CLResponseParams) {
                Log.e(TAG, "UpiServiceCallback: result: $result")
                textviewSetPin!!.text = ""
                textviewSetPin!!.text =
                    "deviceDetail: " + result.deviceDetail.toString() + "\n" +
                                   "encryptedAadharData: " + result.encryptedAadharData.toString() + "\n\n" +
                                   "encryptedATMData: " + result.encryptedATMData.toString() + "\n\n" +
                                   "encryptedCardDetailData: " + result.encryptedCardDetailData.toString() + "\n\n" +
                                   "encryptedIdentity: " + result.encryptedIdentity.toString() + "\n\n" +
                                   "encryptedOTPData: " + result.encryptedOTPData.toString() + "\n\n" +
                                   "Encripted pin: " + result.encryptedPINData.toString() + "\n\n" +
                                   "encryptedPINData: " + result.encryptedPINData.toString() + "\n\n" +
                                   "encryptedSignature: " + result.encryptedSignature
            }

            override fun onError(status: UpiServiceStatus, errors: Failure) {
                Log.e(TAG, "UpiServiceCallback: error: $errors")

                textviewSetPin!!.text = ""
                textviewSetPin!!.text = "Setpin Callback error: " + errors.errorKey
            }
        }

    private fun getConvertedHexToken(): String? {
        try {
            val decodeByteArray =
                Base64.decode(rawtoken, Base64.NO_WRAP)
            return byteArrayToHex(decodeByteArray)
        } catch (e: Exception) {
            return ""
        }
    }

    private fun byteArrayToHex(a: ByteArray): String? {
        val sb = StringBuilder(a.size * 2)
        for (b in a) {
            sb.append(String.format("%02x", *arrayOf<Any>(Integer.valueOf(b.toInt() and 0xFF))))
        }
        Log.e("TAG", "sb.toString()-- " + sb.toString())
        return sb.toString()
    }

    private fun initializeIDFC() {
        npciCommonlib.initializeCL(this@MainActivity, object : UpiServiceCallback<Boolean>(Boolean::class.java) {
            override fun onError(status: UpiServiceStatus, errors: Failure) {
                Log.e(TAG, "initializeCL: status: " + status.name)
                Log.e(TAG, "initializeCL: status name: " + status.ordinal)
                Log.e(TAG, "initializeCL: error: $errors")
            }

            override fun onSuccess(result: Boolean) {
                Log.e(TAG, "initializeCL: result: $result")
                buttonGetChallenge!!.setOnClickListener {
                    textviewSetPin!!.text = ""
                    textviewRegistrationStatus!!.text = ""
                    textviewChallenege!!.text = "----------------------"
//                    editTextToken!!.text = ""
//                    editTextMobile!!.text = ""
                    if (editTextDeviceID!!.text.isEmpty()) {
                        Toast.makeText(this@MainActivity, "Enter device ID", Toast.LENGTH_SHORT).show()
                    } else {

                        device_id = editTextDeviceID!!.text.toString()

                        try {
                            print("device_id-- $device_id")
                            var request_challenge = npciCommonlib.getChallenge("initial", device_id)
                            textviewChallenege!!.text = ""
                            textviewChallenege!!.text = request_challenge
                            Log.e("request_challenge-- ", request_challenge.toString())
                        } catch (e: Exception) {
                            Log.e("CL Service exception", e.printStackTrace().toString())
                        }
                    }
                }
            }
        })
    }

}