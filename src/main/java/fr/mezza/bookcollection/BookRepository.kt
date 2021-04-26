package fr.mezza.bookcollection

import android.net.Uri
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import fr.mezza.bookcollection.BookRepository.Singleton.bookList
import fr.mezza.bookcollection.BookRepository.Singleton.databaseRef
import fr.mezza.bookcollection.BookRepository.Singleton.downloadUri
import fr.mezza.bookcollection.BookRepository.Singleton.storageReference
import java.util.*

class BookRepository {

    object Singleton{
        //donner le lien pour acceder au bucket
        private val BUCKET_URL: String = "gs://bookcollection-66507.appspot.com"

        //se connecter à notre espace de stockage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)

        // se connecter à la référence "books"
        val databaseRef = FirebaseDatabase.getInstance().getReference("books")

        // créer une liste qui va contenir nos livres
        val bookList = arrayListOf<BookModel>()

        //contenir le lien de l'image
        var downloadUri: Uri? = null
    }

    fun updateData(callback: () -> Unit){
        // absorber les données depuis la databaseRef -> liste de livre
        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //retirer les anciennes
                bookList.clear()
                // recolter la liste
                for (ds in snapshot.children){
                    //construire un objet livre
                    val book = ds.getValue(BookModel::class.java)

                    //verifier que le livre n'est pas null
                    if (book != null){
                        //ajouter le livre à la liste
                        bookList.add(book)
                    }
                }
                // actionner le callback
                callback()
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    //creer une fonction pour envoyer des fichiers sur le storage
    fun uploadImage(file: Uri, callback: () -> Unit){
        //verifier que ce fichier n'est pas null
        if (file != null){
            val fileName = UUID.randomUUID().toString() + ".jpg"
            val ref = storageReference.child(fileName)
            val uploadTask = ref.putFile(file)

            //demarrer la tache d'envoie
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task <Uri>>{ task ->
                //si il y a un pb lors de l'envoie du fichier
                if (!task.isSuccessful){
                    task.exception?.let{ throw it }
                }
                return@Continuation ref.downloadUrl
            }).addOnCompleteListener{ task ->
                //verifier si tout a bien fonctionné
                if(task.isSuccessful){
                    //recuperer l'image
                    downloadUri = task.result
                    callback()
                }
            }
        }
    }

    //mettre a jour objet livre en bdd
    fun updateBook(book: BookModel){
        databaseRef.child(book.id).setValue(book)
    }
    //inserer une nouvelle plante
    fun insertBook(book:BookModel) = databaseRef.child(book.id).setValue(book)

    //supprimer un livre de la base
    fun deleteBook(book: BookModel) = databaseRef.child(book.id).removeValue()
}