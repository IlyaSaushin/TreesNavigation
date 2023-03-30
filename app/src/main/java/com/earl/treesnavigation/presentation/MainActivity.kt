package com.earl.treesnavigation.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.earl.treesnavigation.R
import com.earl.treesnavigation.databinding.ActivityMainBinding
import com.earl.treesnavigation.presentation.utils.Nodes

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        showRootFragment()
    }

    private fun showRootFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.root_container, RootNodeFragment.newInstance(Nodes.root), Nodes.root)
            .addToBackStack(Nodes.root)
            .commit()
    }
}