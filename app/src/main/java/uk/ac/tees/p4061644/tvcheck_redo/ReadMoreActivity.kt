package uk.ac.tees.p4061644.tvcheck_redo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_read_more.*


class ReadMoreActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_read_more)
		ReadMore_tv.text = intent.getStringExtra("ReadMore")

		go_back_btn.setOnClickListener { super.onBackPressed() }
	}
}
