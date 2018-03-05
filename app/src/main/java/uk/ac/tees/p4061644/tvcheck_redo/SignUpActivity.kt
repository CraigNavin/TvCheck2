package uk.ac.tees.p4061644.tvcheck_redo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import uk.ac.tees.p4061644.tvcheck_redo.models.User
import uk.ac.tees.p4061644.tvcheck_redo.utils.DatabaseHandler

class SignUpActivity : AppCompatActivity(), View.OnClickListener {

	internal var editTextEmail: EditText? = null
	internal var editTextPassword: EditText? = null
	internal var editTextPasswordConfirm: EditText? = null
	internal var progressBar: ProgressBar? = null
	private var mAuth: FirebaseAuth? = null
	private var dbh : DatabaseHandler? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_sign_up)

		editTextEmail = findViewById(R.id.SIgn_Up_Email) as EditText
		editTextPassword = findViewById(R.id.Sign_Up_Pass1) as EditText
		editTextPasswordConfirm = findViewById(R.id.Sign_Up_Pass2) as EditText
		progressBar = findViewById(R.id.SignUp_progressbar) as ProgressBar

		mAuth = FirebaseAuth.getInstance()
		dbh = DatabaseHandler(applicationContext)

		findViewById(R.id.Sign_Up_Btn).setOnClickListener(this)
		findViewById(R.id.Sign_Up_TVLogin).setOnClickListener(this)
	}

	private fun RegisterUser() {
		val email = editTextEmail!!.text.toString()
		val password = editTextPassword!!.text.toString()
		val confirmPassword = editTextPasswordConfirm!!.text.toString()


		removeWhiteSpace(email)
		removeWhiteSpace(password)
		removeWhiteSpace(confirmPassword)
		if (email.isNullOrEmpty()) {
			editTextEmail!!.error = "Email is required"
			editTextEmail!!.requestFocus()
			return
		}

		if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
			editTextEmail!!.error = "Please enter a valid email"
			editTextEmail!!.requestFocus()
			return
		}

		if (password.isNullOrEmpty()) {
			editTextPassword!!.error = "Password is required"
			editTextPassword!!.requestFocus()
			return
		}

		if (password.length < 6) {
			editTextPassword!!.error = "Minimum length of password should be 6"
			editTextPassword!!.requestFocus()
			return
		}
		if (confirmPassword.isNullOrEmpty()) {
			editTextPasswordConfirm!!.error = "Please Confirm Password"
			editTextPasswordConfirm!!.requestFocus()
			return
		}

		if (confirmPassword != password) {
			Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
			editTextPasswordConfirm!!.setText("")
			editTextPasswordConfirm!!.requestFocus()
			return
		}

		progressBar!!.visibility = View.VISIBLE

		mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
			progressBar!!.visibility = View.GONE
			if (task.isSuccessful) {
				dbh!!.setup(applicationContext)
				dbh!!.insert(User(mAuth!!.currentUser!!.uid,ArrayList()))
				Toast.makeText(applicationContext, "User Registration Successful", Toast.LENGTH_SHORT).show()
				startActivity(Intent(baseContext, LoginActivity::class.java))
			}
			else {
				Toast.makeText(applicationContext, "Some Error Occurred" + task.exception!!.message, Toast.LENGTH_SHORT).show()
			}
		}

	}

	fun removeWhiteSpace(value : String): String{
		var result = ""
		var prevchar = ""

		for (char in value){
			if(!(prevchar ==" " && char ==' ')){
				result += char
			}
			prevchar = char.toString()
		}
		return result
	}

	override fun onClick(view: View) {
		when (view.id) {
			R.id.Sign_Up_Btn -> RegisterUser()
			R.id.Sign_Up_TVLogin -> applicationContext.startActivity(Intent(this, LoginActivity::class.java))
		}
	}
}
