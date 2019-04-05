package cof.gof.dagger2structure.MainActivity.di;

import cof.gof.dagger2structure.mainActivity.MainActivity;
import cof.gof.dagger2structure.shared.User;
import cof.gof.dagger2structure.testModules.FlombulatorFakeModule;
import cof.gof.dagger2structure.testModules.UserFakeModule;
import dagger.BindsInstance;
import dagger.Module;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent(modules = {
        FlombulatorFakeModule.class,
        UserFakeModule.class,
        TestMainActivitySubComponent.MainActivityModule.class
})
public interface TestMainActivitySubComponent extends AndroidInjector<MainActivity> {

    @Module
    class MainActivityModule {}

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainActivity> {
        public abstract void setUserModule(UserFakeModule module);

        @Override
        public void seedInstance(MainActivity instance) {
            User user = new User(instance.getNameForUser());
            setUserModule(new UserFakeModule(user));
            bindMainActivity(instance);
        }
        @BindsInstance
        public abstract void bindMainActivity(MainActivity mainActivity);
    }
}
