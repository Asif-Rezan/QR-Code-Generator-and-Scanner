package com.asifrezan.qrcodegenerator

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asifrezan.qrcodegenerator.activities.EditActivity
import com.asifrezan.qrcodegenerator.adapters.HomeAdapter
import com.asifrezan.qrcodegenerator.databinding.FragmentHomeFregmentBinding
import com.asifrezan.qrcodegenerator.db.DBHelper

class HomeFregment : Fragment() {

    private lateinit var binding: FragmentHomeFregmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home_fregment,
            container,
            false
        )


        val dbHelper = DBHelper(requireContext())
        var dataList = dbHelper.getData()
        var adapter = HomeAdapter(dataList)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        if (binding.recyclerView.adapter?.itemCount == 0) {
            binding.emptyViewButton.visibility = View.VISIBLE
        } else {
            binding.emptyViewButton.visibility = View.GONE
        }



        adapter.setOnItemClickListener(object : HomeAdapter.OnItemClickListener {

            override fun onItemClick(position: Int) {
                val clickedItem = dataList[position]
                val intent = Intent(requireContext(), EditActivity::class.java)
                intent.putExtra("id", clickedItem.id)
                startActivity(intent)
            }

        })


        binding.emptyViewButton.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.todoFragment)
        })




        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                adapter.notifyItemChanged(viewHolder.adapterPosition)
                val position = viewHolder.adapterPosition
                val clickedItem = dataList[position]
                val lastItem = dataList.last()
                dataList = dataList.toMutableList().apply {
                    set(position, lastItem)
                    removeLast()
                }

                dbHelper.deleteData(clickedItem.id)
                adapter.setData(dataList)
                if (binding.recyclerView.adapter?.itemCount == 0) {
                    binding.emptyViewButton.visibility = View.VISIBLE
                } else {
                    binding.emptyViewButton.visibility = View.GONE
                }
                adapter.notifyItemRangeRemoved(position, 1)
            }




            // Override onChildDraw to change background color during swipe
//            override fun onChildDraw(
//                c: Canvas,
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                dX: Float,
//                dY: Float,
//                actionState: Int,
//                isCurrentlyActive: Boolean
//            ) {
//                val itemView = viewHolder.itemView
//                val background = ColorDrawable(Color.RED) // Replace with desired background color
//
//                // Add padding to the view
//                val padding = 100 // Replace with desired padding value
//                itemView.setPadding(padding, padding, padding, padding)
//
//                // Set background bounds based on swipe direction
//                if (dX > 0) {
//                    background.setBounds(
//                        itemView.left,
//                        itemView.top,
//                        itemView.left + dX.toInt(),
//                        itemView.bottom
//                    )
//                } else {
//                    background.setBounds(
//                        itemView.right + dX.toInt(),
//                        itemView.top,
//                        itemView.right,
//                        itemView.bottom
//                    )
//                }
//
//                background.draw(c)
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)


















//        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
//            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
//        ) {
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder
//            ): Boolean {
//                return false
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val position = viewHolder.adapterPosition
//                val clickedItem = dataList[position]
//                val lastItem = dataList.last()
//                dataList = dataList.toMutableList().apply {
//                    set(position, lastItem)
//                    removeLast()
//                }
//                dbHelper.deleteData(clickedItem.id)
//                adapter.setData(dataList)
//                adapter.notifyItemRangeRemoved(position, 1)
//            }
//        }
//        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
//        itemTouchHelper.attachToRecyclerView(binding.recyclerView)










//        adapter.setOnDeleteListener(object : HomeAdapter.OnDeleteClickListener {
//            override fun onDeleteClick(position: Int) {
//                val clickedItem = dataList[position]
//                dbHelper.deleteData(clickedItem.id)
//                adapter.notifyItemRangeRemoved(position, 1)
//                adapter.setData(dataList)
//
////                val clickedItem = dataList[position]
////                dbHelper.deleteData(clickedItem.id)
////                dataList = dbHelper.getData()
////                adapter.notifyItemRangeRemoved(position, 1)
//            }
//        })







//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
//            showExitDialog()
//        }

        return binding.root
    }

    private fun showExitDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("Are you sure you want to exit?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id -> requireActivity().finish() }
            .setNegativeButton("No", null)
            .show()
    }





//    override fun onResume() {
//        super.onResume()
//
//        val dbHelper = DBHelper(requireContext())
//        val dataList = dbHelper.getData()
//        val adapter = HomeAdapter(dataList)
//        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        binding.recyclerView.adapter = adapter
//        adapter.setOnItemClickListener(object : HomeAdapter.OnItemClickListener {
//            override fun onItemClick(position: Int) {
//                val clickedItem = dataList[position]
//                val intent = Intent(requireContext(), EditActivity::class.java)
//                intent.putExtra("id", clickedItem.id)
//                startActivity(intent)
//            }
//        })
//    }







}