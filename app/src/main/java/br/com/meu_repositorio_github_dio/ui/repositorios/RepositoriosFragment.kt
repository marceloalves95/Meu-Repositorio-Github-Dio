package br.com.meu_repositorio_github_dio.ui.repositorios

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.meu_repositorio_github_dio.databinding.FragmentRepositoriosBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepositoriosFragment : Fragment() {

    private val repositoriosViewModel: RepositoriosViewModel by viewModel()
    private lateinit var repositorioAdapter: RepositorioAdapter
    private var _binding: FragmentRepositoriosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRepositoriosBinding.inflate(inflater, container, false)
        binding.run {
            with(swipeLayout) {
                setOnRefreshListener {
                    isRefreshing = false
                }
            }
        }
        initViewModel()
        return binding.root
    }

    private fun initViewModel() {

        repositoriosViewModel.getAllRepository()
        lifecycleScope.launchWhenStarted {
            repositoriosViewModel.state.collect { states ->
                with(states) {
                    when (this) {
                        RepositoryState.Empty -> {

                        }
                        is RepositoryState.Error -> {
                            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
                        }
                        is RepositoryState.Loading -> {
                            with(binding) {
                                swipeLayout.apply {
                                    isRefreshing = isLoading
                                }
                            }
                        }
                        is RepositoryState.Sucess -> {

                            response?.let { repositorios->

                                repositorioAdapter = RepositorioAdapter(repositorios)
                                initRecyclerView()

                            }

                        }

                    }
                }
            }

        }
    }

    private fun initRecyclerView() {
        binding.run {
            with(recyclerView){
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = repositorioAdapter
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}