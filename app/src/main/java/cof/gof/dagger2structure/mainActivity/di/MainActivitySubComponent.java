package cof.gof.dagger2structure.mainActivity.di;

import cof.gof.dagger2structure.diModules.FlombulatorModule;
import cof.gof.dagger2structure.diModules.UserModule;
import cof.gof.dagger2structure.mainActivity.MainActivity;
import cof.gof.dagger2structure.shared.User;
import dagger.BindsInstance;
import dagger.Module;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent(modules = {
        FlombulatorModule.class,
        UserModule.class,
        MainActivitySubComponent.MainActivityModule.class
})
public interface MainActivitySubComponent extends AndroidInjector<MainActivity> {

    @Module
    class MainActivityModule {}

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainActivity> {

        public abstract void setUserModule(UserModule module);

        @Override
        public void seedInstance(MainActivity instance) {
            User user = new User(instance.getNameForUser());
            setUserModule(new UserModule(user));
            bindMainActivity(instance);
        }

        @BindsInstance
        public abstract void bindMainActivity(MainActivity mainActivity);
    }
}
