package com.example.storyapp.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.storyapp.databinding.ActivitySignupBinding
import com.example.storyapp.view.ViewModelFactory

class SignupActivity : AppCompatActivity() {
    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBar.visibility = View.INVISIBLE

        setupView()
        setupAction()
        playAnimation()
        setSignUpButtonEnable()

        binding.emailEditTextLayout.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setSignUpButtonEnable()
            }

            override fun afterTextChanged(p0: Editable?) {
                setSignUpButtonEnable()
            }

        })

        binding.passwordEditTextLayout.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setSignUpButtonEnable()
            }

            override fun afterTextChanged(p0: Editable?) {
                setSignUpButtonEnable()
            }

        })
    }

    private fun setSignUpButtonEnable() {
        val result =
            binding.nameEditTextLayout.text.isNullOrEmpty() || binding.emailEditTextLayout.text.isNullOrEmpty() || binding.passwordEditTextLayout.text.isNullOrEmpty() || binding.passwordEditTextLayout.error != null || binding.emailEditTextLayout.error != null
        binding.signupButton.isEnabled = !result
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val name = binding.nameEditTextLayout.text.toString().trim()
            val email = binding.emailEditTextLayout.text.toString()
            val password = binding.passwordEditTextLayout.text.toString()
            viewModel.signup(name, email, password)
            viewModel.errorResult.observe(this@SignupActivity) { result ->
                if (result!=null && !result.error ) {
                    Log.d("if", "error false")
                    val successBuilder = AlertDialog.Builder(this)
                    binding.progressBar.visibility = View.INVISIBLE
                    with(successBuilder){
                        setTitle("Akun telah didaftarkan")
                        setMessage("Akun dengan email $email sudah terdaftar. \nSilahkan login untuk melihat dan memposting story kamu bersama Dicoding!")
                        setPositiveButton("Oke"){_,_->
                            finish()
                        }
                        setOnDismissListener {
                            finish()
                        }
                        create()
                        show()
                    }
                } else {
                    Log.d("else", "error")
                    val errorBuilder = AlertDialog.Builder(this)
                    binding.progressBar.visibility = View.INVISIBLE
                    with(errorBuilder){
                        setTitle("Error")
                        setMessage(result.message)
                        setPositiveButton("Okay") { dialog, _ ->
                            Log.d("alert", "error dismissed")
                            dialog.dismiss()
                        }
                        create()
                        show()
                    }
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 100
        }.start()
    }
}