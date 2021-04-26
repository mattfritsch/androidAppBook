package fr.mezza.bookcollection

import android.app.Dialog
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import fr.mezza.bookcollection.adapter.BookAdapter

class BookPopup(private val adapter : BookAdapter,
    private val currentBook: BookModel)
    : Dialog(adapter.context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //on ne veut pas de titre sur la popup
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_book_details)
        setupComponent()
        setupCloseButton()
        setupDeleteButton()
        setupStarButton()
    }

    private fun updateStar(button: ImageView){
        if (currentBook.liked){
            button.setImageResource(R.drawable.ic_like)
        }
        else{
            button.setImageResource(R.drawable.ic_unlike)
        }
    }
    private fun setupStarButton() {
        //recuperer
        val starButton = findViewById<ImageView>(R.id.star_button)
        updateStar(starButton)


        //interaction
        starButton.setOnClickListener {
            currentBook.liked = !currentBook.liked
            val repo = BookRepository()
            repo.updateBook(currentBook)
            updateStar(starButton)
        }
    }

    private fun setupDeleteButton() {
        findViewById<ImageView>(R.id.delete_button).setOnClickListener {
            //supprimer le livre selectionner
            val repo = BookRepository()
            repo.deleteBook(currentBook)
            dismiss()
        }
    }

    private fun setupCloseButton() {
        findViewById<ImageView>(R.id.close_button).setOnClickListener {
            //fermer la fenetre popup
            dismiss()
        }
    }

    private fun setupComponent() {
        //actualiser l'image du livre
        val bookImage = findViewById<ImageView>(R.id.image_item)
        Glide.with(adapter.context).load(Uri.parse(currentBook.imageUrl)).into(bookImage)

        //actualiser le titre du livre
        findViewById<TextView>(R.id.popup_book_name).text = currentBook.title

        //actualiser la description du livre
        findViewById<TextView>(R.id.popup_book_description_subtitle).text = currentBook.description

        //actualiser le comics du livre
        findViewById<TextView>(R.id.popup_book_comics_subtitle).text = currentBook.comics

        //actualiser la series du livre
        findViewById<TextView>(R.id.popup_book_series_subtitle).text = currentBook.series
    }

}