package br.com.meu_repositorio_github_dio.ui.main

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import br.com.meu_repositorio_github_dio.R
import br.com.meu_repositorio_github_dio.databinding.ActivityMainBinding
import br.com.meu_repositorio_github_dio.network.domain.User
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val viewModel:UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()

    }

    private fun initViewModel(){
        viewModel.getUser()
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { states->
                with(states){
                    when(this){
                        UIState.Empty -> {

                        }
                        is UIState.Error -> {

                        }
                        is UIState.Loading -> {

                        }
                        is UIState.Sucess -> {
                            binding.run {
                                response?.let { user->
                                    getUser(user)
                                }
                            }
                        }
                    }
                }

            }
        }
    }
    private fun getUser(user:User){

        val header = binding.navView.getHeaderView(0)
        val imageUser:ShapeableImageView = header.findViewById(R.id.imageViewUser)
        val name:TextView = header.findViewById(R.id.user)
        val login:TextView = header.findViewById(R.id.login)
        val bio:TextView = header.findViewById(R.id.bio)
        val seguidores:TextView = header.findViewById(R.id.seguidores)
        val localizacao:TextView = header.findViewById(R.id.localizacao)

        val follow = "${user.followers} followers · ${user.following} following"

        binding.apply {
            name.text = user.name
            login.text = user.login
            bio.text = user.bio
            seguidores.text = follow
            localizacao.text = user.location

            Glide.with(header).load(user.avatar_url).into(imageUser)

        }
    }

    private fun initView(){

        binding.run {
            setSupportActionBar(appBarMain.toolbar)

            val drawerLayout: DrawerLayout = drawerLayout
            val navView: NavigationView = navView
            val navController = findNavController(R.id.nav_host_fragment_content_main)

            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_repositorio), drawerLayout
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}