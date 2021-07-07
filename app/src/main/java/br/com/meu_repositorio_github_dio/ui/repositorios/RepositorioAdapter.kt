package br.com.meu_repositorio_github_dio.ui.repositorios

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import br.com.meu_repositorio_github_dio.databinding.RepositorioAdapterBinding
import br.com.meu_repositorio_github_dio.network.domain.MeusRepositorios
import br.com.meu_repositorio_github_dio.utils.Cor
import com.bumptech.glide.Glide
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * @author RubioAlves
 * Created 06/07/2021 at 10:18
 */
class RepositorioAdapter(private val lista: MutableList<MeusRepositorios>) :
    RecyclerView.Adapter<RepositorioAdapter.RepositorioAdapterViewHolder>(){

    inner class RepositorioAdapterViewHolder(private val itemBinding: RepositorioAdapterBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(repositorios: MeusRepositorios) {

            itemBinding.run {
                with(repositorios){

                    val dias = "Atualizado ${compararDatas(created_at, updated_at)} dias atrás"

                    titulo.text = name
                    descricao.text = description
                    Glide.with(itemView.context).load(repositorios.owner.avatar_url).into(userImage)
                    linguagem.text = language
                    repositorioAtualizado.apply { text = dias }

                    listarCores().forEach { cores->

                        if (cores.nome == language){
                            cor.apply {
                                background = GradientDrawable().apply {

                                    shape = GradientDrawable.OVAL
                                    setColor(Color.parseColor(cores.cor))

                                }
                            }
                        }
                        if (language == null){
                            cor.visibility = View.INVISIBLE
                        }

                    }

                }
            }

        }
    }

    override fun onCreateViewHolder(
        parent: android.view.ViewGroup,
        viewType: Int
    ): RepositorioAdapterViewHolder {

        val itemBinding =
            RepositorioAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepositorioAdapterViewHolder(itemBinding)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RepositorioAdapterViewHolder, position: Int) {

        holder.bind(lista[position])
    }

    override fun getItemCount(): Int = lista.size

    private fun listarCores(): MutableList<Cor> {

        val cores: MutableList<Cor> = mutableListOf()

        Cor.values().forEach { cor -> cores.add(cor) }

        return cores

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun compararDatas(dataInicial:String, dataFinal:String):Int{

        val dias:Int

        val pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'"

        val dtf = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH)

        val diaCriacao = LocalDate.parse(dataInicial, dtf)
        val ultimoDia = LocalDate.parse(dataFinal, dtf)

        val period = Period.between(diaCriacao, ultimoDia)
        dias = period.days

        return dias


    }



}

