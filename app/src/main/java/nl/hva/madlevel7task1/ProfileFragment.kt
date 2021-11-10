package nl.hva.madlevel7task1

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import nl.hva.madlevel7task1.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeProfile()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeProfile() {
        viewModel.getProfile()
        viewModel.profile.observe(viewLifecycleOwner, {
            val profile = it
            binding.tvName.text =
                getString(R.string.profile_name, profile.firstName, profile.lastName)
            binding.tvDescription.text = profile.description
            if (profile.imageUri.isNotEmpty()) {
                binding.ivProfileImage.setImageURI(Uri.parse(profile.imageUri))
            }
        })
    }
}