package com.fullsekurity.urbandict.utils

import androidx.lifecycle.ViewModelProvider
import com.fullsekurity.urbandict.activity.MainActivity
import com.fullsekurity.urbandict.city.CityAdapter
import com.fullsekurity.urbandict.city.CityFragment
import com.fullsekurity.urbandict.city.CityListViewModel
import com.fullsekurity.urbandict.modal.StandardModal
import com.fullsekurity.urbandict.repository.Repository
import com.fullsekurity.urbandict.ui.UIViewModel
import com.fullsekurity.urbandict.ui.UIViewModelFactory
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [MapperInjectorModule::class])
interface MapperDependencyInjector {
    fun inject(viewModel: UIViewModel)
}

@Singleton
@Component(modules = [ViewModelInjectorModule::class])
interface ViewModelDependencyInjector {
    fun inject(fragment: CityFragment)
    fun inject(modal: StandardModal)
    fun inject(viewModel: CityListViewModel)
    fun inject(activity: MainActivity)
    fun inject(adapter: CityAdapter)
}

@Module
class MapperInjectorModule {
    @Provides
    @Singleton
    fun colorMapperProvider() : ColorMapper {
        val colorMapper = ColorMapper()
        colorMapper.initialize()
        return colorMapper
    }
    @Provides
    @Singleton
    fun textSizeMapperProvider() : TextSizeMapper {
        val textSizeMapper = TextSizeMapper()
        textSizeMapper.initialize()
        return textSizeMapper
    }
    @Provides
    @Singleton
    fun typefaceMapperProvider() : TypefaceMapper {
        val typefaceMapper = TypefaceMapper()
        typefaceMapper.initialize()
        return typefaceMapper
    }
}

@Module
class ViewModelInjectorModule(val activity: MainActivity) {
    @Provides
    @Singleton
    fun uiViewModelProvider() : UIViewModel {
        return ViewModelProvider(activity, UIViewModelFactory(activity.application)).get(UIViewModel::class.java)
    }
    @Provides
    @Singleton
    fun repositoryProvider() : Repository {
        return activity.repository
    }

}