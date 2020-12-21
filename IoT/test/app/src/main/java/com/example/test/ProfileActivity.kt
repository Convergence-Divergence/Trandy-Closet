package com.example.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    companion object{ // 어떤 호출인지 상수로 정의
        val REQUEST = 0
        val NAME = ""
        val SEX = ""
        val RESULT = "RESULT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        bt_mom.setOnClickListener {
            val i = Intent(this@ProfileActivity, HomeActivity::class.java)
            i.putExtra(NAME, "MOM")
            startActivityForResult(i, REQUEST)
        }

        bt_dad.setOnClickListener {
            val i = Intent(this@ProfileActivity, HomeActivity::class.java)
            i.putExtra(NAME, "DAD")
            startActivityForResult(i, REQUEST)
        }

        bt_son.setOnClickListener {
            val i = Intent(this@ProfileActivity, HomeActivity::class.java)
            i.putExtra(NAME, "SON")
            startActivityForResult(i, REQUEST)
        }

        bt_daughter.setOnClickListener {
            val i = Intent(this@ProfileActivity, HomeActivity::class.java)
            i.putExtra(NAME, "DAU")
            startActivityForResult(i, REQUEST)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        if(requestCode != REQUEST) return
        data?.getStringExtra(RESULT).let{

        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}