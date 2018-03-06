package uk.ac.tees.p4061644.tvcheck_redo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import uk.ac.tees.p4061644.tvcheck_redo.models.ListModel
import uk.ac.tees.p4061644.tvcheck_redo.models.User
import uk.ac.tees.p4061644.tvcheck_redo.utils.DatabaseHandler


class LoginActivity : AppCompatActivity(), View.OnClickListener {

	var editTextEmail: EditText? = null
	var editTextPassword: EditText? = null
	var progressBar: ProgressBar? = null
	private var mAuth: FirebaseAuth? = null
	private var dbh: DatabaseHandler? = null



	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)

		dbh = DatabaseHandler(applicationContext)

		editTextEmail = findViewById(R.id.Login_email) as EditText
		editTextPassword= findViewById(R.id.Login_Pass) as EditText
		progressBar =findViewById(R.id.Login_progressbar) as ProgressBar
		FirebaseApp.initializeApp(applicationContext)
		mAuth = FirebaseAuth.getInstance()

		editTextEmail!!.requestFocus()
		findViewById(R.id.Login_TVsignUp).setOnClickListener(this)
		findViewById(R.id.Login_Btn).setOnClickListener(this)
	}

	private fun login() {
		val email = editTextEmail!!.text.toString()
		val password = editTextPassword!!.text.toString()

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

		progressBar!!.visibility = View.VISIBLE

		mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->

			if (task.isSuccessful) {
				Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_SHORT).show()
				val user = dbh!!.retrievefirst(mAuth!!.currentUser!!.uid)
				val i = Intent(baseContext, HomeActivity::class.java)
				val gson = Gson()
				i.putExtra("User", gson.toJson(user))
				startActivity(i)
				progressBar!!.visibility = View.GONE
			}
			else {
				progressBar!!.visibility = View.GONE
				Toast.makeText(applicationContext, "Authentication Failed", Toast.LENGTH_SHORT).show()
			}
		}
	}

	override fun onClick(view: View) {
		when (view.id) {
			R.id.Login_TVsignUp -> applicationContext.startActivity(Intent(this, SignUpActivity::class.java))
			R.id.Login_Btn -> login()
		}
	}
}
