package fr.mezza.bookcollection.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.mezza.bookcollection.*

class BookAdapter(
    val context: MainActivity,
    private val bookList: List<BookModel>,
    private val layoutId: Int
) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    //boîte pour ranger tous les composants à contrôler
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //image du livre
        val bookImage = view.findViewById<ImageView>(R.id.image_item)

        //si il ne trouve pas les composants il laisse (si ce n'est pas sur les deux layouts sinon pas d'erreur)
        val bookName: TextView? = view.findViewById(R.id.name_item)
        val bookDescription: TextView? = view.findViewById(R.id.description_item)

        val starIcon = view.findViewById<ImageView>(R.id.star_icon)

    }

    //injecter le layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //recuperer les informations du livre qui a des positions spécifique
        val currentBook = bookList[position]

        //recuperer le repository
        val repo = BookRepository()

        //utiliser glide pour recuperer l'image a partir de son lien -> composant
        Glide.with(context).load(Uri.parse(currentBook.imageUrl)).into(holder.bookImage)

        //mettre a jour le nom du livre
        holder.bookName?.text = currentBook.title

        //mettre a jour la description du livre
        holder.bookDescription?.text = currentBook.description

        //verifier si le livre est liké
        if(currentBook.liked){
            holder.starIcon.setImageResource(R.drawable.ic_like)
        }
        else{
            holder.starIcon.setImageResource(R.drawable.ic_unlike)
        }

        //rajouter une interaction sur l'étoile
        holder.starIcon.setOnClickListener{
            //inverse si le bouton est like ou non
            currentBook.liked = !currentBook.liked
            //mettre a jour l'objet livre
            repo.updateBook(currentBook)
        }

        //interaction lors du clique sur un livre
        holder.itemView.setOnClickListener{
            //afficher la popup
            BookPopup(this, currentBook).show()
        }

    }

    //genere 5 emplacements pour les images
    override fun getItemCount(): Int = bookList.size
}