package com.chwitkey.cherry_pick_android.presentation.ui.mypage

import android.app.AlertDialog
import android.content.Intent
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.chwitkey.cherry_pick_android.R
import com.chwitkey.cherry_pick_android.data.remote.service.user.UserInfoService
import com.chwitkey.cherry_pick_android.databinding.FragmentMypageBinding
import com.chwitkey.cherry_pick_android.domain.repository.UserDataRepository
import com.chwitkey.cherry_pick_android.presentation.ui.login.LoginActivity
import com.chwitkey.cherry_pick_android.presentation.viewmodel.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyPageFragment : Fragment() {
    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var userDataRepository: UserDataRepository
    @Inject
    lateinit var userInfoService: UserInfoService

    companion object{
        const val TAG = "MyPageFragment"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        val view = binding.root

        // 유저네임 변경 감지
        userDataRepository.getNameLiveData().observe(requireActivity(), Observer {
            binding.tvMypageUserName.text = it.toString()
        })
        loadUserInfo()

        setButton()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun setButton() {
        binding.apply {
            // 다이얼로그로 로그아웃 기능 구현
            ibtnMypageLogout.setOnClickListener {
                dialogShow()
            }
            // 계정설정 (프로필) 페이지로 이동
            ibtnMypageSetting.setOnClickListener {
                activity?.let {
                    val intent = Intent(it, ProfileActivity::class.java)
                    it.startActivity(intent)
                }
            }
            // 스토어 별점 남기기 이동
            ibtnMoveToScore.setOnClickListener {
                // val intent = Intent(Intent.ACTION_VIEW)
                // intent.data = Uri.parse("market://details?id=$packageName")
                // requireContext().startActivity(intent)
            }
            // 이용약관 및 사용자 정책 페이지 이동
            ibtnMoveToPolicy.setOnClickListener {
                Log.d(TAG,"이용약관 페이지 이동")
                activity?.let {
                    val intent = Intent(it, UserPolicy::class.java)
                    it.startActivity(intent)
                }
            }
            // 마케팅수신동의 페이지 이동
            tvMypageMarketingPolicy.setOnClickListener {
                Log.d(TAG,"마케팅 페이지 이동")
                activity?.let {
                    val intent = Intent(it, MarketingPolicy::class.java)
                    it.startActivity(intent)
                }
            }
            // 개인정보처리방침 페이지 이동
            tvMypagePrivacyPolicy.setOnClickListener {
                activity?.let {
                    Log.d(TAG,"개인정보 페이지 이동")
                    val intent = Intent(it, PrivacyPolicy::class.java)
                    it.startActivity(intent)
                }
            }
        }
    }

    private fun dialogShow(){
        AlertDialog.Builder(context)
            .setTitle("알림")
            .setMessage("로그아웃 하시겠습니까?")
            .setPositiveButton("로그아웃"){_,_->
                loginViewModel.setUserData("userId", "")
                loginViewModel.setUserData("platform", "")
                loginViewModel.setIsOutView("out")

                activity?.let {
                    val intent = Intent(it, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK // 백스택에 남아있는 액티비티 제거
                    }
                    it.startActivity(intent)
                }
            }
            .setNegativeButton("취소"){_, _->}
            .create()
            .show()
    }

    private fun loadUserInfo(){
        CoroutineScope(Dispatchers.Main).launch {
            val response = userInfoService.getUserInfo().body()
            val statusCode = response?.statusCode

            if(statusCode == 200){
                val imageResponse = response?.data?.memberImgUrl
                binding.tvMypageUserName.text = response.data?.name
                if(imageResponse!=null) {
                    Glide.with(requireActivity().applicationContext).load(imageResponse).circleCrop()
                        .into(binding.ivProfilePic)
                }else{
                    binding.ivProfilePic.setImageResource(R.drawable.ic_my_page_user)
                }
            }else{
                Toast.makeText(requireContext(), "통신 오류:$statusCode", Toast.LENGTH_SHORT).show()
            }
            binding.lottieDotLoading.visibility = View.GONE

        }
    }
}