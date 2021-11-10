package nl.hva.madlevel7task1

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import nl.hva.madlevel7task1.databinding.FragmentCreateProfileBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CreateProfileFragment : Fragment() {

    private var _binding: FragmentCreateProfileBinding? = null
    private val binding get() = _binding!!
    private var profileImageUri: Uri? = null
    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCreateProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGallery.setOnClickListener { onGalleryClick() }
        binding.btnConfirm.setOnClickListener { onConfirmClick() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    profileImageUri = data?.data
                    binding.ivProfileImage.setImageURI(profileImageUri)
                }
            }
        }
    }

    private fun CharSequence?.ifNullOrEmpty(default: String) =
        if (this.isNullOrEmpty()) default else this.toString()

    private fun Uri?.ifNullOrEmpty() =
        this?.toString() ?: ""

    private fun onConfirmClick() {
        viewModel.createProfile(
            binding.etFirstName.text.ifNullOrEmpty(""),
            binding.etLastName.text.ifNullOrEmpty(""),
            binding.etProfileDescription.text.ifNullOrEmpty(""),
            profileImageUri.ifNullOrEmpty()
        )
        observeProfileCreation()
        findNavController().navigate(R.id.ProfileFragment)
    }

    private fun observeProfileCreation() {
        viewModel.createSuccess.observe(viewLifecycleOwner, {
            Toast.makeText(activity, R.string.successfully_created_profile, Toast.LENGTH_LONG)
                .show()
            findNavController().popBackStack()
        })

        viewModel.errorText.observe(viewLifecycleOwner, {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun onGalleryClick() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

    companion object {
        const val GALLERY_REQUEST_CODE = 100
    }
}