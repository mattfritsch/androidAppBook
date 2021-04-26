package fr.mezza.bookcollection.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import fr.mezza.bookcollection.BookModel
import fr.mezza.bookcollection.BookRepository
import fr.mezza.bookcollection.BookRepository.Singleton.downloadUri
import fr.mezza.bookcollection.MainActivity
import fr.mezza.bookcollection.R
import java.util.*

class AddBookFragment(
    private val context: MainActivity
) : Fragment() {
    private var file: Uri? = null
    private var uploadedImage: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.fragment_add_book, container, false)

        //recuperer upload de l'image
        uploadedImage = view.findViewById(R.id.preview_image)

        //recuperer le bouton pour charger l'image
        val pickupImageButton = view.findViewById<Button>(R.id.upload_button)

        //lorqu'on clique dessus ça ouvre les images du téléphone
        pickupImageButton.setOnClickListener { pickupImage() }

        //recuperer le bouton confirmer
        val confirmButton = view.findViewById<Button>(R.id.confirm_button)
        confirmButton.setOnClickListener { sendForm(view) }

        return view
    }

    private fun sendForm(view: View) {

        val repo = BookRepository()
        //forcer le binding
        repo.uploadImage(file!!){
            val bookName = view.findViewById<EditText>(R.id.title_input).text.toString()
            val bookDescription = view.findViewById<EditText>(R.id.description_input).text.toString()
            val comics = view.findViewById<Spinner>(R.id.comics_spinner).selectedItem.toString()
            val series = view.findViewById<Spinner>(R.id.series_spinner).selectedItem.toString()
            val downloadImageUrl = downloadUri
            
            //creer un nouvel objet BookModel
            val book = BookModel(
                UUID.randomUUID().toString(), bookName, bookDescription, downloadImageUrl.toString(), comics, series
            )

            //envoyer en bdd
            repo.insertBook(book)
        }
    }

    private fun pickupImage() {
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 47)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 47 && resultCode == Activity.RESULT_OK) {
            //verifier si les données receptionnés sont nuls
            if (data == null || data.data == null) return

            //recuperer l'image selectionne
            file = data.data

            //mettre a jour l'apercu de l'image
            uploadedImage?.setImageURI(file)

        }

    }
}