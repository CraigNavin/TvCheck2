package uk.ac.tees.p4061644.tvcheck_redo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import uk.ac.tees.p4061644.tvcheck_redo.utils.DatabaseHandler


class LoginActivity : AppCompatActivity(), View.OnClickListener {

	private var mAuth: FirebaseAuth? = null
	private var dbh: DatabaseHandler? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)

		dbh = DatabaseHandler(applicationContext)
		FirebaseApp.initializeApp(applicationContext)
		mAuth = FirebaseAuth.getInstance()
		Login_email.requestFocus()

		findViewById(R.id.Login_TVsignUp).setOnClickListener(this)
		findViewById(R.id.Login_Btn).setOnClickListener(this)
	}

	/**
	 * Perfoms validation on all data entered and attempts to authenticate the user in if information is correct.
	 * If authentication is passed then the user is retrieved from the database and a User object is created using this data.
	 * The home activity is then started and displayed
	 */
	private fun login() {
		val email = Login_email.text.toString()
		val password = Login_Pass.text.toString()

		if (email.isNullOrEmpty()) {
			Login_email.error = "Email is required"
			Login_email.requestFocus()
			return
		}

		if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
			Login_email.error = "Please enter a valid email"
			Login_email.requestFocus()
			return
		}

		if (password.isNullOrEmpty()) {
			Login_Pass.error = "Password is required"
			Login_Pass.requestFocus()
			return
		}

		Login_progressbar!!.visibility = View.VISIBLE
		try{
			mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->

				if (task.isSuccessful) {
					Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_LONG).show()
					val user = dbh!!.retrievefirst(mAuth!!.currentUser!!.uid)
					val i = Intent(baseContext, HomeActivity::class.java)
					i.putExtra("User", Gson().toJson(user))
					startActivity(i)
					Login_progressbar.visibility = View.GONE
				}
				else {
					Login_progressbar.visibility = View.GONE
					Toast.makeText(applicationContext, "Login Unsuccessful", Toast.LENGTH_SHORT).show()
				}
			}
		}catch(e: Exception){
			Toast.makeText(applicationContext,"An Error Occured", Toast.LENGTH_SHORT).show()
		}

	}

	/**
	 * Overridden onClick method that controls what happens when a click happens
	 */
	override fun onClick(view: View) {
		when (view.id) {
			R.id.Login_TVsignUp -> applicationContext.startActivity(Intent(this, SignUpActivity::class.java))
			R.id.Login_Btn -> login()
		}
	}
}
