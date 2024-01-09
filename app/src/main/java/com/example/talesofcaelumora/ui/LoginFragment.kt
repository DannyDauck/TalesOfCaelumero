package com.example.talesofcaelumora.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.talesofcaelumora.R
import com.example.talesofcaelumora.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

val TAG = "LogIn"

class LoginFragment : Fragment() {

    private lateinit var bnd: FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle(R.string.close_app)
                builder.setMessage(R.string.sure_to_leave)
                builder.setPositiveButton(R.string.yes) { _, which ->
                    requireActivity().finish()
                }
                builder.setNegativeButton(R.string.no) { dialog, which ->
                    dialog.dismiss()
                }
                builder.create().show()

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bnd = FragmentLoginBinding.inflate(inflater, container, false)
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        googleSignInLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                onGoogleSignInResult(result.resultCode, result.data)
            }

        if (firebaseAuth.currentUser != null) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSplashScreenFragment())
        }

        bnd.btnSignInWithGoogle.setOnClickListener {
            Log.d(TAG, "Startet google sign in")
            startGoogleSigIn()
        }

        bnd.btnSignIn.setOnClickListener {
            signIn(bnd.itEmail.text.toString(), bnd.itPassword.text.toString())
        }

        bnd.btnSignUp.setOnClickListener {
            if (Patterns.EMAIL_ADDRESS.matcher(bnd.itEmail.text.toString()).matches()) {
                if (bnd.llPasswordBoxTwo.isVisible && bnd.itPasswordTwo.text.toString() == bnd.itPassword.text.toString() && checkPassword(
                        bnd.itPassword.text.toString()
                    )
                ) {
                    Log.d(TAG, "SignUp aufrufen")
                    signUp()
                } else if (!checkPassword(bnd.itPassword.text.toString())) {
                    Log.d(TAG, "Passwort überprüfen aufrufen")
                    Toast.makeText(
                        requireContext(),
                        R.string.password_requirements,
                        Toast.LENGTH_LONG
                    ).show()
                } else if (bnd.llPasswordBoxTwo.isVisible && bnd.itPasswordTwo.text.toString() != bnd.itPassword.text.toString()) {
                    Toast.makeText(
                        requireContext(),
                        R.string.passwords_do_not_match,
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (bnd.llPasswordBoxTwo.isVisible == false) {
                    bnd.llPasswordBoxTwo.isVisible = true
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    R.string.invalid_email,
                    Toast.LENGTH_LONG
                ).show()
                bnd.invalidMail.isVisible = true
                bnd.itEmail.addTextChangedListener {
                    if (Patterns.EMAIL_ADDRESS.matcher(bnd.itEmail.text.toString())
                            .matches()
                    ) bnd.invalidMail.isVisible = false
                }
            }
        }
    }

    private fun startGoogleSigIn() {
        if (::googleSignInClient.isInitialized) {
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        } else {
            Log.e(TAG, "googleSignInClient is not initialized")
            Toast.makeText(
                requireContext(),
                R.string.google_signin_client_not_initialized,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToSplashScreenFragment()
                    )
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        requireContext(),
                        R.string.signin_failed,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun signUp() {
        firebaseAuth.createUserWithEmailAndPassword(
            bnd.itEmail.text.toString(),
            bnd.itPassword.text.toString()
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToSplashScreenFragment()
                )
            } else if (!task.isSuccessful) {
                firebaseAuth.fetchSignInMethodsForEmail(bnd.itEmail.text.toString())
                    .addOnCompleteListener { sectask ->
                        if (sectask.isSuccessful) {
                            if (sectask.result != null) {
                                Toast.makeText(
                                    requireContext(),
                                    R.string.existing_user_account,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                requireContext(),
                                R.string.sign_up_failed,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }
    }

    private fun checkPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[A-Z])(?=.*[0-9@#\$%^&+=!]).{8,}$".toRegex()
        return passwordPattern.matches(password)
    }

    private fun onGoogleSignInResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    Toast.makeText(
                        requireContext(),
                        R.string.signin_with_google_account,
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToSplashScreenFragment()
                    )
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        requireContext(),
                        R.string.signin_with_google_account_failed,
                        Toast.LENGTH_SHORT
                    ).show()
                    Toast.makeText(
                        requireContext(),
                        R.string.try_again_later,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}

