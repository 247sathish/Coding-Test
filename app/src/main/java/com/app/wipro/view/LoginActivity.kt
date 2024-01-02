package com.app.wipro.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.wipro.R
import com.app.wipro.databinding.ActivityLoginBinding
import com.app.wipro.model.LoginRequest
import com.app.wipro.utils.ConnectionLiveData
import com.app.wipro.utils.CustomProgressDialog
import com.app.wipro.utils.Utils
import com.app.wipro.viewmodel.LoginViewModel
import com.google.android.material.internal.ViewUtils.hideKeyboard

class LoginActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var connectionLiveData: ConnectionLiveData
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private var showHideFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Utils().hideKeyboardw(this)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        connectionLiveData = ConnectionLiveData(this)
        connectionLiveData.observe(this) {
            viewModel._isNetworkAvailable.value = it
        }

        viewModel.loginResponseData.observe(this, Observer { results ->
            progressDialog.stop()
            if (results != null) {
                //Utils().showToast(this, results.toString())
                binding.textViewUsername.text = "Username: ${results.username}"
                binding.textViewEmail.text = "Email: ${results.email}"
                binding.textViewFirstName.text = "First Name: ${results.firstName}"
                binding.textViewLastName.text = "Last Name: ${results.lastName}"
                binding.textViewGender.text = "Gender: ${results.gender}"
                binding.lineId.isVisible=true
            }
        })


        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) {
                // Handle the error message, e.g., show a toast or update a TextView
                Utils().showToast(this, errorMessage)
                binding.lineId.isVisible=false
            }
        })

        binding.buttonLogin.setOnClickListener {


            Utils().hideKeyboardw(this)

            if (binding.editTextUsername.text.isNotEmpty() ) {
                if (binding.editTextPassword.text.isNotEmpty()) {
                    if (viewModel.isNetworkAvailable.value == true) {
                        progressDialog.start("Please Wait...")
                        val loginRequest = LoginRequest(
                            binding.editTextUsername.text.toString(),
                            binding.editTextPassword.text.toString()
                        )
                        viewModel.getLoginDetails(loginRequest)
                    } else {
                        Utils().showToast(this,"Please check your Internet Connection!!!")
                    }
                } else {
                    Utils().showToast(this,"Please enter password!")
                }
            } else {
                Utils().showToast(this,"Please enter Username & Password")
            }
        }
    }

}