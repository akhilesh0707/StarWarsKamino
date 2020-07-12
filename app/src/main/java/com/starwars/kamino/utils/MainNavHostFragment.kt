package com.starwars.kamino.utils

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment

/**
 * MainNavHostFragment :
 * Describe own NavHostFragment.
 * The main change here will be checking wether popUpTo is equal to destination.
 * And if yes, that probably means (at least in our project)
 * that you just want to go back to that fragment without recreation,
 * so you can just return navDest without changing ...
 *
 * Don’t forget to place @Navigator.Name annotation on class with name fragment
 * so that your navigator will be used as FragmentNavigator instead of default one.
 */
class MainNavHostFragment : NavHostFragment() {

    override fun createFragmentNavigator() = MainFragmentNavigator(requireContext(), childFragmentManager, id)

    @Navigator.Name("fragment")
    class MainFragmentNavigator(
        context: Context,
        fm: FragmentManager,
        containerId: Int
    ) : FragmentNavigator(context, fm, containerId) {

        override fun navigate(
            destination: Destination,
            args: Bundle?,
            navOptions: NavOptions?,
            navigatorExtras: Navigator.Extras?
        ): NavDestination? {
            val shouldSkip = navOptions?.run {popUpTo == destination.id && !isPopUpToInclusive } ?: false
            return if (shouldSkip) null
            else super.navigate(destination, args, navOptions, navigatorExtras)
        }
    }
}