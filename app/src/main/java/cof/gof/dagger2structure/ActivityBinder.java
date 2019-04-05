package cof.gof.dagger2structure;

import cof.gof.dagger2structure.ThirdActivity.ThirdActivity;
import cof.gof.dagger2structure.diModules.FlombulatorModule;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBinder {

    @ContributesAndroidInjector(modules = FlombulatorModule.class)
    abstract ThirdActivity bindThirdActiivity();
}
