package com.growd25.changeablerecycler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.growd25.changeablerecycler.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val changeableAdapter: ChangeableAdapter = ChangeableAdapter { changeableViewModel.onItemClicked(it) }
    private lateinit var changeableViewModel: ChangeableViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeableViewModel = ViewModelProvider(this).get(ChangeableViewModel::class.java)
        changeableViewModel.items.observe(this) { items -> changeableAdapter.submitList(items) }

        binding.changeableRecycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.changeableRecycler.adapter = changeableAdapter
    }
}
