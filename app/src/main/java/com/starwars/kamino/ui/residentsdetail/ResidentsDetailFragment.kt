package com.starwars.kamino.ui.residentsdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.starwars.kamino.R
import com.starwars.kamino.base.BaseFragment
import com.starwars.kamino.ui.residents.model.ResidentModel
import com.starwars.kamino.utils.bindViewModel
import kotlinx.android.synthetic.main.fragment_residents_detail.*
import javax.inject.Inject

class ResidentsDetailFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var requestManager: RequestManager
    private val viewModel by bindViewModel<ResidentsDetailViewModel>(lazy { viewModelFactory })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_residents_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val resident: ResidentModel? = arguments?.getParcelable(RESIDENT_ARG_KEY) as? ResidentModel
        resident?.let {
            bindResidentDetail(resident)
        }
    }

    /**
     * Bind resident all information to UI (profile pic, name, gender, etc...)
     * @param resident : Resident information
     */
    private fun bindResidentDetail(resident: ResidentModel) {
        // Load resident profile image
        requestManager.load(resident.imageUrl).into(imageResident)

        // Set all resident detail
        textResidentName.text = getString(R.string.text_resident_name, resident.name)
        textResidentGender.text = getString(R.string.text_resident_gender, resident.gender)
        textResidentBirthYear.text =
            getString(R.string.text_resident_birth_year, resident.birthYear)
        textResidentHeight.text = getString(R.string.text_resident_height, resident.height)
        textResidentMass.text = getString(R.string.text_resident_mass, resident.mass)
        textResidentHairColor.text =
            getString(R.string.text_resident_hair_color, resident.hairColor)
        textResidentSkinColor.text =
            getString(R.string.text_resident_skin_color, resident.skinColor)
        textResidentEyeColor.text = getString(R.string.text_resident_eye_color, resident.eyeColor)
    }

    companion object {
        private const val RESIDENT_ARG_KEY = "resident"
    }

}