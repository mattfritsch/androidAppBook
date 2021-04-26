package fr.mezza.bookcollection.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import fr.mezza.bookcollection.BookRepository.Singleton.bookList
import fr.mezza.bookcollection.MainActivity
import fr.mezza.bookcollection.R
import fr.mezza.bookcollection.adapter.BookAdapter
import fr.mezza.bookcollection.adapter.BookItemDecoration

class HomeFragment(private val context : MainActivity) : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?  ): View? {
        val view = inflater?.inflate(R.layout.fragment_home, container, false)


        //recuperer recyclerview
        val horizontalRecyclerView = view.findViewById<RecyclerView>(R.id.horizontal_recycler_view)
        horizontalRecyclerView.adapter = BookAdapter(context, bookList, R.layout.item_horizontal_book)

        //recuperer le second recycler view
        val verticalRecyclerView = view.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        verticalRecyclerView.adapter = BookAdapter(context, bookList, R.layout.item_vertical_book)
        verticalRecyclerView.addItemDecoration(BookItemDecoration())

        return view
    }

}