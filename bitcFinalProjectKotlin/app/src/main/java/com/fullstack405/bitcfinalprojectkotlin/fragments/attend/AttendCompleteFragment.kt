package com.fullstack405.bitcfinalprojectkotlin.fragments.attend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.fullstack405.bitcfinalprojectkotlin.adapter.AttendAllAdapter
import com.fullstack405.bitcfinalprojectkotlin.client.Client
import com.fullstack405.bitcfinalprojectkotlin.data.EventAppData
import com.fullstack405.bitcfinalprojectkotlin.databinding.FragmentAttendCompleteBinding
import retrofit2.Call
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AttendCompleteFragment : Fragment() {
    private lateinit var binding: FragmentAttendCompleteBinding
    private lateinit var attendAllAdapter: AttendAllAdapter

    private lateinit var compList:MutableList<EventAppData>

    var userId = 0L
    var userName = "none"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAttendCompleteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // activity에서 userId 추출
        userId = activity?.intent!!.getLongExtra("userId", 0)
        userName = activity?.intent!!.getStringExtra("userName")!!

        // 수료 내역 데이터
        compList= mutableListOf<EventAppData>()

        // 초기 셋팅
        findMyCompleteApplicationList()

    }// onViewCreated

    override fun onResume() {
        super.onResume()
        findMyCompleteApplicationList()
    }

    private fun findMyCompleteApplicationList(){
        Client.retrofit.findMyCompleteApplicationList(userId).enqueue(object:retrofit2.Callback<List<EventAppData>>{
            override fun onResponse(call: Call<List<EventAppData>>, response: Response<List<EventAppData>>) {
                compList = response.body() as MutableList<EventAppData>

                attendAllAdapter = AttendAllAdapter(compList,userId,userName)
                binding.recyclerViewComp.adapter = attendAllAdapter
                binding.recyclerViewComp.layoutManager = LinearLayoutManager(requireContext())

                attendAllAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<EventAppData>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}