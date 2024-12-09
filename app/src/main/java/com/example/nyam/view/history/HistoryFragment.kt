package com.example.nyam.view.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nyam.adapter.HistoryAdapter
import com.example.nyam.data.ResultState
import com.example.nyam.databinding.FragmentHistoryBinding
import com.example.nyam.helper.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private val viewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(
            requireActivity()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        val firebaseUser = auth.currentUser


        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getHistory(firebaseUser!!.uid).observe(viewLifecycleOwner){ history ->
            when(history){
                is ResultState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE

                    val adapter = HistoryAdapter()
                    adapter.submitList(history.data)
                    binding.rvHistory.adapter = adapter
                }
                is ResultState.Error -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.e("ResultActivity", "Error: $history")
                    Toast.makeText(
                        requireContext(), "Load Articles Error: $history", Toast.LENGTH_LONG
                    ).show()
                }

            }
        }

    }
}