package uk.ac.tees.p4061644.tvcheck_redo

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.omertron.themoviedbapi.model.tv.*
import com.omertron.themoviedbapi.results.ResultList
import uk.ac.tees.p4061644.tvcheck_redo.models.Episode
import uk.ac.tees.p4061644.tvcheck_redo.models.Season
import uk.ac.tees.p4061644.tvcheck_redo.models.Show
import uk.ac.tees.p4061644.tvcheck_redo.utils.AsyncTasker
import uk.ac.tees.p4061644.tvcheck_redo.utils.Converter


class LoginActivity : AppCompatActivity(), View.OnClickListener {

	var editTextEmail: EditText? = null
	var editTextPassword: EditText? = null
	var progressBar: ProgressBar? = null
	private var mAuth: FirebaseAuth? = null
	private val Async : AsyncTasker = AsyncTasker()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)

		editTextEmail = findViewById(R.id.Login_email) as EditText
		editTextPassword= findViewById(R.id.Login_Pass) as EditText
		progressBar =findViewById(R.id.Login_progressbar) as ProgressBar
		FirebaseApp.initializeApp(applicationContext)
		mAuth = FirebaseAuth.getInstance()

		editTextEmail!!.requestFocus()
		findViewById(R.id.Login_TVsignUp).setOnClickListener(this)
		findViewById(R.id.Login_Btn).setOnClickListener(this)

		var rl: List<TVBasic>? = Async.searchShows("marvel")
		var con : Converter = Converter()
		var show1: TVInfo? = Async.getShow(rl!![3].id)
		var season: TVSeasonInfo? = Async.getSeason(show1!!.seasons[0].seasonNumber)
		var episode: TVEpisodeInfo? = Async.getEpisode(3)

		var conShow: Show = con.convert(show1)
		var conSeason: Season = con.convert(season!!)
		var conEpisode: Episode = con.convert(episode!!)

		Log.d("CONVERSION",conShow.toString())
		Log.d("CONVERSION",conSeason.toString())
        Log.d("CONVERSION",conEpisode.toString())
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
			progressBar!!.visibility = View.GONE
			if (task.isSuccessful) {
				Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_SHORT).show()
				val user = mAuth!!.currentUser
				val UID = user!!.uid
				val i = Intent(baseContext, HomeActivity::class.java)
				i.putExtra("UID", UID)
				startActivity(i)
			}
			else {
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
