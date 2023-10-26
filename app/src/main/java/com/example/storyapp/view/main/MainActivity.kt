package com.example.storyapp.view.main

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.adapter.StoryListAdapter
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.di.Injection
import com.example.storyapp.view.ViewModelFactory
import com.example.storyapp.view.detail.DetailActivity
import com.example.storyapp.view.maps.MapsActivity
import com.example.storyapp.view.signup.SignupActivity
import com.example.storyapp.view.upload.UploadActivity
import com.example.storyapp.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var _mainActivityBinding: ActivityMainBinding? = null
    private val binding get() = _mainActivityBinding
    private lateinit var detailAdapter: StoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                setupView()
            }
        }
        showRecycleView()
    }

    private fun setupView() {
        binding?.floatingButton?.setOnClickListener {
            startActivity(Intent(this@MainActivity, UploadActivity::class.java))
        }
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
//        viewModel.getStories()
//        viewModel.isError.observe(this) { isError ->
//            if (isError) {
//                binding?.tvError?.text = getString(R.string.error_message, viewModel.errorMessage)
//                binding?.tvError?.visibility = View.VISIBLE
//                binding?.errorImage?.visibility = View.VISIBLE
//                binding?.floatingButton?.visibility = View.GONE
//            } else {
//                binding?.errorImage?.visibility = View.GONE
//                binding?.tvError?.visibility = View.GONE
//                viewModel.listUsers.observe(this) { consumerUsers ->
//                    setStoriesList(consumerUsers)
//                }
//                Log.d("mainactivity", "stories consumed")
//            }
//        }
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                val activity = this@MainActivity
                val alertDialog: AlertDialog = activity.let {
                    val builder = AlertDialog.Builder(it)
                    builder.apply {
                        setTitle(getString(R.string.logout_title))
                        setMessage(getString(R.string.logout_msg))
                        setPositiveButton(
                            R.string.ok
                        ) { dialog, _ ->
                            viewModel.logout()
                            dialog.dismiss()
                            finish()
                        }
                        setNegativeButton(
                            R.string.cancel
                        ) { dialog, _ ->
                            dialog.dismiss()
                        }
                    }
                    builder.create()
                }
                alertDialog.show()
                return true
            }

            R.id.language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                return true
            }

            R.id.maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showRecycleView() {
        val layoutManager = LinearLayoutManager(this@MainActivity)
        binding?.rvStory?.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding?.rvStory?.addItemDecoration(itemDecoration)
        detailAdapter = StoryListAdapter()
        binding?.rvStory?.adapter = detailAdapter
        detailAdapter.setOnItemClickCallback(object : StoryListAdapter.OnItemClickCallBack {
            override fun onItemClicked(profile: ListStoryItem) {
                val moveStoryDetail = Intent(this@MainActivity, DetailActivity::class.java)
                moveStoryDetail.putExtra("Story", profile)
                startActivity(moveStoryDetail)
            }
        }
        )
    }

//    private fun setStoriesList(consumerStories: List<ListStoryItem>) {
//        detailAdapter.submitList(consumerStories)
//    }

    private fun getPagerData() {
        val adapter = StoryListAdapter()
        binding?.rvStory?.adapter = adapter
        viewModel.storyPager.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _mainActivityBinding = null
    }

}