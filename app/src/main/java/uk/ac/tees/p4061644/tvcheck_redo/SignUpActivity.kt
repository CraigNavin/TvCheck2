package uk.ac.tees.p4061644.tvcheck_redo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*
import uk.ac.tees.p4061644.tvcheck_redo.models.ListModel
import uk.ac.tees.p4061644.tvcheck_redo.models.User
import uk.ac.tees.p4061644.tvcheck_redo.utils.DatabaseHandler
import java.util.*

class SignUpActivity : AppCompatActivity(){

	private var mAuth: FirebaseAuth? = null
	private var dbh : DatabaseHandler? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_sign_up)

		mAuth = FirebaseAuth.getInstance()
		dbh = DatabaseHandler(applicationContext)

		findViewById(R.id.Sign_Up_Btn).setOnClickListener{ RegisterUser() }
		findViewById(R.id.Sign_Up_TVLogin).setOnClickListener{ startActivity(Intent(this, LoginActivity::class.java)) }
	}

	/**
	 * Performs validation on the data that is entered. Produces an error if any of the validation fails
	 * If all of the validation passes, then the user is added to the firebase authtication service. In
	 * addition to this, a User object is created and added to the parse database for use within the app
	 */
	private fun RegisterUser() {
		val email = SIgn_Up_Email.text.toString()
		val password = Sign_Up_Pass1.text.toString()
		val confirmPassword = Sign_Up_Pass2.text.toString()


		removeWhiteSpace(email)
		removeWhiteSpace(password)
		removeWhiteSpace(confirmPassword)
		if (email.isNullOrEmpty()) {
			SIgn_Up_Email.error = "Email is required"
			SIgn_Up_Email.requestFocus()
			return
		}

		if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
			SIgn_Up_Email.error = "Please enter a valid email"
			SIgn_Up_Email.requestFocus()
			return
		}

		if (password.isNullOrEmpty()) {
			Sign_Up_Pass1.error = "Password is required"
			Sign_Up_Pass1.requestFocus()
			return
		}

		if (password.length < 6) {
			Sign_Up_Pass1.error = "Minimum length of password should be 6"
			Sign_Up_Pass1.requestFocus()
			return
		}
		if (confirmPassword.isNullOrEmpty()) {
			Sign_Up_Pass2.error = "Please Confirm Password"
			Sign_Up_Pass2.requestFocus()
			return
		}

		if (confirmPassword != password) {
			Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
			Sign_Up_Pass2.setText("")
			Sign_Up_Pass2.requestFocus()
			return
		}

		SignUp_progressbar.visibility = View.VISIBLE

		mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
			SignUp_progressbar.visibility = View.GONE
			if (task.isSuccessful) {
				dbh!!.setup(applicationContext)
				dbh!!.insert(User(mAuth!!.currentUser!!.uid,ArrayList(Arrays.asList(ListModel("Watched"), ListModel("Watching"),ListModel("Going To Watch")))))
				Toast.makeText(applicationContext, "User Registration Successful", Toast.LENGTH_SHORT).show()
				startActivity(Intent(baseContext, LoginActivity::class.java))
			}
			else {
				Toast.makeText(applicationContext, "Some Error Occurred" + task.exception!!.message, Toast.LENGTH_SHORT).show()
			}
		}

	}

	/**
	 * Removes any whitespace from passed string
	 * @param [string] string that will have white space removed
	 * @return updated string with no white space in it
	 */
	fun removeWhiteSpace(string: String): String{
		var result = ""
		var prevchar = ""

		for (char in string){
			if(!(prevchar ==" " && char ==' ')){
				result += char
			}
			prevchar = char.toString()
		}
		return result
	}
}
