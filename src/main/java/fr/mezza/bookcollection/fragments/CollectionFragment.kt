package fr.mezza.bookcollection.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.mezza.bookcollection.BookRepository.Singleton.bookList
import fr.mezza.bookcollection.MainActivity
import fr.mezza.bookcollection.R
import fr.mezza.bookcollection.adapter.BookAdapter
import fr.mezza.bookcollection.adapter.BookItemDecoration

class CollectionFragment(
    private val context: MainActivity
) : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.fragment_collection, container, false)

        //recuperer la recycler view
        val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.collection_recycler_list)
        collectionRecyclerView.adapter = BookAdapter(context, bookList, R.layout.item_vertical_book)
        collectionRecyclerView.layoutManager = LinearLayoutManager(context)
        collectionRecyclerView.addItemDecoration(BookItemDecoration())
        return view
    }

}