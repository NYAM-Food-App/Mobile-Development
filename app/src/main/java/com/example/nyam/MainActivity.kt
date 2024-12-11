package com.example.nyam

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable

class MainActivity : BaseClass(true) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val navView : BottomNavigationView = findViewById(R.id.nav_view)
        val radius = resources.getDimension(R.dimen.cornerSize)

        //Rounded NavView
        val shapeDrawable : MaterialShapeDrawable = navView.background as MaterialShapeDrawable
        shapeDrawable.shapeAppearanceModel = shapeDrawable.shapeAppearanceModel
            .toBuilder()
//            .setAllCorners(CornerFamily.ROUNDED,radius)
            .setTopLeftCorner(CornerFamily.ROUNDED,radius)
            .setTopRightCorner(CornerFamily.ROUNDED,radius)
            .build()

        val navController = findNavController(R.id.nav_host_fragment_activity_navigation)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_camera,
                R.id.navigation_menu,
                R.id.navigation_history
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        setActionBar()

    }
}